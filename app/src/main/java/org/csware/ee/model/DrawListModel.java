package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class DrawListModel extends Return {


    public List<DrawsEntity> Draws;

    public static class DrawsEntity {
        public String AliPayAccount;
        public String AliPayName;
        public double Amount;
        public int ApplyTime;
        public String BankAddress;
        public String BankCardNo;
        public String BankName;
        public int CreateTime;
        public int Id;
        public int Status;
        public String TransactionId;
        public int TransactionTime;
        public int UserId;
    }
}
