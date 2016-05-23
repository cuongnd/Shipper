package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.Return;
import org.csware.ee.view.TopActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2016/2/26.
 */
public class BankCardInfoActivity extends FragmentActivityBase {

    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.ivIcon)
    ImageView ivIcon;
    @InjectView(R.id.labName)
    TextView labName;
    @InjectView(R.id.labCarNo)
    TextView labCarNo;
    @InjectView(R.id.txtSingleLmt)
    TextView txtSingleLmt;
    @InjectView(R.id.txtDailyLmt)
    TextView txtDailyLmt;
    BankInfo bankInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bank_info);
        ButterKnife.inject(this);
        topBar.setTitle("银行卡详情");
//        topBar.setMenuTitle("管理");
        bankInfo = (BankInfo) getIntent().getSerializableExtra("bankInfo");
        if (!Guard.isNull(bankInfo)){
            ivIcon.setImageResource(bankInfo.getBankResId());
            labName.setText(bankInfo.getBankName());
            labCarNo.setText(bankInfo.getCardNo());
            txtSingleLmt.setText(bankInfo.getSingleLimit());
            txtDailyLmt.setText(bankInfo.getDayLimit());
        }
        topBar.setMenu(R.drawable.w_cysj_icon_bj, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
                baseActivity.setTheme(R.style.ActionSheetStyle);
                ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("删除")
                        .setCancelableOnTouchOutside(true)
                        .setListener(actionSheetListener).show();
            }
        });

        //TODO 传递数据  控件数据绑定

    }

    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, final int index) {
            //TODO 删除银行卡
            final HttpParams pms = new HttpParams(API.BANKCARD.DELETECARD);
            pms.addParam("cardid",bankInfo.getId());
            BackcardApi.delete(baseActivity, baseActivity, pms, new IJsonResult() {
                @Override
                public void ok(Return json) {
                    //成功
                    toastFast("删除成功");
                    Intent intent = new Intent();
                    intent.putExtra("methodAction","del");
                    setResult(Code.OK.toValue(), intent);
                    finish();
//                        ((FragmentActivityBase) baseFragment.getActivity()).delayedFinish(50); //TODO:这个有问题呀，关到首页去了
                }

                @Override
                public void error(Return json) {
                    //失败
                    toastFast("删除失败");
                }
            });

        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {

        }
    };

}
