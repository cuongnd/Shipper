package org.csware.ee.shipper.app;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.baidu.mapapi.SDKInitializer;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.orhanobut.hawk.Hawk;
import com.orhanobut.hawk.HawkBuilder;


import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.shipper.R;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import cn.jpush.android.api.JPushInterface;
//import us.costan.chrome.ChromeView;

/**
 * Created by Yu on 2015/6/5.
 * 全局初始化
 */
public class App extends Application {
    final static String TAG = "App";
    public static boolean isSaveCache = false;
    public static void setSaveCache(boolean isSave){
        isSaveCache = isSave;
    }
    public static boolean getSaveCache(){
        return isSaveCache;
    }

    public List<Activity> activityList = new LinkedList<Activity>();
    public static App instance;

    public App() {
    }

    // 单例模式中获取唯一的MyApplication实例
    public static App getInstance() {
        if (null == instance) {
            instance = new App();
        }
        return instance;

    }

    // 添加Activity到容器中
    public void addActivity(Activity activity) {
        activityList.add(activity);
    }

    public void exit() {

        for (Activity activity : activityList) {
            activity.finish();
        }

//        System.exit(0);

    }


    @Override
    public void onCreate() {
        super.onCreate();

        Tracer.w(TAG, "======================================" + Tracer.getDebugText() + "======================================");

        bdmapInit();

//        xgPushInit();

        hawkInit();

        imageLoaderInit();

        JpushInit();

        //testIn
//        TestinAgent.init(this);
//        TestinAgent.uploadException(this, "OutOfMemory", new Throwable(OutOfMemoryError));

        chromeInit();
    }

    private void chromeInit() {
//        ChromeView.initialize(this);
    }

    void JpushInit() {
        JPushInterface.setDebugMode(false);
        JPushInterface.init(getApplicationContext());

    }


    void imageLoaderInit() {

//                //.discCacheFileNameGenerator(new Md5FileNameGenerator())//将保存的时候的URI名称用MD5 加密
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                //.discCache(new UnlimitedDiscCache(cacheDir))//自定义缓存路径
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple())
//                .imageDownloader(new BaseImageDownloader(this, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
//                .writeDebugLogs() // Remove for release app
//                .build();//开始构建
        DisplayImageOptions defaultOptions = new DisplayImageOptions
                .Builder()
                .showImageForEmptyUri(R.drawable.rz_icon_sczp)
                .showImageOnFail(R.drawable.rz_icon_sczp)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .build();
        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .discCacheSize(50 * 1024 * 1024)//
                .discCacheFileCount(100)//缓存一百张图片
                .writeDebugLogs()
                .build();
//        File cacheDir = StorageUtils.getCacheDirectory(this);
//
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
//                //.memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每个缓存文件的最大长宽
//                //.diskCacheExtraOptions(480, 800, null) // Can slow ImageLoader, use it carefully (Better don't use it)/设置缓存的详细信息，最好不要设置这个
//                //.taskExecutor(...)
//                //.taskExecutorForCachedImages(...)
//                .threadPoolSize(3) //线程池内加载的数量
//                        //.threadPriority(Thread.NORM_PRIORITY - 2) // default
//                        //.tasksProcessingOrder(QueueProcessingType.FIFO) // default
//                .denyCacheImageMultipleSizesInMemory()
//                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))// You can pass your own memory cache implementation/你可以通过自己的内存缓存实现
//                .memoryCacheSize(2 * 1024 * 1024)
//                        //.memoryCacheSizePercentage(13) // default
//                        //.diskCache(new UnlimitedDiscCache(cacheDir)) // default
//                .diskCacheSize(50 * 1024 * 1024)
//                .diskCacheFileCount(100)  //缓存的文件数量
//                        //.diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
//                        //.imageDownloader(new BaseImageDownloader(this)) // default
//                        //.imageDecoder(new BaseImageDecoder()) // default
//                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
//                .writeDebugLogs()
//                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }

    void hawkInit() {
        //hawk存储
        Hawk.init(this)
                .setEncryptionMethod(HawkBuilder.EncryptionMethod.NO_ENCRYPTION)
                .build();
    }


    public boolean isMainProcess() {
        ActivityManager am = ((ActivityManager) getSystemService(Context.ACTIVITY_SERVICE));
        List<ActivityManager.RunningAppProcessInfo> processInfos = am.getRunningAppProcesses();
        String mainProcessName = getPackageName();
        int myPid = android.os.Process.myPid();
        for (ActivityManager.RunningAppProcessInfo info : processInfos) {
            if (info.pid == myPid && mainProcessName.equals(info.processName)) {
                return true;
            }
        }
        return false;
    }


//    void xgPushInit() {
//        // 在主进程设置信鸽相关的内容
//        if (isMainProcess()) {
//            // 为保证弹出通知前一定调用本方法，需要在application的onCreate注册
//            // 收到通知时，会调用本回调函数。
//            // 相当于这个回调会拦截在信鸽的弹出通知之前被截取
//            // 一般上针对需要获取通知内容、标题，设置通知点击的跳转逻辑等等
//            XGPushManager
//                    .setNotifactionCallback(new XGPushNotifactionCallback() {
//
//                        @Override
//                        public void handleNotify(XGNotifaction xGNotifaction) {
////                            Log.i("test", "处理信鸽通知：" + xGNotifaction);
//                            // 获取标签、内容、自定义内容
//                            String title = xGNotifaction.getTitle();
//                            String content = xGNotifaction.getContent();
//                            String customContent = xGNotifaction.getCustomContent();
////                            Log.i(TAG, "PUSH-TITLE:" + title);
//                            // 其它的处理
//                            // 如果还要弹出通知，可直接调用以下代码或自己创建Notifaction，否则，本通知将不会弹出在通知栏中。
//                            xGNotifaction.doNotify();
//                        }
//                    });
//        }
//    }

    void bdmapInit() {


        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        //SDKInitializer.initialize(this);
        TelephonyManager telmgr = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        String deviceID = telmgr.getDeviceId();
        boolean isEmulator = "000000000000000".equalsIgnoreCase(deviceID);
        if (isEmulator) {
            Tracer.w(TAG, "在虚拟机中运行..."+" :"+deviceID);
//            SDKInitializer.initialize(getApplicationContext());
        } else {
            Tracer.w(TAG, "在实体机中运行...");
            //在使用SDK各组件之前初始化context信息，传入ApplicationContext
            //注意该方法要再setContentView方法之前实现
            SDKInitializer.initialize(this);
        }

    }


}
