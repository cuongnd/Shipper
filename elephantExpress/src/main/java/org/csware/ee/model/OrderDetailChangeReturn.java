package org.csware.ee.model;

import java.util.List;

/**
 * Created by Yu on 2015/6/4.
 */
public class OrderDetailChangeReturn extends Return {


    /**
     * Order : {"GoodsType":"电子产品","Message":"今天1","GoodsUnit":"吨","ToDetail":"红旗大街","Tax":"无需发票","TruckLength":0,"BearerRate":false,"ToTime":1440683161,"OwnerId":50001,"PriceCalType":"零担","PayPoint":4,"To":230101,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/f6573111-194b-4348-9108-c38456f3ca36/original","Rate":0,"VehicleType":"厢式车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/3bd458c9-ecc3-4daa-bf85-72d37fa4506b/original","Latitude":31.170889,"Longitude":121.405876,"Name":"尹成泰","Plate":"沪s66666","VehicleLoad":38,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/be65d6e0-a14b-4c55-a702-eacd27adf60d/original","Score":0,"VehicleLength":12.5,"UserId":50002,"OrderAmount":2},"FromTime":1440683082,"GoodsAmount":100,"PaymentStatus":0,"ToName":"王","Status":3,"BearerId":50002,"BearerUser":{"Status":1,"CreateTime":"2015-08-26T10:51:25","Id":50002,"Mobile":"15021066792","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/99155714-30ae-4691-a1e4-c3f071396e36/original"},"CreateTime":1440570741,"From":310102,"BidsAmount":1,"OwnerRate":false,"ToNumber":"911","DealPrice":66666600,"Price":0.01,"TruckType":"冷藏车","PayMethod":2,"Id":50035,"FromDetail":"古美路1582号","PriceType":"元/吨"}
     * Bids : [{"BearerId":50002,"BearerUser":{"Status":1,"CreateTime":"2015-08-26T10:51:25","Id":50002,"Mobile":"15021066792","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/99155714-30ae-4691-a1e4-c3f071396e36/original"},"Price":666666,"Id":50017,"OrderId":50035,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/f6573111-194b-4348-9108-c38456f3ca36/original","Rate":0,"VehicleType":"厢式车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/3bd458c9-ecc3-4daa-bf85-72d37fa4506b/original","Latitude":31.170889,"Longitude":121.405876,"Name":"尹成泰","Plate":"沪s66666","VehicleLoad":38,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/be65d6e0-a14b-4c55-a702-eacd27adf60d/original","Score":0,"VehicleLength":12.5,"UserId":50002,"OrderAmount":2},"PriceType":"元/吨"}]
     */
    public OrderEntity Order;

    public List<BidsEntity> Bids;

    public static class OrderEntity {
        /**
         * GoodsType : 电子产品
         * Message : 今天1
         * GoodsUnit : 吨
         * ToDetail : 红旗大街
         * Tax : 无需发票
         * TruckLength : 0
         * BearerRate : false
         * ToTime : 1440683161
         * OwnerId : 50001
         * PriceCalType : 零担
         * PayPoint : 4
         * To : 230101
         * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/f6573111-194b-4348-9108-c38456f3ca36/original","Rate":0,"VehicleType":"厢式车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/3bd458c9-ecc3-4daa-bf85-72d37fa4506b/original","Latitude":31.170889,"Longitude":121.405876,"Name":"尹成泰","Plate":"沪s66666","VehicleLoad":38,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/be65d6e0-a14b-4c55-a702-eacd27adf60d/original","Score":0,"VehicleLength":12.5,"UserId":50002,"OrderAmount":2}
         * FromTime : 1440683082
         * GoodsAmount : 100
         * Insurance: 0
         * PaymentStatus : 0
         * ToName : 王
         * Status : 3
         * BearerId : 50002
         * BearerUser : {"Status":1,"CreateTime":"2015-08-26T10:51:25","Id":50002,"Mobile":"15021066792","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/99155714-30ae-4691-a1e4-c3f071396e36/original"}
         * CreateTime : 1440570741
         * From : 310102
         * BidsAmount : 1
         * OwnerRate : false
         * ToNumber : 911
         * DealPrice : 66666600
         * Price : 0.01
         * TruckType : 冷藏车
         * PayMethod : 2
         * Paid : 0.00
         * Images : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/ab77e9ac-4794-4f70-acf3-3a995474c36b/original
         * Id : 50035
         * FromDetail : 古美路1582号
         * PriceType : 元/吨
         */
        public String GoodsType;
        public String Images;
        public String Message;
        public String GoodsUnit;
        public String ToDetail;
        public String Tax;
        public String Insurance;
        public double TruckLength;
        public boolean BearerRate;
        public int ToTime;
        public int OwnerId;
        public String PriceCalType;
        public int PayPoint;
        public List<PayeesEntity> Payees;
        public List<EvidencesEntity> Evidences;
        public int To;
        public BearerEntity Bearer;
        public int FromTime;
        public double GoodsAmount;
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
        public double DealPrice;
        public double Price;
        public String TruckType;
        public int PayMethod;
        public double Paid;
        public int Id;
        public String FromDetail;
        public String PriceType;
        public BearerCompanyEntity BearerCompany;

