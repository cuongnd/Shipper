package org.csware.ee.shipper.fragment;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.LinearLayout;

import org.csware.ee.app.FragmentBase;
import org.csware.ee.consts.API;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserHelpFragmentActivity;
import org.csware.ee.shipper.WebViewActivity;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserHelpFragment extends FragmentBase {


    @InjectView(R.id.btnGuide)
    LinearLayout btnGuide;
    @InjectView(R.id.btnServicePhone)
    LinearLayout btnServicePhone;
    @InjectView(R.id.btnFeedback)
    LinearLayout btnFeedback;

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_help_fragment;
    }

    @Override
    public void init() {
        ((UserHelpFragmentActivity)getActivity()).finish(getString(R.string.icon_help));
    }

    @OnClick({R.id.btnGuide})
    void openGuide(View v) {
        //((UserHelpFragmentActivity) getActivity()).replace(new UserSettingMessageFragment(), "使用指南");
        Intent intent = new Intent(baseActivity, WebViewActivity.class);
        intent.putExtra("url", API.FAQ);
        startActivity(intent);
    }


    @OnClick({R.id.btnServicePhone})
    void openServicePhone(View v) {
        //((UserHelpFragmentActivity) getActivity()).replace(new UserSettingMessageFragment(), "客服电话");
        Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000-316-888" ));
        startActivity(nIntent);
    }


    @OnClick({R.id.btnFeedback})
    void openFeedback(View v) {
        ((UserHelpFragmentActivity) getActivity()).replace(new UserFeedbackFragment(), "意见反馈");
    }

}
