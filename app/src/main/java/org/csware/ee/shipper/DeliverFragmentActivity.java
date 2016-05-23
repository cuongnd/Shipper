package org.csware.ee.shipper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.actionsheet.ActionSheet;
import com.github.jjobes.slidedatetimepicker.SlideDateTimeListener;
import com.github.jjobes.slidedatetimepicker.SlideDateTimePicker;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.AddressApi;
import org.csware.ee.api.OrderApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IASyncQCloudResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.AddressReturn;
import org.csware.ee.model.Code;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.GetIdReturn;
import org.csware.ee.model.PayMethodType;
import org.csware.ee.model.PaymentPointType;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.shipper.adapter.LabelGridAdapter;
import org.csware.ee.shipper.app.App;
import org.csware.ee.utils.Bimp;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.EnumHelper;
import org.csware.ee.utils.FileUtils;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ImageByAndroid;
import org.csware.ee.utils.ImageUtil;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.FlowRadioGroup;
import org.csware.ee.widget.InputMethodLinearLayout;
import org.csware.ee.widget.PhotoPopWindow;
import org.csware.ee.widget.ScrollViewForListView;
import org.csware.ee.widget.crop.Crop;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/10.
 */
public class DeliverFragmentActivity extends FragmentActivityBase implements
        InputMethodLinearLayout.OnSizeChangedListenner {

    final static String TAG = "DeliverFragmentActivity";

    @InjectView(R.id.topBar)
    TopActionBar actionBar;
    @InjectView(R.id.labFrom)
    TextView labFrom;
    @InjectView(R.id.optFrom)
    LinearLayout optFrom;
    @InjectView(R.id.btnFrom)
    ImageButton btnFrom;
    @InjectView(R.id.labTo)
    TextView labTo;
    @InjectView(R.id.optTo)
    LinearLayout optTo;
    @InjectView(R.id.btnTo)
    ImageButton btnTo;
    @InjectView(R.id.labGoodsStyle)
    TextView labGoodsStyle;
    @InjectView(R.id.labAmount)
    TextView labAmount;
    @InjectView(R.id.radZC)
    RadioButton radZC;
    @InjectView(R.id.radLD)
    RadioButton radLD;
    @InjectView(R.id.radTransStyle)
    RadioGroup radTransStyle;
    @InjectView(R.id.labTruckStyle)
    TextView labTruckStyle;
    @InjectView(R.id.optTruckStyle)
    LinearLayout opTruckStyle;
    @InjectView(R.id.labBeginTime)
    TextView labBeginTime;
    @InjectView(R.id.optBeginTime)
    LinearLayout optBeginTime;

//    @InjectView(R.id.boxGoodsAmount)
//    LinearLayout boxGoodsAmount;

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
    TextView labPayPoint;
    @InjectView(R.id.optPayMethod)
    LinearLayout optPayMethod;
    @InjectView(R.id.txtMemo)
    EditText txtMemo;
    @InjectView(R.id.btnPressSay)
    Button btnPressSay;
    @InjectView(R.id.btnPhoto)
    ImageButton btnPhoto;
    @InjectView(R.id.txtName)
    EditText txtName;
    @InjectView(R.id.btnName)
    ImageView btnName;
    @InjectView(R.id.optGoodsStyle)
    LinearLayout optGoodsStyle;
    @InjectView(R.id.txtPhone)
    EditText txtPhone;
    @InjectView(R.id.optAmount)
    LinearLayout optAmount;


    @InjectView(R.id.txtHackmanPhone)
    EditText txtHackmanPhone;

    @InjectView(R.id.btnDeliver)
    Button btnDeliver;
    @InjectView(R.id.selectimg_horizontalScrollView)
    HorizontalScrollView selectimg_horizontalScrollView;
    @InjectView(R.id.noScrollgridview)
    GridView gridview;
    @InjectView(R.id.Lin_select_photo)
    LinearLayout Lin_select_photo;
    @InjectView(R.id.optInsurance)
    LinearLayout optInsurance;
    @InjectView(R.id.labInsurance)
    TextView labInsurance;
    @InjectView(R.id.optHackman)
    ImageView optHackman;
    @InjectView(R.id.chkBoxInsurance)
    CheckBox chkBoxInsurance;
    @InjectView(R.id.txt_insurance_protocol)
    TextView txtInsuranceProtocol;
    @InjectView(R.id.Lin_protocol)
    LinearLayout LinProtocol;
    @InjectView(R.id.Lin_hackman)
    LinearLayout LinHackman;
    @InjectView(R.id.scroll_collection)
    ScrollView scrollCollection;

    @InjectView(R.id.LinShowMore)
    LinearLayout LinShowMore;
    @InjectView(R.id.LinOptional)
    LinearLayout LinOptional;
    @InjectView(R.id.Lin_AddHackman)
    LinearLayout Lin_AddHackman;
    @InjectView(R.id.switchHackman)
    Switch switchHackman;
    @InjectView(R.id.listViewHackman)
    ScrollViewForListView listViewHackman;

    ContactListReturn model;
    CommonAdapter<ContactListReturn.ContactsEntity> adapter;
    private GridAdapter gridAdapter;
    String _tmpInsurance="";
    DbCache _dbCache;
    DeliverInfo _info;
    LayoutInflater _layoutInflater;
    SimpleDateFormat cnDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH时");
    int minDate = ParamTool.getTimeSeconds(new Date());
    ChinaAreaHelper ah;
    long id;
    public static Activity activity;
    private float dp;
    AlertDialog alertDialog;
    private CompoundButton.OnCheckedChangeListener chkBoxlisener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                _info.insurance = _tmpInsurance;
                _dbCache.save(_info);
                labInsurance.setText("免费赠送一份5万保险");
            }else {
                if (LinProtocol.getVisibility()==View.VISIBLE) {
                    showUpdataDialog();
                }
            }
        }
    };
    CompoundButton.OnCheckedChangeListener hackAddlisener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked){
                listViewHackman.setVisibility(View.VISIBLE);
                Lin_AddHackman.setVisibility(View.VISIBLE);
                addHackman();
            }else {
                listViewHackman.setVisibility(View.GONE);
                Lin_AddHackman.setVisibility(View.GONE);
            }
        }
    };

    protected void showUpdataDialog() {
        try {
            AlertDialog.Builder builer = new AlertDialog.Builder(this);
            builer.setTitle("不接受本协议您将不能获得免费赠送的保险");
//            builer.setMessage("");

            builer.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    _info.insurance = "";
                    labInsurance.setText(_info.insurance);
                    _dbCache.save(_info);
                }
            });
            // 当点取消按钮时进行登录
            builer.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
