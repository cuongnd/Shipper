package org.csware.ee.model;

import java.util.List;

/**
 * Created by Yu on 2015/6/3.
 * 订单列表返回结果
 */
public class OrderListReturn extends Return {


    /**
     * Orders : [{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"","Tax":"普通发票","TruckLength":17.5,"BearerRate":false,"ToTime":1441696638,"OwnerId":10013,"PriceCalType":"整车","PayPoint":3,"Payees":[{"Status":1,"Area":310106,"Address":"华宁路2988号","Price":524.2,"Id":24,"OrderId":20105,"Mobile":"13341421526","Name":"隔壁老王","TransactionId":""},{"Status":1,"Area":110104,"Address":"西兰","Price":32.52,"Id":25,"OrderId":20105,"Mobile":"13341421526","Name":"隔壁老王","TransactionId":""}],"To":0,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original","Rate":0,"VehicleType":"危险品车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original","Latitude":0,"Longitude":0,"Name":"工商局","Plate":"沪A12345","VehicleLoad":100,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original","Score":0,"VehicleLength":18,"UserId":10004,"OrderAmount":0},"FromTime":1441617436,"GoodsAmount":56.85,"PaymentStatus":0,"ToName":"","Status":1,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/42564f06-f39a-47b9-bd3d-9e37acecd3c7/original"},"CreateTime":1441613287,"From":310102,"BidsAmount":0,"OwnerRate":false,"ToNumber":"","DealPrice":0,"Price":10.05,"TruckType":"集装箱车","PayMethod":1,"Id":20105,"FromDetail":"漕河泾","PriceType":"元/包车"},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"","Tax":"普通发票","TruckLength":17.5,"BearerRate":false,"ToTime":1441696638,"OwnerId":10013,"PriceCalType":"整车","PayPoint":3,"Payees":[{"Status":1,"Area":310106,"Address":"华宁路2988号","Price":6325,"Id":22,"OrderId":20103,"Mobile":"13341423154","Name":"隔壁老杨","TransactionId":""},{"Status":1,"Area":110104,"Address":"西兰","Price":2115,"Id":23,"OrderId":20103,"Mobile":"13341421526","Name":"隔壁老王","TransactionId":""}],"To":0,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original","Rate":0,"VehicleType":"危险品车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original","Latitude":0,"Longitude":0,"Name":"工商局","Plate":"沪A12345","VehicleLoad":100,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original","Score":0,"VehicleLength":18,"UserId":10004,"OrderAmount":0},"FromTime":1441617436,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":1,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/42564f06-f39a-47b9-bd3d-9e37acecd3c7/original"},"CreateTime":1441610181,"From":310102,"BidsAmount":0,"OwnerRate":false,"ToNumber":"","DealPrice":0,"Price":0.01,"TruckType":"集装箱车","PayMethod":1,"Id":20103,"FromDetail":"漕河泾","PriceType":"元/包车"},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"西兰","Tax":"普通发票","TruckLength":17.5,"BearerRate":false,"ToTime":1441696638,"OwnerId":10013,"PriceCalType":"整车","PayPoint":3,"To":110104,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original","Rate":0,"VehicleType":"危险品车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original","Latitude":0,"Longitude":0,"Name":"工商局","Plate":"沪A12345","VehicleLoad":100,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original","Score":0,"VehicleLength":18,"UserId":10004,"OrderAmount":0},"FromTime":1441617436,"GoodsAmount":1,"PaymentStatus":0,"ToName":"隔壁老杨","Status":1,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/42564f06-f39a-47b9-bd3d-9e37acecd3c7/original"},"CreateTime":1441609791,"From":310102,"BidsAmount":0,"OwnerRate":false,"ToNumber":"13341423154","DealPrice":0,"Price":0.01,"TruckType":"集装箱车","PayMethod":1,"Id":20102,"FromDetail":"漕河泾","PriceType":"元/包车"},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"","Tax":"普通发票","TruckLength":17.5,"BearerRate":false,"ToTime":1441696638,"OwnerId":10013,"PriceCalType":"整车","PayPoint":3,"Payees":[{"Status":1,"Area":310106,"Address":"华宁路2988号","Price":845,"Id":20,"OrderId":20101,"Mobile":"13341425247","Name":"隔壁老王","TransactionId":""},{"Status":1,"Area":110104,"Address":"西兰","Price":5464,"Id":21,"OrderId":20101,"Mobile":"13341423154","Name":"隔壁老杨","TransactionId":""}],"To":0,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original","Rate":0,"VehicleType":"危险品车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original","Latitude":0,"Longitude":0,"Name":"工商局","Plate":"沪A12345","VehicleLoad":100,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original","Score":0,"VehicleLength":18,"UserId":10004,"OrderAmount":0},"FromTime":1441617436,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":1,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/42564f06-f39a-47b9-bd3d-9e37acecd3c7/original"},"CreateTime":1441609341,"From":310102,"BidsAmount":0,"OwnerRate":false,"ToNumber":"","DealPrice":0,"Price":0.01,"TruckType":"集装箱车","PayMethod":1,"Id":20101,"FromDetail":"漕河泾","PriceType":"元/包车"},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"","Tax":"普通发票","TruckLength":17.5,"BearerRate":false,"ToTime":1441696638,"OwnerId":10013,"PriceCalType":"整车","PayPoint":3,"Payees":[{"Status":1,"Area":310106,"Address":"华宁路2988号","Price":5255,"Id":19,"OrderId":20100,"Mobile":"13341425247","Name":"隔壁老王","TransactionId":""}],"To":0,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original","Rate":0,"VehicleType":"危险品车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original","Latitude":0,"Longitude":0,"Name":"工商局","Plate":"沪A12345","VehicleLoad":100,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original","Score":0,"VehicleLength":18,"UserId":10004,"OrderAmount":0},"FromTime":1441617436,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":1,"BearerId":10004,"BearerUser":{"Status":1,"CreateTime":"2015-07-29T17:09:15","Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/42564f06-f39a-47b9-bd3d-9e37acecd3c7/original"},"CreateTime":1441609002,"From":310102,"BidsAmount":0,"OwnerRate":false,"ToNumber":"","DealPrice":0,"Price":0.01,"TruckType":"集装箱车","PayMethod":1,"Id":20100,"FromDetail":"漕河泾","PriceType":"元/包车"},{"OwnerUser":{"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"","Tax":"普通发票","TruckLength":17.5,"BearerRate":false,"ToTime":1441696638,"OwnerId":10013,"PriceCalType":"整车","PayPoint":3,"Payees":[{"Status":0,"Area":310106,"Address":"华宁路2988号","Price":5245,"Id":18,"OrderId":20099,"Mobile":"13341425247","Name":"隔壁老王","TransactionId":""}],"To":0,"Bearer":{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/59cdcea2-918e-48e7-ba56-e4ce6e4049c4/original","Rate":0,"VehicleType":"平板车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/46337ba3-9282-4d60-a70c-f49bdd119d7a/original","Latitude":37.785835,"Longitude":121.40561,"Name":"李","Plate":"沪A889988","VehicleLoad":23,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/152300ec-cfff-4be9-8f45-e0213daa45ac/original","Score":0,"VehicleLength":13,"UserId":10006,"OrderAmount":0},"FromTime":1441617436,"GoodsAmount":1,"PaymentStatus":0,"ToName":"","Status":1,"BearerId":10006,"BearerUser":{"Status":1,"CreateTime":"2015-07-31T15:58:45","Id":10006,"Mobile":"13381603257","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/e3c19c09-b8d4-4933-a532-4dc4ca28d158/original"},"CreateTime":1441608655,"From":310102,"BidsAmount":0,"OwnerRate":false,"ToNumber":"","DealPrice":0,"Price":0.01,"TruckType":"集装箱车","PayMethod":1,"Id":20099,"FromDetail":"漕河泾","PriceType":"元/包车"}]
     * Page : {"PageSize":10,"Page":1,"Count":12}
     */
    private List<OrdersEntity> Orders;
    private PageEntity Page;
    private TypeCountEntity TypeCount;

