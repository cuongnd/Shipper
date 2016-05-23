package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/5.
 */
public class CarLengthModel extends Return {

    public List<VehicleTypesBean> VehicleTypes;

    public static class VehicleTypesBean {
        public String Vehicle;
        public List<String > Lengths;
    }
}
