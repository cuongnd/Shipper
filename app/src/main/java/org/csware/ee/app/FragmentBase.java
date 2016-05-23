package org.csware.ee.app;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.shipper.LoginActivity;

import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Yu on 2015/5/29.
 * Fragment 基类
 * 1. 注入
 */
public abstract class FragmentBase extends Fragment {

    protected View rootView;

    protected Fragment baseFragment;

    protected Context baseActivity;

    protected boolean isVisible;
    boolean isOvered;
    AlertDialog dlg;
    SharedPreferences  preces;
    protected abstract boolean getIsPrepared();

    /**
     * 在这里实现Fragment数据的缓加载.
     * @param isVisibleToUser
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }
    protected void onVisible(){
        lazyLoad();
    }
    protected abstract void lazyLoad();
    protected void onInvisible(){}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseFragment = this;
        baseActivity = getActivity();
        dlg = new AlertDialog.Builder(baseActivity).create();
        preces = baseActivity.getSharedPreferences("isOverled", baseActivity.MODE_PRIVATE);
        isOvered = preces.getBoolean("isOverled", false);
        SharedPreferences preference = baseActivity.getSharedPreferences("InitJpush",baseActivity.MODE_PRIVATE);
        String JpushId = preference.getString("JpushId","");
        if (!Guard.isNullOrEmpty(JpushId)){
            JPushInterface.resumePush(baseActivity.getApplicationContext());
        }
    }

    @Override
    public void onResume() {
        MobclickAgent.onResume(baseActivity);
        super.onResume();
        if (isOvered){
//            DialogUtil.showExitAlert(listener, dlg);
        }
    }

    @Override
    public void onPause() {
        MobclickAgent.onPause(baseActivity);
        super.onPause();
    }

    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dlg.dismiss();
            SharedPreferences.Editor editor = preces.edit();
            editor.clear().commit();
            if (baseActivity instanceof LoginActivity) startActivity(new Intent(baseActivity, LoginActivity.class));
//            getActivity().finish();
        }
    };

    protected abstract int getLayoutId();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(getLayoutId(), container, false);
        //初始化view的各控件
        ButterKnife.inject(this, rootView);
        init();
        lazyLoad();
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 内容初始化，必须重写
     */
    public abstract void init();


    /**
     * 返回不用强转类型的View对象 = findViewById
     * @param resId  资源Id
     * @param <T>
     * @return
     */
    protected <T extends View> T getView(int resId){
        View view = rootView.findViewById(resId);
        return (T) view;
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
        if(baseActivity == null) return;
        showToast(baseActivity.getString(resId), duration);
    }

    /**创建一个模态对话框*/
    protected ProgressDialog createDialog(String msg) {
        ProgressDialog dialog = ProgressDialog.show(baseActivity, null, msg, true, true);
        //dialog.setView(); //TODO:重设显示UI
        dialog.setCanceledOnTouchOutside(false); //禁止触碰取消
        // dialog.getWindow().setContentView(R.layout.refresh);
        return dialog;
    }
    /**创建一个模态对话框*/
    protected ProgressDialog createDialog(int stringId){
        return createDialog(getResources().getString(stringId));
    }



}
