package org.csware.ee.shipper.fragment;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.shipper.R;
import org.csware.ee.widget.photoview.PhotoViewAttacher;

import java.io.InputStream;

/**
 * Created by Administrator on 2016/2/24.
 */
public class ImageDetailFragment extends Fragment {
    private static DisplayImageOptions options;
    private String mImageUrl;
    private ImageView mImageView;
    private ProgressBar progressBar;
    private PhotoViewAttacher mAttacher;
    String TAG = "ImageDetailF";
    public static ImageDetailFragment newInstance(String imageUrl) {
        final ImageDetailFragment f = new ImageDetailFragment();

        final Bundle args = new Bundle();
        args.putString("url", imageUrl);
        f.setArguments(args);

        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mImageUrl = getArguments() != null ? getArguments().getString("url") : null;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View v = inflater.inflate(R.layout.image_detail_fragment, container, false);
        mImageView = (ImageView) v.findViewById(R.id.image);
        mAttacher = new PhotoViewAttacher(mImageView);

        mAttacher.setOnPhotoTapListener(new PhotoViewAttacher.OnPhotoTapListener() {

            @Override
            public void onPhotoTap(View arg0, float arg1, float arg2) {
                getActivity().finish();
            }
        });

        progressBar = (ProgressBar) v.findViewById(R.id.loading);
        return v;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


        ImageLoader.getInstance().displayImage(mImageUrl, mImageView, new SimpleImageLoadingListener() {
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                progressBar.setVisibility(View.VISIBLE);
            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                String message = null;
                switch (failReason.getType()) {
                    case IO_ERROR:
                        message = "下载错误";
                        break;
                    case DECODING_ERROR:
                        message = "图片无法显示";
                        break;
                    case NETWORK_DENIED:
                        message = "网络有问题，无法下载";
                        break;
                    case OUT_OF_MEMORY:
                        message = "图片太大无法显示";
                        break;
                    case UNKNOWN:
                        message = "未知的错误";
                        break;
                }
                if (message.equals("图片太大无法显示")){
                    new Thread(){
                        @Override
                        public void run()
                        {
                            getNetImage(mImageUrl);
                        }
                    }.start();
                }else {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
//                        ImageLoader.getInstance().displayImage(mImageUrl, mImageView);
//                QCloudService.asyncDisplayImage(getActivity(), mImageUrl, mImageView);
//                ImageLoader.getInstance().displayImage(mImageUrl, mImageView, getOptions());

                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                progressBar.setVisibility(View.GONE);
                mAttacher.update();
            }
        });




    }

    /**
     * 获取网络图片
     */
    protected void getNetImage(String imageUrl) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(imageUrl);
            HttpResponse response = client.execute(httpGet);
            int code = response.getStatusLine().getStatusCode();

            if (200 == code) {
                InputStream is = response.getEntity().getContent();

                BitmapFactory.Options opts = new BitmapFactory.Options();

                //根据计算出的比例进行缩放
//                int scale = getScare(imageUrl);
//                Tracer.e(TAG,scale+"");
                opts.inSampleSize = 5;
                Bitmap bm = null;
                try {
                    bm = BitmapFactory.decodeStream(is, null, opts);
                }catch (OutOfMemoryError error){
                    error.printStackTrace();
                }

                //将bm发生给主线程用于显示图片，更新UI
                if (!Guard.isNull(bm)) {
                    Message msg = Message.obtain();
                    msg.obj = bm;
                    handler.sendMessage(msg);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    //显示图片
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            Bitmap bm = (Bitmap) msg.obj;
            mImageView.setImageBitmap(bm);
            mImageView.setScaleType(ImageView.ScaleType.FIT_XY);
        }
    };

}