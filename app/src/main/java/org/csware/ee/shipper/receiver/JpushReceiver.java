package org.csware.ee.shipper.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.shipper.AuthenticationActivity;
import org.csware.ee.shipper.MainTabFragmentActivity;
import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Administrator on 2015/10/9.
 *
 * 自定义接收器
 *
 * 如果不定义这个 Receiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JpushReceiver extends BroadcastReceiver {
    private static final String TAG = "JPush";
    SharedPreferences preferences;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        if (!Guard.isNull(bundle)) {
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
                Tracer.w(TAG, "[JpushReceiver] 接收Registration Id : " + regId);
                //send the Registration Id to your server...

            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
                processCustomMessage(context, bundle);

            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                SharedPreferences preferences = context.getSharedPreferences("UpdateTip", context.MODE_PRIVATE);
//        boolean isShowUpdateTip = preferences.getBoolean("isShowTip",false);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isShowTip", true).commit();
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知");
                //标题
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                //内容
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                //附加字段
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Tracer.e("Jpush", "content:" + title + " " + content + " extras:" + extras + " notifyId:" + notifactionId);
                String key1 = "", key2 = "";
                //打开自定义的Activity
                try {
                    JSONObject jsonObject = new JSONObject(extras);
                    key1 = jsonObject.getString("Key1");
                    key2 = jsonObject.getString("Type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (key2.contains("Order")) {
//            Log.d(TAG, "[MyReceiver] 接收到推送下来的通知的ID: " + notifactionId);
                    SharedPreferences clearSp = context.getSharedPreferences("notifyId", context.MODE_PRIVATE);
                    SharedPreferences.Editor cledit = clearSp.edit();
                    cledit.putInt("notifyId", notifactionId).putString("orderId", key1 + "").commit();
                }
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
                //标题
                String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                //内容
                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
                //附加字段
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
                //notificationId
                int notificationId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
                Tracer.e("Jpush", "content:" + title + " " + content + " extras:" + extras + " notifyId:" + notificationId);
                String key1 = "", key2 = "";
                //打开自定义的Activity
                try {
                    JSONObject jsonObject = new JSONObject(extras);
                    key1 = jsonObject.getString("Key1");
                    key2 = jsonObject.getString("Type");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Intent i = new Intent(context, MainTabFragmentActivity.class);
                if (key2.contains("Order")) {
                    preferences = context.getSharedPreferences("isTeamPage", context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isTeamPage", true).commit();
                    SharedPreferences ferences = context.getSharedPreferences("isRefresh", context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = ferences.edit();
                    edit.putBoolean("isRefresh", true).commit();

//                if (MainTabFragmentActivity.instance.isFinishing()) {
////                startActivity(new Intent(this, MainTabFragmentActivity.class));
//                    i = new Intent(context, MainTabFragmentActivity.class);
//                }
                } else if (key2.contains("Verify")) {
                    i = new Intent(context, AuthenticationActivity.class);
                } else {
//                if (MainTabFragmentActivity.instance.isFinishing()) {
////                startActivity(new Intent(this, MainTabFragmentActivity.class));
//                    i = new Intent(context, MainTabFragmentActivity.class);
//                }
                }
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if (i != null) {
                    context.startActivity(i);
                }

            } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
//            Log.d(TAG, "[MyReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
                //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

            } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
                boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
//            Log.w(TAG, "[MyReceiver]" + intent.getAction() +" connected state change to "+connected);
            } else {
//            Log.d(TAG, "[MyReceiver] Unhandled intent - " + intent.getAction());
            }
        }
    }

    //send msg to MainActivity
    private void processCustomMessage(Context context, Bundle bundle) {
        if (MainTabFragmentActivity.isForeground) {
            String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
            String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
            Intent msgIntent = new Intent(MainTabFragmentActivity.MESSAGE_RECEIVED_ACTION);
            msgIntent.putExtra(MainTabFragmentActivity.KEY_MESSAGE, message);
            if (!Guard.isEmpty(extras)) {
                try {
                    JSONObject extraJson = new JSONObject(extras);
                    if (null != extraJson && extraJson.length() > 0) {
                        msgIntent.putExtra(MainTabFragmentActivity.KEY_EXTRAS, extras);
                    }
                } catch (JSONException e) {

                }

            }
            context.sendBroadcast(msgIntent);
        }
    }
}
