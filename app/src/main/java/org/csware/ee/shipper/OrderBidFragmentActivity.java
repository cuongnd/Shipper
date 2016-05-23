package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.OnClickListener;

import org.csware.ee.Guard;
import org.csware.ee.api.BidApi;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.RegistModel;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.PayMethodType;
import org.csware.ee.model.Return;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ScrollViewForListView;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by Yu on 2015/6/3.
 * 订单 出价列表
 */
public class OrderBidFragmentActivity extends FragmentActivityBase {

    final static String TAG = "OrderBidAct";
    TopActionBar topBar;
    public static Activity instance;
    private static Handler sHandler;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        setContentView(R.layout.order_bid_fragment_activity);
        ViewUtils.inject(this); //注入view和事件

        sHandler = new Handler();

        sHandler.post(mHideRunnable); // hide the navigation bar
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sHandler.post(mHideRunnable); // hide the navigation bar
            }
        });

        txt_collection_numbs = (TextView) findViewById(R.id.txt_collection_numbs);
        txt_collection_numbs_pay = (TextView) findViewById(R.id.txt_collection_numbs_pay);
        topBar = (TopActionBar) findViewById(R.id.topBar);
        topBar.setMenu(R.drawable.dd_icon_delete, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
//                Log.d(TAG, "setMenuClickListener");
                baseActivity.setTheme(R.style.ActionSheetStyle);
                ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("废除当前订单")
                        .setCancelableOnTouchOutside(true)
                        .setListener(actionSheetListener).show();
            }
        });
        init();
    }

    Runnable mHideRunnable = new Runnable() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void run() {
            int flags;
            int curApiVersion = android.os.Build.VERSION.SDK_INT;
            // This work only for android 4.4+
            if(curApiVersion >= Build.VERSION_CODES.KITKAT){
                // This work only for android 4.4+
                // hide navigation bar permanently in android activity
                // touch the screen, the navigation bar will not show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            }else{
                // touch the screen, the navigation bar will show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            // must be executed in main thread :)
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    };

    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//            Log.d(TAG, "click item index = " + index);
            if (index == 0) {
                if (_result.Order.Id<=0){
                    return;
                }
                //废除订单
                OrderApi.delorder(baseActivity, baseActivity,_result.Order.Id,new IJsonResult<RegistModel>() {
                    @Override
                    public void ok(RegistModel json) {
                        //成功
                        if (json.Result==0) {
                            toastFast("废除成功！");
                            SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isRefresh",true).commit();
                            finish();
                        }else {
                            toastFast("废除失败！");
                        }
                    }

                    @Override
                    public void error(Return json) {
                        toastFast("废除失败！");
                    }
                });
            }

        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
//            Log.d(TAG, "dismissed isCancle = " + isCancle);
        }
    };


    /**
     * 给ListView添加下拉刷新
     */
//    private SwipeRefreshLayout swipeLayout;
    private ScrollViewForListView listView;
    //    private PullToRefreshListView listView;
    private CommonAdapter<OrderDetailChangeReturn.BidsEntity> adapter;
    private org.csware.ee.shipper.adapter.CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity> PayeeAdapter;
    private List<OrderDetailChangeReturn.BidsEntity> bidList;
    private List<OrderDetailChangeReturn.OrderEntity.PayeesEntity> payeeList;
    LinearLayout Lin_BidAdressList, Lin_BidAdress, Lin_PayeeMineAddress, Lin_collectionPerson;
    ScrollViewForListView listviewBidAddress;
    TextView txt_collection_numbs, txt_collection_numbs_pay;
    OrderDetailChangeReturn _result;
    ChinaAreaHelper areaHelper;
    long orderId;
    int payMethod;
    DbCache _dbCache;

    void init() {
        _dbCache = new DbCache(baseActivity);
        Lin_BidAdressList = (LinearLayout) findViewById(R.id.Lin_BidAdressList);
        Lin_PayeeMineAddress = (LinearLayout) findViewById(R.id.Lin_PayeeMineAddress);
        Lin_collectionPerson = (LinearLayout) findViewById(R.id.Lin_collectionPerson);
        Lin_BidAdress = (LinearLayout) findViewById(R.id.Lin_BidAdress);
        listviewBidAddress = (ScrollViewForListView) findViewById(R.id.listviewBidAddress);


        areaHelper = new ChinaAreaHelper(baseActivity);
        Intent intent = this.getIntent();
        final String json = intent.getStringExtra(ParamKey.ORDER_DETAIL);
//        Log.e(TAG, json);
        _result = GsonHelper.fromJson(json, OrderDetailChangeReturn.class);
        if (_result == null) {
            finish(); //直接关掉
            return;
        }
        if (_result.Order == null) _result.Order = (new OrderDetailChangeReturn.OrderEntity());
        fillData(_result);

        //代收货款收货地址集
        payeeList = _result.Order.Payees;
        //收款人点击事件
        Lin_collectionPerson.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payeeList.size() > 0) {
                    Intent intent = new Intent(OrderBidFragmentActivity.this, OrderCollectionActivity.class);
                    intent.putExtra("json", json);
                    startActivity(intent);
                }
            }
        });

        //V4 刷新列表功能
        bidList = _result.Bids;
