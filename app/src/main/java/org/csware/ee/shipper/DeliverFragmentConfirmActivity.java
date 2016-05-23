package org.csware.ee.shipper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IASyncQCloudResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.GetIdReturn;
import org.csware.ee.model.PayMethodType;
import org.csware.ee.model.PaymentPointType;
import org.csware.ee.model.Return;
import org.csware.ee.model.TruckLengthType;
import org.csware.ee.shipper.app.App;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.EnumHelper;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/21.
 */
public class DeliverFragmentConfirmActivity extends FragmentActivityBase {
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txt_fragment_progress)
    TextView txtFragmentProgress;
    @InjectView(R.id.labFrom)
    TextView labFrom;

    @InjectView(R.id.labTo)
    TextView labTo;

    @InjectView(R.id.labGoodsStyle)
    TextView labGoodsStyle;

    @InjectView(R.id.labAmount)
    TextView labAmount;

    @InjectView(R.id.radZC)
    TextView radZC;
    @InjectView(R.id.txt_deliver_models)
    TextView txtDeliverModels;
    @InjectView(R.id.txt_deliver_lengths)
    TextView txtDeliverLengths;

    @InjectView(R.id.labBeginTime)
    TextView labBeginTime;

    @InjectView(R.id.labEndTime)
    TextView labEndTime;
    @InjectView(R.id.labPayMethod)
    TextView labPayMethod;

    @InjectView(R.id.txtName)
    TextView txtName;

    @InjectView(R.id.txtPhone)
    TextView txtPhone;
    @InjectView(R.id.labPrice)
    TextView labPrice;
    @InjectView(R.id.labPayTime)
    TextView labPayTime;

    @InjectView(R.id.Lin_totalPrice)
    LinearLayout Lin_totalPrice;
    @InjectView(R.id.labTotalPrice)
    TextView labTotalPrice;
    @InjectView(R.id.txt_deliver_payModel)
    TextView txtDeliverPayModel;
    @InjectView(R.id.btnDeliver)
    Button btnDeliver;
    @InjectView(R.id.progressBar)
    ProgressBar progressBar;
    DbCache _dbCache;
    DeliverInfo _info;
    ChinaAreaHelper ah;
    SimpleDateFormat cnDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
    ProgressDialog dialog;
    HashMap<String, String> hashMap;
    @InjectView(R.id.Lin_CollectionName)
    LinearLayout LinCollectionName;
    @InjectView(R.id.Lin_CollectionPhone)
    LinearLayout LinCollectionPhone;
    ArrayList<String> drr;
    String action;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliver_fragment);
        ButterKnife.inject(this);
        topBar.setTitle("确认订单");
        labFrom.setFocusable(true);
        labFrom.setFocusableInTouchMode(true);
        labFrom.requestFocus();//将控件初始化到顶部
        id = getIntent().getLongExtra("id", 0);
        action = getIntent().getStringExtra("action");
        if (Guard.isNull(action)){
            action = "deliver";
        }
        drr = getIntent().getStringArrayListExtra("picList");
        hashMap = (HashMap<String, String>) getIntent().getSerializableExtra("map");
        _dbCache = new DbCache(baseActivity);
        ah = new ChinaAreaHelper(baseActivity);
        _info = _dbCache.GetObject(DeliverInfo.class);
        if (Guard.isNull(_info)){
            _info = new DeliverInfo();
        }
        initData();
    }

    long id;


    private void initData() {
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
//            labGoodsStyle.setTextAppearance(baseActivity, R.style.EdgeOnFont);
        }
        if (!Guard.isNullOrEmpty(_info.goodsUnit) && !Guard.isNullOrEmpty(_info.amount)) {
            labAmount.setText(_info.amount + "  " + _info.goodsUnit);
        }
        if (!Guard.isNullOrEmpty(_info.transStyle)) {
            radZC.setText(_info.transStyle);
        }
        if (!Guard.isNullOrEmpty(_info.truckStyle)) {
            txtDeliverModels.setText(_info.truckStyle);
            txtDeliverLengths.setText(_info.truckLength + "米");
//            txtDeliverLengths.setText(EnumHelper.getCnName(_info.truckLength, TruckLengthType.class));
        }
        if (_info.beginTime > 0) {
            labBeginTime.setText(ParamTool.fromTimeSeconds(_info.beginTime, cnDateFormat));
        }
        if (_info.endTime > 0) {
            labEndTime.setText(ParamTool.fromTimeSeconds(_info.endTime, cnDateFormat));
        }
        if (_info.payPoint >= 0) {
            labPayMethod.setText(EnumHelper.getCnName(_info.payMethod, PayMethodType.class));
            labPayTime.setText(EnumHelper.getCnName(_info.payPoint, PaymentPointType.class));
//            txtDeliverPayModel.setText(EnumHelper.getCnName(_info.payMethod, PayMethodType.class) + "/" + EnumHelper.getCnName(_info.payPoint, PaymentPointType.class));
        }
        if (action.equals("collection")) {
            LinCollectionName.setVisibility(View.GONE);
            LinCollectionPhone.setVisibility(View.GONE);
        } else {
            if (!Guard.isNullOrEmpty(_info.name)) {
                txtName.setText(_info.name);
            }
            if (!Guard.isNullOrEmpty(_info.phone)) {
                txtPhone.setText(_info.phone);
            }
        }
        if (_info.price > 0) {
            //.equals(getString(R.string.lab_price_style_truck))
            String priceStyle = "";
            if (!Guard.isNull(_info.priceStyle)){
                priceStyle = _info.priceStyle;
            }
            labPrice.setText(_info.price + " " + priceStyle);
            if (priceStyle.contains("包车")) {
                String unit = "元/" + priceStyle;
                labPrice.setText(_info.price + unit + "");
                labTotalPrice.setText(_info.price + "");
            } else {
                labTotalPrice.setText(FormatHelper.toMoney(_info.price * Double.valueOf(_info.amount)) + "");
            }
        } else {
            labPrice.setText("暂未报价");
            Lin_totalPrice.setVisibility(View.GONE);
        }
        int pros = 70;
        if (_info.price > 0) {
            pros += 7;
        }
        if (!Guard.isNullOrEmpty(_info.invoiceStyle)) {
            pros += 7;
        }
        if (!Guard.isNullOrEmpty(_info.memo)) {
            pros += 7;
        }
        if (!Guard.isNullOrEmpty(_info.hackmanPhone)) {
            pros += 7;
        }
        if (pros == 98) {
            pros = 100;
        }
        txtFragmentProgress.setText("填写完成数" + pros + "%" + "(填写详细内容，有助于您精确找车)");
        progressBar.setProgress(pros);
