package org.csware.ee.shipper.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.TabHost;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.OrderListType;
import org.csware.ee.model.Return;
import org.csware.ee.model.TabCountModel;
import org.csware.ee.shipper.R;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Yu on 2015/5/29.
 *
 */
public class OrderTabFragment extends FragmentBase {

    final static String TAG = "OrderTabFragment";
    @Override
    protected int getLayoutId(){
        return R.layout.order_tab_fragment;
    }

    String[] tabNames;

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
//        Log.e(TAG, "延迟加载开始");

    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isRefresh", false);
//        Log.w(TAG, "onResume" +isRefresh);
        if (isRefresh) {
//            asyncLoadingData();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
        Log.e(TAG, "加载开始  OnResume");
    }

    @Override
    public void init() {
        asyncLoadingData();
//        tabNames = new String[]{
//                getString(R.string.tab_lab_quoting),
//                getString(R.string.tab_lab_shipping),
//                getString(R.string.tab_lab_finished)
//        };
//
//        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabOrder);
//        tabHost.setup();
//        tabHost.addTab(tabHost.newTabSpec("0").setIndicator(tabNames[0]).setContent(R.id.orderQuoting));
//        tabHost.addTab(tabHost.newTabSpec("1").setIndicator(tabNames[1]).setContent(R.id.orderShipping));
//        tabHost.addTab(tabHost.newTabSpec("2").setIndicator(tabNames[2]).setContent(R.id.orderFinished));
//        onTabChanged tabChanged = new onTabChanged();
//        tabHost.setOnTabChangedListener(tabChanged);
//        //tabHost.performClick();
//        tabHost.setCurrentTab(0); //init loaded this is not trigger the onTabChanged .must code it.
//        tabChanged.onTabChanged("0");


    }

    class onTabChanged implements TabHost.OnTabChangeListener{

        @Override
        public void onTabChanged(String tabId) {
            //Log.e("onTabChanged", "tabId:" + tabId);
            Fragment fragment =null;
            String eventId = "";
            switch (tabId)
            {
                case "0":
                    fragment = new NewOrderQuotingFragment();
                    eventId = "myorder_talking";
                    break;
                case "1":
                    fragment = new NewOrderShippingFragment();
                    eventId = "myorder_shipping";
                    break;
                case "2":
                    fragment = new NewOrderFinishedFragment();
                    eventId = "myorder_completed";
                    break;
            }
            HashMap<String,String> map = new HashMap<>();
            MobclickAgent.onEvent(baseActivity, eventId, map);
            getFragmentManager().beginTransaction().replace(R.id.orderBody, fragment).commitAllowingStateLoss();
        }
    }
    TabHost tabHost;
    void asyncLoadingData() {
        HttpParams pms = new HttpParams(API.ORDER.LIST);
        pms.addParam("type", "new");
        pms.addParam("pagesize", "1");
        pms.addParam("page", "1");
        OrderApi.getCount(baseFragment.getActivity(), baseActivity, pms, new IJsonResult<TabCountModel>() {
            @Override
            public void ok(TabCountModel json) {
                //成功
                int news = 0, shipping = 0, done = 0;
                if (!Guard.isNull(json)) {
                    if (!Guard.isNull(json.TypeCount)){
                        if (!Guard.isNull(json.TypeCount.newX)){
                            news = json.TypeCount.newX;
                        }
                        if (!Guard.isNull(json.TypeCount.shipping)){
                            shipping = json.TypeCount.shipping;
                        }
                        if (!Guard.isNull(json.TypeCount.done)){
                            done = json.TypeCount.done;
                        }
                    }
                }
                String labQuoting = isAdded() ? getString(R.string.tab_lab_quoting) : "报价中";
                String labShipping = isAdded() ? getString(R.string.tab_lab_shipping) : "运送中";
                String labFinished = isAdded() ? getString(R.string.tab_lab_finished) : "已完成";
                if (news>0){
                    labQuoting +="("+news+")";
                }
                if (shipping>0){
                    labShipping +="("+shipping+")";
                }
                if (done>0){
                    labFinished +="("+done+")";

                }
                tabNames = new String[]{
                        labQuoting,
                        labShipping,
                        labFinished
                };
                Tracer.e(TAG,news+" "+labQuoting+" shipping:"+shipping+" "+labShipping+" finish:"+done+" "+labFinished);
                tabHost = (TabHost) rootView.findViewById(R.id.tabOrder);
                if (!Guard.isNull(tabHost)) {
                    tabHost.setCurrentTab(0);
//                    if (tabHost.getTabWidget().)
//                    tabHost.clearAllTabs();
                }
                tabHost.setup();
                tabHost.addTab(tabHost.newTabSpec("0").setIndicator(tabNames[0]).setContent(R.id.orderQuoting));
                tabHost.addTab(tabHost.newTabSpec("1").setIndicator(tabNames[1]).setContent(R.id.orderShipping));
                tabHost.addTab(tabHost.newTabSpec("2").setIndicator(tabNames[2]).setContent(R.id.orderFinished));
                onTabChanged tabChanged = new onTabChanged();
                tabHost.setOnTabChangedListener(tabChanged);
                //tabHost.performClick();
                tabHost.setCurrentTab(0); //init loaded this is not trigger the onTabChanged .must code it.
                tabChanged.onTabChanged("0");
            }

            @Override
            public void error(Return json) {
            }
        });

    }



}
