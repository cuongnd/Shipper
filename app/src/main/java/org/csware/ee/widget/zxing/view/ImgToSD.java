package org.csware.ee.widget.zxing.view;

/**
 * Created with IntelliJ IDEA.
 * User: xiexy
 * Date: 14-3-5
 * Time: 下午2:06
 * To change this template use File | Settings | File Templates.
 */

import android.graphics.Bitmap;
import android.os.Environment;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 将生成的二维码保存在SD卡
 */

public class ImgToSD {

    /**
     * 将位图对象转换为字节数组
     * @param bm
     * @return
     */
    private byte[] Bitmap2Bytes(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }
    /**
     * 保存二维码至SD卡
     * @param filename
     * @param bitmap
     */
    public void saveToSDCard(String filename, Bitmap bitmap) throws Exception {
        // 获取SD卡的路径：Environment.getExternalStorageDirectory()
        File file = new File(Environment.getExternalStorageDirectory(),
                filename);
        FileOutputStream outStream = new FileOutputStream(file);
        outStream.write(Bitmap2Bytes(bitmap));
        outStream.close();
    }

}
