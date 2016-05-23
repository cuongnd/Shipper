package org.csware.ee.shipper.fragment;

import android.view.View;
import android.widget.RelativeLayout;

import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Setting;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserSettingFragmentActivity;

/**
 * 系统通知
 * Created by Yu on 2015/6/30.
 */
public class UserSysNotifyFragment extends FragmentBase {

final static String TAG = "UserSysNotify";



    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    DbCache dbCache;
    Setting setting;

    @Override
    protected void lazyLoad() {
        dbCache = new DbCache(baseActivity);
        setting = dbCache.GetObject(Setting.class);
        if (setting == null) {
            setting = new Setting();
        }


    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_sys_notify_fragment;
    }

    @Override
    public void init() {
        ((UserSettingFragmentActivity) getActivity()).back(getString(R.string.sys_message));
    }





}
