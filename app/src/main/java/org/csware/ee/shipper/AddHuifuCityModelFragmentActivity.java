package org.csware.ee.shipper;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.model.HuifuCityModel;

import org.csware.ee.widget.HuifuCityPicker;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class AddHuifuCityModelFragmentActivity extends FragmentActivityBase {

    @InjectView(R.id.dialogTitle)
    TextView dialogTitle;
    @InjectView(R.id.dialogConfirm)
    Button dialogConfirm;
//    DeliverInfo _info;
    HuifuCityModel cityModel;
    DbCache _dbCache;
    @InjectView(R.id.huifuCityPicker)
    HuifuCityPicker huifuCityPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.huifu_city_activity);
        ButterKnife.inject(this);
        huifuCityPicker.setVisibility(View.VISIBLE);
        //让两边不再有间隙
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        _dbCache = new DbCache(baseActivity);
        try {
            cityModel = _dbCache.GetObject(HuifuCityModel.class);
            if (cityModel == null) {
                cityModel = new HuifuCityModel();
            }
        } catch (Exception e) {
            //序列化错，重新创建对象
            cityModel = new HuifuCityModel();
        }
        init();
    }

    void init() {

        dialogTitle.setText(getString(R.string.tip_please_select_area));

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String ProvinceName = huifuCityPicker.getProvince_string().trim();
                String ProvinceCode = huifuCityPicker.getProvince_code_string();
                String CityName = huifuCityPicker.getCity_string();
                String CityCode = huifuCityPicker.getCity_code_string();
                cityModel.provinceName = ProvinceName;
                cityModel.provinceId = ProvinceCode;
                cityModel.cityName = CityName;
                cityModel.cityId = CityCode;
                Tracer.e("AddHuifuFragment","provinceName:"+ProvinceName+" provinceId:"+ProvinceCode+" cityName:"+CityName+" CityCode:"+CityCode);
//                cityModel.provinceId = huifuCityPicker
                _dbCache.save(cityModel);
//                Intent intent = new Intent();
//                intent.putExtra(ParamKey.AREA_STRING,Address);
//                intent.putExtra(ParamKey.AREA_ARRAY,cityPicker.getArea());
//                if (Address.equals("面包车")&& TextUtils.isEmpty(Code)){
//                    Code = "110100";
//                }
//                intent.putExtra(ParamKey.AREA_CODE,Code);
//                setResult(org.csware.ee.model.Code.OK.toValue(),intent);
                finish();
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void finish() {
        super.finish();
        //手动加入动画效果
        baseActivity.overridePendingTransition(R.anim.pop_from_bottom, R.anim.close_to_bottom);
    }

}
