package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.orhanobut.dialogplus.DialogPlus;
import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.OrderStatusType;
import org.csware.ee.model.PayMethodType;
import org.csware.ee.model.PaymentPointType;
import org.csware.ee.model.PaymentStatusType;
import org.csware.ee.model.PromoteActionType;
import org.csware.ee.model.RegistModel;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.EnumHelper;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.CircleImageView;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ScrollViewForListView;

import java.text.Format;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import cn.jpush.android.api.JPushInterface;


public class OrderDetailFragmentActivity extends FragmentActivityBase {

    final static String TAG = "OrderDetailAct";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.labOrderStatus)
    TextView labOrderStatus;
    @InjectView(R.id.labOrderId)
    TextView labOrderId;
    @InjectView(R.id.ivHeadPic)
    CircleImageView ivHeadPic;
    @InjectView(R.id.labName)
    TextView labName;
    @InjectView(R.id.labCPH)
    TextView labCPH;
    @InjectView(R.id.labPhoneNumber)
    TextView labPhoneNumber;
    @InjectView(R.id.btnPhoneCall)
    ImageButton btnPhoneCall;
    @InjectView(R.id.labHPrice)
    TextView labHPrice;
    @InjectView(R.id.labPriceUnit)
    TextView labPriceUnit;
    @InjectView(R.id.labTotalPrice)
    TextView labTotalPrice;
    @InjectView(R.id.labPriceExpress)
    TextView labPriceExpress;
    @InjectView(R.id.labHPayStatus)
    TextView labHPayStatus;
    @InjectView(R.id.labHPayMethod)
    TextView labHPayMethod;
    @InjectView(R.id.btnComment)
    Button btnComment;
    @InjectView(R.id.labFrom)
    TextView labFrom;
    @InjectView(R.id.labTo)
    TextView labTo;
    @InjectView(R.id.labFromAddress)
    TextView labFromAddress;
    @InjectView(R.id.optFrom)
    LinearLayout optFrom;
    @InjectView(R.id.Lin_totalPrice)
    LinearLayout Lin_totalPrice;
    @InjectView(R.id.labToAddress)
    TextView labToAddress;
    @InjectView(R.id.optTo)
    LinearLayout optTo;
    @InjectView(R.id.labGoodsStyle)
    TextView labGoodsStyle;
    //    @InjectView(R.id.optGoodsStyle)
//    LinearLayout optGoodsStyle;
    @InjectView(R.id.labAmount)
    TextView labAmount;
    //    @InjectView(R.id.optAmount)
//    LinearLayout optAmount;
    @InjectView(R.id.labTransStyle)
    TextView labTransStyle;
    @InjectView(R.id.linearLayout4)
    LinearLayout linearLayout4;
    @InjectView(R.id.labTruckStyle)
    TextView labTruckStyle;
    @InjectView(R.id.labTruckLength)
    TextView labTruckLength;
    @InjectView(R.id.optTruckStyle)
    LinearLayout optTruckStyle;
    @InjectView(R.id.labBeginTime)
    TextView labBeginTime;
    @InjectView(R.id.optBeginTime)
    LinearLayout optBeginTime;
    @InjectView(R.id.labEndTime)
    TextView labEndTime;
    @InjectView(R.id.optEndTime)
    LinearLayout optEndTime;
    @InjectView(R.id.labPrice)
    TextView labPrice;
    @InjectView(R.id.optPrice)
    LinearLayout optPrice;
    @InjectView(R.id.labInvoice)
    TextView labInvoice;
    @InjectView(R.id.labGoodsUnit)
    TextView labGoodsUnit;
    @InjectView(R.id.optInvoice)
    LinearLayout optInvoice;
    @InjectView(R.id.labPayMethod)
    TextView labPayMethod;
    @InjectView(R.id.optPayMethod)
    LinearLayout optPayMethod;
    @InjectView(R.id.txtMemo)
    TextView txtMemo;
    @InjectView(R.id.btnPressSay)
    Button btnPressSay;
    @InjectView(R.id.btnPhoto)
    ImageButton btnPhoto;
    @InjectView(R.id.txtName)
    TextView txtName;
    @InjectView(R.id.optGoodsStyle)
    LinearLayout optGoodsStyle;
    @InjectView(R.id.txtPhone)
    TextView txtPhone;
    @InjectView(R.id.labPayAction)
    TextView labPayAction;
    @InjectView(R.id.optAmount)
    LinearLayout optAmount;
    @InjectView(R.id.Lin_order_sub_hackman_phone)
    LinearLayout linPhone;
    @InjectView(R.id.Lin_DetailAdress)
    LinearLayout Lin_DetailAdress;
    @InjectView(R.id.Lin_DetailAdressList)
    LinearLayout Lin_DetailAdressList;
    @InjectView(R.id.LinBeaerList)
    LinearLayout LinBeaerList;
    @InjectView(R.id.LinShowMore)
    LinearLayout LinShowMore;
    @InjectView(R.id.listviewDetailAddress)
    ScrollViewForListView listviewDetailAddress;
    @InjectView(R.id.selectimg_horizontalScrollView)
    HorizontalScrollView selectimg_horizontalScrollView;
    @InjectView(R.id.listViewBeaer)
    ScrollViewForListView listViewBeaer;
    @InjectView(R.id.noScrollgridview)
    GridView gridview;
    @InjectView(R.id.rptScrollgridview)
    GridView rptScrollGridview;
    @InjectView(R.id.labInsurance)
    TextView labInsurance;
    @InjectView(R.id.txtConComp)
    TextView txtConComp;
    @InjectView(R.id.optInsurance)
    LinearLayout optInsurance;
    @InjectView(R.id.LinContacts)
    LinearLayout LinContacts;
    @InjectView(R.id.boxHackman)
    LinearLayout boxHackman;
    @InjectView(R.id.imgGameHead)
    ImageView imgGameHead;
    @InjectView(R.id.imgPhoneCall)
    ImageView imgPhoneCall;
    @InjectView(R.id.rate_rating)
    RatingBar rate_rating;
    @InjectView(R.id.horizontalScrollView)
    HorizontalScrollView horizontalScrollView;
    @InjectView(R.id.LinReceipt)
    LinearLayout LinReceipt;
    @InjectView(R.id.LinPayTo)
    LinearLayout LinPayTo;
    @InjectView(R.id.btnPay)
    LinearLayout btnPay;
    @InjectView(R.id.payPrice)
    TextView payPrice;
    private GridAdapter gridAdapter;
    LinearLayout Lin_PayAddIcons, Lin_DetailConsignee;
    ImageView img_call_up;
    ImageView img_deliver_icon;
    LinearLayout Lin_AddPoints, Lin_collectionPerson;
    TextView name_txt;
    TextView phone_number;
    TextView txtPayeeCollectionAddress, txt_collection_numbs, txt_collection_numbs_pay;
    CommonAdapter<OrderDetailChangeReturn.OrderEntity.SubOrdersEntity> adapter;
    List<OrderDetailChangeReturn.OrderEntity.SubOrdersEntity> contactsList = new ArrayList<>();
    List<OrderDetailChangeReturn.OrderEntity.SubOrdersEntity> tmpList = new ArrayList<>();
    private List<OrderDetailChangeReturn.OrderEntity.PayeesEntity> payeeList = new ArrayList<>();
    private CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity> PayeeAdapter;
    String title = "";
    private static Handler sHandler;
    public static Activity instance;
    Shipper shipper;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_detail_fragment_activity);
        ButterKnife.inject(this);
