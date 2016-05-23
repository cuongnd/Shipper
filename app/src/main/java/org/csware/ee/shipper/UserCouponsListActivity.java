package org.csware.ee.shipper;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.CouponListInfo;
import org.csware.ee.model.RecordDetailInfo;
import org.csware.ee.model.RecordInfo;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by zhojiulong on 2016/5/11.
 */
public class UserCouponsListActivity extends ActivityBase {
    private static final String TAG = "UserCouponsListActivity";
    @InjectView(R.id.topBar)
    TopActionBar mTopBar;
    @InjectView(R.id.couponsListView)
    ListView mCouponsListView;
    @InjectView(R.id.LinDataNone)
    LinearLayout mLinDataNone;
    private CommonAdapter<CouponListInfo.CouponsBean> _jsonAdapter;
    private List<CouponListInfo.CouponsBean> _coupons = new ArrayList<>();
    CouponListInfo result;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupons_list_layout);
        ButterKnife.inject(this);
        mTopBar.setTitle("我的优惠券");
        //获取获取优惠券的数据
        _jsonAdapter = new CommonAdapter<CouponListInfo.CouponsBean>(this, _coupons, R.layout.coupons_list_item) {
            @Override
            public void convert(ViewHolder helper, final CouponListInfo.CouponsBean item, int position) {
                LinearLayout couponsLayout = helper.getView(R.id.couponsLayout);
                TextView couponsName = helper.getView(R.id.couponsName);
                TextView couponsInstruction = helper.getView(R.id.couponsInstruction);
                TextView untilLine = helper.getView(R.id.untilLine);
                TextView deadLine = helper.getView(R.id.deadLine);
                TextView outDate = helper.getView(R.id.outDate);

                //优惠券金额
                helper.setText(R.id.couponAmount, item.Info.Discount + "");
                //优惠券的类型
                helper.setText(R.id.couponsName, item.Info.Name);
                //优惠券的描述
                helper.setText(R.id.couponsInstruction, item.Info.Description);
                //优惠券截止日期
                Date date = new Date(Long.parseLong(String.valueOf(item.ExpireTime)) * 1000);
                String time = Utils.getCurrentTime("yyyy.MM.dd", date);
                helper.setText(R.id.deadLine, time);


                //TODO 判断是油卡优惠券还是其它优惠券
                if (item.Info.Category.equals("oil")) {
                    //判断是否过期
                    if (item.IsExpired) {
                        //过期
                        outDate.setVisibility(View.VISIBLE);
                        couponsLayout.setBackgroundResource(R.mipmap.coupon_invalid_01);//背
                        couponsName.setTextColor(getResources().getColor(R.color.label_text_gray));//名
                        couponsInstruction.setTextColor(getResources().getColor(R.color.label_text_gray));//说明
                        untilLine.setTextColor(getResources().getColor(R.color.label_text_gray));//至
                        deadLine.setTextColor(getResources().getColor(R.color.label_text_gray));//期
                    } else {
                        //未过期
                        outDate.setVisibility(View.GONE);
                        couponsLayout.setBackgroundResource(R.mipmap.coupon_olicard_01);//背
                        couponsName.setTextColor(getResources().getColor(R.color.most_black));//名
                        couponsInstruction.setTextColor(getResources().getColor(R.color.most_black));//说明
                        untilLine.setTextColor(getResources().getColor(R.color.most_black));//至
                        deadLine.setTextColor(getResources().getColor(R.color.most_black));//期
                    }
                } else {
                    //判断是否过期
                    if (item.IsExpired) {
                        //过期
                        outDate.setVisibility(View.VISIBLE);
                        couponsLayout.setBackgroundResource(R.mipmap.coupon_invalid_01);//背
                        couponsName.setTextColor(getResources().getColor(R.color.label_text_gray));//名
                        couponsInstruction.setTextColor(getResources().getColor(R.color.label_text_gray));//说明
                        untilLine.setTextColor(getResources().getColor(R.color.label_text_gray));//至
                        deadLine.setTextColor(getResources().getColor(R.color.label_text_gray));//期
                    } else {
                        //未过期
                        outDate.setVisibility(View.GONE);
                        couponsLayout.setBackgroundResource(R.mipmap.coupon_logistics_01);//背
                        couponsName.setTextColor(getResources().getColor(R.color.most_black));//名
                        couponsInstruction.setTextColor(getResources().getColor(R.color.most_black));//说明
                        untilLine.setTextColor(getResources().getColor(R.color.most_black));//至
                        deadLine.setTextColor(getResources().getColor(R.color.most_black));//期
                    }
                }
            }
        };
        mCouponsListView.setAdapter(_jsonAdapter);
        asyncLoadingData();
    }

    //TODO 获取获取优惠券的数据
    private void asyncLoadingData() {
        String json = getIntent().getStringExtra("CouponListInfo");
        result = GsonHelper.fromJson(json, CouponListInfo.class);
        //成功
        if (!Guard.isNull(result)) {
            List<CouponListInfo.CouponsBean> list = result.Coupons;

            if (list == null || list.size() == 0) {
                Tracer.e(TAG, "暂无数据");
            } else {
                _coupons.addAll(list); //将新数据加入
                _jsonAdapter.notifyDataSetChanged();
            }
            if (_coupons.size() > 0) {
                if (!Guard.isNull(mLinDataNone)) {
                    mLinDataNone.setVisibility(View.GONE);
                    mCouponsListView.setVisibility(View.VISIBLE);
                }
            } else {
                if (!Guard.isNull(mLinDataNone)) {
                    mCouponsListView.setVisibility(View.GONE);
                    mLinDataNone.setVisibility(View.VISIBLE);
                }
            }

        }
    }
}