//                    LoginMain();
                    chkBoxInsurance.setChecked(true);
                }
            });
            alertDialog = builer.create();
            alertDialog.show();
            alertDialog.setCanceledOnTouchOutside(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliver_fragment_activity);
        ButterKnife.inject(this);
        chkBoxInsurance.setOnCheckedChangeListener(chkBoxlisener);
        switchHackman.setOnCheckedChangeListener(hackAddlisener);
        dp = getResources().getDimension(R.dimen.icon_very_small_h);
        activity = this;
        labFrom.setFocusable(true);
        labFrom.setFocusableInTouchMode(true);
        labFrom.requestFocus();//将控件初始化到顶部
        _layoutInflater = LayoutInflater.from(this);
        OrderApi.getId(baseActivity, this, new IJsonResult<GetIdReturn>() {
            @Override
            public void ok(GetIdReturn json) {
                id = json.Id;
            }

            @Override
            public void error(Return json) {
                if (!Guard.isNull(json)) {
                    if (!Guard.isNullOrEmpty(json.Message)) {
                        if (json.Message.equals("货主状态错误")) {  //toastSlow("订单号获取失败,请稍候重试");
                            finish();
                            startActivity(new Intent(DeliverFragmentActivity.this, UserAuthActivity.class));
                        }
                    }
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        init();

    }

    void init() {
        _dbCache = new DbCache(baseActivity);
        ah = new ChinaAreaHelper(baseContext);
        model = _dbCache.GetObject(ContactListReturn.class);
        if (model==null){
            model = new ContactListReturn();
        }
        if (model.Contacts == null){
            model.Contacts = new ArrayList<>();
        }
        adapter = new CommonAdapter<ContactListReturn.ContactsEntity>(this,model.Contacts,R.layout.list_contact_item) {
            @Override
            public void convert(org.csware.ee.view.ViewHolder helper, ContactListReturn.ContactsEntity item, final int position) {
                helper.getView(R.id.LinContacts).setVisibility(View.VISIBLE);
                helper.getView(R.id.LinMailList).setVisibility(View.GONE);
                String Name = "";
                if (!Guard.isNull(item.BearerCompany)){
                    if (!Guard.isNullOrEmpty(item.BearerCompany.CompanyName)){
                        Name = item.BearerCompany.CompanyName;
                        helper.getView(R.id.txtPlate).setVisibility(View.GONE);
                    }
                }else {
                    Name = item.Name;
                    helper.getView(R.id.txtPlate).setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.txtConComp, Name);
                helper.setText(R.id.txtPlate, item.Mobile);
//                helper.getView(R.id.imgPhoneCall).setVisibility(View.GONE);
                helper.setImageResource(R.id.imgPhoneCall, R.mipmap.fh_zdlxr_deleted);
                helper.setRating(R.id.rate_rating,(float)item.Rate);
//                if (!Guard.isNull(item.bitmap)) {
//                    helper.setImageBitmap(R.id.imgMailHead, item.bitmap);
//                }
                if (!Guard.isNullOrEmpty(item.Avatar)){
                    QCloudService.asyncDisplayImage(baseActivity, item.Avatar, (ImageView) helper.getView(R.id.imgGameHead));
                }else {
                    helper.setImageResource(R.id.imgGameHead,R.drawable.w_icon_tjzp);
                }
                helper.getView(R.id.imgPhoneCall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        model.Contacts.remove(position);
                        _dbCache.save(model);
                        notifyDataSetChanged();
                    }
                });
            }

        };
        listViewHackman.setAdapter(adapter);
        try {
            _info = _dbCache.GetObject(DeliverInfo.class);
            if (_info == null) {
                Tracer.e(TAG, "NULL IFON");
                _info = new DeliverInfo();
            }
        } catch (Exception e) {
            Tracer.e(TAG, e.getMessage());
            //序列化错，重新创建对象
            _info = new DeliverInfo();
        }



        if (!Guard.isNullOrEmpty(_info.fromId)) {
            String[] names = ah.getName(_info.fromId);
            labFrom.setText(ParamTool.join(names, ",") + "\n" + _info.fromDetail);
        }
        if (!Guard.isNullOrEmpty(_info.toId)) {
            String[] names = ah.getName(_info.toId);
            labTo.setText(ParamTool.join(names, ",") + "\n" + _info.toDetail);
        }

        if (!Guard.isNullOrEmpty(_info.goodsStyle)) {
            labGoodsStyle.setText(_info.goodsStyle);
            labGoodsStyle.setTextAppearance(baseActivity, R.style.EdgeOnFont);
        }
        if (!Guard.isNullOrEmpty(_info.goodsUnit) && !Guard.isNullOrEmpty(_info.amount)) {
            labAmount.setText(_info.amount + "  " + _info.goodsUnit);
        }

        if (getString(R.string.lab_carload).equals(_info.transStyle)) {
            radLD.setChecked(true);

            //boxGoodsAmount.setVisibility(View.VISIBLE);
        } else {
            _info.transStyle = getString(R.string.lab_vehicle);
            radZC.setChecked(true);
            //boxGoodsAmount.setVisibility(View.GONE);
        }

        if (!Guard.isNullOrEmpty(_info.truckStyle)) {
//            labTruckStyle.setText(_info.truckStyle + "    " + EnumHelper.getCnName(_info.truckLength, TruckLengthType.class));
            labTruckStyle.setText(_info.truckStyle + "    " +_info.truckLength + "米");
            labTruckStyle.setTextAppearance(baseActivity, R.style.EdgeOnFont);
        }

        if (_info.beginTime > 0) {
            labBeginTime.setText(ParamTool.fromTimeSeconds(_info.beginTime, cnDateFormat));
        }
//        if (_info.endTime > 0) {
//            labEndTime.setText(ParamTool.fromTimeSeconds(_info.endTime, cnDateFormat));
//        }

        if (!Guard.isNullOrEmpty(_info.priceStyle) && _info.price > 0) {
            String unit = _info.priceStyle;
            if (unit.contains("包车")) {
                unit = "元/" + _info.priceStyle;
            }
            labPrice.setText(_info.price + " " + unit);
            labPrice.setTextAppearance(baseActivity, R.style.EdgeOnFont);
        } else if (_info.price <= 0) {
            labPrice.setText("");
        }

        if (!Guard.isNullOrEmpty(_info.invoiceStyle)) {
            labInvoice.setText(_info.invoiceStyle);
            labInvoice.setTextAppearance(baseActivity, R.style.EdgeOnFont);
        }

        if (_info.payPoint >= 0) {
            labPayPoint.setText(EnumHelper.getCnName(_info.payMethod, PayMethodType.class) + "   " + EnumHelper.getCnName(_info.payPoint, PaymentPointType.class));
            labPayPoint.setTextAppearance(baseActivity, R.style.EdgeOnFont);
        }

        if (!Guard.isNullOrEmpty(_info.memo)) {
            txtMemo.setText(_info.memo);
        }

        if (!Guard.isNullOrEmpty(_info.name)) {
            txtName.setText(_info.name);
        }

        if (!Guard.isNullOrEmpty(_info.phone)) {
            txtPhone.setText(_info.phone + "");
        }
        if (!Guard.isNullOrEmpty(_info.hackmanPhone)) {
            txtHackmanPhone.setText(_info.hackmanPhone);
        }
        _tmpInsurance = _info.insurance;
        if (!Guard.isNullOrEmpty(_info.insurance)) {
            labInsurance.setText("免费赠送一份5万保险");
            LinProtocol.setVisibility(View.VISIBLE);
            chkBoxInsurance.setChecked(true);
        } else {
            LinProtocol.setVisibility(View.GONE);
            labInsurance.setText("");
            chkBoxInsurance.setChecked(false);
        }

        /**配送方式变更*/
        radTransStyle.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton chked = (RadioButton) group.findViewById(checkedId);
                //Tracer.w(TAG, chked.getText() + "");
                _info.transStyle = chked.getText().toString();
                if (getString(R.string.lab_carload).equals(_info.transStyle)) {
                    //可以有方和吨
                    resetPrice(1);
                } else {
                    //包车价
                    resetPrice(0);
                }
                _dbCache.save(_info);
            }
        });
        btnDeliver.setClickable(true);
    }

    public List<Bitmap> bmp = new ArrayList<Bitmap>();
    public ArrayList<String> drr = new ArrayList<String>();
    public ArrayList<String> netdrr = new ArrayList<String>();

    @OnClick(R.id.optHackman)
    void setOptHackman() {
        SharedPreferences preferences = getSharedPreferences("Contact", MODE_PRIVATE); //intent.putExtra("action", "select");
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("action", "select").commit();
        Intent intent = new Intent(baseActivity, UserFriendFragmentActivity.class);
        startActivity(intent);
    }
    /**
     * 指定司机
     * */
    @OnClick(R.id.Lin_AddHackman)
    void setLin_AddHackman(){
        addHackman();
    }

    void addHackman(){
        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "ship_assign");
        Intent intent = new Intent(this,MailListActivity.class);
        intent.putExtra("isMail",false);
        intent.putExtra("isShow", true);
        startActivity(intent);
    }

    @OnClick(R.id.LinShowMore)
    void setLinShowMore(){
        LinShowMore.setVisibility(View.GONE);
        LinOptional.setVisibility(View.VISIBLE);
    }

    /**
     * 保险协议
     * */
    @OnClick(R.id.txt_insurance_protocol)
    void setTxtInsuranceProtocol(){
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", API.INSURANCE_PAPER);
        startActivity(intent);
    }
    /**
     * 图片选择
     */
    @OnClick(R.id.Lin_select_photo)
    void setLin_select_photo(View v) {
//        Log.e(TAG,netdrr.size()+"");
        if (netdrr.size() < 3) {
            new PhotoPopWindow(this, v);
        }else {
            toastFast("只能上传3张图片哦，亲");
        }
    }

    /**
     * 图片适配器
     */
    public class GridAdapter extends BaseAdapter {
        private LayoutInflater listContainer;
        private int selectedPosition = -1;
        private boolean shape;

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

        public GridAdapter(Context context) {
            listContainer = LayoutInflater.from(context);
        }

        public int getCount() {
            if (bmp.size() < 6) {
                return bmp.size() + 1;
            } else {
                return bmp.size();
            }
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
                // 设置控件集到convertView
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (position == bmp.size()) {
//                holder.image.setImageBitmap(BitmapFactory.decodeResource(
//                        getResources(), R.drawable.icon_addpic_unfocused));
                holder.bt.setVisibility(View.GONE);
                if (position == 6) {
                    holder.image.setVisibility(View.GONE);
                }
            } else {
                holder.image.setImageBitmap(bmp.get(position));
                holder.bt.setOnClickListener(new View.OnClickListener() {

                    public void onClick(View v) {
                        PhotoActivity.bitmap.remove(sign);
                        bmp.get(sign).recycle();
                        bmp.remove(sign);
                        drr.remove(sign);
                        QCloudService.DeleteFile(DeliverFragmentActivity.this, netdrr.get(sign));
                        netdrr.remove(sign);
                        gridviewInit();
                    }
                });
            }

            return convertView;
        }
    }

    public void gridviewInit() {
        gridAdapter = new GridAdapter(this);
        gridAdapter.setSelectedPosition(0);
        int size = 0;
        if (bmp.size() < 6) {
            size = bmp.size() + 1;
        } else {
            size = bmp.size();
        }
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
                if (position == bmp.size()) {
//                    String sdcardState = Environment.getExternalStorageState();
//                    if (Environment.MEDIA_MOUNTED.equals(sdcardState)) {
//                        new PopupWindows(DeliverFragmentActivity.this, gridview);
//                    } else {
//                        Toast.makeText(getApplicationContext(), "sdcard已拔出，不能选择照片",
//                                Toast.LENGTH_SHORT).show();
//                    }
                } else {
                    Intent intent = new Intent(DeliverFragmentActivity.this,
                            PhotoActivity.class);

                    intent.putExtra("ID", position);
                    startActivity(intent);
                }
            }
        });

        selectimg_horizontalScrollView.getViewTreeObserver()
                .addOnPreDrawListener(// 绘制完毕
                        new ViewTreeObserver.OnPreDrawListener() {
                            public boolean onPreDraw() {
                                selectimg_horizontalScrollView.scrollTo(width,
                                        0);
                                selectimg_horizontalScrollView
                                        .getViewTreeObserver()
                                        .removeOnPreDrawListener(this);
                                return false;
                            }
                        });
    }

    /**
     * 价格重置 ，当配送方式改变时价格重置  0时为包车价
     */
    void resetPrice(int style) {
        _info.price = 0;
        if (style == 0) {
            _info.priceStyle = getString(R.string.lab_price_style_truck);
        }
        labPrice.setText("");
        //_dbCache.save(_info);
    }


    /**
     * 发布订单
     */
    int pros;

    @OnClick({R.id.btnDeliver})
    void clickDeliver(View v) {

        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "ship_first_step");

        if (Guard.isNullOrEmpty(id + "")) {
            toastFast("没有获取到订单ID，请稍候重试");
        }

        if (Guard.isNullOrEmpty(_info.fromId) || Guard.isNullOrEmpty(_info.toId)) {
            toastFast("发货与收货地址为必填项");
            return;
        }
        if (Guard.isNullOrEmpty(_info.goodsStyle)) {
            toastFast("货物类型为必填项");
            return;
        }

        if (Guard.isNullOrEmpty(_info.amount)) {
            toastFast("货物数量为必填项");
            return;
        }
