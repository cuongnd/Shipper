package org.csware.ee.shipper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.shipper.fragment.UserToolFragment;
import org.csware.ee.view.TopActionBar;


public class UserToolFragmentActivity extends FragmentActivityBase {

    final static String TAG = "UserTool";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_tool_fragment_activity);
//        Log.d(TAG, "onCreate");

        actionBar = getView(R.id.topBar);
        actionBar.setTitle(getString(R.string.icon_tool));
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.boxFragment, new UserToolFragment())
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


}