    public TypeCountEntity getTypeCount() {
        return TypeCount;
    }

    public void setTypeCount(TypeCountEntity typeCount) {
        TypeCount = typeCount;
    }

    public void setOrders(List<OrdersEntity> Orders) {
        this.Orders = Orders;
    }

    public void setPage(PageEntity Page) {
        this.Page = Page;
    }

    public List<OrdersEntity> getOrders() {
        return Orders;
    }

    public PageEntity getPage() {
        return Page;
    }

    public static class OrdersEntity {
        /**
         * OwnerUser : {"Status":1,"CreateTime":"2015-09-06T17:51:49","Id":10013,"Mobile":"15371452845","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original"}
         * GoodsType : 机械设备
         * Message :
         * GoodsUnit : 吨
         * ToDetail :
         * Tax : 普通发票
         * TruckLength : 17.5
         * BearerRate : false
         * ToTime : 1441696638
         * OwnerId : 10013
         * PriceCalType : 整车
         * PayPoint : 3
         * Payees : [{"Status":1,"Area":310106,"Address":"华宁路2988号","Price":524.2,"Id":24,"OrderId":20105,"Mobile":"13341421526","Name":"隔壁老王","TransactionId":""},{"Status":1,"Area":110104,"Address":"西兰","Price":32.52,"Id":25,"OrderId":20105,"Mobile":"13341421526","Name":"隔壁老王","TransactionId":""}]
         * To : 0
         * Bearer : {"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original","Rate":0,"VehicleType":"危险品车","DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original","Latitude":0,"Longitude":0,"Name":"工商局","Plate":"沪A12345","VehicleLoad":100,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original","Score":0,"VehicleLength":18,"UserId":10004,"OrderAmount":0}
         * FromTime : 1441617436
         * GoodsAmount : 56.85
         * PaymentStatus : 0
         * ToName :
         * Status : 1
         * BearerId : 10004
         * BearerUser : {"Status":1,"CreateTime":"2015-07-29T17:09:15","Id":10004,"Mobile":"18656315210","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/42564f06-f39a-47b9-bd3d-9e37acecd3c7/original"}
         * CreateTime : 1441613287
         * From : 310102
         * BidsAmount : 0
         * OwnerRate : false
         * ToNumber :
         * DealPrice : 0.0
         * Price : 10.05
         * TruckType : 集装箱车
         * PayMethod : 1
         * Id : 20105
         * FromDetail : 漕河泾
         * PriceType : 元/包车
         */
        private OwnerUserEntity OwnerUser;
        private String GoodsType;
        private String Message;
        private String GoodsUnit;
        private String ToDetail;
        private String Tax;
        private double TruckLength;
        private boolean BearerRate;
        private int ToTime;
        private int OwnerId;
        private String PriceCalType;
        private int PayPoint;
        private List<PayeesEntity> Payees;
        private int To;
        private BidEntity Bid;
        private BearerEntity Bearer;
        private int FromTime;
        private double GoodsAmount;
        private int PaymentStatus;
        private String ToName;
        private int Status;
        private int BearerId;
        private BearerUserEntity BearerUser;
        public BearerCompanyEntity BearerCompany;
        private int CreateTime;
        private int From;
        private int BidsAmount;
        private boolean OwnerRate;
        private String ToNumber;
        private double DealPrice;
        private double Price;
        private String TruckType;
        private int PayMethod;
        private int Id;
        private String FromDetail;
        private String PriceType;

