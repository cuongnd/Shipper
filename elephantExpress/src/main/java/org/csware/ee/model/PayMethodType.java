package org.csware.ee.model;

/**
 * Created by Yu on 2015/6/25.
 */
public enum PayMethodType implements ICnEnum {

//    付款方式(1:平台付款,2:其他)

    UNKNOWN("UnKnown", "未设置", 0),
    PLATFORM("Platform", "平台付款", 1),
    OTHER("Other", "其他", 2),;

    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    PayMethodType(String name, String cnName, int value) {
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
