package org.csware.ee.shipper;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Envir;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IASyncQCloudResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.UserVerifyType;
import org.csware.ee.shipper.fragment.MineFragment;
import org.csware.ee.utils.EnumHelper;
import org.csware.ee.utils.ImageByAndroid;
import org.csware.ee.utils.ImageUtil;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.PhotoPopWindow;
import org.csware.ee.widget.crop.Crop;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/30.
 */
public abstract class UserDetailFragmentActivityBase extends FragmentActivityBase {

    final static String TAG = "UserDetailAct";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.labName)
    TextView labName;
    @InjectView(R.id.ivSFRZ)
    ImageView ivSFRZ;
    @InjectView(R.id.labSFRZ)
    TextView labSFRZ;
    @InjectView(R.id.ivLevel)
    ImageView ivLevel;
    @InjectView(R.id.btnPhoneCall)
    LinearLayout btnPhoneCall;
    @InjectView(R.id.boxUserInfo)
    LinearLayout boxUserInfo;
    @InjectView(R.id.ivHeadPic)
    ImageView ivHeadPic;
    @InjectView(R.id.frameLayout)
    FrameLayout frameLayout;
    @InjectView(R.id.labPhoneNumber)
    TextView labPhoneNumber;
    @InjectView(R.id.labOrderAmount)
    TextView labOrderAmount;
    @InjectView(R.id.labZGRZ)
    TextView labZGRZ;
    @InjectView(R.id.btnZGRZ)
    LinearLayout btnZGRZ;
    @InjectView(R.id.labRealNameRZ)
    TextView labRealNameRZ;
    @InjectView(R.id.btnRealNameRZ)
    LinearLayout btnRealNameRZ;
    @InjectView(R.id.labScroe)
    TextView labScroe;
    @InjectView(R.id.btnExchange)
    LinearLayout btnExchange;
    @InjectView(R.id.boxExchange)
    LinearLayout boxExchange;
    @InjectView(R.id.rateComment)
    RatingBar rateComment;
    @InjectView(R.id.btnComment)
    LinearLayout btnComment;
    @InjectView(R.id.boxComment)
    LinearLayout boxComment;
    @InjectView(R.id.ivTruckPhoto)
    ImageView ivTruckPhoto;
    @InjectView(R.id.boxTruck)
    LinearLayout boxTruck;
    @InjectView(R.id.ivCommentGo)
    ImageView ivCommentGo;
    @InjectView(R.id.ivPhoneCall)
    ImageView ivPhoneCall;

    DbCache dbCache;
    MeReturn userInfo;
    @InjectView(R.id.Lin_orderAmount)
    LinearLayout LinOrderAmount;
    @InjectView(R.id.txtScore)
    TextView txtScore;

    @Override
    protected void onResume() {
        super.onResume();
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class); //重新载入缓存的用户数据
        initData();
        if (userInfo == null) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_fragment_activity);
        ButterKnife.inject(this);
