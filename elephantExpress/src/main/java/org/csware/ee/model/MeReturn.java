package org.csware.ee.model;

/**
 *
 * Owner 是认证相关信息，可能为null
 *
 * Created by Yu on 2015/7/21.
 */
public class MeReturn extends Return {


    /**
     * OwnerUser : {"Status":1,"CreateTime":"2015-11-24T10:39:21","CompanyStatus":1,"Id":10036,"Mobile":"13120780521","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c8ad06ec-bd35-4a1f-8485-17dcc611f6c7/original"}
     * Owner : {"Score":1260,"Rate":4,"UserId":10036,"OrderAmount":2,"VerifyMessage":"","Identity":"32032119890911****","Name":"王杰"}
     * OwnerCompany : {"CompanyName":"三个小矮人","LegalPersonBack":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9a7a3f22-1861-4cf0-9d8a-1c88893780da/original","BusinessLicence":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fe22d0fb-5bd2-4b67-b618-66b9902fe2dc/original","UserId":10036,"LegalPersonFront":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/d9ab1e03-2fa9-4a52-b76e-8edbaf5cac10/original","LegalPerson":"王杰","LegalPersonId":"320321198909114616"}
     */
    public OwnerUserEntity OwnerUser;
    public OwnerEntity Owner;
    public OwnerCompanyEntity OwnerCompany;

    public static class OwnerUserEntity {
        /**
         * Status : 1
         * CreateTime : 2015-11-24T10:39:21
         * CompanyStatus : 1
         * Id : 10036
         * Mobile : 13120780521
         * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/c8ad06ec-bd35-4a1f-8485-17dcc611f6c7/original
         */
        public int Status;
        public String CreateTime;
        public int CompanyStatus = -1;
        public int Id;
        public String Mobile;
        public String Avatar;
    }

    public static class OwnerEntity {
        /**
         * Score : 1260
         * Rate : 4.0
         * UserId : 10036
         * OrderAmount : 2
         * VerifyMessage :
         * Identity : 32032119890911****
         * Name : 王杰
         */
        public float Rate;
        public int UserId;
        public int OrderAmount;
        public String VerifyMessage;
        public String Identity;
        public String Name;
        public String Score;
    }

    public static class OwnerCompanyEntity {
        /**
         * CompanyName : 三个小矮人
         * LegalPersonBack : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/9a7a3f22-1861-4cf0-9d8a-1c88893780da/original
         * BusinessLicence : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/fe22d0fb-5bd2-4b67-b618-66b9902fe2dc/original
         * UserId : 10036
         * LegalPersonFront : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/d9ab1e03-2fa9-4a52-b76e-8edbaf5cac10/original
         * LegalPerson : 王杰
         * LegalPersonId : 320321198909114616
         */
        public String CompanyName;
        public String LegalPersonBack;
        public String BusinessLicence;
        public int UserId;
        public String LegalPersonFront;
        public String LegalPerson;
        public String LegalPersonId;
    }
}
