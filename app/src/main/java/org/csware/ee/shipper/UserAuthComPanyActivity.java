package org.csware.ee.shipper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IASyncQCloudResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.utils.ClientCheck;
import org.csware.ee.utils.FileUtils;
import org.csware.ee.utils.ImageByAndroid;
import org.csware.ee.utils.ImageUtil;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.CustomView;
import org.csware.ee.widget.PhotoPopWindow;
import org.csware.ee.widget.crop.Crop;

import java.io.File;
import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/17.
 */
public class UserAuthComPanyActivity extends ActivityBase {
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtRealname)
    EditText txtRealname;
    @InjectView(R.id.txtIDCard)
    EditText txtIDCard;
    @InjectView(R.id.txtCompanyTip)
    TextView txtCompanyTip;
    @InjectView(R.id.txtCompany)
    EditText txtCompany;
    @InjectView(R.id.ivIDCard)
    ImageView ivIDCard;
    @InjectView(R.id.customView)
    CustomView customView;
    @InjectView(R.id.ivIDCardBack)
    ImageView ivIDCardBack;
    @InjectView(R.id.customBackView)
    CustomView customBackView;
    @InjectView(R.id.txtBusiliceTip)
    TextView txtBusiliceTip;
    @InjectView(R.id.ivBusiLicen)
    ImageView ivBusiLicen;
    @InjectView(R.id.customBusiLicen)
    CustomView customBusiLicen;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    ProgressDialog dialog;
    //三星
    public static final int PHOTO_REQUEST_CAMERA = 1;// 拍照
    public static final int PHOTO_REQUEST_GALLERY = 2;// 从相册中选择
    public static final int PHOTO_REQUEST_CUT = 3;// 结果
    String tempCard, tempBackCard, tempComp, realname, IDCard, Company;
    SharedPreferences preferences;
    int defaultHeight;
    int defaultWidth;
    public static String hmtempPath = "", _tmpBackPath = "", _tmpCompanyPath = "";
    public static File tempFile;
    static int star;
    //三星
    /* 头像名称 */
    public static final String PHOTO_FILE_NAME = "Shipper_crop.jpg";
    MeReturn userInfo;
    DbCache dbCache;
    String TAG = "UserAuthCAct";
    @InjectView(R.id.txtPersonTip)
    TextView txtPersonTip;
    @InjectView(R.id.txtPersonEmpty)
    TextView txtPersonEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_auth);
        ButterKnife.inject(this);

        dbCache = new DbCache(this);

        topBar.setVisibility(View.VISIBLE);
        txtPersonEmpty.setVisibility(View.GONE);
        topBar.setTitle("企业认证");
        txtBusiliceTip.setText("营业执照");
        txtCompanyTip.setText("公司名称");
        txtPersonTip.setText("法人姓名");

        preferences = getSharedPreferences("TempURL", MODE_PRIVATE);
        ImageByAndroid.makeRootDirectory(ImageByAndroid.avatorpath);
        tempFile = ImageByAndroid.getFile(ImageByAndroid.avatorpath + "/" + PHOTO_FILE_NAME);
        defaultHeight = Tools.getScreenHeight(this);
        defaultWidth = Tools.getScreenWidth(this);

        String Positive = "";
        String Back = "", Company = "";
        if (savedInstanceState != null) {
            Positive = savedInstanceState.getString("Positive");
            Back = savedInstanceState.getString("Back");
            Company = savedInstanceState.getString("Company");
        }
        tempCard = preferences.getString("tempCard", "");
        tempBackCard = preferences.getString("tempBackCard", "");
        tempComp = preferences.getString("tempCompany", "");
        if (Guard.isNullOrEmpty(Positive)) {
            if (!Guard.isNullOrEmpty(tempCard)) {
                QCloudService.asyncDisplayImage(baseActivity, tempCard, ivIDCard, true);
            }
        } else {
            setPic(Positive, ivIDCard, null);
        }
        if (Guard.isNullOrEmpty(Back)) {
            if (!Guard.isNullOrEmpty(tempBackCard)) {
                QCloudService.asyncDisplayImage(baseActivity, tempBackCard, ivIDCardBack, true);
            }
        } else {
            setPic(Back, ivIDCardBack, null);
        }
        if (Guard.isNullOrEmpty(Company)) {
            if (!Guard.isNullOrEmpty(tempComp)) {
                QCloudService.asyncDisplayImage(baseActivity, tempComp, ivBusiLicen, true);
            }
        } else {
            setPic(Back, ivBusiLicen, null);
        }
        init();
    }

    void init() {
        //btnConfirm.setEnabled(false); //默认不可用
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
        String userName = preferences.getString("userName", "");
        String userID = preferences.getString("userID", "");
        String userCompany = preferences.getString("userCompany", "");
        if (!Guard.isNullOrEmpty(userName)) {
            txtRealname.setText(userName);
        }
        if (!Guard.isNullOrEmpty(userID)) {
            txtIDCard.setText(userID);
        }
        if (!Guard.isNullOrEmpty(userCompany)) {
            txtCompany.setText(userCompany);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        Tracer.d(TAG, "HELLO：当Activity被销毁的时候，不是用户主动按back销毁，例如按了home键");
        super.onSaveInstanceState(savedInstanceState);
        if (star == 0) {
            savedInstanceState.putString("Positive", hmtempPath);
        } else if (star == 1) {
            savedInstanceState.putString("Back", hmtempPath);
        } else if (star == 2) {
            savedInstanceState.putString("Company", hmtempPath);
        }
    }

    @OnClick({R.id.btnConfirm})
    void clickConfirm(View v) {
//        Log.d(TAG, "clickConfirm");
        realname = txtRealname.getText().toString().trim();
        IDCard = txtIDCard.getText().toString().trim();
        Company = txtCompany.getText().toString().trim();
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("userName", realname).putString("userID", IDCard).putString("userCompany", Company).commit();
        if (Guard.isNullOrEmpty(realname)) {
            toastFast("请填写实名");
            return;
        }
        if (!ClientCheck.isValiIDCard(this, IDCard, "请输入正确的身份证号")) {
            return;
        }
        if (Guard.isNullOrEmpty(Company)) {
            toastFast("请填写公司名称");
            return;
        }
        if (Guard.isNullOrEmpty(hmtempPath)) {
            if (Guard.isNullOrEmpty(tempCard)) {
                toastFast("请上传身份证照片");
                return;
            }
        }
        if (Guard.isNullOrEmpty(_tmpBackPath)) {
            if (Guard.isNullOrEmpty(tempBackCard)) {
                toastFast("请上传身份证照片背面");
                return;
            }
        }
        if (Guard.isNullOrEmpty(_tmpCompanyPath)) {
            if (Guard.isNullOrEmpty(tempComp)) {
                toastFast("请上传营业执照");
                return;
            }
        }
        if (Guard.isNullOrEmpty(tempCard) ) {
            toastFast("请上传身份证照片");
            return;
        }
        if (Guard.isNullOrEmpty(tempBackCard) ) {
            toastFast("请上传身份证照片背面");
            return;
        }
        if (Guard.isNullOrEmpty(tempComp) ) {
            toastFast("请上传营业执照");
            return;
        }
        dialog = Tools.getDialog(this);
        dialog.setCanceledOnTouchOutside(false);

        verify(tempCard, tempBackCard);
    }

    @OnClick({R.id.ivIDCard})
    void openActionSheet(View v) {
        PhotoPopWindow popWindow = null;
        baseActivity.setTheme(R.style.ActionSheetStyle);
        star = 0;
        if (popWindow != null) {
            if (popWindow.isShowing()) {
                return;
            }
        } else {
            popWindow = new PhotoPopWindow(this, v);
        }
    }

    @OnClick({R.id.ivIDCardBack})
    void openivIDCardBack(View v) {
        if (Guard.isNullOrEmpty(tempCard)) {
            toastFast("请选择身份证正面");
        } else {
            PhotoPopWindow popBackWindow = null;
            star = 1;
            baseActivity.setTheme(R.style.ActionSheetStyle);
            if (popBackWindow != null) {
                if (popBackWindow.isShowing()) {
                    return;
                }
            } else {
                popBackWindow = new PhotoPopWindow(this, v);
            }
        }
    }

    @OnClick({R.id.ivBusiLicen})
    void openivBusiLicen(View v) {
        if (Guard.isNullOrEmpty(tempBackCard)) {
            toastFast("请选择身份证背面");
        } else {
            PhotoPopWindow popBackWindow = null;
            star = 2;
            baseActivity.setTheme(R.style.ActionSheetStyle);
            if (popBackWindow != null) {
                if (popBackWindow.isShowing()) {
                    return;
                }
            } else {
                popBackWindow = new PhotoPopWindow(this, v);
            }
        }
    }

    static String IDCardUrl = "", IDCardBackUrl = "", CompanyUrl = "";
    IASyncQCloudResult asyncCard = new IASyncQCloudResult() {
        @Override
        public void notify(boolean result, final String urlOrPath) {
            if (result) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                IDCardUrl = urlOrPath;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tempCard", IDCardUrl).commit();
                tempCard = preferences.getString("tempCard", "");
            } else {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                toastFast("上传失败");
            }
        }
    };
    IASyncQCloudResult asyncCardBack = new IASyncQCloudResult() {
        @Override
        public void notify(boolean result, final String urlOrPath) {

            if (result) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                IDCardBackUrl = urlOrPath;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tempBackCard", IDCardBackUrl).commit();
                tempBackCard = preferences.getString("tempBackCard", "");
            } else {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                toastFast("上传失败");
            }
        }
    };
    IASyncQCloudResult asyncCompany = new IASyncQCloudResult() {
        @Override
        public void notify(boolean result, final String urlOrPath) {

            if (result) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                CompanyUrl = urlOrPath;
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("tempCompany", CompanyUrl).commit();
                tempComp = preferences.getString("tempCompany", "");
            } else {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                toastFast("上传失败");
            }
        }
    };
    QCloudService.NofityProgress nofityProgress = new QCloudService.NofityProgress() {
        @Override
        public void notify(final long p) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int i=0;i<100;i++){
                        Tracer.e(TAG,i+" P:"+p);
                        if (p == 100 || !Guard.isNullOrEmpty(tempCard)) {//图片上传完成
                            handler.sendEmptyMessage(0);
                            return;
                        }
                        customView.setProgress(i);
                        try {
                            Thread.sleep(50);  //暂停0.2秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }
    };
    QCloudService.NofityProgress nofityProgressBack = new QCloudService.NofityProgress() {
        @Override
        public void notify(final long p) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    while (true) {
                    for (int i=0;i<100;i++) {
                        if (p == 100 || !Guard.isNullOrEmpty(tempBackCard)) {//图片上传完成
                            handler.sendEmptyMessage(1);
                            return;
                        }
                        customBackView.setProgress((int) i);
                        try {
                            Thread.sleep(50);  //暂停0.2秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    }
                }
            }).start();
        }
    };
    QCloudService.NofityProgress nofityProgressCompany = new QCloudService.NofityProgress() {
        @Override
        public void notify(final long p) {
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    while (true) {
                    for (int i=0;i<100;i++) {
                        if (p == 100 || !Guard.isNullOrEmpty(tempComp)) {//图片上传完成
                            handler.sendEmptyMessage(2);
                            return;
                        }
                        customBusiLicen.setProgress((int) i);
                        try {
                            Thread.sleep(50);  //暂停0.2秒
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
//                    }
                }
            }).start();
        }
    };

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    customView.setVisibility(View.GONE);
                    break;
                case 1:
                    customBackView.setVisibility(View.GONE);
                    break;
                case 2:
                    customBusiLicen.setVisibility(View.GONE);
                    break;
            }
        }
    };

    void verify(String urlOrPath, String tmpBackPath) {
        UserApi.enterpriseVerify(baseActivity, baseContext, realname, IDCard, urlOrPath, tmpBackPath, Company, tempComp, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                initData();
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

    void initData() {

        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                userInfo = json;
                if (userInfo != null) {
                    dbCache.save(userInfo);
                }
                toastFast(R.string.auth_time_tip);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                    }
                }, 1000);
