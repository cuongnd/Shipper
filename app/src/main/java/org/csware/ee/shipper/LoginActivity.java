package org.csware.ee.shipper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.annotation.view.ViewInject;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.SigninResult;
import org.csware.ee.model.UserStateInfo;
import org.csware.ee.model.UserStatusInfo;
import org.csware.ee.shipper.app.App;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientCheck;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Md5Encryption;
import org.csware.ee.utils.Tools;
import org.csware.ee.widget.InputMethodLinearLayout;

import java.util.HashMap;


public class LoginActivity extends ActivityBase implements
        InputMethodLinearLayout.OnSizeChangedListenner {

    final static String TAG = "LoginAct";

    @ViewInject(id = R.id.login_layout)
    InputMethodLinearLayout layout;

    @ViewInject(id = R.id.app_logo)
    ImageView imageLogo;

    @ViewInject(id = R.id.btnLogin, click = "clickLogin")
    Button btnLogin;

    @ViewInject(id = R.id.btnReg, click = "clickReg")
    Button btnReg;

    @ViewInject(id = R.id.forgot_password, click = "clickForgot")
    LinearLayout btnForgot;

    @ViewInject(id = R.id.txtUsername)
    EditText txtUsername;
    @ViewInject(id = R.id.txtPassword)
    EditText txtPassword;

    ProgressDialog dialog;

    DbCache dbCache;
    Shipper user;
    UserStateInfo userStatusInfo;
    public static Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        activity = this;
        //getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        // 设置监听事件
        layout.setOnSizeChangedListenner(this);
        init();
    }

    void init() {
        //初始化控件内容
        dbCache = new DbCache(baseActivity);
        user = dbCache.GetObject(Shipper.class);
        userStatusInfo= dbCache.GetObject(UserStateInfo.class);
        Tracer.d(TAG, "unitTimeLag=" + user.unitTimeLag);
        if (user != null) {
            txtUsername.setText(user.phone);
        } else {
            user = new Shipper();
        }
        if(Guard.isNull(userStatusInfo)){
            userStatusInfo = new UserStateInfo();
        }else{
            txtUsername.setText(userStatusInfo.getPhoneNumber());
        }
    }

    public void clickLogin(View view) {
        Tracer.d(TAG, "clickLogin,deviceId=" + AppStatus.getDeviceId());
        final String username = txtUsername.getText().toString().trim();
        String pwd = txtPassword.getText().toString().trim();

        HashMap<String,String> map = new HashMap<>();
        map.put("username",username);
        map.put("pwd", Md5Encryption.MD5(pwd));
        MobclickAgent.onEvent(baseActivity, "user_login", map);

        boolean isValidPhone = ClientCheck.isValidLength(baseActivity, username, getResources().getString(R.string.tip_need_phone));
        boolean isVlaidPwd = ClientCheck.isValidPassword(baseActivity, pwd, getResources().getString(R.string.tip_need_pwd));
        if (isValidPhone && isVlaidPwd) {
            dialog = Tools.getDialog(this);
            dialog.setCanceledOnTouchOutside(false);
            UserApi.signin(baseContext, username, pwd, new IJsonResult<SigninResult>() {
                @Override
                public void ok(SigninResult json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    if (!Guard.isNull(json)){
                        //成功
                        if (json.Result==0) {
                            user.phone = username;
                            user.userId = json.UserId;
                            user.setToken(json.Key);
                            user.setDeviceId(AppStatus.getDeviceId()); //有可能为空;
                            ClientStatus.userId = user.userId;
                            dbCache.save(user);
                            userStatusInfo.setPhoneNumber(username);
                            dbCache.save(userStatusInfo);
                            toastFast("登录成功");
                            SharedPreferences  preferences = getSharedPreferences("InstallStatus",MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isFirstInstall",false).commit();
                            App.getInstance().exit();
                            Intent intent = new Intent(baseActivity, MainTabFragmentActivity.class);
                            //intent.putExtra(ParamKey.USER_ID,json.UserId);
                            startActivity(intent);
//                            finish();
                        }else {
                            toastFast(R.string.result_failed_net);
                        }
                    }else {
                        toastFast(R.string.result_failed_net);
                    }

                }

                @Override
                public void error(Return json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            });
        }
    }

    public void clickReg(View view) {
        Tracer.d("LoginActivity", "clickReg");
//        startActivity(new Intent(baseActivity, RegisterActivity.class));
        startActivity(new Intent(baseActivity, RegisterNewActivity.class));
    }

    public void clickForgot(View view) {
        Tracer.d("LoginActivity", "clickForgot");

        Intent intent = new Intent();
        intent.putExtra("username", txtUsername.getText().toString().trim());
        intent.setClass(baseActivity, ForgotActivity.class);
        startActivity(intent);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)){
            finish();
            App.getInstance().exit();
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 在Activity中实现OnSizeChangedListener，原理是设置该布局的paddingTop属性来控制子View的偏移
     */
    @Override
    public void onSizeChange(boolean flag, int w, int h) {
        if (flag) {// 键盘弹出时
            // layout.setPadding(0, 10, 0, 0);
            // boot.setVisibility(View.GONE) ;
            imageLogo.setVisibility(View.GONE);

            // login_logo_layout_h.setVisibility(View.VISIBLE) ;
        } else { // 键盘隐藏时
            // layout.setPadding(0, 0, 0, 0);
            // boot.setVisibility(View.VISIBLE) ;
            imageLogo.setVisibility(View.VISIBLE);

            // login_logo_layout_h.setVisibility(View.GONE);
        }
    }

//    @Override
//    public boolean onTouchEvent(android.view.MotionEvent event) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        IBinder windowToken = null;
//        windowToken = this.getCurrentFocus().getWindowToken();
//        return imm.hideSoftInputFromWindow(windowToken, 0);
//
//    }


}
