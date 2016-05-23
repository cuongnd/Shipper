package org.csware.ee.model;

/**
 * 列表项状态
 * Created by Yu on 2015/7/6.
 */
public enum OrderListType implements ICnEnum {

    //"new(未开始),shipping(运送中),done(已完成)"

    UNKNOWN("UnKnown", "未知", 0),
    NEW("new", "未开始", 1),
    SHIPPING("shipping", "运送中", 2),
    DONE("done", "已完成", 3),;
    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    OrderListType(String name, String cnName, int value) {
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
