package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lidroid.xutils.view.annotation.event.OnClick;

import org.csware.ee.api.ToolApi;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.model.FreightResult;
import org.csware.ee.model.Return;
import org.csware.ee.utils.GsonHelper;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class FreightFragmentActivity extends FragmentActivityBase {

    final static String TAG = "FreightAct";

    @InjectView(R.id.labTotalPrice)
    TextView labTotalPrice;
    @InjectView(R.id.labFrom)
    TextView labFrom;
    @InjectView(R.id.btnFrom)
    ImageButton btnFrom;
    @InjectView(R.id.optFrom)
    LinearLayout optFrom;
    @InjectView(R.id.labTo)
    TextView labTo;
    @InjectView(R.id.btnTo)
    ImageButton btnTo;
    @InjectView(R.id.optTo)
    LinearLayout optTo;
    @InjectView(R.id.txtTon)
    EditText txtTon;
    @InjectView(R.id.btnCalculate)
    Button btnCalculate;
    @InjectView(R.id.txt_distance)
    TextView txtDistance;
    @InjectView(R.id.Lin_calculation)
    LinearLayout LinCalculation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.freight_fragment_activity);
        ButterKnife.inject(this);
//        Log.d(TAG, "onCreate");

        //labFrom.setHint("TEST,GO");

        btnCalculate.setOnClickListener(calculatePrice);
        optFrom.setOnClickListener(openCitySelect);
        optTo.setOnClickListener(openCitySelect);

    }

    boolean isLegal(String fromCode, String toCode, String ton) {
        if (TextUtils.isEmpty(fromCode)) {
            toastFast("请选择出发地");
            return false;
        } else if (TextUtils.isEmpty(toCode)) {
            toastFast("请选择目的地");
            return false;
        } else if (TextUtils.isEmpty(ton)) {
            toastFast("请输入吨位");
            return false;
        } else {
            return true;
        }

    }


    View.OnClickListener calculatePrice = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Log.i(TAG, "onClick");
            labTotalPrice.setText("--.--");

            String ton = txtTon.getText().toString();
            if (!isLegal(fromCode, toCode, ton)) return;


            ToolApi.freightCalculation(baseActivity, baseContext, fromCode, toCode, ton, new IJsonResult<FreightResult>() {
                @Override
                public void ok(FreightResult json) {
                    //成功
//                    LinCalculation.setAnimation(AnimationUtils.loadAnimation(FreightFragmentActivity.this,R.anim.push_bottom_out));
                    LinCalculation.startAnimation(AnimationUtils.loadAnimation(FreightFragmentActivity.this, R.anim.popshow_anim));
                    LinCalculation.setVisibility(View.VISIBLE);
                    double price = 0;
                    txtDistance.setText(json.Distance + "");
                    if (json.IsWhole) {
                        price = json.Price;
                    } else {
                        price = json.Price * json.Amount;
                    }
//                    price = Integer.valueOf(String.valueOf(price));
                    labTotalPrice.setText((int) price + "元");
                    if ((int) price == 0) {
                        labTotalPrice.setText("暂无数据");
                        txtDistance.setText(json.Distance + "");
                    }
                }

                @Override
                public void error(Return json) {

                }
            });


        }
    };

    @OnClick(R.id.btnCalculate)
    void calculatePrice(View v) {
//        Log.i(TAG, "calculatePrice");
    }

    View.OnClickListener openCitySelect = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Log.i(TAG, "onClick");
            Code style = null;
            if (v.getId() == R.id.optFrom)
                style = Code.FROM_AREA;
            if (v.getId() == R.id.optTo)
                style = Code.TO_AREA;
            if (style == null) {
                return;
            }
            startActivityForResult(new Intent(baseContext, AddressFragmentActivity.class), style.toValue());
        }
    };

    @OnClick({R.id.optFrom, R.id.optTo})
    void openCitySelect(final View v) {
//        Log.i(TAG, "openCityBox");
    }


    String fromCode;
    String toCode;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null && resultCode == Code.OK.toValue()) {
            if (requestCode == Code.FROM_AREA.toValue()) {
                labFrom.setText(data.getExtras().getString(ParamKey.AREA_STRING));
                if (data.getExtras().getString(ParamKey.AREA_STRING).equals("北京市北京市东城区")) {
                    fromCode = "110101";
                } else {
                    fromCode = data.getExtras().getString(ParamKey.AREA_CODE);
                }
            }

            if (requestCode == Code.TO_AREA.toValue()) {
                labTo.setText(data.getExtras().getString(ParamKey.AREA_STRING));
                if (data.getExtras().getString(ParamKey.AREA_STRING).equals("北京市北京市东城区")) {
                    toCode = "110101";
                } else {
                    toCode = data.getExtras().getString(ParamKey.AREA_CODE);
                }
            }

            String[] areas = data.getStringArrayExtra(ParamKey.AREA_ARRAY);
//            Log.i(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode + "   Code:" + data.getExtras().getString(ParamKey.AREA_CODE) + "result:" + GsonHelper.toJson(areas));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
