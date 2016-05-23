package org.csware.ee.shipper;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.Return;
import org.csware.ee.view.TopActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/31.
 */
public class UserFriendAddActivity extends ActivityBase {

    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtPhone)
    EditText txtPhone;
    @InjectView(R.id.optAmount)
    LinearLayout optAmount;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_friend_add_fragment);
        ButterKnife.inject(this);
        topBar.setVisibility(View.VISIBLE);
        topBar.setTitle("添加司机");
    }

    @OnClick(R.id.btnConfirm)
    void setBtnConfirm(){
        String phone = txtPhone.getText().toString();
        if (Guard.isNullOrEmpty(phone)) {
            toastFast("请输入常用司机的手机号");
            return;
        }
        btnConfirm.setEnabled(false);
        ContactApi.edit(baseActivity, baseActivity, ActionType.ADD, phone, new IJsonResult() {
            @Override
            public void ok(Return json) {

                //成功
                toastFast("增加成功");
                topBar.setTitle("常用司机");
                txtPhone.setText("");
                finish();
            }

            @Override
            public void error(Return json) {
                btnConfirm.setEnabled(true);
            }
        });
    }

}
