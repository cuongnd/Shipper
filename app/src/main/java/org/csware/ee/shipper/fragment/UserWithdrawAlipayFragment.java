package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.AlipayInfo;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.Tools;
import org.csware.ee.widget.DialogWidget;
import org.csware.ee.widget.PayPasswordView;

import java.util.HashMap;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserWithdrawAlipayFragment extends FragmentBase {

    final static String TAG = "UserWithdrawAlipay";

    @InjectView(R.id.txtAccount)
    EditText txtAccount;
    @InjectView(R.id.txtRealname)
    TextView txtRealname;
    @InjectView(R.id.txtMoney)
    EditText txtMoney;
    @InjectView(R.id.btnCoupon)
    LinearLayout btnCoupon;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
//    private DialogWidget mDialogWidget;

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_withdraw_alipay_fragment;
    }

    DbCache dbCache;
    AlipayInfo alipayInfo;
    AccountReturn ac;
    MeReturn userInfo;

    @Override
    public void init() {
        dbCache = new DbCache(baseActivity);
        alipayInfo = dbCache.GetObject(AlipayInfo.class);
        if (alipayInfo == null) {
            alipayInfo = new AlipayInfo();
        }
        ac = dbCache.GetObject(AccountReturn.class);
        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }

        if (!Guard.isNullOrEmpty(alipayInfo.account))
            txtAccount.setText(alipayInfo.account);

        String realname = "";
        if (userInfo.OwnerUser.CompanyStatus == 1) {
            if (!Guard.isNull(userInfo.OwnerCompany)) {
                realname = userInfo.OwnerCompany.LegalPerson;
            }
        } else {
            if (!Guard.isNullOrEmpty(alipayInfo.realname)) {
                realname = alipayInfo.realname;
            } else if (!Guard.isNull(userInfo.Owner.Name)) {
                realname = userInfo.Owner.Name;
            }
        }
//        Tracer.e(TAG,userInfo.OwnerUser.CompanyStatus+":"+realname);
        txtRealname.setText(realname);


