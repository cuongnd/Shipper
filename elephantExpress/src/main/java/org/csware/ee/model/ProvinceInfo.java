package org.csware.ee.model;

import java.util.List;

/**
 *  省，市，县 的序列化模型
 *
 * Created by Yu on 2015/8/10.
 */
public class ProvinceInfo {


    /**
     * Cities : [{"Code":110100,"Name":"北京市","Areas":[{"Code":110101,"Name":"东城区"},{"Code":110102,"Name":"西城区"},{"Code":110103,"Name":"崇文区"},{"Code":110104,"Name":"宣武区"},{"Code":110105,"Name":"朝阳区"},{"Code":110106,"Name":"丰台区"},{"Code":110107,"Name":"石景山区"},{"Code":110108,"Name":"海淀区"},{"Code":110109,"Name":"门头沟区"},{"Code":110110,"Name":"房山区"},{"Code":110111,"Name":"通州区"},{"Code":110112,"Name":"顺义区"},{"Code":110113,"Name":"昌平区"},{"Code":110114,"Name":"大兴区"},{"Code":110115,"Name":"平谷区"},{"Code":110116,"Name":"怀柔区"},{"Code":110117,"Name":"密云县"},{"Code":110118,"Name":"延庆县"}]}]
     * Code : 110000
     * Name : 北京市
     */
    public List<CitiesEntity> Cities;
    public int Code;
    public String Name;

    public static class CitiesEntity {
        /**
         * Code : 110100
         * Name : 北京市
         * Areas : [{"Code":110101,"Name":"东城区"},{"Code":110102,"Name":"西城区"},{"Code":110103,"Name":"崇文区"},{"Code":110104,"Name":"宣武区"},{"Code":110105,"Name":"朝阳区"},{"Code":110106,"Name":"丰台区"},{"Code":110107,"Name":"石景山区"},{"Code":110108,"Name":"海淀区"},{"Code":110109,"Name":"门头沟区"},{"Code":110110,"Name":"房山区"},{"Code":110111,"Name":"通州区"},{"Code":110112,"Name":"顺义区"},{"Code":110113,"Name":"昌平区"},{"Code":110114,"Name":"大兴区"},{"Code":110115,"Name":"平谷区"},{"Code":110116,"Name":"怀柔区"},{"Code":110117,"Name":"密云县"},{"Code":110118,"Name":"延庆县"}]
         */
        public int Code;
        public String Name;
        public List<AreasEntity> Areas;

        public static class AreasEntity {
            /**
             * Code : 110101
             * Name : 东城区
             */
            public int Code;
            public String Name;
        }
    }
}
