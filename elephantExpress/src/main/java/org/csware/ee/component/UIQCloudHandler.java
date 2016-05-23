package org.csware.ee.component;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;

import com.lidroid.xutils.BitmapUtils;

/**
 * Q 云 图片下载UI线程操作
 * Created by Yu on 2015/7/31.
 */
public class UIQCloudHandler extends Handler {
    final static String TAG = "UIImageHandler";
    public static final int DOWNLOADED = 1;

    Context context;
    ImageView imageView;
    String downloadedUrl;
    BitmapUtils bitmapUtils;

    public UIQCloudHandler(Context context) {
        this.context = context;
        bitmapUtils = new BitmapUtils(context);
    }

//    /**
//     * 开始下载内容 TODO:异步线程我暂时没办法搞定
//     */
//    public void beginDownload(final IAsyncDownloaded downloaded, ImageView iv, int resId, String downUrl) {
//        imageView = iv;
//        //bitmapUtils.display(imageView, defaultUrl);
//        imageView.setImageDrawable(context.getResources().getDrawable(resId));
//
//        //        开始异步下载
//        QCloudService.asyncDonwload(new IASyncQCloudResult() {
//            @Override
//            public void notify(boolean result, String iUrl) {
//                if (result && !Guard.isNullOrEmpty(iUrl)) {
//                    downloadedUrl = iUrl;
//                    Log.d(TAG, "URL:" + iUrl);
//                    downloaded.onDownloaded();
//                    //sendEmptyMessage(DOWNLOADED); //必须到UI线程中通知才行
//                }
//            }
//        }, context, downUrl);
//
//    }

    public void setImageUrl(ImageView iv,String uri){
        imageView = iv;
        downloadedUrl = uri;
    }

    @Override
    public void handleMessage(Message msg) {
//        Log.w(TAG,"msg="+msg);
        if (msg.what == DOWNLOADED) {
//            Log.e(TAG, "XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX::::DOWNLOADED"+downloadedUrl);
            bitmapUtils = new BitmapUtils(context);
            bitmapUtils.display(imageView, downloadedUrl);
        }

    }
}
