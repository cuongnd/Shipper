package org.csware.ee.model;

import java.util.List;

/**
 * Created by Yu on 2015/6/4.
 */
public class OrderDetailReturn extends Return {

    /**
     * Order : {"OwnerUser":null,"GoodsType":"机械设备","Message":"快递","GoodsUnit":"方","ToDetail":"河西","Tax":"普通发票","TruckLength":6,"BearerRate":false,"ToTime":1541562134,"OwnerId":40017,"PriceCalType":"整车","PayPoint":3,"To":120103,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9a0aa6c3-2050-4e30-82e6-b1bcb8d431f0/original","Rate":0,"VehicleType":"中栏车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/dc1d9fe0-187a-420b-961d-faf85048f99a/original","Latitude":31.171127,"Longitude":121.405686,"Plate":"沪A12345","Name":"好","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/628d8524-6e7d-45d8-b058-434eb962abd3/original","Score":0,"VehicleLength":0,"UserId":40006,"OrderAmount":0},"FromTime":1441618928,"GoodsAmount":1,"PaymentStatus":0,"ToName":"你","Status":1,"BearerId":40006,"BearerUser":{"Status":1,"CreateTime":"2015-08-17T18:19:10","Id":40006,"Mobile":"18656315210","Avatar":""},"CreateTime":1439961919,"From":130103,"BidsAmount":0,"OwnerRate":false,"ToNumber":"15121019915","Price":0.01,"DealPrice":0,"TruckType":"平板车","PayMethod":2,"Id":40165,"FromDetail":"西兰","PriceType":"包车价","Track":null}
     * Bids : [{"BearerId":40020,"BearerUser":{"Status":1,"CreateTime":"2015-08-18T13:08:31","Id":40020,"Mobile":"13917055800","Avatar":""},"Price":0.1,"Id":40042,"OrderId":40149,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/329ba5fc-7236-49ce-8ef7-3871ae5218bd/original","Rate":4.5,"VehicleType":"不限","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/095abbed-9371-4a35-8a47-854cebbf6a0b/original","Latitude":31.171199,"Longitude":121.406028,"Plate":"沪A00001","Name":"孙嘉玮","VehicleLoad":20057696,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9281cf7e-8009-4173-9095-60cc4ab4e2aa/original","Score":0,"VehicleLength":0,"UserId":40020,"OrderAmount":2}}]
     */
    public OrderEntity Order;
    public List<BidsEntity> Bids;

    public static class OrderEntity {
        /**
         * OwnerUser : null
         * GoodsType : 机械设备
         * Message : 快递
         * GoodsUnit : 方
         * ToDetail : 河西
         * Tax : 普通发票
         * TruckLength : 6
         * BearerRate : false
         * ToTime : 1541562134
         * OwnerId : 40017
         * PriceCalType : 整车
         * PayPoint : 3
         * To : 120103
         * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9a0aa6c3-2050-4e30-82e6-b1bcb8d431f0/original","Rate":0,"VehicleType":"中栏车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/dc1d9fe0-187a-420b-961d-faf85048f99a/original","Latitude":31.171127,"Longitude":121.405686,"Plate":"沪A12345","Name":"好","VehicleLoad":20,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/628d8524-6e7d-45d8-b058-434eb962abd3/original","Score":0,"VehicleLength":0,"UserId":40006,"OrderAmount":0}
         * FromTime : 1441618928
         * GoodsAmount : 1
         * PaymentStatus : 0
         * ToName : 你
         * Status : 1
         * BearerId : 40006
         * BearerUser : {"Status":1,"CreateTime":"2015-08-17T18:19:10","Id":40006,"Mobile":"18656315210","Avatar":""}
         * CreateTime : 1439961919
         * From : 130103
         * BidsAmount : 0
         * OwnerRate : false
         * ToNumber : 15121019915
         * Price : 0.01
         * DealPrice : 0
         * TruckType : 平板车
         * PayMethod : 2
         * Id : 40165
         * FromDetail : 西兰
         * PriceType : 包车价
         * Track : null
         */
        public String OwnerUser;
        public String GoodsType;
        public String Message;
        public String GoodsUnit;
        public String ToDetail;
        public String Tax;
        public float TruckLength;
        public boolean BearerRate;
        public int ToTime;
        public int OwnerId;
        public String PriceCalType;
        public int PayPoint;
        public int To;
        public BearerEntity Bearer;
        public int FromTime;
        public String GoodsAmount;
        public int PaymentStatus;
        public String ToName;
        public int Status;
        public int BearerId;
        public BearerUserEntity BearerUser;
        public int CreateTime;
        public int From;
        public int BidsAmount;
        public boolean OwnerRate;
        public String ToNumber;
        public double Price;
        public double DealPrice;
        public String TruckType;
        public int PayMethod;
        public int Id;
        public String FromDetail;
        public String PriceType;
        public String Track;

