package org.csware.ee.model;

import java.util.List;

/**
 * Created by zhojiulong on 2016/5/12.
 */
public class CouponListInfo extends Return {

    public List<CouponsBean> Coupons;

    public static class CouponsBean {
        public int Addon;
        public int CouponId;
        public int CreateTime;
        public int ExpireTime;
        public int Id;
        public InfoBean Info;
        public boolean IsExpired;
        public int Status;
        public String Target;
        public int UserId;
        public boolean isSelect = false;

        public static class InfoBean {
            public String Category;
            public String Description;
            public double Discount;
            public int Id;
            public int Limen;
            public String Name;
            public int Terminable;
            public int Timespan;
            public int Type;
        }
    }
}
