package org.csware.ee.shipper.wxapi;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.tencent.mm.sdk.constants.ConstantsAPI;
import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.wxchat.sdk.pay.WxChatpayHelper;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserPayToBankActivity;
import org.csware.ee.shipper.UserPayToDriverActivity;
import org.csware.ee.shipper.UserPayToPlatformFragmentActivity;
import org.csware.ee.utils.Md5Encryption;

public class WXPayEntryActivity extends ActivityBase implements IWXAPIEventHandler {

    private static final String TAG = "WXPayEntryActivity";
    private IWXAPI api;
    //油卡信息
    String money, orederNo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, WxChatpayHelper.appid);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        Tracer.e(TAG, "onPayFinish, errCode = " + resp.errCode);
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            int code = resp.errCode;
            String msg = "";
            switch (code) {
                case 0:
                    msg = "支付成功！";
                    SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isRefresh", true).commit();
                    if (UserPayToDriverActivity.instance != null) {
                        UserPayToDriverActivity.instance.finish();
                    }
                    if (UserPayToPlatformFragmentActivity.instanse != null) {
                        UserPayToPlatformFragmentActivity.instanse.finish();
                    }
                    if (UserPayToBankActivity.instanse != null) {
                        UserPayToBankActivity.instanse.finish();
                    }
                    SharedPreferences getOilData = getSharedPreferences("isReqWeChat", MODE_PRIVATE);
                    money = getOilData.getString("money", "");//订单金额
                    orederNo = getOilData.getString("orderNo", "");//订单号
                    if (!Guard.isNullOrEmpty(money) && !Guard.isNullOrEmpty(orederNo)) {
                        //TODO  油卡支付成功调用后台回调，告知后台
                        payResultBack(baseActivity);
                        SharedPreferences pref = getSharedPreferences("RefreshOil", MODE_PRIVATE);
                        SharedPreferences.Editor ed = pref.edit();
                        ed.putBoolean("isRefresh", true).commit();
                    }
                    break;
                case -1:
                    msg = "支付失败！";
                    break;
                case -2:
                    msg = "您取消了支付！";
                    break;
                default:
                    msg = " 支付失败！";
                    break;
            }
            toastFast(msg);
            SharedPreferences preferences = getSharedPreferences("isReqWeChat", MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.clear().commit();
            WXPayEntryActivity.this.finish();
        }

    }

    void payResultBack(Context context) {
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
        callBack(baseActivity, context, params);

    }

    private static void callBack(Activity activity, Context context, HttpParams params) {

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
}