//        Log.d(TAG, "onCreate");

        init();
    }

    @OnClick({R.id.ivHeadPic})
    void selectHeadPic(View v) {
//        Log.d(TAG, "vID=" + v.getId() + "    R.id.ivHeadPicId=" + R.id.ivHeadPic);
        baseActivity.setTheme(R.style.ActionSheetStyle);
        new PhotoPopWindow(this, v);
    }


    @OnClick({R.id.btnPhoneCall})
    void callPhone(View v) {
//        Log.i(TAG, "btnPhoneCall");

    }

    @OnClick({R.id.btnComment, R.id.rateComment})
    void setBtnComment() {
        Intent intent = new Intent(this, EvaluationRecordsActivity.class);
        intent.putExtra("UsId", userInfo.Owner.UserId);
        startActivity(intent);
    }

    @OnClick({R.id.btnRealNameRZ})
    void openRealNameRZActivity(View v) {
        openUserAuth(v);
    }

    protected void openUserAuth(View v) {
//        Log.d(TAG, "btnRealNameRZ");
        initData();
        if (userInfo.Owner == null || userInfo.OwnerUser.Status == UserVerifyType.NOT_VERIFIED.toValue()) {
            Intent intent=null;
            if (!Guard.isNull(userInfo.OwnerUser)){
                if (userInfo.OwnerUser.CompanyStatus>userInfo.OwnerUser.Status){
                    intent = new Intent(baseContext, AuthenticationActivity.class); //TODO:modfiy to  the croect activity
                    intent.putExtra("Status", userInfo.OwnerUser.CompanyStatus);//userInfo.OwnerUser.Status
                    intent.putExtra("Name", userInfo.Owner.Name);
                    intent.putExtra("auth", "company");
    //                startActivity(intent);
                }else {
                    intent = new Intent(baseContext, UserAuthActivity.class); //TODO:modfiy to  the croect activity
                }
                startActivity(intent);
            }else {
                finish();
            }
        } else {
            Intent intent = new Intent(baseContext, AuthenticationActivity.class); //TODO:modfiy to  the croect activity
            intent.putExtra("Status", userInfo.OwnerUser.Status);//userInfo.OwnerUser.Status
            intent.putExtra("Name", userInfo.Owner.Name);
            startActivity(intent);
        }
    }

    @OnClick(R.id.btnZGRZ)
    void setBtnZGRZ(){
        initData();
        if (userInfo.Owner == null || userInfo.OwnerUser.CompanyStatus == UserVerifyType.NOT_VERIFIED.toValue()) {
            Intent intent = new Intent(baseContext, UserAuthComPanyActivity.class); //TODO:modfiy to  the croect activity
            startActivity(intent);
        } else {
            Intent intent = new Intent(baseContext, AuthenticationActivity.class); //TODO:modfiy to  the croect activity
            intent.putExtra("Status", userInfo.OwnerUser.CompanyStatus);//userInfo.OwnerUser.Status
            intent.putExtra("Name", userInfo.Owner.Name);
            intent.putExtra("auth", "company");
            startActivity(intent);
        }
//        startActivity(new Intent(this,UserAuthComPanyActivity.class));
    }

    @OnClick({R.id.btnExchange})
    void openExchangePrizeActivity(View v) {
//        Log.i(TAG, "btnExchange");
        Intent intent = new Intent(baseContext, UserWalletFragmentActivity.class); //TODO:modfiy to  the croect activity
        startActivity(intent);
    }


    /**
     * 根据不同的页面来显示不同的项
     */
    protected void init() {
        initData();
        //setHeadPic();
    }

    void initData() {

        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                userInfo = json;
                if (Guard.isNull(userInfo.OwnerUser.CompanyStatus)){
                    userInfo.OwnerUser.CompanyStatus = -1;
                }
                dbCache.save(userInfo);
//                QCloudService.asyncDisplayImage(baseActivity, userInfo.OwnerUser.Avatar, ivHeadPic);
                setCompanyStaus(json);
            }

            @Override
            public void error(Return json) {

            }
        });

    }

