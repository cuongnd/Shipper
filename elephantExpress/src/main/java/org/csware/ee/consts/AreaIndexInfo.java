package org.csware.ee.consts;

/**
 * Created by Yu on 2015/5/27.
 */
public class AreaIndexInfo {

    public AreaIndexInfo() {
        provinceIndex = 1;
        cityIndex = 1;
        areaIndex = 1;
    }

    int provinceIndex;
    int cityIndex;
    int areaIndex;

    public int getProvinceIndex() {
        return provinceIndex;
    }

    public void setProvinceIndex(int provinceIndex) {
        this.provinceIndex = provinceIndex;
    }

    public int getCityIndex() {
        return cityIndex;
    }

    public void setCityIndex(int cityIndex) {
        this.cityIndex = cityIndex;
    }

    public int getAreaIndex() {
        return areaIndex;
    }

    public void setAreaIndex(int areaIndex) {
        this.areaIndex = areaIndex;
    }
}
