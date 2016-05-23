package org.csware.ee.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/10/9.
 */
public class CarInfo implements Serializable {

    private String id;
    private String car_type;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity_name() {
        return car_type;
    }

    public void setCity_name(String city_name) {
        this.car_type = city_name;
    }

    @Override
    public String toString() {
        return "Cityinfo [id=" + id + ", city_name=" + car_type + "]";
    }


}
