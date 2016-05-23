package org.csware.ee.shipper;


import android.view.MotionEvent;
import android.view.View;

import org.csware.ee.Guard;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.QCloudService;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.UserVerifyType;
import org.csware.ee.utils.EnumHelper;

/**
 * 货主信息页面
 * <p/>
 * TODO:隐藏其它角色页面
 */
public class UserShipperFragmentActivity extends UserDetailFragmentActivityBase {

    final static String TAG = "UserShipperAct";


    DbCache dbCache;
    MeReturn userInfo;

    @Override
    protected void init() {
        super.init();

//        btnZGRZ.setVisibility(View.GONE);
        boxExchange.setVisibility(View.GONE);
        boxTruck.setVisibility(View.GONE);
//        ivCommentGo.setVisibility(View.GONE);
        ivPhoneCall.setVisibility(View.GONE);

    }

    @Override
    public void onResume(){
        super.onResume();
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class); //重新载入缓存的用户数据
        if (userInfo == null) {
            finish();
        }
        labRealNameRZ.setText("");
        String sfrz ="";
            int stauts = -1;
        if (!Guard.isNull(userInfo)) {
            if (!Guard.isNull(userInfo.OwnerUser)) {
                if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                    if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                        stauts = userInfo.OwnerUser.Status;
                    } else {
                        stauts = userInfo.OwnerUser.CompanyStatus;
                    }
                } else {
                    stauts = userInfo.OwnerUser.Status;
                }
                if (userInfo.OwnerUser.CompanyStatus == -2) {
                    stauts = userInfo.OwnerUser.CompanyStatus;
                }
                if (userInfo.OwnerUser.Status == -2) {
                    stauts = userInfo.OwnerUser.Status;
                }

                sfrz = Guard.isNull(stauts) ? "" : EnumHelper.getCnName(stauts, UserVerifyType.class);
                if (!Guard.isNullOrEmpty(userInfo.OwnerUser.Avatar)) {
                    QCloudService.asyncDisplayImage(baseActivity, userInfo.OwnerUser.Avatar, ivHeadPic);
                }
                labPhoneNumber.setText(userInfo.OwnerUser.Mobile);
            }

//        labSFRZ.setText(sfrz);
            if (userInfo.Owner == null || stauts == UserVerifyType.NOT_VERIFIED.toValue()) {
                btnRealNameRZ.setVisibility(View.VISIBLE);
                labName.setText("");
                labOrderAmount.setText("");
                LinOrderAmount.setVisibility(View.GONE);
                btnComment.setVisibility(View.GONE);
            } else {
//            btnRealNameRZ.setVisibility(View.GONE);
                if (!Guard.isNull(userInfo.OwnerUser)) {
                    if (Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                        labName.setText(userInfo.Owner.Name + "");
                    } else {
                        if (userInfo.OwnerUser.CompanyStatus == 1) {
                            btnRealNameRZ.setVisibility(View.GONE);
                            if (!Guard.isNull(userInfo.OwnerCompany)) {
                                String companyName = "";
                                if (!Guard.isNull(userInfo.OwnerCompany)) {
                                    if (!Guard.isNullOrEmpty(userInfo.OwnerCompany.CompanyName)) {
                                        companyName = userInfo.OwnerCompany.CompanyName;
                                    }
                                }
                                labName.setText(companyName);
                            }
                        } else {
                            labName.setText(userInfo.Owner.Name + "");
                        }
                    }
                    labOrderAmount.setText(userInfo.Owner.OrderAmount + "");
                    txtScore.setText(userInfo.Owner.Score + "");
                    rateComment.setRating(userInfo.Owner.Rate);
                    //禁止修改
                    rateComment.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View arg0, MotionEvent arg1) {
                            return true;
                        }
                    });
                }
            }
        }
    }

//    /**重写基本的实名认证打开方式*/
//    @Override
//    protected void openUserAuth(View v){
//        if(userInfo.OwnerUser.Status == UserVerifyType.NOT_VERIFIED.toValue()){
//            super.openUserAuth(v);
//        }else if(userInfo.OwnerUser.Status == UserVerifyType.VERIFYING.toValue()){
//            //打开正在验证中的提示
//        }else{
//            //不作提示
//        }
//    }
}
