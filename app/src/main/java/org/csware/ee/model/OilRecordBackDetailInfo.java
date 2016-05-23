package org.csware.ee.model;

/**
 * Created by zhoujiulong on 2016/4/21.
 */
public class OilRecordBackDetailInfo extends Return{


    public int Code;
    public ContentBean Content;

    public static class ContentBean {
        public  double Money;
        public String ApplyTime;
        public String DealTime;
        public String ResMessage;
        public int Status;
    }
}