//        swipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefresh);
//        swipeLayout.setOnRefreshListener(refreshListener);
//        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright); //刷新的样式
        listView = (ScrollViewForListView) findViewById(R.id.listview);
        adapter = new CommonAdapter<OrderDetailChangeReturn.BidsEntity>(baseActivity, bidList, R.layout.order_detail_bid_list_item_new) {

            /**重写，返回了序列元素的标识Id
             * 返回0时表示没有主键Id
             * */
            @Override
            public long getItemId(int position) {
                OrderDetailChangeReturn.BidsEntity item = bidList.get(position);
                //Log.e(TAG,"itemId="+item.Id);
                if (item != null)
                    return item.Id;
                return 0;
            }

            // 出价列表数据填充
            @Override
            public void convert(ViewHolder helper, OrderDetailChangeReturn.BidsEntity item) {
                OrderDetailChangeReturn.OrderEntity.BearerEntity bear = item.Bearer;
                final OrderDetailChangeReturn.OrderEntity.BearerUserEntity order = item.BearerUser;
                if (bear == null) bear = new OrderDetailChangeReturn.OrderEntity.BearerEntity();
//                Log.e(TAG + bidList.size(), item.Price + " "+bear.Rate+" "+order.Id);
                helper.setText(R.id.labUnitPrice, item.Price + "" + item.PriceType); // //TODO:这个出价是总价还是单价？？？
                String Name = "";
                if (!Guard.isNull(item.BearerCompany)){
                    if (!Guard.isNullOrEmpty(item.BearerCompany.CompanyName)&& item.BearerUser.CompanyStatus==1){
                        Name = item.BearerCompany.CompanyName;
                        helper.getView(R.id.labPlate).setVisibility(View.GONE);
                    }else {
                        Name = bear.Name;
                        helper.getView(R.id.labPlate).setVisibility(View.VISIBLE);
                    }
                }else {
                    Name = bear.Name;
                    helper.getView(R.id.labPlate).setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.labName, Name + "");
                helper.setText(R.id.labPlate, bear.Plate + "");
//                helper.setText(R.id.labLevel, bear.Score + "");
                if (order != null) {
                    if (!Guard.isNullOrEmpty(order.Avatar)) {
                        QCloudService.asyncDisplayImage(baseContext, order.Avatar, (ImageView) helper.getView(R.id.ivHeadPic));
                    } else {
                        helper.setImageResource(R.id.ivHeadPic, R.drawable.w_icon_tjzp);
                    }
                    RatingBar ratingBar = helper.getView(R.id.rate_rating);
                    double star = Float.parseFloat(bear.Rate+"");
                    ratingBar.setRating(Float.parseFloat(star+""));
//                    helper.getView(R.id.ivPhoneStatus).setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View v) {
//                            if (!Guard.isNullOrEmpty(order.Mobile)) {
//                                Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + order.Mobile));
//                                startActivity(nIntent);
//                            }
//                        }
//                    });
                }else {
                    helper.setImageResource(R.id.ivHeadPic, R.drawable.w_icon_tjzp);
                }
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);
        if (payeeList != null) {
            txt_collection_numbs.setText(payeeList.size() + "");
            int j = 0;
            for (int i = 0; i < payeeList.size(); i++) {
                if (payeeList.get(i).Status != 1) {
                    j++;
                }
            }
            txt_collection_numbs_pay.setText(j + "");
            PayeeAdapter = new org.csware.ee.shipper.adapter.CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity>(baseActivity, payeeList, R.layout.payee_address_item) {

                /**
                 * 重写，返回了序列元素的标识Id
                 * 返回0时表示没有主键Id
                 */
                @Override
                public long getItemId(int position) {
                    OrderDetailChangeReturn.OrderEntity.PayeesEntity item = payeeList.get(position);
                    //Log.e(TAG,"itemId="+item.Id);
                    if (item != null)
                        return item.Id;
                    return 0;
                }

                // 出价列表数据填充
                @Override
                public void convert(ViewHolder helper, final OrderDetailChangeReturn.OrderEntity.PayeesEntity item, int position) {
                    String[] names = areaHelper.getName(Integer.toString(item.Area));
                    String ToAdress = ParamTool.join(names, ",");
                    if (position != (payeeList.size() - 1)) {
                        AddPoint((ViewGroup) helper.getView(R.id.Lin_PayAddIcons), OrderBidFragmentActivity.this);
                    }
                    helper.setText(R.id.txtPayeeCollectionAddress, ToAdress + item.Address);
                    String Consignee = item.Name+"";
                    if (Guard.isNullOrEmpty(Consignee)){
                        Consignee = "未确认";
                    }
                    helper.setText(R.id.name_txt, Consignee + "");
                    helper.setText(R.id.phone_number, item.Mobile + "");
                    if (Guard.isNullOrEmpty(item.Mobile)){
                        helper.getView(R.id.img_call_up).setVisibility(View.GONE);
                    }else {
                        helper.getView(R.id.img_call_up).setVisibility(View.VISIBLE);
                        helper.getView(R.id.img_call_up).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Guard.isNullOrEmpty(item.Mobile)) {
                                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.Mobile));
                                    startActivity(nIntent);
                                }
                            }
                        });
                    }
                }
            };
            listviewBidAddress.setAdapter(PayeeAdapter);
            listviewBidAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    Intent intent = OrderBidFragmentActivity.this.getIntent();
                    String json = intent.getStringExtra(ParamKey.ORDER_DETAIL);
                    intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_lab_quoting));
                    intent.setClass(baseContext, OrderDetailFragmentActivity.class);
                    startActivity(intent);
                }
            });
        }
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            asyncRefreshOrderDetail();
        }
    };
    int bidId;
    double price;
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Log.e(TAG,"itemId = "+id+"  Position :"+position);
            final OrderDetailChangeReturn.OrderEntity order = _result.Order;
            final OrderDetailChangeReturn.BidsEntity bid = bidList.get(position);
            bidId = bid.Id;
            price = bid.Price;