        public BearerCompanyEntity getBearerCompany() {
            return BearerCompany;
        }

        public void setBearerCompany(BearerCompanyEntity bearerCompany) {
            BearerCompany = bearerCompany;
        }

        public void setOwnerUser(OwnerUserEntity OwnerUser) {
            this.OwnerUser = OwnerUser;
        }

        public void setGoodsType(String GoodsType) {
            this.GoodsType = GoodsType;
        }

        public void setMessage(String Message) {
            this.Message = Message;
        }

        public void setGoodsUnit(String GoodsUnit) {
            this.GoodsUnit = GoodsUnit;
        }

        public void setToDetail(String ToDetail) {
            this.ToDetail = ToDetail;
        }

        public void setTax(String Tax) {
            this.Tax = Tax;
        }

        public void setBid(BidEntity Bid) {
            this.Bid = Bid;
        }

        public void setTruckLength(double TruckLength) {
            this.TruckLength = TruckLength;
        }

        public void setBearerRate(boolean BearerRate) {
            this.BearerRate = BearerRate;
        }

        public void setToTime(int ToTime) {
            this.ToTime = ToTime;
        }

        public void setOwnerId(int OwnerId) {
            this.OwnerId = OwnerId;
        }

        public void setPriceCalType(String PriceCalType) {
            this.PriceCalType = PriceCalType;
        }

        public void setPayPoint(int PayPoint) {
            this.PayPoint = PayPoint;
        }

        public void setPayees(List<PayeesEntity> Payees) {
            this.Payees = Payees;
        }

        public void setTo(int To) {
            this.To = To;
        }

        public void setBearer(BearerEntity Bearer) {
            this.Bearer = Bearer;
        }

