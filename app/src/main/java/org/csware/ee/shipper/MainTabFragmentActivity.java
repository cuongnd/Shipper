package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.NotificationCompat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;
import android.widget.Toast;

import com.jfeinstein.jazzyviewpager.JazzyViewPager;
import com.nineoldandroids.view.ViewHelper;
import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;


import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.api.ToolApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.model.UpdataInfo;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.app.App;
import org.csware.ee.shipper.fragment.HomeFragment;
import org.csware.ee.shipper.fragment.MineFragment;
import org.csware.ee.shipper.fragment.OrderTabFragment;
import org.csware.ee.shipper.fragment.TrackMapFragment;
import org.csware.ee.utils.DialogUtil;
import org.csware.ee.utils.DownLoadManager;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.zxing.CaptureActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

public class MainTabFragmentActivity extends FragmentActivityBase {

    final static String TAG = "MainTabFragAct";

    SystemBarTintManager mTintManager;
    JazzyViewPager mJazzy;
    List<Map<String, View>> tabViews = new ArrayList<Map<String, View>>();
    String[] pageNames;
    TabHost tabHost;
    TabWidget tabMenus;
    TopActionBar topActionBar;
    int userId = 0;
    public static Activity instance;
    static final int UPDATA_CLIENT = 1;
    static final int GET_RESPONSE = 2;
    static final int GET_UNDATAINFO_ERROR = -1;
    static final int DOWN_ERROR = -2;
    UpdataInfo info = new UpdataInfo();
    UpdataInfo.VersionEntity versionEntity = new UpdataInfo.VersionEntity();
    //Jpush
    public static boolean isForeground = false;
    BindStatusInfo bankinfo;
    MeReturn userInfo;
    DbCache dbCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main_tab);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        dlg  = new AlertDialog.Builder(baseActivity).create();
        instance = this;
        dbCache = new DbCache(baseActivity);
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }else {
            userId = shipper.userId;
        }

        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
        Tracer.e(TAG,"andorid API"+android.os.Build.VERSION.SDK_INT+"");
//        mTintManager = new SystemBarTintManager(this);
//        mTintManager.setStatusBarTintEnabled(true);
//        mTintManager.setNavigationBarTintEnabled(true);
        initData();//获取用户信息
        initTabBox();
        initXGService();
        registerMessageReceiver();  // used for receive msg
        topActionBar = (TopActionBar) findViewById(R.id.topBar);
        topActionBar.setVisibility(View.VISIBLE);
        topActionBar.setMenu(R.mipmap.button_scancode, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
//                Log.d(TAG, "showDelMenu");
                Intent intent = new Intent(baseActivity,CaptureActivity.class);
                startActivityForResult(intent,0);
            }
        });
        tabMenus = tabHost.getTabWidget();
        //检查更新
        CheckVersionTask cTask = new CheckVersionTask();
        cTask.start();// 启动线程