//                QCloudService.asyncDisplayImage(baseActivity, userInfo.OwnerUser.Avatar, ivHeadPic);
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

    void setPic(String path, ImageView imageView, Bitmap bitmap) {
        Bitmap cardPic = null;
        if (bitmap == null) {
            cardPic = ImageUtil.getBitmap(path);
        } else {
            cardPic = bitmap;
        }
        if (cardPic != null) {
//            Tracer.e(TAG, _tmpBackPath+"\n"+path);
            imageView.setImageBitmap(cardPic);
        }
//        else {
//            Tracer.e(TAG, "can't load image.");
//        }
    }

    /**
     * 接收intent传回的信息
     */
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
        Tracer.e(TAG, "resultCODE:" + resultCode + " " + requestCode);
        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(result.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, result);
        }
        if (requestCode == Code.CAMERA_REQUEST.toValue() && resultCode == RESULT_OK) {
            Uri uri = ImageByAndroid.getUri();
            if (uri == null) {
                SharedPreferences settings = getSharedPreferences("PhotoPopWindow", MODE_PRIVATE);
                uri = Uri.parse(settings.getString("Uri", ""));
            }
            Tracer.e(TAG, uri + "");
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, uri);//decode bitmap
            if (bitmap == null) {
                bitmap = isBitNull(uri);
            }
            setPicUpload(bitmap);