//        if (!Guard.isNullOrEmpty(userInfo.Owner.Name))
//            txtRealname.setText(userInfo.Owner.Name);
        if (ac != null) {
            if (ac.Account != null) {
                if (!Guard.isNull(ac.Account.Amount) && ac.Account.Amount >= 100) {
                    txtMoney.setHint("可提现金额" + ac.Account.Amount + "元");
                } else {
                    txtMoney.setHint("提款金额必须不小于100元");
                }
            }
        } else {
            txtMoney.setHint("可提现金额0元");
        }
    }


    //String password;
    String money;

    @OnClick({R.id.btnConfirm})
    void openWithdraw(View v) {
        alipayInfo.account = txtAccount.getText().toString();
        alipayInfo.realname = txtRealname.getText().toString();
        money = txtMoney.getText().toString();

        dbCache.save(alipayInfo);

        if (Guard.isNullOrEmpty(alipayInfo.account)) {
            toastFast("请输入支付宝账号");
            return;
        }

        if (Guard.isNullOrEmpty(alipayInfo.realname)) {
            toastFast("请输入收款人姓名");
            return;
        }
        if (Guard.isNullOrEmpty(money)) {
            toastFast("请输入提现金额");
            return;
        }

        if (ac == null) {
            toastFast("金额不足，无法提现");
            return;
        }

        double m = 0;
        if (!Guard.isNullOrEmpty(money)) {
            m = Double.valueOf(money);
        }
        if (ac.Account.Amount < 100) {
            toastFast("余额不足100，无法提现");
            return;
        }
        if (m < 100) {
            toastFast("金额不足100，无法提现");
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("alipayAccount",alipayInfo.account);
        map.put("alipayName",alipayInfo.realname);
        MobclickAgent.onEvent(baseActivity, "wallet_money_alipay", map);

        if (Guard.isNullOrEmpty(ac.Account.Password)) {

            UserWithdrawFragment.popPasswordBox(baseActivity, getString(R.string.tip_first_withdraw), new IAsyncPassword() {
                @Override
                public void notify(final String password) {
                    HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
                    pms.addParam("password", password);
                    pms.addParam("idcard", "");
                    pms.addParam("name", "");
                    BackcardApi.accountBank(baseFragment.getActivity(), baseActivity, pms, new IJsonResult() {
                        @Override
                        public void ok(Return json) {
                            //成功
                            ac.Account.Password = "*";
                            dbCache.save(ac);

                            asyncDraw(password);
                        }

                        @Override
                        public void error(Return json) {

                        }
                    });

//                    HttpParams pms = new HttpParams();
//                    pms.addParam("password", password);
//                    String url = pms.getUrl(API.BANKCARD.ACCOUNTEDIT);
//                    Log.d("RequestURL", url);
//                    final ProgressDialog processDialog = createDialog(R.string.dialog_loading);
//                    FinalHttp fh = new FinalHttp();
//                    fh.get(url, new AjaxCallBack<String>() {
//
//                        @Override
//                        public void onSuccess(String res) {
//                            //Log.d(TAG, "RESPONSE=\n" + res);
//                            Return result = GsonHelper.fromJson(res, Return.class);
//                            if (result.Result == 0) {
//                                //成功
//                                ac.Account.Password = "*";
//                                dbCache.save(ac);
//
//                                asyncDraw(password);
//
//
//                            } else {
//                                //失败
//                                String msg = Guard.isNullOrEmpty(result.Message) ? getString(R.string.tip_inner_error) : result.Message;
//                                toastSlow(msg);
//                            }
//                            processDialog.dismiss();
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t, int errCode, String strMsg) {
//                            //加载失败的时候回调
//                            Log.e(TAG, "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
//                            processDialog.dismiss();
//                            toastSlow(R.string.tip_server_error);
//                        }
//                    });
                }
            });

//            mDialogWidget = new DialogWidget(getActivity(), setDecorViewDialog());
//            mDialogWidget.show();

        }else
            //开始提现
            if (ClientStatus.getNetWork(baseActivity)) {

            UserWithdrawFragment.popPasswordBox(baseActivity, "请输入支付密码", new IAsyncPassword() {
                @Override
                public void notify(String password) {
                    asyncDraw(password);
                }
            });
//                mDialogWidget = new DialogWidget(getActivity(), getDecorViewDialog());
//                mDialogWidget.show();
            }

    }

    /**
     * 设置密码弹窗
     */

    private View setDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayPasswordView.getInstance(getString(R.string.tip_first_withdraw), "", getActivity(), new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(final String password) {
                // TODO Auto-generated method stub

                HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
                pms.addParam("password", password);
                pms.addParam("idcard", "");
                pms.addParam("name", "");
                BackcardApi.accountBank(baseFragment.getActivity(), baseActivity, pms, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        //成功
                        ac.Account.Password = "*";
                        dbCache.save(ac);

                        asyncDraw(password);
                    }

                    @Override
                    public void error(Return json) {

                    }


                });

            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub

            }
        }).getView();
    }

    /**
     * 提现密码弹窗
     */

    private View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayPasswordView.getInstance(/*"请输入支付密码"*/getString(R.string.wallet_password), "", getActivity(), new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                // TODO Auto-generated method stub

                asyncDraw(password);

            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub

            }
        }).getView();
    }

    /*
    * 检验密码
    *
    * */
    ProgressDialog dialog;
    void asyncDraw(String password) {
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        BackcardApi.draw(baseFragment.getActivity(), baseActivity, password, alipayInfo.account, alipayInfo.realname, null, money, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                toastSlow(R.string.withdraw_time_tip);
                if (ac.Account.Amount - Float.valueOf(money) >= 100) {
                    txtMoney.setHint("可提现金额" + ac.Account.Amount + "元");
                } else {
                    txtMoney.setHint("提款金额必须100起");
                }
                ac.Account.Amount = ac.Account.Amount - Float.valueOf(money);
                dbCache.save(ac);
                txtMoney.setHint("可提现金额" + ac.Account.Amount + "元");
                ((UserWalletFragmentActivity) getActivity()).finish();
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
//                if (!Guard.isNullOrEmpty(json.Message)) {
//                    toastSlow(json.Message);
//                } else {
//                    toastSlow("提现失败");
//                }
            }
        });

    }


}