//        initImageLoader(this);
//        this.initLoder();

        sHandler = new Handler();
        instance = this;
        sHandler.post(mHideRunnable); // hide the navigation bar
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sHandler.post(mHideRunnable); // hide the navigation bar
            }
        });

        //ParamKey.BACK_TITLE, getString(R.string.tab_lab_quoting)
        title = getIntent().getStringExtra(ParamKey.BACK_TITLE);
        if (!Guard.isNullOrEmpty(title)){
            title = "";
        }
        txt_collection_numbs = (TextView) findViewById(R.id.txt_collection_numbs);
        txt_collection_numbs_pay = (TextView) findViewById(R.id.txt_collection_numbs_pay);
        name_txt = (TextView) findViewById(R.id.name_txt);
        txtPayeeCollectionAddress = (TextView) findViewById(R.id.txtPayeeCollectionAddress);
        phone_number = (TextView) findViewById(R.id.phone_number);
        img_call_up = (ImageView) findViewById(R.id.img_call_up);
        img_deliver_icon = (ImageView) findViewById(R.id.img_deliver_icon);
        Lin_AddPoints = (LinearLayout) findViewById(R.id.Lin_AddPoints);
        Lin_DetailConsignee = (LinearLayout) findViewById(R.id.Lin_DetailConsignee);
        Lin_PayAddIcons = (LinearLayout) findViewById(R.id.Lin_PayAddIcons);
        Lin_collectionPerson = (LinearLayout) findViewById(R.id.Lin_collectionPerson);

        init();

        //承运司机列表适配器
        setBeaerAdapter();
        if (contactsList.size() >= 1) {
            tmpList.add(contactsList.get(0));
//            tmpList.add(contactsList.get(1));
        } else {
            tmpList.addAll(contactsList);
        }
        adapter.notifyDataSetChanged();
        //是否显示删除按钮
        if (title.equals(getString(R.string.tab_lab_quoting)) || status < 2) {
            Lin_DetailConsignee.setVisibility(View.GONE);
            topBar.setMenu(R.drawable.dd_icon_delete, new TopActionBar.MenuClickListener() {
                @Override
                public void menuClick() {
//                    Log.d(TAG, "setMenuClickListener");
                    baseActivity.setTheme(R.style.ActionSheetStyle);
                    ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                            .setCancelButtonTitle("取消")
                            .setOtherButtonTitles("废除当前订单")
                            .setCancelableOnTouchOutside(true)
                            .setListener(actionSheetListener).show();
                }
            });
        }

    }

    void setBeaerAdapter() {
        adapter = new CommonAdapter<OrderDetailChangeReturn.OrderEntity.SubOrdersEntity>(this, tmpList, R.layout.list_contact_item) {
            @Override
            public void convert(ViewHolder helper, final OrderDetailChangeReturn.OrderEntity.SubOrdersEntity item, final int position) {
//                if (item.Status>3) {
//                    helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_dhwqr);
//                }else if (item.Status==3){
//                    helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_ysz);
//                }
//                switch (item.Status) {
//                    case 3:
//                        helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_ysz);
//                        break;
//                    case 5:
//                        helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_dhwqr);
//                        break;
//                    case 6:
//                        if (item.BearerRate) {
//                            helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_ywc);
//                        } else {
//                            helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_wpj);
//                        }
//                        break;
//                }
                helper.getView(R.id.imgBeaerStatus).setVisibility(View.GONE);
                String Name = "";
//                if (!Guard.isNull(item.BearerCompany)) {
//                    if (!Guard.isNullOrEmpty(item.BearerCompany.CompanyName)) {
//                        Name = item.BearerCompany.CompanyName;
//                        helper.getView(R.id.txtPlate).setVisibility(View.GONE);
//                    }
//                } else {
                    Name = item.Bearer.Name;
                    helper.getView(R.id.txtPlate).setVisibility(View.VISIBLE);
//                }
                helper.setText(R.id.txtConComp, Name.trim() + "");
                helper.setText(R.id.txtPlate, item.Bearer.Plate + "");
                helper.setRating(R.id.rate_rating, (float) item.Bearer.Rate);
//                helper.getView(R.id.imgBeaerStatus).setVisibility(View.VISIBLE);
//                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) helper.getView(R.id.imgBeaerStatus).getLayoutParams();
//                params.setMargins(0, 0, 10, 0);
//                helper.getView(R.id.imgPhoneCall).setLayoutParams(params);
                helper.getView(R.id.imgPhoneCall).setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Guard.isNullOrEmpty(item.BearerUser.Mobile)) {
                            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + item.BearerUser.Mobile));
                            startActivity(nIntent);
                        }
                    }
                });
                if (!Guard.isNullOrEmpty(item.BearerUser.Avatar)) {
                    ImageLoader.getInstance().displayImage(item.BearerUser.Avatar, (ImageView) helper.getView(R.id.imgGameHead));
                }else {
                    helper.setImageResource(R.id.imgGameHead,R.drawable.w_icon_tjzp);
                }

            }
        };
        listViewBeaer.setAdapter(adapter);
    }

    //展开司机列表
    @OnClick(R.id.LinShowMore)
    void setLinShowMore() {
        //Show More and Gone
        LinShowMore.setVisibility(View.GONE);
        tmpList.clear();
        tmpList.addAll(contactsList);
        adapter.notifyDataSetChanged();
    }

    Runnable mHideRunnable = new Runnable() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void run() {
            int flags;
            int curApiVersion = Build.VERSION.SDK_INT;
            // This work only for android 4.4+
            if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
                // This work only for android 4.4+
                // hide navigation bar permanently in android activity
                // touch the screen, the navigation bar will not show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            } else {
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
                //废除订单
                if (orderEntity.Id <= 0) {
                    return;
                }
                OrderApi.delorder(baseActivity, baseActivity, orderEntity.Id, new IJsonResult<RegistModel>() {
                    @Override
                    public void ok(RegistModel json) {
                        //成功
                        if (json.Result == 0) {
                            toastFast("废除成功！");
                            SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferences.edit();
                            editor.putBoolean("isRefresh", true).commit();
                            finish();
                            if (OrderBidFragmentActivity.instance != null) {
                                OrderBidFragmentActivity.instance.finish();
                            }
                        } else {
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

    ChinaAreaHelper areaHelper;
    int status;
    long orderId;
    double dealPrice;
    String subject;
    OrderDetailChangeReturn.OrderEntity orderEntity;
    OrderDetailChangeReturn.BidsEntity bidsEntity;
    DbCache _dbCache;
    MeReturn userInfo;
    List<String> images = new ArrayList<>();
    List<String> rptImages = new ArrayList<>();
    String rptJson = "";

    void init() {
        _dbCache = new DbCache(baseActivity);
        shipper = _dbCache.GetObject(Shipper.class);
        bindStatusInfo = _dbCache.GetObject(BindStatusInfo.class);
        if (Guard.isNull(bindStatusInfo)) {
            bindStatusInfo = new BindStatusInfo();
        }
        try {
            userInfo = _dbCache.GetObject(MeReturn.class);
            if (userInfo == null) {
                Tracer.e(TAG, "NULL IFON");
                userInfo = new MeReturn();

            }
        } catch (Exception e) {
            Tracer.e(TAG, e.getMessage() + "");
            //序列化错，重新创建对象
            userInfo = new MeReturn();
        }
        areaHelper = new ChinaAreaHelper(baseActivity);
        Intent intent = this.getIntent();
        final String json = intent.getStringExtra(ParamKey.ORDER_DETAIL);
        rptJson = json;
        OrderDetailChangeReturn result = GsonHelper.fromJson(json, OrderDetailChangeReturn.class);
        if (!Guard.isNull(result)){
            if (!Guard.isNull(result.Order)){
                if (!Guard.isNull(result.Order.Status)){
                    if (result.Order.Status < 2) {
                        LinPayTo.setVisibility(View.GONE);
                    }
                }

                if (!Guard.isNull(result.Order.SubOrders)) {
                    contactsList = result.Order.SubOrders;
                }

                if (!Guard.isNull(result.Order.Evidences)) {
                    for (int i = 0; i < result.Order.Evidences.size(); i++) {
                        if (!Guard.isNullOrEmpty(result.Order.Evidences.get(i).Url)) {
                            rptImages.add(result.Order.Evidences.get(i).Url);
                        }
                    }
                }
            }
        }else {
            result = new OrderDetailChangeReturn();
        }

        if (!Guard.isNull(result)){
            orderEntity = result.Order;
        }
        if (Guard.isNull(orderEntity)){
            finish();
            return;
        }
        if (!Guard.isNull(result.Bids)) {
            if (result.Bids.size() > 0) {
                bidsEntity = result.Bids.get(0);
            }
        }
        if (!Guard.isNull(orderEntity)) {
            if (!Guard.isNull(orderEntity.Paid)) {
                if (orderEntity.Paid > 0) {
                    payPrice.setVisibility(View.VISIBLE);
                    SpannableString spanText = new SpannableString("已付" + FormatHelper.toMoney(orderEntity.Paid) + getString(R.string.unit_rmb));
                    spanText.setSpan(new ForegroundColorSpan(Color.RED), 2, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                    payPrice.setText(spanText);
                    payPrice.setMovementMethod(LinkMovementMethod.getInstance());
                } else {
                    payPrice.setVisibility(View.GONE);
                }
            }
            //备注照片显示
            String img = orderEntity.Images;
            if (!Guard.isNullOrEmpty(img)) {
                String[] imgs = img.split(",");
                for (int i = 0; i < imgs.length; i++) {
                    images.add(imgs[i]);
                }
//            if (images.size() > 0) {
                gridviewInit(images, gridview, selectimg_horizontalScrollView, false);
//            }
            } else {
                selectimg_horizontalScrollView.setVisibility(View.GONE);
            }
        }

        if (!Guard.isNull(contactsList)) {
            if (contactsList.size() > 0 && orderEntity.Status >= 2 && orderEntity.Status < 5) {
                LinBeaerList.setVisibility(View.VISIBLE);
            } else {
                LinBeaerList.setVisibility(View.GONE);
            }
        }


        //回单照片显示适配 rptImages rptScrollGridView honScrollView //TODO 跟备注照搬...需要改变图片查看方式
        if (rptImages.size() > 0) {
            gridviewInit(rptImages, rptScrollGridview, horizontalScrollView, true);
        } else {
            LinReceipt.setVisibility(View.GONE);
        }


        //代收货款收货地址集
        payeeList = result.Order.Payees;
        if (orderEntity == null || result.Result != 0) {
            toastSlow("订单数据错误");
            delayedFinish(500);
            return;
        }
        if (orderEntity.Bearer == null) {
//            View x = findViewById(R.id.boxHackman);
            boxHackman.setVisibility(View.GONE);
        }
//        if (title.equals(getString(R.string.tab_lab_quoting))){
//            boxHackman.setVisibility(View.GONE);
//        }
        //收款人点击事件
        Lin_collectionPerson.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (payeeList != null) {
                    if (payeeList.size() > 0) {
                        Intent intent = new Intent(OrderDetailFragmentActivity.this, OrderCollectionActivity.class);
                        intent.putExtra("json", json);
                        startActivity(intent);
                    }
                }
            }
        });
        orderId = orderEntity.Id;
        SharedPreferences clearSp = getSharedPreferences("notifyId", MODE_PRIVATE);
        int notyfyId = clearSp.getInt("notifyId", 0);
        String notyfyOrderId = clearSp.getString("orderId", "");
        if (!Guard.isNullOrEmpty(notyfyOrderId) && !Guard.isNull(notyfyId)) {
            if (Integer.valueOf(notyfyOrderId) == orderId) {
                JPushInterface.clearNotificationById(baseActivity, notyfyId);
            }
        }
//        SharedPreferences.Editor cledit = clearSp.edit();
//        cledit.putInt("notifyId", notifactionId).putString("orderId",key1+"").commit();
        dealPrice = orderEntity.DealPrice;
        //Tracer.e(TAG, order.Status+"");
        status = orderEntity.Status;
        subject = orderEntity.GoodsType;
        labOrderStatus.setText(EnumHelper.getCnName(status, OrderStatusType.class) + "");
        labOrderId.setText(orderEntity.Id + "");
        if (!Guard.isNullOrEmpty(orderEntity.Insurance)) {
            labInsurance.setText("免费赠送一份5万保险");
        } else {
            labInsurance.setText("无保");
            labInsurance.setTextColor(Color.GRAY);
        }
        OrderDetailChangeReturn.OrderEntity.BearerEntity bear = orderEntity.Bearer;
        if (bear == null) bear = new OrderDetailChangeReturn.OrderEntity.BearerEntity();
        String Name = "";
        if (!Guard.isNull(orderEntity.BearerCompany)) {
            if (Guard.isNull(orderEntity.BearerUser.CompanyStatus))
                orderEntity.BearerUser.CompanyStatus = -1;
            double rate = 0;
            if (!Guard.isNullOrEmpty(orderEntity.BearerCompany.CompanyName) && orderEntity.BearerUser.CompanyStatus == 1) {
                Name = orderEntity.BearerCompany.CompanyName;
                if (!Guard.isNull(orderEntity.Bearer)){
                    if (!Guard.isNull(orderEntity.Bearer.Rate)){
                        rate = orderEntity.Bearer.Rate;
                    }
                }
//                boxHackman.setVisibility(View.GONE);
//                txtConComp.setText(Name);
//                rate_rating.setRating((float) orderEntity.Bearer.Rate);
//
//                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Avatar)) {
//                    ImageLoader.getInstance().displayImage(orderEntity.BearerUser.Avatar, imgGameHead);
//                }
            }else {
                Name = bear.Name;
            }
            txtConComp.setText(Name);
            rate_rating.setRating((float) rate);

            if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Avatar)) {
                ImageLoader.getInstance().displayImage(orderEntity.BearerUser.Avatar, imgGameHead);
                ImageLoader.getInstance().displayImage(orderEntity.BearerUser.Avatar, ivHeadPic);
            }
        } else {

            Name = bear.Name;
            if (title.equals(getString(R.string.tab_lab_quoting))) {
                boxHackman.setVisibility(View.GONE);
            } else {
                boxHackman.setVisibility(View.VISIBLE);
            }

        }
        if (!Guard.isNullOrEmpty(Name) && orderEntity.Status>=2){
//            LinContacts.setVisibility(View.VISIBLE);
            boxHackman.setVisibility(View.VISIBLE);
        }else {
//            LinContacts.setVisibility(View.GONE);
            boxHackman.setVisibility(View.GONE);
        }
        if (boxHackman.getVisibility() == View.GONE && !Guard.isNullOrEmpty(Name) && orderEntity.Status>=2){
            LinContacts.setVisibility(View.VISIBLE);
        }
        labName.setText(Name);
        labCPH.setText(bear.Plate);
        if (orderEntity.BearerUser != null) {
            labPhoneNumber.setText(orderEntity.BearerUser.Mobile + "");
            if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Avatar)) {
                ImageLoader.getInstance().displayImage(orderEntity.BearerUser.Avatar, ivHeadPic);
                ImageLoader.getInstance().displayImage(orderEntity.BearerUser.Avatar, imgGameHead);
            }
        }
        imgPhoneCall.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderEntity.BearerUser.Mobile));
                    startActivity(nIntent);
                }
            }
        });
        labPriceUnit.setText(orderEntity.PriceType);
        if (orderEntity.Bearer == null) {
            //未人中标时显示的计价
            labHPrice.setText(FormatHelper.toMoney(orderEntity.Price));
            //getString(R.string.lab_price_style_truck).equals(order.PriceType)||getString(R.string.lab_price_style_rmb_truck).equals(order.PriceType)
            if (orderEntity.PriceType.contains("包车")) {
                //包车价
                labTotalPrice.setText(FormatHelper.toMoney(orderEntity.Price) + getString(R.string.unit_rmb));
//                labPriceExpress.setText("元");
            } else {
                //零担计算价格
                if (orderEntity.GoodsAmount > 0) {
                    labTotalPrice.setText(FormatHelper.toMoney(orderEntity.Price * Double.valueOf(orderEntity.GoodsAmount)) + getString(R.string.unit_rmb));
//                    labPriceExpress.setText("(" + order.Price + "*" + order.GoodsAmount + ")" + getString(R.string.unit_rmb));
                }
            }
        } else {
            //中标后的计价
            if (orderEntity.DealPrice > 0) {
                labHPrice.setText(FormatHelper.toMoney(orderEntity.DealPrice));
                labPriceUnit.setText(getString(R.string.unit_rmb));
                Lin_totalPrice.setVisibility(View.GONE);
            } else {
                labHPrice.setText(FormatHelper.toMoney(orderEntity.Price));
            }
            //getString(R.string.lab_price_style_truck).equals(order.PriceType)||getString(R.string.lab_price_style_rmb_truck).equals(order.PriceType)
            if (orderEntity.PriceType.contains("包车")) {
                //包车价
                if (orderEntity.DealPrice > 0) {
                    labTotalPrice.setText(FormatHelper.toMoney(orderEntity.DealPrice) + getString(R.string.unit_rmb));
                    labPriceExpress.setText("");
                } else {
                    labTotalPrice.setText(FormatHelper.toMoney(orderEntity.Price) + getString(R.string.unit_rmb));
                }

            } else {
                //零担计算价格
                if (orderEntity.DealPrice > 0) {
                    labTotalPrice.setText(FormatHelper.toMoney(orderEntity.DealPrice) + getString(R.string.unit_rmb));
                    labPriceExpress.setText("");
                } else {
                    if (orderEntity.GoodsAmount > 0) {
                        labTotalPrice.setText(FormatHelper.toMoney(orderEntity.Price * Double.valueOf(orderEntity.GoodsAmount)) + getString(R.string.unit_rmb));
//                        labPriceExpress.setText("(" + order.Price + "*" + order.GoodsAmount + ")" + getString(R.string.unit_rmb));
                    }
                }
            }
        }

        String payStatus = EnumHelper.getCnName(orderEntity.PaymentStatus, PaymentStatusType.class);
        String payMethod = EnumHelper.getCnName(orderEntity.PayMethod, PayMethodType.class);
        //Tracer.e(TAG, "" + payMethod);
        if (orderEntity.PayMethod == PayMethodType.OTHER.toValue()) {
            //其它付款方式时不用显示付款状态
            labHPayStatus.setText("");
        } else {
            labHPayStatus.setText(payStatus);
        }
        labHPayMethod.setText(payMethod);

        String FromAdress = "", ToAdress = "";
        if (orderEntity.From > 0) {
//            String[] names = areaHelper.getName(Integer.toString(orderEntity.From));
//            FromAdress = ParamTool.join(names, ",");
            FromAdress = areaHelper.getCityName(Integer.toString(orderEntity.From));
        }
        if (orderEntity.Payees != null) {
//            String[] names = areaHelper.getName(Integer.toString(orderEntity.To));
//            ToAdress = ParamTool.join(names, ",");
            if (orderEntity.Payees.size() > 0) {
                Lin_DetailConsignee.setVisibility(View.GONE);
                Lin_DetailAdress.setVisibility(View.GONE);
                Lin_DetailAdressList.setVisibility(View.VISIBLE);
                Lin_collectionPerson.setVisibility(View.VISIBLE);
                img_call_up.setVisibility(View.GONE);
                Lin_AddPoints.setVisibility(View.GONE);
                Lin_AddPoints.setVisibility(View.GONE);
                img_deliver_icon.setImageResource(R.mipmap.fh_icon_address);
                img_deliver_icon.setLayoutParams(Utils.setParams(0, 0, 30, 0, 0, LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                String[] names = areaHelper.getName(Integer.toString(orderEntity.From));
                FromAdress = ParamTool.join(names, ",");
                txtPayeeCollectionAddress.setText(FromAdress + orderEntity.FromDetail);
                String labName = "";

                if (!Guard.isNull(userInfo.OwnerUser)) {
                    if (Guard.isNull(userInfo.OwnerUser.CompanyStatus))
                        userInfo.OwnerUser.CompanyStatus = -1;
                    if (!Guard.isNull(userInfo.OwnerCompany) && userInfo.OwnerUser.CompanyStatus == 1) {
                        labName = userInfo.OwnerCompany.CompanyName;
                    }
                    if (!Guard.isNullOrEmpty(userInfo.OwnerUser.Mobile)) {
                        phone_number.setText(userInfo.OwnerUser.Mobile);
                    }
                } else {
                    if (!Guard.isNull(userInfo.Owner)) {
                        labName = userInfo.Owner.Name;
                    }
                }
                name_txt.setText(labName);

                OrderBidFragmentActivity.AddPoint(Lin_PayAddIcons, this);
            }
        } else {
            if (!Guard.isNull(orderEntity.To)) {
                ToAdress = areaHelper.getCityName(Integer.toString(orderEntity.To));
                if (orderEntity.To==0){
                    ToAdress = "未知";
                }
            }
            Lin_collectionPerson.setVisibility(View.GONE);
        }
        labFrom.setText(FromAdress);
        labTo.setText(ToAdress);
        if (orderEntity.From > 0) {
            String[] FromNames = areaHelper.getName(orderEntity.From + "");
            String FromDetail = ParamTool.join(FromNames, "") + "\n" + orderEntity.FromDetail;
            labFromAddress.setText(FromDetail);
        }
        if (orderEntity.To > 0) {
            String[] ToNames = areaHelper.getName(orderEntity.To + "");
            String ToDetail = ParamTool.join(ToNames, "") + "\n" + orderEntity.ToDetail;
            labToAddress.setText(ToDetail);
        }
//        labFromAddress.setText(orderEntity.FromDetail);
//        labToAddress.setText(orderEntity.ToDetail);

        labGoodsStyle.setText(orderEntity.GoodsType);
        labAmount.setText(orderEntity.GoodsAmount + " " + orderEntity.GoodsUnit);
        labTransStyle.setText(orderEntity.PriceCalType);
        labTruckStyle.setText(orderEntity.TruckType);
        String carLength = orderEntity.TruckLength + "";
        if (orderEntity.TruckLength > 0) {
            labTruckLength.setText(carLength + "米");
        } else {
            labTruckLength.setText("不限");
        }

        labBeginTime.setText(ParamTool.fromTimeSeconds(orderEntity.FromTime));
        labEndTime.setText(ParamTool.fromTimeSeconds(orderEntity.ToTime));
        //+getString(R.string.unit_rmb)
        labPrice.setText(labTotalPrice.getText());
        labPrice.setTextColor(getResources().getColor(R.color.orange_red));
        labPayMethod.setText(EnumHelper.getCnName(orderEntity.PayPoint, PaymentPointType.class));
        if (orderEntity.PayPoint == 4) {
            labPayMethod.setText("见回单付款");
        }
        labPayAction.setText(EnumHelper.getCnName(orderEntity.PayMethod, PayMethodType.class));
        String tax = orderEntity.Tax;
        if (Guard.isNullOrEmpty(tax)) {
            tax = "无需发票";
        }
        labInvoice.setText(tax);

        txtMemo.setText(orderEntity.Message + "");
        txtName.setText(orderEntity.ToName + "");
        txtPhone.setText(orderEntity.ToNumber + "");


        //TODO: 不同状态下的判断 运送中状态下显示承认司机列表待定
        if (status == 2) {
//            if (orderEntity.PayMethod == 2) {
            //其它付款方式时不用支付到平台，否则全部要支付到平台 。
            btnComment.setText("确认送达");
            btnComment.setEnabled(true);
            btnComment.setVisibility(View.VISIBLE);
            btnComment.setOnClickListener(clickReceiveDone);
//                btnComment.setOnClickListener(clickLoaded);

//            } else {
//                btnComment.setVisibility(View.VISIBLE);
//                if (orderEntity.PaymentStatus == PaymentStatusType.NOT_YET.toValue()) {
//                    btnComment.setText("付款(平台担保)");
//                    btnComment.setEnabled(true);
//                    btnComment.setOnClickListener(clickPayToPlatform);
//                } else if (orderEntity.PaymentStatus == PaymentStatusType.GIVEN.toValue()) {
//                    btnComment.setEnabled(false);
//                    btnComment.setText(R.string.ysz);
//                } else if (orderEntity.PaymentStatus == PaymentStatusType.GOT.toValue()) {
//                    btnComment.setVisibility(View.GONE); //司机已收到
//                }
//
//            }
        } else if (status == 5) {
            //货物到站，我来确认收到了，已签收了
            String btnText = orderEntity.PayMethod == 2 ? "签收" : "签收(打款给司机)"; //TODO:不同付款方式会有不同的打款结果  见票付款 见回单付款 在结束后再付
            btnComment.setText(btnText);
            btnComment.setEnabled(true);
            btnComment.setVisibility(View.VISIBLE);
            btnComment.setOnClickListener(clickReceiveDone);
        } else if (status == 6) {
            btnComment.setText("评价");
            btnComment.setEnabled(true);
            btnComment.setVisibility(View.VISIBLE);
            btnComment.setOnClickListener(clickComment);
        } else {
            btnComment.setEnabled(false);
            btnComment.setVisibility(View.GONE);
        }

        //已评价，不再显示
        if (orderEntity.OwnerRate) {
            btnComment.setEnabled(false);
            btnComment.setText("已完成");
            btnComment.setBackgroundResource(R.color.face_grey);
            btnComment.setTextColor(getResources().getColor(R.color.white));
        }

        if (payeeList != null) {
            txt_collection_numbs.setText(payeeList.size() + "");
            int j = 0;
            for (int i = 0; i < payeeList.size(); i++) {
                if (payeeList.get(i).Status != 1) {
                    j++;
                }
            }
            txt_collection_numbs_pay.setText(j + "");
            PayeeAdapter = new CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity>(baseActivity, payeeList, R.layout.payee_address_item) {

                /**
                 * 重写，返回了序列元素的标识Id
                 * 返回0时表示没有主键Id
                 */
                @Override
                public long getItemId(int position) {
                    OrderDetailChangeReturn.OrderEntity.PayeesEntity item = payeeList.get(position);
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
                        OrderBidFragmentActivity.AddPoint((ViewGroup) helper.getView(R.id.Lin_PayAddIcons), OrderDetailFragmentActivity.this);
                    }
                    helper.setText(R.id.txtPayeeCollectionAddress, ToAdress + item.Address);
                    String Consignee = item.Name + "";
                    if (Guard.isNullOrEmpty(Consignee)) {
                        Consignee = "未确认";
                    }
                    helper.setText(R.id.name_txt, Consignee + "");
                    helper.setText(R.id.phone_number, item.Mobile + "");
                    if (Guard.isNullOrEmpty(item.Mobile)) {
                        helper.getView(R.id.img_call_up).setVisibility(View.GONE);
                    } else {
                        helper.getView(R.id.img_call_up).setVisibility(View.VISIBLE);
                        helper.getView(R.id.img_call_up).setOnClickListener(new OnClickListener() {
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
            listviewDetailAddress.setAdapter(PayeeAdapter);

        }

    }

    public void gridviewInit(final List<String> images, GridView gridview, final HorizontalScrollView scrollView, final boolean isSubmit) {
        gridAdapter = new GridAdapter(this, images);
        gridAdapter.setSelectedPosition(0);
        int size = 0;
        size = images.size() + 1;
        ViewGroup.LayoutParams params = gridview.getLayoutParams();
        final int width = size * (int) (getResources().getDimension(R.dimen.icon_very_small_h) * 9.4f);
        params.width = width;
        gridview.setLayoutParams(params);
        gridview.setColumnWidth((int) (getResources().getDimension(R.dimen.icon_very_small_h) * 9.4f));
        gridview.setStretchMode(GridView.NO_STRETCH);
        gridview.setNumColumns(size);
        gridview.setAdapter(gridAdapter);

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                imageBrower(position, images, isSubmit);
            }
        });

        scrollView.getViewTreeObserver()
                .addOnPreDrawListener(// 绘制完毕
                        new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {
                                scrollView.scrollTo(width,
                                        0);
                                scrollView
                                        .getViewTreeObserver()
                                        .removeOnPreDrawListener(this);
                                return false;
                            }
                        });
    }

    private void imageBrower(int position, List<String> images, boolean isSubmit) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        //List 转换为 String数组
        String[] urls = images.toArray(new String[images.size()]);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        if (isSubmit) {
            intent.putExtra("info", rptJson);
        }
        startActivity(intent);
    }

    //图片下载option