//        if (Guard.isNullOrEmpty(_info.transStyle)) {
//            toastFast("请选择一种配送方式");
//            return;
//        }
        if (Guard.isNullOrEmpty(_info.truckStyle) && Guard.isNullOrEmpty(_info.truckLength)) {
            toastFast("车型车长为必选项");
            return;
        }
        if (minDate > _info.beginTime) {
            toastFast("发货时间至少在当前时间之后");
            return;
        }
        if (_info.endTime>0) {
            if (_info.endTime < _info.beginTime) {
                toastFast("收货时间不能早于发货时间");
                return;
            }
        }
//        if (_info.payMethod == 0 || _info.payPoint < 0) {
//            toastFast("付款方式为必填项");
//            return;
//        }
//        if (Guard.isNullOrEmpty(txtName.getText().toString().trim())) {
//            toastFast("请填写收货人");
//            return;
//        }
//        if (Guard.isNullOrEmpty(txtPhone.getText().toString())) {
//            toastFast("请填写收货人联系方式");
//            return;
//        }
            String beaerIds = "";
        if (model.Contacts.size()>0){
            StringBuffer buffer = new StringBuffer();
            for (int i=0;i<model.Contacts.size();i++){
                buffer.append(model.Contacts.get(i).UserId);
                if (i!=(model.Contacts.size()-1)){
                    buffer.append(",");
                }
            }
            beaerIds = buffer.toString();
            if (!Guard.isNullOrEmpty(beaerIds)){
                _info.bearerids = beaerIds;
                _dbCache.save(_info);
            }
        }
        Tracer.e(TAG,_info.bearerids+" str beaerIds"+beaerIds+" size:"+model.Contacts.size());
        _info.memo = txtMemo.getText().toString();
        _info.name = txtName.getText().toString();
        _info.phone = txtPhone.getText().toString();
        _info.hackmanPhone = txtHackmanPhone.getText().toString();
        _dbCache.save(_info);
        Intent intent = new Intent(this, DeliverFragmentConfirmActivity.class);
        intent.putExtra("id", id);
        intent.putExtra("map", new HashMap<String, String>());
        intent.putExtra("picList", netdrr);
        intent.putExtra("action", "deliver");
        startActivity(intent);
        btnDeliver.setClickable(false);
        //btnDeliver.setVisibility(View.INVISIBLE);

    }


    @OnClick({R.id.btnFrom, R.id.btnTo})
    void openLocationMap(View v) {
        int fromOrTo = 0;
        if (v.getId() == R.id.btnFrom) fromOrTo = 1;
        if (v.getId() == R.id.btnTo) fromOrTo = 2;
        Tracer.d(TAG, "X:" + fromOrTo);

    }

    //地址选择与处理开始