        public static class BearerCompanyEntity {
            public int UserId;
            public String LegalPerson;
            public String LegalPersonId;
            public String CompanyName;
            public String BusinessLicence;
            public String LegalPersonFront;
            public String LegalPersonBack;
        }

        public List<SubOrdersEntity> SubOrders;

        public static class SubOrdersEntity {
            /**
             * GoodsType : 机械设备
             * Message :
             * GoodsUnit : 公斤
             * ToDetail : 驾校H
             * Tax :
             * TruckLength : 1.8
             * BearerRate : false
             * ToTime : 1451555013
             * OwnerId : 10013
             * PriceCalType : 整车
             * PayPoint : 0
             * To : 110101
             * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original","Rate":5,"VehicleType":"平板车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original","VerifyMessage":"","Latitude":37.791485,"Longitude":121.40561,"Name":"李","Plate":"沪A889988","VehicleLoad":23,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original","Score":998463,"VehicleLength":13,"UserId":10006,"OrderAmount":1}
             * FromTime : 1451472208
             * GoodsAmount : 1.0
             * PaymentStatus : 0
             * ToName :
             * Status : 3
             * BearerId : 10006
             * BearerUser : {"Status":1,"CreateTime":"2015-07-31T15:58:45","CompanyStatus":-1,"Id":10006,"Mobile":"13381603257","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original"}
             * CreateTime : 1451470259
             * Images :
             * From : 310101
             * BidsAmount : 1
             * OwnerRate : false
             * ToNumber :
             * DealPrice : 1.0
             * Price : 0.0
             * Insurance :
             * TruckType : 面包车
             * PayMethod : 2
             * Id : 21967
             * FromDetail : 耀华路/上南路(路口)
             * PriceType :
             */
            public String GoodsType;
            public String Message;
            public String GoodsUnit;
            public String ToDetail;
            public String Tax;
            public double TruckLength;
            public boolean BearerRate;
            public int ToTime;
            public int OwnerId;
            public String PriceCalType;
            public int PayPoint;
            public int To;
            public BearerEntity Bearer;
            public int FromTime;
            public double GoodsAmount;
            public int PaymentStatus;
            public String ToName;
            public int Status;
            public int BearerId;
            public BearerUserEntity BearerUser;
            public int CreateTime;
            public String Images;
            public int From;
            public int BidsAmount;
            public boolean OwnerRate;
            public String ToNumber;
            public double DealPrice;
            public double Price;
            public String Insurance;
            public String TruckType;
            public int PayMethod;
            public int Id;
            public String FromDetail;
            public String PriceType;
            public OrderEntity.BearerCompanyEntity BearerCompany;

            public static class BearerEntity {
                /**
                 * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original
                 * Rate : 5.0
                 * VehicleType : 平板车
                 * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original
                 * VerifyMessage :
                 * Latitude : 37.791485
                 * Longitude : 121.40561
                 * Name : 李
                 * Plate : 沪A889988
                 * VehicleLoad : 23.0
                 * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original
                 * Score : 998463
                 * VehicleLength : 13.0
                 * UserId : 10006
                 * OrderAmount : 1
                 */
                public String VehicleLicense;
                public double Rate;
                public String VehicleType;
                public String DrivingLicense;
                public String VerifyMessage;
                public double Latitude;
                public double Longitude;
                public String Name;
                public String Plate;
                public double VehicleLoad;
                public String VehiclePhoto;
                public int Score;
                public double VehicleLength;
                public int UserId;
                public int OrderAmount;
            }

