package org.csware.ee.shipper.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.readystatesoftware.systembartint.SystemBarTintManager;
import com.umeng.analytics.MobclickAgent;

import net.tsz.afinal.FinalBitmap;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.FlashInfo;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.UserVerifyType;
import org.csware.ee.shipper.AuthenticationActivity;
import org.csware.ee.shipper.DeliverCollectionExtraFragmentActivity;
import org.csware.ee.shipper.DeliverCollectionFragmentActivity;
import org.csware.ee.shipper.DeliverFragmentActivity;
import org.csware.ee.shipper.FreightFragmentActivity;
import org.csware.ee.shipper.LoadingActivity;
import org.csware.ee.shipper.MailListActivity;
import org.csware.ee.shipper.MainTabFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserAuthActivity;
import org.csware.ee.shipper.UserAuthComPanyActivity;
import org.csware.ee.shipper.UserFriendFragmentActivity;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.shipper.WebViewActivity;
import org.csware.ee.shipper.WebViewOilActivity;
import org.csware.ee.shipper.adapter.ImagePagerAdapter;
import org.csware.ee.utils.Tools;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cn.trinea.android.common.autoscrollviewpager.AutoScrollViewPager;

/**
 * Created by Yu on 2015/5/29.
 * 主TAB - 首页
 */
public class HomeFragment extends FragmentBase {

    private SystemBarTintManager mTintManager;
    final static String TAG = "HomeFragment";

    @Override
    protected int getLayoutId() {
        return R.layout.home_fragment;
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        rootView = inflater.inflate(R.layout.fragment_home, container, false);
//        FinalActivity.initInjectedView(this, rootView);
//        init();
//        return rootView;
//    }

    //Note:注入的有问题，无法在移回来后重新获得控件ID
//    @ViewInject(id=R.id.ad_pictures)
//    AutoScrollViewPager scrollAds;

//    public void onDestroyView() {
//        // TODO Auto-generated method stub
//        super.onDestroyView();
//
//        //内嵌的Fragment UserProfileFragment
//        HomeFragment fragment = (HomeFragment) getFragmentManager().findFragmentById(R.layout.fragment_home);
//        if (fragment != null) {
//            getFragmentManager().beginTransaction().remove(fragment).commit();
//        }
//    }


    void changeStatusBgColor() {
        mTintManager.setTintColor(getResources().getColor(org.csware.ee.R.color.bg_darkblue));
    }

    // 标志位，标志已经初始化完成。
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

        if (!getIsPrepared() || !isVisible) {
            return;
        }
        //填充各控件的数据
        Tracer.e(TAG, "延迟加载开始");
//        Log.e(TAG, "Screen:" + Tools.getScreenWidth(getActivity())+"  "+Tools.getScreenHeight(getActivity()));
    }

    @Override
    public void onResume() {
        super.onResume();
        if (scrollAds!=null) {
            scrollAds.startAutoScroll();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (scrollAds!=null) {
            scrollAds.stopAutoScroll();
        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }

    private FinalBitmap fb;
    AutoScrollViewPager scrollAds;
    void setViewPagerAdapter(List<FlashInfo.SlidersEntity> list){
        //广告轮播图
        List<FlashInfo.SlidersEntity> imageIdList = new ArrayList<>();
//        imageIdList.add(R.drawable.banner1);
//        imageIdList.add(R.drawable.banner2);
//        imageIdList.add(R.drawable.banner3);//TODO:改为从网络获取
        FlashInfo.SlidersEntity slidersEntity = new FlashInfo.SlidersEntity();
        slidersEntity.setId(R.drawable.banner1);
        scrollAds = (AutoScrollViewPager) rootView.findViewById(R.id.ad_pictures);
        ViewGroup.LayoutParams v =scrollAds.getLayoutParams();
        v.height = screenWidth / 2 ;
        scrollAds.setLayoutParams(v);
        if (list.size()>0) {
            scrollAds.setAdapter((new ImagePagerAdapter(baseActivity, list,false).setInfiniteLoop(true)));
        }else {
            scrollAds.setAdapter((new ImagePagerAdapter(baseActivity, imageIdList,false).setInfiniteLoop(true)));
        }
//        scrollAds.setAdapter((new ImagePagerAdapter(baseActivity, imageIdList).setInfiniteLoop(true)));
        scrollAds.setInterval(3000); //设置自动滚动的间隔时间，单位为毫秒
        //scrollAds.setDirection(int); // 设置自动滚动的方向，默认向右
        //scrollAds.setCycle(boolean); // 是否自动循环轮播，默认为true
        //scrollAds.setScrollDurationFactor(double); // 设置ViewPager滑动动画间隔时间的倍率，达到减慢动画或改变动画速度的效果
        //scrollAds.setStopScrollWhenTouch(boolean); // 当手指碰到ViewPager时是否停止自动滚动，默认为true
        //scrollAds.setSlideBorderMode(int); // 滑动到第一个或最后一个Item的处理方式，支持没有任何操作、轮播以及传递到父View三种模式
        //scrollAds.setBorderAnimation(boolean); // 设置循环滚动时滑动到从边缘滚动到下一个是否需要动画，默认为true
        scrollAds.startAutoScroll(); //启动自动滚动    stopAutoScroll() 停止自动滚动
    }
    ProgressDialog dialog;
    MeReturn userInfo;
    DbCache dbCache;
    int screenWidth;
    @Override
    public void init() {
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
        SharedPreferences preferences = baseActivity.getSharedPreferences("Screen", Context.MODE_PRIVATE);
        screenWidth = preferences.getInt("ScreenWidth",0);
        mTintManager = new SystemBarTintManager(getActivity());
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        OrderApi.getFlash(baseFragment.getActivity(), baseActivity, new IJsonResult<FlashInfo>() {
            @Override
            public void ok(FlashInfo json) {
                List<FlashInfo.SlidersEntity> list = json.getSliders();
//                List<Object> imageStringList = new ArrayList<>();
//                for (int i=0;i<list.size();i++) {
//                    imageStringList.add(list.get(i).getSource());
//                }
                setViewPagerAdapter(list);
            }

            @Override
            public void error(Return json) {

            }
        });
        changeStatusBgColor();

        fb = FinalBitmap.create(baseActivity);//初始化FinalBitmap模块
        fb.configLoadingImage(R.drawable.w_icon_sz);


//        //设置提示数字签
//        BadgeView badge = new BadgeView(baseActivity, rootView.findViewById(R.id.ivFriend));
//        //badge.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
//        badge.setText("1");
//        badge.show();



        //发货按钮
        final ImageButton btnDeliver = (ImageButton) rootView.findViewById(R.id.btnDeliver);
        if (btnDeliver != null) btnDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo(false,1);
            }
        });


