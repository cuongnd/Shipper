package org.csware.ee.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2015/9/17.
 */
public class InsuranceInfo implements Serializable {

    private String GoodsType;
    private List<String> goodsInsurance;

    public String getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(String goodsType) {
        GoodsType = goodsType;
    }

    public List<String> getGoodsInsurance() {
        return goodsInsurance;
    }

    public void setGoodsInsurance(List<String> goodsInsurance) {
        this.goodsInsurance = goodsInsurance;
    }
}