//    void setPerson(MeReturn json){
//        if (userInfo.Owner != null && labName != null) {
//            String name = userInfo.Owner.Name+"";
//            int Status;
//
//            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
//                Status = userInfo.OwnerUser.Status;
//            } else {
//                Status = userInfo.OwnerUser.CompanyStatus;
//            }
//            String sfrz = Guard.isNull(Status) ? "" : EnumHelper.getCnName(Status, UserVerifyType.class);
//            if (!Guard.isNull(labSFRZ)) {
//                labSFRZ.setText(sfrz);
//                labRealNameRZ.setText(sfrz);
//
//            }
//            String comsfrz =  Guard.isNull(userInfo.OwnerUser.CompanyStatus) ? "" : EnumHelper.getCnName(userInfo.OwnerUser.CompanyStatus, UserVerifyType.class);
//            labZGRZ.setText(comsfrz);
//            if (userInfo.OwnerUser.CompanyStatus==1){
//                labSFRZ.setText("企业认证");
//                labZGRZ.setText("企业认证");
//                if (!Guard.isNull(userInfo.OwnerCompany)){
//                    name = userInfo.OwnerCompany.CompanyName;
//                }
//            }
//            labName.setText(name + "");
//            txtScore.setText(json.Owner.Score + "");
//        }else {
//            Tracer.e(TAG,Guard.isNull(userInfo.Owner)+" "+ Guard.isNull(labName));
//        }
//    }

    void setCompanyStaus(MeReturn json){
        if (userInfo.Owner != null && labName != null) {
            String name = userInfo.Owner.Name+"";
//            int Status;
//            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
//                Status = userInfo.OwnerUser.Status;
//            } else {
//                Status = userInfo.OwnerUser.CompanyStatus;
//            }
//            if (userInfo.OwnerUser.CompanyStatus==-2){
//                Status = userInfo.OwnerUser.CompanyStatus;
//            }
//            if (userInfo.OwnerUser.Status ==-2){
//                Status = userInfo.OwnerUser.Status;
//            }
            String sfrz = Guard.isNull(userInfo.OwnerUser.Status) ? "" : EnumHelper.getCnName(userInfo.OwnerUser.Status, UserVerifyType.class);
            if (!Guard.isNull(labSFRZ)) {
                labSFRZ.setText(sfrz);
                labRealNameRZ.setText(sfrz);
            }
            String comsfrz =  Guard.isNull(userInfo.OwnerUser.CompanyStatus) ? "" : EnumHelper.getCnName(userInfo.OwnerUser.CompanyStatus, UserVerifyType.class);
            labZGRZ.setText(comsfrz);
            if (userInfo.OwnerUser.CompanyStatus==1){
                labSFRZ.setText("企业认证");
                labZGRZ.setText("企业认证");
                btnRealNameRZ.setVisibility(View.GONE);
                if (!Guard.isNull(userInfo.OwnerCompany)){
                    name = userInfo.OwnerCompany.CompanyName;
                }
            }
            labName.setText(name + "");
            txtScore.setText(json.Owner.Score + "");
        }else {
            Tracer.e(TAG,Guard.isNull(userInfo.Owner)+" "+ Guard.isNull(labName));
        }
    }

    public static String hmtempPath = "";
    private final String avatorpath = Environment.getExternalStorageDirectory() + "/shipper/avator/";
    String HeadImage = Envir.getImagePath(ParamKey.MINE_HEADPIC);

    /**
     * 接收intent传回的信息
     */
    public void onActivityResult(int requestCode, int resultCode, Intent result) {

        Tracer.e(TAG, "resultCODE:" + resultCode + " " + requestCode);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
//            startCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
//            handleCrop(resultCode);
        }
        if (requestCode == Code.CAMERA_REQUEST.toValue() && resultCode == RESULT_OK) {
            Uri uri = ImageByAndroid.getUri();
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            Crop.of(uri, destination).asSquare().start(this);
        }
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            if (result != null) {
                Bitmap bitmap = ImageByAndroid.getmInsertPicture();
                setPicUpload(bitmap);
            }
        }
        if (requestCode == UserAuthActivity.PHOTO_REQUEST_GALLERY && result != null) {

            ImageByAndroid.startPhotoZoom(result.getData(), this, Uri.fromFile(UserAuthActivity.tempFile), true);
        }
        //照相机选取
        else if (requestCode == UserAuthActivity.PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            //这里拍摄图片和截取后的图片文件都写入了同一个文件，photo.jpg
            ImageByAndroid.startPhotoZoom(Uri.fromFile(UserAuthActivity.tempFile), this, Uri.fromFile(UserAuthActivity.tempFile), true);
        } else if (requestCode == UserAuthActivity.PHOTO_REQUEST_CUT && result != null) {
            //裁切大图使用Uri
            Bitmap bitmap = ImageByAndroid.decodeUriAsBitmap(this, Uri.fromFile(UserAuthActivity.tempFile));//decode bitmap
            setPicUpload(bitmap);
        }
    }

    private void startCrop(Uri source) {
        ImageByAndroid.setUri(source);
        startActivityForResult(new Intent(this, CropImageNewActivity.class), Crop.REQUEST_CROP);
    }


    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));

        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        SharedPreferences preferences = getSharedPreferences("Crop", MODE_PRIVATE);
        boolean isCrop = preferences.getBoolean("Crop", false);
        if (resultCode == RESULT_OK) {
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, Crop.getOutput(result));
            ImageByAndroid.setmInsertPicture(bitmap);
            Intent intent = new Intent(this, RotationActivity.class);
            startActivityForResult(intent, 10001);