        final ImageButton btnCollectionDeliver = (ImageButton) rootView.findViewById(R.id.btnHackman);
        if (btnCollectionDeliver != null) btnCollectionDeliver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo(false,2);
            }
        });
//        if(!Tracer.isRelease()){
//            //Note:数字提醒
//            BadgeView home = new BadgeView(baseActivity, rootView.findViewById(R.id.btnDeliver));
//            home.setText(Tracer.getDebugText());
//            home.show();
//        }


//        //消息按鈕
//        final LinearLayout btnMessage = (LinearLayout) rootView.findViewById(R.id.categoryMessage);
//        if (btnMessage != null) btnMessage.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Tracer.d(TAG, "消息按鈕");
//                baseActivity.startActivity(new Intent(baseActivity, DemoActivity.class));
//                //context.startActivity(new Intent(context, DeliverActivity.class));
//            }
//        });

//        //禮包
//        final LinearLayout btnPrize = (LinearLayout) rootView.findViewById(R.id.categoryPrize);
//        if (btnPrize != null) btnPrize.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Tracer.d(TAG, "礼包");
//                //context.startActivity(new Intent(context, DeliverActivity.class));
//
//                startActivity(new Intent(baseActivity, CropImageFragmentActivity.class));
//            }
//        });

//        //路况
//        final LinearLayout btnRoadCondition = (LinearLayout) rootView.findViewById(R.id.categoryRoadCondition);
//        if (btnRoadCondition != null)
//            btnRoadCondition.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Tracer.d(TAG, "路况");
//                    //context.startActivity(new Intent(context, DeliverActivity.class));
//
//
//                    startActivity(new Intent(baseActivity, Demo2Activity.class));
//                }
//            });

        //联系人
        final LinearLayout btnFriend = (LinearLayout) rootView.findViewById(R.id.btnFriend);

        if (btnFriend != null) btnFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getUserInfo(false, 3);
                //startActivity(new Intent(baseActivity, BdMapActivity.class)); //开启调试
                //context.startActivity(new Intent(context, DeliverActivity.class));

