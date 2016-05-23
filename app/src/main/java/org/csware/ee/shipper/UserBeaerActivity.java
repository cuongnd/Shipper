package org.csware.ee.shipper;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
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
import org.csware.ee.app.Tracer;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.RecordDetailInfo;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.view.TopActionBar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/21.
 */
public class UserBeaerActivity extends ActivityBase {
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
    //    ContactListReturn.ContactsEntity info;
    RecordDetailInfo.AccountLogBean.OrderBean info;
    RecordDetailInfo.AccountLogBean.PayerBean payperInfo;
    RecordDetailInfo result;
    DbCache _dbCache;
    DeliverInfo _info;
    int Type;

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
        Type = getIntent().getIntExtra("Type", 0);
        String json = getIntent().getStringExtra("BeaerInfo");
        result = GsonHelper.fromJson(json, RecordDetailInfo.class);
        btnExchange.setVisibility(View.GONE);
        btnDeliver.setVisibility(View.GONE);
        img_beaer_detail_zgrz.setVisibility(View.GONE);
        img_beaer_detail_zgr.setVisibility(View.GONE);
        if (Type == 10002 || Type == 10004) {
            //运费订单支付
            if (!Guard.isNull(result)) {
                info = result.AccountLog.Order;
                if (!Guard.isNull(info)) {
                    //名字
                    if (!Guard.isNull(info.BearerUser)) {
                        if (!Guard.isNull(info.BearerUser.CompanyStatus)) {
                            if (info.BearerUser.CompanyStatus == 1) {
                                btnRealNameRZ.setVisibility(View.GONE);
                                if (!Guard.isNull(info.BearerCompany)) {
                                    if (!Guard.isNullOrEmpty(info.BearerCompany.CompanyName)) {
                                        labName.setText(info.BearerCompany.CompanyName + "");
                                    }
                                }
                            } else {
                                if (!Guard.isNull(info.Bearer)) {
                                    if (!Guard.isNullOrEmpty(info.Bearer.Name)) {
                                        labName.setText(info.Bearer.Name);
                                    }
                                }
                            }
                        }
                        //头像
                        if (!Guard.isNullOrEmpty(info.BearerUser.Avatar)) {
                            QCloudService.asyncDisplayImage(baseActivity, info.BearerUser.Avatar, ivHeadPic);
                        }
                        //电话
                        if (!Guard.isNullOrEmpty(info.BearerUser.Mobile)) {
                            labPhoneNumber.setText(info.BearerUser.Mobile);
                        }

                        //企业认证状态
                        if (!Guard.isNull(info.BearerUser.CompanyStatus)) {
                            if (info.BearerUser.CompanyStatus == 1) {
                                //企业
                                goToCompany(info.BearerUser.CompanyStatus);
                            } else {
                                goToCompany(info.BearerUser.CompanyStatus);
                                if (!Guard.isNull(info.BearerUser.Status)) {
                                    //个人
                                    goToPersonal(info.BearerUser.Status);
                                }
                            }
                        }

                    }
                    labOrderAmount.setText(info.Bearer.OrderAmount + "");
                    rateComment.setRating((float) info.Bearer.Rate);
                    ivCommentGo.setVisibility(View.VISIBLE);


                    if (!Guard.isNullOrEmpty(info.Bearer.VehiclePhoto)) {
                        QCloudService.asyncDisplayImage(baseActivity, info.Bearer.VehiclePhoto, ivTruckPhoto, true);
                    }
                }
            }
        } else if (Type == 20002 || Type == 20004) {
            btnDeliver.setVisibility(View.INVISIBLE);
            boxTruck.setVisibility(View.INVISIBLE);
            //代收货款支付
            if (!Guard.isNull(result)) {
                info = result.AccountLog.Order;
                if (!Guard.isNull(info)) {
                    //名字
                    if (!Guard.isNull(info.OwnerUser)) {
                        if (!Guard.isNull(info.OwnerUser.CompanyStatus)) {
                            if (info.OwnerUser.CompanyStatus == 1) {
                                btnRealNameRZ.setVisibility(View.GONE);
                                if (!Guard.isNull(info.OwnerCompany)) {
                                    if (!Guard.isNullOrEmpty(info.OwnerCompany.CompanyName)) {
                                        labName.setText(info.OwnerCompany.CompanyName);
                                    }
                                }
                            } else {
                                if (!Guard.isNull(info.Owner)) {
                                    if (!Guard.isNullOrEmpty(info.Owner.Name)) {
                                        labName.setText(info.Owner.Name);
                                    }
                                }
                            }
                        }
                        //电话
                        if (!Guard.isNullOrEmpty(info.OwnerUser.Mobile)) {
                            labPhoneNumber.setText(info.OwnerUser.Mobile);
                        }
                        //企业认证状态
                        if (!Guard.isNull(info.OwnerUser.CompanyStatus)) {
                            if (info.OwnerUser.CompanyStatus == 1) {
                                //企业
                                goToCompany(info.OwnerUser.CompanyStatus);
                            } else {
                                goToCompany(info.OwnerUser.CompanyStatus);
                                if (!Guard.isNull(info.OwnerUser.Status)) {
                                    //个人
                                    goToPersonal(info.OwnerUser.Status);
                                }
                            }
                        }
                        //头像
                        if (!Guard.isNullOrEmpty(info.OwnerUser.Avatar)) {
                            QCloudService.asyncDisplayImage(baseActivity, info.OwnerUser.Avatar, ivHeadPic);
                        }
                    }
                    labOrderAmount.setText(info.Owner.OrderAmount + "");
                    rateComment.setRating((float) info.Owner.Rate);
                    ivCommentGo.setVisibility(View.VISIBLE);
                }
            }
        } else if (Type == 20003) {
            btnDeliver.setVisibility(View.INVISIBLE);
            boxTruck.setVisibility(View.INVISIBLE);
            //代收货款收入
            if (!Guard.isNull(result)) {
                info = result.AccountLog.Order;
                payperInfo = result.AccountLog.Payer;
                if (!Guard.isNull(payperInfo)) {
                    //名字
                    if (!Guard.isNull(payperInfo.OwnerUser)) {
                        if (!Guard.isNull(payperInfo.OwnerUser.CompanyStatus)) {
                            if (payperInfo.OwnerUser.CompanyStatus == 1) {
                                btnRealNameRZ.setVisibility(View.GONE);
                                if (!Guard.isNull(payperInfo.OwnerCompany)) {
                                    if (!Guard.isNullOrEmpty(payperInfo.OwnerCompany.CompanyName)) {
                                        labName.setText(payperInfo.OwnerCompany.CompanyName);
                                    }
                                }
                            } else {
                                if (!Guard.isNull(payperInfo.Owner)) {
                                    if (!Guard.isNullOrEmpty(payperInfo.Owner.Name)) {
                                        labName.setText(payperInfo.Owner.Name);
                                    }
                                }
                            }
                        }
                        //电话
                        if (!Guard.isNullOrEmpty(payperInfo.OwnerUser.Mobile)) {
                            labPhoneNumber.setText(payperInfo.OwnerUser.Mobile);
                        }
                        //企业认证状态
                        if (!Guard.isNull(payperInfo.OwnerUser.CompanyStatus)) {
                            if (payperInfo.OwnerUser.CompanyStatus == 1) {
                                //企业
                                goToCompany(payperInfo.OwnerUser.CompanyStatus);
                            } else {
                                goToCompany(payperInfo.OwnerUser.CompanyStatus);
                                if (!Guard.isNull(payperInfo.OwnerUser.Status)) {
                                    //个人
                                    goToPersonal(payperInfo.OwnerUser.Status);
                                }
                            }
                        }
                        //头像
                        if (!Guard.isNullOrEmpty(payperInfo.OwnerUser.Avatar)) {
                            QCloudService.asyncDisplayImage(baseActivity, payperInfo.OwnerUser.Avatar, ivHeadPic);
                        }
                    }

                    labOrderAmount.setText(payperInfo.Owner.OrderAmount + "");
                    rateComment.setRating((float) payperInfo.Owner.Rate);
                    ivCommentGo.setVisibility(View.VISIBLE);
                }
            }
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
        if (!Guard.isNullOrEmpty(info.BearerUser.Mobile)) {
            _info.hackmanPhone = info.BearerUser.Mobile;
            _dbCache.save(_info);
//            finish();
            startActivity(new Intent(this, DeliverFragmentActivity.class));
        } else {
            toastFast("该司机号码为空");
        }
    }

