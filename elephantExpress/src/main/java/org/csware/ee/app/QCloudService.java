package org.csware.ee.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.tencent.download.Downloader;
import com.tencent.download.core.DownloadResult;
import com.tencent.upload.Const;
import com.tencent.upload.UploadManager;
import com.tencent.upload.task.ITask;
import com.tencent.upload.task.IUploadTaskListener;
import com.tencent.upload.task.data.FileInfo;
import com.tencent.upload.task.impl.FileDeleteTask;
import com.tencent.upload.task.impl.PhotoUploadTask;

import org.csware.ee.Api.QCloudApi;
import org.csware.ee.Guard;
import org.csware.ee.R;
import org.csware.ee.component.IASyncQCloudResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Return;
import org.csware.ee.model.SignReturn;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;

import java.io.File;

/**
 * QQ 云图片上传
 * Created by Yu on 2015/7/21.
 */
public class QCloudService {

    final static String TAG = "QCloudService";

    /*******************以下参数需要根据业务修改**************************/
    /**
     * ****修改之后需要自己实现签名逻辑，详见{@link UpdateSignTask}******
     */
    public static String APPID = "10000961";  //10000961    10001855      201575
    static String SIGN = "RQZ0H763BMbrhK87DkB65QnmY5RhPTEwMDAxODU1JmI9MjAxNTc1Jms9QUtJRFhFdUNZYkRraHd6Sk5ISGVGTGt2VElRQlZIV29xc0R5JmU9MTQ0MDEyODI0MCZ0PTE0Mzc1MzYyNDAmcj0xNjk4NjA1MzY4JnU9NCZmPQ==";
    public static String SECRETID = "AKIDAsqjH35AoJNmzjB3lfVUIHLDMB18cXG8";  // SerectID, 正式业务请勿将该值保存在客户端，否则泄露可能导致安全隐患
    public static String BUCKET = "elephant"; //10000961    elephant 业务请自行修改成自己配置的bucket10000961   201575
    static String CACHE_PATH_ID = "persistenceId"; //每个Download实例需要分配一个唯一的id，该ID用于区分临时缓存目录
    /**
     * *************************************************************
     */

    static String userId = ClientStatus.userId + "";

    public static String APP_VERSION = "1.1.1";
    public static Const.ServerEnv ENVIRONMENT;

    private static QCloudService sIntance = null;
    private static final byte[] INSTANCE_LOCK = new byte[0];


    private UploadManager mUploadManager;

    private SharedPreferences mSharedPreferences;

    public static synchronized QCloudService getInstance() {
        if (sIntance == null) {
            synchronized (INSTANCE_LOCK) {
                if (sIntance == null) {
                    sIntance = new QCloudService();
                }
            }
        }
        return sIntance;
    }

    private QCloudService() {
    }

    public void init(Context context, String sign) {
        mSharedPreferences = context.getSharedPreferences("cloud_sign", 0);
        UploadManager.authorize(APPID, ClientStatus.userId + "", sign);
    }


