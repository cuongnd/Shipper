package org.csware.ee.model;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.List;

/** 常用司机列表
 * Created by Yu on 2015/7/29.
 */
public class ContactListReturn extends Return {


    /**
     * Contacts : [{"VehicleLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/145ef10d-d4ba-4a50-ace9-232f36ada077/original","OwnerContactId":10019,"Rate":0,"VehicleType":"中栏车","CreateTime":1439549158,"DrivingLicense":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/553e652c-c3c2-4b5f-84ea-7d8af2c275b2/original","Latitude":31.170867,"Longitude":121.405996,"Mobile":"15121019915","Avatar":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/4f8e8d14-135c-418e-9682-888b945a1212/original","Plate":"沪A520131","Name":"毛哥","VehicleLoad":12,"VehiclePhoto":"http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cf3bc667-d6ab-4eec-a316-398987dd0dcf/original","Score":0,"VehicleLength":13,"UserId":20010,"OrderAmount":0}]
     */
    public List<ContactsEntity> Contacts;

    public static class ContactsEntity implements Serializable {
        /**
         * VehicleLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/145ef10d-d4ba-4a50-ace9-232f36ada077/original
         * OwnerContactId : 10019
         * Rate : 0.0
         * VehicleType : 中栏车
         * CreateTime : 1439549158
         * DrivingLicense : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/553e652c-c3c2-4b5f-84ea-7d8af2c275b2/original
         * Latitude : 31.170867
         * Longitude : 121.405996
         * Mobile : 15121019915
         * Avatar : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/4f8e8d14-135c-418e-9682-888b945a1212/original
         * Plate : 沪A520131
         * Name : 毛哥
         * VehicleLoad : 12.0
         * VehiclePhoto : http://elephant-10000961.image.myqcloud.com/elephant-10000961/0/cf3bc667-d6ab-4eec-a316-398987dd0dcf/original
         * Score : 0
         * VehicleLength : 13.0
         * UserId : 20010
         * OrderAmount : 0
         */
        public String VehicleLicense;
        public int OwnerContactId;
        public double Rate;
        public String VehicleType;
        public int CreateTime;
        public String DrivingLicense;
        public double Latitude;
        public double Longitude;
        public String Mobile;
        public String Avatar;
        public String Plate;
        public String Name;
        public double VehicleLoad;
        public String VehiclePhoto;
        public int Score;
        public double VehicleLength;
        public int UserId;
        public int OrderAmount;
        public boolean isSelect = false;
        public boolean Invited = false;
        public boolean Notified = false;
        public boolean Registed = false;
        public boolean Verified = false;
        public boolean InContact = false;
        public Bitmap bitmap;
        public BearerCompanyEntity BearerCompany;

        public static class BearerCompanyEntity {
            public int UserId;
            public String LegalPerson;
            public String LegalPersonId;
            public String CompanyName;
            public String BusinessLicence;
            public String LegalPersonFront;
            public String LegalPersonBack;
        }
    }
}