//    public ImageLoader loader;
//    private DisplayImageOptions options;
//
//    private void initLoder() {
//
//        loader = ImageLoader.getInstance();
//        options = new DisplayImageOptions.Builder()
//                .showStubImage(R.drawable.rz_icon_sczp)
//                .showImageForEmptyUri(R.drawable.rz_icon_sczp)
//                .showImageOnFail(R.drawable.rz_icon_sczp).cacheOnDisc().cacheInMemory()
//                .displayer(new RoundedBitmapDisplayer(0))
//                .build();
//    }
//
//    public static void initImageLoader(Context context) {
//        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
//                .threadPriority(Thread.NORM_PRIORITY - 2)
//                .denyCacheImageMultipleSizesInMemory()
//                .discCacheFileNameGenerator(new Md5FileNameGenerator())
//                .tasksProcessingOrder(QueueProcessingType.LIFO)
//                .build();
//        ImageLoader.getInstance().init(config);
//    }

    /**
     * 图片适配器
     */
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater listContainer;
        private int selectedPosition = -1;
        private boolean shape;
        private List<String> images = new ArrayList<>();

        public boolean isShape() {
            return shape;
        }

        public void setShape(boolean shape) {
            this.shape = shape;
        }

        public class ViewHolder {
            public ImageView image;
            public Button bt;
        }

        public GridAdapter(Context context, List<String> images) {
            listContainer = LayoutInflater.from(context);
            this.images = images;
        }

        public int getCount() {
            return images.size();
        }

        public Object getItem(int arg0) {

            return null;
        }

        public long getItemId(int arg0) {

            return 0;
        }

        public void setSelectedPosition(int position) {
            selectedPosition = position;
        }

        public int getSelectedPosition() {
            return selectedPosition;
        }

        /**
         * ListView Item设置
         */
        public View getView(int position, View convertView, ViewGroup parent) {
            final int sign = position;
            // 自定义视图
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                // 获取list_item布局文件的视图

                convertView = listContainer.inflate(
                        R.layout.item_published_grida, null);

                // 获取控件对象
                holder.image = (ImageView) convertView
                        .findViewById(R.id.item_grida_image);
                holder.bt = (Button) convertView
                        .findViewById(R.id.item_grida_bt);
                holder.bt.setVisibility(View.GONE);
                // 设置控件集到convertView
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (!Guard.isNullOrEmpty(images.get(sign))) {
//                QCloudService.asyncDisplayImage(baseActivity, images.get(sign), holder.image);
//                loader.displayImage(images.get(sign), holder.image, options);
//                QCloudService.asyncDisplayImage(baseActivity, images.get(sign), holder.image, true);
                ImageLoader.getInstance().displayImage(images.get(sign), holder.image);
            } else {
                holder.image.setImageResource(R.drawable.rz_icon_sczp);
            }

            return convertView;
        }
    }

    @OnClick(R.id.Lin_order_sub_hackman_phone)
    void setLinPhone() {
        String phone = labPhoneNumber.getText().toString().trim();
        if (!Guard.isNullOrEmpty(phone)) {
            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(nIntent);
        }
    }

    @OnClick(R.id.btnPhoneCall)
    void setBtnPhoneCall() {
        String phone = labPhoneNumber.getText().toString().trim();
        if (!Guard.isNullOrEmpty(phone)) {
            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phone));
            startActivity(nIntent);
        }
    }

    @OnClick(R.id.LinContacts)
    void setLinContacts() {
//        showDialog();
        Intent intent = new Intent(this, UserBeaerInfoActivity.class);
//        intent.putExtra("Type", Type);
        intent.putExtra(ParamKey.ORDER_DETAIL, rptJson);
        startActivity(intent);
    }

    @OnClick({R.id.boxHackman})
    void openHackMan(View v) {
//        showDialog();
        Intent intent = new Intent(this, UserBeaerInfoActivity.class);
//        intent.putExtra("Type", Type);
        intent.putExtra(ParamKey.ORDER_DETAIL, rptJson);
        startActivity(intent);

    }

    //支付给司机
    @OnClick(R.id.btnPay)
    void setbtnPay() {

        HashMap<String,String> map = new HashMap<>();
        map.put("orderId",orderId+"");
        MobclickAgent.onEvent(baseActivity, "myorder_payEMS_button", map);

        getBindStatus();
    }

    ProgressDialog dialog;
    BindStatusInfo bindStatusInfo;

    void getBindStatus() {
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        //TODO:获取绑卡状态
        HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
        BackcardApi.bindStatus(baseActivity, this, pms, new IJsonResult<BindStatusInfo>() {
            @Override
            public void ok(BindStatusInfo json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
//                Tracer.e(TAG,Guard.isNull(json.PnrInfo)+" isNull");
                if (Guard.isNull(json.PnrInfo)) {
                    shipper.isFirstCg = true;
                } else {
                    shipper.isFirstCg = false;
                }
                _dbCache.save(shipper);
                if (!Guard.isNull(json.PnrInfo)) {
                    bindStatusInfo = json;
                    _dbCache.save(bindStatusInfo);

                    if (!shipper.havedPayMethod && shipper.cardid < 0 && bindStatusInfo.PnrCards.size() > 0) {
                        String number = "", cardNum = bindStatusInfo.PnrCards.get(0).CardNo;
                        if (cardNum.length() > 4) {
                            number = cardNum.substring(cardNum.length() - 4, cardNum.length());
                        }
                        String bankName = "";
                        for (int i = 0; i < BankInfoService.getBankData().size(); i++) {
                            if (BankInfoService.getBankData().get(i).getBankcode().equals(json.PnrCards.get(0).BankCode)) {
                                bankName = BankInfoService.getBankData().get(i).getBankName();
                            }
                        }
                        String payment = "使用\t\t" + bankName + "(" + number + ")\t\t" + "付款";
                        shipper.payment = payment;
                        shipper.cardid = bindStatusInfo.PnrCards.get(0).Id;
                        _dbCache.save(shipper);
                    }

                    if (bindStatusInfo.PnrCards.size() <= 0) {
                        payToPlat();
                    } else {
                        Intent intent = new Intent(baseActivity, UserPayToDriverActivity.class);
                        intent.putExtra("action", "freight");
                        intent.putExtra("info", rptJson);
                        startActivity(intent);
                    }
                } else {
                    payToPlat();
                }
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

    void showDialog() {
        final com.orhanobut.dialogplus.ViewHolder vh = new com.orhanobut.dialogplus.ViewHolder(R.layout.dialog_commission_hackman);
        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(vh)
                .setCancelable(false) //点击对话框外部不关闭
                .setGravity(DialogPlus.Gravity.CENTER)
                        //.setOnClickListener(clickListener)
                .setBackgroundColorResourceId(org.csware.ee.R.color.trans)
                .create();

        if (orderEntity == null) {
            dialog.dismiss();
        }
        vh.getView(R.id.btnConfirm).setVisibility(View.GONE);
        vh.getView(R.id.tipMessage).setVisibility(View.GONE);
        Button btnClose = vh.getView(R.id.btnCancel);
        btnClose.setText("关闭");
//        btnClose.setTextAppearance(this,R.style.BlueFillMarginButton);
        btnClose.setBackgroundResource(R.drawable.btn_blue);
        btnClose.setTextColor(getResources().getColor(R.color.white));
        btnClose.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        vh.setText(R.id.ivLevel, (int) orderEntity.Bearer.Rate + "星");
        if (Guard.isNull(orderEntity.BearerUser.CompanyStatus))
            orderEntity.BearerUser.CompanyStatus = -1;
        String SFRZ = (orderEntity.BearerUser.CompanyStatus == 1) ? "企业认证" : "个人认证";
        String PTRZ = (orderEntity.BearerUser.CompanyStatus == 1) ? getString(R.string.lab_certification_hackman_company) : getString(R.string.lab_certification_hackman);
        vh.setText(R.id.labPTRZ, PTRZ + "");
        vh.setText(R.id.labSFRZ, SFRZ + "");
        String Name = "";
        if (!Guard.isNull(orderEntity.BearerCompany)) {
            if (!Guard.isNullOrEmpty(orderEntity.BearerCompany.CompanyName)) {
                Name = orderEntity.BearerCompany.CompanyName;
                vh.getView(R.id.labCPH).setVisibility(View.GONE);
                vh.getView(R.id.LinCarModel).setVisibility(View.GONE);
                vh.setImageResource(R.id.ivPTRZ, R.mipmap.dd_icon_company);
            }
        } else {
            Name = orderEntity.Bearer.Name;
            vh.getView(R.id.labCPH).setVisibility(View.VISIBLE);
            vh.getView(R.id.LinCarModel).setVisibility(View.VISIBLE);
            vh.setImageResource(R.id.ivPTRZ, R.drawable.w_icon_ptrz);
        }

        vh.setText(R.id.labName, Name + "");
//        vh.setText(R.id.labName, orderEntity.Bearer.Name + "");
        vh.setText(R.id.labCPH, orderEntity.Bearer.Plate + "");
        QCloudService.asyncDisplayImage(baseContext, orderEntity.BearerUser.Avatar, (ImageView) vh.getView(R.id.ivHeadPic));
        vh.setText(R.id.labPhoneNo, orderEntity.BearerUser.Mobile + "");
        vh.setText(R.id.labSendAmount, orderEntity.Bearer.OrderAmount + "");
        vh.setText(R.id.labTruckType, orderEntity.Bearer.VehicleType);
        vh.setText(R.id.labTruckLength, orderEntity.Bearer.VehicleLength + "米");
//        vh.setText(R.id.labTruckLength, EnumHelper.getCnName(orderEntity.Bearer.VehicleLength + "", TruckLengthType.class));
        //vh.setText(R.id.labUnitPrice, "-5");

        String toMoney = "";
        if (bidsEntity.Price > 0) {
            toMoney = FormatHelper.toMoney(bidsEntity.Price);
        } else {
            toMoney = FormatHelper.toMoney(orderEntity.DealPrice);
        }

        vh.setText(R.id.labBidPrice, toMoney + getString(R.string.unit_rmb));
        //TODO:零担时才显示表达式
        //getString(R.string.lab_price_style_truck).equals(order.PriceType)||getString(R.string.lab_price_style_rmb_truck).equals(order.PriceType)
        if (orderEntity.PriceType.contains("包车")) {
            //包车价
            String totalMoney = "";
            if (bidsEntity.Price > 0) {
                totalMoney = FormatHelper.toMoney(bidsEntity.Price);
            } else {
                totalMoney = FormatHelper.toMoney(orderEntity.DealPrice);
            }
            vh.setText(R.id.labTotalPrice, totalMoney);
        } else {
            //零担计算价格
            if (orderEntity.GoodsAmount > 0) {
                String totalMoney = "";
                if (bidsEntity.Price > 0) {
                    totalMoney = FormatHelper.toMoney(bidsEntity.Price * Double.valueOf(orderEntity.GoodsAmount));
                } else {
                    totalMoney = FormatHelper.toMoney(orderEntity.DealPrice);
                }
                vh.setText(R.id.labTotalPrice, totalMoney + "");
            }
        }

        vh.getView(R.id.ivPhone).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderEntity.BearerUser.Mobile));
                    startActivity(nIntent);
                }
            }
        });
        vh.getView(R.id.labPhoneNo).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderEntity.BearerUser.Mobile));
                    startActivity(nIntent);
                }
            }
        });

        dialog.show();
    }

    OnClickListener clickComment = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracer.d(TAG, "clickComment");
            HashMap<String,String> map = new HashMap<>();
            map.put("orderId",orderId+"");
            MobclickAgent.onEvent(baseActivity, "myorder_comment", map);
            Intent intent = new Intent(baseActivity, OrderCommentFragmentActivity.class);
            intent.putExtra(ParamKey.ORDER_ID, orderId);
            startActivityForResult(intent, Code.COMMENT.toValue());
        }
    };


    OnClickListener clickReceiveDone = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracer.d(TAG, "clickReceiveDone");
            btnComment.setEnabled(false);
            dialog = Tools.getDialog(baseActivity);
            dialog.setCanceledOnTouchOutside(false);
            OrderApi.promote(baseActivity, baseContext, PromoteActionType.ONWER_DONE, orderId, new IJsonResult() {
                @Override
                public void ok(Return json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    //成功
//                    btnComment.setVisibility(View.GONE);
                    SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isTrackRefresh", true).commit();
                    SharedPreferences pref = getSharedPreferences("isRefresh", MODE_PRIVATE);
                    SharedPreferences.Editor edt = pref.edit();
                    edt.putBoolean("isTrackRefresh", true).commit();
//                    finish();
                    btnComment.setText("评价");
                    btnComment.setEnabled(true);
                    btnComment.setVisibility(View.VISIBLE);
                    btnComment.setOnClickListener(clickComment);
                }

                @Override
                public void error(Return json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    btnComment.setEnabled(true);
                }
            });


        }
    };


    OnClickListener clickLoaded = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracer.d(TAG, "clickLoaded");

            btnComment.setEnabled(false);
            OrderApi.promote(baseActivity, baseContext, PromoteActionType.DEPART, orderId, new IJsonResult() {
                @Override
                public void ok(Return json) {
                    //成功
                    btnComment.setVisibility(View.GONE);
                    SharedPreferences preferences = getSharedPreferences("isTrackRefresh", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putBoolean("isTrackRefresh", true).commit();
                    topBar.hideMenu();
                }

                @Override
                public void error(Return json) {
                    btnComment.setEnabled(true);
                }
            });

        }
    };

    OnClickListener clickPayToPlatform = new OnClickListener() {
        @Override
        public void onClick(View v) {
            Tracer.d(TAG, "clickPayToPlatform");
            topBar.hideMenu();
            payToPlat();
        }
    };

    void payToPlat() {
        Intent intent = new Intent(baseActivity, UserPayToPlatformFragmentActivity.class);
        intent.putExtra(ParamKey.PAY_PRICE, dealPrice);
        intent.putExtra(ParamKey.ORDER_ID, orderId);
        intent.putExtra("info", rptJson);
        intent.putExtra("subject", subject);
        startActivityForResult(intent, Code.PAY_REQUEST.toValue());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Tracer.w(TAG, "requestCode:" + requestCode + "  resultCode:" + resultCode);
        if (resultCode == Code.OK.toValue()) {
//            if (requestCode == Code.PAY_REQUEST.toValue()) {
//                //付款成功
//                labHPayStatus.setText(PaymentStatusType.GIVEN.toCnName());
//                labHPayStatus.setTextColor(getResources().getColor(R.color.green));
//                btnComment.setEnabled(false);
//                btnComment.setText(R.string.ysz);
//            }
            if (requestCode == Code.COMMENT.toValue()) {
                btnComment.setEnabled(false);
                btnComment.setText("已完成");
                btnComment.setBackgroundResource(R.color.face_grey);
                btnComment.setTextColor(getResources().getColor(R.color.white));
//                btnComment.setVisibility(View.GONE);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }


}
