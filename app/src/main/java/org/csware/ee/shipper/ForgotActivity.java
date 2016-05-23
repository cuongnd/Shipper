package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.TimeCountButton;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.SigninResult;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientCheck;
import org.csware.ee.view.TopActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * 取回密码
 */
public class ForgotActivity extends ActivityBase {
    final static String TAG = "ForgotAct";


    TimeCountButton timerButton;

    String randStr;
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtUsername)
    EditText txtUsername;
    @InjectView(R.id.txtValidCode)
    EditText txtValidCode;
    @InjectView(R.id.btnGetValidCode)
    Button btnGetValidCode;
    @InjectView(R.id.boxPhoneForm)
    LinearLayout boxPhoneForm;
    @InjectView(R.id.txtPassword)
    EditText txtPassword;
    @InjectView(R.id.txtPasswordConfirm)
    EditText txtPasswordConfirm;
    @InjectView(R.id.boxLoginForm)
    LinearLayout boxLoginForm;
    @InjectView(R.id.submit)
    Button submit;
    @InjectView(R.id.txt_forgot_tip)
    TextView txtForgotTip;
    @InjectView(R.id.txt_regist_papers)
    TextView txtRegistPapers;
    @InjectView(R.id.Lin_invation)
    LinearLayout LinInvation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);
        ButterKnife.inject(this);
        txtForgotTip.setVisibility(View.GONE);
        txtRegistPapers.setVisibility(View.GONE);
        LinInvation.setVisibility(View.GONE);
        submit.setText(getString(R.string.btn_submit));

        timerButton = new TimeCountButton(btnGetValidCode, 60000, 1000);
        timerButton.setText(getResources().getString(R.string.lab_repeat_send_code), getResources().getString(R.string.lab_repeat_send));
        timerButton.setStatusColor(getResources().getColor(R.color.white), getResources().getColor(R.color.bg_blue), getResources().getColor(R.color.white), getResources().getColor(R.color.grey));

        String username = this.getIntent().getStringExtra("username");
        txtUsername.setText(username);

        btnGetValidCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getValidCode(v);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickRetrievalPassword(v);
            }
        });
    }


    /**
     * 获取验证码
     */
    //@OnClick({R.id.btnGetValidCode})
    void getValidCode(View v) {
        String username = txtUsername.getText().toString().trim();
        if (!ClientCheck.isValidPhone(baseActivity, username, getResources().getString(R.string.tip_need_phone)))
            return;

        UserApi.resetPasswordSMS(baseActivity, baseContext, username, new IJsonResult() {
            @Override
            public void ok(Return json) {
                timerButton.start();
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    /**
     * 使用新的密码
     */
    //@OnClick({R.id.submit})
    void clickRetrievalPassword(View v) {
        final String username = txtUsername.getText().toString().trim();
        String code = txtValidCode.getText().toString().trim();
        String pwd = txtPassword.getText().toString().trim();
        String pwd2 = txtPasswordConfirm.getText().toString().trim();
        if (!ClientCheck.isValidLength(baseActivity, username, getResources().getString(R.string.tip_need_phone)))
            return;
        if (!ClientCheck.isValidStrnig(baseActivity, code, 4, 8, getResources().getString(R.string.tip_invalid_code)))
            return;
        if (!ClientCheck.isValidPassword(baseActivity, pwd, getResources().getString(R.string.tip_need_pwd)))
            return;
        if (!pwd2.equals(pwd)) {
            AppStatus.ToastShowS(baseActivity, getResources().getString(R.string.tip_need_pwd_confirm));
            return;
        }

        UserApi.resetPassword(baseActivity, baseContext, username, pwd, code, new IJsonResult<SigninResult>() {
            @Override
            public void ok(SigninResult json) {

                Shipper shipper;
                DbCache db = new DbCache(baseActivity);
                shipper = db.GetObject(Shipper.class);
                if (shipper == null) {
                    shipper = new Shipper();
                    shipper.phone = (username);
                }
                shipper.userId = json.UserId;
                shipper.setToken(json.Key);
                db.save(shipper);
                toastFast("修改成功！");
                finish();
//                startActivity(new Intent(baseActivity, MainTabFragmentActivity.class)); //立即转向首页

            }

            @Override
            public void error(Return json) {
                toastFast("修改失败！");
            }
        });

    }

//    /**
//     * 必须为public方法
//     */
//    public void clickBack(View v) {
//        Tracer.d("clickBack", "返回上一页，结束当前页");
//        ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
//                .hideSoftInputFromWindow(ForgotActivity.this
//                                .getCurrentFocus().getWindowToken(),
//                        InputMethodManager.HIDE_NOT_ALWAYS);
//        finish(); //结束了当前的UI就会自动弹到上一个UI去
//    }


//    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        return imm.hideSoftInputFromWindow(this.getCurrentFocus()
//                .getWindowToken(), 0);
//
//    }
}
