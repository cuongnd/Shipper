package org.csware.ee.utils;

import android.content.Context;

import org.csware.ee.Guard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Formatter;
import java.util.UUID;

/**
 * Created by Yu on 2015/5/26.
 */
public class ParamTool {

    /**
     * 自动截取字符串
     * ...
     */
    public static String cutString(String name, int length) {
        return cutString(name, length, "...");
    }

    /**
     * 自动截取字符串
     */
    public static String cutString(String name, int length, String omit) {
        if (length < 2) length = 2;//自动纠正错误
        if (Guard.isNullOrEmpty(name)) return null;
        if (name.length() <= length) return name;
        return name.substring(0, length - 1) + omit;
    }

    /**
     * 获取元素在数组中的索引
     */
    public static int getIndex(String aim, String[] array) {
        if (Guard.isNullOrEmpty(aim) || array == null || array.length == 0) return -1;
        for (int i = 0; i < array.length; i++) {
            if (aim.equals(array[i])) return i;
        }
        return -1;
    }


    /**
     * 获取现在时间
     *
     * @return 返回时间类型 yyyy-MM-dd HH:mm:ss
     */
    public static Date getNowDate() {
        Date currentTime = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateString = formatter.format(currentTime);
        ParsePosition pos = new ParsePosition(8);
        Date currentTime_2 = formatter.parse(dateString, pos);
        return currentTime_2;
    }

    public static int getTimeSeconds(String dateTime) {
        if (Guard.isNullOrEmpty(dateTime)) return -1;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            ParsePosition pos = new ParsePosition(0);
            Date date = sdf.parse(dateTime, pos);
            //Date start = sdf.parse("1970-1-1 0:00:00",pos);
            int seconds = (int) ((date.getTime()) / 1000);
            return seconds;
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }

    public static int getTimeSeconds(Date date) {
        int seconds = (int) ((date.getTime()) / 1000);
        return seconds;
    }

    public static String fromTimeSeconds(int seconds, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return fromTimeSeconds(seconds, sdf);
    }

    public static String fromTimeSeconds(int seconds) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH时");
        return fromTimeSeconds(seconds, sdf);
    }

    public static String fromTimeSeconds(int seconds, SimpleDateFormat format) {
        if (seconds < 1) return null;
        Date date = new java.util.Date(seconds * 1000l);
        return format.format(date);
    }

    public static Date toDate(int seconds) {
        if (seconds < 1) return null;
        Date date = new java.util.Date(seconds * 1000l);
        return date;
    }


    /**
     * 转为int类型，默认为0
     *
     * @param o
     * @return
     */
    public static int toInt(Object o) {
        if (Guard.isNull(o)) return 0;
        return toInt(o.toString());
    }


    /**
     * 转为int类型，默认为0
     */
    public static int toInt(String s) {
        if (Guard.isNullOrEmpty(s))
            return 0;
        return Integer.parseInt(s);
    }

    public static String toString(int d) {
        return Integer.toString(d);
    }

    public static String toString(double d) {
        return Double.toString(d);
    }

    /**
     * 转为double
     */
    public static double toDouble(Object o) {
        if (Guard.isNull(o)) return 0;
        return toDouble(o.toString());
    }

    /**
     * 转为double
     */
    public static double toDouble(String str) {
        if (Guard.isNullOrEmpty(str)) return 0;
        return Double.parseDouble(str);
    }


    /**
     * dip/px像素单位转换
     */
    public static int dip2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue / scale + 0.5f);
    }

    /**
     * dip/px像素单位转换
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue * scale + 0.5f);
    }


    /**join*/
    public static String join( Object[] o , String flag ){
        StringBuilder str_buff = new StringBuilder();
        for(int i=0 , len=o.length ; i<len ; i++){
            str_buff.append( String.valueOf( o[i] ) );
            if(i<len-1)str_buff.append( flag );
        }
        return str_buff.toString();
    }




    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }


    String urlEncoding(String str) {
        if (Guard.isNull(str)) return "";
        try {
            return URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
