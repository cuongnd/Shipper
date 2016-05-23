package org.csware.ee.consts;

import org.csware.ee.app.Tracer;

/**
 * Created by Atwind on 2015/5/14.
 * 访问网络API接口
 * 注意：接口只针对货主端使用
 */
public class API {

    public static boolean isInner() {
        return 1 == 1;
    }

    private final static String baseUrl_inner = "http://192.168.1.80:8001/owner/";
    private final static String Domain_Inner = "http://demo.xx3700.com/";
    private final static String Domain_net = "http://api.xx3700.com/";
    private final static String baseUrl_net = isInner() ? Domain_Inner + "owner/" : Domain_net + "owner/";
    private final static String baseUrl_pay = isInner() ? Domain_Inner + "" : Domain_net + "";
    //http://182.254.150.208/owner/
    private final static String baseUrl = Tracer.isUseDebugNet() ? baseUrl_inner : baseUrl_net;

    //FQA
    public static String FAQ = "http://mp.weixin.qq.com/s?__biz=MzIxMzAyMzEyOQ==&mid=401393022&idx=1&sn=35df2b1b957061e41a60f68fc69facf0&scene=1&srcid=1229d93WAj93SV4SN7BDEhWK#rd";
    //注册协议
    public static String REGISTER_PAPER = Domain_net + "Protocol.html";
    //保险协议
    public static String INSURANCE_PAPER = Domain_net + "Insurance.html";
    //信息显示
    public static String INFO = baseUrl + "info.ashx?";
    //意见反馈
    public static String FEEDBACK = baseUrl + "feedback.ashx?";
    //消息列表
    public static String MESSAGE = baseUrl + "message.ashx?";

    //图片轮播
    public static String FLASH = baseUrl + "slider.ashx?";
    /**
     * 扫码支付信息
     */
    public final static String SCANPAYINFO = baseUrl + "payeeinfo.ashx?";

    public static class USER {


        final static String subName = "user/";
        /**
         * Key验证
         */
        public final static String KEY_CHECK = baseUrl + subName + "keycheck.ashx?";
        /**
         * 服务器时间
         */
        public static String SERVICETIME = baseUrl + subName + "time.ashx?";

        /**
         * 上传推送机器标识
         */
        public final static String NOTIFY_TOKEN = baseUrl + subName + "notifytoken.ashx?";

        /**
         * 登录
         */
        public final static String SIGNIN = baseUrl + subName + "signin.ashx?";

        /**
         * 注册
         */
        public final static String SIGNUP = baseUrl + subName + "signup.ashx?";
        /**
         * 密码重置
         */
        public final static String RESET_PASSWORD = baseUrl + subName + "resetpassword.ashx?";
        /**
         * 密码重置短信
         */
        public final static String RESET_PASSWORD_SMS = baseUrl + subName + "resetpasswordsms.ashx?";
        /**
         * SMS短信发送
         */
        public final static String SIGNUP_SMS = baseUrl + subName + "signupsms.ashx?";

        /**
         * 编辑用户信息（目前只能改头像）
         */
        public final static String EDIT = baseUrl + subName + "edit.ashx?";

        /**
         * 身份验证接口
         */
        public final static String VERIFY = baseUrl + subName + "verify.ashx?";
        /**
         * 企业验证接口
         */
        public final static String ENERPRISE_VERIFY = baseUrl + subName + "verifycompany.ashx?";
        /**
         * 货主查看自己
         */
        public final static String RATING = baseUrl + "ownerrateinfo.ashx?";
        /**
         * 货主查看司机
         */
        public final static String BEARRATING = baseUrl + "bearerrateinfo.ashx?";

    }

    /**
     * 地址
     */
    public static class ADDRESS {

        final static String subName = "address/";
        /**
         * 地址编辑
         */
        public final static String EDIT = baseUrl + subName + "edit.ashx?";
        /**
         * 地址列表
         */
        public final static String LIST = baseUrl + subName + "list.ashx?";
    }

    /**
     * 地址车长
     */
    public static class CONFIG {
        final static String subName = "config/";
        /**
         * 版本号
         */
        public final static String VERSION = baseUrl + subName + "version.ashx?";
        /**
         * 地址
         */
        public final static String ADDRESS = baseUrl + subName + "areainfo.ashx?";
        /**
         * 车型车长
         */
        public final static String VehicleInfo = baseUrl + subName + "vehicleinfo.ashx?";
    }

    /**
     * 银行卡
     */
    public static class BANKCARD {

        final static String subName = "bankcard/";
        final static String recordName = "account/";

        /**
         * 钱包
         */
        public final static String ACCOUNT = baseUrl + subName + "account.ashx?";
        /**
         * 提现中数据
         */
        public final static String DRAWLIST = baseUrl + recordName + "drawlist.ashx?";

