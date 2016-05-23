package org.csware.ee.model;

import com.baidu.mapapi.map.MyLocationData;

import org.csware.ee.utils.ParamTool;

import java.util.Date;

/**
 * Created by Yu on 2015/6/16.
 * 定位信息
 */
public class Location {

    public Location(){
        updateTime();
    }

    public boolean isChanged(double latitude,double longitude){
        if(this.latitude != latitude||this.longitude!=longitude){
            this.latitude = latitude;
            this.longitude = longitude;
            updateTime();
            return true;
        }
        return false;
    }

    void updateTime(){
        lastLocationTime = ParamTool.getTimeSeconds(new Date());
    }

    /**作为变更依据，用来判断是否要更新数据*/
    public int isChanged;

    /**
     * 获取纬度坐标
     */
    public double latitude;
    /**
     * 获取经度坐标
     */
    public double longitude;

    /**
     * 获取定位精度
     */
    public float radius;
    /**
     * 获取速度，仅gps定位结果时有速度信息
     */
    public float speed;
    /**
     * gps定位结果时，获取gps锁定用的卫星数
     */
    public int satelliteNumber;
    /**
     * 获取详细地址信息
     */
    public String address;

    /**
     * 获取定位类型: 参考 定位结果描述 相关的字段
     */
    public int locaType;

    /**
     * 最后位定时间
     */
    public int lastLocationTime;
    /**获取定位数据时的时间*/
    public String locationTime;

    public boolean hasPosition(){
        return latitude > 0 && longitude > 0;
    }

    public MyLocationData getData(){
        // 构造定位数据
        MyLocationData locData = new MyLocationData.Builder()
                .accuracy(this.radius)
                        // 此处设置开发者获取到的方向信息，顺时针0-360
                .direction(100).latitude(this.latitude)
                .longitude(this.longitude).build();
        return locData;
    }



}
