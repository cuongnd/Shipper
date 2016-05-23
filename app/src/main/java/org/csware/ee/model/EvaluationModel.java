package org.csware.ee.model;

import java.util.List;

/**
 * Created by Administrator on 2016/5/4.
 */
public class EvaluationModel extends Return {


    public List<RatesEntity> Rates;

    public static class RatesEntity {
        public String Message;
        public String Name;
        public int OrderId;
        public double Star;
        public String Time;
        public int UserId;
    }
}
