package org.csware.ee.shipper;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.DialogUtil;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ScrollViewForListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

import static org.csware.ee.shipper.UserPayToPlatformFragmentActivity.getAllApps;

/**
 * Created by Administrator on 2016/2/25.
 */
public class UserPayMethodActivity extends ActivityBase {

    private static final String TAG = "UserPayMethodActivity";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.listVMethod)
    ScrollViewForListView listVMethod;
    @InjectView(R.id.btnAddBankCard)
    LinearLayout btnAddBankCard;
    @InjectView(R.id.boxAlipay)
    LinearLayout boxAlipay;
    @InjectView(R.id.boxWX)
    LinearLayout boxWX;

    CommonAdapter<BankInfo> adapter;
    List<BankInfo> bankList = new ArrayList<>();
    List<BankInfo> adapterList = new ArrayList<>();
    DbCache dbCache;
    String action;

    MeReturn userInfo;
    AccountReturn ac;
    @InjectView(R.id.imgBalanceIcon)
    ImageView imgBalanceIcon;
    @InjectView(R.id.txtBalance)
    TextView txtBalance;
    @InjectView(R.id.ivBalancePay)
    ImageView ivBalancePay;
    @InjectView(R.id.boxBalance)
    LinearLayout boxBalance;
    double payPrice = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_method);
        ButterKnife.inject(this);

        payPrice = getIntent().getDoubleExtra("payPrice",0.0);
        dbCache = new DbCache(baseActivity);
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
        topBar.setTitle("选择付款方式");
        bankList = BankInfoService.getBankData();
        alertDialog = new AlertDialog.Builder(baseActivity).create();
        ivBalancePay.setVisibility(View.INVISIBLE);

        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
        int status = -1;
        if (!Guard.isNull(userInfo.OwnerUser)) {
            if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus) && !Guard.isNull(userInfo.OwnerUser.Status)) {
                status = userInfo.OwnerUser.CompanyStatus > userInfo.OwnerUser.Status ? userInfo.OwnerUser.CompanyStatus : userInfo.OwnerUser.Status;
            }
        }

        if (status < 1) {
            btnAddBankCard.setVisibility(View.GONE);
            listVMethod.setVisibility(View.GONE);
            boxBalance.setVisibility(View.GONE);
        }

        getBindStatus();
        asyncLoadAccount();

    }

    ProgressDialog dialog;
    BindStatusInfo bindStatusInfo;
    Shipper shipper;
    double balance = 0.0;
    boolean isPay = false;

    void getBindStatus() {
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        //TODO:获取绑卡状态
        HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
        BackcardApi.bindStatus(baseActivity, this, pms, new IJsonResult<BindStatusInfo>() {
            @Override
            public void ok(BindStatusInfo json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                bindStatusInfo = json;
                dbCache.save(bindStatusInfo);
                if (Guard.isNull(bindStatusInfo.PnrInfo)) {
                    shipper.isFirstCg = true;
                } else {
                    shipper.isFirstCg = false;
                }
                dbCache.save(shipper);
                if (!Guard.isNull(bindStatusInfo)) {
                    if (bindStatusInfo.PnrCards.size() > 0) {
                        for (int i = 0; i < bindStatusInfo.PnrCards.size(); i++) {
                            for (int j = 0; j < bankList.size(); j++) {
                                if (bindStatusInfo.PnrCards.get(i).BankCode.equals(bankList.get(j).getBankcode())) {
                                    BankInfo bankInfo = new BankInfo();
                                    bankInfo.setBankName(bankList.get(j).getBankName());
                                    bankInfo.setBankcode(bindStatusInfo.PnrCards.get(i).BankCode);
                                    bankInfo.setCardNo(bindStatusInfo.PnrCards.get(i).CardNo);
                                    bankInfo.setId(bindStatusInfo.PnrCards.get(i).Id);
                                    bankInfo.setBankResId(bankList.get(j).getBankResId());
                                    bankInfo.setDayLimit(bankList.get(j).getDayLimit());
                                    bankInfo.setSingleLimit(bankList.get(j).getSingleLimit());
                                    adapterList.add(bankInfo);
                                }
                            }
                        }
                    }
                }
                adapter = new CommonAdapter<BankInfo>(baseActivity, adapterList, R.layout.list_item_bank_select) {
                    @Override
                    public void convert(ViewHolder helper, BankInfo item, final int position) {
                        helper.setText(R.id.labName, item.getBankName());
                        helper.setText(R.id.labCarNo, item.getCardNo() + "");
                        //TODO 银行图标设置待定
                        helper.setImageResource(R.id.ivIcon, item.getBankResId());
                        helper.getView(R.id.txtMailBtn).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //TODO 详情点击跳转
                                Intent intent = new Intent(baseActivity, BankCardInfoActivity.class);
                                intent.putExtra("bankInfo", adapterList.get(position));
                                startActivityForResult(intent, 100);
                            }
                        });
                    }
                };
                listVMethod.setAdapter(adapter);
                listVMethod.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //TODO 选中返回数据进行操作
                        Shipper shipper = dbCache.GetObject(Shipper.class);
                        shipper.havedPayMethod = true;
                        String number = "", cardNum = adapterList.get(position).getCardNo();
                        Tracer.e("userPayMethod", cardNum);
                        if (cardNum.length() > 4) {
                            number = cardNum.substring(cardNum.length() - 4, cardNum.length());
                        }
                        String payment = "使用\t\t" + adapterList.get(position).getBankName() + "(" + number + ")\t\t" + "付款";
                        shipper.payment = payment;
                        int cardId = adapterList.get(position).getId();
                        Tracer.e("userPayMethod",cardId+"");
                        shipper.cardid = cardId;
                        dbCache.save(shipper);
                        Intent intent = new Intent();
                        intent.putExtra("payment", payment);
                        intent.putExtra("cardid", cardId);
                        setResult(Code.OK.toValue(), intent);
                        finish();
                    }
                });
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });
    }
    final Handler myHandler = new Handler();
    void asyncLoadAccount() {
        BackcardApi.account(baseActivity, baseActivity, new IJsonResult<AccountReturn>() {
            @Override
            public void ok(AccountReturn json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                //成功
                ac = json;
                dbCache.save(json);

                myHandler.post(new Runnable() {
                    public void run() {
                        ac = dbCache.GetObject(AccountReturn.class);
                        if (ac == null) {
                            ac = new AccountReturn();
                        }

                        if (!Guard.isNull(ac)) {
                            if (!Guard.isNull(ac.Account)) {
                                if (!Guard.isNull(ac.Account.Amount)) {
                                    if (ac.Account.Amount > 0) {
                                        balance = Double.valueOf(FormatHelper.toMoney(ac.Account.Amount));
                                    }
                                }
                            }
                        }
                        if (!Guard.isNull(payPrice)) {
                            if (payPrice > balance) {
                                imgBalanceIcon.setImageResource(R.mipmap.fkm_icon_wallet_unselected);
                                txtBalance.setText(balance + "元(余额不足)");
                                ivBalancePay.setVisibility(View.INVISIBLE);
                                isPay = false;
                            } else {
                                isPay = true;
                                txtBalance.setText(balance + "元");
                            }
                        }
                    }
                });
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.boxBalance)
    void setBoxBalance() {
        //TODO 选中返回数据进行操作
        if (balance>payPrice) {
            Shipper shipper = dbCache.GetObject(Shipper.class);
            shipper.payment = "使用\t\t余额\t\t付款";
            shipper.havedPayMethod = true;
            dbCache.save(shipper);
            Intent intent = new Intent();
            intent.putExtra("payment", shipper.payment);
            setResult(Code.OK.toValue(), intent);
            finish();
        }else {
//            toastFast("余额不足,请更换付款方式");
        }
    }
    @OnClick(R.id.boxAlipay)
    void setBoxAlipay() {
        //TODO 选中返回数据进行操作
        Shipper shipper = dbCache.GetObject(Shipper.class);
        shipper.payment = "使用\t\t支付宝\t\t付款";
        shipper.havedPayMethod = true;
        dbCache.save(shipper);
        Intent intent = new Intent();
        intent.putExtra("payment", shipper.payment);
        setResult(Code.OK.toValue(), intent);
        finish();
    }

    public boolean isWXAppInstalledAndSupported() {
        List<PackageInfo> apps = getAllApps(baseActivity);
        boolean sIsWXAppInstalledAndSupported = false;
        for (int i = 0; i < apps.size(); i++) {
            if (apps.get(i).packageName.equals("com.tencent.mm")) {
                sIsWXAppInstalledAndSupported = true;
                return sIsWXAppInstalledAndSupported;
            }
        }
        Tracer.e(TAG, "是否安装微信" + sIsWXAppInstalledAndSupported + "");
        return sIsWXAppInstalledAndSupported;
    }


    @OnClick(R.id.boxWX)
    void setBoxWX() {
        //TODO 选中返回数据进行操作
        if (isWXAppInstalledAndSupported()) {
            Shipper shipper = dbCache.GetObject(Shipper.class);
            shipper.payment = "使用\t\t微信\t\t付款";
            shipper.havedPayMethod = true;
            dbCache.save(shipper);
            Intent intent = new Intent();
            intent.putExtra("payment", shipper.payment);
            setResult(Code.OK.toValue(), intent);
            finish();
        } else {
            toastFast("未安装微信，提交失败。请尝试使用其他支付方式。");
        }
    }

    AlertDialog alertDialog;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.dismiss();
            alertDialog.cancel();

        }
    };
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    };

    @OnClick(R.id.btnAddBankCard)
    void setBtnAddBankCard() {
        //TODO 跳转到汇付绑定银行卡
        if (adapterList.size() < 9) {
            Intent intent = new Intent(this, BindBankCardActivity.class);
            intent.putExtra("addBank", "addBank");
            intent.putExtra("mode", "add");
            startActivityForResult(intent, 101);
        } else {
            alertDialog.setCanceledOnTouchOutside(false);
            DialogUtil.showUpdateAlert("", getString(R.string.dialog_bind_limit), getString(R.string.btn_cancel), getString(R.string.btn_confirm), "提示", listener, cancelListener, alertDialog);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Code.OK.toValue()) {
            if (requestCode == 100) {
                String action = data.getStringExtra("methodAction");
                if (action.equals("del")) {
                    Tracer.e("payMethod", action);
                    adapterList.clear();
                    getBindStatus();
                }
//                shipper.payment = payment;
//                shipper.havedPayMethod = true;
//                dbCache.save(shipper);
            }
            if (requestCode == 101) {
                String action = data.getStringExtra("methodAction");
                if (action.equals("add")) {
                    getBindStatus();
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
