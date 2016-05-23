package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/9/25.
 */
public class FlashInfo extends Return {


    /**
     * Sliders : [{"Target":"1","EndTime":"2015-09-30T10:55:03","StartTime":"2015-09-22T10:54:59","Id":1,"Source":"https://www.baidu.com/img/bdlogo.png"}]
     */
    private List<SlidersEntity> Sliders;

    public void setSliders(List<SlidersEntity> Sliders) {
        this.Sliders = Sliders;
    }

    public List<SlidersEntity> getSliders() {
        return Sliders;
    }

    public static class SlidersEntity {
        /**
         * Target : 1
         * EndTime : 2015-09-30T10:55:03
         * StartTime : 2015-09-22T10:54:59
         * Id : 1
         * Source : https://www.baidu.com/img/bdlogo.png
         */
        private String Target;
        private String EndTime;
        private String StartTime;
        private int Id;
        private String Source;

        public void setTarget(String Target) {
            this.Target = Target;
        }

        public void setEndTime(String EndTime) {
            this.EndTime = EndTime;
        }

        public void setStartTime(String StartTime) {
            this.StartTime = StartTime;
        }

        public void setId(int Id) {
            this.Id = Id;
        }

        public void setSource(String Source) {
            this.Source = Source;
        }

        public String getTarget() {
            return Target;
        }

        public String getEndTime() {
            return EndTime;
        }

        public String getStartTime() {
            return StartTime;
        }

        public int getId() {
            return Id;
        }

        public String getSource() {
            return Source;
        }
    }
}
