package org.csware.ee.shipper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import net.tsz.afinal.annotation.view.ViewInject;

import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.TimeCountButton;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.SigninResult;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientCheck;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


public class RegisterActivity extends ActivityBase {

    final static String TAG = "RegisterAct";

    @ViewInject(id = R.id.txtPassword)
    EditText txtPassword;
    @ViewInject(id = R.id.txtPasswordConfirm)
    EditText txtPasswordConfirm;
    @ViewInject(id = R.id.txtValidCode)
    EditText txtValidCode;

    @ViewInject(id = R.id.btnBack, click = "clickBack")
    ImageButton btnBack;
    @ViewInject(id = R.id.submit, click = "clickSubmit")
    Button btnRetrievalPassword;
    @ViewInject(id = R.id.btnGetValidCode, click = "clickGetValidCode")
    Button btnGetValidCode;

    //计时按钮
    TimeCountButton timerButton;

    DbCache dbCache;
    Shipper user;

    Map<String, String> msgDic = new HashMap<String, String>();
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtUsername)
    EditText txtUsername;
    @InjectView(R.id.txtInvitation)
    EditText txtInvitation;
    @InjectView(R.id.txt_regist_papers)
    TextView txtRegistPapers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);

        dbCache = new DbCache(baseContext);
        user = dbCache.GetObject(Shipper.class);
        if (user == null) user = new Shipper();

        initDic();

        timerButton = new TimeCountButton(btnGetValidCode, 60000, 1000);
        timerButton.setText(getResources().getString(R.string.lab_repeat_send_code), getResources().getString(R.string.lab_repeat_send));
        timerButton.setStatusColor(getResources().getColor(R.color.white), getResources().getColor(R.color.bg_blue), getResources().getColor(R.color.white), getResources().getColor(R.color.grey));
    }
    @OnClick(R.id.txt_regist_papers)
            void setTxtRegistPapers(){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", API.REGISTER_PAPER);
        startActivity(intent);
    }
    /**
     * 初始化返回的JSON数据翻译字典
     */
    void initDic() {
        msgDic.put("User  Exist", getResources().getString(R.string.tip_user_exist));
    }

    public void clickSubmit(View v) {
        Tracer.d("RegisterActivity", "clickSubmit:");
        final String username = txtUsername.getText().toString().trim();
        String code = txtValidCode.getText().toString().trim();
        String pwd = txtPassword.getText().toString().trim();
        String pwd2 = txtPasswordConfirm.getText().toString().trim();
        String invitation = txtInvitation.getText().toString().trim();
        if (!ClientCheck.isValidLength(baseActivity, username, getResources().getString(R.string.tip_need_phone)))
            return;
        if (!ClientCheck.isValidPassword(baseActivity, pwd, getResources().getString(R.string.tip_need_pwd)))
            return;
        if (!pwd2.equals(pwd)) {
            AppStatus.ToastShowS(baseActivity, getResources().getString(R.string.tip_need_pwd_confirm));
            return;
        }
        if (!ClientCheck.isValidStrnig(baseActivity, code, 4, 8, getResources().getString(R.string.tip_invalid_code)))
            return;

        UserApi.signup(baseContext, username, pwd, code, invitation, new IJsonResult<SigninResult>() {
            @Override
            public void ok(SigninResult json) {
                user.setToken(json.Key);
                user.userId = json.UserId;
                user.setDeviceId(AppStatus.getDeviceId(baseActivity));
                ClientStatus.userId = user.userId;
                dbCache.save(user);
                //延迟关闭，注册成功
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        startActivity(new Intent(baseActivity, MainTabFragmentActivity.class));
                        LoginActivity.activity.finish();
                        baseActivity.finish();
                    }
                }, 1000);
                toastFast(R.string.tip_reg_succeed);
            }

            @Override
            public void error(Return json) {

            }
        });


    }

    public void clickBack(View v) {
        Tracer.d("RegisterActivity", "clickBack:");
        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(RegisterActivity.this.getCurrentFocus()
                                .getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        finish();
    }
    ProgressDialog dialog;
    public void clickGetValidCode(View v) {
        Tracer.d("RegisterActivity", "clickGetValidCode:");
        String username = txtUsername.getText().toString().trim();
//        if (!ClientCheck.isValidPhone(baseActivity, username, getResources().getString(R.string.tip_need_phone)))
//            return;
//        if (username.length() != 11) {
//            return;
//        } else {
//            toastFast(getString(R.string.tip_need_phone));
//        }
        if (!ClientCheck.isValidLength(baseActivity, username, getResources().getString(R.string.tip_need_phone)))
            return;
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        UserApi.signupSMS(baseActivity, baseContext, username, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                timerButton.start();

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
