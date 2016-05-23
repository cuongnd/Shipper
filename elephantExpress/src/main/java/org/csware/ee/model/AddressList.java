package org.csware.ee.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yu on 2015/6/12.
 */
public class AddressList {

    public AddressList() {
        addresses = new ArrayList<>();
        Address demo = new Address();
        demo.address = ("填写过的地址，以后不用再次填写。");
        addresses.add(demo);
    }

    public List<Address> addresses;

    public static class Address {

        public int provinceId;

        public int cityId;
        /**
         * 区域Id
         */
        public String areaId;
        public int isDemo;
        /**
         * 省，市，县
         */
        public String city;
        public String address;
        public int style;//0出发，1目的地
        public boolean isDefault; //是否为默认地址

    }

}


