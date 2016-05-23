package org.csware.ee.model;

import com.baidu.mapapi.model.LatLng;

import java.util.List;

/**
 * Created by Yu on 2015/8/12.
 */
public class MapCenterPointInfo {

    public MapCenterPointInfo(List<LatLng> list) {
//        southWest = new LatLng(39.92235, 116.380338);
//        northEast = new LatLng(39.947246, 116.414977);
        southWest = list.get(0);
        northEast = list.get(list.size()-1);
        if (list == null || list.size() == 0) return;

        for (LatLng item : list) {
            if (item.latitude < southWest.latitude || item.longitude < southWest.longitude)
                southWest = item;

            if (item.longitude > northEast.longitude || item.latitude > northEast.latitude)
                northEast = item;
        }

    }

    public LatLng southWest;

    public LatLng northEast;

}
