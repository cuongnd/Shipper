package org.csware.ee.shipper.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.CouponListInfo;
import org.csware.ee.model.DrawListModel;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.UserVerifyType;
import org.csware.ee.shipper.DeliverCollectionExtraFragmentActivity;
import org.csware.ee.shipper.DeliverFragmentActivity;
import org.csware.ee.shipper.MailListActivity;
import org.csware.ee.shipper.MainTabFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserBeaerActivity;
import org.csware.ee.shipper.UserCouponsListActivity;
import org.csware.ee.shipper.UserRecordActivity;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserWalletFragment extends FragmentBase {

    final static String TAG = "UserWalletFragment";
    @InjectView(R.id.labMoney)
    TextView labMoney;
    @InjectView(R.id.btnWallet)
    LinearLayout btnWallet;
    @InjectView(R.id.labBankCard)
    TextView labBankCard;
    @InjectView(R.id.btnBankCard)
    LinearLayout btnBankCard;
    @InjectView(R.id.labGiftBag)
    TextView labGiftBag;
    @InjectView(R.id.btnGiftBag)
    LinearLayout btnGiftBag;
    @InjectView(R.id.labScroeShop)
    TextView labScroeShop;
    @InjectView(R.id.btnScoreShop)
    LinearLayout btnScoreShop;
    @InjectView(R.id.labCoupon)
    TextView labCoupon;
    @InjectView(R.id.btnCoupon)
    LinearLayout btnCoupon;
    @InjectView(R.id.btnRecord)
    LinearLayout btnRecord;
    //    RelativeLayout btn_Back;
    ProgressDialog dialog;
    @InjectView(R.id.labRecord)
    TextView mLabRecord;
    @InjectView(R.id.labCoupons)
    TextView mLabCoupons;
    @InjectView(R.id.btnCoupons)
    LinearLayout mBtnCoupons;
    String Result;
    MeReturn userInfo;
    DbCache dbCache;

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_wallet_fragment;
    }

    @Override
    public void init() {
        //labMoney.setText("钱包:0元");
        //labGiftBag.setText("领取88888元大礼包");
        //labScroeShop.setText("55555分");
        //labCoupon.setText("5张");
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
        asyncLoadAccount();
//        btn_Back = (RelativeLayout)rootView.findViewById(R.id.btn_Back);
//        btn_Back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });
        ((UserWalletFragmentActivity) getActivity()).finish(getString(R.string.icon_wallet));
    }

    void asyncLoadAccount() {
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        BackcardApi.account(baseFragment.getActivity(), baseActivity, new IJsonResult<AccountReturn>() {
            @Override
            public void ok(AccountReturn json) {
                //成功
                if (labMoney != null) {
                    labMoney.setText("钱包:" + FormatHelper.toMoney(json.Account.Amount) + "元");
                    dbCache.save(json);
                }
                asyncDrawList();
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

        HttpParams params = new HttpParams(API.BANKCARD.COUPONLIST);
        params.addParam("category", "");
        BackcardApi.getCouponListApi(getActivity(), baseActivity, params, new IJsonResult<CouponListInfo>() {
            @Override
            public void ok(CouponListInfo json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                //成功
                if (!Guard.isNull(json)) {
                    List<CouponListInfo.CouponsBean> list = json.Coupons;
                    Result = GsonHelper.toJson(json);
                    if (list.size() > 0 && list != null) {
                        if (mLabCoupons != null) {
                            mLabCoupons.setText(list.size() + " 张优惠券");
                        }
                    } else {
                        if (mLabCoupons != null) {
                            mLabCoupons.setText("");
                        }
                    }
                }

            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                Tracer.e(TAG, "暂无数据");
            }
        });
    }

    void asyncDrawList() {
        HttpParams pms = new HttpParams(API.BANKCARD.DRAWLIST);
        BackcardApi.drawlist(baseFragment.getActivity(), baseActivity, pms, new IJsonResult<DrawListModel>() {
            @Override
            public void ok(DrawListModel json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                if (!Guard.isNull(json)) {
                    if (json.Draws.size() > 0) {
                        double drawMoney = 0;
                        for (int i = 0; i < json.Draws.size(); i++) {
                            if (json.Draws.get(i).Status < 3) {
                                drawMoney += json.Draws.get(i).Amount;
                            }
                        }
                        if (labCoupon != null) {
                            labCoupon.setText("即将到账：" + FormatHelper.toMoney(drawMoney) + "元");
                        }
                    }
                }
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


    @OnClick({R.id.btnBankCard})
    void openBankCard(View v) {
        getUserInfo(false, 1);
    }

    @OnClick({R.id.btnWallet})
    void openWithdraw(View v) {
        getUserInfo(false, 2);
    }

    @OnClick(R.id.btnCoupon)
    void setBtnCoupon() {
        getUserInfo(false, 2);
    }

    //交易明细
    @OnClick(R.id.btnRecord)
    void setBtnRecord() {
        HashMap<String, String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "wallet_record");
        startActivity(new Intent(getActivity(), UserRecordActivity.class));
    }

    @OnClick(R.id.btnCoupons)
    void setBtnCoupons() {
        //优惠券信息
        if (!Guard.isNull(Result)) {
            Intent intent = new Intent(getActivity(), UserCouponsListActivity.class);
            intent.putExtra("CouponListInfo", Result);
            startActivity(intent);
        }
    }

    void isDraw() {
        HashMap<String, String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "wallet_money");
        AccountReturn accountReturn = dbCache.GetObject(AccountReturn.class);
//        if (!Guard.isNull(accountReturn)) {
//            if (!Guard.isNull(accountReturn.Account)) {
//                if (accountReturn.Account.Amount >= 100) {
        ((UserWalletFragmentActivity) getActivity()).replace(new UserWithdrawFragment(), "提现");
//                } else {
//                    toastFast("余额不足100哦亲，暂不能提现");
//                }
//            }
//        }

    }

    void getUserInfo(final boolean getUserId, final int what) {
        if (Tools.getNetWork(baseActivity)) {
            dialog = Tools.getDialog(baseActivity);
            dialog.setCanceledOnTouchOutside(false);
            Activity activity = MainTabFragmentActivity.instance;
            UserApi.info(activity, baseActivity, new IJsonResult<MeReturn>() {
                @Override
                public void ok(MeReturn json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    userInfo = json;
                    if (!getUserId) {
                        if (Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                            userInfo.OwnerUser.CompanyStatus = -1;
                        }
                        dbCache.save(userInfo);
                        int Status;
                        if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                                Status = userInfo.OwnerUser.Status;
                            } else {
                                Status = userInfo.OwnerUser.CompanyStatus;
                            }
                        } else {
                            Status = userInfo.OwnerUser.Status;
                        }
                        if (Status == UserVerifyType.VERIFIED.toValue()) {
                            if (what == 1) {
                                Tracer.d(TAG, "银行卡");
                                HashMap<String, String> map = new HashMap<>();
                                MobclickAgent.onEvent(baseActivity, "wallet_card");
                                ((UserWalletFragmentActivity) getActivity()).replace(new UserBankCardFragment(), "银行卡");
                            } else if (what == 2) {
                                Tracer.d(TAG, "提现or余额");
                                isDraw();
                            }

                        } else {
                            toastFast("暂未实名认证,认证后更多功能等着您！");
                        }
                    }
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
        } else {
            toastFast(org.csware.ee.R.string.tip_need_net);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
