package org.csware.ee.shipper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.QCloudService;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.RecordDetailInfo;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.view.TopActionBar;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/21.
 */
public class UserBeaerInfoActivity extends ActivityBase {
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.btnDeliver)
    Button btnDeliver;
    @InjectView(R.id.labName)
    TextView labName;
    @InjectView(R.id.ivSFRZ)
    ImageView ivSFRZ;
    @InjectView(R.id.img_beaer_detail_zgrz)
    ImageView img_beaer_detail_zgrz;
    @InjectView(R.id.img_beaer_detail_zgr)
    ImageView img_beaer_detail_zgr;
    @InjectView(R.id.labSFRZ)
    TextView labSFRZ;
    @InjectView(R.id.ivLevel)
    ImageView ivLevel;
    @InjectView(R.id.ivPhoneCall)
    ImageView ivPhoneCall;
    @InjectView(R.id.btnPhoneCall)
    LinearLayout btnPhoneCall;
    @InjectView(R.id.boxUserInfo)
    LinearLayout boxUserInfo;
    @InjectView(R.id.ivHeadPic)
    ImageView ivHeadPic;
    @InjectView(R.id.frameLayout)
    FrameLayout frameLayout;
    @InjectView(R.id.labPhoneNumber)
    TextView labPhoneNumber;
    @InjectView(R.id.labOrderAmount)
    TextView labOrderAmount;
    @InjectView(R.id.labZGRZ)
    TextView labZGRZ;
    @InjectView(R.id.labRealNameRZ)
    TextView labRealNameRZ;
    @InjectView(R.id.btnRealNameRZ)
    LinearLayout btnRealNameRZ;
    @InjectView(R.id.labScroe)
    TextView labScroe;
    @InjectView(R.id.btnExchange)
    LinearLayout btnExchange;
    @InjectView(R.id.boxExchange)
    LinearLayout boxExchange;
    @InjectView(R.id.rateComment)
    RatingBar rateComment;
    @InjectView(R.id.ivCommentGo)
    ImageView ivCommentGo;
    @InjectView(R.id.btnComment)
    LinearLayout btnComment;
    @InjectView(R.id.boxComment)
    LinearLayout boxComment;

    @InjectView(R.id.ivTruckPhoto)
    ImageView ivTruckPhoto;
    @InjectView(R.id.boxTruck)
    LinearLayout boxTruck;
    @InjectView(R.id.txtScore)
    TextView txtScore;

    DbCache _dbCache;
    OrderDetailChangeReturn.OrderEntity orderEntity;
    OrderDetailChangeReturn.BidsEntity bidsEntity;
    DeliverInfo _info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_detail_fragment_activity);
        ButterKnife.inject(this);
        topBar.setTitle("个人资料");
        _dbCache = new DbCache(baseActivity);
        try {
            _info = _dbCache.GetObject(DeliverInfo.class);
            if (_info == null) {
                _info = new DeliverInfo();
            }
        } catch (Exception e) {
            //序列化错，重新创建对象
            _info = new DeliverInfo();
        }
        final String json = getIntent().getStringExtra(ParamKey.ORDER_DETAIL);
        OrderDetailChangeReturn result = GsonHelper.fromJson(json, OrderDetailChangeReturn.class);
        if (!Guard.isNull(result)) {
            orderEntity = result.Order;
            if (!Guard.isNull(result.Bids)) {
                if (result.Bids.size() > 0) {
                    bidsEntity = result.Bids.get(0);
                }
            }
        }
        init();
    }

    private void init() {
        btnExchange.setVisibility(View.GONE);
        img_beaer_detail_zgrz.setVisibility(View.INVISIBLE);
        img_beaer_detail_zgr.setVisibility(View.INVISIBLE);
        if (!Guard.isNull(orderEntity)) {
            if (!Guard.isNull(orderEntity.BearerUser)) {
                if (!Guard.isNull(orderEntity.BearerUser.CompanyStatus)) {
                    if (orderEntity.BearerUser.CompanyStatus == 1) {
                        ivSFRZ.setImageResource(R.mipmap.dd_icon_company);
                        btnRealNameRZ.setVisibility(View.GONE);
                        if (!Guard.isNull(orderEntity.BearerCompany)) {
                            if (!Guard.isNullOrEmpty(orderEntity.BearerCompany.CompanyName)) {
                                labName.setText(orderEntity.BearerCompany.CompanyName);
                            }
                        }
                    } else {
                        if (!Guard.isNull(orderEntity.Bearer)) {
                            if (!Guard.isNullOrEmpty(orderEntity.Bearer.Name)) {
                                labName.setText(orderEntity.Bearer.Name);
                            }
                        }
                    }
                }
                //头像
                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Avatar)) {
                    QCloudService.asyncDisplayImage(baseActivity, orderEntity.BearerUser.Avatar, ivHeadPic);
                }
                if (!Guard.isNullOrEmpty(orderEntity.Bearer.VehiclePhoto)) {
                    QCloudService.asyncDisplayImage(baseActivity, orderEntity.Bearer.VehiclePhoto, ivTruckPhoto, true);
                }
                //电话
                if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
                    labPhoneNumber.setText(orderEntity.BearerUser.Mobile);
                }

                //企业认证状态
                if (!Guard.isNull(orderEntity.BearerUser.CompanyStatus)) {
                    if (orderEntity.BearerUser.CompanyStatus == 1) {
                        //企业
                        goToCompany(orderEntity.BearerUser.CompanyStatus);
                    } else {
                        goToCompany(orderEntity.BearerUser.CompanyStatus);
                        if (!Guard.isNull(orderEntity.BearerUser.Status)) {
                            //个人
                            goToPersonal(orderEntity.BearerUser.Status);
                        }
                    }
                }
            }
            labOrderAmount.setText(orderEntity.Bearer.OrderAmount + "");
            rateComment.setRating((float) bidsEntity.Bearer.Rate);
            ivCommentGo.setVisibility(View.VISIBLE);
        }
    }

    private void goToPersonal(int bearerStatus) {
        switch (bearerStatus) {
            case -1:
                //评价不可点击
//                btnComment.setVisibility(View.GONE);
                labSFRZ.setText(getResources().getString(R.string.hint_unAuthentication));
                labRealNameRZ.setText(getResources().getString(R.string.hint_unAuthentication));

                break;
            case 0:
                labSFRZ.setText(getResources().getString(R.string.hint_authentication));
                labRealNameRZ.setText(getResources().getString(R.string.hint_authentication));
                break;
            case 1:
                labSFRZ.setText(getResources().getString(R.string.hint_authenticationed));
                labRealNameRZ.setText(getResources().getString(R.string.hint_authenticationed));
                break;
            case -2:
                labSFRZ.setText(getResources().getString(R.string.authentication_failed));
                labRealNameRZ.setText(getResources().getString(R.string.authentication_failed));
                break;
            default:
                break;
        }

    }

    private void goToCompany(int companyStatus) {
        switch (companyStatus) {
            case -1:
                //评价不可点击
//                btnComment.setVisibility(View.GONE);
                labSFRZ.setText(getResources().getString(R.string.hint_unAuthentication_company));
                labZGRZ.setText(getResources().getString(R.string.hint_unAuthentication_company));
                break;
            case 0:
                labSFRZ.setText(getResources().getString(R.string.hint_authentication_company));
                labZGRZ.setText(getResources().getString(R.string.hint_authentication_company));
                break;
            case 1:
                labSFRZ.setText(getResources().getString(R.string.hint_authenticationed_company));
                labZGRZ.setText(getResources().getString(R.string.hint_authenticationed_company));
                break;
            case -2:
                labSFRZ.setText(getResources().getString(R.string.authentication_failed));
                labZGRZ.setText(getResources().getString(R.string.authentication_failed));
                break;
            default:
                break;

        }

    }

    @OnClick(R.id.btnDeliver)
    void setBtnDeliver() {
        if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
            _info.hackmanPhone = orderEntity.BearerUser.Mobile;
            _dbCache.save(_info);
//            finish();
            startActivity(new Intent(this, DeliverFragmentActivity.class));
        } else {
            toastFast("该司机号码为空");
        }
    }

    @OnClick(R.id.ivPhoneCall)
    void setBtnPhoneCall() {
        if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Mobile)) {
            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + orderEntity.BearerUser.Mobile));
            startActivity(nIntent);
        }
    }

    @OnClick(R.id.ivHeadPic)
    void setbtnHeadPic() {
        if (!Guard.isNullOrEmpty(orderEntity.BearerUser.Avatar)) {
            List<String> images = new ArrayList<>();
            images.add(orderEntity.BearerUser.Avatar);
            imageBrower(0, images);
        }
    }

    @OnClick(R.id.ivTruckPhoto)
    void setBtnTruckPhoto() {
        if (!Guard.isNullOrEmpty(bidsEntity.Bearer.VehiclePhoto)) {
            List<String> images = new ArrayList<>();
            images.add(bidsEntity.Bearer.VehiclePhoto);
            imageBrower(0, images);
        }
    }

    @OnClick(R.id.btnComment)
    void setBtnComment() {
        Intent intent = new Intent(this, EvaluationRecordsActivity.class);
        intent.putExtra("UsId", orderEntity.Bearer.UserId);
        intent.putExtra("action", "BearerRate");
        startActivity(intent);
    }
    private void imageBrower(int position, List<String> images) {
        Intent intent = new Intent(this, ImagePagerActivity.class);
        // 图片url,为了演示这里使用常量，一般从数据库中或网络中获取
        //List 转换为 String数组
        String[] urls = images.toArray(new String[images.size()]);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_URLS, urls);
        intent.putExtra(ImagePagerActivity.EXTRA_IMAGE_INDEX, position);
        startActivity(intent);
    }

}
