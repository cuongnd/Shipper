package org.csware.ee.utils;

import java.text.DecimalFormat;

/**
 * Created by Yu on 2015/7/13.
 */
public class FormatHelper {


    /**将Double格式化为两位小数的字符串*/
    public static String toMoney(double d){
        DecimalFormat df = new DecimalFormat("0.00");
        return df.format(d);
    }

}
