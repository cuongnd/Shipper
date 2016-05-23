package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.widget.CityDynamicPicker;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class AddressFragmentActivity extends FragmentActivityBase {

    @InjectView(R.id.dialogTitle)
    TextView dialogTitle;
    @InjectView(R.id.dialogConfirm)
    Button dialogConfirm;
    @InjectView(R.id.cityPicker)
    CityDynamicPicker cityPicker;
    @InjectView(R.id.dialogCancel)
    Button dialogCancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.address_fragment_activity);
        ButterKnife.inject(this);
        //让两边不再有间隙
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        init();
    }

    void init() {

        dialogTitle.setText(getString(R.string.tip_please_select_area));

        dialogConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Address = cityPicker.getCity_string();
                String Code = cityPicker.getCity_code_string();
                Intent intent = new Intent();
                intent.putExtra(ParamKey.AREA_STRING, Address);
                intent.putExtra(ParamKey.AREA_ARRAY, cityPicker.getArea());
                if (Address.equals("北京市市辖区东城区") && TextUtils.isEmpty(Code)) {
                    Code = "110101";
                }
                intent.putExtra(ParamKey.AREA_CODE, Code);
                setResult(org.csware.ee.model.Code.OK.toValue(), intent);
                finish();
            }
        });

    }

    @OnClick(R.id.dialogCancel)
    void setDialogCancel() {
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