        public void setFromTime(int FromTime) {
            this.FromTime = FromTime;
        }

        public void setGoodsAmount(double GoodsAmount) {
            this.GoodsAmount = GoodsAmount;
        }

        public void setPaymentStatus(int PaymentStatus) {
            this.PaymentStatus = PaymentStatus;
        }

        public void setToName(String ToName) {
            this.ToName = ToName;
        }

        public void setStatus(int Status) {
            this.Status = Status;
        }

        public void setBearerId(int BearerId) {
            this.BearerId = BearerId;
        }

        public void setBearerUser(BearerUserEntity BearerUser) {
            this.BearerUser = BearerUser;
        }

        public void setCreateTime(int CreateTime) {
            this.CreateTime = CreateTime;
        }

        public void setFrom(int From) {
            this.From = From;
        }

        public void setBidsAmount(int BidsAmount) {
            this.BidsAmount = BidsAmount;
        }

        public void setOwnerRate(boolean OwnerRate) {
            this.OwnerRate = OwnerRate;
        }

        public void setToNumber(String ToNumber) {
            this.ToNumber = ToNumber;
        }

        public void setDealPrice(double DealPrice) {
            this.DealPrice = DealPrice;
        }

        public void setPrice(double Price) {
            this.Price = Price;
        }

        public void setTruckType(String TruckType) {
            this.TruckType = TruckType;
        }

