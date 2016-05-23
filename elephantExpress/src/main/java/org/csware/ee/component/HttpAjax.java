package org.csware.ee.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;
import net.tsz.afinal.http.AjaxParams;

import org.csware.ee.Guard;
import org.csware.ee.R;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.DialogHelper;
import org.csware.ee.utils.GsonHelper;

import java.util.HashMap;

/**
 * Http方式的API异步请求方式
 * <p/>
 * 统一入口调用，方便修改，链式调用
 * <p/>
 * Created by Yu on 2015/8/6.
 */
public class HttpAjax extends FinalHttp {

    final static String TAG = "HttpAjax";

    public static int TIP_SHOW_RPOGRESS = 2;//显示提交请求框
    public static int TIP_SHOW_NEED_NET = 1; //是否显示网络提示


    Context context;
    boolean isShowProgressTip = true; //默认显示提示框
    boolean isShowNetNeed = true;
    ProgressDialog dialog;
    /**
     * 默认初始化
     */
    public HttpAjax(Context context) {
        this.context = context;
    }

    /**
     * !2设置是否显示网络请求中的框框
     */
    public HttpAjax setTipDisplay(int tip_show_pogress) {
        isShowProgressTip = tip_show_pogress == TIP_SHOW_RPOGRESS;
        return this;
    }

    /**
     * !2设置是否提示网络可用性
     */
    public HttpAjax setTipNet(int tip_show_need_net) {
        isShowNetNeed = TIP_SHOW_NEED_NET == tip_show_need_net;
        return this;
    }


    /**
     * 开始请求
     */
    public void beginRequest(HttpParams httpParams, final IAjaxResult ajax) {

        if (isShowNetNeed) {
            if (!ClientStatus.getNetWork(context)) {
                toastSlow(R.string.tip_need_net,true);
                return;
            }
        }
        long unitTimeLag = 0;
//        DbCache db = new DbCache(context);
//        Shipper user = db.GetObject(Shipper.class);
//        if (!Guard.isNull(user.unitTimeLag)){
//            unitTimeLag = user.unitTimeLag;
//        }
        SharedPreferences preferences = context.getSharedPreferences("unitTimeLag", context.MODE_PRIVATE);
        unitTimeLag = preferences.getLong("unitTimeLag",0);
        Tracer.e(TAG,unitTimeLag+"");
        final String url = httpParams.getUrl(unitTimeLag);
        Tracer.e(TAG, "REQUEST: " + url);
        get(url, new AjaxCallBack<String>() {


            @Override
            public void onStart() {
                openProgress();
            }

            @Override
            public void onLoading(long count, long current) {
                if (count == current) {
                    Tracer.d(TAG, "DONE");
                }
            }

            @Override
            public void onSuccess(String res) {
                ajax.notify(true, res);//成功获取返回
//                if (Tracer.isDebug()) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        SharedPreferences  preferences = context.getSharedPreferences("InstallStatus",context.MODE_PRIVATE);
                        SharedPreferences  preces = context.getSharedPreferences("isOverled",context.MODE_PRIVATE);
                        boolean isFirstInstall = preferences.getBoolean("isFirstInstall", true);
                        boolean isShow = true;
                        Tracer.e(TAG, "RESPONSE=    \n" + res);
                        Return result = GsonHelper.fromJson(res, Return.class);
                        if (result != null) {
                            if (result.Result != 0) {
                                String msg = Guard.isNullOrEmpty(result.Message) ? context.getString(R.string.result_error) : result.Message;
    //                        msg = result.Result + " \n " + msg;
                                Tracer.w(TAG, msg);
                                Tracer.w(TAG, "REQUEST: " + url);
                                if (!Guard.isNullOrEmpty(result.Message)) {
                                    if (result.Message.equals("货主状态错误")) {
                                        msg = "暂未实名认证,认证后更多功能等着您！";
                                    } else if (result.Message.equals("接口返回内容出错")) {

                                        if (isFirstInstall){
                                            msg = "请登录后操作";
                                        }else {
                                            msg = "登录过期，请重新登录";
                                            if (!Guard.isNull(result.Validator)){
                                                if (!Guard.isNullOrEmpty(result.Validator)){
                                                    msg = result.Validator;
                                                }
                                            }
                                            isShow = true;
//                                            SharedPreferences.Editor editor = preces.edit();
//                                            editor.putBoolean("isOverled",true).commit();
                                        }
                                    }

                                }
                                if (msg.equals("接口返回内容出错")) {
                                    if (isFirstInstall){
                                        msg = "请登录后操作";
                                    }else {
                                        msg = "签名过期，请重新登录";
                                        if (!Guard.isNull(result.Validator)){
                                            if (!Guard.isNullOrEmpty(result.Validator)){
                                                msg = result.Validator;
                                            }
                                        }
                                        isShow = true;
//                                        SharedPreferences.Editor editor = preces.edit();
//                                        editor.putBoolean("isOverled",true).commit();
                                    }
                                }
                                if (!Guard.isNullOrEmpty(result.RawMessage)){
                                    if (Guard.isNullOrEmpty(result.Message)) {
                                        msg = result.RawMessage;
                                    }
                                }
                                    toastSlow(msg, isShow);
                            }
                        }
                    }
//                }
                closeProgress();
            }

            @Override
            public void onFailure(Throwable t, int errCode, String strMsg) {
                //加载失败的时候回调
                ajax.notify(false, null);
                Tracer.e(TAG, "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
                //processDialog.dismiss();
//                toastSlow(R.string.result_failed);
                if (errCode==500){
                    toastSlow(R.string.result_failed_service, true);
                }else {
                    toastSlow(R.string.result_failed_net, true);
                }
                closeProgress();
            }
        });
    }

