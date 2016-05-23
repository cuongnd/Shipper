package org.csware.ee.model;

import java.util.List;

/**
 * Created by Yu on 2015/8/11.
 */
public class OrderTrackReturn extends Return {

    /**
     * Track : {"LastTrackPoint":{"Latitude":31.170848,"Time":1439280462,"Longitude":121.40597},"LastUpdate":1439251662,"CreateTime":1439251629,"OrderId":10052,"Track":[{"Latitude":31.170849,"Time":1439280429,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280431,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280436,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280441,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280447,"Longitude":121.405971},{"Latitude":31.170848,"Time":1439280459,"Longitude":121.40597},{"Latitude":31.170848,"Time":1439280462,"Longitude":121.40597}]}
     */
    public TrackEntity Track;

    public static class TrackEntity {
        /**
         * LastTrackPoint : {"Latitude":31.170848,"Time":1439280462,"Longitude":121.40597}
         * LastUpdate : 1439251662
         * CreateTime : 1439251629
         * OrderId : 10052
         * Track : [{"Latitude":31.170849,"Time":1439280429,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280431,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280436,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280441,"Longitude":121.405971},{"Latitude":31.170849,"Time":1439280447,"Longitude":121.405971},{"Latitude":31.170848,"Time":1439280459,"Longitude":121.40597},{"Latitude":31.170848,"Time":1439280462,"Longitude":121.40597}]
         */
        public LastTrackPointEntity LastTrackPoint;
        public int LastUpdate;
        public int CreateTime;
        public int OrderId;
        public List<LastTrackPointEntity> Track;

        public static class LastTrackPointEntity {
            /**
             * Latitude : 31.170848
             * Time : 1439280462
             * Longitude : 121.40597
             */
            public double Latitude;
            public int Time;
            public double Longitude;
        }


    }
}
