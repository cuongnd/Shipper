package org.csware.ee.shipper;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import org.csware.ee.app.ActivityBase;
import org.csware.ee.utils.ImageByAndroid;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/26.
 */
public class RotationActivity extends ActivityBase {
    @InjectView(R.id.img_rotation)
    ImageView imgRotation;
    @InjectView(R.id.btnLeftRota)
    ImageView btnLeftRota;
    @InjectView(R.id.btnRightRota)
    ImageView btnRightRota;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    Bitmap bitmap,temBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_rotation);
        ButterKnife.inject(this);
        bitmap = ImageByAndroid.getmInsertPicture();
        if (bitmap!=null){
            temBitmap = bitmap;
            imgRotation.setImageBitmap(bitmap);
        }
    }
    @OnClick(R.id.btnLeftRota)
    void setBtnLeftRota(){
        temBitmap = ImageByAndroid.rotateBitmapByDegree(temBitmap,90);
        imgRotation.setImageBitmap(temBitmap);
    }
    @OnClick(R.id.btnRightRota)
    void setBtnRightRota(){
        temBitmap = ImageByAndroid.rotateBitmapByDegree(temBitmap,-90);
        imgRotation.setImageBitmap(temBitmap);
    }
    @OnClick(R.id.btnConfirm)
    void setBtnConfirm(){
        Intent intent = new Intent();
        ImageByAndroid.setmInsertPicture(temBitmap);
        setResult(RESULT_OK, intent);
        finish();
    }
//    public static void MyRecycle(Bitmap bmp){
//        if(!bmp.isRecycled() && null!=bmp){
//            bmp.recycle(); //此句造成的以上异常
//            bmp=null;
//        }
//    }
//    @Override
//    public void onDestroy() {
//        // TODO Auto-generated method stub
////        MyRecycle(temBitmap);
//        MyRecycle(bitmap);
////        imgRotation=null;
//        super.onDestroy();
//
//    }

}