//            try {
//                ImageByAndroid.saveBitmap("Shipper_crop",bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            String filePath = ImageByAndroid.avatorpath+"Shipper_crop"+".jpg";
//            setPic(filePath);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED && isCrop) {
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, ImageByAndroid.getUri());
            if (bitmap != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();
                setPicUpload(bitmap);
            }
        }
    }

    public void setPicUpload(Bitmap bitmap) {
        try {
            ImageByAndroid.saveBitmap("Shipper_crop", bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        hmtempPath = ImageByAndroid.avatorpath + "Shipper_crop" + ".jpg";
        String _tmpPath = ImageByAndroid.avatorpath + "Shipper" + System.currentTimeMillis() + ".jpg";
        ImageUtil.getimage(hmtempPath, _tmpPath, ImageByAndroid.avatorpath, 400, 400);
        int degree = ImageByAndroid.getBitmapDegree(hmtempPath);
//                Log.e("UserAuth", degree + "");
//            /**
//             * 获取图片的旋转角度，有些系统把拍照的图片旋转了，有的没有旋转
//             */
//            int degree = ImageByAndroid.getBitmapDegree(hmtempPath);
//            Log.e("UserAuth",degree+"");
//            bitmap = ImageByAndroid.rotateBitmapByDegree(bitmap,degree);
//            try {
//                ImageByAndroid.saveBitmap("Shipper_crop",bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
        setPic(_tmpPath);
    }

    private void handleCrop(int resultCode) {
        if (resultCode == RESULT_OK) {
//            ImageByAndroid.setmInsertPicture(bitmap);
            Intent intent = new Intent(this, RotationActivity.class);
            startActivityForResult(intent, 10001);
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, "裁剪失败！", Toast.LENGTH_SHORT).show();
        }
    }

    void setPic(String path) {
//        Log.d(TAG, "setHeadPic");
        Bitmap cardPic = ImageUtil.getBitmap(path);
        if (cardPic != null) {
            QCloudService.asyncUploadFile(asyncNotify, baseActivity, path);
        } else {
//            Log.e(TAG, "can't load image.");
        }
    }

    IASyncQCloudResult asyncNotify = new IASyncQCloudResult() {
        @Override
        public void notify(boolean result, final String urlOrPath) {
            if (result) {
                UserApi.edit(baseActivity, baseContext, urlOrPath, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        QCloudService.asyncDisplayImage(baseActivity, urlOrPath, ivHeadPic);
                        MineFragment.headUrl = urlOrPath;
                        UserApi.info(baseActivity, baseContext, new IJsonResult<MeReturn>() {
                            @Override
                            public void ok(MeReturn json) {
                                //成功
                                DbCache dbCache = new DbCache(baseActivity);
                                dbCache.save(json);
                                QCloudService.asyncDisplayImage(baseActivity, json.OwnerUser.Avatar, ivHeadPic);
                            }

                            @Override
                            public void error(Return json) {
                            }
                        });

                        toastFast("更新成功");
                    }

                    @Override
                    public void error(Return json) {

                    }
                });
            } else {
                toastFast("上传失败");
            }
        }
    };

}