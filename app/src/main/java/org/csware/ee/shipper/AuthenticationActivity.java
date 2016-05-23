package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.view.TopActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by WuHaoyu on 2015/7/29.
 */
public class AuthenticationActivity extends ActivityBase {

    @InjectView(R.id.back_img)
    ImageView backImg;
    @InjectView(R.id.btn_Back)
    RelativeLayout btnBack;
    @InjectView(R.id.more_img)
    ImageView moreImg;
    @InjectView(R.id.order_title)
    RelativeLayout orderTitle;
    @InjectView(R.id.state_img)
    ImageView stateImg;
    @InjectView(R.id.state_txt)
    TextView stateTxt;
    @InjectView(R.id.name_ed)
    TextView nameEd;
    @InjectView(R.id.txt_ac_certification_verify_tip)
    TextView txtAcCertificationVerifyTip;
    @InjectView(R.id.audit_lin)
    LinearLayout auditLin;
    @InjectView(R.id.has_passed_lin)
    LinearLayout hasPassedLin;
    @InjectView(R.id.resubmit_btn)
    Button resubmitBtn;
    @InjectView(R.id.not_by_lin)
    LinearLayout notByLin;
    @InjectView(R.id.txt_auth_error)
    TextView txtAuthError;
    @InjectView(R.id.Lin_Error)
    LinearLayout LinError;
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtAuthVerfyTip)
    TextView txtAuthVerfyTip;
    private int Status;
    Intent intent;
    DbCache dbCache;
    MeReturn userInfo;
    String auth = "";

    //    ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_certificationing);
        ButterKnife.inject(this);
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class); //重新载入缓存的用户数据
        if (userInfo == null) {
            finish();
        }
        topBar.setTitle("认证");
        auth = getIntent().getStringExtra("auth");
        Status = getIntent().getIntExtra("Status", -1);
        if (Guard.isNullOrEmpty(auth)) {
            auth = "";
        }
        initData();
        if (userInfo.Owner == null) {
            finish();
        } else {
//            if (!Guard.isNullOrEmpty(userInfo.OwnerUser.Status + "") && !Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
//                if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
//                    Status = userInfo.OwnerUser.Status;
//                } else {
//                    Status = userInfo.OwnerUser.CompanyStatus;
//                }
            showAuth(Status);
            if (userInfo.OwnerUser.CompanyStatus==1){
                nameEd.setText(userInfo.OwnerCompany.CompanyName+"");
            }
//            }
        }
    }

    void showAuth(int status) {
        switch (status) {
            case -1:
                notByLin.setVisibility(View.VISIBLE);
                if (!Guard.isNullOrEmpty(userInfo.Owner.VerifyMessage)) {
                    txtAuthError.setText(userInfo.Owner.VerifyMessage);
                }
                auditLin.setVisibility(View.GONE);
                hasPassedLin.setVisibility(View.GONE);
                break;
            case 0:
                auditLin.setVisibility(View.VISIBLE);
                hasPassedLin.setVisibility(View.GONE);
                notByLin.setVisibility(View.GONE);
                break;
            case 1:
                intent = getIntent();
//                String name = intent.getStringExtra("Name");
                if (!Guard.isNullOrEmpty(userInfo.Owner.Name)) {
                    nameEd.setText(userInfo.Owner.Name);
                    auditLin.setVisibility(View.GONE);
                    hasPassedLin.setVisibility(View.VISIBLE);
                    notByLin.setVisibility(View.GONE);
                    txtAuthVerfyTip.setVisibility(View.GONE);
                }
                break;
            case -2:
                notByLin.setVisibility(View.VISIBLE);
                if (!Guard.isNullOrEmpty(userInfo.Owner.VerifyMessage)) {
                    txtAuthError.setText(userInfo.Owner.VerifyMessage);
                }
                auditLin.setVisibility(View.GONE);
                hasPassedLin.setVisibility(View.GONE);
                break;

        }
    }

    void initData() {
//        dialog = Tools.getDialog(baseActivity);
//        dialog.setCanceledOnTouchOutside(false);
        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
//                if (dialog != null) {
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
                userInfo = json;
                dbCache.save(userInfo);
            }

            @Override
            public void error(Return json) {
//                if (dialog != null) {
//                    if (dialog.isShowing()) {
//                        dialog.dismiss();
//                    }
//                }
            }
        });
//        dialog.dismiss();
    }

    @OnClick({R.id.resubmit_btn})
    void setResubmitBtn() {
        finish();
        if (auth.equals("company")) {
            startActivity(new Intent(this, UserAuthComPanyActivity.class));
        } else {
            startActivity(new Intent(this, UserAuthActivity.class));
        }
    }

    //    @OnClick({R.id.order_btn})
//    void setOrderBtn(){
//        startActivity(new Intent(this,FindGoodsActivity.class));
//    }
    @OnClick({R.id.btn_Back})
    void setBtnBack() {
        finish();
    }

}
