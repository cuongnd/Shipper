package org.csware.ee.utils;

import android.content.Context;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.orhanobut.hawk.Hawk;

import org.csware.ee.Guard;
import org.csware.ee.R;
import org.csware.ee.consts.ParamKey;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.List;

/**
 * Created by Atwind on 2015/5/14.
 * 获得客户端相关状态
 */
public class ClientStatus {


    static String token;

    /**
     * 获取当前通信Key，每次通信都要用到的
     */
    public static String getToken() {
        if (Guard.isNullOrEmpty(token)) {
            token = Hawk.get(ParamKey.TOKEN);
        }
        return token;
    }

    /**
     * 设置当前通信Key，初始化载入，登录，注册，找回密码时用到
     */
    public static void setToken(String k) {
        if (!Guard.isNullOrEmpty(k)) {
            token = k;
            Hawk.chain().put(ParamKey.TOKEN, k).commit();
        }
    }

    public static void clearToken(){
        token = null;
        Hawk.chain().put(ParamKey.TOKEN, "").commit();
    }

    /**
     * 当前用户的UserId
     */
    public static int userId;


    // 检查网络是否可用
    public static boolean getNetWork(Context context) {
        if (context == null) {
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (manager != null) {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if (null != info) {
                return true;
            }
        }
        //提示网络状态不可用
        AppStatus.ToastShowS(context, context.getString(R.string.tip_need_net));
        return false;
    }

    /**
     * 判断wifi是否打开
     *
     * @return
     * @paramxt
     */
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }

    /**
     * 判读GPRS是否打开
     *
     * @param context
     * @return
     */
    public static boolean isGpsEnabled(Context context) {
        LocationManager lm = ((LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE));
        List<String> accessibleProviders = lm.getProviders(true);
        return accessibleProviders != null && accessibleProviders.size() > 0;
    }

    /**
     * 获取MAC地址
     */
    public static String getMac() {
        String macSerial = null;
        String str = "";
        try {
            Process pp = Runtime.getRuntime().exec(
                    "cat /sys/class/net/wlan0/address ");
            InputStreamReader ir = new InputStreamReader(pp.getInputStream());
            LineNumberReader input = new LineNumberReader(ir);


            for (; null != str; ) {
                str = input.readLine();
                if (str != null) {
                    macSerial = str.trim();// 去空格
                    break;
                }
            }
        } catch (IOException ex) {
            // 赋予默认值
            ex.printStackTrace();
        }
        return macSerial;
    }

    /**
     * 获取手机相关信息
     */
    public static PhoneInfo getPhone(Context context) {
        //创建电话管理
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneInfo info = new PhoneInfo();
        info.setBrand(android.os.Build.BRAND);
        info.setImei(tm.getDeviceId());
        info.setImsi(tm.getSubscriberId());
        info.setModel(android.os.Build.MODEL);
        info.setServiceName(tm.getSimOperatorName());
        info.setPhoneNum(tm.getLine1Number());
        return info;
    }

    /**
     * 获取IP
     * is Wifi
     */
    public static String getIP(Context context) {
        String IP = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        int ipAddress = wifiInfo.getIpAddress();
        IP = intToIp(ipAddress);
        return IP;
    }

    private static String intToIp(int i) {
        return (i & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                (i >> 24 & 0xFF);
    }


    /**
     * 获取IP
     * is GPRS
     */
    public static String getLocalIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
//            Log.e("getLocalIpAddress", ex.toString());
        }
        return null;
    }


}
