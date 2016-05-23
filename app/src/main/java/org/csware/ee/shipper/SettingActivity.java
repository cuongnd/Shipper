package org.csware.ee.shipper;

import android.os.Bundle;

import com.baidu.mapapi.SDKInitializer;

import org.csware.ee.app.ActivityBase;


public class SettingActivity extends ActivityBase {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        //注意该方法要再setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_setting);
    }



}
