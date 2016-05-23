package org.csware.ee.consts;

/**
 * Created by Yu on 2015/6/4.
 */
public class ParamKey {

    /**
     * 订单ID
     */
    public static final String ORDER_ID = "ORDER_ID";

    /**用户ID*/
    public static final String USER_ID = "USER_ID";



    /**
     * 订单详情
     */
    public static final String ORDER_DETAIL = "ORDER_DETAIL";

    /**
     * Intent传参时的上一级Activity名称
     */
    public static final String BACK_TITLE = "BACK_TITLE";


    /**
     * 选中的地区字符串
     */
    public static final String AREA_STRING = "AREA_STRING";
    /**
     * 选中的城市CODE
     */
    public static final String AREA_CODE = "AREA_CODE";
    /**
     * 省，市，区三级字符串数组
     */
    public static final String AREA_ARRAY = "AREA_ARRAY";

    /**图片处理结果*/
    public static final String CROP_RESULT = "CROP_RESULT";

    /**支付价格*/
    public static final String PAY_PRICE = "PAY_PRICE";
    /**支付结果代码*/
    public static final String PAY_RESULT_CODE = "PAY_RESULT_CODE";



    public static final String MINE_HEADPIC_TEMP = "tmp_head.jpg";
    public static final String MINE_HEADPIC = "head.jpg";

    public static final String MINE_IDCARD_TEMP = "tmp_idcard.jpg";
    public static final String MINE_IDCARD = "idcard.jpg";


    /**用于网络持久化保存数据的ID*/
    public static final String MINE_HEADPIC_ID = "avator";

    public static final String FILE_PATH = "FILE_PATH";

    public static final String IMAGE_PATH = "IMAGE_PATH";
    /**裁剪后的文件保存位置Key*/
    public static final String CROPED_PATH = "CROPED_PATH";


    /**图像宽*/
    public static final String IMAGE_WIDTH = "IMAGE_WIDTH";
    /**图像高*/
    public static final String IMAGE_HEIGHT = "IMAGE_HEIGHT";


    public static final String DEVICE_ID = "DeviceId";

    public static final String TOKEN = "TOKEN_KEy";

    public static final String XG_MESSAGE_QUEUE = "XG_messageQueue";
    /**信鸽通知的Intent内容*/
    public static final String XG_INTENT_KEY = "XG_INTENT_KEY";

    public static final String XG_INTENT_ACTION = "org.csware.ee.shipper.XG_NOTIFY";


}
