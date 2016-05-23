package org.csware.ee.model;

/**
 * Created by Administrator on 2015/11/5.
 */
public class PayeeScanModel extends Return {


    /**
     * Order : {"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/5d3a2e12-4137-40dc-82f4-930cf2957ceb/original"},"Owner":{"Score":0,"UserId":10013,"Rate":0,"OrderAmount":0,"VerifyMessage":"","Identity":"42133319900122****","Name":"LawSkyFall"},"GoodsType":"食品","Message":"","GoodsUnit":"方","ToDetail":"","Tax":"","TruckLength":4.2,"BearerRate":false,"ToTime":1446884472,"OwnerId":10013,"PriceCalType":"整车","PayPoint":0,"Payees":null,"To":0,"FromTime":1446798068,"GoodsAmount":1.1,"PaymentStatus":0,"ToName":"","Status":3,"CreateTime":1446711694,"Images":"","From":310106,"BidsAmount":1,"OwnerRate":false,"ToNumber":"","Price":0,"DealPrice":0.01,"Insurance":"","TruckType":"高栏车","PayMethod":2,"Id":21051,"FromDetail":"华宁路2988号","PriceType":"","Track":null}
     * Payee : {"Status":1,"Area":110104,"Address":"西兰","Price":0.01,"Id":54,"OrderId":21051,"Mobile":"18653554555","TransactionId":"","Name":"隔壁老王"}
     */
    public OrderEntity Order;
    public PayeeEntity Payee;

    public static class OrderEntity {
        /**
         * OwnerUser : {"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/5d3a2e12-4137-40dc-82f4-930cf2957ceb/original"}
         * Owner : {"Score":0,"UserId":10013,"Rate":0,"OrderAmount":0,"VerifyMessage":"","Identity":"42133319900122****","Name":"LawSkyFall"}
         * GoodsType : 食品
         * Message :
         * GoodsUnit : 方
         * ToDetail :
         * Tax :
         * TruckLength : 4.2
         * BearerRate : false
         * ToTime : 1446884472
         * OwnerId : 10013
         * PriceCalType : 整车
         * PayPoint : 0
         * Payees : null
         * To : 0
         * FromTime : 1446798068
         * GoodsAmount : 1.1
         * PaymentStatus : 0
         * ToName :
         * Status : 3
         * CreateTime : 1446711694
         * Images :
         * From : 310106
         * BidsAmount : 1
         * OwnerRate : false
         * ToNumber :
         * Price : 0.0
         * DealPrice : 0.01
         * Insurance :
         * TruckType : 高栏车
         * PayMethod : 2
         * Id : 21051
         * FromDetail : 华宁路2988号
         * PriceType :
         * Track : null
         */
        public OwnerUserEntity OwnerUser;
        public OwnerCompanyEntity OwnerCompany;
        public OwnerEntity Owner;
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
        public String Payees;
        public int To;
        public int FromTime;
        public double GoodsAmount;
        public int PaymentStatus;
        public String ToName;
        public int Status;
        public int CreateTime;
        public String Images;
        public int From;
        public int BidsAmount;
        public boolean OwnerRate;
        public String ToNumber;
        public double Price;
        public double DealPrice;
        public String Insurance;
        public String TruckType;
        public int PayMethod;
        public int Id;
        public String FromDetail;
        public String PriceType;
        public String Track;

        public static class OwnerUserEntity {
            /**
             * Status : 1
             * CompanyStatus : 1
             * CreateTime : 2015-09-06T17:51:49
             * Id : 10013
             * Mobile : 15371452845
             * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/5d3a2e12-4137-40dc-82f4-930cf2957ceb/original
             */
            public int Status;
            public int CompanyStatus;
            public String CreateTime;
            public int Id;
            public String Mobile;
            public String Avatar;
        }

        public static class OwnerCompanyEntity {
            /**
             * BusinessLicence : 1
             * CompanyName : 1
             * UserId : 1
             * LegalPerson : 2015-09-06T17:51:49
             * LegalPersonBack : 10013
             * LegalPersonFront : 15371452845
             * LegalPersonId : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/5d3a2e12-4137-40dc-82f4-930cf2957ceb/original
             */
            public String BusinessLicence;
            public String CompanyName;
            public int UserId;
            public String LegalPerson;
            public String LegalPersonBack;
            public String LegalPersonFront;
            public String LegalPersonId;
        }

        public static class OwnerEntity {
            /**
             * Score : 0
             * UserId : 10013
             * Rate : 0.0
             * OrderAmount : 0
             * VerifyMessage :
             * Identity : 42133319900122****
             * Name : LawSkyFall
             */
            public int Score;
            public int UserId;
            public double Rate;
            public int OrderAmount;
            public String VerifyMessage;
            public String Identity;
            public String Name;
        }
    }

    public static class PayeeEntity {
        /**
         * Status : 1
         * Area : 110104
         * Address : 西兰
         * Price : 0.01
         * Id : 54
         * OrderId : 21051
         * Mobile : 18653554555
         * TransactionId :
         * Name : 隔壁老王
         * BearerBonus: 0.00
         * OwnerBonus: 0.00
         * PayerBonus: 0.00
         */
        public int Status;
        public int Area;
        public String Address;
        public double Price;
        public double BearerBonus;
        public double OwnerBonus;
        public double PayerBonus;
        public int Id;
        public int OrderId;
        public String Mobile;
        public String TransactionId;
        public String Name;
    }
}
