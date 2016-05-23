package org.csware.ee.model;

/**
 * Created by Yu on 2015/7/6.
 */
public enum OrderStatusType implements ICnEnum {

    //"new(未开始),shipping(运送中),done(已完成)"

    CREATED("Created", "已建立", 0),
    PRE_CONFIRMED("PreConfirmed", "预约司机", 1),
    CONFIRMED("Confirmed", "已确认司机", 2),
    DEPARTED("Departed", "已提货发车", 3),
    ARRIVED("Arrived", "运输中", 4),
    BEARER_DONE("BearerDone", "货主确认", 5),
    OWNER_DONE("OwnerDone", "车主确认", 6),;

    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    OrderStatusType(String name, String cnName, int value) {
        this.name = name;
        this.cnName = cnName;
        this.value = value;
    }

    //覆盖方法
    @Override
    public String toString() {
        return "[" + this.value + ":" + this.name + "][" + this.cnName + "]";
    }

    /**
     * 获得名称
     */
    public String toName() {
        return this.name;
    }

    public String toCnName() {
        return this.cnName;
    }

    /**
     * 获得int值
     */
    public int toValue() {
        return this.value;
    }

    public CnEnum toItem() {
        return new CnEnum(this);
    }
}
