package org.csware.ee.shipper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.AlipayHelper;
import com.alipay.sdk.pay.PayResult;
import com.jungly.gridpasswordview.GridPasswordView;
import com.lidroid.xutils.cache.MD5FileNameGenerator;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tencent.mm.sdk.constants.Build;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.wxchat.sdk.pay.WxChatpayHelper;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.CouponListInfo;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.PayeeScanModel;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.User;
import org.csware.ee.model.WeiXinPayResultInfo;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.fragment.UserWithdrawFragment;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Md5Encryption;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.DialogWidget;
import org.csware.ee.widget.PayPasswordView;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/4.
 */
public class UserPayToBankActivity extends ActivityBase {
    private static final String TAG = "UserPayToBankActivity";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtPayMoney)
    TextView txtPayMoney;
    @InjectView(R.id.labRewardPrice)
    TextView labRewardPrice;
    @InjectView(R.id.Lin_calculation)
    LinearLayout LinCalculation;
    @InjectView(R.id.txtRecName)
    TextView txtRecName;
    @InjectView(R.id.txtAddress)
    TextView txtAddress;
    @InjectView(R.id.txtGoodsType)
    TextView txtGoodsType;
    @InjectView(R.id.txtCreateTime)
    TextView txtCreateTime;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @InjectView(R.id.txtPayMethod)
    TextView txtPayMethod;
    @InjectView(R.id.txtPayChange)
    TextView txtPayChange;

    View contentView;
    DialogPlus dialog;
    GridPasswordView txtPassword;
    double price;
    static String money;
    double Discount;
    long orderId;
    String cardNumber;
    String RewardPrice = "0", action = "", payeeid = "", payToName = "";
    public static Activity instanse;
    ChinaAreaHelper areaHelper;
    BindStatusInfo bankinfo;
    Shipper shipper;
    DbCache dbCache;
    String bankName = "", createTime;
    int bankResId = 0, payMethod = 1;
    static String orederNo;
    @InjectView(R.id.LinAward)
    LinearLayout LinAward;
    OilCardInfo info;
    @InjectView(R.id.labCoupons)
    TextView mLabCoupons;
    @InjectView(R.id.btnCoupons)
    LinearLayout mBtnCoupons;
    private String payment = "", cardid, name = "";
    static MeReturn userInfo;
    AccountReturn ac;
    Handler mHandler;
    ProgressDialog progressDialog;
    private IWXAPI api;
    long PayId;
    ViewHolder passHolder;
    DialogPlus passDialog;
    View passView;
    public static boolean isClicks = true;
    private List<CouponListInfo.CouponsBean> _coupons = new ArrayList<>();
    String Result;
    ProgressDialog mProgressDialog;
    int pos = -1;
    String couponId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_to_bank);
        ButterKnife.inject(this);
        instanse = this;
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WxChatpayHelper.appid, false);
        api.registerApp(WxChatpayHelper.appid);

        dbCache = new DbCache(baseActivity);
        info = dbCache.GetObject(OilCardInfo.class);
        bankinfo = dbCache.GetObject(BindStatusInfo.class);
        shipper = dbCache.GetObject(Shipper.class);
        userInfo = dbCache.GetObject(MeReturn.class);
        areaHelper = new ChinaAreaHelper(baseActivity);
        ac = dbCache.GetObject(AccountReturn.class);
        if (Guard.isNull(ac)) {
            ac = new AccountReturn();
        }
        topBar.setTitle("支付确认");
        //获取油卡优惠券的数据
        asyncLoadingData();
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
        if (Guard.isNull(userInfo)) {
            userInfo = new MeReturn();
        }
        if (Guard.isNull(info)) {
            info = new OilCardInfo();
        }
        if (shipper.cardid > 0) {
            cardid = shipper.cardid + "";
        }
        mHandler = new alipayHandler(baseActivity, dbCache);
        // press tracaction password
        ViewHolder holder = new ViewHolder(R.layout.dialog_set_bankpay_pwd);
        dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(DialogPlus.Gravity.CENTER)
                .create();
        contentView = dialog.getHolderView();
        txtPassword = (GridPasswordView) contentView.findViewById(R.id.txtPassword);
        //first create password
        passHolder = new ViewHolder(R.layout.dialog_set_pay_pwd);
        passDialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(passHolder)
                .setCancelable(false)
                .setGravity(DialogPlus.Gravity.CENTER)
                .create();
        passView = passDialog.getHolderView();

        action = getIntent().getStringExtra("action");
        if (Guard.isNullOrEmpty(action)) {
            action = "";
        }
        if (action.equals("Oil")) {
            money = info.getMoney();//价格
            cardNumber = info.getCardNumber();//油卡号
            createTime = info.getCreateTime();//创建时间
            orederNo = info.getOrderNO();//订单号
            txtPayMoney.setText(money);
            txtAddress.setText(cardNumber);
            txtCreateTime.setText(createTime);
            Tracer.e("PayToBank", Guard.isNull(userInfo.Owner) + "");
            if (!Guard.isNull(userInfo.OwnerUser)) {
                if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                    if (userInfo.OwnerUser.CompanyStatus == 1) {
                        if (!Guard.isNull(userInfo.OwnerCompany)) {
                            if (!Guard.isNullOrEmpty(userInfo.OwnerCompany.LegalPerson)) {
                                name = userInfo.OwnerCompany.LegalPerson;
                            }
                        }
                    } else {
                        setRecName();
                    }
                } else {
                    setRecName();
                }
            }
            setRecName();
            txtRecName.setText(name);
        } else {
            price = getIntent().getDoubleExtra("price", -1);
            orderId = getIntent().getLongExtra("orderId", -1);
        }


        if (!Guard.isNullOrEmpty(shipper.payment)) {
            txtPayMethod.setText(shipper.payment);
        }
    }

    private void asyncLoadingData() {
        mProgressDialog = Tools.getDialog(baseActivity);
        mProgressDialog.setCanceledOnTouchOutside(false);
        HttpParams params = new HttpParams(API.BANKCARD.COUPONOIL);
        params.addParam("category", "oil");
        BackcardApi.getCouponOilApi(baseActivity, baseActivity, params, new IJsonResult<CouponListInfo>() {
            @Override
            public void ok(CouponListInfo json) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
                //成功
                if (!Guard.isNull(json)) {
                    List<CouponListInfo.CouponsBean> list = json.Coupons;
                    Result = GsonHelper.toJson(json);
                    if (list.size() > 0 && list != null) {
                        int couponNumber = 0;
                        _coupons.addAll(list); //将新数据加入
                        if (mLabCoupons != null) {
                            for (int i = 0; i < _coupons.size(); i++) {
                                if (!_coupons.get(i).IsExpired) {
                                    couponNumber = couponNumber + 1;
                                }
                            }
                            mLabCoupons.setText(couponNumber + " 张可用优惠券");
                        }
                    } else {
                        if (mLabCoupons != null) {
                            mLabCoupons.setText("无可用优惠券");
                        }
                    }
                }


            }

            @Override
            public void error(Return json) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
                Tracer.e(TAG, "暂无数据");
            }
        });


    }

    void setRecName() {
        if (!Guard.isNull(userInfo.Owner)) {
            if (!Guard.isNullOrEmpty(userInfo.Owner.Name)) {
                name = userInfo.Owner.Name;
            }
        }
        if (Guard.isNullOrEmpty(name)) {
            if (!Guard.isNull(userInfo.OwnerUser)) {
                if (!Guard.isNullOrEmpty(userInfo.OwnerUser.Mobile)) {
                    name = userInfo.OwnerUser.Mobile;
                }
            }
        }
    }

    //选择支付方式
    @OnClick(R.id.txtPayChange)
    void setTxtPayChange() {
        Intent intent = new Intent(this, UserPayMethodActivity.class);
        if (action.equals("Oil")) {
            double p = Double.parseDouble(txtPayMoney.getText().toString());
            intent.putExtra("payPrice", money);
        } else {
            intent.putExtra("payPrice", price);

        }
        startActivityForResult(intent, 100);
    }

    //选择优惠券
    @OnClick(R.id.btnCoupons)
    void setBtnCoupons() {
        if (isClicks) {
            isClicks = false;
            //优惠券信息
            if (!Guard.isNull(Result)) {
                Intent intent = new Intent(UserPayToBankActivity.this, ChooseCoupon.class);
                intent.putExtra("CouponListInfo", Result);//优惠券的信息
                intent.putExtra("pos", pos);//位置
                startActivityForResult(intent, 101);
            }
        }
    }

    //点击确认付款按钮，进行付款操作
    @OnClick(R.id.btnConfirm)
    void setBtnConfirm() {
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }

        HashMap<String, String> map = new HashMap<>();
        map.put("oilCardNumber", cardNumber);
        map.put("oilOrderId", orederNo);
        map.put("oilPrice", money);
        MobclickAgent.onEvent(baseActivity, "oil_payConfirm", map);

        if (!Guard.isNullOrEmpty(shipper.payment)) {
            Tracer.e(TAG, shipper.payment + "");
            payment = shipper.payment;
            if (payment.contains("支付宝")) {
                if (action.equals("Oil")) {
                    asyncAlipay();
                }
            } else if (payment.contains("微信")) {
                Weixinpay();
            } else if (payment.contains("余额")) {
                if (!Guard.isNull(ac.Account)) {
                    payMethod = 2;
                    if (!Guard.isNullOrEmpty(ac.Account.Password)) {
                        Balancepay();
                    } else {
                        createView(getString(R.string.tip_first_withdraw));
                    }
                }else {
                    asyncLoadAccount();
                }
            } else if (payment.contains("银行") || shipper.payment.contains("银行")) {
                payMethod = 1;
                UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, money + "", "", listener);
//                        mDialogWidget = new DialogWidget(baseActivity, getDecorViewDialog());
//                        mDialogWidget.show();
            }
        } else {
            toastFast("请选择付款方式");
        }
    }

    void asyncLoadAccount() {

        BackcardApi.account(baseActivity, baseActivity, new IJsonResult<AccountReturn>() {
            @Override
            public void ok(AccountReturn json) {
                //成功
                ac = json;
                dbCache.save(json);
                if (Guard.isNull(json)){
                    return;
                }
                if (Guard.isNull(json.Account)){
                    return;
                }
                if (!Guard.isNullOrEmpty(json.Account.Password)) {
                    Balancepay();
                }else {
                    createView(getString(R.string.tip_first_withdraw));
                }
            }

            @Override
            public void error(Return json) {

            }
        });


    }

    private void asyncAlipay() {
        mProgressDialog = Tools.getDialog(baseActivity);
        mProgressDialog.setCanceledOnTouchOutside(false);
        HttpParams pms = new HttpParams(API.BANKCARD.AlIPAYOIL);
        pms.addParam("id", orederNo);
        pms.addParam("couponid", couponId);
        BackcardApi.accountBank(baseActivity, baseActivity, pms, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
                Alipay();
            }

            @Override
            public void error(Return json) {
                if (mProgressDialog != null) {
                    if (mProgressDialog.isShowing()) {
                        mProgressDialog.dismiss();
                    }
                }
            }
        });

    }


    void createView(String title) {
        UserWithdrawFragment.popPasswordBox(baseActivity, title, okListener, cancelListener, passDialog, passView);
    }

    void resetPassView(boolean isHide) {
        final GridPasswordView txtPassword = (GridPasswordView) passView.findViewById(R.id.txtPassword);
        txtPassword.clearPassword();
        if (isHide) {
            Tools.hideInput(baseActivity, txtPassword);
        }
    }

    int viewStatus = 1;
    String firstPass = "", confirmPass = "", finalActStr = "";

    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            viewStatus = 1;
            resetPassView(true);
            passDialog.dismiss();
        }
    };
    View.OnClickListener okListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            final GridPasswordView txtPassword = (GridPasswordView) passView.findViewById(R.id.txtPassword);
            String pass = txtPassword.getPassWord();
            if (!Guard.isNullOrEmpty(pass)) {
                if (pass.length() == 6) {
                    txtPassword.clearPassword();
                    if (viewStatus == 1) {
                        viewStatus = 2;
                        firstPass = pass;
                        createView(getString(R.string.tip_first_confirm));
                    } else if (viewStatus == 2) {
                        viewStatus = 1;
                        confirmPass = pass;
                        if (firstPass.equals(confirmPass)) {
                            createPass(confirmPass);
                        } else {
                            viewStatus = 2;
                            txtPassword.clearPassword();
                            toastFast("两次密码不一致，请重新输入");
                        }
                    } else if (viewStatus == 3) {
                        resetPassView(true);
                        passDialog.dismiss();
                        //准备支付
//                        Balancepay();
                        HttpParams pms = null;
                        if (payMethod == 2) {
                            pms = new HttpParams(API.BANKCARD.BALANCEOILPAY);
                            pms.addParam("password", txtPassword.getPassWord());
                            pms.addParam("id", orederNo);
                            pms.addParam("price", money);
                            pms.addParam("couponid", couponId);
                        } else {
                            pms = new HttpParams(API.BANKCARD.GASBACK);
                            pms.addParam("password", txtPassword.getPassWord());
                            pms.addParam("orderid", orederNo);
                            pms.addParam("cardid", cardid);
                            pms.addParam("price", money);
                            pms.addParam("couponid", couponId);
                        }
                        asyncDraw(pms);
                    }
                } else {
                    toastFast("请输入正确的密码");
                }
            } else {
                toastFast("密码不能为空");
            }
        }
    };

    void createPass(String password) {
        HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
        pms.addParam("password", password);
        pms.addParam("idcard", "");
        pms.addParam("name", "");
//        resetPassView();
        BackcardApi.accountBank(baseActivity, baseActivity, pms, new IJsonResult() {
            @Override
            public void ok(Return json) {
                //成功
                ac.Account.Password = "*";
                dbCache.save(ac);
                resetPassView(false);
                toastFast("设置交易密码成功");
                viewStatus = 3;
                resetPassView(true);
                //准备支付
                createView(getString(R.string.hint_setPass_tip));
            }

            @Override
            public void error(Return json) {
                viewStatus = 1;
                resetPassView(false);
                passDialog.dismiss();
            }
        });
    }

    /**
     * 密码弹窗
     */

    private View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayPasswordView.getInstance("请输入支付密码", "", baseActivity, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                // TODO Auto-generated method stub
//                if (mDialogWidget != null) {
//                    if (mDialogWidget.isShowing()) {
//                        mDialogWidget.dismiss();
//                    }
//                }
//                asyncDraw(password);
            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub

            }
        }).getView();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pwd = txtPassword.getPassWord();
            if (!Guard.isNullOrEmpty(pwd)) {
                if (pwd.length() == 6) {
                    Tools.hideInput(baseActivity, txtPassword);
                    dialog.dismiss();
                    HttpParams pms = null;
                    if (payMethod == 2) {
                        pms = new HttpParams(API.BANKCARD.BALANCEOILPAY);
                        pms.addParam("password", txtPassword.getPassWord());
                        pms.addParam("id", orederNo);
                        pms.addParam("price", money);
                        pms.addParam("couponid", couponId);
                    } else {
                        pms = new HttpParams(API.BANKCARD.GASBACK);
                        pms.addParam("password", txtPassword.getPassWord());
                        pms.addParam("orderid", orederNo);
                        pms.addParam("cardid", cardid);
                        pms.addParam("price", money);
                        pms.addParam("couponid", couponId);
                    }
                    asyncDraw(pms);
                } else {
                    toastFast("请输入正确的密码");
                }
            } else {
                toastFast("密码不能为空");
            }
        }
    };

    private void asyncDraw(HttpParams pms) {
//        txtPassword.clearPassword();
        //TODO 支付接口 成功失败跳转
        progressDialog = Tools.getDialog(baseActivity);
        progressDialog.setCanceledOnTouchOutside(false);

        asyncPayBank(pms);
    }

    void asyncPayBank(HttpParams params) {
        BackcardApi.payBank(baseActivity, this, params, new IJsonResult<Return>() {
            @Override
            public void ok(Return json) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                txtPassword.clearPassword();
                SharedPreferences preferences = getSharedPreferences("RefreshOil", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isRefresh", true).commit();
                Intent intent = new Intent(UserPayToBankActivity.this, PayResultActivity.class);
                intent.putExtra("RewardPrice", "");
                intent.putExtra("price", money);
                intent.putExtra("isSuccess", true);
                startActivity(intent);
                payResultBack(baseActivity, baseActivity);
                if (UserPayToPlatformFragmentActivity.instanse != null) {
                    UserPayToPlatformFragmentActivity.instanse.finish();
                }
                instanse.finish();

            }

            @Override
            public void error(Return json) {
                txtPassword.clearPassword();
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    private void Balancepay() {
//        progressDialog = Tools.getDialog(baseActivity);
//        progressDialog.setCanceledOnTouchOutside(false);
//        HttpParams params = new HttpParams(API.BANKCARD.BALANCEOILPAY);
//        params.addParam("orderid", orederNo);
//        params.addParam("price", money);
////        params.addParam("paymethod", "account");
//        getPrepayId(params);
        UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, money + "", "", listener);
    }

    private void Weixinpay() {
        progressDialog = Tools.getDialog(baseActivity);
        progressDialog.setCanceledOnTouchOutside(false);
        HttpParams params = new HttpParams(API.BANKCARD.OILWECHATPAY);
        params.addParam("orderid", orederNo);
        params.addParam("price", money);
        params.addParam("couponid", couponId);
        getPrepayId(params);
    }

    private void getPrepayId(HttpParams params) {

        BackcardApi.getWeiXinPayId(baseActivity, this, params, new IJsonResult<WeiXinPayResultInfo>() {
            @Override
            public void ok(WeiXinPayResultInfo json) {
                Tracer.e(TAG, "json=" + json + "");
                PayId = json.Id;
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                String timestamp = HttpParams.createTimestamp();
                if (!Guard.isNull(json.Weixin)) {
                    Tracer.e(TAG, json.Weixin.appid + " " + json.Weixin.mch_id + " " + json.Weixin.prepay_id + "  " + json.Weixin.nonce_str + " " + timestamp + " " + WxChatpayHelper.packages + " " + json.Weixin.sign);
                    PayReq req = new PayReq();
                    req.appId = json.Weixin.appid;
                    req.partnerId = json.Weixin.mch_id;
                    req.prepayId = json.Weixin.prepay_id;
                    req.nonceStr = json.Weixin.nonce_str;
                    req.timeStamp = json.Weixin.timestamp;
                    req.packageValue = "Sign=WXPay";
                    req.sign = json.Weixin.sign;
                    api.registerApp(WxChatpayHelper.appid);
                    api.sendReq(req);
                    //TODO 存下订单的信息用于微信支付回调界面获取
                    SharedPreferences preferences = getSharedPreferences("isReqWeChat", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("money", money);
                    editor.putString("orderNo", orederNo);
                    editor.commit();
                }
                // optional
                // 在支付之前，如果应用没有注册到微信，应该先调用IWXMsg.registerApp将应用注册到微信
                boolean isPaySupported = api.getWXAppSupportAPI() >= Build.PAY_SUPPORTED_SDK_INT;
                Tracer.e(TAG, "是否注册过微信" + String.valueOf(isPaySupported));
            }

            @Override
            public void error(Return json) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                if (Guard.isNull(PayId)) {
                    toastFast("获取到订单号错误，请稍候重试");
                    return;
                }
            }
        });
    }

    private void Alipay() {
        String msg = orederNo;
        String callback = "";
        callback = API.ORDER.ALIPAYOIlCALLBACK;

        String orderInfo = AlipayHelper.getOrderInfo(msg, money, "油卡支付", userInfo.Owner.UserId + "", callback);
        Tracer.e(TAG, callback + " " + msg + " id:" + orederNo + "\n" + orderInfo);
        // 对订单做RSA 签名
        String sign = AlipayHelper.sign(orderInfo);
        try {
            // 仅需对sign 做URL编码
            sign = URLEncoder.encode(sign, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        // 完整的符合支付宝参数规范的订单信息
        final String payInfo = orderInfo + "&sign=\"" + sign + "\"&"
                + AlipayHelper.getSignType();

        Runnable payRunnable = new Runnable() {

            @Override
            public void run() {
                try {
                    // 构造PayTask 对象
                    PayTask alipay = new PayTask(baseActivity);
                    // 调用支付接口，获取支付结果
                    String result = alipay.pay(payInfo);
                    Message msg = new Message();
                    msg.what = AlipayHelper.SDK_PAY_FLAG;
                    msg.obj = result;
                    mHandler.sendMessage(msg);
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };

        // 必须异步调用
        Thread payThread = new Thread(payRunnable);
        payThread.start();
        SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isRefresh", true).commit();
    }

    //    @OnClick(R.id.btnConfirm)
//    void setBtnConfirm() {
//        if (ClientStatus.getNetWork(baseActivity)) {
//
//            UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, payPrice + "", payToName, listener);
//        }
//    }
//
//    private void asyncDraw(String password) {
//        txtPassword.clearPassword();
//        //TODO 支付接口 成功失败跳转
//        progressDialog = Tools.getDialog(baseActivity);
//        progressDialog.setCanceledOnTouchOutside(false);
//        if (action.equals("scan")) {
//            HttpParams pms = new HttpParams(API.BANKCARD.PAYCOLLECTION);
//            pms.addParam("password", password);
//            pms.addParam("payeeid", payeeid);
//            asyncPayBank(pms);
//        } else if (action.equals("freight")) {
//            HttpParams pms = new HttpParams(API.BANKCARD.PAYORDER);
//            pms.addParam("password", password);
//            pms.addParam("orderid", orderId);
//            asyncPayBank(pms);
//        } else if (action.equals("Oil")) {
//            toastFast("还在紧张的开发中。。。。");
//
//        }
//
//    }
//
//    void asyncPayBank(HttpParams params) {
//        BackcardApi.payBank(this, params, new IJsonResult<Return>() {
//            @Override
//            public void ok(Return json) {
//                if (progressDialog != null) {
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                }
//
//                SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
//                SharedPreferences.Editor editor = preferences.edit();
//                editor.putBoolean("isRefresh", true).commit();
//                Intent intent = new Intent(UserPayToBankActivity.this, PayResultActivity.class);
//                intent.putExtra("RewardPrice", RewardPrice);
//                intent.putExtra("price", payPrice);
//                intent.putExtra("isSuccess", true);
//                startActivity(intent);
//            }
//
//            @Override
//            public void error(Return json) {
//                if (progressDialog != null) {
//                    if (progressDialog.isShowing()) {
//                        progressDialog.dismiss();
//                    }
//                }
//            }
//        });
//    }
//
//    View.OnClickListener listener = new View.OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            dialog.dismiss();
//            asyncDraw(txtPassword.getPassWord());
//        }
//    };
    static class alipayHandler extends Handler {

        Activity context;
        DbCache dbCache;

        public alipayHandler(Activity context, DbCache dbCache) {
            this.context = context;
            this.dbCache = dbCache;
        }

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case AlipayHelper.SDK_PAY_FLAG: {
                    PayResult payResult = new PayResult((String) msg.obj);

                    //                    Log.e(TAG, "[JSON]PayResult:\n" + GsonHelper.toJson(payResult));

                    // 支付宝返回此次支付结果及加签，建议对支付宝签名信息拿签约时支付宝提供的公钥做验签
                    String resultInfo = payResult.getResult();

                    String resultStatus = payResult.getResultStatus();
                    // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
                    if (TextUtils.equals(resultStatus, "9000")) {
                        payResultBack(context, context);
                        Toast.makeText(context, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        Shipper shipper = dbCache.GetObject(Shipper.class);
                        shipper.payment = "使用\t\t支付宝\t\t付款";
                        shipper.havedPayMethod = true;
                        dbCache.save(shipper);
                        SharedPreferences preferences = context.getSharedPreferences("RefreshOil", MODE_PRIVATE);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putBoolean("isRefresh", true).commit();
                        //TODO:支付成功后，关闭支付页面，返回订单详情页面，修改订单详情的支付状态。提示等待司机上门取货。
                        Intent intent = new Intent();
                        //intent.putExtra(ParamKey.PAY_RESULT_CODE, Code.OK.toValue());

                        context.setResult(Code.OK.toValue(), intent);
                        if (UserPayToPlatformFragmentActivity.instanse != null) {
                            UserPayToPlatformFragmentActivity.instanse.finish();
                        }
                        context.finish();

                    } else {
                        // 判断resultStatus 为非“9000”则代表可能支付失败
                        // “8000”代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
                        if (TextUtils.equals(resultStatus, "8000")) {
                            Toast.makeText(context, "支付结果确认中",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                            Toast.makeText(context, "支付失败",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                    break;
                }
                case AlipayHelper.SDK_CHECK_FLAG: {
                    Toast.makeText(context, "检查结果为：" + msg.obj,
                            Toast.LENGTH_SHORT).show();
                    break;
                }
                default:
                    break;
            }
        }
    }

    static void payResultBack(Activity activity, Context context) {
        //OrderNo=OO160324191437&Money=5980&CheckCode=bcd1b77d96a45ab9&From=1&SerialNo=345678&PayName=支付";
        String checkCode = Md5Encryption.md5(orederNo + money + "1XXKY");
        Tracer.e("MD5信息", "orderNo=" + orederNo + "price=" + money + "from=" + "1" + "checkCode=" + checkCode);
        HttpParams params = new HttpParams(API.ORDER.OILPAYCALLBACK);
        params.addParam("OrderNo", orederNo);
        params.addParam("Money", money);
        params.addParam("CheckCode", checkCode);
        params.addParam("From", "1");
//                    params.addParam("SerialNo","流水号");
//                    params.addParam("PayName", "支付宝");
        callBack(activity, context, params);

    }

    public static void callBack(final Activity activity, Context context, HttpParams params) {

        BackcardApi.payBank(activity, context, params, new IJsonResult<Return>() {
            @Override
            public void ok(Return json) {
                Tracer.e("pay", "OK");

            }

            @Override
            public void error(Return json) {
                Tracer.e("pay", "failed");
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == Code.OK.toValue()) {
            if (requestCode == 100) {
                payment = data.getStringExtra("payment");
                cardid = data.getStringExtra("cardid") + "";
                txtPayMethod.setText(payment);
            } else if (requestCode == 101) {
                pos = data.getIntExtra("pos", -1);//位置
                Discount = data.getDoubleExtra("Discount", 0.0);//金额
                if (data.getIntExtra("couponId", -1) > 0) {
                    couponId = String.valueOf(data.getIntExtra("couponId", -1));//优惠券ID
                } else {
                    couponId = "";
                }
                Tracer.e(TAG, "onActivityResult position: " + pos + "--" + "couponId: " + couponId + "---" + "Discount: " + Discount);
                if (Discount > 0) {
                    mLabCoupons.setText("- " + Discount+"元");
                    double tempMoney = Double.parseDouble(info.getMoney());
                    money = FormatHelper.toMoney(tempMoney - Discount);
                    txtPayMoney.setText(money);
                } else {
                    money = info.getMoney();//价格
                    txtPayMoney.setText(money);
                    if (_coupons.size() > 0 && _coupons != null) {
                        int num = 0;
                        if (mLabCoupons != null) {
                            for (int i = 0; i < _coupons.size(); i++) {
                                if (!_coupons.get(i).IsExpired) {
                                    num = num + 1;
                                }
                            }
                            mLabCoupons.setText(num + " 张可用优惠券");
                        }
                    } else {
                        if (mLabCoupons != null) {
                            mLabCoupons.setText("无可用优惠券");
                        }
                    }
                }


            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
