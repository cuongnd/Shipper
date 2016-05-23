package org.csware.ee.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Toast;


import org.csware.ee.app.Envir;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserAuthActivity;
import org.csware.ee.shipper.UserDetailFragmentActivityBase;
import org.csware.ee.utils.ImageByAndroid;
import org.csware.ee.widget.crop.Crop;

import java.io.File;

/**
 * Created by WuHaoyu on 2015/7/21.
 */
public class PhotoPopWindow extends PopupWindow {

    public PhotoPopWindow(final Activity mContext, View parent) {

        View view = View.inflate(mContext, R.layout.item_popupwindows, null);
//            view.startAnimation(AnimationUtils.loadAnimation(mContext,R.anim.fade_ins));
//            LinearLayout ll_popup = (LinearLayout) view.findViewById(R.id.ll_popup);
        // ll_popup.startAnimation(AnimationUtils.loadAnimation(mContext,
        // R.anim.push_bottom_in_2));
        setAnimationStyle(R.style.AnimationPreview);
        setWidth(ViewGroup.LayoutParams.MATCH_PARENT);
        setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setContentView(view);
        showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        update();

        Button bt1 = (Button) view
                .findViewById(R.id.item_popupwindows_camera);
        Button bt2 = (Button) view
                .findViewById(R.id.item_popupwindows_Photo);
        Button bt3 = (Button) view
                .findViewById(R.id.item_popupwindows_cancel);
        bt1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (android.os.Build.MODEL.contains("GT-N7100")||android.os.Build.BRAND.contains("samsung"))
                {
                    // 判断存储卡是否可以用，可用进行存储
                    if (ImageByAndroid.hasSdcard()) {
                        UserAuthActivity.tempFile = ImageByAndroid.getFile(ImageByAndroid.avatorpath + "/" + UserAuthActivity.PHOTO_FILE_NAME);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);//调用照相机
                        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(UserAuthActivity.tempFile));
                        if (intent.resolveActivity(mContext.getPackageManager()) != null) {
                            mContext.startActivityForResult(intent, UserAuthActivity.PHOTO_REQUEST_CAMERA);
                        }
                    }else {
                        Toast.makeText(mContext,"没有足够存储空间",Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Uri imgUri = Envir.getImageUri(ParamKey.MINE_HEADPIC_TEMP);
                    UserDetailFragmentActivityBase.hmtempPath = imgUri.getPath();
//                Log.i(TAG, "拍照 imageFileString=" + imgFile);
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                    ImageByAndroid.setUri(imgUri);
                    SharedPreferences settings = mContext.getSharedPreferences("PhotoPopWindow", mContext.MODE_PRIVATE);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("Uri", String.valueOf(imgUri));
                    cameraIntent.putExtra(ParamKey.FILE_PATH, UserDetailFragmentActivityBase.hmtempPath);
                    if (cameraIntent.resolveActivity(mContext.getPackageManager()) != null) {
                        mContext.startActivityForResult(cameraIntent, Code.CAMERA_REQUEST.toValue());
                    }
                }
                dismiss();
            }
        });
        bt2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
//                if (android.os.Build.MODEL.contains("GT-N7100")||android.os.Build.BRAND.contains("samsung"))
//                {
//                    Intent intent=new Intent(Intent.ACTION_PICK);
//                    intent.setType("image/*");
//                    mContext.startActivityForResult(intent, UserAuthActivity.PHOTO_REQUEST_GALLERY);
//                }else {
                    Crop.pickImage(mContext);
//                }
                dismiss();
            }
        });
        bt3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}