    /**
     * 重构请求方式
     * */
    public void beginRequest(final Activity activity, HttpParams httpParams, final IAjaxResult ajax) {

        if (isShowNetNeed) {
            if (!ClientStatus.getNetWork(context)) {
                toastSlow(R.string.tip_need_net,true);
                return;
            }
        }
        long unitTimeLag = 0;

//        if (!Guard.isNull(user.unitTimeLag)){
//            unitTimeLag = user.unitTimeLag;
//        }
        SharedPreferences preferences = context.getSharedPreferences("unitTimeLag", context.MODE_PRIVATE);
        unitTimeLag = preferences.getLong("unitTimeLag",0);
        Tracer.e(TAG,unitTimeLag+"");
        final String url = httpParams.getUrl(unitTimeLag);
        Tracer.e(TAG, "REQUEST: " + url);
        get(url, new AjaxCallBack<String>() {


            @Override
            public void onStart() {
                openProgress();
            }

            @Override
            public void onLoading(long count, long current) {
                if (count == current) {
                    Tracer.d(TAG, "DONE");
                }
            }

            @Override
            public void onSuccess(String res) {
                ajax.notify(true, res);//成功获取返回
//                if (Tracer.isDebug()) {
                if (res.startsWith("{")&&res.endsWith("}")) {
                    SharedPreferences  preferences = context.getSharedPreferences("InstallStatus",context.MODE_PRIVATE);
                    SharedPreferences  preces = context.getSharedPreferences("isOverled",context.MODE_PRIVATE);
                    boolean isFirstInstall = preferences.getBoolean("isFirstInstall", true);
                    boolean isShow = true;
                    Tracer.e(TAG, "RESPONSE=    \n" + res);
                    Return result = null;
                    if (res.startsWith("{")||res.startsWith("{")) {
                        result = GsonHelper.fromJson(res, Return.class);
                    }
                    if (result != null) {
                        if (result.Result != 0) {
                            if (result.Result == -1) {
                                //TODO 登录被挤，强制登出
                                DbCache db = new DbCache(context);
                                Shipper user = new Shipper();
                                db.save(user);
                                Class homeC = null;
                                try {
                                    homeC = Class.forName("org.csware.ee.shipper.LoadingActivity");
                                } catch (ClassNotFoundException e) {
                                    e.printStackTrace();
                                }
                                Intent intent = new Intent(context,homeC);
                                intent.putExtra("isShow",true);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                                context.startActivity(intent);
                                if (activity!=null) {
                                    activity.finish();
                                }
                            }else {
                                String msg = Guard.isNullOrEmpty(result.Message) ? context.getString(R.string.result_error) : result.Message;
                                //                        msg = result.Result + " \n " + msg;
                                Tracer.w(TAG, msg);
                                Tracer.w(TAG, "REQUEST: " + url);
                                if (!Guard.isNullOrEmpty(result.Message)) {
                                    if (result.Message.equals("货主状态错误")) {
                                        msg = "暂未实名认证,认证后更多功能等着您！";
                                    } else if (result.Message.equals("接口返回内容出错")) {

                                        if (isFirstInstall) {
                                            msg = "请登录后操作";
                                        } else {
                                            msg = "登录过期，请重新登录";
                                            if (!Guard.isNull(result.Validator)) {
                                                if (!Guard.isNullOrEmpty(result.Validator)) {
                                                    msg = result.Validator;
                                                }
                                            }
                                            isShow = true;
//                                            SharedPreferences.Editor editor = preces.edit();
//                                            editor.putBoolean("isOverled",true).commit();
                                        }
                                    }

                                }
                                if (msg.equals("接口返回内容出错")) {
                                    if (isFirstInstall) {
                                        msg = "请登录后操作";
                                    } else {
                                        msg = "签名过期，请重新登录";
                                        if (!Guard.isNull(result.Validator)) {
                                            if (!Guard.isNullOrEmpty(result.Validator)) {
                                                msg = result.Validator;
                                            }
                                        }
                                        isShow = true;
//                                        SharedPreferences.Editor editor = preces.edit();
//                                        editor.putBoolean("isOverled",true).commit();
                                    }
                                }
                                if (!Guard.isNullOrEmpty(result.RawMessage)) {
                                    if (Guard.isNullOrEmpty(result.Message)) {
                                        msg = result.RawMessage;
                                    }
                                }
                                toastSlow(msg, isShow);
                            }
                        }
                    }
                }
//                }
                closeProgress();
            }

            @Override
            public void onFailure(Throwable t, int errCode, String strMsg) {
                //加载失败的时候回调
                ajax.notify(false, null);
                Tracer.e(TAG, "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
                //processDialog.dismiss();
//                toastSlow(R.string.result_failed);
                if (errCode==500){
                    toastSlow(R.string.result_failed_service, true);
                }else {
                    toastSlow(R.string.result_failed_net, true);
                }
                closeProgress();
            }
        });
    }
    /**
     * post 请求
     * */
    public void beginPostRequest(String url,HashMap<String,String> map, final IAjaxResult ajax) {

        if (isShowNetNeed) {
            if (!ClientStatus.getNetWork(context)) {
                toastSlow(R.string.tip_need_net, true);
                return;
            }
        }
        post(url, new AjaxParams(map), new AjaxCallBack<String>() {


            @Override
            public void onStart() {
                openProgress();
            }

            @Override
            public void onLoading(long count, long current) {
                if (count == current) {
                    Tracer.d(TAG, "DONE");
                }
            }

            @Override
            public void onSuccess(String res) {
                ajax.notify(true, res);//成功获取返回
//                if (Tracer.isDebug()) {
                    Tracer.d(TAG, "RESPONSE=    \n" + res);
                    Return result = GsonHelper.fromJson(res, Return.class);
                    if (result!=null) {
                        if (result.Result != 0) {
                            String msg = Guard.isNullOrEmpty(result.Message) ? context.getString(R.string.result_error) : result.Message;
                            if (Guard.isNull(result.Message)){
                                result.Message = context.getString(R.string.result_error);
                            }
//                        msg = result.Result + " \n " + msg;
                            Tracer.w(TAG, msg);
                            if (result.Message.equals("货主状态错误")) {
                                msg = "暂未实名认证,认证后更多功能等着您！";
                            }
                            if (!msg.equals("订单编号错误")) {
                                toastSlow(msg, true);
                            }
                        }
                    }
//                }
                closeProgress();
            }

            @Override
            public void onFailure(Throwable t, int errCode, String strMsg) {
                //加载失败的时候回调
                ajax.notify(false, null);
                Tracer.e(TAG, "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
                //processDialog.dismiss();
                if (errCode==500){
                    toastSlow(R.string.result_failed_service, true);
                }else {
                    toastSlow(R.string.result_failed_net, true);
                }
                closeProgress();
            }
        });
    }

   public void toastSlow(int resId, boolean isShow) {
//        String msg = context.getString(resId);
//        toastSlow(msg, isShow);
       showToast(resId, isShow);
    }

    public  void toastSlow(String msg, boolean isShow) {
        if (isShow) {
            showToast(msg);
        }
    }

    private static String oldMsg;
    protected static Toast toast   = null;
    private static long oneTime=0;
    private static long twoTime=0;
    public void showToast(String s){
            if (toast == null) {
                toast = Toast.makeText(context, s, Toast.LENGTH_SHORT);
                toast.show();
                oneTime = System.currentTimeMillis();
            } else {
                twoTime = System.currentTimeMillis();
                if (s.equals(oldMsg)) {
                    if (twoTime - oneTime > Toast.LENGTH_SHORT) {
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
    public void showToast(int resId, boolean isShow){
        if (isShow) {
            showToast(context.getString(resId));
        }
    }
    void openProgress() {

        if (isShowProgressTip) {
            dialog = createDialog(R.string.dialog_loading);
        }
    }

    void closeProgress() {
        if (dialog == null) return;
        dialog.dismiss();
    }

    /**
     * 创建一个模态对话框
     */
    ProgressDialog createDialog(String msg) {
        if(dialog == null) return null;
        ProgressDialog dialog = ProgressDialog.show(context, null, msg, true, true);
        dialog.setCanceledOnTouchOutside(false); //禁止触碰取消
        // dialog.getWindow().setContentView(R.layout.refresh);
        return dialog;
    }

    /**
     * 创建一个模态对话框
     */
    ProgressDialog createDialog(int resId) {
        if(context==null)return null;
        return createDialog(context.getResources().getString(resId));
    }

}
