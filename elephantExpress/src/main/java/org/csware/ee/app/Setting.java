package org.csware.ee.app;

/**
 * 系统设置信息，保存至数据库
 * Created by Yu on 2015/6/30.
 */
public  class Setting {

    public Setting(){
        MESSAGE = new Message();
    }

    public Message MESSAGE;


    public static class USER {

    }

    /**
     * 消息
     */
    public static class Message {

        public Message(){
            NOTIFY = true;
            SOUND  = true;
        }

        /**
         * 通知可用否
         */
        public  boolean NOTIFY;
        /**
         * 发出声音通知
         */
        public  boolean SOUND;
        /**
         * 震动通知
         */
        public  boolean SHARK;

    }

}
