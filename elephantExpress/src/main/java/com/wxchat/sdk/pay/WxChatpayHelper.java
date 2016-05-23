package com.wxchat.sdk.pay;

import com.alipay.sdk.pay.SignUtils;

import org.csware.ee.app.Tracer;
import org.csware.ee.utils.Md5Encryption;

/**
 * Created by Administrator on 2016/3/30.
 */
public class WxChatpayHelper {

    //APPID
    public static String appid = "wx7d0824d1d60affec";
    //商户ID
    public static String partnerid = "1328556901";
    //扩展字段
    public static String packages = "Sign=WXPay";

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param noncestr
     *            待签名订单信息
     */
    public static String sign(String prepayid, String noncestr, String timestamp) {
        //参数appid,partnerid,prepayid,package,noncestr,timestamp
        String StringA = "",stringSignTemp = "";
        //appid
        StringA +="appid="+appid;
        //noncestr
        StringA +="&noncestr="+noncestr;
        //package
        StringA +="&package="+packages;
        //partnerid
        StringA +="&partnerid="+partnerid;
        //prepayid
        StringA +="&prepayid="+prepayid;
        //timestamp
        StringA +="&timestamp="+timestamp;
        Tracer.e("WxChatpay",StringA);
        stringSignTemp = StringA +"&key=6961b4944de609da0fbb8198f2bcfc8c";
        Tracer.e("WxChatpay",stringSignTemp);
        return Md5Encryption.MD5(stringSignTemp).toUpperCase();
    }

}