    public static void asyncUploadFile(final IASyncQCloudResult syncResult, final Context context, final String filePath) {

        QCloudApi.sign(context, new IJsonResult<SignReturn>() {
            @Override
            public void ok(SignReturn json) {
                //成功
                Tracer.i(TAG, "签名超时时间：" + ParamTool.fromTimeSeconds(json.Expire));
                UploadManager.authorize(APPID, userId, json.Sign);
                //UploadManager.authorize(APPID, "4", SIGN);
                String fileHash = filePath.hashCode() + "";
                Tracer.w(TAG, "FileHASH:" + fileHash);
                UploadManager uploadManager = new UploadManager(context, fileHash);
                final PhotoUploadTask task = new PhotoUploadTask(filePath, new IUploadTaskListener() {
                    @Override
                    public void onUploadSucceed(final FileInfo result) {
                        Tracer.d(TAG, "upload succeed and url: " + result.url);
                        Tracer.d(TAG, "upload pic result:\n" + GsonHelper.toJson(result));
                        syncResult.notify(true, result.url);

                        //TODO:下载测试，回头要改走
//                        asyncDonwload(new IASyncQCloudResult() {
//                            @Override
//                            public void notify(boolean result, String url) {
//
//                            }
//                        }, context, result.url);
                    }

                    @Override
                    public void onUploadStateChange(ITask.TaskState state) {
                    }

                    @Override
                    public void onUploadProgress(long totalSize, long sendSize) {
                        long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
                        Tracer.i(TAG, "上传进度: " + p + "%");

                    }

                    @Override
                    public void onUploadFailed(final int errorCode, final String errorMsg) {
                        Tracer.e(TAG, "上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
                        syncResult.notify(false, null);

                    }
                });
                task.setBucket(BUCKET);  // 设置Bucket(可选)
                //task.setFileId(ParamKey.MINE_HEADPIC_ID); // 为图片自定义FileID(可选)
                uploadManager.upload(task);  // 开始上传

//                    int taskId = task.getTaskId();
//                    um.pause(taskId);  // 暂停上传
//                    um.resume(taskId); // 恢复上传
//                    um.cancel(taskId); // 取消上传
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    public static void asyncUploadFile(final IASyncQCloudResult syncResult, final Context context, final String filePath, final NofityProgress nofityProgress) {

        QCloudApi.sign(context, new IJsonResult<SignReturn>() {
            @Override
            public void ok(SignReturn json) {
                //成功
                Tracer.i(TAG, "签名超时时间：" + ParamTool.fromTimeSeconds(json.Expire));
                UploadManager.authorize(APPID, userId, json.Sign);
                //UploadManager.authorize(APPID, "4", SIGN);
                String fileHash = filePath.hashCode() + "";
                Tracer.w(TAG, "FileHASH:" + fileHash);
                UploadManager uploadManager = new UploadManager(context, fileHash);
                final PhotoUploadTask task = new PhotoUploadTask(filePath, new IUploadTaskListener() {
                    @Override
                    public void onUploadSucceed(final FileInfo result) {
                        Tracer.d(TAG, "upload succeed and url: " + result.url);
                        Tracer.d(TAG, "upload pic result:\n" + GsonHelper.toJson(result));
                        syncResult.notify(true, result.url);

                        //TODO:下载测试，回头要改走
//                        asyncDonwload(new IASyncQCloudResult() {
//                            @Override
//                            public void notify(boolean result, String url) {
//
//                            }
//                        }, context, result.url);
                    }

                    @Override
                    public void onUploadStateChange(ITask.TaskState state) {
                    }

                    @Override
                    public void onUploadProgress(long totalSize, long sendSize) {
                        long p = (long) ((sendSize * 100) / (totalSize * 1.0f));
                        Tracer.i(TAG, "上传进度: " + p + "%");
                        nofityProgress.notify(p);
                    }

                    @Override
                    public void onUploadFailed(final int errorCode, final String errorMsg) {
                        Tracer.e(TAG, "上传结果:失败! ret:" + errorCode + " msg:" + errorMsg);
                        syncResult.notify(false, null);

                    }
                });
                task.setBucket(BUCKET);  // 设置Bucket(可选)
                //task.setFileId(ParamKey.MINE_HEADPIC_ID); // 为图片自定义FileID(可选)
                uploadManager.upload(task);  // 开始上传

//                    int taskId = task.getTaskId();
//                    um.pause(taskId);  // 暂停上传
//                    um.resume(taskId); // 恢复上传
//                    um.cancel(taskId); // 取消上传
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    public interface NofityProgress{
        void notify(long p);
    }
    public static void DeleteFile(final Context context, final String filePath){
        QCloudApi.sign(context, new IJsonResult<SignReturn>() {

            @Override
            public void ok(SignReturn json) {
                UploadManager.authorize(APPID, userId, json.Sign);
                String fileHash = filePath.hashCode() + "";
                Tracer.w(TAG, "FileHASH:" + fileHash);
                UploadManager uploadManager = new UploadManager(context, fileHash);

                FileDeleteTask filetask = new FileDeleteTask(filePath, new FileDeleteTask.IListener() {
                    @Override
                    public void onSuccess(Void result) {
//                        Log.e("DeleteFile", "删除结果:成功!");
                    }
                    @Override
                    public void onFailure(final int ret, final String msg) {
//                        Log.e("DeleteFile", "删除结果:失败! ret:" + ret + " msg:" + msg);
                    }
                });
                uploadManager.sendCommand(filetask);

            }

            @Override
            public void error(Return json) {

            }
        });
    }



    static DisplayImageOptions options;

    static DisplayImageOptions getOptions() {
        if (options == null) {
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.w_icon_tjzp) //设置图片在下载期间显示的图片
                    .showImageForEmptyUri(R.drawable.w_icon_tjzp)//设置图片Uri为空或是错误的时候显示的图片
                    .showImageOnFail(R.drawable.w_icon_tjzp)  //设置图片加载/解码过程中错误时候显示的图片
                    .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                            //.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                    .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//            ImageScaleType属性
//            EXACTLY :图像将完全按比例缩小的目标大小
//            EXACTLY_STRETCHED:图片会缩放到目标大小完全
//            IN_SAMPLE_INT:图像将被二次采样的整数倍
//            IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
//            NONE:图片不会调整
                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                    .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                            //.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                            //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                            //设置图片加入缓存前，对bitmap进行设置
                            //.preProcessor(BitmapProcessor preProcessor)
                    .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                            //.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                    .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                    .build();//构建完成
        }
        return options;
    }
    static DisplayImageOptions getOptions(boolean isDefaut) {
        if (options == null) {
            if (isDefaut) {
                options = new DisplayImageOptions.Builder()
                        .showImageOnLoading(R.drawable.rz_icon_sczp) //设置图片在下载期间显示的图片
                        .showImageForEmptyUri(R.drawable.rz_icon_sczp)//设置图片Uri为空或是错误的时候显示的图片
                        .showImageOnFail(R.drawable.rz_icon_sczp)  //设置图片加载/解码过程中错误时候显示的图片
                        .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                                //.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                        .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//            ImageScaleType属性
//            EXACTLY :图像将完全按比例缩小的目标大小
//            EXACTLY_STRETCHED:图片会缩放到目标大小完全
//            IN_SAMPLE_INT:图像将被二次采样的整数倍
//            IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
//            NONE:图片不会调整
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                                //.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                                //设置图片加入缓存前，对bitmap进行设置
                                //.preProcessor(BitmapProcessor preProcessor)
                        .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                                //.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                        .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                        .build();//构建完成
            }else {
                options = new DisplayImageOptions.Builder()
//                        .showImageOnLoading(R.drawable.rz_icon_sczp) //设置图片在下载期间显示的图片
//                        .showImageForEmptyUri(R.drawable.rz_icon_sczp)//设置图片Uri为空或是错误的时候显示的图片
//                        .showImageOnFail(R.drawable.rz_icon_sczp)  //设置图片加载/解码过程中错误时候显示的图片
                        .cacheInMemory(true)//设置下载的图片是否缓存在内存中
                                //.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
                        .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//            ImageScaleType属性
//            EXACTLY :图像将完全按比例缩小的目标大小
//            EXACTLY_STRETCHED:图片会缩放到目标大小完全
//            IN_SAMPLE_INT:图像将被二次采样的整数倍
//            IN_SAMPLE_POWER_OF_2:图片将降低2倍，直到下一减少步骤，使图像更小的目标大小
//            NONE:图片不会调整
                        .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
                        .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
                                //.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
                                //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
                                //设置图片加入缓存前，对bitmap进行设置
                                //.preProcessor(BitmapProcessor preProcessor)
                        .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
                                //.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
                        .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
                        .build();//构建完成
            }
        }
        return options;
    }

//    static DisplayImageOptions options1080P;
//    public static DisplayImageOptions get1080POptions(){
//        if (options1080P == null) {
//            options1080P = new DisplayImageOptions.Builder()
//                    .showImageOnLoading(R.drawable.w_icon_tjzp) //设置图片在下载期间显示的图片
//                    .showImageForEmptyUri(R.drawable.w_icon_tjzp)//设置图片Uri为空或是错误的时候显示的图片
//                    .showImageOnFail(R.drawable.w_icon_tjzp)  //设置图片加载/解码过程中错误时候显示的图片
//                    .cacheInMemory(true)//设置下载的图片是否缓存在内存中
//                            //.cacheOnDisc(true)//设置下载的图片是否缓存在SD卡中
//                    .considerExifParams(true)  //是否考虑JPEG图像EXIF参数（旋转，翻转）
//                    .imageScaleType(ImageScaleType.EXACTLY_STRETCHED)//设置图片以如何的编码方式显示
//                    .bitmapConfig(Bitmap.Config.RGB_565)//设置图片的解码类型//
//                            //.decodingOptions(android.graphics.BitmapFactory.Options decodingOptions)//设置图片的解码配置
//                            //.delayBeforeLoading(int delayInMillis)//int delayInMillis为你设置的下载前的延迟时间
//                            //设置图片加入缓存前，对bitmap进行设置
//                            //.preProcessor(BitmapProcessor preProcessor)
//                    .resetViewBeforeLoading(true)//设置图片在下载前是否重置，复位
//                            //.displayer(new RoundedBitmapDisplayer(20))//是否设置为圆角，弧度为多少
//                    .displayer(new FadeInBitmapDisplayer(100))//是否图片加载好后渐入的动画时间
//                    .build();//构建完成
//        }
//        return options1080P;
//    }

    /**
     * 显示缓存中的图片
     */
    public static void displayImage(String url, final ImageView iView) {
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, iView, getOptions());
    }

    public static void asyncDisplayImage(final Context context, String url, final ImageView iView) {
        asyncDisplayImage(context, url, iView, getOptions());
    }
    public static void asyncDisplayImage(final Context context, String url, final ImageView iView,boolean isDefaut) {
//        asyncDisplayImage(context, url, iView, getOptions(isDefaut));
        if (iView==null){
            return;
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, iView, getOptions(isDefaut));
//        QCloudService.asyncDonwload(new IASyncQCloudResult() {
//            @Override
//            public void notify(boolean result, String urlOrPath) {
//                if (result && !Guard.isNullOrEmpty(urlOrPath)) {
////                    Tracer.e(TAG, "asyncDisplayImage--------------------------URI:" + urlOrPath);
////                    String uri4ImageLoader = "file://" + urlOrPath;
//                    //TODO:由Qcloud下载图像，否则加了防盗后下载会失败 .暂时没工夫来处理。但是直接使用后读取缓存的路径又有问题。
////                    BitmapUtils bitmapUtils = new BitmapUtils(context);
////                    bitmapUtils.display(iView, urlOrPath);
////                    ImageLoader imageLoader = ImageLoader.getInstance();
////                    imageLoader.displayImage(uri4ImageLoader, iView, getOptions());
//                }
//            }
//        }, context, url);
    }

    /**
     * 异步自动设置图像
     */
    public static void asyncDisplayImage(final Context context, String url, final ImageView iView, DisplayImageOptions displayImageOptions) {
        if (iView==null){
            return;
        }
        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.displayImage(url, iView, displayImageOptions);

        QCloudService.asyncDonwload(new IASyncQCloudResult() {
            @Override
            public void notify(boolean result, String urlOrPath) {
                if (result && !Guard.isNullOrEmpty(urlOrPath)) {
//                    Tracer.e(TAG, "asyncDisplayImage--------------------------URI:" + urlOrPath);
//                    String uri4ImageLoader = "file://" + urlOrPath;
                    //TODO:由Qcloud下载图像，否则加了防盗后下载会失败 .暂时没工夫来处理。但是直接使用后读取缓存的路径又有问题。
//                    BitmapUtils bitmapUtils = new BitmapUtils(context);
//                    bitmapUtils.display(iView, urlOrPath);
//                    ImageLoader imageLoader = ImageLoader.getInstance();
//                    imageLoader.displayImage(uri4ImageLoader, iView, getOptions());
                }
            }
        }, context, url);
    }


    /**
     * 该方法会产生非UI线程操作的异常
     */
    public static void asyncDonwload(final IASyncQCloudResult asyncResult, final Context context, final String url) {

        Tracer.w("DownloadTestActivity", "download url:" + url);
        if (Guard.isNullOrEmpty(url)) return;

        final Downloader.FileType fileType = Downloader.FileType.Photo;
        Downloader.authorize(APPID, userId);

        Downloader downloader = new Downloader(context, fileType, CACHE_PATH_ID);
        downloader.setMaxConcurrent(3);
        //downloader.enableHTTPRange(true);

        // 先查找是否有本地缓存文件
        if (downloader.hasCache(url)) {
            File file = downloader.getCacheFile(url);
            if (file != null && file.exists()) {
                //onDownloadCompleted(url, fileType, file.getAbsolutePath());
                Tracer.d(TAG, "命中缓存");
                asyncResult.notify(true, file.getAbsolutePath());
                return;
            }

        }

        Downloader.DownloadListener listener = new Downloader.DownloadListener() {
            @Override
            public void onDownloadSucceed(String url, DownloadResult result) {
                final String path = result.getPath();
                Tracer.d(TAG, "下载成功: " + path);
                asyncResult.notify(true, path);
            }

            @Override
            public void onDownloadProgress(String url, long totalSize, float progress) {
                long nProgress = (int) (progress * 100);
                Tracer.d(TAG, "下载进度: " + nProgress + "%");
            }

            @Override
            public void onDownloadFailed(String url, DownloadResult result) {
                Tracer.e(TAG, "下载失败！ " + result.getMessage() + " ret:" + result.getErrorCode());
                asyncResult.notify(false, null);
            }

            @Override
            public void onDownloadCanceled(String url) {
                Tracer.i(TAG, "下载任务被取消");
                asyncResult.notify(false, null);
            }
        };
        downloader.download(url, listener);

    }


}
