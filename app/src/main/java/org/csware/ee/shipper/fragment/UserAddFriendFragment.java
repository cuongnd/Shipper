package org.csware.ee.shipper.fragment;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserFriendFragmentActivity;
import org.csware.ee.shipper.UserSettingFragmentActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 常用司机-添加
 * Created by Yu on 2015/6/30.
 */
public class UserAddFriendFragment extends FragmentBase {
    final static String TAG = "UserAddFriendFragment";
    @InjectView(R.id.txtPhone)
    EditText txtPhone;
    @InjectView(R.id.optAmount)
    LinearLayout optAmount;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {
//        Log.d(TAG, "lazyLoad");

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_friend_add_fragment;
    }

    UserFriendFragmentActivity activity;

    @Override
    public void init() {
        activity = (UserFriendFragmentActivity) getActivity();
        activity.hideMenu();//禁用
//        activity.hideBar();
//        ((UserFriendFragmentActivity)getActivity()).back(getString(R.string.mine_addBeaer));
    }


    @OnClick({R.id.btnConfirm})
    void addFriend(View v) {
//        Log.d(TAG, "addFriend");
        String phone = txtPhone.getText().toString();
        if (Guard.isNullOrEmpty(phone)) {
            toastFast("请输入常用司机的手机号");
            return;
        }

        ContactApi.edit(baseFragment.getActivity(), baseActivity, ActionType.ADD, phone, new IJsonResult() {
            @Override
            public void ok(Return json) {

                //成功
                toastFast("增加成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        activity.UserFriendFrag = new UserFriendFragment();
                        activity.replace(activity.UserFriendFrag, "常用司机");
                    }
                }, 500);
            }

            @Override
            public void error(Return json) {

            }
        });

//        if (ClientStatus.getNetWork(baseActivity)) {
//            HttpParams pms = new HttpParams();
//            pms.addParam("action", "add");
//            pms.addParam("mobile", phone);
//            String url = pms.getUrl(API.BEARER.EDIT);
//            Log.d("RequestURL", url);
//            final ProgressDialog processDialog = createDialog(R.string.dialog_loading);
//            FinalHttp fh = new FinalHttp();
//            fh.get(url, new AjaxCallBack<String>() {
//
//                @Override
//                public void onSuccess(String res) {
//                    //Log.d(TAG, "RESPONSE=\n" + res);
//                    Return result = GsonHelper.fromJson(res, Return.class);
//                    if (result.Result == 0) {
//                        //成功
//                        toastFast("增加成功");
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                activity.UserFriendFrag = new UserFriendFragment();
//                                activity.replace(activity.UserFriendFrag, "常用司机");
//                            }
//                        }, 500);
//
//                    } else {
//                        //失败
//                        String msg = Guard.isNullOrEmpty(result.Message) ? getString(R.string.tip_inner_error) : result.Message;
//                        toastSlow(msg);
//                    }
//                    processDialog.dismiss();
//                }
//
//                @Override
//                public void onFailure(Throwable t, int errCode, String strMsg) {
//                    //加载失败的时候回调
//                    Log.e(TAG, "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
//                    processDialog.dismiss();
//                    toastSlow(R.string.tip_server_error);
//                }
//            });
//        }

    }


}