//    private CommonAdapter<AddressReturn.AddressesEntity> adapter;
    int type = 3;
    SwipeRefreshLayout swipeLayout;
    List<AddressReturn.AddressesEntity> addressList = new ArrayList<>();

    int addressPosition = 0;
    ActionSheet.ActionSheetListener actionSheetListener;

    /**
     * 异步获取数据缓存且更新UI
     */
    void asyncLoadAddress() {
        //开始异步同步数据
        AddressApi.list(baseActivity, baseContext, type, new IJsonResult<AddressReturn>() {
            @Override
            public void ok(AddressReturn json) {
                swipeLayout.setRefreshing(false);
                addressList.clear();
                addressList.addAll(json.Addresses);
//                adapter.notifyDataSetChanged();
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    /**
     * 地址选择
     */
    @OnClick({R.id.optFrom, R.id.optTo})
    void clickelectAddress(final View v) {
        if (v.getId() == R.id.optFrom) {
            _isFromArea = true;
            type = 1;
        } else {
            _isFromArea = false;
            type = 2;
        }
        Intent intent = new Intent(this, DeliverSelectorAddressActivity.class);
        intent.putExtra("isFrom", _isFromArea);
        intent.putExtra("type", type);
        startActivityForResult(intent, 100);

    }

    String _tmpArea;
    TextView _tmpLabArea;
    String _tmpCode;
    boolean _isFromArea;
    ProgressDialog dialog;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == Code.OK.toValue()) {
            if (requestCode == Code.AREA.toValue()) {
                if (_tmpLabArea != null) {
                    String[] areas = data.getStringArrayExtra(ParamKey.AREA_ARRAY);
                    _tmpArea = TextUtils.join(",", areas);
                    _tmpLabArea.setText(_tmpArea);
                    _tmpCode = data.getStringExtra(ParamKey.AREA_CODE);
                    Tracer.d(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode + "   Code:" + data.getExtras().getString(ParamKey.AREA_CODE) + "result:" + GsonHelper.toJson(areas));
                }
            }
        }

        if (resultCode == 111) {
            if (_tmpLabArea != null) {
                String[] areas = data.getStringArrayExtra(ParamKey.AREA_ARRAY);
                _tmpArea = TextUtils.join(",", areas);
                _tmpLabArea.setText(_tmpArea);
                _tmpCode = data.getStringExtra(ParamKey.AREA_CODE);
                Tracer.d(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode + "   Code:" + data.getExtras().getString(ParamKey.AREA_CODE) + "result:" + GsonHelper.toJson(areas));
            }
        }

        if (requestCode == Crop.REQUEST_PICK && resultCode == RESULT_OK) {
            beginCrop(data.getData());
        } else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        if (requestCode == Code.CAMERA_REQUEST.toValue() && resultCode == RESULT_OK) {
            Uri uri = ImageByAndroid.getUri();
            Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
            Crop.of(uri, destination).asSquare().start(this);
        }
        if (requestCode == 10001 && resultCode == RESULT_OK) {
            if (data != null) {
                Bitmap bitmap = ImageByAndroid.getmInsertPicture();
                setPicUpload(bitmap);
            }
        }
        if (requestCode == UserAuthActivity.PHOTO_REQUEST_CAMERA && resultCode == Activity.RESULT_OK) {
            //这里拍摄图片和截取后的图片文件都写入了同一个文件，photo.jpg
//            ImageByAndroid.startPhotoZoom(Uri.fromFile(tempFile), this, Uri.fromFile(tempFile), false);
            beginCrop(Uri.fromFile(UserAuthActivity.tempFile));
//            Bitmap bitmap = ImageByAndroid.decodeUriAsBitmap(this, Uri.fromFile(UserAuthActivity.tempFile));//decode bitmap
//            setPicUpload(bitmap);
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setPicUpload(Bitmap bitmap) {
        try {
            SimpleDateFormat sDateFormat = new SimpleDateFormat(
                    "yyyyMMddhhmmss");
            String address = sDateFormat.format(new Date());
            if (!FileUtils.isFileExist("")) {
                FileUtils.createSDDir("");
            }
            ImageByAndroid.saveBitmap("Shipper_crop", address, bitmap);
            String Path = FileUtils.SDPATH + "Shipper_crop" + address + ".JPEG";
            String _tmpPath = FileUtils.SDPATH + "Shipper_compress" + address + ".JPEG";
            ImageUtil.getimage(Path, _tmpPath, ImageByAndroid.avatorpath, 400, 400);
            drr.add(_tmpPath);
            if (baseActivity!=null) {
                dialog = Tools.getDialog(baseActivity);
                dialog.setCanceledOnTouchOutside(false);
            }
            QCloudService.asyncUploadFile(asyncNotify, baseActivity, _tmpPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        PhotoActivity.bitmap.add(bitmap);
        bitmap = Bimp.createFramedPhoto(480, 480, bitmap,
                (int) (dp * 1.6f));
        bmp.add(bitmap);
        gridviewInit();
    }

    IASyncQCloudResult asyncNotify = new IASyncQCloudResult() {
        @Override
        public void notify(boolean result, final String urlOrPath) {
            if (result) {
//                UserApi.edit(baseContext, urlOrPath, new IJsonResult() {
//                    @Override
//                    public void ok(Return json) {
                        if (dialog != null) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        netdrr.add(urlOrPath);
////                        toastFast("上传成功");
//                    }
//
//                    @Override
//                    public void error(Return json) {
//                        if (dialog != null) {
//                            if (dialog.isShowing()) {
//                                dialog.dismiss();
//                            }
//                        }
//                    }
//                });
            } else {
                toastFast("上传失败");
            }
        }
    };

    private void beginCrop(Uri source) {
        Uri destination = Uri.fromFile(new File(getCacheDir(), "cropped"));
//        Crop.of(source, destination).start(this);
        Crop.of(source, destination).asSquare().start(this);
    }

    private void handleCrop(int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
//            Log.e(TAG, "resultCODE:" + resultCode);
            Bitmap bitmap = ImageByAndroid.getBitmapFromUri(this, Crop.getOutput(result));
            ImageByAndroid.setmInsertPicture(bitmap);
            Intent intent = new Intent(this, RotationActivity.class);
            startActivityForResult(intent, 10001);

        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(result).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    protected void onDestroy() {

//        FileUtils.deleteDir(FileUtils.SDPATH);
//        FileUtils.deleteDir(FileUtils.SDPATH1);
        // 清理图片缓存
        for (int i = 0; i < bmp.size(); i++) {
            bmp.get(i).recycle();
        }
        for (int i = 0; i < PhotoActivity.bitmap.size(); i++) {
            PhotoActivity.bitmap.get(i).recycle();
        }
        model = new ContactListReturn();
        _dbCache.save(model);
//        _info.insurance = "";
//        if (!Tracer.isDebug()|| !App.getSaveCache()) {
        if (Tracer.isRelease()) {
            _info = new DeliverInfo();
            _dbCache.save(_info);
        }
//        }
        if (alertDialog!=null){
            alertDialog.dismiss();
        }
        if (dialog!=null){
            dialog.dismiss();
        }
        PhotoActivity.bitmap.clear();
        bmp.clear();
        drr.clear();
        netdrr.clear();
        super.onDestroy();
    }

    /**
     * 关闭软件盘
     */
    void closeInput() {
        View view = getWindow().peekDecorView();
        if (view != null) {
            InputMethodManager inputmanger = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputmanger.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    /**
     * 保险
     */
    @OnClick(R.id.optInsurance)
    void setOptInsurance() {
        Intent intent = new Intent(this, InsuranceActivity.class);
        startActivity(intent);
    }

    /**
     * 货物类选择
     */
    @OnClick({R.id.optGoodsStyle})
    void clickSelectGoodsStyle(View v) {
        String value = _info.goodsStyle;
        //Tracer.d(this.toString(), "id=" + v.getId() + "  :" + value + "  :" + Guard.isNullOrEmpty(value));
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                TextView tv = (TextView) view.findViewById(R.id.text_view);
                labGoodsStyle.setText(tv.getText());
                labGoodsStyle.setTextAppearance(baseActivity, R.style.EdgeOnFont);
                _info.goodsStyle = (tv.getText().toString());
                _dbCache.save(_info);
                dialog.dismiss();
            }
        };
        LabelGridAdapter adapter = new LabelGridAdapter(baseActivity, R.array.goods_style, value);
        GridHolder holder = new GridHolder(3);
        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header)
                .setFooter(R.layout.dialog_footer_empty)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .create();
        dialog.setHeaderText(R.id.dialogTitle, getString(R.string.lab_goods_style));//必须创建后更改
        dialog.show();
    }

    /**
     * 货物数量
     */
    @OnClick({R.id.optAmount})
    void clickPopGoodsAmount(View v) {

        final ViewHolder holder = new ViewHolder(R.layout.dialog_goods_amount);
        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header_confirm)
                .setFooter(R.layout.dialog_footer_empty)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .create();

        //初始值设定
        final View contentView = dialog.getHolderView();
        View cv = "吨".equals(_info.goodsUnit) ? holder.getView(R.id.radTon) : holder.getView(R.id.radCube);
        RadioButton rd = (RadioButton) cv;
        rd.setChecked(true);
        final EditText txtA = (EditText) holder.getView(R.id.txtAmount);
        //限制输入小数点2位
        Utils.setPricePoint(txtA);
        RadioGroup rg = (RadioGroup) contentView.findViewById(R.id.radGroup);
        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton chked = (RadioButton) group.findViewById(checkedId);
                String unit = null;
                unit = chked.getText().toString();
                if (!Guard.isNull(unit)){
                    txtA.setText("");
                    if (unit.equals("件")) {
                        txtA.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_SIGNED);
                        txtA.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
                    }else {
                        txtA.setInputType(InputType.TYPE_CLASS_NUMBER|InputType.TYPE_NUMBER_FLAG_DECIMAL);
                        Utils.setPricePoint(txtA);
                    }
                }
            }
        });

//        txtA.setText(Guard.isNullOrEmpty(_info.amount) ? "" : (_info.amount + ""));

        //必须创建后更改
        dialog.setHeaderText(R.id.dialogTitle, getString(R.string.lab_goods_amount));
        View header = dialog.getHeaderView();
        Button dialogCancel = (Button) header.findViewById(R.id.dialogCancel);
        Button btnConfirm = (Button) header.findViewById(R.id.dialogConfirm);
        dialogCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String unit = null;
                RadioGroup rg = (RadioGroup) contentView.findViewById(R.id.radGroup);
                if (rg != null) {
                    RadioButton rb = (RadioButton) contentView.findViewById(rg.getCheckedRadioButtonId());
                    unit = rb.getText().toString();
                }
                EditText et = (EditText) contentView.findViewById(R.id.txtAmount);
                if (Guard.isNull(unit)){
                    toastSlow("请输入正确的单位");
                    return;
                }
                String amount = et.getText().toString();
                //Tracer.e("value", "amount:" + amount + "    rad:" + unit);
                if (Guard.isNullOrEmpty(amount)) {
                    toastSlow("请输入正确的数量");
                    return;
                }
                if (!Guard.isNullOrEmpty(amount)) {
                    boolean isWrong = true;
                    if (unit.contains("件")){
                        if (Integer.valueOf(amount)>0){
                            isWrong = false;
                        }else {
                            isWrong = true;
                        }
                    }else {
                        if (Double.valueOf(amount)>0){
                            isWrong = false;
                        }else {
                            isWrong = true;
                        }
                    }
                    if (isWrong) {
                        toastSlow("请输入正确的数量");
                        return;
                    }
                }
                _info.amount = (amount);
                _info.goodsUnit = (unit);
                _dbCache.save(_info);
                labAmount.setText(amount + " " + unit);
                closeInput();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 车型车长选择
     */
    @OnClick({R.id.optTruckStyle})
    void clickOpenTruckStyle(View v) {
        startActivity(new Intent(this,AddCarModelFragmentActivity.class));
//        final String[] arrTruckStyle = baseActivity.getResources().getStringArray(R.array.truck_style);
//        final String[] arrTruckLength = EnumHelper.getCnNameArray(TruckLengthType.class); //baseActivity.getResources().getStringArray(R.array.truck_length);
//
//        final ViewHolder holder = new ViewHolder(R.layout.dialog_truck_style);
//        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
//                .setContentHolder(holder)
//                .setHeader(R.layout.dialog_header_confirm)
//                .setFooter(R.layout.dialog_footer_empty)
//                .setCancelable(true)
//                .setGravity(DialogPlus.Gravity.BOTTOM)
//                .create();
//
//        //必须创建后更改
//        final View contentView = dialog.getHolderView();
//
//        //初始值设定
//        final FlowRadioGroup styleGroup = (FlowRadioGroup) contentView.findViewById(R.id.radGroupTruckStyle);
//        final FlowRadioGroup lengthGroup = (FlowRadioGroup) contentView.findViewById(R.id.radGroupTruckLength);
//
////        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  // , 1是可选写的
////        lp.setMargins(100, 200, 300, 400);
////        styleGroup.setLayoutParams(lp);
//
//        final View itemView = _layoutInflater.inflate(R.layout.radio_item, null);
//        Tracer.w("itemVIew", "NULL =" + itemView);
//
////        RelativeLayout.LayoutParams titleParams = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
////        titleParams.addRule(RelativeLayout.CENTER_IN_PARENT, -1);
////        //titleParams.pa(20, 10, 20, 10);
//
//        int id = 1;
//        for (int i = 0; i < arrTruckStyle.length; i++) {
//            id = id + 1;
//            RadioButton rd = getRadioItem();
//            rd.setText(arrTruckStyle[i]);
//            rd.setId(id);
//            if (arrTruckStyle[i].equals(_info.truckStyle)) rd.setChecked(true);
//            styleGroup.addView(rd);
//        }
//
//        //lengthGroup.removeAllViews();
//        for (int i = 0; i < arrTruckLength.length; i++) {
//            id = id + 1;
//            RadioButton rd = getRadioItem();
//            rd.setText(arrTruckLength[i]);
//            rd.setId(id);
//            if (arrTruckLength[i].equals(_info.truckLength)) rd.setChecked(true);
//            lengthGroup.addView(rd);
//        }
//
//        dialog.setHeaderText(R.id.dialogTitle, getString(R.string.lab_truck_style));
//        View header = dialog.getHeaderView();
//        Button btnConfirm = (Button) header.findViewById(R.id.dialogConfirm);
//        btnConfirm.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //styleGroup.getCheckedRadioButtonId();
//                int styleId = styleGroup.getCheckedRadioButtonId();
//                int lengthId = lengthGroup.getCheckedRadioButtonId();
//                Tracer.i("Test", "styleId=" + styleId + "  lengthId=" + lengthId);
//                if (styleId == -1 || lengthId == -1) {
//                    toastSlow("请选定车型和车长");
//                    return;
//                }
//                _info.truckStyle = ((RadioButton) contentView.findViewById(styleId)).getText().toString();
//                _info.truckLength = EnumHelper.getName(((RadioButton) contentView.findViewById(lengthId)).getText().toString(), TruckLengthType.class);
//                _dbCache.save(_info);
//                String len = _info.truckLength + "";
//                if (len.equals("0")) {
//                    len = "不限";
//                } else {
//                    len = _info.truckLength + "米";
//                }
//                labTruckStyle.setText(_info.truckStyle + "    " + len);
//                labTruckStyle.setTextAppearance(baseActivity, R.style.EdgeOnFont);
//                dialog.dismiss();
//            }
//        });
//        dialog.show();
    }

    /**
     * 返回FlowRadioButton模板
     */
    RadioButton getRadioItem() {
        RadioButton btn = (RadioButton) LayoutInflater.from(baseActivity).inflate(R.layout.radio_item, null);

//        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1);  // , 1是可选写的
//        lp.setMargins(100, 200, 30, 40);
//        btn.setLayoutParams(lp);

        return btn;
    }

    //SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd yyyy hh:mm aa");
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    boolean isBegionTime;

    /**
     * 开始与结束时间
     */
    @OnClick({R.id.optBeginTime, R.id.optEndTime})
    void openDateTimeDialog(View v) {
        //TextView tv = (TextView)v;
        Tracer.d("Id", "optBeginTime:" + (v.getId() == R.id.optBeginTime) + "    optEndTime:" + (v.getId() == R.id.optEndTime));
        isBegionTime = v.getId() == R.id.optBeginTime;
        Date date = null;
        if (isBegionTime && _info.beginTime > 0) {
            date = ParamTool.toDate(_info.beginTime);
        }
        if (!isBegionTime && _info.endTime > 0) {
            date = ParamTool.toDate(_info.endTime);
        }
        if (date == null) date = new Date();
        new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(date)
                        //.setMinDate(minDate)
                        //.setMaxDate(maxDate)
                .setIs24HourTime(true)
                        //.setTheme(SlideDateTimePicker.HOLO_DARK)
                        //.setIndicatorColor(Color.parseColor("#990000"))
                .build()
                .show();
    }

    private SlideDateTimeListener listener = new SlideDateTimeListener() {

        @Override
        public void onDateTimeSet(Date date) {
            //Tracer.d("dateSet", "   " + dateFormat.format(date));

            if (isBegionTime) {
                _info.beginTime = ParamTool.getTimeSeconds(date);
                labBeginTime.setText(ParamTool.fromTimeSeconds(_info.beginTime, cnDateFormat));
            } else {
                _info.endTime = ParamTool.getTimeSeconds(date);
                labEndTime.setText(ParamTool.fromTimeSeconds(_info.endTime, cnDateFormat));
            }
            if (minDate > _info.beginTime) {
                toastFast("发货时间至少在当前时间之后");
                return;
            }
            if (_info.endTime>0) {
                if (_info.endTime < _info.beginTime) {
                    toastFast("到货时间不能小于发货时间");
                    return;
                }
            }

            _dbCache.save(_info);
        }

        // Optional cancel listener
        @Override
        public void onDateTimeCancel() {
            Tracer.d("canncel", "   " + "cancel");
        }
    };


    /**
     * 报价方式与价格
     */
    @OnClick({R.id.optPrice})
    void openPriceDialog(View v) {

        final ViewHolder holder = new ViewHolder(R.layout.dialog_goods_price);

        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header_confirm)
                .setFooter(R.layout.dialog_footer_empty)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .create();

        //初始值设定
        final View contentView = dialog.getHolderView();
        final EditText price = holder.getView(R.id.txtPrice);
        RadioButton radView = null;
        if (getString(R.string.lab_price_style_truck).equals(_info.priceStyle))
            radView = holder.getView(R.id.radTruck);
        if (getString(R.string.lab_price_style_ton).equals(_info.priceStyle))
            radView = holder.getView(R.id.radTon);
        if (getString(R.string.lab_price_style_cube).equals(_info.priceStyle))
            radView = holder.getView(R.id.radCube);
        if (getString(R.string.lab_price_style_kg).equals(_info.priceStyle))
            radView = holder.getView(R.id.radKg);
        if (getString(R.string.lab_price_style_piece).equals(_info.priceStyle)) {
            radView = holder.getView(R.id.radPiece);
//            price.setKeyListener(DigitsKeyListener.getInstance("0123456789"));
        }
        if (radView != null) {
            radView.setChecked(true);
        }

        //价格单位显示
        if (getString(R.string.lab_carload).equals(_info.transStyle)) {
            holder.getView(R.id.radTruck).setVisibility(View.GONE);
            switch (_info.goodsUnit) {
                case "吨":
                    holder.getView(R.id.radTone).setVisibility(View.VISIBLE);
                    holder.getView(R.id.radCube).setVisibility(View.GONE);
                    holder.getView(R.id.radKg).setVisibility(View.GONE);
                    holder.getView(R.id.radPiece).setVisibility(View.GONE);
                    break;
                case "方":
                    holder.getView(R.id.radTone).setVisibility(View.GONE);
                    holder.getView(R.id.radKg).setVisibility(View.GONE);
                    holder.getView(R.id.radPiece).setVisibility(View.GONE);
                    holder.getView(R.id.radCube).setVisibility(View.VISIBLE);
                    break;
                case "公斤":
                    holder.getView(R.id.radTone).setVisibility(View.GONE);
                    holder.getView(R.id.radKg).setVisibility(View.VISIBLE);
                    holder.getView(R.id.radCube).setVisibility(View.GONE);
                    holder.getView(R.id.radPiece).setVisibility(View.GONE);
                    break;
                case "件":
                    holder.getView(R.id.radTone).setVisibility(View.GONE);
                    holder.getView(R.id.radKg).setVisibility(View.GONE);
                    holder.getView(R.id.radCube).setVisibility(View.GONE);
                    holder.getView(R.id.radPiece).setVisibility(View.VISIBLE);
                    break;
            }
        } else {
            if (!Guard.isNullOrEmpty(_info.goodsUnit)) {
                holder.getView(R.id.radTruck).setVisibility(View.VISIBLE);
                switch (_info.goodsUnit) {
                    case "吨":
                        holder.getView(R.id.radTone).setVisibility(View.VISIBLE);
                        holder.getView(R.id.radCube).setVisibility(View.GONE);
                        holder.getView(R.id.radKg).setVisibility(View.GONE);
                        holder.getView(R.id.radPiece).setVisibility(View.GONE);
                        break;
                    case "方":
                        holder.getView(R.id.radTone).setVisibility(View.GONE);
                        holder.getView(R.id.radKg).setVisibility(View.GONE);
                        holder.getView(R.id.radPiece).setVisibility(View.GONE);
                        holder.getView(R.id.radCube).setVisibility(View.VISIBLE);
                        break;
                    case "公斤":
                        holder.getView(R.id.radTone).setVisibility(View.GONE);
                        holder.getView(R.id.radKg).setVisibility(View.VISIBLE);
                        holder.getView(R.id.radCube).setVisibility(View.GONE);
                        holder.getView(R.id.radPiece).setVisibility(View.GONE);
                        break;
                    case "件":
                        holder.getView(R.id.radTone).setVisibility(View.GONE);
                        holder.getView(R.id.radKg).setVisibility(View.GONE);
                        holder.getView(R.id.radCube).setVisibility(View.GONE);
                        holder.getView(R.id.radPiece).setVisibility(View.VISIBLE);
                        break;
                }
            }else {
                toastFast("请填写货物数量");
                return;
            }
        }

        if (_info.price > 0) {
            price.setText(_info.price + "");  //TODO:切记，设置值的时候，必须要转为字符串，或者是资源ID
        }
        Utils.setPricePoint(price);
        //必须创建后更改
        dialog.setHeaderText(R.id.dialogTitle, getString(R.string.lab_price));
        View header = dialog.getHeaderView();
        Button btnConfirm = (Button) header.findViewById(R.id.dialogConfirm);
        Button btnCancel = (Button) header.findViewById(R.id.dialogCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String style = null;
                RadioGroup rg = (RadioGroup) contentView.findViewById(R.id.radGroup);
                if (rg != null) {
                    RadioButton rb = (RadioButton) contentView.findViewById(rg.getCheckedRadioButtonId());
                    style = rb.getText().toString();
                }
                EditText et = (EditText) contentView.findViewById(R.id.txtPrice);
                double price = ParamTool.toDouble(et.getText());
//                if (price <= 0 || Guard.isNull(style)) {
//                    toastSlow("请输入正确的报价方式和价格");
//                    return;
//                }
                _info.price = (price);
                _info.priceStyle = (style);
                if (getString(R.string.lab_carload).equals(_info.transStyle)) {
                    //不能为包车
                    if (getString(R.string.lab_price_style_truck).equals(_info.priceStyle)) {
                        if (price > 0) {
                            toastFast("请选择报价方式");
                        } else {
                            labPrice.setText("");
                            dialog.dismiss();
                        }
                        return;
                    }
                } else {
                    //不能为方和吨
//                    if (getString(R.string.lab_price_style_ton).equals(_info.priceStyle) || getString(R.string.lab_price_style_cube).equals(_info.priceStyle)) {
//                        if (price>0) {
//                            toastFast("整车时只能选包车价");
//                        }else {
//                            labPrice.setText("");
//                            dialog.dismiss();
//                        }
//                        return;
//                    }
                }
                _dbCache.save(_info);
                if (price <= 0) {
                    labPrice.setText("");
                } else {
                    labPrice.setText(price + " " + style);
                }
                labPrice.setTextAppearance(baseActivity, R.style.EdgeOnFont);
                closeInput();
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 发票类选择
     */
    @OnClick({R.id.optInvoice})
    void openInvoiceDialog(View v) {
        //String value = labGoodsStyle.getText().toString();
        String value = _info.invoiceStyle + "";
        //Tracer.d(this.toString(), "id=" + v.getId() + "  :" + value + "  :" + Guard.isNullOrEmpty(value));
        //final ViewHolder vh = new ViewHolder(R.layout.dialog_radio_flow);
        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
                TextView tv = (TextView) view.findViewById(R.id.text_view);
                labInvoice.setText(tv.getText());
                labInvoice.setTextAppearance(baseActivity, R.style.EdgeOnFont);
                _info.invoiceStyle = (tv.getText().toString()) + "";
                _dbCache.save(_info);
                dialog.dismiss();
            }
        };
//        Log.e(TAG,getResources().getStringArray(R.array.invoice_style)[1]);
        LabelGridAdapter adapter = new LabelGridAdapter(baseActivity, R.array.invoice_style, value);
        GridHolder holder = new GridHolder(2);
        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header)
                .setFooter(R.layout.dialog_footer_empty)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setAdapter(adapter)
                .setOnItemClickListener(itemClickListener)
                .create();
        dialog.setHeaderText(R.id.dialogTitle, getString(R.string.lab_invoice));//必须创建后更改
        dialog.show();
    }


    /**
     * 付款方式 付款时间  对应payMethod , payPoint  枚举
     */
    @OnClick({R.id.optPayMethod})
    void openPayMethodDialog(View v) {

        final ViewHolder holder = new ViewHolder(R.layout.dialog_pay_style);
        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header_confirm)
                .setFooter(R.layout.dialog_footer_empty)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .create();

        //必须创建后更改
        final View contentView = dialog.getHolderView();

