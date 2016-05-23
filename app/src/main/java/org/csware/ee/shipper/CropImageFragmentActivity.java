package org.csware.ee.shipper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;

import com.edmodo.cropper.CropImageView;

import org.csware.ee.Guard;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.utils.ImageUtil;

import java.io.IOException;


public class CropImageFragmentActivity extends FragmentActivityBase {

    final static String TAG = "CropImageFragmentActivity";

    CropImageView ivImage;

    FrameLayout btnCancel;
    FrameLayout btnDone;

    Intent intent;

    /**
     * 传入的裁剪后的保存位置
     */
    String cropedPath;
    int aimWidth;
    int aimHeight;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_image_fragment_activity);


        ivImage = getView(R.id.ivCropImage);
        btnCancel = getView(R.id.btnCancel);
        btnDone = getView(R.id.btnDone);

        intent = getIntent();

        aimWidth = intent.getIntExtra(ParamKey.IMAGE_WIDTH, 300);
        aimHeight = intent.getIntExtra(ParamKey.IMAGE_HEIGHT, 300);

        ivImage.setAspectRatio(aimWidth, aimHeight);
        ivImage.setFixedAspectRatio(true);
        ivImage.setGuidelines(2);

        String file = intent.getStringExtra(ParamKey.IMAGE_PATH);
        cropedPath = intent.getStringExtra(ParamKey.CROPED_PATH);
        if (Guard.isNullOrEmpty(cropedPath))
            toastFast("路径设置错误");

        if (!Guard.isNullOrEmpty(file)) {
            DisplayMetrics dm = new DisplayMetrics(); //缩放至满屏
            int sw = dm.widthPixels;
            int sh = dm.heightPixels;
            Tracer.w(TAG, "SET IMAGE TO CROP PATH:" + file);


//            BitmapUtils bu = new BitmapUtils(this);
//            bu.display(ivImage, file);


            Bitmap x = ImageUtil.getBitmap(file);
            Tracer.w(TAG,"X:"+x.getByteCount());
            ivImage.setImageBitmap(ImageUtil.getBitmap(file, sw, sh));
        }

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra(ParamKey.CROP_RESULT, Code.FAILED.toValue());
                setResult(Code.CANCEL.toValue(), intent);
                finish();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Bitmap result = ivImage.getCroppedImage();
                    //要进行缩放处理
                    int iw = result.getWidth();
                    int ih = result.getHeight();
                    //以宽为准
                    int xHeight = aimWidth * ih / iw;
                    float rw = (float) aimWidth / (float) iw;
                    float rh = (float) xHeight / (float) ih;
                    Tracer.e(TAG, "width:" + rw + "   height:" + rh);

                    Matrix matrix = new Matrix();
                    matrix.postScale(rw, rh);
                    Bitmap si = Bitmap.createBitmap(result, 0, 0, iw, ih, matrix, true);
                    ImageUtil.saveImage(si, cropedPath);

                    result.recycle();
                    si.recycle();

                    setResult(Code.OK.toValue(), intent);
                } catch (IOException e) {
                    setResult(Code.FAILED.toValue(), intent);
                    e.printStackTrace();
                }
                finish();
            }
        });


    }


}
