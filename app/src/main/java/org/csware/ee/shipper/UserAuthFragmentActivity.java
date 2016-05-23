package org.csware.ee.shipper;

import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baoyz.actionsheet.ActionSheet;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.Envir;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IASyncQCloudResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.model.Return;
import org.csware.ee.utils.ImageUtil;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 个人实名认证
 */
public class UserAuthFragmentActivity extends FragmentActivityBase {

    final static String TAG = "UserAuthAct";
    @InjectView(R.id.txtRealname)
    EditText txtRealname;
    @InjectView(R.id.ivIDCard)
    ImageView ivIDCard;

    @InjectView(R.id.btnConfirm)
    Button btnConfirm;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_id_rz_fragment_activity);
        ButterKnife.inject(this);
//        Log.d(TAG, "onCreate");
        init();
    }

    void init() {
        //btnConfirm.setEnabled(false); //默认不可用
    }

    String realname;

    @OnClick({R.id.btnConfirm})
    void clickConfirm(View v) {
//        Log.d(TAG, "clickConfirm");
        realname = txtRealname.getText().toString();
        if (Guard.isNullOrEmpty(realname) || Guard.isNullOrEmpty(cardImagePath)) {
            toastFast("请填写实名并选择身份证照片");
            return;
        }
        QCloudService.asyncUploadFile(asyncNotify, baseActivity, cardImagePath);
    }


    //String IDCardUrl = null;
    IASyncQCloudResult asyncNotify = new IASyncQCloudResult() {
        @Override
        public void notify(boolean result, final String urlOrPath) {
            if (result) {

                UserApi.verify(baseActivity, baseContext, realname,"","", urlOrPath, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        QCloudService.asyncDisplayImage(baseActivity, urlOrPath, ivIDCard);

//                        toastFast("上传成功，请等待审核！");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                finish();
//                            }
//                        }, 500);
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


    @OnClick({R.id.ivIDCard})
    void openActionSheet(View v) {
//        Log.d(TAG, "ivIDCard");
        baseActivity.setTheme(R.style.ActionSheetStyle);
        ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                .setCancelButtonTitle("取消")
                .setOtherButtonTitles("摄像头", "相册")
                .setCancelableOnTouchOutside(true)
                .setListener(actionSheetListener).show();
    }

    String imgFile;
    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//            Log.d(TAG, "click item index = " + index);
            if (index == 0) {

                Uri imgUri = Envir.getImageUri(ParamKey.MINE_IDCARD_TEMP);
                imgFile = imgUri.getPath();
//                Log.i(TAG, "imageFileString=" + imgFile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                cameraIntent.putExtra(ParamKey.FILE_PATH, imgFile);
                startActivityForResult(cameraIntent, Code.CAMERA_REQUEST.toValue());
            } else if (index == 1) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), Code.ALBUM_REQUEST.toValue());
            }

        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
//            Log.d(TAG, "dismissed isCancle = " + isCancle);
        }
    };

    String IDCardImage = Envir.getImagePath(ParamKey.MINE_IDCARD);
    String cardImagePath = null;

    final static int WIDTH = 1024;
    final static int HEIGHT = 500;

    /**
     * 接收intent传回的信息
     */
    public void onActivityResult(int requestCode, int resultCode, Intent result) {
//        Log.e(TAG, "resultCODE:" + resultCode);

        if (resultCode == 0) {
//            Log.i(TAG, "user cancel the operation.");
            return;
        }

        if (requestCode == Code.CAMERA_REQUEST.toValue()) {
            //摄像头图像处理
            Intent intent = new Intent(baseActivity, CropImageFragmentActivity.class);
            intent.putExtra(ParamKey.CROPED_PATH, IDCardImage);
            intent.putExtra(ParamKey.IMAGE_HEIGHT, HEIGHT);
            intent.putExtra(ParamKey.IMAGE_WIDTH, WIDTH);
            intent.putExtra(ParamKey.IMAGE_PATH, imgFile);
            startActivityForResult(intent, Code.IMAGE_CROP.toValue());
        }
        if (requestCode == Code.ALBUM_REQUEST.toValue()) {
            //来源地址图片处理
            //Log.w(TAG,"result:"+ GsonHelper.toJson(result));
            //Log.w(TAG,"imagePath:"+ GsonHelper.toJson(result.getData()));
            Uri uri = result.getData();
            ContentResolver cr = baseActivity.getContentResolver();
            try {
                Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                imgFile = Envir.getImagePath(ParamKey.MINE_IDCARD_TEMP);
                ImageUtil.saveImage(bmp, imgFile);

                bmp.recycle();
                Intent intent = new Intent(baseActivity, CropImageFragmentActivity.class);
                intent.putExtra(ParamKey.CROPED_PATH, IDCardImage);
                intent.putExtra(ParamKey.IMAGE_HEIGHT, HEIGHT);
                intent.putExtra(ParamKey.IMAGE_WIDTH, WIDTH);
                intent.putExtra(ParamKey.IMAGE_PATH, imgFile);
                startActivityForResult(intent, Code.IMAGE_CROP.toValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //裁剪完成
        if (requestCode == Code.IMAGE_CROP.toValue()) {
            Tracer.d(TAG, "//变更了图像，准备上传");
            //btnConfirm.setEnabled(true);
            cardImagePath = IDCardImage;
            setPic();
        }

    }


    void setPic() {
//        Log.d(TAG, "setHeadPic");
        Bitmap cardPic = ImageUtil.getBitmap(IDCardImage);
        if (cardPic != null) {
            ivIDCard.setImageBitmap(cardPic);
        } else {
//            Log.e(TAG, "can't load image.");
        }
    }

}