//        //Note:数字提醒
//        BadgeView badge = new BadgeView(baseActivity, tabMenus, 1);
//        //badge.setBadgePosition(BadgeView.POSITION_CENTER);
//        badge.setText("1");
//        badge.show();


    }

    void initData() {

        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                userInfo = json;
                dbCache.save(userInfo);
            }

            @Override
            public void error(Return json) {
            }
        });

    }

    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";

    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!Guard.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                setCostomMsg(showMsg.toString());
            }
        }
    }

    private void setCostomMsg(String msg){
//        if (null != msgText) {
//            msgText.setText(msg);
//            msgText.setVisibility(android.view.View.VISIBLE);
        toastFast(msg);
//        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("isTeamPage",MODE_PRIVATE);
        boolean isTeamPage = preferences.getBoolean("isTeamPage",false);
        if (isTeamPage){
            changeViewPager();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
        initXGService();
        JPushInterface.onResume(this);
        isForeground = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
        isForeground = false;
    }
    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }

    public void changeViewPager(){
        tabHost.setCurrentTab(1);
        setTabSelectedState(1, 4);
    }

    private void setStyleBasic(){
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(baseActivity);
        builder.statusBarDrawable = R.mipmap.logo;
        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL;  //设置为点击后自动消失
        builder.notificationDefaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;  //设置为铃声（ Notification.DEFAULT_SOUND）或者震动（ Notification.DEFAULT_VIBRATE）
        JPushInterface.setDefaultPushNotificationBuilder(builder);
        JPushInterface.setPushNotificationBuilder(2, builder);
//        NotificationManager manager = (NotificationManager) baseActivity.getSystemService(Context.NOTIFICATION_SERVICE);
//        NotificationCompat.Builder builders = new NotificationCompat.Builder(baseActivity);
//        builders.setSmallIcon(R.mipmap.logo).setContentTitle(JPushInterface.EXTRA_TITLE);
//        builders.setDefaults(Notification.DEFAULT_SOUND);
//        manager.notify(1,builders.build());
    }

    void initXGService() {

//        //设置提示数字签 TODO:有问题，会将原来的图像与文本给消掉。
//        BadgeView badge = new BadgeView(baseActivity, findViewById(R.id.tabTrack));
//        badge.setText("2");
//        badge.show();

        Tracer.d(TAG, "begin registerPush method. and userid=" + userId);

        //TODO:信鸽调试
        // 开启logcat输出，方便debug，发布时请关闭
        //XGPushConfig.enableDebug(this, true);
        // 如果需要知道注册是否成功，请使用 registerPush(getApplicationContext(), XGIOperateCallback)带callback版本
        // 如果需要绑定账号，请使用 registerPush(getApplicationContext(),account)版本
        // 具体可参考详细的开发指南
        // 传递的参数为ApplicationContext

        Context context = getApplicationContext();
        //XGPushManager.registerPush(context);
        // 注册接口
//        if (userId > 0) {
//            XGPushManager.registerPush(context, userId + "", xgioCallback);
//        }
        JPushInterface.init(context);
        setStyleBasic();
        String JpushId = JPushInterface.getRegistrationID(context);
        if (Guard.isNullOrEmpty(JpushId)){
            JPushInterface.resumePush(baseActivity.getApplicationContext());
            JpushId = JPushInterface.getRegistrationID(context);
        }
        Tracer.e(TAG,"JpushId:"+JpushId);
        SharedPreferences preference = getSharedPreferences("InitJpush",MODE_PRIVATE);
        SharedPreferences.Editor edit = preference.edit();
        edit.putString("JpushId",JpushId).commit();

        //TODO:友盟账户统计
        if (userId>0){
            MobclickAgent.onProfileSignIn(userId+"");
        }

        if (userId > 0 && !Guard.isNullOrEmpty(JpushId)) {
            //通知服务端绑定
            UserApi.notifyToken(baseContext,JpushId, new IJsonResult() {
                @Override
                public void ok(Return json) {
                    Tracer.i(TAG, "bind userid= " + userId + " to JPush's token[" + JPushInterface.getRegistrationID(baseActivity.getApplicationContext()) + "] is ok.");
                }

                @Override
                public void error(Return json) {

                }
            });
        }
// else {
//            XGPushManager.registerPush(context, xgioCallback);
//        }

//        // 2.36（不包括）之前的版本需要调用以下2行代码
//        Intent service = new Intent(context, XGPushService.class);
//        context.startService(service);

        // 其它常用的API：
        // 绑定账号（别名）注册：registerPush(context,account)或registerPush(context,account, XGIOperateCallback)，其中account为APP账号，可以为任意字符串（qq、openid或任意第三方），业务方一定要注意终端与后台保持一致。
        // 取消绑定账号（别名）：registerPush(context,"*")，即account="*"为取消绑定，解绑后，该针对该账号的推送将失效
        // 反注册（不再接收消息）：unregisterPush(context)
        // 设置标签：setTag(context, tagName)
        // 删除标签：deleteTag(context, tagName)
    }

