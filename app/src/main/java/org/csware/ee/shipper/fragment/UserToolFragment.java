package org.csware.ee.shipper.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.BindBankCardActivity;
import org.csware.ee.shipper.FreightFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserPayToDriverActivity;
import org.csware.ee.widget.zxing.CaptureActivity;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserToolFragment extends FragmentBase {


    @InjectView(R.id.btnFreight)
    LinearLayout btnFreight;
    @InjectView(R.id.btnScanCodePay)
    LinearLayout btnScanCodePay;
    BindStatusInfo bankinfo;
    Shipper shipper;
    DbCache dbCache;
    @InjectView(R.id.btnInitJpush)
    Button btnInitJpush;
    @InjectView(R.id.btnGetJpushId)
    Button btnGetJpushId;
    @InjectView(R.id.btnResumePush)
    Button btnResumePush;

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_tool_fragment;
    }

    @Override
    public void init() {
        dbCache = new DbCache(baseActivity);
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
    }


    @OnClick({R.id.btnFreight})
    void opFreight(View v) {
        Intent intent = new Intent(baseActivity, FreightFragmentActivity.class);
        //intent.putExtra(ParamKey.BACK_TITLE, "设置");
        baseActivity.startActivity(intent);

    }

    @OnClick(R.id.btnScanCodePay)
    void setBtnScanCodePay() {
        Intent intent = new Intent(baseActivity, CaptureActivity.class);
        startActivityForResult(intent, 0);
    }

    @OnClick(R.id.btnInitJpush)
    void setBtnInitJpush() {
        JPushInterface.init(baseActivity.getApplicationContext());
        toastFast("init");
    }

    @OnClick(R.id.btnGetJpushId)
    void setBtnGetJpushId() {
        String JpushId = JPushInterface.getRegistrationID(baseActivity.getApplicationContext());
        toastFast(JpushId);
    }

    @OnClick(R.id.btnResumePush)
    void setBtnResumePush() {
        JPushInterface.resumePush(baseActivity.getApplicationContext());
        toastFast("resumePush");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
        if (resultCode == getActivity().RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanRes = (String) bundle.get("result");
            //ErrorCode:　80036　请下载小象快运（货主端）进行扫码
            if (scanRes.contains("　")) {
                final String ScanCode = scanRes.substring(scanRes.indexOf("　") + 1, scanRes.lastIndexOf("　"));
                if (ScanCode.length() > 0) {
                    HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
                    BackcardApi.bindStatus(baseFragment.getActivity(), baseActivity, pms, new IJsonResult<BindStatusInfo>() {
                        @Override
                        public void ok(BindStatusInfo json) {
                            if (Guard.isNull(json.PnrInfo)) {
                                shipper.isFirstCg = true;
                            } else {
                                shipper.isFirstCg = false;
                            }
                            dbCache.save(shipper);
                            if (!Guard.isNull(json.PnrInfo)) {

                                if (!(json.PnrCards.size() > 0)) {
                                    Intent intent = new Intent(baseActivity, BindBankCardActivity.class);
                                    intent.putExtra("action", "scan");
                                    intent.putExtra("mode", "pay");
                                    intent.putExtra("payeeid", ScanCode);
                                    startActivity(intent);
                                } else {
                                    bankinfo = json;
                                    dbCache.save(bankinfo);
                                    if (!shipper.havedPayMethod && shipper.cardid < 0) {
                                        String number = "", cardNum = bankinfo.PnrCards.get(0).CardNo;
                                        if (cardNum.length() > 4) {
                                            number = cardNum.substring(cardNum.length() - 4, cardNum.length());
                                        }
                                        String bankName = "";
                                        for (int i = 0; i < BankInfoService.getBankData().size(); i++) {
                                            if (BankInfoService.getBankData().get(i).getBankcode().equals(json.PnrCards.get(0).BankCode)) {
                                                bankName = BankInfoService.getBankData().get(i).getBankName();
                                            }
                                        }
                                        String payment = "使用\t\t" + bankName + "(" + number + ")\t\t" + "付款";
                                        shipper.payment = payment;
                                        shipper.cardid = bankinfo.PnrCards.get(0).Id;
                                        dbCache.save(shipper);
                                    }
                                    Intent intent = new Intent(baseActivity, UserPayToDriverActivity.class);
                                    intent.putExtra("action", "scan");
                                    intent.putExtra("payeeid", ScanCode);
                                    startActivity(intent);
                                }
                            } else {
                                Intent intent = new Intent(baseActivity, BindBankCardActivity.class);
                                intent.putExtra("action", "scan");
                                intent.putExtra("mode", "pay");
                                intent.putExtra("payeeid", ScanCode);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void error(Return json) {
                        }
                    });
//                    Intent intent = new Intent(baseActivity, UserPayToBankActivity.class);
//                    intent.putExtra("action","scan");
//                    intent.putExtra("payeeid",ScanCode);
//                    startActivity(intent);
                }
            } else {
                toastFast(scanRes);
            }
//            resultTextView.setText(scanRes);
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
