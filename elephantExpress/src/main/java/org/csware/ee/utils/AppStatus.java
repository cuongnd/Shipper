package org.csware.ee.utils;

import android.app.ProgressDialog;
import android.content.Context;
import android.widget.Toast;

import com.orhanobut.hawk.Hawk;

import org.csware.ee.Guard;
import org.csware.ee.consts.ParamKey;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

/**
 * Created by Atwind on 2015/5/15.
 * 当前应用的相关状态
 */
public class AppStatus {


    public static ProgressDialog getDialog(Context context, String msg) {
        ProgressDialog dialog = ProgressDialog.show(context, null, msg, true, true);
        dialog.setCanceledOnTouchOutside(false); //禁止触碰取消
        // dialog.getWindow().setContentView(R.layout.refresh);
        return dialog;
    }

    static String deviceId;

    public static String getDeviceId() {
        if (Guard.isNullOrEmpty(deviceId)) {
            deviceId = Hawk.get(ParamKey.DEVICE_ID);
        }
        return deviceId;
    }

    /**
     * 获得deviceId,如果没有得到会自动创建的
     */
    public static String getDeviceId(Context activity) {
        if (!Guard.isNullOrEmpty(deviceId)) return deviceId;
        PhoneInfo phoneInfo = ClientStatus.getPhone(activity);
        deviceId = phoneInfo.getImsi();
        if (Guard.isNullOrEmpty(deviceId)) {
            deviceId = UUID.randomUUID().toString();
        }
        return deviceId;
    }

    public static void setDeviceId(String deviceid) {
        deviceId = deviceid;
        if (!Guard.isNullOrEmpty(deviceid)) {
            //存到文件中去
            Hawk.chain().put(ParamKey.DEVICE_ID, deviceid).commit();
        }
    }

    /**
     * 慢3秒
     */
    public static void ToastShowL(Context context, String s) {
        //3秒
        Toast.makeText(context, s, Toast.LENGTH_LONG).show();
    }

    /**
     * 快1秒
     */
    public static void ToastShowS(Context context, String s) {
        //1秒
        Toast.makeText(context, s, Toast.LENGTH_SHORT).show();
    }


    public static final int UPDATA_CLIENT = 1;
    public static final int GET_RESPONSE = 2;
    public static final int GET_UNDATAINFO_ERROR = -1;
    public static final int DOWN_ERROR = -2;

    /**
     * 没有Token，可能是初次使用
     */
    public static final int NONE_TOKEN = -9;


    /**
     * 获取当前的日期yyyyMMdd
     */
    public static String getCurrentDate() {
        return new SimpleDateFormat("yyyyMMdd").format(new Date());
    }


    /**
     * 获取当前的时间yyyyMMddHHmmss
     */
    public static String getCurrentTime() {
        return new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
    }
}
