package org.csware.ee.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicInteger;

public class Utils {

	/**
	 * 动态生成View ID
	 * API LEVEL 17 以上View.generateViewId()生成
	 * API LEVEL 17 以下需要手动生成
	 */
	private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
	public static int generateViewId() {

		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
			for (; ; ) {
				final int result = sNextGeneratedId.get();
				// aapt-generated IDs have the high byte nonzero; clamp to the range under that.
				int newValue = result + 1;
				if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
				if (sNextGeneratedId.compareAndSet(result, newValue)) {
					return result;
				}
			}
		} else {
			return View.generateViewId();
		}
	}

    public static String getTextTime(String time){
        String getTime = "";
        String st = time.substring(time.lastIndexOf("(")+1,time.lastIndexOf("+"));
        Date date = new Date(Long.parseLong(st));
        getTime = Utils.getCurrentTime("yyyy-MM-dd HH时", date);
        return getTime;
    }

	public static String toUnixTime(String local,String format){
		SimpleDateFormat df = new SimpleDateFormat(format);
		String unix = "";
		try {
//            Date date = df.parse(local);
			unix = df.parse(local).getTime()/1000 + "";
//            unix = df.format(date);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return unix;
	}

	public static String getCurrentTime(String format) {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
		String currentTime = sdf.format(date);
		return currentTime;
	}
    public static String getCurrentTime(String format,Date date) {
//		Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.getDefault());
        String currentTime = sdf.format(date);
        return currentTime;
    }
	public static String dateLine2Date(String dateline) {
		String result = "";
		if (dateline.length() == 10) {
			long time = Long.parseLong(dateline);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
			result = sdf.format(new Date(time * 1000L));
		}
		return result;
	}
	public static String getCurrentTime() {
		return getCurrentTime("yyyy-MM-dd  HH:mm:ss");
	}
	public static int getFontSize(Context context, int textSize) {
		DisplayMetrics dm = new
				DisplayMetrics();
		WindowManager windowManager = (WindowManager)
				context
						.getSystemService(Context.WINDOW_SERVICE);

		windowManager.getDefaultDisplay().getMetrics(dm);
		int screenHeight = dm.heightPixels;
		// screenWidth = screenWidth > screenHeight ?  screenWidth : screenHeight;
		int rate = (int) (textSize * (float) screenHeight / 1280);
		return rate;
	}

	public static Bitmap decodeSampledBitmap(String path, int sample)
	{
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		// Calculate inSampleSize
		options.inSampleSize = sample;

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeFile(path, options);
	}

	public static LinearLayout.LayoutParams setParams(int weight,int left,int top,int right,int bottom,int width,int height){
		LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width,
				height);
		lp.weight = weight;
		lp.setMargins(left, top, right, bottom);
		return lp;
	}
	public static int getBigValue(double v1,double v2){
		int x=0;
		 x =  Double.compare(v1,v2);
		return x;
	}
	/**
	 * 动态增加控件
	 * */
	public static void addView(ViewGroup vg, View view){
		if(null == vg || null == view)
			return;
		vg.addView(view);
	}
	public static void getResponse(Context context,final String url,final Handler handler,final int what){
//		String json="";
		if (Tools.getNetWork(context)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String json = Tools.GetMethod(url);
					Message message = new Message();
					message.what = what;
					message.obj = json;
					handler.sendMessage(message);
				}
			}).start();
		}else {
			Toast.makeText(context,"网络未连接！",Toast.LENGTH_SHORT);
		}
//		return json;
	}
	public static void getResponse(Context context,final String url,final Handler handler,final int what,final int arg1){
//		String json="";
		if (Tools.getNetWork(context)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String json = Tools.GetMethod(url);
					Message message = new Message();
					message.what = what;
					message.arg1 = arg1;
					message.obj = json;
					handler.sendMessage(message);
				}
			}).start();
		}else {
			Toast.makeText(context, "网络未连接！", Toast.LENGTH_SHORT);
		}
//		return json;
	}

	public static void getResponse(Context context,final String url,final Handler handler,final int what,final int arg1,final int arg2){
//		String json="";
		if (Tools.getNetWork(context)) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					String json = Tools.GetMethod(url);
					Message message = new Message();
					message.what = what;
					message.arg1 = arg1;
					message.arg2 = arg2;
					message.obj = json;
					handler.sendMessage(message);
				}
			}).start();
		}else {
			Toast.makeText(context,"网络未连接！",Toast.LENGTH_SHORT);
		}
//		return json;
	}

	public static void setPricePoint(final EditText editText) {
		editText.addTextChangedListener(new TextWatcher() {

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
									  int count) {
				if (s.toString().contains(".")) {
					if (s.length() - 1 - s.toString().indexOf(".") > 2) {
						s = s.toString().subSequence(0,
								s.toString().indexOf(".") + 3);
						editText.setText(s);
						editText.setSelection(s.length());
					}
				}
				if (s.toString().trim().substring(0).equals(".")) {
					s = "0" + s;
					editText.setText(s);
					editText.setSelection(2);
				}

				if (s.toString().startsWith("0")
						&& s.toString().trim().length() > 1) {
					if (!s.toString().substring(1, 2).equals(".")) {
						editText.setText(s.subSequence(0, 1));
						editText.setSelection(1);
						return;
					}
				}
			}

			@Override
			public void afterTextChanged(Editable s) {

			}

		});

	}

}
