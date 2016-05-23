package org.csware.ee.shipper.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.csware.ee.api.ToolApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.AlipayInfo;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.HuifuCityModel;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.RegistModel;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.UpdataInfo;
import org.csware.ee.shipper.LoginActivity;
import org.csware.ee.shipper.MainTabFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserSettingFragmentActivity;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.DialogUtil;
import org.csware.ee.utils.DownLoadManager;
import org.csware.ee.utils.Tools;

import java.io.File;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserSettingFragment extends FragmentBase {
    @InjectView(R.id.btnMessageSetting)
    LinearLayout btnMessageSetting;
    @InjectView(R.id.btnSystemNotice)
    LinearLayout btnSystemNotice;
    @InjectView(R.id.btnCheckUpdate)
    LinearLayout btnCheckUpdate;
    @InjectView(R.id.btnAboutUs)
    LinearLayout btnAboutUs;
    @InjectView(R.id.btnExit)
    LinearLayout btnExit;
    ProgressDialog dialog;
    UpdataInfo info = new UpdataInfo();
    UpdataInfo.VersionEntity versionEntity = new UpdataInfo.VersionEntity();
    @InjectView(R.id.txt_version)
    TextView txtVersion;
    AlertDialog dlg;
    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_setting_fragment;
    }

    @Override
    public void init() {
        ((UserSettingFragmentActivity) getActivity()).finish(getString(R.string.icon_setting));
        dlg  = new AlertDialog.Builder(baseActivity).create();
        txtVersion.setText("v"+getVersion());
    }

    @OnClick({R.id.btnMessageSetting})
    void openMessageSetting(View v) {
        ((UserSettingFragmentActivity) getActivity()).replace(new UserSettingMessageFragment(), "消息设置");
    }

    @OnClick({R.id.btnSystemNotice})
    void openSystemNotice(View v) {
        ((UserSettingFragmentActivity) getActivity()).replace(new UserSysNotifyFragment(), "系统消息");
    }

    @OnClick({R.id.btnCheckUpdate})
    void openCheckUpdate(View v) {
        if (Tools.getNetWork(baseActivity)) {
            dialog = Tools.getDialog(baseActivity);
            dialog.setCanceledOnTouchOutside(false);
//        ((UserSettingFragmentActivity) getActivity()).replace(new UserSysNotifyFragment(), "检查更新");
            //检查更新
            CheckVersionTask cTask = new CheckVersionTask();
            cTask.start();// 启动线程
        }else {
            toastFast(R.string.tip_need_net);
        }
    }


    @OnClick({R.id.btnAboutUs})
    void openAboutUs(View v) {
        ((UserSettingFragmentActivity) getActivity()).replace(new UserAboutUsFragment(), "关于我们");
    }


    @OnClick({R.id.btnExit})
    void opExit(View v) {
        if (Tools.getNetWork(baseActivity)) {
            DbCache dbCache = new DbCache(baseActivity);
//        Shipper user = dbCache.GetObject(Shipper.class);
            Shipper user = new Shipper();
            MeReturn userInfo = new MeReturn();
            AccountReturn ac = new AccountReturn();
            BindStatusInfo bankinfo = new BindStatusInfo();
            DeliverInfo deliverInfo = new DeliverInfo();
            AlipayInfo alipayInfo = new AlipayInfo();
            RegistModel registModel = new RegistModel();
            OilCardInfo info = new OilCardInfo();
            HuifuCityModel cityModel = new HuifuCityModel();
            user.setToken(null);
            ClientStatus.clearToken();
            dbCache.save(bankinfo);
            dbCache.save(deliverInfo);
            dbCache.save(ac);
            dbCache.save(user);
            dbCache.save(userInfo);
            dbCache.save(registModel);
            dbCache.save(alipayInfo);
            dbCache.save(info);
            dbCache.save(cityModel);
//        XGPushManager.unregisterPush(getActivity());
            SharedPreferences preference = baseActivity.getSharedPreferences("InitJpush",baseActivity.MODE_PRIVATE);
            SharedPreferences.Editor edit = preference.edit();
            edit.clear().commit();
            SharedPreferences preferences = baseActivity.getSharedPreferences("TempURL",baseActivity.MODE_PRIVATE);
            SharedPreferences.Editor edits = preferences.edit();
            edits.clear().commit();
//            SharedPreferences unitTime = baseActivity.getSharedPreferences("unitTimeLag", baseActivity.MODE_PRIVATE);
//            SharedPreferences.Editor editor = unitTime.edit();
//            editor.clear().commit();
            JPushInterface.stopPush(baseActivity.getApplicationContext());
            baseFragment.getActivity().finish();
            MainTabFragmentActivity.instance.finish();
//        ActivityManager am = (ActivityManager) baseActivity.getSystemService(baseActivity.ACTIVITY_SERVICE);
//        am.restartPackage(baseActivity.getPackageName());
//        System.exit(1);
//        android.os.Process.killProcess(android.os.Process.myPid());
//        System.exit(1);

            baseActivity.startActivity(new Intent(baseActivity, LoginActivity.class));
        }else {
            toastFast(org.csware.ee.R.string.tip_need_net);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    /**
     * 从服务器获取json解析并进行比对版本号
     */
    public class CheckVersionTask extends Thread {

        public void run() {
            Message msg = new Message();
            handler.sendMessage(msg);
        }
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToolApi.update(baseFragment.getActivity(), baseActivity, new IJsonResult<UpdataInfo>() {
                @Override
                public void ok(UpdataInfo json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    versionEntity.VersionCode = json.Version.VersionCode;
                    versionEntity.VersionName = json.Version.VersionName;
                    versionEntity.Description = json.Version.Description;
                    versionEntity.Url = json.Version.Url;
                    info.Version = versionEntity;
                    compareversion();
                }

                @Override
                public void error(Return json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            });
        }
    };

    // 比较版本号方法
    protected void compareversion() {
        int versioncode = getVersionCode();
        if (versioncode < Integer.valueOf(versionEntity.VersionCode)) {
            showUpdataDialog();
//            Message msg = new Message();
//            msg.what = UPDATA_CLIENT;
//            handler.sendMessage(msg);
        } else {
            Toast.makeText(baseActivity, "已是最新版本", Toast.LENGTH_SHORT).show();
//            LoginMain();
        }

    }
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dlg.dismiss();
            dlg.cancel();
            if (versionEntity.Url.contains("http")) {
                downLoadApk();
            }else {
                toastFast("下载新版本失败");
            }
        }
    };
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            SharedPreferences preferences = baseActivity.getSharedPreferences("UpdateTip", baseActivity.MODE_PRIVATE);
//        boolean isShowUpdateTip = preferences.getBoolean("isShowTip",false);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isShowTip", true).commit();
            dlg.dismiss();
            dlg.cancel();
        }
    };
    /**
     * 弹出对话框通知用户更新程序 弹出对话框的步骤：
     * 1.创建alertDialog的builder.
     * 2.要给builder设置属性,* 对话框的内容,样式,按钮
     * 3.通过builder 创建一个对话框
     * 4.对话框show()出来
     */
    protected void showUpdataDialog() {
        try {
             dlg.setCanceledOnTouchOutside(false);
            String vDetail = versionEntity.Description;
            //1.优化界面,2.新增汇付功能,3.新增积分商城,4.在闲暇时可以玩游戏啦,5.修复若干BUG
            String des = "";
            if (vDetail.contains(",")){
                String[] ds =vDetail.split(",");
                StringBuffer sb = new StringBuffer();
                if (ds.length>0){
                    for (int i=0;i<ds.length;i++){
                        sb.append(ds[i]);
                        if (i!=(ds.length-1)){
                            sb.append("\n");
                        }
                    }
                    des = sb.toString();
                }
            }
            DialogUtil.showUpdateAlert("小象快运 v"+versionEntity.VersionName, des, getString(R.string.dialog_later), getString(R.string.dialog_update), "发现新版本", listener,cancelListener,dlg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(baseActivity);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    //创建一个文件夹来放安装包
                    File fileRjban = new File(Environment.getExternalStorageDirectory() + getResources().getString(R.string.storePath));
                    if (!fileRjban.exists()) {
                        fileRjban.mkdirs();
                    }
                    File file = DownLoadManager.getFileFromServer(info, pd, fileRjban);
                    sleep(2000);
                    installApk(file);
                    pd.dismiss(); // 结束掉进度条对话框
                } catch (Exception e) {
//                    toastFast("下载新版本失败");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 安装apk
     *
     * @param file
     */
    protected void installApk(File file) {
        Intent intent = new Intent();
        // 执行动作
        intent.setAction(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//添加的一句
        // 执行的数据类型
        intent.setDataAndType(Uri.fromFile(file),
                "application/vnd.android.package-archive");// 特殊类型，在此处固定

		/*//监听系统新安装程序的广播 (监听已经在主配置AndroidManifest.xml中注册了)
        BootReceiver receiver= new BootReceiver();
        android.content.IntentFilter filter = new android.content.IntentFilter(Intent.ACTION_PACKAGE_ADDED);
        filter.addDataScheme("package");    //必须添加这项，否则拦截不到广播
        registerReceiver(receiver, filter);*/

        startActivity(intent);
    }

    /**
     * 取得应用的版本号
     *
     * @return
     */
    public String getVersion() {
        PackageManager pm = baseActivity.getPackageManager();   //取得包管理器的对象，这样就可以拿到应用程序的管理对象
        try {
            PackageInfo info = pm.getPackageInfo(baseActivity.getPackageName(), 0);   //得到应用程序的包信息对象
            return info.versionName;   //取得应用程序的版本号
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            //此异常不会发生
            return "";
        }
    }

    /**
     * 获取当前程序的版本号
     */
    public int getVersionCode() {
        // 获取packagemanager的实例
        PackageManager packageManager = baseActivity.getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(baseActivity.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }


}
