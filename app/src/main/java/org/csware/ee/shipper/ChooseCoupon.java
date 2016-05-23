package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.model.Code;
import org.csware.ee.model.CouponListInfo;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by zhojiulong on 2016/5/11.
 */
public class ChooseCoupon extends ActivityBase {

    private static final String TAG = "ChooseCoupon";
    @InjectView(R.id.view)
    View mView;
    @InjectView(R.id.btn_close)
    TextView mBtnClose;
    @InjectView(R.id.btn_finish)
    TextView mBtnFinish;
    @InjectView(R.id.top_bar_title)
    RelativeLayout mTopBarTitle;
    @InjectView(R.id.ReNotCoupon)
    RelativeLayout mReNotCoupon;
    @InjectView(R.id.couponList)
    ListView mCouponList;
    private CommonAdapter<CouponListInfo.CouponsBean> _jsonAdapter;
    private List<CouponListInfo.CouponsBean> _coupons = new ArrayList<>();
    CouponListInfo result;
    int pos = -1;
    int couponId = -1;
    Double Discount;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_choose_coupon);
        ButterKnife.inject(this);
        //让两边不再有间隙
        getWindow().setLayout(FrameLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        _jsonAdapter = new CommonAdapter<CouponListInfo.CouponsBean>(this, _coupons, R.layout.coupons_item) {
            @Override
            public void convert(ViewHolder helper, final CouponListInfo.CouponsBean item, final int position) {
                final ImageView selectImage = helper.getView(R.id.checkImage);
                LinearLayout layout = helper.getView(R.id.layout);
                //优惠券金额
                helper.setText(R.id.couponAmount, item.Info.Discount + "");
                //优惠券的类型
                helper.setText(R.id.couponsName, item.Info.Name);
                //优惠券截止日期
                Date date = new Date(Long.parseLong(String.valueOf(item.ExpireTime)) * 1000);
                final String time = Utils.getCurrentTime("yyyy.MM.dd", date);
                helper.setText(R.id.deadLine, time);
                if (item.isSelect) {
                    selectImage.setImageResource(R.mipmap.lxr_icon_selected);
                    couponId = item.Id;
                    Discount = item.Info.Discount;
                } else {
                    selectImage.setImageResource(R.mipmap.lxr_icon_unselected);
                }
            }
        };

        mCouponList.setAdapter(_jsonAdapter);
        asyncLoadingData();
        mCouponList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < _coupons.size(); i++) {
                    CouponListInfo.CouponsBean newBean = _coupons.get(i);
                    if (i == position) {
                        if (newBean.isSelect) {
                            newBean.isSelect = false;
                            pos = -1;
                            couponId = -1;
                            Discount = 0.0;
                        } else {
                            newBean.isSelect = true;
                            pos = position;
                        }
                    } else {
                        newBean.isSelect = false;
                        couponId = -1;
                        Discount = 0.0;
                    }
                    _coupons.set(i, newBean);
                }
                _jsonAdapter.notifyDataSetChanged();
            }
        });

    }

    //TODO 获取获取优惠券的数据
    private void asyncLoadingData() {
        String json = getIntent().getStringExtra("CouponListInfo");//优惠券的信息
        pos = getIntent().getIntExtra("pos", -1);//位置

        Tracer.e(TAG, "asyncLoadingData position: " + pos);

        result = GsonHelper.fromJson(json, CouponListInfo.class);
        //成功
        List<CouponListInfo.CouponsBean> list = result.Coupons;
        if (!Guard.isNull(result)) {
            if (list == null || list.size() == 0) {
                Tracer.e(TAG, "暂无数据");
            } else {
                for (int i = 0; i < list.size(); i++) {
                    CouponListInfo.CouponsBean Bean = list.get(i);
                    if (!Bean.IsExpired) {
                        _coupons.add(Bean);//将新数据加入
                    }
                }
                _jsonAdapter.notifyDataSetChanged();
            }
            if (_coupons.size() > 0) {
                if (!Guard.isNull(mReNotCoupon)) {
                    mReNotCoupon.setVisibility(View.GONE);
                    mCouponList.setVisibility(View.VISIBLE);
                }
            } else {
                if (!Guard.isNull(mReNotCoupon)) {
                    mCouponList.setVisibility(View.GONE);
                    mReNotCoupon.setVisibility(View.VISIBLE);
                }
            }
        }
        if (pos >= 0) {
            for (int i = 0; i < _coupons.size(); i++) {
                CouponListInfo.CouponsBean newBean = _coupons.get(i);
                if (i == pos) {
                    newBean.isSelect = true;
                    Tracer.e(TAG, "pos==" + pos + ", i== " + i);
                } else {
                    newBean.isSelect = false;
                }
                _coupons.set(i, newBean);
            }
            _jsonAdapter.notifyDataSetChanged();
        }
    }

    @OnClick(R.id.btn_close)
    void setBtnClose() {
        UserPayToBankActivity.isClicks = true;
        finish();
    }

    @OnClick(R.id.btn_finish)
    void setBtnFinish() {
        UserPayToBankActivity.isClicks = true;
        Intent intent = new Intent();
        intent.putExtra("pos", pos);//位置
        intent.putExtra("couponId", couponId);//优惠券ID
        intent.putExtra("Discount", Discount);//金额
        Tracer.e(TAG, "pos: " + pos + "--" + "couponId: " + couponId + "---" + "Discount: " + Discount);
        setResult(Code.OK.toValue(), intent);
        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            UserPayToBankActivity.isClicks = true;
            this.finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
