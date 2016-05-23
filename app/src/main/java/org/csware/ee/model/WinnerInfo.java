package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
public class WinnerInfo extends Return {


    /**
     * RedPacket : {"Free":false}
     * Log : [{"Item":{"Amount":0.05},"UserId":10006,"Time":1448506436,"Id":91,"Mobile":"133*****257","Avatar":"","Name":"李"}]
     */
    public RedPacketEntity RedPacket;
    public List<LogEntity> Log;

    public static class RedPacketEntity {
        /**
         * Free : false
         */
        public boolean Free;
    }

    public static class LogEntity {
        /**
         * Item : {"Amount":0.05,"Type":2,"Desc":"" }
         * UserId : 10006
         * Time : 1448506436
         * Id : 91
         * Mobile : 133*****257
         * Avatar :
         * Name : 李
         */
        public ItemEntity Item;
        public int UserId;
        public int Time;
        public int Id;
        public String Mobile;
        public String Avatar;
        public String Name;

        public static class ItemEntity {
            /**
             * Amount : 0.05
             * Type : 2
             * Desc : ""
             */
            public String Amount;
            public int Type;
            public String Desc;
        }
    }
}
