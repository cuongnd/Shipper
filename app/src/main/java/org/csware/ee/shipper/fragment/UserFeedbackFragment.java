package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import org.csware.ee.Guard;
import org.csware.ee.api.ToolApi;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserHelpFragmentActivity;
import org.csware.ee.utils.Tools;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserFeedbackFragment extends FragmentBase {
    @InjectView(R.id.txtFeedback)
    EditText txtFeedback;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    ProgressDialog dialog;
    //owner/feedback.ashx
    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_feedback_fragment;
    }

    @Override
    public void init() {
        ((UserHelpFragmentActivity) getActivity()).back(getString(R.string.help_feedback));

    }

    @OnClick(R.id.btnConfirm)
    void setBtnConfirm(){
        String message = txtFeedback.getText().toString().trim();
        if (Guard.isNullOrEmpty(message)){
            toastFast("请填写您宝贵的意见！");
            return;
        }
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);

        ToolApi.feedback(baseFragment.getActivity(), baseActivity,message, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                toastFast("反馈成功，感谢您的反馈");
                getActivity().finish();
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                toastFast("反馈失败");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
