package org.csware.ee.model;

/**
 * 车长枚举
 * Created by Yu on 2015/7/6.
 */
public enum TruckLengthType implements ICnEnum {

    //"new(未开始),shipping(运送中),done(已完成)"

    DEFAULT("0", "不限", 0),
    M4("4.2", "4.2米", 4),
    M5("6.2", "6.2米", 5),
    M6("6.8", "6.8米", 6),
    M7("7.2", "7.2米", 7),
    M9("9.6", "9.6米", 9),
    M13("12.5", "12.5米", 13),
    M18("17.5", "17.5米", 18),;
    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    TruckLengthType(String name, String cnName, int value) {
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
