package org.csware.ee.model;


import java.util.List;

/**
 * Created by zhojiulong on 2016/4/8.
 */
public class RecordDetailInfo extends Return {


    public AccountLogBean AccountLog;

    public static class AccountLogBean {
        public String Addon;
        public double Change;
        public int Id;
        public OrderBean Order;
        public OrderPayBean OrderPay;
        public OrderPayeeBean OrderPayee;
        public OrderPayeePayBean OrderPayeePay;
        public PayerBean Payer;
        public double Original;
        public int Time;
        public int Type;
        public int UserId;
        public Object BearerDraw;
        public OwnerDrawBean OwnerDraw;

        public static class OwnerDrawBean {

            public int Id;
            public int UserId;
            public int Status;
            public String AliPayName;
            public String AliPayAccount;
            public String BankName;
            public String BankAddress;
            public String BankCardNo;
            public double Amount;
            public String CreateTime;
            public String ApplyTime;
            public String TransactionTime;
            public String TransactionId;
            public int SyncStatus;
            public String Reason;
        }

        public static class OrderBean {
            public BearerBean Bearer;
            public BearerCompanyBean BearerCompany;
            public int BearerId;
            public boolean BearerRate;
            public BearerUserBean BearerUser;
            public int BidsAmount;
            public int CreateTime;
            public double DealPrice;
            public int From;
            public String FromDetail;
            public int FromTime;
            public double GoodsAmount;
            public String GoodsType;
            public String GoodsUnit;
            public int Id;
            public String Images;
            public String Insurance;
            public int MainId;
            public String Message;
            public OwnerBean Owner;
            public OwnerCompanyBean OwnerCompany;
            public int OwnerId;
            public boolean OwnerRate;
            public OwnerUserBean OwnerUser;
            public double Paid;
            public int PayMethod;
            public int PayPoint;
            public int PaymentStatus;
            public double Price;
            public String PriceCalType;
            public String PriceType;
            public int Status;
            public String Tax;
            public int To;
            public String ToDetail;
            public String ToName;
            public String ToNumber;
            public int ToTime;
            public double TruckLength;
            public String TruckType;
            public List<?> Evidences;

            public static class BearerBean {
                public String DrivingLicense;
                public double Latitude;
                public double Longitude;
                public String Name;
                public int OrderAmount;
                public String Plate;
                public double Rate;
                public int Score;
                public int UserId;
                public double VehicleLength;
                public String VehicleLicense;
                public int VehicleLoad;
                public String VehiclePhoto;
                public String VehicleType;
                public String VerifyMessage;
            }

            public static class BearerCompanyBean {
                public String BusinessLicence;
                public String CompanyName;
                public String LegalPerson;
                public String LegalPersonBack;
                public String LegalPersonFront;
                public String LegalPersonId;
                public int UserId;
            }

            public static class BearerUserBean {
                public String Avatar;
                public int CompanyStatus;
                public int CreateTime;
                public int Id;
                public String Mobile;
                public int Status;
            }

            public static class OwnerUserBean {
                public String Avatar;
                public int CompanyStatus;
                public int CreateTime;
                public int Id;
                public String Mobile;
                public int Status;
            }

            public static class OwnerBean {
                public String Identity;
                public String Name;
                public int OrderAmount;
                public double Rate;
                public int Score;
                public int UserId;
                public String VerifyMessage;

            }

            public static class OwnerCompanyBean {
                public String BusinessLicence;
                public String CompanyName;
                public String LegalPerson;
                public String LegalPersonBack;
                public String LegalPersonFront;
                public String LegalPersonId;
                public int UserId;
            }


        }

        public static class OrderPayBean {
            public int CreateTime;
            public int DealTime;
            public int Id;
            public String Message;
            public int OrderId;
            public int OwnerId;
            public double Price;
            public String TransactionFrom;
            public String TransactionId;
            public String TransactionInfo;
        }

        public static class OrderPayeeBean {

            public String Address;
            public int Area;
            public int Id;
            public String Mobile;
            public String Name;
            public int OrderId;
            public double Paid;
            public double Price;
            public int Status;
            public String TransactionFrom;
            public String TransactionId;

        }

        public static class OrderPayeePayBean {
            public int CreateTime;
            public int DealTime;
            public int Id;
            public String Message;
            public int OrderId;
            public int OwnerId;
            public int OrderPayeeId;
            public double Price;
            public String TransactionFrom;
            public String TransactionId;
            public String TransactionInfo;
        }

        public static class PayerBean {
            public OwnerBean Owner;
            public OwnerUserBean OwnerUser;
            public OwnerCompanyBean OwnerCompany;


            public static class OwnerUserBean {
                public String Avatar;
                public int CompanyStatus;
                public int CreateTime;
                public int Id;
                public String Mobile;
                public int Status;
            }

            public static class OwnerBean {
                public String Identity;
                public String Name;
                public int OrderAmount;
                public double Rate;
                public int Score;
                public int UserId;
                public String VerifyMessage;

            }

            public static class OwnerCompanyBean {
                public String BusinessLicence;
                public String CompanyName;
                public String LegalPerson;
                public String LegalPersonBack;
                public String LegalPersonFront;
                public String LegalPersonId;
                public int UserId;
            }
        }

    }
}
