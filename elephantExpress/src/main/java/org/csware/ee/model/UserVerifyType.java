package org.csware.ee.model;

/**
 * 用户验证状态
 * Created by Yu on 2015/7/23.
 */
public enum UserVerifyType implements ICnEnum {


    NOT_VERIFIED("NotVerified", "未验证", -1),
    VERIFYING("Verifying", "审核中", 0),
    VERIFIED("Verified", "个人认证", 1),
    VERIFIEDFAIL("VerifiedFail", "认证未通过", -2),;

    // 成员变量
    String name;
    String cnName;
    int value;

    // 构造方法
    UserVerifyType(String name, String cnName, int value) {
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