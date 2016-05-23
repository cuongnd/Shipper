package org.csware.ee.shipper;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.view.TopActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/11/4.
 */
public class PayResultActivity extends ActivityBase {
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.imgResult)
    ImageView imgResult;
    @InjectView(R.id.txtPayMoney)
    TextView txtPayMoney;
    @InjectView(R.id.txtRewardMoney)
    TextView txtRewardMoney;
    @InjectView(R.id.LinSuccPay)
    LinearLayout LinSuccPay;
    @InjectView(R.id.LinFailPay)
    LinearLayout LinFailPay;

    boolean isSuccess = false;
    String price;
    String RewardPrice;
    @InjectView(R.id.LinReward)
    LinearLayout LinReward;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_pay_result);
        ButterKnife.inject(this);
        topBar.setTitle("支付结果");

        init();

    }

    private void init() {
        isSuccess = getIntent().getBooleanExtra("isSuccess", false);
        price = getIntent().getStringExtra("price");
        RewardPrice = getIntent().getStringExtra("RewardPrice");

        if (isSuccess) {
            imgResult.setImageResource(R.mipmap.zfmm_icon_right);
            LinSuccPay.setVisibility(View.VISIBLE);
            LinFailPay.setVisibility(View.GONE);
            if (!Guard.isNullOrEmpty(price)) {
                txtPayMoney.setText(price + "");
            }
            if (!Guard.isNullOrEmpty(RewardPrice)) {
                txtRewardMoney.setText(RewardPrice);
            } else {
                LinReward.setVisibility(View.GONE);
            }
        } else {
            imgResult.setImageResource(R.mipmap.zfmm_icon_wrong);
            LinSuccPay.setVisibility(View.GONE);
            LinFailPay.setVisibility(View.VISIBLE);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
//                if (UserPayToBankActivity.instanse!=null){
//                    UserPayToBankActivity.instanse.finish();
//                }
                if (UserPayToDriverActivity.instance != null) {
                    UserPayToDriverActivity.instance.finish();
                }
                if (BindBankCardActivity.instanse != null) {
                    BindBankCardActivity.instanse.finish();
                }
                if (UserPayToPlatformFragmentActivity.instanse != null) {
                    UserPayToPlatformFragmentActivity.instanse.finish();
                }
                if (OrderDetailFragmentActivity.instance != null) {
                    OrderDetailFragmentActivity.instance.finish();
                }
            }
        }, 3000);

    }
}
