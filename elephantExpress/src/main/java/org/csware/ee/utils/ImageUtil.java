package org.csware.ee.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.Display;

import org.csware.ee.app.Tracer;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Yu on 2015/6/24.
 */
public class ImageUtil {


    public static void saveImage(Bitmap bitmap, String filePath) throws IOException {
        BufferedOutputStream os = null;
        try {
            File file = new File(filePath);
            if (!file.exists())
                file.createNewFile();
            os = new BufferedOutputStream(new FileOutputStream(file));
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Bitmap getBitmap(String filePath) {
        return getBitmap(filePath, 360, 360);
    }

    public static Bitmap getBitmap(String filePaht, Activity activity) {
        Display currentDisplay = activity.getWindowManager().getDefaultDisplay();
        int width = currentDisplay.getWidth();
        int height = currentDisplay.getHeight();
        return getBitmap(filePaht, width, height);
    }

    public static Bitmap getBitmap(String filePath, int width, int height) {
        File file = new File(filePath);
        return getBitmapFromFile(file, width, height);
    }

    /**
     * 图片压缩方法实现
     * @param srcPath 原图地址
     * @param finishPath 压缩后保存图片地址
     * @param avatorpath 保存的文件夹路径
     * @return
     */
    public static Bitmap getimage(String srcPath,String finishPath,String avatorpath,int width, int height) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        int hh = height;// 这里设置高度
        int ww = width;// 这里设置宽度
        // 缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;// be=1表示不缩放

        if (h > hh || w > ww) {

            // Calculate ratios of height and width to requested height and
            // width
            final int heightRatio = Math.round((float) h
                    / (float) hh);
            final int widthRatio = Math.round((float) w / (float) ww);
            // Choose the smallest ratio as inSampleSize value, this will
            // guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            be = heightRatio < widthRatio ? widthRatio : heightRatio;
        }
        Tracer.e("ImageUtil",be+" inSampleSize" +w+" h:"+h);
//        be = 1;
        newOpts.inSampleSize = be;// 设置缩放比例
//        if (w>=h){
//            newOpts.outWidth = 380;
//            newOpts.outHeight = 380 * (4/3);
//        }else if (h>w){
//            newOpts.outHeight = 380;
//            newOpts.outWidth = 380 * (4/3);
//        }
        Tracer.e("ImageUtil",be+" inSampleSize" +newOpts.outWidth+" h:"+newOpts.outHeight);
        newOpts.inPreferredConfig = Bitmap.Config.RGB_565;
        newOpts.inPurgeable = true;
        newOpts.inInputShareable = true;
        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
//        Bitmap newBitmap = BitmapFactory.decodeFile(srcPath,newOpts);
        return compressImage(bitmap,finishPath,avatorpath);// 压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     * @param image
     * @return
     */
    private static Bitmap compressImage(Bitmap image,String finishPath,String avatorpath) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 60, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 60;
        while (baos.toByteArray().length / 1024 > 100) { // 循环判断如果压缩后图片是否大于100kb,大于继续压缩
            baos.reset();// 重置baos即清空baos
            options -= 10;// 每次都减少10
            image.compress(Bitmap.CompressFormat.JPEG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中

        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        try {
            File fileDir = new File(avatorpath);
            if (!fileDir.exists()) {
                fileDir.mkdirs();// 创建文件夹
            }
            FileOutputStream out = new FileOutputStream(finishPath);
            bitmap.compress(Bitmap.CompressFormat.PNG, 60, out);
        } catch (Exception e) {
            e.printStackTrace();
        }
//        ImageUtil.toRoundBitmap(bitmap);
        return bitmap;
    }

    public static Bitmap getBitmapFromFile(File dst, int width, int height) {
        if (null != dst && dst.exists()) {
            BitmapFactory.Options opts = null;
            if (width > 0 && height > 0) {
                opts = new BitmapFactory.Options();
                opts.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(dst.getPath(), opts);
                // 计算图片缩放比例
                final int minSideLength = Math.min(width, height);
                opts.inSampleSize = computeSampleSize(opts, minSideLength,
                        width * height);
                opts.inJustDecodeBounds = false;
                opts.inInputShareable = true;
                opts.inPurgeable = true;
            }
            try {
                return BitmapFactory.decodeFile(dst.getPath(), opts);
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static int computeSampleSize(BitmapFactory.Options options,
                                        int minSideLength, int maxNumOfPixels) {
        int initialSize = computeInitialSampleSize(options, minSideLength,
                maxNumOfPixels);

        int roundedSize;
        if (initialSize <= 8) {
            roundedSize = 1;
            while (roundedSize < initialSize) {
                roundedSize <<= 1;
            }
        } else {
            roundedSize = (initialSize + 7) / 8 * 8;
        }

        return roundedSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options,
                                                int minSideLength, int maxNumOfPixels) {
        double w = options.outWidth;
        double h = options.outHeight;

        int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
                .sqrt(w * h / maxNumOfPixels));
        int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(Math
                .floor(w / minSideLength), Math.floor(h / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
            return 1;
        } else if (minSideLength == -1) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }
}
