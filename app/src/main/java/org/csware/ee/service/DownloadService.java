package org.csware.ee.service;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.tencent.download.Downloader;
import com.tencent.download.core.DownloadResult;

import org.csware.ee.app.QCloudService;
import org.csware.ee.utils.Utils;

import java.io.File;

/**
 * Created by WuHaoyu on 2015/7/22.
 */
public class DownloadService {

    public static final int FILE_TYPE_NONE = 0;
    public static final int FILE_TYPE_PIC = 1;
    public static final int FILE_TYPE_VIDEO = 2;

    private int mFileType = FILE_TYPE_NONE;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());

    private static DownloadService sIntance = null;
    private static final byte[] INSTANCE_LOCK = new byte[0];
    public static synchronized DownloadService getInstance()
    {
        if (sIntance == null)
        {
            synchronized (INSTANCE_LOCK)
            {
                if (sIntance == null)
                {
                    sIntance = new DownloadService();
                }
            }
        }
        return sIntance;
    }

    public DownloadService(){

    }

//    com.tencent.download.Downloader.FileType type = com.tencent.download.Downloader.FileType.Other;
    public void init(String url, ImageView view, Context context){
        //APPID   USERID
        Downloader.FileType type = Downloader.FileType.Photo;
        Downloader mDownloader = new Downloader(context, type, "ivHeadPic");
        Downloader.authorize(QCloudService.APPID, "0");
        mDownloader.setMaxConcurrent(3);
        downloadFile(url,FILE_TYPE_PIC,mDownloader,view);
    }



    private int downloadFile(String url, final int fileType, Downloader mDownloader, final ImageView view)
    {
        Log.w("DownloadService", "download url:" + url + " type:" + fileType);
        if(TextUtils.isEmpty(url))
            return -1;

        // 先查找是否有本地缓存文件
        if (mDownloader.hasCache(url)) {
            File file = mDownloader.getCacheFile(url);
            if (file != null && file.exists()) {
//                onDownloadCompleted(url, fileType, file.getAbsolutePath());
                showImage(file.getAbsolutePath(), view);
                return 0;
            }
        }

        // 开始下载
        mDownloader.download(url, new Downloader.DownloadListener() {
            @Override
            public void onDownloadSucceed(final String url, DownloadResult result) {
                final String path = result.getPath();
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
//                        onDownloadCompleted(url, fileType, path);
                        showImage(path,view);
                    }
                });
            }

            @Override
            public void onDownloadProgress(String url, long totalSize, float progress) {
                final int nProgress = (int) (progress * 100);
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
//                        mTextProgress.setText("进度:" + nProgress + "%");
                    }
                });
            }

            @Override
            public void onDownloadFailed(String url, final DownloadResult result) {
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
                        //http://201575.image.myqcloud.com/201575/0/5d6d6a32-c6a7-4b28-a9f4-980260b233bc/original
//                        mProgressBar.setVisibility(View.GONE);
                         Log.e("DownloadService","下载失败！ " + result.getMessage() + " ret:" + result.getErrorCode());
                    }
                });
            }

            @Override
            public void onDownloadCanceled(String url) {
                mMainHandler.post(new Runnable() {

                    @Override
                    public void run() {
//                        mProgressBar.setVisibility(View.GONE);
//                        mTextProgress.setText("取消下载");
                    }
                });
            }
        });
//        Toast.makeText(this, "正在下载文件......", Toast.LENGTH_SHORT).show();
        return 1;
    }

    private void showImage(String file_path,ImageView view)
    {

        Bitmap bmp = null;
        try
        {
            bmp = Utils.decodeSampledBitmap(file_path, 2);
            if(bmp != null) {
                view.setImageBitmap(bmp);
            }
            else {
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

}
