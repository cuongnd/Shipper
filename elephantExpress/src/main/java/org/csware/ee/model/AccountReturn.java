package org.csware.ee.model;

/**
 * Created by Yu on 2015/7/28.
 */
public class AccountReturn extends Return {

    /**
     * Account : {"UserId":5,"Amount":0,"Password":""}
     */
    public AccountEntity Account;

    public static class AccountEntity {
        /**
         * UserId : 5
         * Amount : 0
         * Password :
         */
        public int UserId;
        public float Amount;
        public String Password;
    }
}
