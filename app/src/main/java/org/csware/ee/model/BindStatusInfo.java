package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/5.
 */
public class BindStatusInfo extends Return {


    /**
     * PnrInfo : {"OwnerId":80021,"Identity":"**************4513","Password":"","Name":"傅忠凡"}
     * PnrCards : [{"CardNo":"***************0555","OwnerId":80021,"BankCode":"01050000","Id":2}]
     */
    public PnrInfoEntity PnrInfo;
    public List<PnrCardsEntity> PnrCards;

    public static class PnrInfoEntity {
        /**
         * OwnerId : 80021
         * Identity : **************4513
         * Password :
         * Name : 傅忠凡
         */
        public int OwnerId;
        public String Identity;
        public String Password;
        public String Name;
    }

    public static class PnrCardsEntity {
        /**
         * CardNo : ***************0555
         * OwnerId : 80021
         * BankCode : 01050000
         * Id : 2
         */
        public String CardNo;
        public int OwnerId;
        public String BankCode;
        public int Id;
    }
}
