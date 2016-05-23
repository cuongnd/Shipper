package org.csware.ee.shipper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.fragment.UserBankCardFragment;
import org.csware.ee.shipper.fragment.UserWalletFragment;
import org.csware.ee.view.TopActionBar;


public class UserWalletFragmentActivity extends FragmentActivityBase {

    final static String TAG = "UserWallet";

    MeReturn userInfo;
    DbCache dbCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_wallet_fragment_activity);
//        Log.d(TAG, "onCreate");

        actionBar = getView(R.id.topBar);
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
        initData();
         getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.boxFragment, new UserWalletFragment())
                .commit();

    }

    void initData() {

        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                userInfo = json;
                if (Guard.isNull(userInfo.OwnerUser.CompanyStatus)){
                    userInfo.OwnerUser.CompanyStatus = -1;
                }
                dbCache.save(userInfo);
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    TopActionBar actionBar;

    public void replace(Fragment fragment, String title) {

        //if (!Guard.isNullOrEmpty(title))
            actionBar.setTitle(title);  //不能设置，否则不能回退原来的Title上
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(title)
                .replace(R.id.boxFragment, fragment)
                .commit();
    }
    public void remove(Fragment fragment) {

        //if (!Guard.isNullOrEmpty(title))
        getSupportFragmentManager()
                .beginTransaction()
//                .addToBackStack(title)
                .remove(fragment)
                .commit();
    }
    public void back(String title){
        actionBar.setTitle(title);
        actionBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                replace(new UserWalletFragment(), "");
            }
        });
    }
    public void backBank(String title){
        actionBar.setTitle(title);
        actionBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                replace(new UserBankCardFragment(), "");
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