//    XGIOperateCallback xgioCallback = new XGIOperateCallback() {
//
//        @Override
//        public void onSuccess(final Object data, int flag) {
//            Tracer.i(Constants.LogTag,
//                    "+++ register push sucess. token:" + data);
////                        m.obj = "+++ register push sucess. token:" + data;
////                        m.sendToTarget();
//            if (userId > 0) {
//                //通知服务端绑定
//                UserApi.notifyToken(baseContext, data.toString(), new IJsonResult() {
//                    @Override
//                    public void ok(Return json) {
//                        Tracer.i(TAG, "bind userid= " + userId + " to XG's token[" + data + "] is ok.");
//                    }
//
//                    @Override
//                    public void error(Return json) {
//
//                    }
//                });
//            }
//        }
//
//        @Override
//        public void onFail(Object data, int errCode, String msg) {
//            Tracer.e(Constants.LogTag,
//                    "+++ register push fail. token:" + data
//                            + ", errCode:" + errCode + ",msg:"
//                            + msg);
//        }
//    };


    void initTabBox() {

        pageNames = new String[]{
                getResources().getString(R.string.tab_home),
                getResources().getString(R.string.tab_order),
                getResources().getString(R.string.tab_track),
                getResources().getString(R.string.tab_mine)

        };


        mJazzy = (JazzyViewPager) findViewById(R.id.jazzy_pager);
        initJazzyPager();

        tabHost = (TabHost) findViewById(android.R.id.tabhost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("0").setIndicator(createTab(pageNames[0], 0)).setContent(android.R.id.tabcontent));
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator(createTab(pageNames[1], 1)).setContent(android.R.id.tabcontent));
        tabHost.addTab(tabHost.newTabSpec("2").setIndicator(createTab(pageNames[2], 2)).setContent(android.R.id.tabcontent));
        tabHost.addTab(tabHost.newTabSpec("3").setIndicator(createTab(pageNames[3], 3)).setContent(android.R.id.tabcontent));
        // 点击tabHost 来切换不同的消息
        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                int index = Integer.parseInt(tabId);


                setTabSelectedState(index, 4);
                tabHost.getTabContentView().setVisibility(View.GONE);// 隐藏content
            }
        });
        tabHost.setCurrentTab(0);
    }

    private void initJazzyPager() {
        mJazzy.setTransitionEffect(JazzyViewPager.TransitionEffect.Standard);
        mJazzy.setAdapter(new TabFragmentAdapter(getSupportFragmentManager()));
        mJazzy.setFadeEnabled(true); //设置是否有谈出谈出效果，还有其它的相关设置，可以到
        mJazzy.setSlideCallBack(new JazzyViewPager.SlideCallback() {
            @Override
            public void callBack(int position, float positionOffset) {
                Map<String, View> map = tabViews.get(position);
                ViewHelper.setAlpha(map.get("selected"), positionOffset);
                ViewHelper.setAlpha(map.get("normal"), 1 - positionOffset);
            }
        });
        mJazzy.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                //Note:使用JazzyViewPager切换页面时对应的TABMenu切换
                tabHost.setCurrentTab(position);
                topActionBar.setVisibility(View.VISIBLE);
                HashMap<String,String> map = new HashMap<>();
                String eventId = "";
                switch (position){
                    case 0:
                        eventId = "footer_fist";
                        break;
                    case 1:
                        eventId = "footer_second";
                        break;
                    case 2:
                        eventId = "footer_third";
                        break;
                    case 3:
                        eventId = "footer_fourth";
                        break;
                }
                MobclickAgent.onEvent(baseActivity, eventId);

                if (position==1){
                    SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isRefresh",true).commit();
                }else if (position == 2){
                    topActionBar.setVisibility(View.GONE);
                }
                topActionBar.setTitle(pageNames[position]);
                if (position!=0){
                    topActionBar.hideMenu();
                } else {
                    topActionBar.setMenu(R.mipmap.button_scancode, new TopActionBar.MenuClickListener() {
                        @Override
                        public void menuClick() {
//                Log.d(TAG, "showDelMenu");
                            Intent intent = new Intent(baseActivity,CaptureActivity.class);
                            startActivityForResult(intent,0);
                        }
                    });
                }
            }

            @Override
            public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
            }

            @Override
            public void onPageScrollStateChanged(int paramInt) {
            }
        });
    }

    /**
     * 设置tab状态选中
     *
     * @param index
     */
    @TargetApi(11)
    private void setTabSelectedState(int index, int tabCount) {
        //Tracer.d(TAG, "index=" + index + "  tabCount:" + tabCount);
        for (int i = 0; i < tabCount; i++) {
            if (i == index) {
                if (android.os.Build.VERSION.SDK_INT<11){
                    ViewHelper.setAlpha(tabViews.get(i).get("normal"),0f);
                    ViewHelper.setAlpha(tabViews.get(i).get("selected"),1f);
                }else {
                    tabViews.get(i).get("normal").setAlpha(0f);
                    tabViews.get(i).get("selected").setAlpha(1f);
                }

            } else {
                if (android.os.Build.VERSION.SDK_INT<11){
                    ViewHelper.setAlpha(tabViews.get(i).get("normal"),1f);
                    ViewHelper.setAlpha(tabViews.get(i).get("selected"),0f);
                }else {
                    tabViews.get(i).get("normal").setAlpha(1f);
                    tabViews.get(i).get("selected").setAlpha(0f);
                }
            }
        }
        mJazzy.setCurrentItem(index, false);// false表示 代码切换 pager 的时候不带scroll动画
    }


    /**
     * 动态创建 TabWidget 的Tab项,并设置normalLayout的alpha为1，selectedLayout的alpha为0[显示normal，隐藏selected]
     *
     * @param tabLabelText
     * @param tabIndex
     * @return
     */
    @TargetApi(11)
    private View createTab(String tabLabelText, int tabIndex) {
        View tabIndicator = LayoutInflater.from(this).inflate(R.layout.item_main_tab_menu, null);
        TextView normalTV = (TextView) tabIndicator.findViewById(R.id.normalTV); //未选中文本
        TextView selectedTV = (TextView) tabIndicator.findViewById(R.id.selectedTV);//已选中文本
        normalTV.setText(tabLabelText);
        selectedTV.setText(tabLabelText);
        ImageView normalImg = (ImageView) tabIndicator.findViewById(R.id.normalImg);//未选中图片
        ImageView selectedImg = (ImageView) tabIndicator.findViewById(R.id.selectedImage);//已选中图片
        switch (tabIndex) {
            case 0:
                normalImg.setImageResource(R.drawable.tab_icon_ky);
                selectedImg.setImageResource(R.drawable.tab_icon_ky_selected);
                break;
            case 1:
                SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isRefresh",true).commit();
                normalImg.setImageResource(R.drawable.tab_icon_dd);
                selectedImg.setImageResource(R.drawable.tab_icon_dd_selected);
                break;
            case 2:
                normalImg.setImageResource(R.drawable.tab_icon_gz);
                selectedImg.setImageResource(R.drawable.tab_icon_gz_selected);
                break;
            case 3:
                normalImg.setImageResource(R.drawable.tab_icon_w);
                selectedImg.setImageResource(R.drawable.tab_icon_w_selected);
                break;
        }
        View normalLayout = tabIndicator.findViewById(R.id.normalLayout);
        View selectedLayout = tabIndicator.findViewById(R.id.selectedLayout);
        if (android.os.Build.VERSION.SDK_INT<11){
            ViewHelper.setAlpha(normalLayout,1f);
            ViewHelper.setAlpha(normalLayout,0f);
        }else {
            normalLayout.setAlpha(1f);// 透明度显示
            selectedLayout.setAlpha(0f);// 透明的隐藏
        }
        Map<String, View> map = new HashMap<String, View>();
        map.put("normal", normalLayout);
        map.put("selected", selectedLayout);
        tabViews.add(map);
        return tabIndicator;
    }

    /**
     * 种适配器
     */
    class TabFragmentAdapter extends FragmentPagerAdapter {

        public TabFragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        Fragment[] pages = new Fragment[]{
                new HomeFragment(),
                new OrderTabFragment(),
                new TrackMapFragment(),
                new MineFragment()
        };


        @Override
        public Fragment getItem(int position) {
            return pages[position];
        }

        @Override
        public int getCount() {
            return pages.length;
        }

        /**
         * 实例化可能会出现的页面实例
         *
         * @param container
         * @param position
         * @return
         */
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            //Tracer.i(TAG,"instantiateItem  position:"+position+"   count:"+getCount());
            Object obj = super.instantiateItem(container, position);
            mJazzy.setObjectForPosition(obj, position); //动画效果
            return obj;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return object != null && ((Fragment) object).getView() == view;
        }

    }

    /**
     * 从服务器获取json解析并进行比对版本号
     */
    public class CheckVersionTask extends Thread {

        public void run() {
            Message msg = new Message();
            msg.what =GET_RESPONSE ;
            handler.sendMessage(msg);
        }
    }
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ToolApi.update(baseActivity, baseContext, new IJsonResult<UpdataInfo>() {
                @Override
                public void ok(UpdataInfo json) {
//                    Log.e(TAG, json.Version.VersionCode + "" + json.Version.VersionName + json.Version.Description);
                    versionEntity.VersionCode = json.Version.VersionCode;
                    versionEntity.VersionName = json.Version.VersionName;
                    versionEntity.Description = json.Version.Description;
                    versionEntity.Url = json.Version.Url;
                    info.Version = versionEntity;
                    compareversion();
                }

                @Override
                public void error(Return json) {
                }
            });
        }
    };
    // 比较版本号方法
    protected void compareversion() {
        //Note:数字提醒
//        BadgeView badge1 = new BadgeView(baseActivity, tabMenus, 3);
//        //badge.setBadgePosition(BadgeView.POSITION_CENTER);
//        badge1.setText("  ");
//        badge1.show();
//        badge1.hide();
        int versioncode = getVersionCode();
        if (versioncode<Integer.valueOf(versionEntity.VersionCode)) {

        //Note:数字提醒
//        BadgeView badge = new BadgeView(baseActivity, tabMenus, 3);
//        //badge.setBadgePosition(BadgeView.POSITION_CENTER);
////        badge.setText("1");
//             badge.show();
            showUpdataDialog();
//            Message msg = new Message();
//            msg.what = UPDATA_CLIENT;
//            handler.sendMessage(msg);
        } else {
//            Toast.makeText(MainActivity.this, "已是最新版本", Toast.LENGTH_SHORT).show();
//            LoginMain();
        }

    }
    AlertDialog dlg;
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
     *
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
//            AlertDialog.Builder builer = new AlertDialog.Builder(this);
//            builer.setTitle("发现新版本");
//            builer.setMessage(versionEntity.Description);
//            // 当点确定按钮时从服务器上下载 新的apk 然后安装
//            builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
//                    if (versionEntity.Url.contains("http")) {
//                        downLoadApk();
//                    }else {
//                        toastFast("下载新版本失败");
//                    }
//                }
//            });
//            // 当点取消按钮时进行登录
//            builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
//                public void onClick(DialogInterface dialog, int which) {
////                    LoginMain();
//                    SharedPreferences preferences = baseActivity.getSharedPreferences("UpdateTip", baseActivity.MODE_PRIVATE);
////        boolean isShowUpdateTip = preferences.getBoolean("isShowTip",false);
//                    SharedPreferences.Editor editor = preferences.edit();
//                    editor.putBoolean("isShowTip", true).commit();
//                }
//            });
//            AlertDialog dialog = builer.create();
//            dialog.show();
//            dialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 从服务器中下载APK
     */
    protected void downLoadApk() {
        final ProgressDialog pd; // 进度条对话框
        pd = new ProgressDialog(this);
        pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
        pd.setMessage("正在下载更新");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        new Thread() {
            @Override
            public void run() {
                try {
                    //创建一个文件夹来放安装包
                    File fileRjban=new File(Environment.getExternalStorageDirectory()+getResources().getString(R.string.storePath));
                    if(!fileRjban.exists()){
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
     * @return
     */
    public String getVersion(){
        PackageManager pm = getPackageManager();   //取得包管理器的对象，这样就可以拿到应用程序的管理对象
        try {
            PackageInfo info = pm.getPackageInfo(getPackageName(), 0);   //得到应用程序的包信息对象
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
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = null;
        try {
            packInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return packInfo.versionCode;
    }
    Shipper shipper;
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);    //To change body of overridden methods use File | Settings | File Templates.
        if (resultCode==RESULT_OK){
            Bundle bundle = data.getExtras();
            String scanRes = (String) bundle.get("result");
            //ErrorCode:　80036　请下载小象快运（货主端）进行扫码
            if (scanRes.contains("　")) {
                final String ScanCode = scanRes.substring(scanRes.indexOf("　") + 1, scanRes.lastIndexOf("　"));
                if (ScanCode.length() > 0) {
                    HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
                    BackcardApi.bindStatus(baseActivity, baseActivity, pms, new IJsonResult<BindStatusInfo>() {
                        @Override
                        public void ok(BindStatusInfo json) {
                            if (Guard.isNull(json.PnrInfo)){
                                shipper.isFirstCg = true;
                            }else {
                                shipper.isFirstCg = false;
                            }
                            dbCache.save(shipper);
                            if (!Guard.isNull(json.PnrInfo)) {

                                if (!(json.PnrCards.size()>0)) {
                                    Intent intent = new Intent(baseActivity, UserPayToPlatformFragmentActivity.class);
                                    intent.putExtra("action", "scan");
                                    intent.putExtra("mode","pay");
                                    intent.putExtra("payeeid", ScanCode);
                                    startActivity(intent);
                                } else {
                                    bankinfo = json;
                                    dbCache.save(bankinfo);
                                    if (!shipper.havedPayMethod&&shipper.cardid<0 && bankinfo.PnrCards.size()>0){
                                        String number = "",cardNum = bankinfo.PnrCards.get(0).CardNo;
                                        if (cardNum.length()>4) {
                                            number =cardNum.substring(cardNum.length()-4,cardNum.length());
                                        }
                                        String bankName = "";
                                        for (int i = 0; i < BankInfoService.getBankData().size(); i++) {
                                            if (BankInfoService.getBankData().get(i).getBankcode().equals(json.PnrCards.get(0).BankCode)) {
                                                bankName = BankInfoService.getBankData().get(i).getBankName();
                                            }
                                        }
                                        String payment = "使用\t\t"+bankName + "("+number+")\t\t"+"付款";
                                        shipper.payment = payment;
                                        shipper.cardid = bankinfo.PnrCards.get(0).Id;
                                        dbCache.save(shipper);
                                    }
                                    Intent intent = null;
                                    if (bankinfo.PnrCards.size()>0){
                                        intent = new Intent(baseActivity, UserPayToDriverActivity.class);
                                    }else {
                                        intent = new Intent(baseActivity, UserPayToPlatformFragmentActivity.class);
                                    }
                                    if (!Guard.isNull(intent)) {
                                        intent.putExtra("action", "scan");
                                        intent.putExtra("payeeid", ScanCode);
                                        startActivity(intent);
                                    }
                                }
                            } else {
                                Intent intent = new Intent(baseActivity, UserPayToPlatformFragmentActivity.class);
                                intent.putExtra("action", "scan");
                                intent.putExtra("mode","pay");
                                intent.putExtra("payeeid", ScanCode);
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void error(Return json) {
                        }
                    });
//                    Intent intent = new Intent(baseActivity, UserPayToBankActivity.class);
//                    intent.putExtra("action","scan");
//                    intent.putExtra("payeeid",ScanCode);
//                    startActivity(intent);
                }
            }else {
                toastFast(scanRes);
            }
//            resultTextView.setText(scanRes);
        }
    }

    long oldTime=0;
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        long currentTime=System.currentTimeMillis();

        if (keyCode == KeyEvent.KEYCODE_BACK && currentTime-oldTime>3*1000 ) {
            toastFast("再按一次退出应用");
            oldTime=System.currentTimeMillis();
            return true;
        }else if (keyCode == KeyEvent.KEYCODE_BACK && currentTime-oldTime<3*1000) {
            oldTime=0;
//            webview.goBack();
            finish();
            App.getInstance().exit();
            return true;
        }

        return false;
//        return super.onKeyDown(keyCode, event);
    }

}
