package org.csware.ee.shipper;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.FrameLayout;

import org.csware.ee.app.ActivityBase;
import org.csware.ee.utils.ImageByAndroid;
import org.csware.ee.widget.crop.ClipImageLayout;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/31.
 */
public class CropImageNewActivity extends ActivityBase {

    @InjectView(R.id.id_clipImageLayout)
    ClipImageLayout idClipImageLayout;
    @InjectView(R.id.btnCancel)
    FrameLayout btnCancel;
    @InjectView(R.id.btnDone)
    FrameLayout btnDone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_crop);
        ButterKnife.inject(this);
        ClipImageLayout.setmZoomImageView(ImageByAndroid.getUri());
    }
    @OnClick(R.id.btnCancel)
    void setBtnCancel(){
        setResult(RESULT_CANCELED);
        finish();
    }
    @OnClick(R.id.btnDone)
    void setBtnDone(){
        Bitmap bitmap = idClipImageLayout.clip();
        ImageByAndroid.setmInsertPicture(bitmap);
        setResult(RESULT_OK);
        finish();
    }
}