            public static class BearerUserEntity {
                /**
                 * Status : 1
                 * CreateTime : 2015-07-31T15:58:45
                 * CompanyStatus : -1
                 * Id : 10006
                 * Mobile : 13381603257
                 * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/6cdf951f-fda6-43d4-857b-cb59a6ae8673/original
                 */
                public int Status;
                public String CreateTime;
                public int CompanyStatus;
                public int Id;
                public String Mobile;
                public String Avatar;
            }
        }

        public static class BearerEntity {
            /**
             * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/f6573111-194b-4348-9108-c38456f3ca36/original
             * Rate : 0
             * VehicleType : 厢式车
             * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/3bd458c9-ecc3-4daa-bf85-72d37fa4506b/original
             * Latitude : 31.170889
             * Longitude : 121.405876
             * Name : 尹成泰
             * Plate : 沪s66666
             * VehicleLoad : 38
             * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/be65d6e0-a14b-4c55-a702-eacd27adf60d/original
             * Score : 0
             * VehicleLength : 12.5
             * UserId : 50002
             * OrderAmount : 2
             */
            public String VehicleLicense;
            public double Rate;
            public String VehicleType;
            public String DrivingLicense;
            public double Latitude;
            public double Longitude;
            public String Name;
            public String Plate;
            public double VehicleLoad;
            public String VehiclePhoto;
            public int Score;
            public double VehicleLength;
            public int UserId;
            public int OrderAmount;
        }

        public static class BearerUserEntity {
            /**
             * Status : 1
             * CreateTime : 2015-08-26T10:51:25
             * Id : 50002
             * Mobile : 15021066792
             * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/99155714-30ae-4691-a1e4-c3f071396e36/original
             */
            public int Status;
            public int CompanyStatus;
            public String CreateTime;
            public int Id;
            public String Mobile;
            public String Avatar;
        }

        public static class PayeesEntity {
            /**
             * Status : 1
             * Area : 310106
             * Paid: 0.01,
             * Address : 华宁路2988号
             * Price : 524.2
             * Id : 24
             * OrderId : 20105
             * Mobile : 13341421526
             * Name : 隔壁老王
             * TransactionId :
             */
            public int Status;
            public int Area;
            public String Address;
            public double Price;
            public double Paid;
            public int Id;
            public int OrderId;
            public String Mobile;
            public String Name;
            public String TransactionId;
        }

        public static class EvidencesEntity {
            /**
             * Id : 24
             * OrderId : 20105
             * Content : 13341421526
             * Url : 隔壁老王
             * CreateTime :
             * BearerName
             */
            public int Id;
            public int OrderId;
            public String Content;
            public String Url;
            public String CreateTime;
            public String BearerName;
        }
    }

    public static class BidsEntity {
        /**
         * BearerId : 50002
         * BearerUser : {"Status":1,"CreateTime":"2015-08-26T10:51:25","Id":50002,"Mobile":"15021066792","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/99155714-30ae-4691-a1e4-c3f071396e36/original"}
         * Price : 666666
         * Id : 50017
         * OrderId : 50035
         * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/f6573111-194b-4348-9108-c38456f3ca36/original","Rate":0,"VehicleType":"厢式车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/3bd458c9-ecc3-4daa-bf85-72d37fa4506b/original","Latitude":31.170889,"Longitude":121.405876,"Name":"尹成泰","Plate":"沪s66666","VehicleLoad":38,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/be65d6e0-a14b-4c55-a702-eacd27adf60d/original","Score":0,"VehicleLength":12.5,"UserId":50002,"OrderAmount":2}
         * PriceType : 元/吨
         */
        public int BearerId;
        public OrderEntity.BearerUserEntity BearerUser;
        public double Price;
        public int Id;
        public int OrderId;
        public OrderEntity.BearerEntity Bearer;
        public OrderEntity.BearerCompanyEntity BearerCompany;
        public String PriceType;
    }
}
