package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;

import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/9/2.
 */
public class DeliverAddCollectionActivity extends ActivityBase {

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
    @InjectView(R.id.edt_deliver_collection_amount)
    EditText edtDeliverCollectionAmount;
    @InjectView(R.id.btnConfirmCollection)
    Button btnDeliver;
    @InjectView(R.id.Lin_AddCollectionDetail)
    LinearLayout LinAddCollectionDetail;
    List<HashMap<String, String>> collectionList;
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    DbCache _dbCache;
    DeliverInfo _info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.deliver_sub_consignee);
        ButterKnife.inject(this);
        _dbCache = new DbCache(baseActivity);
        try {
            _info = _dbCache.GetObject(DeliverInfo.class);

            if (_info == null) {
                _info = new DeliverInfo();
            }
        } catch (Exception e) {
            //序列化错，重新创建对象
            _info = new DeliverInfo();
        }
        if (!Guard.isNullOrEmpty(_info.name)){
            txtName.setText(_info.name);
        }
        if (!Guard.isNullOrEmpty(_info.phone)){
            txtPhone.setText(_info.phone);
        }
//        Editable ea = edtDeliverCollectionAmount.getText();
//        edtDeliverCollectionAmount.setSelection(ea.length());
        Utils.setPricePoint(edtDeliverCollectionAmount);
//        Editable text = edtDeliverCollectionAmount.getText();
//        Spannable spanText = text;
//        Selection.setSelection(spanText, text.length());
        if (!Guard.isNullOrEmpty(_info.price+"" ) && _info.price>0){
            edtDeliverCollectionAmount.setText(_info.price+"");
        }
        LinAddCollectionDetail.setVisibility(View.VISIBLE);
        topBar.setVisibility(View.VISIBLE);
        topBar.setTitle("收货人");
    }

    @OnClick(R.id.btnConfirmCollection)
    void setBtnDeliver() {
        String name = txtName.getText().toString().trim();
        String mobile = txtPhone.getText().toString().trim();
        String money = edtDeliverCollectionAmount.getText().toString().trim();
        _info.name = name;
        _info.phone = mobile;
        if (!Guard.isNullOrEmpty(money)) {
            _info.price = Double.parseDouble(money);
        }
        if (_info.price<=0) {
            toastFast("请输入金额");
            return;
        }
        _dbCache.save(_info);
//        if (Guard.isNullOrEmpty(name)) {
//            toastFast("请输入收货人");
//            return;
//        }
//        if (Guard.isNullOrEmpty(mobile)) {
//            toastFast("请输入联系方式");
//            return;
//        }

        Intent intent = new Intent();
        intent.putExtra("name", name+"");
        intent.putExtra("mobile", mobile+"");
        intent.putExtra("money", money);
        setResult(111, intent);
        finish();

    }

}