//            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
//            Crop.of(uri, destination).withAspect(4, 3).start(this);
        }
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            if (result != null) {
                Bitmap bitmap = ImageByAndroid.getmInsertPicture();
                if (bitmap == null) {
                    bitmap = isBitNull(result.getData());
                }
                setPicUpload(bitmap);
//                int degree = ImageByAndroid.getBitmapDegree(hmtempPath);
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
            }
        }
        //相册选取
        if (requestCode == PHOTO_REQUEST_GALLERY && result != null) {
//            ImageByAndroid.startPhotoZoom(result.getData(), this, Uri.fromFile(tempFile), false);
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, result.getData());//decode bitmap
            if (bitmap == null) {
                bitmap = isBitNull(result.getData());
            }
            setPicUpload(bitmap);
        }//返回截取的图片
        else if (requestCode == PHOTO_REQUEST_CUT && result != null) {
            //裁切大图使用Uri
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, Uri.fromFile(tempFile));//decode bitmap
            if (bitmap == null) {
                bitmap = isBitNull(Uri.fromFile(tempFile));
            }
            setPicUpload(bitmap);
        }
        //照相机选取
        else if (requestCode == PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            //这里拍摄图片和截取后的图片文件都写入了同一个文件，photo.jpg
//            ImageByAndroid.startPhotoZoom(Uri.fromFile(tempFile), this, Uri.fromFile(tempFile), false);
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, Uri.fromFile(tempFile));//decode bitmap
            if (bitmap == null) {
                bitmap = isBitNull(Uri.fromFile(tempFile));
            }
            setPicUpload(bitmap);
        }
    }

    private void beginCrop(Uri source) {
//        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
//        ImageByAndroid.startPhotoZoom(source, this, Uri.fromFile(tempFile), false);
//        Crop.of(source, destination).start(this);
//        Crop.of(source, destination).withAspect(4, 3).start(this);
        Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, source);//decode bitmap
        if (bitmap == null) {
            if (bitmap == null) {
                bitmap = isBitNull(source);
            }
        }
        setPicUpload(bitmap);
    }

    private void handleCrop(int resultCode, Intent result) {
        SharedPreferences preferences = getSharedPreferences("Crop", MODE_PRIVATE);
        boolean isCrop = preferences.getBoolean("Crop", false);
        if (resultCode == RESULT_OK) {
//            Log.e(TAG, "resultCODE:" + resultCode);
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, Crop.getOutput(result));
            ImageByAndroid.setmInsertPicture(bitmap);
            ImageByAndroid.setUri(Crop.getOutput(result));
            Intent intent = new Intent(this, RotationActivity.class);
//            intent.putExtra("bitmap",bitmap);
            startActivityForResult(intent, 10001);
//            try {
//                ImageByAndroid.saveBitmap("Shipper_crop",bitmap);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            hmtempPath = ImageByAndroid.avatorpath+"Shipper_crop"+".jpg";
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
//            setPic(hmtempPath);
        } else if (resultCode == Crop.RESULT_ERROR) {
            //android.os.Build.MODEL  型号   android.os.Build.BRAND  品牌
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        } else if (resultCode == RESULT_CANCELED && isCrop) {
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, ImageByAndroid.getUri());
            if (bitmap == null) {
                bitmap = isBitNull(ImageByAndroid.getUri());
            }
            if (bitmap != null) {
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear().commit();
                setPicUpload(bitmap);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ImageByAndroid.setUri(null);
        if (ImageByAndroid.getmInsertPicture() != null) {
            MyRecycle(ImageByAndroid.getmInsertPicture());
            System.gc();
        }
        Tracer.e(TAG, "onDestroy ");
    }

    public static void MyRecycle(Bitmap bmp) {
        if (!bmp.isRecycled() && null != bmp) {
            bmp.recycle(); //此句造成的以上异常
            bmp = null;
        }
    }

    Bitmap isBitNull(Uri uri) {
        return ImageByAndroid.decodeUriAsBitmap(this, uri, defaultHeight, defaultWidth);
    }

    public void setPicUpload(Bitmap bitmap) {
        try {
            ImageByAndroid.saveBitmap("Shipper_crop", bitmap);
            MyRecycle(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        _tmpBackPath = ImageByAndroid.avatorpath + "Shipper_crop" + ".jpg";
        String _tmpPath = "";
        Bitmap bit = null;
        if (FileUtils.isCompress(_tmpBackPath, 200)) {
            _tmpPath = _tmpBackPath;
        } else {
            _tmpPath = ImageByAndroid.avatorpath + "Shipper" + System.currentTimeMillis() + ".jpg";
            bit = ImageUtil.getimage(_tmpBackPath, _tmpPath, ImageByAndroid.avatorpath, 600, 450);
        }
        hmtempPath = _tmpPath;
//        dialog = Tools.getDialog(UserAuthActivity.this);
//        dialog.setCanceledOnTouchOutside(false);
        if (star == 1) {
            tempBackCard = "";
            customBackView.setVisibility(View.VISIBLE);
            QCloudService.asyncUploadFile(asyncCardBack, baseActivity, _tmpPath, nofityProgressBack);
            setPic(_tmpPath, ivIDCardBack, bit);
        } else if (star == 0) {
//                    hmtempPath = ImageByAndroid.avatorpath + "Shipper_crop" + ".jpg";
            tempCard = "";
            customView.setVisibility(View.VISIBLE);
            QCloudService.asyncUploadFile(asyncCard, baseActivity, _tmpPath, nofityProgress);
            setPic(_tmpPath, ivIDCard, bit);
        } else if (star == 2) {
            tempComp = "";
            _tmpCompanyPath = _tmpPath;
            customBusiLicen.setVisibility(View.VISIBLE);
            QCloudService.asyncUploadFile(asyncCompany, baseActivity, _tmpPath, nofityProgressCompany);
            setPic(_tmpPath, ivBusiLicen, bit);
        }
    }


}
