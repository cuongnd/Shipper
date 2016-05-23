package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/12/10.
 */
public class ContactsModel extends Return {


    /**
     * Contacts : [{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"13111415241"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"13345645879"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"15165421453"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"15364587471"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"15374542456"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"15615241411"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"17715321457"},{"Notified":false,"Verified":false,"Registed":false,"Invited":false,"Mobile":"18932541458"}]
     */
    public List<ContactsEntity> Contacts;

    public static class ContactsEntity {
        /**
         * Notified : false
         * Verified : false
         * Registed : false
         * Invited : false
         * Mobile : 13111415241
         */
        public boolean Notified;
        public boolean Verified;
        public boolean Registed;
        public boolean Invited;
        public boolean InContact;
        public String Mobile;
    }
}
