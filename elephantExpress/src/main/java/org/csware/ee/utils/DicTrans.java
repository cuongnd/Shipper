package org.csware.ee.utils;

import java.util.Map;

/**
 * Created by Atwind on 2015/5/18.
 */
public class DicTrans {

    final static String unknown = "未知错误";

    public static String getString(Map<String, String> dic, String key) {
        if (dic.containsKey(key)) return dic.get(key);
        return unknown;
    }

}
