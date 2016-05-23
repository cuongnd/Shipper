package org.csware.ee;

import android.text.TextUtils;

import java.util.List;

/**
 * Created by Atwind on 2015/5/14.
 * 参数拦截
 */
public class Guard {

    public static <T> void NotNull(String paramName, T param) {
        if (param == null)
            throw new IllegalArgumentException("the parameter " + paramName + " can not be null.");
    }


    /**
     * 集合是否为空或null
     */
    public static <T> Boolean IsNullOrEmpty(List<T> list) {
        return list == null || list.size() == 0;
    }

    /**
     * 判断输入的字符串参数是否为空 or null
     * @return boolean 空则返回true,非空则flase
     */
    public static boolean isNullOrEmpty(String input) {
        return input == null || isEmpty(input) || input.equals("null");
        //return null==input || 0==input.length() || 0==input.replaceAll("\\s", "").length();
    }

    public static boolean isEmpty(String str)
    {
        return TextUtils.isEmpty(str);
    }

    /**是否为null串*/
    public static boolean isNull(String str)
    {
        return str == null;
    }


    /**
     * 判断输入的字节数组是否为空 or null
     * @return boolean 空则返回true,非空则flase
     */
    public static boolean isNullOrEmpty(byte[] bytes){
        return null==bytes || 0==bytes.length;
    }

    /**是否为null*/
    public static boolean isNull(Object obj){
        return obj == null;
    }

}