//            final com.orhanobut.dialogplus.ViewHolder vh = new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_commission_hackman);
            final com.orhanobut.dialogplus.ViewHolder vh = new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_commission_hackman);
            final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                    .setContentHolder(vh)
                    .setCancelable(false) //点击对话框外部不关闭
                    .setGravity(DialogPlus.Gravity.CENTER)
                    .setOnClickListener(clickListener)
                    .setBackgroundColorResourceId(org.csware.ee.R.color.trans)
                    .create();

            //TODO：现在的实现比较麻烦要在上面定义，下面赋值。回头可改  getBearerUser 可能为NULL 后续处理
            OrderDetailChangeReturn.OrderEntity.BearerEntity bear = bid.Bearer;
            final OrderDetailChangeReturn.OrderEntity.BearerUserEntity user = bid.BearerUser;
            if (user == null) {
//                Log.e(TAG, true + "");
            } else {
//                Log.e(TAG, false + " " + user.Mobile);
            }
            if (bear == null) bear = new OrderDetailChangeReturn.OrderEntity.BearerEntity();
            String SFRZ = (bid.BearerUser.CompanyStatus == 1) ? "企业认证" :"个人认证";
            String PTRZ = (bid.BearerUser.CompanyStatus == 1) ? getString(R.string.lab_certification_hackman_company) :getString(R.string.lab_certification_hackman);
            vh.setText(R.id.labPTRZ, PTRZ + "");
            vh.setText(R.id.labSFRZ, SFRZ + "");
            String Name = "";
            if (!Guard.isNull(bid.BearerCompany)){
                if (!Guard.isNullOrEmpty(bid.BearerCompany.CompanyName)){
                    Name = bid.BearerCompany.CompanyName;
                    vh.getView(R.id.labCPH).setVisibility(View.GONE);
                    vh.getView(R.id.LinCarModel).setVisibility(View.GONE);
                    vh.setImageResource(R.id.ivPTRZ, R.mipmap.dd_icon_company);
                }
            }else {
                Name = bear.Name;
                vh.getView(R.id.labCPH).setVisibility(View.VISIBLE);
                vh.getView(R.id.LinCarModel).setVisibility(View.VISIBLE);
                vh.setImageResource(R.id.ivPTRZ, R.drawable.w_icon_ptrz);
            }

            vh.setText(R.id.labName, Name + "");
            vh.setText(R.id.labCPH, bear.Plate + "");
            vh.setText(R.id.ivLevel,bear.Rate+"星");
            if (user != null) {
                QCloudService.asyncDisplayImage(baseContext, user.Avatar, (ImageView) vh.getView(R.id.ivHeadPic));
                vh.setText(R.id.labPhoneNo, user.Mobile + "");
                vh.getView(R.id.ivPhone).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Guard.isNullOrEmpty(user.Mobile)) {
                            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + user.Mobile));
                            startActivity(nIntent);
                        }
                    }
                });
            }

            vh.setText(R.id.labSendAmount, bear.OrderAmount + "");
            vh.setText(R.id.labTruckType, bear.VehicleType);
            vh.setText(R.id.labTruckLength, bear.VehicleLength + "米");
