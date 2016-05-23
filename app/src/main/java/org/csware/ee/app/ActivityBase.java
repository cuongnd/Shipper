package org.csware.ee.app;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalActivity;
import net.tsz.afinal.FinalDb;

import org.csware.ee.Guard;
import org.csware.ee.shipper.LoginActivity;
import org.csware.ee.shipper.app.App;
import org.csware.ee.utils.DbManager;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Atwind on 2015/5/14.
 * 通用ActivityBase，FinalActivity(可以实现View控件注入)
 * 默认将Title标题隐藏了,增加了View.OnClickListener的实现
 */
public class ActivityBase extends FinalActivity implements View.OnClickListener {

    /**当前Activity*/
    protected Activity baseActivity;

    /**当前上下文*/
    protected Context baseContext;
    boolean isOvered;
    AlertDialog dlg;
    SharedPreferences  preces;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        baseActivity = this;
        baseContext = this.getBaseContext();
        App.getInstance().addActivity(this);
        dlg = new AlertDialog.Builder(baseActivity).create();
        SharedPreferences preference = getSharedPreferences("InitJpush",MODE_PRIVATE);
        preces = getSharedPreferences("isOverled", MODE_PRIVATE);
        isOvered = preces.getBoolean("isOverled",false);
        String JpushId = preference.getString("JpushId","");
        if (!Guard.isNullOrEmpty(JpushId)){
            JPushInterface.resumePush(getApplicationContext());
        }
    }


    @Override
    protected void onStart(){
        super.onStart();
    }

    @Override
    protected void onResume() {
        MobclickAgent.onResume(baseActivity);
        super.onResume();
        if (isOvered){
//            DialogUtil.showExitAlert(listener,dlg);
        }
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dlg.dismiss();
            SharedPreferences.Editor editor = preces.edit();
            editor.clear().commit();
            startActivity(new Intent(baseActivity, LoginActivity.class));
//            finish();
        }
    };

    @Override
    protected void onPause() {
        MobclickAgent.onPause(baseActivity);
        super.onPause();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }



    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }


    /**
    * 创建并返回Db
    * /n注意：FinalDb并不能从继承的模型中获得父类的信息，因此，不能使用继承功能来写实体类
    * */
    protected FinalDb openDb()
    {
        return DbManager.OpenDb(baseActivity);
    }


    /**慢3秒*/
    protected void toastSlow(int stringId) {
        //3秒
//        Toast.makeText(baseActivity,getResources().getString(stringId),Toast.LENGTH_LONG).show();
        showToast(stringId, Toast.LENGTH_LONG);
    }

    protected void toastSlow(String msg) {
        //3秒
//        Toast.makeText(baseActivity,msg,Toast.LENGTH_LONG).show();
        showToast(msg, Toast.LENGTH_LONG);
    }

    /**快1秒*/
    protected  void toastFast(int stringId) {
        //1秒
//        Toast.makeText(baseActivity,getResources().getString(stringId), Toast.LENGTH_SHORT).show();
        showToast(stringId, Toast.LENGTH_SHORT);
    }

    protected  void toastFast(String msg) {
        //1秒
        showToast(msg, Toast.LENGTH_SHORT);
//        Toast.makeText(baseActivity,msg, Toast.LENGTH_SHORT).show();
    }

    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;
    public void showToast(String s, int duration){
        if (toast == null) {
            toast = Toast.makeText(baseActivity, s, duration);
            toast.show();
            oneTime = System.currentTimeMillis();
        } else {
            twoTime = System.currentTimeMillis();
            if (s.equals(oldMsg)) {
                if (twoTime - oneTime > duration) {
                    toast.show();
                }
            } else {
                oldMsg = s;
                toast.setText(s);
                toast.show();
            }
        }
        oneTime = twoTime;
    }
    public void showToast(int resId, int duration){
            showToast(baseActivity.getString(resId), duration);
    }

    /**创建一个模态对话框*/
    protected ProgressDialog createDialog(String msg) {
        ProgressDialog dialog = ProgressDialog.show(baseActivity, null, msg, true, true);
        dialog.setCanceledOnTouchOutside(false); //禁止触碰取消
        // dialog.getWindow().setContentView(R.layout.refresh);
        return dialog;
    }
    /**创建一个模态对话框*/
    protected ProgressDialog createDialog(int stringId){
        return createDialog(getResources().getString(stringId));
    }

    /**
     * 点击其他地方 失去焦点  隐藏软键盘
     * */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {

            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            View v = getCurrentFocus();

            if (isShouldHideInput(v, ev)) {
                hideSoftInput(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }

    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideInput(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {
            int[] l = { 0, 0 };
            v.getLocationInWindow(l);
            int left = l[0], top = l[1], bottom = top + v.getHeight(), right = left
                    + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 多种隐藏软件盘方法的其中一种
     *
     * @param token
     */
    private void hideSoftInput(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token,
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