    @OnClick(R.id.ivPhoneCall)
    void setBtnPhoneCall() {
        if (!Guard.isNullOrEmpty(info.BearerUser.Mobile)) {
            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + info.BearerUser.Mobile));
            startActivity(nIntent);
        }
    }

    @OnClick(R.id.ivHeadPic)
    void setbtnHeadPic() {
        //TODO 显示头像
        if (Type == 10002 || Type == 10004) {
            if (!Guard.isNullOrEmpty(info.BearerUser.Avatar)) {
//                enlargePhoto(info.BearerUser.Avatar);
                List<String> images = new ArrayList<>();
                images.add(info.BearerUser.Avatar);
                imageBrower(0, images);
            }
        } else if (Type == 20002 || Type == 20004) {
            if (!Guard.isNullOrEmpty(info.OwnerUser.Avatar)) {
//                enlargePhoto(info.OwnerUser.Avatar);
                List<String> images = new ArrayList<>();
                images.add(info.OwnerUser.Avatar);
                imageBrower(0, images);
            }
        } else if (Type == 20003) {
            if (!Guard.isNullOrEmpty(payperInfo.OwnerUser.Avatar)) {
//                enlargePhoto(payperInfo.OwnerUser.Avatar);
                List<String> images = new ArrayList<>();
                images.add(payperInfo.OwnerUser.Avatar);
                imageBrower(0, images);
            }
        }
    }

    @OnClick(R.id.ivTruckPhoto)
    void setBtnTruckPhoto() {
        if (!Guard.isNullOrEmpty(info.Bearer.VehiclePhoto)) {
//            enlargePhoto(info.Bearer.VehiclePhoto);
            //TODO 显示司机车辆照片
            List<String> images = new ArrayList<>();
            images.add(info.Bearer.VehiclePhoto);
            imageBrower(0, images);
        }
    }

    @OnClick(R.id.btnComment)
    void setBtnComment() {
        Intent intent = new Intent(this, EvaluationRecordsActivity.class);
        if (Type == 10002 || Type == 10004) {
            intent.putExtra("UsId", info.Bearer.UserId);
            intent.putExtra("action", "BearerRate");
        } else if (Type == 20002 || Type == 20004) {
            intent.putExtra("UsId", info.Owner.UserId);
            intent.putExtra("action", "OwnerRate");
        } else if (Type == 20003) {
            intent.putExtra("UsId", payperInfo.Owner.UserId);
            intent.putExtra("action", "PayperRate");
        }
        startActivity(intent);
    }

    void enlargePhoto(String srcPath) {
        Intent intent = new Intent(this, EnlargePhotoActivity.class);
        intent.putExtra("srcPath", srcPath);
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
