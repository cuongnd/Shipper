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
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.widget.CarDynamicPicker;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddCarModelFragmentActivity extends FragmentActivityBase {

    @InjectView(R.id.dialogTitle)
    TextView dialogTitle;
    @InjectView(R.id.dialogConfirm)
    Button dialogConfirm;
    @InjectView(R.id.cityPicker)
    CarDynamicPicker cityPicker;
    DeliverInfo _info;
    DbCache _dbCache;
    @InjectView(R.id.dialogCancel)
    Button dialogCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.carmodel_activity);
        ButterKnife.inject(this);
        //让两边不再有间隙
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
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
        init();
    }

    void init() {

        dialogTitle.setText(getString(R.string.tip_please_select_carmodel));

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Model = cityPicker.getProvince_string().trim();
                String Length = cityPicker.getCity_string();
                _info.truckStyle = Model;
                _info.truckLength = Length;
                _dbCache.save(_info);
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

    @OnClick(R.id.dialogCancel)
    void setDialogCancel(){
        finish();
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
//        baseActivity.overridePendingTransition(R.anim.pop_from_bottom, R.anim.close_to_bottom);
    }

}
