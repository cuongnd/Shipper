package org.csware.ee.shipper.fragment;

import android.app.Notification;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;


import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Setting;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserSettingFragmentActivity;

import butterknife.InjectView;
import cn.jpush.android.api.JPushInterface;
import me.xiaopan.switchbutton.SwitchButton;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserSettingMessageFragment extends FragmentBase {

final static String TAG = "UserSettingMessage";

    @InjectView(R.id.chkMessage)
    SwitchButton chkMessage;
    @InjectView(R.id.chkSound)
    SwitchButton chkSound;
    @InjectView(R.id.chkShake)
    SwitchButton chkShake;
    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    DbCache dbCache;
    Setting setting;
    Shipper user;
    int userId = 0;
    AudioManager audioManager;
    SharedPreferences preference;
    @Override
    protected void lazyLoad() {
        dbCache = new DbCache(baseActivity);
        setting = dbCache.GetObject(Setting.class);
        if (setting == null) {
            setting = new Setting();
        }
        user = dbCache.GetObject(Shipper.class);
        if (user != null) userId = user.userId;

        chkMessage.setChecked(setting.MESSAGE.NOTIFY);
        chkSound.setChecked(setting.MESSAGE.SOUND);
        chkShake.setChecked(setting.MESSAGE.SHARK);
        audioManager = (AudioManager) baseActivity.getSystemService(Context.AUDIO_SERVICE);

        chkMessage.setOnCheckedChangeListener(checkChangeListener);
        chkSound.setOnCheckedChangeListener(checkChangeListener);
        chkShake.setOnCheckedChangeListener(checkChangeListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_setting_message_fragment;
    }

    @Override
    public void init() {
        preference = baseActivity.getSharedPreferences("InitJpush", baseActivity.MODE_PRIVATE);
                ((UserSettingFragmentActivity) getActivity()).back(getString(R.string.message_settings));
    }

    CompoundButton.OnCheckedChangeListener checkChangeListener =  new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//            Log.d(TAG, "buttonView:" + buttonView.getText() + "  isChecked:" + isChecked);
            //消息
            if(buttonView.getId() == R.id.chkMessage) {
                setting.MESSAGE.NOTIFY = isChecked;
                if (setting.MESSAGE.NOTIFY) {
//                XGPushManager.onActivityStoped(getActivity());
//                    if (userId > 0) {
//                        XGPushManager.registerPush(getActivity().getApplicationContext(), userId + "", xgioCallback);
//                    }
                    final String JpushId = JPushInterface.getRegistrationID(baseActivity);
                    SharedPreferences.Editor edit = preference.edit();
                    edit.putString("JpushId",JpushId).commit();
                    JPushInterface.resumePush(baseActivity.getApplicationContext());
                } else {
//                    XGPushManager.unregisterPush(getActivity());
                    SharedPreferences.Editor edit = preference.edit();
                    edit.clear().commit();
                    JPushInterface.stopPush(baseActivity.getApplicationContext());
                }
            }
            //声音
            if(buttonView.getId() == R.id.chkSound)
            {
                setting.MESSAGE.SOUND = isChecked;
                if (setting.MESSAGE.SOUND) {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
                    Notification notification = new Notification();
                    // 设置显示图标，该图标会在状态栏显示
                    int icon = notification.icon = R.mipmap.logo;
                    // 设置显示提示信息，该信息也在状态栏显示
                    String tickerText = "小象快运";
                    // 显示时间
                    long when = System.currentTimeMillis();
                    notification.icon = icon;
                    notification.tickerText = tickerText;
                    notification.when = when;
                    long[] vibrate = {0, 100, 200, 300};
                    notification.vibrate = vibrate;
                    setAlarmParams(notification);
//                audioManager.setStreamVolume(AudioManager.RINGER_MODE_NORMAL, ringerVolume, 0);
                } else {
                    audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                }
            }
            if(buttonView.getId() == R.id.chkShake)
            {
                if (setting.MESSAGE.SHARK) {
                    vibrate(audioManager);
                } else {
                    ring(audioManager);
                }
                setting.MESSAGE.SHARK = isChecked;

            }
            dbCache.save(setting);
        }
    };
    void ring(AudioManager audio) {
        audio.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_OFF);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_OFF);
    }
    void vibrate(AudioManager audio) {
        audio.setRingerMode(AudioManager.RINGER_MODE_VIBRATE);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER,
                AudioManager.VIBRATE_SETTING_ON);
        audio.setVibrateSetting(AudioManager.VIBRATE_TYPE_NOTIFICATION,
                AudioManager.VIBRATE_SETTING_ON);
    }
