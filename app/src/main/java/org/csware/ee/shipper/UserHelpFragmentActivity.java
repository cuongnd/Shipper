package org.csware.ee.shipper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;

import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.shipper.fragment.UserFriendFragment;
import org.csware.ee.shipper.fragment.UserHelpFragment;
import org.csware.ee.view.TopActionBar;


public class UserHelpFragmentActivity extends FragmentActivityBase {

    final static String TAG = "UserHelpActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_setting_help_activity);
//        Log.d(TAG, "onCreate");

        actionBar = getView(R.id.topBar);
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.boxFragment, new UserHelpFragment())
                .commit();
    }

    TopActionBar actionBar;

    public void replace(Fragment fragment, String title) {

        //if (!Guard.isNullOrEmpty(title)) actionBar.setTitle(title);  //不能设置，否则不能回退原来的Title上
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(title)
                .replace(R.id.boxFragment, fragment)
                .commit();
    }
    public void back(String title){
        actionBar.setTitle(title);
        actionBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                replace(new UserHelpFragment(), "");
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
