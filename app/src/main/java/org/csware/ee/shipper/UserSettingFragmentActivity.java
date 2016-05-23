package org.csware.ee.shipper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.shipper.fragment.UserHelpFragment;
import org.csware.ee.shipper.fragment.UserSettingFragment;
import org.csware.ee.view.TopActionBar;


/**
 * 设置页面，TODO:后面的设置子类全部使用Fragment
 */
public class UserSettingFragmentActivity extends FragmentActivityBase {

    final static String TAG = "UserSetting";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_fragment_activity);
//        Log.d(TAG, "onCreate");

        actionBar = getView(R.id.topBar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.boxFragment, new UserSettingFragment())
                .commit();

    }

    TopActionBar actionBar;

    public void replace(Fragment fragment, String title) {

        //if (!Guard.isNullOrEmpty(title)) actionBar.setTitle(title);  //不能设置，否则不能回退原来的Title上
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.boxFragment, fragment)
                .commit();
    }
    public void remove(Fragment fragment) {

        //if (!Guard.isNullOrEmpty(title)) actionBar.setTitle(title);  //不能设置，否则不能回退原来的Title上
        getSupportFragmentManager()
                .beginTransaction()
//                .addToBackStack(null)
                .remove(fragment)
                .commit();
    }
    public void back(String title){
        actionBar.setTitle(title);
        actionBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                replace(new UserSettingFragment(), "");
            }
        });
    }

    public void finish(String title){
        actionBar.setTitle(title);
        actionBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                finish();
            }
        });
    }

}