        /**
         * 账号编辑
         */
        public final static String ACCOUNTEDIT = baseUrl + subName + "accountedit.ashx?";

        /**
         * 列表
         */
        public final static String LIST = baseUrl + subName + "list.ashx?";
        /**
         * 银行卡编辑
         */
        public final static String EDIT = baseUrl + subName + "edit.ashx?";
        /**
         * 提现
         */
        public final static String DRAW = baseUrl + subName + "draw.ashx?";
        /**
         * 银行密码确认
         */
        public final static String CONFIRM = baseUrl + "huifu/register.ashx?";
        /**
         * 获取绑卡状态
         */
        public final static String BINDER = baseUrl + "huifu/info.ashx?";
        /**
         * 绑卡
         */
        public final static String BINDCARD = baseUrl + "huifu/bindcard.ashx?";
        /**
         * 支付订单
         */
        public final static String PAYORDER = baseUrl + "huifu/order.ashx?";
        /**
         * 支付代收货款
         */
        public final static String PAYCOLLECTION = baseUrl + "huifu/payee.ashx?";
        /**
         * 汇付卡删除
         */
        public final static String DELETECARD = baseUrl + "huifu/deletecard.ashx?";
        /**
         * 银行卡限额
         */
        public final static String BANKQUOTA = "http://demo.xx3700.com/BankList.html";
        /**
         * 汇付支付油卡
         */
        public final static String GASBACK = baseUrl + "huifu/gas.ashx?";
        /**
         * 交易明细列表
         */
        public final static String RECORD = baseUrl + recordName + "historylist.ashx?";
        /**
         * 交易明细详情
         */
        public final static String RECORDDETAIL = baseUrl + recordName + "historydetail.ashx?";
        /**
         * 余额支付 运费
         */
        public final static String BALANCEPAY = baseUrl + recordName + "order.ashx?";
        /**
         * 余额支付 代收货款
         */
        public final static String BALANCESCANPAY = baseUrl + recordName + "payee.ashx?";
        /**
         * 余额支付 油卡
         */
        public final static String BALANCEOILPAY = baseUrl + recordName + "gas.ashx?";
        /**
         * 油卡微信支付
         */
        public final static String OILWECHATPAY = baseUrl + "weixin/gas.ashx?";
        /**
         * 油卡充值详情
         */
        public final static String OILRECHARGE = isInner() ? "http://testoil.xx3700.com:92/api/oil/order/GetAppOrderDetail?" : "http://oil.xx3700.com:92/api/oil/order/GetAppOrderDetail?";
        /**
         * 油卡退款or赎回详情
         */
        public final static String OILBACK = isInner() ? "http://testoil.xx3700.com:92/api/oil/order/GetAppOrderReturn?" : "http://oil.xx3700.com:92/api/oil/order/GetAppOrderReturn?";
        /**
         * 优惠券列表
         */
        public final static String COUPONLIST = baseUrl + recordName + "coupon/list.ashx?";
        /**
         * 油卡优惠券
         */
        public final static String COUPONOIL = baseUrl + recordName + "coupon/list.ashx?";
        /**
         * 支付宝支付油卡
         */
        public final static String AlIPAYOIL = baseUrl + "alipay/gas.ashx?";
    }

    /**
     * 常用司机
     */
    public static class CONTACT {

        final static String subName = "contact/";

        public final static String EDIT = baseUrl + subName + "edit.ashx?";

        public final static String LIST = baseUrl + subName + "list.ashx?";

        final static String subNameChild = subName + "contact/";
        /**
         * 比对通信录返回结果
         */
        public final static String CONTRAST = baseUrl + subNameChild + "contact.ashx?";
        /**
         * 发送安装邀请
         */
        public final static String INVITE = baseUrl + subNameChild + "invite.ashx?";
        public final static String NOTIFY = baseUrl + subNameChild + "notify.ashx?";
    }

    /**
     * 竞价
     */
    public static class BID {

        final static String subName = "bid/";

        /**
         * 结束
         */
        public final static String CANCLE = baseUrl + subName + "cancle.ashx?";

        /**
         * 确认
         */
        public final static String CONFIRM = baseUrl + subName + "confirm.ashx?";

    }


    /**
     * 订单
     */
    public static class ORDER {
        final static String subName = "order/";
        /**
         * 订单支付Id
         */
        public final static String GETPAYID = baseUrl + subName + "payid.ashx?";
        /**
         * 订单代收支付Id
         */
        public final static String GETPAYEEID = baseUrl + subName + "payeeid.ashx?";
        /**
         * 先拿到OrderId
         */
        public final static String GETID = baseUrl + subName + "getid.ashx?";
        /**
         * 获取保险
         */
        public final static String INSURANCE = baseUrl + "insurancetype.ashx?";
        /**
         * 创建订单
         */
        public final static String CREATE = baseUrl + subName + "create.ashx?";