        public static class BearerEntity {
            /**
             * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9a0aa6c3-2050-4e30-82e6-b1bcb8d431f0/original
             * Rate : 0
             * VehicleType : 中栏车
             * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/dc1d9fe0-187a-420b-961d-faf85048f99a/original
             * Latitude : 31.171127
             * Longitude : 121.405686
             * Plate : 沪A12345
             * Name : 好
             * VehicleLoad : 20
             * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/628d8524-6e7d-45d8-b058-434eb962abd3/original
             * Score : 0
             * VehicleLength : 0
             * UserId : 40006
             * OrderAmount : 0
             */
            public String VehicleLicense;
            public float Rate;
            public String VehicleType;
            public String DrivingLicense;
            public double Latitude;
            public double Longitude;
            public String Plate;
            public String Name;
            public int VehicleLoad;
            public String VehiclePhoto;
            public int Score;
            public double VehicleLength;
            public int UserId;
            public int OrderAmount;
        }

        public static class BearerUserEntity {
            /**
             * Status : 1
             * CreateTime : 2015-08-17T18:19:10
             * Id : 40006
             * Mobile : 18656315210
             * Avatar :
             */
            public int Status;
            public String CreateTime;
            public int Id;
            public String Mobile;
            public String Avatar;
        }
    }

    public static class BidsEntity {
        /**
         * BearerId : 40020
         * BearerUser : {"Status":1,"CreateTime":"2015-08-18T13:08:31","Id":40020,"Mobile":"13917055800","Avatar":""}
         * Price : 0.1
         * Id : 40042
         * OrderId : 40149
         * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/329ba5fc-7236-49ce-8ef7-3871ae5218bd/original","Rate":4.5,"VehicleType":"不限","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/095abbed-9371-4a35-8a47-854cebbf6a0b/original","Latitude":31.171199,"Longitude":121.406028,"Plate":"沪A00001","Name":"孙嘉玮","VehicleLoad":20057696,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9281cf7e-8009-4173-9095-60cc4ab4e2aa/original","Score":0,"VehicleLength":0,"UserId":40020,"OrderAmount":2}
         */
        public int BearerId;
        public BearerUserEntity BearerUser;
        public double Price;
        public int Id;
        public int OrderId;
        public BearerEntity Bearer;

        public static class BearerUserEntity {
            /**
             * Status : 1
             * CreateTime : 2015-08-18T13:08:31
             * Id : 40020
             * Mobile : 13917055800
             * Avatar :
             */
            public int Status;
            public String CreateTime;
            public int Id;
            public String Mobile;
            public String Avatar;
        }

        public static class BearerEntity {
            /**
             * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/329ba5fc-7236-49ce-8ef7-3871ae5218bd/original
             * Rate : 4.5
             * VehicleType : 不限
             * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/095abbed-9371-4a35-8a47-854cebbf6a0b/original
             * Latitude : 31.171199
             * Longitude : 121.406028
             * Plate : 沪A00001
             * Name : 孙嘉玮
             * VehicleLoad : 20057696
             * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9281cf7e-8009-4173-9095-60cc4ab4e2aa/original
             * Score : 0
             * VehicleLength : 0
             * UserId : 40020
             * OrderAmount : 2
             */
            public String VehicleLicense;
            public double Rate;
            public String VehicleType;
            public String DrivingLicense;
            public double Latitude;
            public double Longitude;
            public String Plate;
            public String Name;
            public int VehicleLoad;
            public String VehiclePhoto;
            public int Score;
            public double VehicleLength;
            public int UserId;
            public int OrderAmount;
        }
    }

}
