package org.csware.ee.model;

/**
 * Created by Yu on 2015/6/25.
 * 中文枚举接口
 */
public interface ICnEnum {

    String toCnName();

    String toName();

    int toValue();

    /**
     * 转为实体
     */
    CnEnum toItem();

    public static class CnEnum {

        public CnEnum(ICnEnum cnEnum) {
            this.name = cnEnum.toName();
            this.cnName = cnEnum.toCnName();
            this.value = cnEnum.toValue();
        }

        public String name;

        public String cnName;

        public int value;

        public String toString() {
            return "[" + this.value + ":" + this.name + "][" + this.cnName + "]";
        }
    }
}


