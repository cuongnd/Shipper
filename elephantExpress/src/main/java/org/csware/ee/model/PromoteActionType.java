package org.csware.ee.model;

/**
 * Created by Yu on 2015/6/25.
 * 付款方式
 */
public enum PromoteActionType implements ICnEnum {

    /* "depart(已发货),ownerdone(货主确认完成)")*/


    DEPART("depart", "已发货", 1),
    ONWER_DONE("ownerdone", "货主确认完成", 2),;

    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    PromoteActionType(String name, String cnName, int value) {
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
