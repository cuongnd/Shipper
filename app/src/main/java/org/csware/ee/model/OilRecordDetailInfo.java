package org.csware.ee.model;

/**
 * Created by zhoujiulong on 2016/4/21.
 */
public class OilRecordDetailInfo extends Return{

    public int Code;
    public ContentBean Content;

    public static class ContentBean {
        public String CreateTime;
        public String Mobile;
        public double Money;
        public String Name;
        public String OilCardNo;
        //1-中石油 2-中石化
        public int OilCardType;
        public String OrderInfo;
        public String PayName;
        public String PayNo;

    }
}
