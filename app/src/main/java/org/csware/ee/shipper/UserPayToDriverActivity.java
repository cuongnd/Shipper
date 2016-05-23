package org.csware.ee.shipper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.AlipayHelper;
import com.alipay.sdk.pay.PayResult;
import com.jungly.gridpasswordview.GridPasswordView;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.Code;
import org.csware.ee.model.GetIdReturn;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.PayeeScanModel;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.WeiXinPayResultInfo;
import org.csware.ee.shipper.fragment.UserWithdrawFragment;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2016/2/24.
 */
public class UserPayToDriverActivity extends ActivityBase {

    private static final String TAG = "UserPayToDriverActivity";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.imgDriverHead)
    ImageView imgDriverHead;
    @InjectView(R.id.txtDriverName)
    TextView txtDriverName;
    @InjectView(R.id.txtDriverPhone)
    TextView txtDriverPhone;
    @InjectView(R.id.optGoodsStyle)
    LinearLayout optGoodsStyle;
    @InjectView(R.id.txtMemo)
    EditText txtMemo;
    @InjectView(R.id.btnPressSay)
    Button btnPressSay;
    @InjectView(R.id.btnPhoto)
    ImageButton btnPhoto;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @InjectView(R.id.txtPayMethod)
    TextView txtPayMethod;
    @InjectView(R.id.txtPayChange)
    TextView txtPayChange;

    @InjectView(R.id.txtPayMoney)
    EditText txtPayMoney;

    long PayId, orderId;
    View contentView;
    DialogPlus dialog;
    GridPasswordView txtPassword;
    ProgressDialog progressDialog;
    public static Activity instance;
    Shipper shipper;
    DbCache dbCache;
    String subject = "", action = "";
    Handler mHandler;
    @InjectView(R.id.txtPayeeMoney)
    TextView txtPayeeMoney;
    @InjectView(R.id.LinBindOne)
    LinearLayout LinBindOne;
    @InjectView(R.id.fromAddress)
    TextView fromAddress;
    @InjectView(R.id.typeGoods)
    TextView typeGoods;
    private String payeeid = "", cardid = "";
    ChinaAreaHelper areaHelper;
    private String FromArea, ToArea;
    private IWXAPI api;
    int payMethod = 1;

    ViewHolder passHolder;
    DialogPlus passDialog;
    View passView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_to_driver);
        ButterKnife.inject(this);
        topBar.setTitle(getString(R.string.pay_to));
        instance = this;
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WxChatpayHelper.appid, false);
        api.registerApp(WxChatpayHelper.appid);
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class);
        shipper = dbCache.GetObject(Shipper.class);
        areaHelper = new ChinaAreaHelper(baseActivity);
        ac = dbCache.GetObject(AccountReturn.class);
        if (Guard.isNull(ac)){
            ac = new AccountReturn();
        }
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
        if (Guard.isNull(userInfo)) {
            userInfo = new MeReturn();
        }
        if (shipper.cardid > 0) {
            cardid = shipper.cardid + "";
        }
        mHandler = new alipayHandler(baseActivity, dbCache);
        init();
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

        Utils.setPricePoint(txtPayMoney);

    }

    @Override
    protected void onResume() {
        super.onResume();
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
        if (shipper.cardid > 0) {
            cardid = shipper.cardid + "";
        }
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

    private void getId(HttpParams params, final String messeage) {

        BackcardApi.getPayId(baseActivity, this, params, new IJsonResult<GetIdReturn>() {
            @Override
            public void ok(GetIdReturn json) {
                PayId = json.Id;
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                switch (payMethod){
                    case 1:
                        UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, payPrice + "", payToName, listener);
                        break;
                    case 2:
                        UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, payPrice + "", payToName, listener);
                        break;
                    case 3:
                        alipay(messeage);
                        break;
                }
//                if (paymethod == 1) {
//                    alipay(messeage);
//                } else {
//                    UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, payPrice + "", payToName, listener);
//                }
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
//                if (!Guard.isNull(json)) {
//                    if (json.Message.equals("货主状态错误")) {
//                        finish();
//                    }
//                }
            }
        });
    }

    void alipay(String messeage) {
        String msg = messeage + PayId;
//            String msg = "order_"+orderId;
        String callback = "";
        if (action.equals("scan")) {
            callback = API.ORDER.ALIPAYCALLBACK_PAYEE;
        } else {
            callback = API.ORDER.ALIPAYCALLBACK;
        }
        String orderInfo = AlipayHelper.getOrderInfo(msg, payPrice, subject, userInfo.Owner.UserId + "", callback);
        Tracer.e("payToDriver", callback + " " + msg + " id:" + PayId + "\n" + orderInfo);
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

    String payToName = "";
    String payPrice = "";
    String payMemo = "";

    private void init() {
        String info = getIntent().getStringExtra("info");
        action = getIntent().getStringExtra("action");
        if (action.equals("scan")) {
            payeeid = getIntent().getStringExtra("payeeid");
            subject = getIntent().getStringExtra("subject");
            getPayee();
//            txtPayeeMoney.setText("");
        } else {
            if (!Guard.isNullOrEmpty(info)) {
                OrderDetailChangeReturn result = GsonHelper.fromJson(info, OrderDetailChangeReturn.class);
                OrderDetailChangeReturn.OrderEntity orderEntity = result.Order;
                orderId = orderEntity.Id;
                subject = orderEntity.GoodsType;

                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Avatar)) {
                    ImageLoader.getInstance().displayImage(orderEntity.BearerUser.Avatar, imgDriverHead);
                }

                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
                    txtDriverPhone.setText(orderEntity.BearerUser.Mobile);
                }
                if (!Guard.isNull(orderEntity.BearerCompany) && orderEntity.BearerUser.CompanyStatus == 1) {
                    txtDriverName.setText(orderEntity.BearerCompany.CompanyName + "");
                    payToName = orderEntity.BearerCompany.CompanyName;
                } else {
                    if (!Guard.isNullOrEmpty(orderEntity.Bearer.Name)) {
                        txtDriverName.setText(orderEntity.Bearer.Name);
                        payToName = orderEntity.Bearer.Name;
                    }
                }

                if (!Guard.isNullOrEmpty(orderEntity.DealPrice + "")) {
                    txtPayMoney.setText(FormatHelper.toMoney(orderEntity.DealPrice) + "");
                }

                if (!Guard.isNullOrEmpty(orderEntity.From + "")) {
                    FromArea = areaHelper.getProvinceName(orderEntity.From + "");

                }

                if (!Guard.isNullOrEmpty(orderEntity.To + "")) {
                    if (!Guard.isNull(orderEntity.To)) {
                        ToArea = areaHelper.getProvinceName(orderEntity.To + "");
                    }else {
                        ToArea = "未知";
                    }
//                    String pId = areaHelper.getProvinceID(orderEntity.To+"");
//                    Tracer.e("PayTodriver","To:"+orderEntity.To+" pId:"+pId);
                } else {
                    if (!Guard.isNull(orderEntity.Payees)) {
                        if (orderEntity.Payees.size() > 0) {
                            String Areas = "";
                            if (!Guard.isNull(orderEntity.Payees.get(0).Area)) {
                                Areas = orderEntity.Payees.get(0).Area + "";
                            }
                            if (!Guard.isNullOrEmpty(Areas)) {
                                ToArea = areaHelper.getProvinceName(orderEntity.Payees.get(0).Area + "");
                            }else {
                                ToArea = "未知";
                            }

                        }
                    }
                }

                fromAddress.setText(FromArea + " 至 " + ToArea);
                typeGoods.setText(orderEntity.GoodsType + orderEntity.GoodsAmount + orderEntity.GoodsUnit);

            }
        }
        if (!Guard.isNullOrEmpty(shipper.payment)) {
            txtPayMethod.setText(shipper.payment);
        }
    }

    void getPayee() {
        progressDialog = Tools.getDialog(baseActivity);
        progressDialog.setCanceledOnTouchOutside(false);
        HttpParams pms = new HttpParams(API.SCANPAYINFO);
        pms.addParam("payeeid", payeeid);
        BackcardApi.scanpayInfo(baseActivity, this, pms, new IJsonResult<PayeeScanModel>() {
            @Override
            public void ok(PayeeScanModel json) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
                String payToMobile = "";
                if (!Guard.isNull(json)) {
                    payPrice = FormatHelper.toMoney(json.Payee.Price) + "";
                    payToName = json.Payee.Name + "";
                    if (!Guard.isNull(json.Order.OwnerUser)) {
                        if (!Guard.isNull(json.Order.OwnerUser.Mobile)) {
                            payToMobile = json.Order.OwnerUser.Mobile;
                        }
                        if (!Guard.isNullOrEmpty(json.Order.OwnerUser.Avatar)) {
                            ImageLoader.getInstance().displayImage(json.Order.OwnerUser.Avatar, imgDriverHead);
                        }
                        if (!Guard.isNull(json.Order.OwnerUser.CompanyStatus)) {
                            if (json.Order.OwnerUser.CompanyStatus == 1) {
                                if (!Guard.isNull(json.Order.OwnerCompany)) {
                                    if (!Guard.isNullOrEmpty(json.Order.OwnerCompany.CompanyName)) {
                                        payToName = json.Order.OwnerCompany.CompanyName;
                                    }
                                }
                            } else {
                                if (!Guard.isNull(json.Order.Owner)) {
                                    if (!Guard.isNullOrEmpty(json.Order.Owner.Name)) {
                                        payToName = json.Order.Owner.Name;
                                    }
                                }
                            }
                        }
                    }
                    txtDriverName.setText(payToName);
                    txtDriverPhone.setText(payToMobile + "");
                    txtPayMoney.setText(payPrice);
                    if (action.equals("scan")) {
                        subject = json.Order.GoodsType;
                        if (json.Payee.PayerBonus > 0) {
                            String RewardPrice = FormatHelper.toMoney(json.Payee.PayerBonus);
                            txtPayeeMoney.setText("代收货款奖励金额" + RewardPrice + getString(R.string.unit_rmb));
                            LinBindOne.setVisibility(View.VISIBLE);

                        }
                    }
                    if (!Guard.isNullOrEmpty(json.Order.From + "")) {

                        FromArea = areaHelper.getProvinceName(json.Order.From + "");
                    }
                    if (!Guard.isNullOrEmpty(json.Payee.Area + "")) {

                        ToArea = areaHelper.getProvinceName(json.Payee.Area + "");
                    }

                    fromAddress.setText(FromArea + " 至 " + ToArea);
                    typeGoods.setText(json.Order.GoodsType + json.Order.GoodsAmount + json.Order.GoodsUnit);
                }
            }

            @Override
            public void error(Return json) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    @OnClick(R.id.txtPayChange)
    void setTxtPayChange() {
        Intent intent = new Intent(this, UserPayMethodActivity.class);
        double payP = 0.0;
        if (!Guard.isNullOrEmpty(txtPayMoney.getText().toString())) {
            payP = Double.parseDouble(txtPayMoney.getText().toString());
        }
        intent.putExtra("payPrice", payP);
        startActivityForResult(intent, 100);
    }

    String payment = "";
    MeReturn userInfo;
    AccountReturn ac;

    @OnClick(R.id.btnConfirm)
    void setBtnConfirm() {
        payPrice = txtPayMoney.getText().toString().trim();
        payMemo = txtMemo.getText().toString().trim() + "";
        if (Guard.isNullOrEmpty(payPrice)) {
            toastFast("请输入支付金额");
            return;
        }
//        if (Guard.isNullOrEmpty(payMemo)) {
//            toastFast("请输入备注信息");
//            return;
//        }
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }

        final HashMap<String, String> map = new HashMap<>();


        if (!Guard.isNullOrEmpty(shipper.payment)) {
            Tracer.e("payToDriver", shipper.payment + "");
            payment = shipper.payment;
            if (payment.contains("支付宝")) {
                payMethod = 3;
                if (action.equals("scan")) {
                    progressDialog = Tools.getDialog(baseActivity);
                    progressDialog.setCanceledOnTouchOutside(false);
                    HttpParams params = new HttpParams(API.ORDER.GETPAYEEID);
                    params.addParam("payeeid", payeeid);
                    params.addParam("message", payMemo + " ");
                    params.addParam("price", payPrice);
                    params.addParam("paymethod", "alipay");
                    String message = "payeepay_";
                    map.put("payeeid", payeeid + "");
                    getId(params, message);
                } else if (action.equals("freight")) {
                    progressDialog = Tools.getDialog(baseActivity);
                    progressDialog.setCanceledOnTouchOutside(false);
                    HttpParams params = new HttpParams(API.ORDER.GETPAYID);
                    params.addParam("orderid", orderId);
                    params.addParam("message", payMemo + " ");
                    params.addParam("price", payPrice);
                    params.addParam("paymethod", "alipay");
                    String message = "orderpay_";
                    map.put("orderid", orderId + "");
                    getId(params, message);
                }
            } else if (payment.contains("微信")) {

                progressDialog = Tools.getDialog(baseActivity);
                progressDialog.setCanceledOnTouchOutside(false);
                HttpParams params = new HttpParams(API.ORDER.GETPAYID);
                params.addParam("orderid", orderId);
                params.addParam("message", "");
                params.addParam("price", payPrice);
                params.addParam("paymethod", "weixin");
                map.put("orderid", orderId + "");
                getPrepayId(params);
            } else if (payment.contains("银行") || shipper.payment.contains("银行")) {
                payMethod = 1;
                if (action.equals("scan")) {
                    progressDialog = Tools.getDialog(baseActivity);
                    progressDialog.setCanceledOnTouchOutside(false);
                    HttpParams params = new HttpParams(API.ORDER.GETPAYEEID);
                    params.addParam("payeeid", payeeid);
                    params.addParam("message", payMemo + " ");
                    params.addParam("price", payPrice);
                    params.addParam("paymethod", "huifu");
                    String message = "payeepay_";
                    map.put("payeeid", payeeid + "");
                    getId(params, message);
                } else if (action.equals("freight")) {
                    progressDialog = Tools.getDialog(baseActivity);
                    progressDialog.setCanceledOnTouchOutside(false);
                    HttpParams params = new HttpParams(API.ORDER.GETPAYID);
                    params.addParam("orderid", orderId);
                    params.addParam("message", payMemo + " ");
                    params.addParam("price", payPrice);
                    params.addParam("paymethod", "huifu");
                    String message = "orderpay_";
                    map.put("orderid", orderId + "");
                    getId(params, message);
                }
//                UserWithdrawFragment.popPasswordBox(baseActivity, dialog, contentView, payPrice + "", payToName, listener);
            } else if (payment.contains("余额")) {
                if (!Guard.isNull(ac.Account)) {
                    if (!Guard.isNullOrEmpty(ac.Account.Password)) {
                        balancePay(map);
                    }else {
                        createView(getString(R.string.tip_first_withdraw));
                    }
                }else {
                    asyncLoadAccount();
                }
            } else {
                toastFast("请选择付款方式");
            }
            map.put("price", payPrice + "");
            MobclickAgent.onEvent(baseActivity, "myorder_payEMS_payConfirm", map);
        } else {
            toastFast("请选择付款方式");
        }

    }

    void createView(String title){
        UserWithdrawFragment.popPasswordBox(baseActivity, title, okListener, cancelListener, passDialog, passView);
    }

    void resetPassView(boolean isHide){
        final GridPasswordView txtPassword = (GridPasswordView) passView.findViewById(R.id.txtPassword);
        txtPassword.clearPassword();
        if (isHide) {
            Tools.hideInput(baseActivity, txtPassword);
        }
    }
    int viewStatus = 1;
    String firstPass = "", confirmPass = "", finalActStr= "";

    View.OnClickListener cancelListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            viewStatus = 1;
            resetPassView(true);
            passDialog.dismiss();
        }
    };
    View.OnClickListener okListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            final GridPasswordView txtPassword = (GridPasswordView) passView.findViewById(R.id.txtPassword);
            String pass = txtPassword.getPassWord();
            if (!Guard.isNullOrEmpty(pass)) {
                if (pass.length()==6) {
                    txtPassword.clearPassword();
                    if (viewStatus==1) {
                        viewStatus = 2;
                        firstPass = pass;
                        createView(getString(R.string.tip_first_confirm));
                    }else if (viewStatus==2){
                        viewStatus=1;
                        confirmPass = pass;
                        if (firstPass.equals(confirmPass)) {
                            createPass(confirmPass);
                        }else {
                            viewStatus=2;
                            txtPassword.clearPassword();
                            toastFast("两次密码不一致，请重新输入");
                        }
                    }else if (viewStatus ==3){
                        resetPassView(true);
                        passDialog.dismiss();
                        //准备支付
                        HashMap<String, String> map = new HashMap<>();
                        balancePay(map);
                    }
                }else {
                    toastFast("请输入正确的密码");
                }
            }else {
                toastFast("密码不能为空");
            }
        }
    };

    void createPass(String password){
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

    void balancePay(HashMap<String, String> map){
        payMethod = 2;
        if (action.equals("scan")) {
            progressDialog = Tools.getDialog(baseActivity);
            progressDialog.setCanceledOnTouchOutside(false);
            HttpParams params = new HttpParams(API.ORDER.GETPAYEEID);
            params.addParam("payeeid", payeeid);
            params.addParam("message", payMemo + " ");
            params.addParam("price", payPrice);
            params.addParam("paymethod", "account");
            map.put("payeeid", payeeid + "");
            getId(params, "");
        } else if (action.equals("freight")) {
            progressDialog = Tools.getDialog(baseActivity);
            progressDialog.setCanceledOnTouchOutside(false);
            HttpParams params = new HttpParams(API.ORDER.GETPAYID);
            params.addParam("orderid", orderId);
            params.addParam("message", payMemo + " ");
            params.addParam("price", payPrice);
            params.addParam("paymethod", "account");
            map.put("orderid", orderId + "");
            getId(params, "");
        }
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
                SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isRefresh", true).commit();
                Intent intent = new Intent(UserPayToDriverActivity.this, PayResultActivity.class);
                intent.putExtra("RewardPrice", "");
                intent.putExtra("price", payPrice);
                intent.putExtra("isSuccess", true);
                startActivity(intent);
            }

            @Override
            public void error(Return json) {
                if (progressDialog != null) {
                    if (progressDialog.isShowing()) {
                        progressDialog.dismiss();
                    }
                }
            }
        });
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            String pwd = txtPassword.getPassWord();
            if (!Guard.isNullOrEmpty(pwd)) {
                if (pwd.length() == 6) {
                    txtPassword.clearPassword();
                    dialog.dismiss();
                    Tools.hideInput(baseActivity, txtPassword);
                    switch (payMethod){
                        case 1:
                            asyncDraw(pwd);
                            break;
                        case 2:
                            asyncBalanceDraw(pwd);
                            break;
                    }
                } else {
                    toastFast("请输入正确的密码");
                }
            } else {
                toastFast("密码不能为空");
            }
        }
    };

    private void asyncDraw(String password) {
        txtPassword.clearPassword();
        //TODO 支付接口 成功失败跳转
        progressDialog = Tools.getDialog(baseActivity);
        progressDialog.setCanceledOnTouchOutside(false);
        if (action.equals("scan")) {
            HttpParams pms = new HttpParams(API.BANKCARD.PAYCOLLECTION);
            pms.addParam("password", password);
            pms.addParam("payeeid", payeeid);
            pms.addParam("payeepayid", PayId);
//            pms.addParam("price", payPrice);
//            pms.addParam("message", payMemo);
            pms.addParam("cardid", cardid);
            asyncPayBank(pms);
        } else if (action.equals("freight")) {
            HttpParams pms = new HttpParams(API.BANKCARD.PAYORDER);
            pms.addParam("password", password);
            pms.addParam("orderid", orderId);
            pms.addParam("orderpayid", PayId);
            pms.addParam("cardid", cardid);
//            pms.addParam("message", payMemo);
//            pms.addParam("price", payPrice);
            asyncPayBank(pms);
        }
    }

    private void asyncBalanceDraw(String password) {
        txtPassword.clearPassword();
        //TODO 支付接口 成功失败跳转
        progressDialog = Tools.getDialog(baseActivity);
        progressDialog.setCanceledOnTouchOutside(false);
        if (action.equals("scan")) {
            HttpParams pms = new HttpParams(API.BANKCARD.BALANCESCANPAY);
            pms.addParam("password", password);
            pms.addParam("id", PayId);
            asyncPayBank(pms);
        } else if (action.equals("freight")) {
            HttpParams pms = new HttpParams(API.BANKCARD.BALANCEPAY);
            pms.addParam("password", password);
            pms.addParam("id", PayId);
            asyncPayBank(pms);
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
                    balancePay(new HashMap<String, String>());
                }else {
                    createView(getString(R.string.tip_first_withdraw));
                }
            }

            @Override
            public void error(Return json) {

            }
        });


    }

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
                        Toast.makeText(context, "支付成功",
                                Toast.LENGTH_SHORT).show();
                        Shipper shipper = dbCache.GetObject(Shipper.class);
                        shipper.payment = "使用\t\t支付宝\t\t付款";
                        shipper.havedPayMethod = true;
                        dbCache.save(shipper);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Code.OK.toValue()) {
            if (requestCode == 100) {
                payment = data.getStringExtra("payment");
                if (payment.contains("银行")) {
                    int Id = -1;
                    if (!Guard.isNull(data)) {
                        if (!Guard.isNull(data.getIntExtra("cardid", -1))) {
                            Id = data.getIntExtra("cardid", -1);
                        }
                    }
                    if (Id > -1) {
                        cardid = Id + "";
                    } else {
                        toastFast("获取银行卡失败");
                    }
                }
                txtPayMethod.setText(payment);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
