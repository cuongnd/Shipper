package org.csware.ee.utils;

import android.content.Context;

import net.tsz.afinal.FinalDb;

/**
 * Created by Atwind on 2015/5/14.
 */
public class DbManager {

    //数据库名称
    public static final String DbName = "ee.db";
    //数据库版本
    public static final int DbVersion = 1;

//    private static Context context;
//    /**
//     * 重置DB对像上下文
//     */
//    public static void resetContext(Context cotx) {
//        context = cotx;
//    }

    /**
     * 获得一个可用的数据库
     *
     * @param content
     * @return
     */
    public static FinalDb OpenDb(Context content) {
        return FinalDb.create(content, DbName, true, DbVersion, null);//默认开启了调试,升级时清空所有数据
    }


}
