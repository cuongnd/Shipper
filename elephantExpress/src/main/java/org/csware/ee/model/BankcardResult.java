package org.csware.ee.model;

import java.util.List;

/**
 * Created by Yu on 2015/7/2.
 */
public class BankcardResult extends Return {
    /**
     * BankCards : [{"BankName":"招商银行","IsDefault":false,"CardNo":"as445656","UserId":5,"BankAddress":"招商银行","Id":5,"Name":"4554654"},{"BankName":"招商银行","IsDefault":false,"CardNo":"as445656","UserId":5,"BankAddress":"招商银行","Id":6,"Name":"4554654"}]
     */
    public List<BankCardsEntity> BankCards;

    public static class BankCardsEntity {
        /**
         * BankName : 招商银行
         * IsDefault : false
         * CardNo : as445656
         * UserId : 5
         * BankAddress : 招商银行
         * Id : 5
         * Name : 4554654
         */
        public String BankName;
        public boolean IsDefault;
        public String CardNo;
        public int UserId;
        public String BankAddress;
        public int Id;
        public String Name;
    }


//    public static class BankCard{
//        public String name;
//
//        public String bankName;
//
//        public String bankAddress;
//
//        public String cardNo;
//
//        public int Id;
//
//    }

}
