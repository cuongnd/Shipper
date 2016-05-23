package org.csware.ee.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/3.
 */
public class HuifuCityModel extends Return  implements Serializable {

    public String provinceId;
    public String cityId;
    public  String cityName;
    public String provinceName;

    public String getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(String provinceId) {
        this.provinceId = provinceId;
    }

    public String getCityId() {
        return cityId;
    }

    public void setCityId(String cityId) {
        this.cityId = cityId;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }
}