//            Log.e(TAG,bear.VehicleLength+" "+EnumHelper.getCnName(bear.VehicleLength + "", TruckLengthType.class));
            //vh.setText(R.id.labUnitPrice, "-5");
            vh.setText(R.id.labBidPrice, FormatHelper.toMoney(bid.Price) + getString(R.string.unit_rmb));
            //TODO:零担时才显示表达式
            //getString(R.string.lab_price_style_truck).equals(order.PriceType)||getString(R.string.lab_price_style_rmb_truck).equals(order.PriceType)
            if (bid.PriceType.contains("包车")) {
                //包车价
                vh.setText(R.id.labTotalPrice, FormatHelper.toMoney(price) + "");
            } else {
                //零担计算价格
                if (order.GoodsAmount > 0) {
                    vh.setText(R.id.labTotalPrice, FormatHelper.toMoney(price * Double.valueOf(order.GoodsAmount)) + "");
                } else {
                    vh.setText(R.id.labTotalPrice, FormatHelper.toMoney(price) + "");
                }
            }
            dialog.show();
        }
    };
    OnClickListener clickListener = new OnClickListener() {
        @Override
        public void onClick(final DialogPlus dialog, final View view) {
            switch (view.getId()) {
                case R.id.btnConfirm:

                    BidApi.confirm(baseActivity, baseContext, bidId, price, new IJsonResult() {
                        @Override
                        public void ok(Return json) {
                            view.setEnabled(false);
//                            if (payMethod == PayMethodType.PLATFORM.toValue()) {
                                toastSlow(R.string.tip_delete_ok);
//                            } else {
                                toastSlow(R.string.tip_delete_ok);
//                            }
                            SharedPreferences preferences = getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isRefresh",true).commit();
                            OrderApi.detail(baseActivity, baseContext, orderId, new IJsonResult<OrderDetailChangeReturn>() {
                                @Override
                                public void ok(OrderDetailChangeReturn json) {
                                    //成功
                                    //TODO:待测试：结束当前窗口，打开新的窗口
                                    dialog.dismiss();
                                    finish();
                                    String orderDetail = "";
                                    if (!Guard.isNull(json)){
                                        //成功
                                        if (json.Result==0) {
                                            orderDetail = GsonHelper.toJson(json);
                                            Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
                                            intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
                                            intent.putExtra(ParamKey.ORDER_DETAIL, orderDetail);
                                            startActivity(intent);
                                        }else {
                                            toastFast(R.string.result_failed_net);
                                        }
                                    }else {
                                        toastFast(R.string.result_failed_net);
                                    }
//                                    Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
//                                    intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
//                                    intent.putExtra(ParamKey.ORDER_DETAIL, GsonHelper.toJson(json));
//                                    startActivity(intent);
                                }

                                @Override
                                public void error(Return json) {

                                }
                            });

//                            HttpParams pms = new HttpParams();
//                            //pms.addParam("device", AppStatus.getDeviceId());
//                            pms.addParam("id", orderId);
//                            String url = pms.getUrl(API.ORDER.DETAIL);
//                            Log.d(ParamKey.ORDER_DETAIL, url);
//                            final ProgressDialog dialog;
//                            dialog = createDialog(R.string.dialog_loading);
//                            FinalHttp fh = new FinalHttp();
//                            fh.get(url, new AjaxCallBack<String>() {
//
//                                @Override
//                                public void onSuccess(String res) {
//                                    //Log.e(ParamKey.ORDER_DETAIL + "-107", "" + res);
//                                    Return json = GsonHelper.fromJson(res, Return.class);
//                                    if (json.Result == 0) {
//                                        //成功
//                                        //TODO:待测试：结束当前窗口，打开新的窗口
//                                        dialog.dismiss();
//                                        finish();
//                                        Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
//                                        intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
//                                        intent.putExtra(ParamKey.ORDER_DETAIL, res);
//                                        startActivity(intent);
//                                    } else {
//                                        toastSlow(R.string.tip_inner_error);
//                                    }
//                                }
//
//                                @Override
//                                public void onFailure(Throwable t, int errCode, String strMsg) {
//                                    //加载失败的时候回调
//                                    Log.e("OrderQuotingFragment", "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
//                                    toastSlow(R.string.tip_server_error);
//                                }
//                            });
                        }

                        @Override
                        public void error(Return json) {

                        }
                    });

                    delayedDismiss(dialog, 1000);
                    break;
                case R.id.btnCancel:
                    dialog.dismiss();
                    break;
            }
        }
    };


    @ViewInject(R.id.labFromArea)
    TextView labFromArea;
    @ViewInject(R.id.labToArea)
    TextView labToArea;
    @ViewInject(R.id.labOrderId)
    TextView labOrderId;

    @ViewInject(R.id.labFromDetail)
    TextView labFromDetail;
    @ViewInject(R.id.labToDetail)
    TextView labToDetail;

    @ViewInject(R.id.labGoodsStyle)
    TextView labGoodsStyle;
    @ViewInject(R.id.labGoodsAmount)
    TextView labGoodsAmount;

    @ViewInject(R.id.labUnitPrice)
    TextView labUnitPrice;
    @ViewInject(R.id.labPriceUnit)
    TextView labPriceUnit;
    @ViewInject(R.id.labTotalPrice)
    TextView labTotalPrice;
    @ViewInject(R.id.txt_bidHide)
    TextView txt_bidHide;
    @ViewInject(R.id.txtPayeeCollectionAddress)
    TextView txtPayeeCollectionAddress;
    @ViewInject(R.id.name_txt)
    TextView name_txt;
    @ViewInject(R.id.phone_number)
    TextView phone_number;

    @ViewInject(R.id.btnShowDetail)
    LinearLayout btnShowDetail;
    @ViewInject(R.id.Lin_deliverAction)
    LinearLayout Lin_deliverAction;
    @ViewInject(R.id.Lin_myBid)
    LinearLayout Lin_myBid;
    @ViewInject(R.id.Lin_PayAddIcons)
    LinearLayout Lin_PayAddIcons;
    @ViewInject(R.id.img_call_up)
    ImageView img_call_up;
    @ViewInject(R.id.img_deliver_icon)
    ImageView img_deliver_icon;
    @ViewInject(R.id.Lin_AddPoints)
    LinearLayout Lin_AddPoints;

    @OnClick(R.id.btnShowDetail)
    public void showDetail(View v) {
//        Log.i("ShowDetailOrder", "showDetail:" + v.getId());
        Intent intent = this.getIntent();
        String json = intent.getStringExtra(ParamKey.ORDER_DETAIL);
        intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_lab_quoting));
        intent.setClass(baseContext, OrderDetailFragmentActivity.class);
        startActivity(intent);
    }

    void asyncRefreshOrderDetail() {

        OrderApi.detail(baseActivity, baseContext, orderId, new IJsonResult<OrderDetailChangeReturn>() {
            @Override
            public void ok(OrderDetailChangeReturn json) {
                //成功
//                swipeLayout.setRefreshing(false);
                bidList.clear();
                bidList.addAll(json.Bids);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    MeReturn userInfo;

    void fillData(OrderDetailChangeReturn inf) {
        try {
            userInfo = _dbCache.GetObject(MeReturn.class);
            if (userInfo == null) {
                Tracer.e(TAG, "NULL IFON");
                userInfo = new MeReturn();
            }
        } catch (Exception e) {
            Tracer.e(TAG, e.getMessage());
            //序列化错，重新创建对象
            userInfo = new MeReturn();
        }
        OrderDetailChangeReturn.OrderEntity o = inf.Order;
        orderId = o.Id;
        SharedPreferences clearSp = getSharedPreferences("notifyId", MODE_PRIVATE);
        int notyfyId = clearSp.getInt("notifyId", 0);
        String notyfyOrderId = clearSp.getString("orderId", "");
        if (!Guard.isNullOrEmpty(notyfyOrderId)&&!Guard.isNull(notyfyId)){
            if (Integer.valueOf(notyfyOrderId) == orderId){
                JPushInterface.clearNotificationById(baseActivity, notyfyId);
            }
        }
        payMethod = o.PayMethod;
        String FromAdress = "", ToAdress = "";
        if (o.From > 0) {
            FromAdress = areaHelper.getCityName(Integer.toString(o.From));
//            String[] names = areaHelper.getName(Integer.toString(o.From));
//            FromAdress = ParamTool.join(names, ",");
        }
        if (o.Payees != null) {
            if (o.Payees.size() > 0) {
//            String[] names = areaHelper.getName(Integer.toString(o.To));
//            ToAdress = ParamTool.join(names, ",");
                Lin_deliverAction.setVisibility(View.INVISIBLE);
                Lin_BidAdress.setVisibility(View.GONE);
                Lin_BidAdressList.setVisibility(View.VISIBLE);
                Lin_collectionPerson.setVisibility(View.VISIBLE);
                img_call_up.setVisibility(View.GONE);
                Lin_AddPoints.setVisibility(View.GONE);
                img_deliver_icon.setImageResource(R.mipmap.fh_icon_address);
                img_deliver_icon.setLayoutParams(Utils.setParams(0, 0, 25, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                Lin_PayeeMineAddress.setVisibility(View.VISIBLE);
                String[] names = areaHelper.getName(Integer.toString(o.From));
                FromAdress = ParamTool.join(names, ",");
                txtPayeeCollectionAddress.setText(FromAdress + o.FromDetail);
                String labName = "";
                if (!Guard.isNull(userInfo.OwnerUser)) {
                    if (userInfo.OwnerUser.CompanyStatus == 1) {
                        if (!Guard.isNull(userInfo.OwnerCompany)) {
                            labName = userInfo.OwnerCompany.CompanyName;
                        }
                    } else {
                        labName = userInfo.Owner.Name;
                    }
                }
                name_txt.setText(labName);
                if (!Guard.isNull(userInfo.OwnerUser)){
                    if (!Guard.isNullOrEmpty(userInfo.OwnerUser.Mobile)) {
                        phone_number.setText(userInfo.OwnerUser.Mobile);
                    }
                }
                AddPoint(Lin_PayAddIcons, this);
            }
        } else {
            if (!Guard.isNull(o.To)) {
                ToAdress = areaHelper.getCityName(Integer.toString(o.To));
                if (o.To==0){
                    ToAdress = "未知";
                }
            }
            Lin_BidAdress.setVisibility(View.VISIBLE);
            Lin_BidAdressList.setVisibility(View.GONE);
            Lin_PayeeMineAddress.setVisibility(View.GONE);
            Lin_collectionPerson.setVisibility(View.GONE);
        }
        labFromArea.setText(FromAdress);
        labToArea.setText(ToAdress);
        labOrderId.setText(o.Id + ""); //Note:必须要转为字符串，不然会去R资源中去找
        if (o.From>0) {
            String[] FromNames = areaHelper.getName(o.From + "");
            String FromDetail = ParamTool.join(FromNames, "") + "\n" + o.FromDetail;
            labFromDetail.setText(FromDetail);
        }
        if (o.To>0) {
            String[] ToNames = areaHelper.getName(o.To + "");
            String ToDetail = ParamTool.join(ToNames, "") + "\n" + o.ToDetail;
            labToDetail.setText(ToDetail);
        }
        labGoodsStyle.setText(o.GoodsType + "");
        labGoodsAmount.setText(o.GoodsAmount + " " + o.GoodsUnit);
//        if (o.Price > 0) {
            labUnitPrice.setText(o.Price+"");
//        } else {
//            Lin_myBid.setVisibility(View.GONE);
//            txt_bidHide.setVisibility(View.VISIBLE);
//        }
        String priceType = o.PriceType;
        if (Guard.isNullOrEmpty(priceType)){
            priceType = "元";
        }
        labPriceUnit.setText(priceType);

        //TODO:零担时才显示表达式
//        getString(R.string.lab_price_style_truck).equals(o.PriceType)||getString(R.string.lab_price_style_rmb_truck).equals(o.PriceType)
        if (o.PriceType.contains("包车")) {
            //包车价
            labTotalPrice.setText(FormatHelper.toMoney((o.Price)));
        } else {
            //零担计算价格
//            if (o.GoodsAmount > 0) {
                labTotalPrice.setText(FormatHelper.toMoney((o.Price * Double.valueOf(o.GoodsAmount))));
//            }
        }
        pressOne();
    }

    //增加小图标
    public static void AddPoint(ViewGroup view, Context context) {
        for (int i = 0; i < 7; i++) {
            ImageView imageinfo = new ImageView(context);
            imageinfo.setImageResource(R.mipmap.fh_icon_point);
            imageinfo.setLayoutParams(Utils.setParams(0, 0, 10, 0, 10, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
            Utils.addView(view, imageinfo);
        }
    }

    void sortBids(int style) {
        if (bidList == null) return;
        Comparator comparator;
        if (style == 1) {
            comparator = new ComparatorRate();
        } else if (style == 2) {
            comparator = new ComparatorObjectPrice();
        } else {
            comparator = new ComparatorDefault();
        }
        Collections.sort(bidList, comparator);
//        for (int i=0;i<bidList.size();i++){
//            Log.e(TAG,bidList.get(i).Price+" rate:"+bidList.get(i).Bearer.Rate+" id:"+bidList.get(i).Id);
//        }
//        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
//        Log.d(TAG, "notifyDataSetChanged"+" "+style);
    }

    void pressOne() {
        btnOrderByDefault.setEnabled(false);
        btnOrderByDefault.setTextAppearance(baseActivity, R.style.BlueText);

        btnOrderByCredit.setEnabled(true);
        btnOrderByCredit.setTextAppearance(baseActivity, R.style.DisableFont);

        btnOrderByPrice.setEnabled(true);
        btnOrderByPrice.setTextAppearance(baseActivity, R.style.DisableFont);

        sortBids(0);
    }

    void pressTwo() {

        btnOrderByDefault.setEnabled(true);
        btnOrderByDefault.setTextAppearance(baseActivity, R.style.DisableFont);

        btnOrderByCredit.setEnabled(false);
        btnOrderByCredit.setTextAppearance(baseActivity, R.style.BlueText);

        btnOrderByPrice.setEnabled(true);
        btnOrderByPrice.setTextAppearance(baseActivity, R.style.DisableFont);

        sortBids(1);

    }

    void pressThree() {

        btnOrderByDefault.setEnabled(true);
        btnOrderByDefault.setTextAppearance(baseActivity, R.style.DisableFont);

        btnOrderByCredit.setEnabled(true);
        btnOrderByCredit.setTextAppearance(baseActivity, R.style.DisableFont);

        btnOrderByPrice.setEnabled(false);
        btnOrderByPrice.setTextAppearance(baseActivity, R.style.BlueText);

        sortBids(2);

    }


    @ViewInject(R.id.btnOrderByDefault)
    Button btnOrderByDefault;
    @ViewInject(R.id.btnOrderByCredit)
    Button btnOrderByCredit;
    @ViewInject(R.id.btnOrderByPrice)
    Button btnOrderByPrice;

    @OnClick(R.id.btnOrderByDefault)
    public void OrderByDefault(View v) {
        pressOne();
    }

    @OnClick(R.id.btnOrderByCredit)
    public void OrderByCredit(View v) {
        pressTwo();
    }


    @OnClick(R.id.btnOrderByPrice)
    public void OrderByPrice(View v) {
        pressThree();
    }


    class ComparatorDefault implements Comparator {
        public int compare(Object arg0, Object arg1) {
            OrderDetailChangeReturn.BidsEntity bid0 = (OrderDetailChangeReturn.BidsEntity) arg0;
            OrderDetailChangeReturn.BidsEntity bid1 = (OrderDetailChangeReturn.BidsEntity) arg1;
            //按ID排序
            return bid0.Id - bid1.Id;
        }
    }

    class ComparatorCredit implements Comparator {
        public int compare(Object arg0, Object arg1) {
            OrderDetailChangeReturn.BidsEntity bid0 = (OrderDetailChangeReturn.BidsEntity) arg0;
            OrderDetailChangeReturn.BidsEntity bid1 = (OrderDetailChangeReturn.BidsEntity) arg1;
            //按评分排序
            if (bid0.Bearer == null || bid1.Bearer == null) return 0;
            return bid0.Bearer.Score - bid1.Bearer.Score;
        }
    }
    class ComparatorRate implements Comparator{

        @Override
        public int compare(Object arg0, Object arg1) {
            OrderDetailChangeReturn.BidsEntity bid0 = (OrderDetailChangeReturn.BidsEntity) arg0;
            OrderDetailChangeReturn.BidsEntity bid1 = (OrderDetailChangeReturn.BidsEntity) arg1;
            //按评分排序
            if (bid0.Bearer == null || bid1.Bearer == null) return 0;
            BigDecimal data1 = new BigDecimal(bid0.Bearer.Rate);
            BigDecimal data2 = new BigDecimal(bid1.Bearer.Rate);
            return data2.compareTo(data1);
        }
    }
    class ComparatorObjectPrice implements Comparator<OrderDetailChangeReturn.BidsEntity> {
        @Override
        public int compare(OrderDetailChangeReturn.BidsEntity lhs, OrderDetailChangeReturn.BidsEntity rhs) {
            double lhsPrice = lhs.Price;
            double rhsPrice = rhs.Price;
            OrderDetailChangeReturn.OrderEntity order = _result.Order;
//            if (!lhs.PriceType.equals("元/包车")){
//                lhsPrice = order.GoodsAmount * lhs.Price;
//            }
//            if (!lhs.PriceType.equals("元/包车")){
//                rhsPrice = order.GoodsAmount * rhs.Price;
//            }
            Tracer.e(TAG,lhsPrice+" lhsPrice:"+lhs.Price+" rhsPrice:"+rhs.Price+" rhsPrice:"+rhsPrice);
            BigDecimal data1 = new BigDecimal(lhsPrice);
            BigDecimal data2 = new BigDecimal(rhsPrice);
//            return (int)(lhsPrice - rhsPrice);
            return data1.compareTo(data2);
        }
    }
    class ComparatorPrice implements Comparator {
        public int compare(Object arg0, Object arg1) {
            OrderDetailChangeReturn.BidsEntity bid0 = (OrderDetailChangeReturn.BidsEntity) arg0;
            OrderDetailChangeReturn.BidsEntity bid1 = (OrderDetailChangeReturn.BidsEntity) arg1;
            //按价格排序
            return (int) (bid0.Price - bid1.Price);
        }
    }
}
