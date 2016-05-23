package org.csware.ee.shipper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.alipay.sdk.pay.AlipayHelper;
import com.alipay.sdk.pay.PayResult;
import com.jungly.gridpasswordview.GridPasswordView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.analytics.MobclickAgent;
import com.wxchat.sdk.pay.WxChatpayHelper;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.PayeeScanModel;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.shipper.fragment.UserWithdrawFragment;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 付款至平台
 */
public class UserPayToPlatformFragmentActivity extends FragmentActivityBase {

    final static String TAG = "UserPayToPlatformAct";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.labPrice)
    TextView labPrice;
    @InjectView(R.id.ivAplipay)
    ImageView ivAplipay;
    @InjectView(R.id.boxAlipay)
    LinearLayout boxAlipay;
    @InjectView(R.id.ivWXpay)
    ImageView ivWXpay;
    @InjectView(R.id.boxWX)
    LinearLayout boxWX;
    @InjectView(R.id.ivUnionpay)
    ImageView ivUnionpay;
    @InjectView(R.id.boxUnionpay)
    LinearLayout boxUnionpay;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @InjectView(R.id.gridview_bank)
    GridView gridviewBank;
    @InjectView(R.id.Lin_bankList)
    LinearLayout LinBankList;
    String subject = "";
    DbCache dbCache;
    //    List<BankModel> bankList = new ArrayList<>();
//    CommonAdapter<BankModel> adapter;
    public static Activity instanse;
    String action, payeeid;
    ProgressDialog progressDialog;
    @InjectView(R.id.imgBalanceIcon)
    ImageView imgBalanceIcon;
    @InjectView(R.id.txtBalance)
    TextView txtBalance;
    @InjectView(R.id.ivBalancePay)
    ImageView ivBalancePay;
    @InjectView(R.id.boxBalance)
    LinearLayout boxBalance;
    private long PayId;

    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_pay_to_platform_fragment_activit);
        ButterKnife.inject(this);
        dbCache = new DbCache(baseActivity);
        instanse = this;
        // 通过WXAPIFactory工厂，获取IWXAPI的实例
        api = WXAPIFactory.createWXAPI(this, WxChatpayHelper.appid, false);
        api.registerApp(WxChatpayHelper.appid);
        init();
    }


    long orderId;
    double price;
    double balance = 0.0;
    String money;
    String rptJson = "";
    Drawable none;
    Drawable ok;
    AccountReturn ac;
    BindStatusInfo bankinfo;
    OilCardInfo info;
    boolean isPay = false;
    ViewHolder holder;
    DialogPlus passDialog;
    View contentView;

    void init() {
        asyncLoadInfo();
        ac = dbCache.GetObject(AccountReturn.class);
        if (ac == null) {
            ac = new AccountReturn();
        }
        //油卡界面的信息
        info = dbCache.GetObject(OilCardInfo.class);
        if (Guard.isNull(info)) {
            info = new OilCardInfo();
        }

        Intent intent = getIntent();
        orderId = intent.getLongExtra(ParamKey.ORDER_ID, 0);
        price = intent.getDoubleExtra(ParamKey.PAY_PRICE, 0.0);
        rptJson = intent.getStringExtra("info");
        subject = intent.getStringExtra("subject");
        action = intent.getStringExtra("action");
        btnConfirm.setTag(3);
//        Log.i(TAG, "orderId:" + orderId + "  price:" + price);
        if (!Guard.isNullOrEmpty(action) && action.equals("scan")) {
            payeeid = intent.getStringExtra("payeeid");
            getPayee();
        } else if (!Guard.isNullOrEmpty(action) && action.equals("Oil")) {
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
                boxUnionpay.setVisibility(View.GONE);
                LinBankList.setVisibility(View.GONE);
                boxBalance.setVisibility(View.GONE);
            }
            money = info.getMoney();
            labPrice.setText(money);
        } else {
            labPrice.setText(FormatHelper.toMoney(price));
        }
        asyncLoadAccount();

