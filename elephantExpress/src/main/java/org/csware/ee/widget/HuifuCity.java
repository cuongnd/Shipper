package org.csware.ee.widget;

import org.csware.ee.model.Return;

import java.util.List;

/**
 * Created by Administrator on 2016/4/8.
 */
public class HuifuCity extends Return {


    /**
     * province : [{"ProvinceName":"北京","Citys":[{"CityCode":"1100","CityName":"北京"}],"ProvinceCode":"0011"},{"ProvinceName":"天津","Citys":[{"CityCode":"1200","CityName":"天津"}],"ProvinceCode":"0012"}]
     */
    public List<ProvinceEntity> province;

    public static class ProvinceEntity {
        /**
         * ProvinceName : 北京
         * Citys : [{"CityCode":"1100","CityName":"北京"}]
         * ProvinceCode : 0011
         */
        public String ProvinceName;
        public List<CitysEntity> Citys;
        public String ProvinceCode;

        public static class CitysEntity {
            /**
             * CityCode : 1100
             * CityName : 北京
             */
            public String CityCode;
            public String CityName;
        }
    }
}
