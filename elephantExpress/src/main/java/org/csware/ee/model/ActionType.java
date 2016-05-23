package org.csware.ee.model;

/**
 * 操作形为枚举
 * Created by Yu on 2015/7/6.
 */
public enum ActionType implements ICnEnum {

    //"new(未开始),shipping(运送中),done(已完成)"

    DEFAULT("default", "默认", 0),

    ADD("add", "增加", 1),
    EDIT("edit", "编辑", 2),
    DELETE("delete","删除",9);

    ;
    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    ActionType(String name, String cnName, int value) {
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
