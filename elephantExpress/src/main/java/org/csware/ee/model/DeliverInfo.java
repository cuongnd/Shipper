package org.csware.ee.model;

/**
 * Created by Yu on 2015/5/26.
 * 发货订单相关消息
 */
public class DeliverInfo {


    public DeliverInfo() {

    }


    /**
     * 货物类型
     */
    public String goodsStyle;
    /**
     * 货物单位
     */
    public String goodsUnit;
    /**
     * 货物数量
     */
    public String amount;
    /**
     * 配送类型
     */
    public String transStyle;
    /**
     * 车型
     */
    public String truckStyle;
    /**
     * 车长 改为固定的类型
     */
    public String truckLength;
    /**
     * 开始时间
     */
    public int beginTime;
    /**
     * 结束时间
     */
    public int endTime;

    /**
     * 报价类型
     */
    public String priceStyle;
    /**
     * 报价价格
     */
    public double price;

    /**
     * 发票类型
     */
    public String invoiceStyle;
    /**付款类型*/
    public int payPoint;
    /**
     * 付款方式
     */
    public int payMethod;
    /**
     * 备注
     */
    public String memo;

    /**
     * 收货人
     */
    public String name;
    /**
     * 收货人电话
     */
    public String phone;
    /**
     * 保险
     * */
    public String insurance;
    public String fromId;

    public String fromDetail;

    public String toId;

    public String toDetail;

    public String hackmanPhone;

    public String bearerids;

}
