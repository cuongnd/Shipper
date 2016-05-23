package org.csware.ee.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import org.csware.ee.shipper.UserAuthActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2015/4/2.
 */
public class ImageByAndroid {
    private static String sheariamgepath = ""; // 裁剪后头像路径
    private String photographpath = ""; // 拍照完后头像路径
    private static Uri photoUri;
    public static Uri cameraUri;
    public String path = "";
    public static void  setUri(Uri uri){
        cameraUri = uri;
    }
    public static Uri getUri(){
        return cameraUri;
    }
    public static Bitmap mInsertPicture;

    public static Bitmap getmInsertPicture() {
        return mInsertPicture;
    }

    public static void setmInsertPicture(Bitmap bitmap) {
        mInsertPicture = bitmap;
    }
    /**
     * 裁剪图片方法实现
     *uri为选中图片返回的Uri
     *cutImgUri为把截取图片写入sd卡的Uri
     * @param uri
     */

    public static void startPhotoZoom(Uri uri, Activity context, Uri cutImgUri, boolean isCrop) {
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", ""+ isCrop);
        // 裁剪框的比例，4：3
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // 裁剪后输出图片的尺寸大小
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);// true:不返回uri，false：返回uri
        intent.putExtra(MediaStore.EXTRA_OUTPUT, cutImgUri);//写入截取的图片
        ImageByAndroid.setUri(cutImgUri);
        context.startActivityForResult(intent, UserAuthActivity.PHOTO_REQUEST_CUT);
    }

    /**
     * 手机拍照获取照片
     */
    public static void getPhotoByCamere(Activity context, int request) {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // 判断存储卡是否可以用，可用进行存储
        String state = Environment.getExternalStorageState();
        String sdcardPathDir = Environment
                .getExternalStorageDirectory().getPath() + "/tempImage/";
        File file = null;
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            File fileDir = new File(sdcardPathDir);
            if (!fileDir.exists()) {
                fileDir.mkdirs();// 创建文件夹
            }
            // 是否有headImg文件
            file = new File(sdcardPathDir + System.currentTimeMillis()
                    + ".JPEG");

//            photographpath = avatorpath + "qmimage" + new Date().getTime() + ".jpg";
//            CApplication.photographpath = photographpath;
//            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
        }else {
            Toast.makeText(context, "没有检测到SD卡", Toast.LENGTH_SHORT).show();
        }
        if (file != null) {
            photoUri = Uri.fromFile(file);
            setUri(photoUri);
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            context.startActivityForResult(intentFromCapture, request);
        }

    }

    /**
     * 从相册选择
     */
    public static void getPhotoFromAlum(Activity context,int request) {
//        Intent intentFromGallery = new Intent();
//        intentFromGallery.setType("image/*");// 设置文件类型
//        intentFromGallery.addCategory(Intent.CATEGORY_OPENABLE);
////        if (Build.VERSION.SDK_INT <19) {
////            intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
////        }else {
////            intentFromGallery.setAction( Intent.ACTION_OPEN_DOCUMENT);
////        }
//        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
//        context.startActivityForResult(intentFromGallery, request);
        Intent i = new Intent(
                // 相册
                Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(i, request);

    }
    /**
     * 手机拍照获取照片
     */
    public static String photo(Activity context,int request){
        String path = "";
        Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        openCameraIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);

        StringBuffer sDir = new StringBuffer();
        if (hasSDcard()) {
            sDir.append(Environment.getExternalStorageDirectory() + "/MyPicture/");
        } else {
            String dataPath = Environment.getRootDirectory().getPath();
            sDir.append(dataPath + "/MyPicture/");
        }

        File fileDir = new File(sDir.toString());
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        File file = new File(fileDir, String.valueOf(System.currentTimeMillis()) + ".jpg");

        path = file.getPath();
        Uri imageUri = Uri.fromFile(file);
        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        context.startActivityForResult(openCameraIntent, request);
        return path;
    }
    public static boolean hasSDcard() {
        String status = Environment.getExternalStorageState();
        if (status.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 根据Uri获取真实路径
     * */
    public static String getRealPathFromURI(Context context, Uri contentUri) {
        String res = null;
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
        if(cursor.moveToFirst()){
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            res = cursor.getString(column_index);
        }
        cursor.close();
        return res;
    }
    /**
     * Uri转为Bitmap
     * */
    public static Bitmap getBitmapFromUri(Context context, Uri uri)
    {
        try
        {
            // 读取uri所在的图片
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), uri);
            return bitmap;
        }
        catch (Exception e)
        {
//            Log.e("[Android]", e.getMessage());
//            Log.e("[Android]", "目录为：" + uri);
            e.printStackTrace();
            return null;
        }
        catch (OutOfMemoryError error){
            error.printStackTrace();
            return null;
        }
    }
    /**
     * Bitmap转为图片保存
     * */
    public static final String avatorpath = Environment.getExternalStorageDirectory() + "/shipper/";
    public static void saveBitmap(String bitName, Bitmap mBitmap) throws IOException {
        File f = null;
        makeRootDirectory(avatorpath);
        try {
            f = new File(avatorpath + bitName + ".jpg");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public static void saveBitmap(String bitName, String timeAddress, Bitmap mBitmap) throws IOException {
        File f = null;
        makeRootDirectory(avatorpath);
        try {
            f = new File(FileUtils.SDPATH  + bitName + timeAddress + ".JPEG");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        mBitmap.compress(Bitmap.CompressFormat.JPEG, 90, fOut);
        try {
            fOut.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取图片的旋转的角度
     *
     * @param path
     *            图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }
    /**
     * 将图片按照某个角度进行旋转
     *
     * @param bm
     *            需要旋转的图片
     * @param degree
     *            旋转角度
     * @return 旋转后的图片
     */
    public static Bitmap rotateBitmapByDegree(Bitmap bm, int degree) {
        Bitmap returnBm = null;

        // 根据旋转角度，生成旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(degree);
        try {
            // 将原始图片按照旋转矩阵进行旋转，并得到新的图片
            returnBm = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        if (returnBm == null) {
            returnBm = bm;
        }
//        if (bm != returnBm) {
//            bm.recycle();
//        }
        return returnBm;
    }
    public static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {

        }
    }

    /**
     * 判断储存卡是否可用
     * @return
     */
    public static boolean hasSdcard() {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }
    /**
     * 创建文件夹
     * @param path
     * @return
     */
    public static String getPath(String path){
        File f=new File(path);
        if(!f.exists()){
            f.mkdirs();
        }
        return f.getAbsolutePath();
    }
    /**
     * 创建文件夹
     * @param path
     * @return
     */
    public static File getFile(String path){
        File f=new File(path);
        if(!f.exists()){
            try {
                f.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return f;
    }
    /**
     * uri转换为bitmap
     * @param uri
     * @return
     */
    public static Bitmap decodeUriAsBitmap(Context context, Uri uri){
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError error){
            error.printStackTrace();
            return null;
        }
        return bitmap;
    }

    public static Bitmap decodeUriAsBitmap(Context context, Uri uri, int h, int w){
        Bitmap bitmap = null;
        Bitmap bit= null;
        try {

            //由于返回的图像可能太大而无法完全加载到内存中。系统有限制，需要处理。
            int defaultHeight = h;
            int defaultWidth = w;

            BitmapFactory.Options bitmapFactoryOptions = new BitmapFactory.Options();
            bitmapFactoryOptions.inJustDecodeBounds = true;///只是为获取原始图片的尺寸，而不返回Bitmap对象
            bit = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapFactoryOptions);
            int outHeight = bitmapFactoryOptions.outHeight;
            int outWidth = bitmapFactoryOptions.outWidth;
            int heightRatio = (int) Math.ceil((float)outHeight/defaultHeight);
            int widthRatio = (int) Math.ceil((float)outWidth/defaultWidth);

            if (heightRatio > 1 || widthRatio >1) {
                if (heightRatio > widthRatio) {
                    bitmapFactoryOptions.inSampleSize = heightRatio;
                } else {
                    bitmapFactoryOptions.inSampleSize = widthRatio;
                }
            }
            bitmapFactoryOptions.inJustDecodeBounds = false;
//            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri));
            bit = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(uri), null, bitmapFactoryOptions);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError error){
            error.printStackTrace();
            return null;
        }
        return bit;
    }

}