        public void setPayMethod(int PayMethod) {
            this.PayMethod = PayMethod;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public void setFromDetail(String FromDetail) {
            this.FromDetail = FromDetail;
        }

        public void setPriceType(String PriceType) {
            this.PriceType = PriceType;
        }

        public OwnerUserEntity getOwnerUser() {
            return OwnerUser;
        }

        public String getGoodsType() {
            return GoodsType;
        }

        public String getMessage() {
            return Message;
        }

        public String getGoodsUnit() {
            return GoodsUnit;
        }

        public String getToDetail() {
            return ToDetail;
        }

        public String getTax() {
            return Tax;
        }

        public double getTruckLength() {
            return TruckLength;
        }

        public boolean isBearerRate() {
            return BearerRate;
        }

        public int getToTime() {
            return ToTime;
        }

        public int getOwnerId() {
            return OwnerId;
        }

        public String getPriceCalType() {
            return PriceCalType;
        }

        public int getPayPoint() {
            return PayPoint;
        }

        public List<PayeesEntity> getPayees() {
            return Payees;
        }

        public int getTo() {
            return To;
        }

        public BidEntity getBid() {
            return Bid;
        }

        public BearerEntity getBearer() {
            return Bearer;
        }

        public int getFromTime() {
            return FromTime;
        }

        public double getGoodsAmount() {
            return GoodsAmount;
        }

        public int getPaymentStatus() {
            return PaymentStatus;
        }

        public String getToName() {
            return ToName;
        }

        public int getStatus() {
            return Status;
        }

        public int getBearerId() {
            return BearerId;
        }

        public BearerUserEntity getBearerUser() {
            return BearerUser;
        }

        public int getCreateTime() {
            return CreateTime;
        }

        public int getFrom() {
            return From;
        }

        public int getBidsAmount() {
            return BidsAmount;
        }

        public boolean isOwnerRate() {
            return OwnerRate;
        }

        public String getToNumber() {
            return ToNumber;
        }

        public double getDealPrice() {
            return DealPrice;
        }

        public double getPrice() {
            return Price;
        }

        public String getTruckType() {
            return TruckType;
        }

        public int getPayMethod() {
            return PayMethod;
        }

        public int getId() {
            return Id;
        }

        public String getFromDetail() {
            return FromDetail;
        }

        public String getPriceType() {
            return PriceType;
        }

        public static class BearerCompanyEntity {
            public int UserId;
            public String LegalPerson;
            public String LegalPersonId;
            public String CompanyName;
            public String BusinessLicence;
            public String LegalPersonFront;
            public String LegalPersonBack;

            public int getUserId() {
                return UserId;
            }

            public void setUserId(int userId) {
                UserId = userId;
            }

            public String getLegalPerson() {
                return LegalPerson;
            }

            public void setLegalPerson(String legalPerson) {
                LegalPerson = legalPerson;
            }

            public String getLegalPersonId() {
                return LegalPersonId;
            }

            public void setLegalPersonId(String legalPersonId) {
                LegalPersonId = legalPersonId;
            }

            public String getCompanyName() {
                return CompanyName;
            }

            public void setCompanyName(String companyName) {
                CompanyName = companyName;
            }

            public String getBusinessLicence() {
                return BusinessLicence;
            }

            public void setBusinessLicence(String businessLicence) {
                BusinessLicence = businessLicence;
            }

            public String getLegalPersonFront() {
                return LegalPersonFront;
            }

            public void setLegalPersonFront(String legalPersonFront) {
                LegalPersonFront = legalPersonFront;
            }

            public String getLegalPersonBack() {
                return LegalPersonBack;
            }

            public void setLegalPersonBack(String legalPersonBack) {
                LegalPersonBack = legalPersonBack;
            }
        }

        public static class OwnerUserEntity {
            /**
             * Status : 1
             * CreateTime : 2015-09-06T17:51:49
             * Id : 10013
             * Mobile : 15371452845
             * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/31156b87-639e-4dd7-b5a7-5e511efad8f8/original
             */
            private int Status;
            private int CompanyStatus  = -1;
            private String CreateTime;
            private int Id;
            private String Mobile;
            private String Avatar;

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setMobile(String Mobile) {
                this.Mobile = Mobile;
            }

            public void setAvatar(String Avatar) {
                this.Avatar = Avatar;
            }

            public int getStatus() {
                return Status;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public int getId() {
                return Id;
            }

            public String getMobile() {
                return Mobile;
            }

            public String getAvatar() {
                return Avatar;
            }

            public int getCompanyStatus() {
                return CompanyStatus;
            }

            public void setCompanyStatus(int companyStatus) {
                CompanyStatus = companyStatus;
            }
        }

        public static class PayeesEntity {
            /**
             * Status : 1
             * Area : 310106
             * Address : 华宁路2988号
             * Price : 524.2
             * Id : 24
             * OrderId : 20105
             * Mobile : 13341421526
             * Name : 隔壁老王
             * TransactionId :
             */
            private int Status;
            private int Area;
            private String Address;
            private double Price;
            private int Id;
            private int OrderId;
            private String Mobile;
            private String Name;
            private String TransactionId;

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public void setArea(int Area) {
                this.Area = Area;
            }

            public void setAddress(String Address) {
                this.Address = Address;
            }

            public void setPrice(double Price) {
                this.Price = Price;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setOrderId(int OrderId) {
                this.OrderId = OrderId;
            }

            public void setMobile(String Mobile) {
                this.Mobile = Mobile;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public void setTransactionId(String TransactionId) {
                this.TransactionId = TransactionId;
            }

            public int getStatus() {
                return Status;
            }

            public int getArea() {
                return Area;
            }

            public String getAddress() {
                return Address;
            }

            public double getPrice() {
                return Price;
            }

            public int getId() {
                return Id;
            }

            public int getOrderId() {
                return OrderId;
            }

            public String getMobile() {
                return Mobile;
            }

            public String getName() {
                return Name;
            }

            public String getTransactionId() {
                return TransactionId;
            }
        }

        public class BidEntity {
            /**
             * BearerId : 9
             * Price : 55000
             * Id : 14
             * OrderId : 50
             * Bearer : null
             */
            private int BearerId;
            private double Price;
            private int Id;
            private int OrderId;
            private String PriceType;

            public String getPriceType() {
                return PriceType;
            }

            public void setPriceType(String priceType) {
                PriceType = priceType;
            }

            public void setBearerId(int BearerId) {
                this.BearerId = BearerId;
            }

            public void setPrice(double Price) {
                this.Price = Price;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setOrderId(int OrderId) {
                this.OrderId = OrderId;
            }


            public int getBearerId() {
                return BearerId;
            }

            public double getPrice() {
                return Price;
            }

            public int getId() {
                return Id;
            }

            public int getOrderId() {
                return OrderId;
            }
        }

        public static class BearerEntity {
            /**
             * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/226fd501-ee3e-4938-aa06-ad2305f6ce95/original
             * Rate : 0.0
             * VehicleType : 危险品车
             * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/95c22b38-537a-4b9e-9f88-d69ca241341a/original
             * Latitude : 0.0
             * Longitude : 0.0
             * Name : 工商局
             * Plate : 沪A12345
             * VehicleLoad : 100.0
             * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cbb16c40-6c30-4acf-b7be-7ed1efac30e1/original
             * Score : 0
             * VehicleLength : 18.0
             * UserId : 10004
             * OrderAmount : 0
             */
            private String VehicleLicense;
            private double Rate;
            private String VehicleType;
            private String DrivingLicense;
            private double Latitude;
            private double Longitude;
            private String Name;
            private String Plate;
            private double VehicleLoad;
            private String VehiclePhoto;
            private int Score;
            private double VehicleLength;
            private int UserId;
            private int OrderAmount;

            public void setVehicleLicense(String VehicleLicense) {
                this.VehicleLicense = VehicleLicense;
            }

            public void setRate(double Rate) {
                this.Rate = Rate;
            }

            public void setVehicleType(String VehicleType) {
                this.VehicleType = VehicleType;
            }

            public void setDrivingLicense(String DrivingLicense) {
                this.DrivingLicense = DrivingLicense;
            }

            public void setLatitude(double Latitude) {
                this.Latitude = Latitude;
            }

            public void setLongitude(double Longitude) {
                this.Longitude = Longitude;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public void setPlate(String Plate) {
                this.Plate = Plate;
            }

            public void setVehicleLoad(double VehicleLoad) {
                this.VehicleLoad = VehicleLoad;
            }

            public void setVehiclePhoto(String VehiclePhoto) {
                this.VehiclePhoto = VehiclePhoto;
            }

            public void setScore(int Score) {
                this.Score = Score;
            }

            public void setVehicleLength(double VehicleLength) {
                this.VehicleLength = VehicleLength;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public void setOrderAmount(int OrderAmount) {
                this.OrderAmount = OrderAmount;
            }

            public String getVehicleLicense() {
                return VehicleLicense;
            }

            public double getRate() {
                return Rate;
            }

            public String getVehicleType() {
                return VehicleType;
            }

            public String getDrivingLicense() {
                return DrivingLicense;
            }

            public double getLatitude() {
                return Latitude;
            }

            public double getLongitude() {
                return Longitude;
            }

            public String getName() {
                return Name;
            }

            public String getPlate() {
                return Plate;
            }

            public double getVehicleLoad() {
                return VehicleLoad;
            }

            public String getVehiclePhoto() {
                return VehiclePhoto;
            }

            public int getScore() {
                return Score;
            }

            public double getVehicleLength() {
                return VehicleLength;
            }

            public int getUserId() {
                return UserId;
            }

            public int getOrderAmount() {
                return OrderAmount;
            }
        }
        public static class BearerUserEntity {
            /**
             * CreateTime : 2015-06-08T11:58:33
             * Id : 9
             * Mobile : 15121019915
             */
            private String CreateTime;
            private int Id;
            private int Status;
            private int CompanyStatus;
            private String Avatar;
            private String Mobile;

            public int getStatus() {
                return Status;
            }

            public void setStatus(int status) {
                Status = status;
            }

            public int getCompanyStatus() {
                return CompanyStatus;
            }

            public void setCompanyStatus(int companyStatus) {
                CompanyStatus = companyStatus;
            }

            public String getAvatar() {
                return Avatar;
            }

            public void setAvatar(String avatar) {
                Avatar = avatar;
            }

            public void setCreateTime(String CreateTime) {
                this.CreateTime = CreateTime;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public void setMobile(String Mobile) {
                this.Mobile = Mobile;
            }

            public String getCreateTime() {
                return CreateTime;
            }

            public int getId() {
                return Id;
            }

            public String getMobile() {
                return Mobile;
            }
        }
    }

    public static class PageEntity {
        /**
         * PageSize : 10
         * Page : 1
         * Count : 12
         */
        private int PageSize;
        private int Page;
        private int Count;

        public void setPageSize(int PageSize) {
            this.PageSize = PageSize;
        }

        public void setPage(int Page) {
            this.Page = Page;
        }

        public void setCount(int Count) {
            this.Count = Count;
        }

        public int getPageSize() {
            return PageSize;
        }

        public int getPage() {
            return Page;
        }

        public int getCount() {
            return Count;
        }
    }
    public static class TypeCountEntity {
        /**
         * news : 10
         * shipping : 1
         * done : 12
         */
        private int news;
        private int shipping;
        private int done;

        public int getNews() {
            return news;
        }

        public void setNews(int news) {
            this.news = news;
        }

        public int getShipping() {
            return shipping;
        }

        public void setShipping(int shipping) {
            this.shipping = shipping;
        }

        public int getDone() {
            return done;
        }

        public void setDone(int done) {
            this.done = done;
        }
    }
}