//        else {
//            txtBalance.setText(balance+"元");
//        }
        none = getResources().getDrawable(R.drawable.wyfh_dz_icon_djmr);
        ok = getResources().getDrawable(R.drawable.wyfh_dz_icon_mr);

        mHandler = new alipayHandler(baseActivity, dbCache);

        holder = new ViewHolder(R.layout.dialog_set_pay_pwd);
        passDialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(DialogPlus.Gravity.CENTER)
                .create();
        contentView = passDialog.getHolderView();

    }

    /**
     * 代收货款扫描后获取的信息
     */
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
                if (!Guard.isNull(json)) {
//                    payToName = json.Payee.Name+"";
//                    txtDriverName.setText(payToName);
//                    txtDriverPhone.setText(json.Payee.Mobile+"");
//                    txtPayMoney.setText(payPrice);
                    subject = json.Order.GoodsType;
                    labPrice.setText(FormatHelper.toMoney(json.Payee.Price));
                    if (action.equals("scan")) {
                        if (json.Payee.PayerBonus > 0) {
                            String RewardPrice = FormatHelper.toMoney(json.Payee.PayerBonus);
//                            txtPayeeMoney.setText("代收货款奖励金额"+RewardPrice+getString(R.string.unit_rmb));
//                            LinBindOne.setVisibility(View.VISIBLE);
                        }
                    }
//                    txtRecName.setText(payToName);
//                    String fromAddress = areaHelper.getProvinceName(json.Order.From + "");
//                    String toAddress = areaHelper.getProvinceName(json.Payee.Area + "");
//                    txtAddress.setText(fromAddress + "到" + toAddress);
//                    txtGoodsType.setText(json.Order.GoodsType + " " + json.Order.GoodsAmount + " " + json.Order.GoodsUnit);
//                    txtCreateTime.setText(ParamTool.fromTimeSeconds(json.Order.CreateTime, "yyyy-MM-dd HH:mm:ss"));
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

    /**
     * check whether the device has authentication alipay account.
     * 查询终端设备是否存在支付宝认证账户
     */
    public void check(View v) {
        Runnable checkRunnable = new Runnable() {

            @Override
            public void run() {
                // 构造PayTask 对象
                PayTask payTask = new PayTask(baseActivity);
                // 调用查询接口，获取查询结果
                boolean isExist = payTask.checkAccountIfExist();

                Message msg = new Message();
                msg.what = AlipayHelper.SDK_CHECK_FLAG;
                msg.obj = isExist;
                mHandler.sendMessage(msg);
            }
        };

        Thread checkThread = new Thread(checkRunnable);
        checkThread.start();

    }

    // Intent intent;
    Handler mHandler;

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

                        //TODO:支付成功后，关闭支付页面，返回订单详情页面，修改订单详情的支付状态。提示等待司机上门取货。
                        Intent intent = new Intent();
                        //intent.putExtra(ParamKey.PAY_RESULT_CODE, Code.OK.toValue());

                        context.setResult(Code.OK.toValue(), intent);
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

        ;
    }


    void reset() {

        ivAplipay.setImageDrawable(none);
        ivWXpay.setImageDrawable(none);
        ivUnionpay.setImageDrawable(none);
        ivBalancePay.setImageDrawable(none);
        LinBankList.setVisibility(View.GONE);
        btnConfirm.setTag(3); //2微信，3银行， //4余额
    }

    @OnClick({R.id.boxAlipay, R.id.boxWX, R.id.boxUnionpay, R.id.boxBalance})
    void boxSelected(View v) {


        switch (v.getId()) {
            case R.id.boxAlipay:
                reset();
                ivAplipay.setImageDrawable(ok);
                btnConfirm.setTag(1);
                break;
            case R.id.boxWX:
                reset();
                ivWXpay.setImageDrawable(ok);
                btnConfirm.setTag(2);
                break;
            case R.id.boxUnionpay:
                reset();
                ivUnionpay.setImageDrawable(ok);
                LinBankList.setVisibility(View.VISIBLE);
                btnConfirm.setTag(3);
                break;
            case R.id.boxBalance:
                if (isPay) {
                    reset();
                    ivBalancePay.setImageDrawable(ok);
                    btnConfirm.setTag(4);
                }
                break;
//            default:
//                ivUnionpay.setImageDrawable(ok);
//                btnConfirm.setTag(3);
        }
    }

    ProgressDialog dialog;

    public static List<PackageInfo> getAllApps(Context context) {
        List<PackageInfo> apps = new ArrayList<PackageInfo>();
        PackageManager pManager = context.getPackageManager();
        //获取手机内所有应用
        List<PackageInfo> paklist = pManager.getInstalledPackages(0);
        for (int i = 0; i < paklist.size(); i++) {
            PackageInfo pak = (PackageInfo) paklist.get(i);
            //判断是否为非系统预装的应用程序
            if ((pak.applicationInfo.flags & pak.applicationInfo.FLAG_SYSTEM) <= 0) {
                // customs applications
                apps.add(pak);
            }
        }
        return apps;
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

    @OnClick({R.id.btnConfirm})
    void payGo(View v) {

        String actStr = "";
        if (Guard.isNullOrEmpty(action)) {
            actStr = "freight";
        } else {
            actStr = action;
        }
        if (actStr.equals("freight")) {
            if (orderId < 1 || price <= 0) {
                toastFast("支付数据错误，请联系我们！");
                return;
            }
        }
        int tag = ParamTool.toInt(btnConfirm.getTag());

//        Log.d(TAG, "tagVlaue:" + tag);
        if (tag == 2) {
            if (isWXAppInstalledAndSupported()) {
                //TODO:微信支付
                String payMethod = "使用\t\t微信\t\t付款";
                jumpToPay(actStr, payMethod);
            } else {
                toastFast("未安装微信，提交失败。请尝试使用其他支付方式。");
            }
        } else if (tag == 1) {
            //支付宝支付
            //check(v);
            String payMethod = "使用\t\t支付宝\t\t付款";
            jumpToPay(actStr, payMethod);
        } else if (tag == 4){
            if (isPay) {
                ac = dbCache.GetObject(AccountReturn.class);
                if (Guard.isNull(ac)){
                    ac = new AccountReturn();
                }
                if (!Guard.isNull(ac.Account)) {
                    if (!Guard.isNullOrEmpty(ac.Account.Password)) {
                        String payMethod = "使用\t\t余额\t\t付款";
                        jumpToPay(actStr, payMethod);
                    }else {
                        String payMethod = "使用\t\t余额\t\t付款";
                        jumpToPay(actStr, payMethod);
                    }
                }
            }else {
                toastFast("余额不足");
            }
        }
        else if (tag == 3){

            HashMap<String, String> map = new HashMap<>();
            map.put("orderId", orderId + "");
            map.put("action", "add bankcard");
            MobclickAgent.onEvent(baseActivity, "myorder_payEMS_button", map);

            Intent intent = new Intent(this, BindBankCardActivity.class);
            if (actStr.equals("scan")) {
                intent.putExtra("payeeid", payeeid);
            }
            intent.putExtra("action", actStr);
            intent.putExtra("orderId", orderId);
            intent.putExtra("price", price);
            intent.putExtra("info", rptJson);
            intent.putExtra("mode", "pay");
            startActivity(intent);
        }else {
            toastFast("请选择一种支付方式");
        }

    }

    void jumpToPay(String actStr, String payMethod) {
        if (actStr.equals("Oil")) {
            Shipper shipper = dbCache.GetObject(Shipper.class);
            shipper.payment = payMethod;
            shipper.havedPayMethod = true;
            dbCache.save(shipper);
            Intent intent = new Intent(this, UserPayToBankActivity.class);
            intent.putExtra("action", actStr);
            startActivity(intent);
        } else {
            // 订单
            SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isRefresh", true).commit();
            Shipper shipper = dbCache.GetObject(Shipper.class);
            shipper.payment = payMethod;
            shipper.havedPayMethod = true;
            dbCache.save(shipper);
            Intent intent = new Intent(this, UserPayToDriverActivity.class);
            intent.putExtra("action", actStr);
            if (actStr.equals("scan")) {
                intent.putExtra("payeeid", payeeid);
                intent.putExtra("subject", subject);
            }
            intent.putExtra("info", rptJson);
            startActivity(intent);
        }
    }

    MeReturn userInfo;
    final Handler myHandler = new Handler();
    void asyncLoadInfo(){

        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                userInfo = json;
                dbCache.save(userInfo);
            }

            @Override
            public void error(Return json) {

            }
        });
    }
    void asyncLoadAccount() {

        BackcardApi.account(baseActivity, baseActivity, new IJsonResult<AccountReturn>() {
            @Override
            public void ok(AccountReturn json) {
                //成功
                ac = json;
                dbCache.save(json);

                myHandler.post(new Runnable() {
                    public void run() {
                        if (!Guard.isNull(ac)){
                            if (!Guard.isNull(ac.Account)){
                                if (!Guard.isNull(ac.Account.Amount)){
                                    if (ac.Account.Amount>0){
                                        balance = Double.valueOf(FormatHelper.toMoney(ac.Account.Amount));
                                    }
                                }
                            }
                        }
                        if (!Guard.isNullOrEmpty(labPrice.getText().toString())){
                            if (Double.valueOf(labPrice.getText().toString())>balance){
                                imgBalanceIcon.setImageResource(R.mipmap.fkm_icon_wallet_unselected);
                                txtBalance.setText(balance+"元(余额不足)");
                                ivBalancePay.setVisibility(View.INVISIBLE);
                                isPay = false;
                            }else {
                                isPay = true;
                                txtBalance.setText(balance+"元");
                            }
                        }
                    }
                });
            }

            @Override
            public void error(Return json) {

            }
        });


    }

}
