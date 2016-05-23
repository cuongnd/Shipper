package org.csware.ee.model;

import java.util.List;

/**
 * Created by Yu on 2015/6/3.
 * 订单列表返回结果
 */
public class OrderListReturnOld extends Return {


    /**
     * Orders : [{"OwnerUser":{"CreateTime":"2015-05-18T13:54:13","Id":5,"Mobile":"18601676525"},"GoodsType":"机械设备","Message":" only. for. test","GoodsUnit":"吨","ToDetail":"上海市,县,崇明县\nqmx","Tax":"增值税发票(6%)","TruckLength":"18米","ToTime":1441596045,"OwnerId":5,"PriceCalType":"零担","PayPoint":2,"To":310230,"Bid":{"BearerId":9,"Price":55000,"Id":14,"OrderId":50,"Bearer":null},"Bearer":{"Status":1,"Score":0,"UserId":9,"Photo":"","Avatar":"","Plate":"苏NB8888","Name":"牛师傅"},"FromTime":1436325641,"GoodsAmount":418,"PaymentStatus":0,"ToName":"wang","Status":2,"BearerId":9,"BearerUser":{"CreateTime":"2015-06-08T11:58:33","Id":9,"Mobile":"15121019915"},"CreateTime":1436240183,"From":310107,"BidsAmount":1,"ToNumber":"15687456525","Price":50000,"DealPrice":0,"TruckType":"危险品车","PayMethod":0,"Id":50,"FromDetail":"上海市,市辖区,普陀区\nsh ptq","PriceType":"包车价","Track":null},{"OwnerUser":{"CreateTime":"2015-05-18T13:54:13","Id":5,"Mobile":"18601676525"},"GoodsType":"机械设备","Message":"","GoodsUnit":"吨","ToDetail":"上海市,市辖区,浦东新区\nsjsjakaj","Tax":"增值税发票(11%)","TruckLength":"18米","ToTime":1448438486,"OwnerId":5,"PriceCalType":"","PayPoint":1,"To":310115,"Bearer":null,"FromTime":1436083284,"GoodsAmount":500,"PaymentStatus":0,"ToName":"","Status":2,"BearerId":0,"BearerUser":null,"CreateTime":1435722063,"From":310101,"BidsAmount":2,"ToNumber":"","Price":500,"DealPrice":0,"TruckType":"高栏车","PayMethod":1,"Id":46,"FromDetail":"上海市,市辖区,黄浦区\njajsjwk","PriceType":"包车价","Track":null}]
     * Page : {"PageSize":10,"Page":1,"Count":2}
     */
    private List<OrdersEntity> Orders;
    private PageEntity Page;

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
         * OwnerUser : {"CreateTime":"2015-05-18T13:54:13","Id":5,"Mobile":"18601676525"}
         * GoodsType : 机械设备
         * Message :  only. for. test
         * GoodsUnit : 吨
         * ToDetail : 上海市,县,崇明县
         qmx
         * Tax : 增值税发票(6%)
         * TruckLength : 18米
         * ToTime : 1441596045
         * OwnerId : 5
         * PriceCalType : 零担
         * PayPoint : 2
         * To : 310230
         * Bid : {"BearerId":9,"Price":55000,"Id":14,"OrderId":50,"Bearer":null}
         * Bearer : {"Status":1,"Score":0,"UserId":9,"Photo":"","Avatar":"","Plate":"苏NB8888","Name":"牛师傅"}
         * FromTime : 1436325641
         * GoodsAmount : 418
         * PaymentStatus : 0
         * ToName : wang
         * Status : 2
         * BearerId : 9
         * BearerUser : {"CreateTime":"2015-06-08T11:58:33","Id":9,"Mobile":"15121019915"}
         * CreateTime : 1436240183
         * From : 310107
         * BidsAmount : 1
         * ToNumber : 15687456525
         * Price : 50000
         * DealPrice : 0
         * TruckType : 危险品车
         * PayMethod : 0
         * Id : 50
         * FromDetail : 上海市,市辖区,普陀区
         sh ptq
         * PriceType : 包车价
         * Track : null
         */
        private OwnerUserEntity OwnerUser;
        private String GoodsType;
        private String Message;
        private String GoodsUnit;
        private String ToDetail;
        private String Tax;
        private String TruckLength;
        private int ToTime;
        private int OwnerId;
        private String PriceCalType;
        private int PayPoint;
        private int To;
        private BidEntity Bid;
        private BearerEntity Bearer;
        private int FromTime;
        private int PaymentStatus;
        private String ToName;
        private String GoodsAmount;
        private int Status;
        private int BearerId;
        private BearerUserEntity BearerUser;
        private int CreateTime;
        private int From;
        private int BidsAmount;
        private String ToNumber;
        private double Price;
        private double DealPrice;
        private String TruckType;
        private int PayMethod;
        private int Id;
        private String FromDetail;
        private String PriceType;
        private String Track;

