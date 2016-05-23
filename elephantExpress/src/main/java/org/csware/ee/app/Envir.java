package org.csware.ee.app;

import android.net.Uri;

import java.io.File;

import static android.os.Environment.MEDIA_MOUNTED;
import static android.os.Environment.getExternalStorageDirectory;
import static android.os.Environment.getExternalStorageState;

/**
 * Created by Yu on 2015/6/23.
 * 系统环境相关
 */
public class Envir {


    final static String APP_NAME = "Shipper";
    final static String APP_IMAGE_NAME = "images";

    /**
     * 返级应用的缓存目录
     */
    public static String getAppPath() {
        String filePath = getExternalStorageDirectory().getAbsolutePath();
        String cachePath = filePath + File.separator + APP_NAME + File.separator;
        File file = new File(cachePath);//新建目录
        if (!file.exists()) {//判断文件夹目录是否存在
            file.mkdir();//如果不存在则创建
        }
        return cachePath;
    }

    /**
     * 返回应用图片缓存目录
     */
    public static String getAppImagePath() {
        String appPath = getAppPath();
        String imgPath = appPath + File.separator + APP_IMAGE_NAME + File.separator;
        File file = new File(imgPath);//新建目录
        if (!file.exists()) {//判断文件夹目录是否存在
            file.mkdir();//如果不存在则创建
        }
        return imgPath;
    }

    /**
     * 获取一个本地图片路径
     */
    public static String getImagePath(String fileName) {
        return getAppImagePath() + fileName;
    }

    public static Uri getFileUri(String fileName) {
        String filePath = getAppPath() + fileName;
        File file = new File(filePath);
        Uri fileUri = Uri.fromFile(file);
        return fileUri;
    }

    /**
     * 获得图像资源文件的URI
     */
    public static Uri getImageUri(String fileName) {
        String filePath = getAppImagePath() + fileName;
        File file = new File(filePath);
        Uri fileUri = Uri.fromFile(file);
        return fileUri;
    }

    /**
     * 判断是否存在SD扩展卡
     */
    public static boolean getSdCardExist() {
        return getExternalStorageState().equals(MEDIA_MOUNTED);
    }
}