//                List<ICnEnum.CnEnum> array2 = EnumHelper.getList(PayPoint.class);
//                for (ICnEnum.CnEnum item : array2) {
//                    Tracer.d(TAG, item.toString());
//                }

                //TODO:测试
            }
        });

        //运价计算
        final LinearLayout btnCalculate = (LinearLayout) rootView.findViewById(R.id.btnCalculate);
        if (btnCalculate != null) btnCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Tracer.d(TAG, "油价");

                String id = "";
                if (!Guard.isNull(userInfo)){
                    if (!Guard.isNull(userInfo.OwnerUser)){
                        if (!Guard.isNull(userInfo.OwnerUser.Id)) {
                            id = userInfo.OwnerUser.Id + "";
                        }
                    }
                }
                if (!Guard.isNullOrEmpty(id)) {
                    intentOil(id);
//                intent.putExtra("url", "http://192.168.1.82:8520/controllers/oil/index.html?userId=2&typeId=2");
//                intent.putExtra("url", "http://m.sui.taobao.org/demos/");

                }else {
                    getUserInfo(true,0);
                }
            }
        });

        //我的钱包
        final LinearLayout btnWallet = (LinearLayout) rootView.findViewById(R.id.btnWallet);
        if (btnWallet != null) btnWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//               getUserInfo(false, 4);
                Tracer.d(TAG, "我的钱包");
                HashMap<String,String> map = new HashMap<>();
                MobclickAgent.onEvent(baseActivity, "wallet_manage");
                baseActivity.startActivity(new Intent(baseActivity, UserWalletFragmentActivity.class));
            }
        });

    }

    void getUserInfo(final boolean getUserId,final int what){
        if (Tools.getNetWork(baseActivity)) {
            dialog = Tools.getDialog(baseActivity);
            dialog.setCanceledOnTouchOutside(false);
            Activity activity = MainTabFragmentActivity.instance;
            UserApi.info(activity, baseActivity, new IJsonResult<MeReturn>() {
                @Override
                public void ok(MeReturn json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    userInfo = json;
                    if (!getUserId) {
                        if (Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                            userInfo.OwnerUser.CompanyStatus = -1;
                        }
                        dbCache.save(userInfo);
                        int Status;
                        if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                                Status = userInfo.OwnerUser.Status;
                            } else {
                                Status = userInfo.OwnerUser.CompanyStatus;
                            }
                        } else {
                            Status = userInfo.OwnerUser.Status;
                        }
                        if (Status == UserVerifyType.VERIFIED.toValue()) {
//                            if (DeliverCollectionFragmentActivity.activity!=null) {
//                                if (DeliverCollectionFragmentActivity.activity.isFinishing()) {
                            if (what==2) {
                                HashMap<String,String> map = new HashMap<>();
                                MobclickAgent.onEvent(baseActivity, "payment_manage");
                                Intent intent = new Intent(baseActivity, DeliverCollectionExtraFragmentActivity.class);
//                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.app_name));
                                baseActivity.startActivity(intent);
                            }else if (what==1){
                                HashMap<String,String> map = new HashMap<>();
                                MobclickAgent.onEvent(baseActivity, "ship_manage");
                                Intent intent = new Intent(baseActivity, DeliverFragmentActivity.class);
//                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.app_name));
                                baseActivity.startActivity(intent);
                            }else if (what ==3){
                                Tracer.d(TAG, "联系人");
                                Intent intent = new Intent(baseActivity, MailListActivity.class);
                                startActivity(intent);

                            }
//                            else if (what == 4){
//                                Tracer.d(TAG, "我的钱包");
//                                HashMap<String,String> map = new HashMap<>();
//                                MobclickAgent.onEvent(baseActivity, "wallet_manage");
//                                baseActivity.startActivity(new Intent(baseActivity, UserWalletFragmentActivity.class));
//                            }
//                                }
//                            }else {
//                                toastFast("网络不给力  请稍候重试");
//                            }
                        }else {
                            intentAction(Status);
                        }
                    }else {
                        String UserId = "";
                        if (!Guard.isNull(userInfo)){
                            if (!Guard.isNull(userInfo.OwnerUser)){
                                if (!Guard.isNull(userInfo.OwnerUser.Id)) {
                                    UserId = userInfo.OwnerUser.Id + "";
                                }
                            }
                        }
                        if (Guard.isNullOrEmpty(UserId)){
                            toastFast("获取用户信息失败");
//                            MainTabFragmentActivity.instance.finish();
                        }else {
                            intentOil(UserId);
                        }
                    }
//                QCloudService.asyncDisplayImage(baseActivity, userInfo.OwnerUser.Avatar, ivHeadPic);
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
        }else {
            toastFast(org.csware.ee.R.string.tip_need_net);
        }
    }

    void intentOil(String Userid){
        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "oil_enter");
        Intent intent = new Intent(baseActivity, WebViewOilActivity.class);
//                Intent intent = new Intent(baseActivity, FreightFragmentActivity.class);
        if (isAdded()) {
            intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.app_name));
        }
//        String url = "http://192.168.1.82:88/oil.html";
        String subUrl = "oil.xx3700.com/Controllers/Oil/index.html?userId=" + Userid + "&typeId=1";
        String url = API.isInner()?"http://test"+ subUrl:"http://"+subUrl;
        Tracer.e(TAG," userId:"+Userid+" \n"+url);
        intent.putExtra("url", url);
        baseActivity.startActivity(intent);
    }

    void intentAction(int status){
        if (userInfo.Owner == null || status == UserVerifyType.NOT_VERIFIED.toValue()) {
            Intent intent;

            if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                    intent = new Intent(baseActivity, UserAuthActivity.class); //TODO:modfiy to  the croect activity
                } else {
                    intent = new Intent(baseActivity, UserAuthComPanyActivity.class); //TODO:modfiy to  the croect activity
                }
            }else {
                intent = new Intent(baseActivity, UserAuthActivity.class); //TODO:modfiy to  the croect activity
            }
            startActivity(intent);
        } else if (status == UserVerifyType.VERIFYING.toValue() || userInfo.OwnerUser.Status == UserVerifyType.VERIFIEDFAIL.toValue()) {
            Intent intent = new Intent(baseActivity, AuthenticationActivity.class); //TODO:modfiy to  the croect activity
            intent.putExtra("Status", status);//userInfo.OwnerUser.Status
            intent.putExtra("Name", userInfo.Owner.Name);
            startActivity(intent);
        }
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
