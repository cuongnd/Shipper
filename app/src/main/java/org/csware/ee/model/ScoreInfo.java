package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2015/11/26.
 */
public class ScoreInfo extends Return {


    /**
     * ScoreLogs : [{"Addon":"大转盘","UserId":10006,"Time":"2015-11-26T17:21:47+08:00","Id":143,"Change":-50,"Original":995410},{"Addon":"大转盘","UserId":10006,"Time":"2015-11-26T17:21:46+08:00","Id":142,"Change":-50,"Original":995410},{"Addon":"大转盘","UserId":10006,"Time":"2015-11-26T17:21:02+08:00","Id":141,"Change":50,"Original":995410},{"Addon":"大转盘","UserId":10006,"Time":"2015-11-26T17:21:01+08:00","Id":140,"Change":-50,"Original":995410},{"Addon":"大转盘","UserId":10006,"Time":"2015-11-26T17:07:57+08:00","Id":137,"Change":10,"Original":995450}]
     */
    public List<ScoreLogsEntity> ScoreLogs;

    public static class ScoreLogsEntity {
        /**
         * Addon : 大转盘
         * UserId : 10006
         * Time : 2015-11-26T17:21:47+08:00
         * Id : 143
         * Change : -50.0
         * Original : 995410.0
         */
        public String Addon;
        public int UserId;
        public String Time;
        public int Id;
        public double Change;
        public double Original;
    }
}