        public String getGoodsAmount() {
            return GoodsAmount;
        }

        public void setGoodsAmount(String goodsAmount) {
            GoodsAmount = goodsAmount;
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

        public void setTruckLength(String TruckLength) {
            this.TruckLength = TruckLength;
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

        public void setTo(int To) {
            this.To = To;
        }

        public void setBid(BidEntity Bid) {
            this.Bid = Bid;
        }

        public void setBearer(BearerEntity Bearer) {
            this.Bearer = Bearer;
        }

        public void setFromTime(int FromTime) {
            this.FromTime = FromTime;
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

        public void setToNumber(String ToNumber) {
            this.ToNumber = ToNumber;
        }

        public void setPrice(int Price) {
            this.Price = Price;
        }

        public void setDealPrice(double DealPrice) {
            this.DealPrice = DealPrice;
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

        public void setTrack(String Track) {
            this.Track = Track;
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

        public String getTruckLength() {
            return TruckLength;
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

        public String getToNumber() {
            return ToNumber;
        }

        public double getPrice() {
            return Price;
        }

        public double getDealPrice() {
            return DealPrice;
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

        public String getTrack() {
            return Track;
        }

        public class OwnerUserEntity {
            /**
             * CreateTime : 2015-05-18T13:54:13
             * Id : 5
             * Mobile : 18601676525
             */
            private String CreateTime;
            private int Id;
            private String Mobile;

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
            private String Bearer;

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

            public void setBearer(String Bearer) {
                this.Bearer = Bearer;
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

            public String getBearer() {
                return Bearer;
            }
        }

        public  static class BearerEntity {
            /**
             * Status : 1
             * Score : 0
             * UserId : 9
             * Photo :
             * Avatar :
             * Plate : 苏NB8888
             * Name : 牛师傅
             */
            private int Status;
            private int Score;
            private int UserId;
            private String Photo;
            private String Avatar;
            private String Plate;
            private String Name;

            public void setStatus(int Status) {
                this.Status = Status;
            }

            public void setScore(int Score) {
                this.Score = Score;
            }

            public void setUserId(int UserId) {
                this.UserId = UserId;
            }

            public void setPhoto(String Photo) {
                this.Photo = Photo;
            }

            public void setAvatar(String Avatar) {
                this.Avatar = Avatar;
            }

            public void setPlate(String Plate) {
                this.Plate = Plate;
            }

            public void setName(String Name) {
                this.Name = Name;
            }

            public int getStatus() {
                return Status;
            }

            public int getScore() {
                return Score;
            }

            public int getUserId() {
                return UserId;
            }

            public String getPhoto() {
                return Photo;
            }

            public String getAvatar() {
                return Avatar;
            }

            public String getPlate() {
                return Plate;
            }

            public String getName() {
                return Name;
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
            private String Mobile;

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
         * Count : 2
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
}
