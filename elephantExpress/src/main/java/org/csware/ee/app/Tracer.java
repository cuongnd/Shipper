package org.csware.ee.app;

import android.util.Log;

/**
 * 调试根踪使用
 * Created by Yu on 2015/8/3.
 */
public class Tracer {

    /**
     * 是否使用内网通信且调试，使用外网通信时发单数据会清空掉
     */
    public static boolean isUseDebugNet() {
        return 1==2;
    }

    /**
     * 当前应用是否为发行版本
     */
    public static boolean isRelease() {

        //return !BuildConfig.DEBUG;

        return true;
    }

    public static String getDebugText() {
        if (!isRelease()) {
            StringBuilder sb = new StringBuilder();
            if (isDebug())
                sb.append("DEBUG");
            if (isUseDebugNet())
                sb.append("[内]");
            else
                sb.append("[外]");
            return sb.toString();
        }
        return "";
    }

    /**
     * 是否输出调试日志
     */
    public static boolean isDebug() {
        return true;
    }

    public static void i(String tag, String string) {
        if (isDebug()) Log.i(tag, string);
    }

    public static void e(String tag, String string) {
        if (isDebug()) Log.e(tag, string);
    }

    public static void d(String tag, String string) {
        if (isDebug()) Log.d(tag, string);
    }

    public static void v(String tag, String string) {
        if (isDebug()) Log.v(tag, string);
    }

    public static void w(String tag, String string) {
        if (isDebug()) Log.w(tag, string);
    }


}
