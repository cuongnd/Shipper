package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/19.
 */
public class GameInfo extends Return {

    /**
     * GameInfo : [{"ImgSrc":0,"Status":true,"BeganTime":"10:00-12:00","GameName":"幸运大转盘","Id":1},{"ImgSrc":0,"Status":false,"BeganTime":"12:00-15:00","GameName":"幸运刮刮卡","Id":2},{"ImgSrc":0,"Status":false,"BeganTime":"18:00-20:00","GameName":"开红包","Id":3}]
     */
    public List<GameInfoEntity> GameInfo;
    /**
     * Games : [{"EndTime":24,"StartTime":0,"Title":"刮刮卡","Enabled":true,"EndDay":1469376000,"StartDay":1447084800,"Name":"ScratchCard"},{"EndTime":24,"StartTime":0,"Title":"大转盘","Enabled":true,"EndDay":1459440000,"StartDay":1446307200,"Name":"FortuneWheel"},{"EndTime":24,"StartTime":0,"Title":"红包","Enabled":true,"EndDay":1575043200,"StartDay":1445356800,"Name":"RedPacket"}]
     */
    public List<GamesEntity> Games;

    public static class GameInfoEntity {
        /**
         * ImgSrc : 0
         * ImgDefSrc : 0
         * Status : true
         * BeganTime : 10:00-12:00
         * GameName : 幸运大转盘
         * Id : 1
         */
        public int ImgDefSrc;
        public int ImgSrc;
        public boolean Status;
        public String BeganTime;
        public String GameName;
        public int Id;

        public int getImgDefSrc() {
            return ImgDefSrc;
        }

        public void setImgDefSrc(int imgDefSrc) {
            ImgDefSrc = imgDefSrc;
        }

        public int getImgSrc() {
            return ImgSrc;
        }

        public void setImgSrc(int imgSrc) {
            ImgSrc = imgSrc;
        }

        public boolean isStatus() {
            return Status;
        }

        public void setStatus(boolean status) {
            Status = status;
        }

        public String getBeganTime() {
            return BeganTime;
        }

        public void setBeganTime(String beganTime) {
            BeganTime = beganTime;
        }

        public String getGameName() {
            return GameName;
        }

        public void setGameName(String gameName) {
            GameName = gameName;
        }

        public int getId() {
            return Id;
        }

        public void setId(int id) {
            Id = id;
        }
    }

    public static class GamesEntity {
        /**
         * EndTime : 24
         * StartTime : 0
         * Title : 刮刮卡
         * Enabled : true
         * EndDay : 1469376000
         * StartDay : 1447084800
         * Name : ScratchCard
         */
        public int EndTime;
        public int StartTime;
        public String Title;
        public boolean Enabled;
        public int EndDay;
        public int StartDay;
        public String Name;
    }
}
