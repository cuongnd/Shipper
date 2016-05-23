package org.csware.ee.utils;

import android.app.ProgressDialog;
import android.os.Environment;


import org.csware.ee.app.Tracer;
import org.csware.ee.model.UpdataInfo;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 下载管理类
 * @author Administrator
 */
public class DownLoadManager {
	/**从服务器上下载apk
	 * @param info
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(UpdataInfo info, ProgressDialog pd,File fileRjban) throws Exception {
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(info.Version.Url);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小
			int length = conn.getContentLength();
			pd.setMax(100);
			InputStream is = conn.getInputStream();
			File file = new File(fileRjban,"Shipper"+".apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				double rate=(double)100/length;
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				int p=(int)(total*rate);
				pd.setProgress(p);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}
}