        /**
         * 订单详情
         */
        public final static String DETAIL = baseUrl + subName + "detail.ashx?";

        /**
         * 编辑订单
         */
        public final static String EDIT = baseUrl + subName + "edit.ashx?";


        /**
         * 订单列表
         */
        public final static String LIST = baseUrl + subName + "list.ashx?";

        /**
         * 订单废除
         */
        public final static String DELORDER = baseUrl + subName + "cancle.ashx?";


        /**
         * 货已装车并发车
         */
        public final static String PROMOTE = baseUrl + subName + "promote.ashx?";


        /**
         * 货主全部订单跟踪
         */
        public final static String TRACK_LIST = baseUrl + subName + "tracklist.ashx?";
        /**
         * 单个订单跟踪详情
         */
        public final static String TRACK = baseUrl + subName + "track.ashx?";
        /**
         * 评价
         * 订单信息相关里增加了字段OwnerRate BearerRate 类型Bool 为真表示某方已对该订单评价过
         */
        public final static String RATE = baseUrl + subName + "rate.ashx?";
        /**
         * 支付宝 服务器异步通知页面路径
         */
        public final static String ALIPAYCALLBACK = baseUrl_pay + subName + "orderpaycallback/alipay.ashx";
        public final static String ALIPAYCALLBACK_PAYEE = baseUrl_pay + subName + "payeepaycallback/alipay.ashx";
        //        public final static String ALIPAYCALLBACK = Domain_net+ subName + "paycallback/alipay.ashx";
        public final static String ALIPAYOIlCALLBACK = baseUrl_pay + subName + "gascallback/alipay.ashx";

        //http://192.168.1.82:8999/api/oil/Order/Callback?OrderNo=OO160324191437&Money=5980&CheckCode=bcd1b77d96a45ab9&From=1&SerialNo=345678&PayName=支付

        //        public final static String OILPAYCALLBACK = "http://testoil.xx3700.com:92/api/oil/Order/Callback?";
        public final static String OILPAYCALLBACK = isInner() ? "http://testoil.xx3700.com:92/api/oil/Order/Callback?" : "http://oil.xx3700.com:92/api/oil/Order/Callback?";
        //OrderNo=OO160324191437&Money=5980&CheckCode=bcd1b77d96a45ab9&From=1&SerialNo=345678&PayName=支付";
    }

    /**
     * 工具相关
     */
    public static class TOOL {
        final static String subName = "tool/";
        /**
         * 运价计算
         */
        public final static String FREIGHT_CALCULATION = baseUrl + subName + "calculateprice.ashx?";

        /**
         * 地理编码
         */
        public final static String GEOCODER = baseUrl + subName + "geocoder.ashx?";

    }


    /**
     * 工具相关
     */
    public static class UTILITY {
        final static String subName = "utility/";
        public final static String UPDATER = baseUrl + "version.ashx?";

        public static class QQ {


            public static class IMAGE {
                final static String subName = "utility/qq/image/";

                public final static String SIGN = baseUrl + subName + "sign.ashx?";
                //public final static String SIGN = "http://192.168.1.98/qcloud/sign.ashx?";


            }


            //TODO:CHANGING...
            public final static String IMAGE_CHECK_CALLBACK = baseUrl + subName + "qq/imagecheckcallback.ashx?";

            public final static String IMAGE_UPLOAD_CALLBACK = baseUrl + subName + "qq/imageuploadcallback.ashx?";

            public final static String SET_IMAGE = baseUrl + subName + "qq/setimage.ashx?";

        }

    }


    /**
     * 游戏
     */
    public static class GAME {
        final static String subName = "event/";
        final static String gameName = "games/";
        //刮刮卡
        public final static String SCRATCHCARD = baseUrl + subName + "scratchcard/info.ashx?";
        //转盘
        public final static String FORTUNEWHEEL = baseUrl + subName + "fortunewheel/info.ashx?";
        //红包
        public final static String REDPACKET = baseUrl + subName + "redpacket/info.ashx?";
        //积分明细
        public final static String SCORELOG = baseUrl + "bankcard/scorelog.ashx?";
        //游戏开放状态
        public final static String OPEN = baseUrl + subName + "gamelist.ashx?";
        //游戏    "http://192.168.1.73/RouletGameFlash/RouletGame.html?"
        public final static String GAMELIST = isInner() ? Domain_Inner + gameName : Domain_net + gameName;
        //积分规则
        public final static String SCOREROLE = "http://mp.weixin.qq.com/s?__biz=MzIxMzAyMzEyOQ==&mid=400671735&idx=1&sn=bd491374ec0de23768b1da11d020a0fa&scene=0#wechat_redirect";
    }

}