//初始值设定
        final FlowRadioGroup styleGroup = (FlowRadioGroup) contentView.findViewById(R.id.radPayMethod);
        final FlowRadioGroup lengthGroup = (FlowRadioGroup) contentView.findViewById(R.id.radPayPoint);

        final View itemView = _layoutInflater.inflate(R.layout.radio_item, null);
        //Tracer.w("itemVIew", "NULL =" + itemView);
        final String[] arrMethod = EnumHelper.getCnNames(PayMethodType.class);
        final String[] arrPayPoint = EnumHelper.getCnName(PaymentPointType.class);
//        Log.e(TAG, arrPayPoint.length + "");
        int methodValue = _info.payMethod;
        int value = _info.payPoint;
        String cnMethod = EnumHelper.getCnName(methodValue, PayMethodType.class);
        String cnPaypoint = EnumHelper.getCnName(value, PaymentPointType.class);
        int id = 1;
        for (int i = 0; i < arrMethod.length; i++) {
            id = id + 1;
            RadioButton rd = getRadioItem();
            rd.setText(arrMethod[i]);
            rd.setId(id);
            if (arrMethod[i].equals(cnMethod)) rd.setChecked(true);
            styleGroup.addView(rd);
        }
        for (int i = 0; i < arrPayPoint.length; i++) {
            id = id + 1;
            RadioButton rd = getRadioItem();
            rd.setText(arrPayPoint[i]);
            rd.setId(id);
            if (arrPayPoint[i].equals(cnPaypoint)) rd.setChecked(true);
            lengthGroup.addView(rd);
        }

        dialog.setHeaderText(R.id.dialogTitle, "付款方式");

        View header = dialog.getHeaderView();
        Button btnConfirm = (Button) header.findViewById(R.id.dialogConfirm);
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //styleGroup.getCheckedRadioButtonId();
                int payMethodId = styleGroup.getCheckedRadioButtonId();
                int payPointId = lengthGroup.getCheckedRadioButtonId();
                Tracer.i(TAG, "payMethodId=" + payMethodId + "  payPointId=" + payPointId);
                if (payMethodId == -1 || payPointId == -1) {
                    toastSlow("请选择付款方式和时间");
                    return;
                }
                String payMethod = ((RadioButton) contentView.findViewById(payMethodId)).getText().toString();
                String payPoint = ((RadioButton) contentView.findViewById(payPointId)).getText().toString();
                _info.payMethod = EnumHelper.getValue(payMethod, PayMethodType.class);
                _info.payPoint = EnumHelper.getValue(payPoint, PaymentPointType.class);
                Tracer.e(TAG, "payMethod=" + _info.payMethod + "  payPoint=" + _info.payPoint);
                _dbCache.save(_info);
                labPayPoint.setText(payMethod + "    " + payPoint);
                labPayPoint.setTextAppearance(baseActivity, R.style.EdgeOnFont);
                dialog.dismiss();
            }
        });
        dialog.show();
    }


    /**
     * 在Activity中实现OnSizeChangedListener，原理是设置该布局的paddingTop属性来控制子View的偏移
     */
    @Override
    public void onSizeChange(boolean flag, int w, int h) {
        if (flag) {// 键盘弹出时
            // layout.setPadding(0, 10, 0, 0);
            // boot.setVisibility(View.GONE) ;
            //imageLogo.setVisibility(View.GONE);
//            swipeLayout.setVisibility(View.GONE);
            // login_logo_layout_h.setVisibility(View.VISIBLE) ;
        } else { // 键盘隐藏时
            // layout.setPadding(0, 0, 0, 0);
            // boot.setVisibility(View.VISIBLE) ;
            //imageLogo.setVisibility(View.VISIBLE);
//            swipeLayout.setVisibility(View.VISIBLE);
            // login_logo_layout_h.setVisibility(View.GONE);
        }
    }

//    @Override
//    public boolean onTouchEvent(android.view.MotionEvent event) {
//        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
//        return imm.hideSoftInputFromWindow(this.getCurrentFocus()
//                .getWindowToken(), 0);
//
//    }

}