//        if (_info.price>0) {
//            if (_info.priceStyle.equals())
//            labTotalPrice.setText("");
//        }
    }

    @OnClick(R.id.btnDeliver)
    void setBtnDeliver() {
        dialog = Tools.getDialog(this);
        dialog.setCanceledOnTouchOutside(false);
//        if (action.equals("collection")) {
        String eventId = "";
        if (hashMap.isEmpty()){
            eventId = "ship_second_step";
        }else {
            eventId = "payment_second_step";
        }
            HashMap<String,String> map = new HashMap<>();
            MobclickAgent.onEvent(baseActivity, eventId);
            deliver(hashMap);
//        } else {
//            deliver();
//        }
        btnDeliver.setEnabled(false);
    }

    void deliver() {
        OrderApi.create(baseActivity, baseActivity, id, _info, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isRefresh",true).commit();
                _info.insurance = "";
                _dbCache.save(_info);
                toastSlow("订单发布成功");

                if (Tracer.isRelease()) {
                    _info = new DeliverInfo();
                    _dbCache.save(_info);
                }
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        DeliverFragmentActivity.activity.finish();
                    }
                }, 500);
            }

            @Override
            public void error(Return json) {
                btnDeliver.setEnabled(true);
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                _info.insurance = "";
                _dbCache.save(_info);
                //toastSlow("订单发布失败");
            }
        });
    }
    String images = "";


    void deliver(HashMap<String, String> maps) {
        if (drr!=null) {
            for (int i = 0; i < drr.size(); i++) {
                if (i==0){
                    images = drr.get(i);
                }else if (i>0){
                    images = images+","+drr.get(i);
                }
            }
        }
        OrderApi.create(baseActivity, id, _info, maps, images, action, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                SharedPreferences preferences = getSharedPreferences("isRefresh", MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("isRefresh",true).commit();
                _info.insurance = "";
                _info.bearerids = "";
                _dbCache.save(_info);
                toastSlow("订单发布成功");
                if (Tracer.isRelease()) {
                    _info = new DeliverInfo();
                    _dbCache.save(_info);
                }
                App.setSaveCache(false);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        finish();
                        if (action.equals("deliver")) {
                            DeliverFragmentActivity.activity.finish();
                        }else {
                            DeliverCollectionExtraFragmentActivity.activity.finish();
                        }
                    }
                }, 500);
            }

            @Override
            public void error(Return json) {
                _info.insurance = "";
                _info.bearerids = "";
                _dbCache.save(_info);
                App.setSaveCache(true);
//                if (Guard.isNull(json)){
//                    return;
//                }
//                if (!Guard.isNull(json.Message)) {
                    btnDeliver.setEnabled(true);
//                    if (json.Message.equals("订单编号错误")) {
                        OrderApi.getId(baseActivity, baseActivity, new IJsonResult<GetIdReturn>() {
                            @Override
                            public void ok(GetIdReturn json) {
                                id = json.Id;
                                if (dialog != null) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
//                                deliver(hashMap);
                            }

                            @Override
                            public void error(Return json) {

                                if (dialog != null) {
                                    if (dialog.isShowing()) {
                                        dialog.dismiss();
                                    }
                                }
                                if (!Guard.isNull(json)) {
                                    if (json.Message.equals("货主状态错误")) {  //toastSlow("订单号获取失败,请稍候重试");
                                        finish();
                                        startActivity(new Intent(baseActivity, UserAuthActivity.class));
                                    }
                                }
                            }
                        });
//                    }
//                }
//                finish();
//                if (action.equals("deliver")) {
//                    DeliverFragmentActivity.activity.finish();
//                }else {
//                    DeliverCollectionFragmentActivity.activity.finish();
//                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
    }
}
