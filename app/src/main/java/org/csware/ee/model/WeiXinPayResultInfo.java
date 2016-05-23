package org.csware.ee.model;

/**
 * Created by Administrator on 2016/4/5.
 */
public class WeiXinPayResultInfo extends Return {
    public long Id;
    public Weixin Weixin;

    public static class Weixin {
        public String return_code;
        public String return_msg;
        public Object device_info;
        public String result_code;
        public Object err_code;
        public Object err_code_des;
        public String trade_type;
        public String appid;
        public String mch_id;
        public String nonce_str;
        public String sign;
        public String prepay_id;
        public String timestamp;
    }
}
