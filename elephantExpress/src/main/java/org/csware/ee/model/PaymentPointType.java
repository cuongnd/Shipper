package org.csware.ee.model;

/**
 * Created by Yu on 2015/6/25.
 * 付款方式
 */
public enum PaymentPointType implements ICnEnum {

    /*付款时间(1:发货付款,2:到货付款,3:见票付款,4:见回单付款)*/


//    UNKNOWN("UnKnown", "见回单付款", 4),
    BEFORE_DEPART("BeforeDepart", "发货付款", 0),
    AFTER_ARRIVED("AfterArrived", "到货付款", 1),
    INVOICE("Invoice", "见票付款", 2),
    RECEIPT("Receipt", "见回单付款", 3);

    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    PaymentPointType(String name, String cnName, int value) {
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

    public CnEnum toItem(){
        return new CnEnum(this);
    }

}
