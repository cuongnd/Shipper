package org.csware.ee.shipper;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ProgressBar;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.ViewInject;

import org.csware.ee.Guard;
import org.csware.ee.api.ToolApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.AlipayInfo;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.HuifuCityModel;
import org.csware.ee.model.KeyCheckResult;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.RegistModel;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.shipper.app.App;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.DialogHelper;
import org.csware.ee.utils.Tools;

import cn.jpush.android.api.JPushInterface;


public class LoadingActivity extends ActivityBase {

    static final String TAG = "LoadingAct";

    @ViewInject(id = R.id.loadingBar)
    ProgressBar progressBar;

    @ViewInject(id = R.id.txtLoading)
    TextView txtLoading;

    DbCache dbCache;
    String deviceId;
    Shipper user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_loading);
        //Tracer.d(TAG, "onCreated.");

        //获取DeviceId，Token 后续操作，全部需要
        dbCache = new DbCache(baseContext);
        user = dbCache.GetObject(Shipper.class);
        if (user == null) user = new Shipper();
        if (Guard.isNullOrEmpty(user.getDeviceId())) {
            user.setDeviceId(AppStatus.getDeviceId(baseActivity));
        } else {
            AppStatus.setDeviceId(user.getDeviceId());
        }
        int ScreenWidth = Tools.getScreenWidth(this);
        int ScreenHeight = Tools.getScreenHeight(this);
        SharedPreferences preferences = getSharedPreferences("Screen", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("ScreenWidth", ScreenWidth).putInt("ScreenHeight", ScreenHeight).commit();

        ClientStatus.userId = user.userId;
        Tracer.w(TAG, "获取缓存中的TOKEN：" + user.getToken() + " deviceId:" + user.getDeviceId());
        ClientStatus.setToken(user.getToken());
        dbCache.save(user);
        boolean isShow = false;
        isShow = getIntent().getBooleanExtra("isShow",false);
        if (isShow){
            final AlertDialog alertDialog = new AlertDialog.Builder(baseActivity).create();
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alertDialog.dismiss();
                    SharedPreferences preferences = baseContext.getSharedPreferences("unitTimeLag", baseContext.MODE_PRIVATE);
                    long unitTimeLag = preferences.getLong("unitTimeLag", 0);
                    redirectLoginUI();
                }
            };
            DialogHelper.iosOnlyHintDialog(listener, alertDialog);
        }else {
            if (ClientStatus.getNetWork(baseActivity)) {
                Tracer.d(TAG, "有网络可用，调用LoadingTask开始");
                getServiceTime();
                checkKey();
            } else {
                redirectMainTabUI(1500);//没有网络时直接转到首页
            }
        }
    }

    void getServiceTime() {
        ToolApi.getTime(baseActivity, baseContext, new IJsonResult<KeyCheckResult>() {
            @Override
            public void ok(KeyCheckResult json) {
                //Tracer.e(TAG, "result:" + result + "     code:" + json.Result + "    messg:" + json.Message);
                long ServerTime = json.ServerTime;
                long unitTime = System.currentTimeMillis() / 1000;
                long unitTimeLag = ServerTime - unitTime;
                SharedPreferences preferences = getSharedPreferences("unitTimeLag", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putLong("unitTimeLag", unitTimeLag).commit();
                Tracer.e(TAG, "" + unitTimeLag + " unitTime:" + unitTime + " ServerTime:" + json.ServerTime);
            }

            @Override
            public void error(Return json) {
//                redirectLoginUI();
//                progressBar.setVisibility(View.GONE);
            }
        });
    }

    void checkKey() {

        try {
            //首先读取本地内容
            final DbCache dbCache = new DbCache(baseActivity);
            final Shipper shipper = dbCache.GetObject(Shipper.class);
            if (shipper != null) {

                UserApi.checkKey(baseContext, new IJsonResult<KeyCheckResult>() {
                    @Override
                    public void ok(KeyCheckResult json) {
                        //Tracer.e(TAG, "result:" + result + "     code:" + json.Result + "    messg:" + json.Message);
                        DbCache db = new DbCache(baseActivity);
                        Shipper user = db.GetObject(Shipper.class);
                        user.userId = json.UserId;
                        db.save(user);
                        progressBar.setVisibility(View.GONE);
                        redirectMainTabUI(200);
                    }

                    @Override
                    public void error(Return json) {
                        redirectLoginUI();
                        progressBar.setVisibility(View.GONE);
                    }
                });

            } else {
                //没有登录信息，可能是首次安装转向登录页面
                SharedPreferences preferences = baseContext.getSharedPreferences("unitTimeLag", baseContext.MODE_PRIVATE);
                long unitTimeLag = preferences.getLong("unitTimeLag", 0);
                Tracer.e(TAG, unitTimeLag + " error");
                redirectLoginUI();
            }
        } catch (Exception e) {
            e.printStackTrace();
            getError(R.string.tip_exception_error);

        } finally {
            progressBar.setVisibility(View.GONE);
            //handler.handleMessage(msg); //
        }
    }

    void getError(int stringId) {
        txtLoading.setVisibility(View.GONE);
        toastSlow(stringId);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 1500);
    }


    void redirectLoginUI() {
        DbCache dbCache = new DbCache(baseActivity);
//        Shipper user = dbCache.GetObject(Shipper.class);
        Shipper user = new Shipper();
        MeReturn userInfo = new MeReturn();
        AccountReturn ac = new AccountReturn();
        BindStatusInfo bankinfo = new BindStatusInfo();
        DeliverInfo deliverInfo = new DeliverInfo();
        RegistModel registModel = new RegistModel();
        AlipayInfo alipayInfo = new AlipayInfo();
        OilCardInfo info = new OilCardInfo();
        HuifuCityModel cityModel = new HuifuCityModel();
        user.setToken(null);
        ClientStatus.clearToken();
        dbCache.save(bankinfo);
        dbCache.save(deliverInfo);
        dbCache.save(ac);
        dbCache.save(user);
        dbCache.save(userInfo);
        dbCache.save(registModel);
        dbCache.save(alipayInfo);
        dbCache.save(info);
        dbCache.save(cityModel);
//        XGPushManager.unregisterPush(getActivity());
        SharedPreferences preference = baseActivity.getSharedPreferences("InitJpush", baseActivity.MODE_PRIVATE);
        SharedPreferences.Editor edit = preference.edit();
        edit.clear().commit();
        SharedPreferences preferences = baseActivity.getSharedPreferences("TempURL", baseActivity.MODE_PRIVATE);
        SharedPreferences.Editor edits = preferences.edit();
        edits.clear().commit();
        JPushInterface.stopPush(baseActivity.getApplicationContext());
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(baseActivity, LoginActivity.class));
                baseActivity.finish();
            }
        }, 200);

    }

    void redirectMainTabUI(int millis) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(baseActivity, MainTabFragmentActivity.class));
                baseActivity.finish();
            }
        }, millis);
    }

    @Override
    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }
}