//    XGIOperateCallback xgioCallback = new XGIOperateCallback() {
//
//        @Override
//        public void onSuccess(final Object data, int flag) {
//            Tracer.i(Constants.LogTag,
//                    "+++ register push sucess. token:" + data);
////                        m.obj = "+++ register push sucess. token:" + data;
////                        m.sendToTarget();
//            if (userId > 0) {
//                //通知服务端绑定
//                UserApi.notifyToken(getActivity(), data.toString(), new IJsonResult() {
//                    @Override
//                    public void ok(Return json) {
//                        Tracer.i(TAG, "bind userid= " + userId + " to XG's token[" + data + "] is ok.");
//                    }
//
//                    @Override
//                    public void error(Return json) {
//
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onFail(Object data, int errCode, String msg) {
//            Tracer.e(Constants.LogTag,
//                    "+++ register push fail. token:" + data
//                            + ", errCode:" + errCode + ",msg:"
//                            + msg);
//        }
//    };

    //首先需要接收一个Notification的参数
    private void setAlarmParams(Notification notification) {
        //AudioManager provides access to volume and ringer mode control.
        AudioManager volMgr = (AudioManager) baseActivity.getSystemService(Context.AUDIO_SERVICE);
        switch (volMgr.getRingerMode()) {//获取系统设置的铃声模式
            case AudioManager.RINGER_MODE_SILENT://静音模式，值为0，这时候不震动，不响铃
                notification.sound =null;
                notification.vibrate =null;
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动模式，值为1，这时候震动，不响铃
                notification.sound =null;
                notification.defaults |= Notification.DEFAULT_VIBRATE;
                break;
            case AudioManager.RINGER_MODE_NORMAL://常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
                Uri ringTone =null;
                //获取软件的设置
                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(baseActivity);
                if(!sp.contains("KEY_RING_TONE")){//如果没有生成配置文件，那么既有铃声又有震动
                    notification.defaults |= Notification.DEFAULT_VIBRATE;
                    notification.defaults |= Notification.DEFAULT_SOUND;
                }else{
                    String ringFile = sp.getString("KEY_RING_TONE", null);
                    if(ringFile==null){//无值，为空，不播放铃声
                        ringTone=null;
                    }else if(!TextUtils.isEmpty(ringFile)){//有铃声：1，默认2自定义，都返回一个uri
                        ringTone=Uri.parse(ringFile);
                    }
                    notification.sound = ringTone;

                    boolean vibrate = sp.getBoolean("KEY_NEW_MAIL_VIBRATE",true);
                    if(vibrate ==false){//如果软件设置不震动，那么就不震动了
                        notification.vibrate =null;
                    }else{//否则就是需要震动，这时候要看系统是怎么设置的：不震动=0;震动=1；仅在静音模式下震动=2；
                        if(volMgr.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_OFF){
                            //不震动
                            notification.vibrate =null;
                        }else if(volMgr.getVibrateSetting(AudioManager.VIBRATE_TYPE_RINGER) == AudioManager.VIBRATE_SETTING_ONLY_SILENT){
                            //只在静音时震动
                            notification.vibrate =null;
                        }else{
                            //震动
                            notification.defaults |= Notification.DEFAULT_VIBRATE;
                        }
                    }
                }
                notification.flags |= Notification.FLAG_SHOW_LIGHTS;//都给开灯
                break;
            default:
                break;
        }
    }


}
