package org.csware.ee.shipper.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.jungly.gridpasswordview.GridPasswordView;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;

import org.csware.ee.Guard;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.model.AlipayInfo;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.utils.Tools;

/**
 * 提现面面
 * Created by Yu on 2015/6/30.
 */
public class UserWithdrawFragment extends FragmentBase {
    final static String TAG = "UserWithdraw";


    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_withdraw_fragment;
    }

    String[] tabNames;

    @Override
    public void init() {

        ((UserWalletFragmentActivity) getActivity()).back(getString(R.string.mine_withdraw));

        tabNames = new String[]{
                "支付宝", "银行卡"
        };

        TabHost tabHost = (TabHost) rootView.findViewById(R.id.tabHost);
        tabHost.setup();
        tabHost.addTab(tabHost.newTabSpec("0").setIndicator(tabNames[0]).setContent(R.id.tabAlipay));
        tabHost.addTab(tabHost.newTabSpec("1").setIndicator(tabNames[1]).setContent(R.id.tabBankcard));
        onTabChanged tabChanged = new onTabChanged();
        tabHost.setOnTabChangedListener(tabChanged);
        //tabHost.performClick();
        tabHost.setCurrentTab(0); //init loaded this is not trigger the onTabChanged .must code it.
        tabChanged.onTabChanged("0");

    }

    class onTabChanged implements TabHost.OnTabChangeListener {

        @Override
        public void onTabChanged(String tabId) {
            //Log.e("onTabChanged", "tabId:" + tabId);
            Fragment fragment = null;
            switch (tabId) {
                case "0":
                    fragment = new UserWithdrawAlipayFragment();
                    break;
                case "1":
                    fragment = new UserWithdrawBankcardFragment();
                    break;
            }
            getFragmentManager().beginTransaction().replace(R.id.tabBody, fragment).commit();
        }
    }


    public static void popPasswordBox(final Context activity, String titile, final IAsyncPassword asyncPassword) {
        //判断是否为首次提现，如果是首次即要求设置提现密码
        final ViewHolder holder = new ViewHolder(R.layout.dialog_set_pay_pwd);
        final DialogPlus dialog = new DialogPlus.Builder(activity)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(DialogPlus.Gravity.CENTER)
                .create();
        final View contentView = dialog.getHolderView();
        TextView labTip = (TextView) contentView.findViewById(R.id.labTip);
        labTip.setText(titile);

        final Button btnCancel = (Button) contentView.findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) contentView.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GridPasswordView txtPassword = (GridPasswordView) contentView.findViewById(R.id.txtPassword);
                String pwd = txtPassword.getPassWord();
                //toastFast("PWD:" + pwd);
                if (!Guard.isNullOrEmpty(pwd)) {
                    if (pwd.length()==6) {
                        Tools.hideInput(activity,txtPassword);
                        asyncPassword.notify(pwd);
                    }else {
                        Toast.makeText(activity,"请输入正确的密码", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    Toast.makeText(activity,"密码不能为空", Toast.LENGTH_SHORT).show();
                }

                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final GridPasswordView txtPassword = (GridPasswordView) contentView.findViewById(R.id.txtPassword);
                txtPassword.clearPassword();
                Tools.hideInput(activity,txtPassword);
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public static void popPasswordBox(final Context activity, String titile, View.OnClickListener listener, View.OnClickListener cancelListener, final DialogPlus dialog, final View contentView) {
        //判断是否为首次提现，如果是首次即要求设置提现密码
//        final ViewHolder holder = new ViewHolder(R.layout.dialog_set_pay_pwd);
//        final DialogPlus dialog = new DialogPlus.Builder(activity)
//                .setContentHolder(holder)
//                .setCancelable(false)
//                .setGravity(DialogPlus.Gravity.CENTER)
//                .create();
//        final View contentView = dialog.getHolderView();
        TextView labTip = (TextView) contentView.findViewById(R.id.labTip);
        labTip.setText(titile);

        final Button btnCancel = (Button) contentView.findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) contentView.findViewById(R.id.btnConfirm);

        btnConfirm.setOnClickListener(listener);

        btnCancel.setOnClickListener(cancelListener);

        dialog.show();
    }

    public static void popPasswordBox(final Context baseActivity, final DialogPlus dialog, View contentView, String money, String username, View.OnClickListener listener){

        Button btnCancel = (Button) contentView.findViewById(R.id.btnCancel);
        Button btnConfirm = (Button) contentView.findViewById(R.id.btnConfirm);
        TextView title_tip = (TextView) contentView.findViewById(R.id.labTip);
        TextView txtCallWaiter = (TextView) contentView.findViewById(R.id.txtCallWaiter);
        TextView txt_withdraw_fragment_setmoney = (TextView) contentView.findViewById(R.id.txt_withdraw_fragment_setmoney);
        TextView txt_withdraw_fragment_user = (TextView) contentView.findViewById(R.id.txt_withdraw_fragment_user);
        TextView txt_withdraw_fragment_account = (TextView) contentView.findViewById(R.id.txt_withdraw_fragment_account);
        LinearLayout Lin_withdraw_fragment_paymoney = (LinearLayout) contentView.findViewById(R.id.Lin_withdraw_fragment_paymoney);
        LinearLayout Lin_withdraw_fragment_payaccount = (LinearLayout) contentView.findViewById(R.id.Lin_withdraw_fragment_payaccount);

        title_tip.setText(baseActivity.getResources().getString(R.string.hint_setPass_tip));
        Lin_withdraw_fragment_paymoney.setVisibility(View.VISIBLE);
        Tracer.e(TAG,money+"  name:"+username);
        if (!Guard.isNullOrEmpty(username)) {
            Lin_withdraw_fragment_payaccount.setVisibility(View.VISIBLE);
        }else {
            Lin_withdraw_fragment_payaccount.setVisibility(View.GONE);
        }
//        txt_withdraw_fragment_user.setText(alipayInfo.realname);
        txt_withdraw_fragment_setmoney.setText(money);
        txt_withdraw_fragment_account.setText(username);

        btnConfirm.setOnClickListener(listener);

        final GridPasswordView txtPassword = (GridPasswordView) contentView.findViewById(R.id.txtPassword);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtPassword.clearPassword();
                Tools.hideInput(baseActivity,txtPassword);
                dialog.dismiss();
            }
        });
        txtCallWaiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:4000316888"));
                baseActivity.startActivity(nIntent);
            }
        });

        dialog.show();
    }


}
