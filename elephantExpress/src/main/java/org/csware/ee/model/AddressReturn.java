package org.csware.ee.model;

import org.csware.ee.Guard;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yu on 2015/6/25.
 */
public class AddressReturn extends Return {

    public AddressReturn() {
        Addresses = new ArrayList<>();
    }

    /**
     * Addresses : [{"Type":2,"Area":"110101","UserId":5,"Id":25,"Detail":"北京市,市辖区,东城区\n广东人头发"},{"Type":2,"Area":"110101","UserId":5,"Id":26,"Detail":"北京市,市辖区,东城区\n广东人头发"}]
     */
    public List<AddressesEntity> Addresses;

    public boolean exists(String address) {
        if (Addresses == null || Addresses.size() == 0) return false;
        for (int i = 0; i < Addresses.size(); i++) {
            AddressesEntity item = Addresses.get(i);
            if (Guard.isNullOrEmpty(item.Detail)) continue;
            if (item.Detail.equals(address)) return true;
        }
        return false;
    }

    public  static class AddressesEntity {
        /**
         * Type : 2
         * Area : 110101
         * UserId : 5
         * Id : 25
         * Detail : 北京市,市辖区,东城区
         * 广东人头发
         */
        public int Type;
        public String Area;
        public int UserId;
        public int Id;
        public String Detail;
    }
}
