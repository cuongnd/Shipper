package com.alipay.sdk.pay;

import android.app.Activity;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;

import org.csware.ee.consts.API;
import org.csware.ee.utils.FormatHelper;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

/**
 *  TODO:回调地址必须填写，不然没法回调
 *
 * Created by Yu on 2015/7/8.
 */
public class AlipayHelper {


    //商户PID
    public static final String PARTNER = "2088911724528126";
    //商户收款账号
    public static final String SELLER = "lulang@xx3700.com";
    //商户私钥，pkcs8格式
    public static final String RSA_PRIVATE = "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBANXz1xRvWkDMgrm87AcjGuh9bUf96A+MpNr64Myq6Y6SzcV96CLVX6Z6dbAc8kVojd9Js87kk+imeVVbQ3bI5q7/eea0kniMPBGbMG4bdM9a3oEbcGdjQeU0PV5IaVg112hlzednJFk+9cKup5rgdVfwQndp5gJ7GRVH7v1xzNVlAgMBAAECgYEAy9KKmbsjPgwWlZ+3M21sGwQycZ0f0IBqJPtP4WsJKXwNcBFsbpLblpufNnG9FAZw8cEi/PyhM8g0Wu6grK7joyABDA2DFHP0+OccEUrsafpRF+axQwKoJqMxiqvyvWjUfW8PD2QUVqlqXD03dCbxi5fJyGuOxKiIGKxfeFUVI8ECQQD9m1h9iBoQS+11YfxvjAu3Ucafj3jlGJmp99MCZVoYGKx6PL8UKZqWv8Wi/QHYncF9MQvX8Zj1t5stett7w+h9AkEA1/iy8Ybt1esFPTLs0RecYzP0liHj5/YiUW6hQNwbdb0ReKhIEoowClgQSTrh/YKzUYva7fpIcULuyeGusLmdCQJANDKyLvxBaRNHp75oR8lKbAvv1s2f839xuVMh4j9cINOLRImWRp8di2OjWR28MIqf1ZWvu12lvwVwchb0b+/tvQJAU3MiWZcEUTMFjDcgME7KO764lEY2FMwTSJnRrwkdhynuFUwYxSkmFOkgKduZtOYKuciraGbcFP+C2vjcEic2WQJAT5NguEcLNXlwkxWlP5k1NszXOomdnuxw5xDBTSAFevZPOye+UEXQ3Vjeg+5/BkvBWNzdsghP744C6w5uc9cm/w==";
    //支付宝公钥
    public static final String RSA_PUBLIC = "";

    public static final int SDK_PAY_FLAG = 1;

    public static final int SDK_CHECK_FLAG = 2;




    /**
     * get the sdk version. 获取SDK版本号
     *
     */
    public static void getSDKVersion(Activity context) {
        PayTask payTask = new PayTask(context);
        String version = payTask.getVersion();
        Toast.makeText(context, version, Toast.LENGTH_SHORT).show();
    }


    /**
     * 获得定单支付信息
     * @param orderId
     * @param price
     * @return
     */
    public static String getOrderInfo(String orderId, double price, String subject, String userId) {
        return getOrderInfo(orderId+"", FormatHelper.toMoney(price),subject,userId, "");
    }

    /**
     * create the order info. 创建订单信息
     *
     */
    public static String getOrderInfo(String orderId, String price,String subject, String body, String callbackUrl) {
        // 签约合作者身份ID
        String orderInfo = "partner=" + "\"" + PARTNER + "\"";

        // 签约卖家支付宝账号
        orderInfo += "&seller_id=" + "\"" + SELLER + "\"";

        // 商户网站唯一订单号
        orderInfo += "&out_trade_no=" + "\"" + orderId + "\"";

        // 商品名称
        orderInfo += "&subject=" + "\"" + subject + "\"";

        // 商品详情
        orderInfo += "&body=" + "\"" + body + "\"";

        // 商品金额
        orderInfo += "&total_fee=" + "\"" + price + "\"";

        // 服务器异步通知页面路径  "http://api.xx3700.com/order/paycallback/alipay.ashx"
        orderInfo += "&notify_url=" + "\"" + callbackUrl
                + "\"";

        // 服务接口名称， 固定值
        orderInfo += "&service=\"mobile.securitypay.pay\"";

        // 支付类型， 固定值
        orderInfo += "&payment_type=\"1\"";

        // 参数编码， 固定值
        orderInfo += "&_input_charset=\"utf-8\"";

        // 设置未付款交易的超时时间
        // 默认30分钟，一旦超时，该笔交易就会自动被关闭。
        // 取值范围：1m～15d。
        // m-分钟，h-小时，d-天，1c-当天（无论交易何时创建，都在0点关闭）。
        // 该参数数值不接受小数点，如1.5h，可转换为90m。
        orderInfo += "&it_b_pay=\"30m\"";

        // extern_token为经过快登授权获取到的alipay_open_id,带上此参数用户将使用授权的账户进行支付
        // orderInfo += "&extern_token=" + "\"" + extern_token + "\"";

        // 支付宝处理完请求后，当前页面跳转到商户指定页面的路径，可空
        orderInfo += "&return_url=\"m.alipay.com\"";

        // 调用银行卡支付，需配置此参数，参与签名， 固定值 （需要签约《无线银行卡快捷支付》才能使用）
        // orderInfo += "&paymethod=\"expressGateway\"";

        return orderInfo;
    }

    /**
     * get the out_trade_no for an order. 生成商户订单号，该值在商户端应保持唯一（可自定义格式规范）
     *
     */
    public static String getOutTradeNo() {
        SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);

        Random r = new Random();
        key = key + r.nextInt();
        key = key.substring(0, 15);
        return key;
    }

    public static String getDateTime(){
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault());
        Date date = new Date();
        String key = format.format(date);
        return key;
    }

    /**
     * sign the order info. 对订单信息进行签名
     *
     * @param content
     *            待签名订单信息
     */
    public static String sign(String content) {
        return SignUtils.sign(content, RSA_PRIVATE);
    }

    /**
     * get the sign type we use. 获取签名方式
     *
     */
    public static String getSignType() {
        return "sign_type=\"RSA\"";
    }


}
