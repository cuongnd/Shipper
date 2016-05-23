package org.csware.ee.shipper;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.OilRecordBackDetailInfo;
import org.csware.ee.model.OilRecordDetailInfo;
import org.csware.ee.model.RecordDetailInfo;
import org.csware.ee.model.Return;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;

import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;


/**
 * 交易明细列表
 * Created by zhou on 2016/4/7.
 */
public class UserRecordDetailActivity extends ActivityBase {

    private static final String TAG = "UserRecordDetailActivity";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.textAmount)
    TextView textAmount;
    @InjectView(R.id.shipperPic)
    ImageView shipperPic;
    @InjectView(R.id.textName)
    TextView textName;
    @InjectView(R.id.textNumber)
    TextView textNumber;
    @InjectView(R.id.BtnCall)
    LinearLayout BtnCall;
    @InjectView(R.id.orderMessageItem)
    RelativeLayout orderMessageItem;
    @InjectView(R.id.orderInformation)
    TextView orderInformation;
    @InjectView(R.id.paymentList)
    TextView paymentList;
    @InjectView(R.id.serialNumber)
    TextView serialNumber;
    @InjectView(R.id.tradingHours)
    TextView tradingHours;
    ProgressDialog preDialog;
    @InjectView(R.id.txtAmountName)
    TextView mTxtAmountName;
    @InjectView(R.id.transDetailLayout)
    LinearLayout mTransDetailLayout;
    @InjectView(R.id.imageViewOne)
    ImageView mImageViewOne;
    @InjectView(R.id.txtApply)
    TextView mTxtApply;
    @InjectView(R.id.txtApplyTime)
    TextView mTxtApplyTime;
    @InjectView(R.id.imageDealIcon)
    ImageView mImageDealIcon;
    @InjectView(R.id.txtDeal)
    TextView mTxtDeal;
    @InjectView(R.id.txtDealTime)
    TextView mTxtDealTime;
    @InjectView(R.id.imageViewFive)
    ImageView mImageViewFive;
    @InjectView(R.id.txtDealResult)
    TextView mTxtDealResult;
    @InjectView(R.id.txtDealReason)
    TextView mTxtDealReason;
    @InjectView(R.id.txtInstructionOne)
    TextView mTxtInstructionOne;
    @InjectView(R.id.txtInstructionTwo)
    TextView mTxtInstructionTwo;
    @InjectView(R.id.refundLayout)
    LinearLayout mRefundLayout;
    @InjectView(R.id.reasonLayout)
    LinearLayout mReasonLayout;
    @InjectView(R.id.amountLayout)
    RelativeLayout mAmountLayout;
    @InjectView(R.id.viewLine)
    View mViewLine;
    int detailId, typeId;
    ChinaAreaHelper areaHelper;
    //    DbCache dbCache;
    String phoneNumber;
    private String FromArea, ToArea;
    //    Shipper shipper;
    String Addon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_details);
        ButterKnife.inject(this);
        detailId = getIntent().getIntExtra("DetailId", -1);
        typeId = getIntent().getIntExtra("TypeID", -1);
        Addon = getIntent().getStringExtra("Addon");
        Tracer.e(TAG, "DetailId: " + detailId + "---" + "TypeID: " + typeId + "---" + "Addon： " + Addon);

        //TODO title显示文字
        if (typeId == 1001) {
            topBar.setTitle("提现记录");
        } else if (typeId == 80002) {
            topBar.setTitle("退款记录");
        } else if (typeId == 80003) {
            topBar.setTitle("赎回记录");
        } else {
            topBar.setTitle("交易详情");
        }

        areaHelper = new ChinaAreaHelper(baseActivity);
//        dbCache = new DbCache(baseActivity);
//        shipper = dbCache.GetObject(Shipper.class);
//        if (Guard.isNull(shipper)) {
//            shipper = new Shipper();
//        }
        //TODO 获取交易明细的详情数据
        asyncLoadingData();
    }

    private void asyncLoadingData() {

        if (typeId == 80001 || typeId == 80004) {
            //TODO 油卡充值详情
            preDialog = Tools.getDialog(baseActivity);
            preDialog.setCanceledOnTouchOutside(false);
            HttpParams params = new HttpParams(API.BANKCARD.OILRECHARGE);
            params.addParam("orderNo", Addon);
            BackcardApi.getOilRechargeDetailApi(baseActivity, baseActivity, params, new IJsonResult<OilRecordDetailInfo>() {
                @Override
                public void ok(OilRecordDetailInfo json) {
                    if (preDialog != null) {
                        if (preDialog.isShowing()) {
                            preDialog.dismiss();
                        }
                    }
                    //成功
                    if (!Guard.isNull(json)) {
                        if (json.Code == 200) {
                            if (!Guard.isNull(json.Content)) {
                                //显示交易的信息
                                mTransDetailLayout.setVisibility(View.VISIBLE);
                                //隐藏提现or退款or赎回信息
                                mRefundLayout.setVisibility(View.GONE);
                                orderMessageItem.setEnabled(false);
                                if (!Guard.isNullOrEmpty(json.Content.Money + "")) {
                                    //显示金额的layout
                                    mAmountLayout.setVisibility(View.VISIBLE);
                                    //显示交易金额文字信息（交易金额）
                                    mTxtAmountName.setText(getResources().getString(R.string.transaction_amount));
                                    double amount = json.Content.Money;
                                    if (amount > 0) {
                                        SpannableString spanText = new SpannableString("+" + amount + getString(R.string.unit_rmb));
                                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_blue)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                        textAmount.setText(spanText);
                                        textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                    } else if (amount < 0) {
                                        SpannableString spanText = new SpannableString(amount + getString(R.string.unit_rmb));
                                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_red)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                        textAmount.setText(spanText);
                                        textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                    } else {
                                        SpannableString spanText = new SpannableString(amount + getString(R.string.unit_rmb));
                                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_blue)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                        textAmount.setText(spanText);
                                        textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                    }
                                } else {
                                    //隐藏交易金额的layout
                                    mAmountLayout.setVisibility(View.GONE);
                                }

                                //显示油卡充值的信息
                                //油卡所属公司（中石化 or 中石油）
                                if (!Guard.isNullOrEmpty(json.Content.OilCardType + "")) {
                                    if (json.Content.OilCardType == 1) {
                                        shipperPic.setImageResource(R.mipmap.oilcard_icon_petrochina_big);
                                        textName.setText("中国石油");
                                    } else if (json.Content.OilCardType == 2) {
                                        shipperPic.setImageResource(R.mipmap.oilcard_icon_sinopec_big);
                                        textName.setText("中国石化");
                                    }
                                }
                                //油卡号
                                if (!Guard.isNullOrEmpty(json.Content.OilCardNo)) {
                                    textNumber.setText(json.Content.OilCardNo);
                                }
                                //订单信息
                                if (!Guard.isNullOrEmpty(json.Content.OrderInfo)) {
                                    orderInformation.setText(json.Content.OrderInfo);
                                }
                                //付款明细
                                if (!Guard.isNullOrEmpty(json.Content.PayName)) {
                                    if (json.Content.PayName.equals("alipay")) {
                                        paymentList.setText("支付宝");
                                    } else if (json.Content.PayName.equals("huifu")) {
                                        paymentList.setText("银行卡");
                                    } else if (json.Content.PayName.equals("weixin")) {
                                        paymentList.setText("微信");
                                    }else if (json.Content.PayName.equals("owneraccount")){
                                        paymentList.setText("余额支付");
                                    }
                                }
                                //流水号
                                if (!Guard.isNullOrEmpty(json.Content.PayNo)) {
                                    serialNumber.setText(json.Content.PayNo);
                                }
                                //交易时间
                                if (!Guard.isNullOrEmpty(json.Content.CreateTime)) {
                                    tradingHours.setText(json.Content.CreateTime);
                                }
                            }
                        } else {
                            toastFast("暂无数据");
                            //隐藏金额显示的layout
                            mAmountLayout.setVisibility(View.GONE);
                            //隐藏交易的信息layout
                            mTransDetailLayout.setVisibility(View.GONE);
                            //隐藏退款or赎回详情金额的layout
                            mRefundLayout.setVisibility(View.GONE);
                            orderMessageItem.setEnabled(false);
                        }
                    }
                }

                @Override
                public void error(Return json) {
                    if (preDialog != null) {
                        if (preDialog.isShowing()) {
                            preDialog.dismiss();
                        }
                    }
                }
            });
        } else if (typeId == 80002 || typeId == 80003) {
            //TODO 退款or赎回详情
            preDialog = Tools.getDialog(baseActivity);
            preDialog.setCanceledOnTouchOutside(false);
            HttpParams params = new HttpParams(API.BANKCARD.OILBACK);
            params.addParam("orderNo", Addon);
            BackcardApi.getOilDetailApi(baseActivity, baseActivity, params, new IJsonResult<OilRecordBackDetailInfo>() {
                @Override
                public void ok(OilRecordBackDetailInfo json) {
                    if (preDialog != null) {
                        if (preDialog.isShowing()) {
                            preDialog.dismiss();
                        }
                    }
                    //成功
                    if (!Guard.isNull(json)) {
                        //有数据时
                        if (json.Code == 200) {
                            /**
                             * 200 赎回中
                             * 201 赎回成功
                             * 202 赎回失败
                             *
                             * 300 退款中
                             * 301 退款成功
                             * 302 退款失败
                             */
                            if (!Guard.isNull(json.Content)) {
                                //隐藏交易的信息layout
                                mTransDetailLayout.setVisibility(View.GONE);
                                //显示退款or赎回详情金额layout
                                mRefundLayout.setVisibility(View.VISIBLE);
                                orderMessageItem.setEnabled(false);

                                if (!Guard.isNullOrEmpty(json.Content.Money + "")) {
                                    //显示金额的layout
                                    mAmountLayout.setVisibility(View.VISIBLE);
                                    double amount = json.Content.Money;
                                    if (amount > 0) {
                                        SpannableString spanText = new SpannableString("+" + amount + getString(R.string.unit_rmb));
                                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_blue)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                        textAmount.setText(spanText);
                                        textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                    } else if (amount < 0) {
                                        SpannableString spanText = new SpannableString(amount + getString(R.string.unit_rmb));
                                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_red)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                        textAmount.setText(spanText);
                                        textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                    } else {
                                        SpannableString spanText = new SpannableString(amount + getString(R.string.unit_rmb));
                                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_blue)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                        textAmount.setText(spanText);
                                        textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                    }
                                } else {
                                    //隐藏交易金额的layout
                                    mAmountLayout.setVisibility(View.GONE);
                                }
                                //提交申请的时间
                                if (!Guard.isNullOrEmpty(json.Content.ApplyTime)) {
                                    mTxtApplyTime.setText(json.Content.ApplyTime);
                                    mTxtDealTime.setText(json.Content.ApplyTime);
                                }
                                //处理时间
                                if (!Guard.isNullOrEmpty(json.Content.DealTime)) {
                                    mTxtDealTime.setText(json.Content.DealTime);
                                }
                                //原因
                                if (!Guard.isNullOrEmpty(json.Content.ResMessage)) {
                                    mViewLine.setVisibility(View.VISIBLE);
                                    mReasonLayout.setVisibility(View.VISIBLE);
                                    mTxtDealReason.setText("原因：" + json.Content.ResMessage);
                                } else {
                                    mViewLine.setVisibility(View.GONE);
                                    mReasonLayout.setVisibility(View.GONE);
                                }
                                if (typeId == 80002) {
                                    //显示退款的相关信息
                                    mTxtAmountName.setText(getResources().getString(R.string.refund_mount));//（退款金额）
                                    mTxtInstructionOne.setText(getResources().getString(R.string.refund_instructions_one));//（退款说明）
                                    mTxtInstructionTwo.setText(getResources().getString(R.string.refund_instructions_two));
                                    mTxtApply.setText(getResources().getString(R.string.refund_application));//（提交退款申请）
                                    mTxtDeal.setText(getResources().getString(R.string.refund_deal));//（退款处理）
                                    if (!Guard.isNull(json.Content.Status)) {
                                        //退款处理
                                        switch (json.Content.Status) {
                                            case 300://退款中
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_wait);
                                                mTxtDealResult.setText(getResources().getString(R.string.refund_in_the_processing));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                break;
                                            case 301://退款成功
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_complete);
                                                mTxtDealResult.setText(getResources().getString(R.string.refund_deal_success));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                break;
                                            case 302://退款失败
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_failed);
                                                mTxtDealResult.setText(getResources().getString(R.string.refund_deal_filed));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.orange_red));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.orange_red));
                                                break;
                                            default://退款中
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_wait);
                                                mTxtDealResult.setText(getResources().getString(R.string.refund_in_the_processing));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                break;
                                        }
                                    }
                                } else if (typeId == 80003) {
                                    //显示赎回的相关信息
                                    mTxtAmountName.setText(getResources().getString(R.string.redeem_mount));//（赎回金额）
                                    mTxtInstructionOne.setText(getResources().getString(R.string.redeem_instructions_one));//(赎回说明)
                                    mTxtInstructionTwo.setText(getResources().getString(R.string.redeem_instructions_two));
                                    mTxtApply.setText(getResources().getString(R.string.redeem_application));//（提交赎回申请）
                                    mTxtDeal.setText(getResources().getString(R.string.redeem_deal));//（赎回处理）
                                    if (!Guard.isNull(json.Content.Status)) {
                                        //赎回处理
                                        switch (json.Content.Status) {
                                            case 200://赎回中
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_wait);
                                                mTxtDealResult.setText(getResources().getString(R.string.redeem_in_the_processing));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                break;
                                            case 201://赎回成功
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_complete);
                                                mTxtDealResult.setText(getResources().getString(R.string.redeem_deal_success));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                break;
                                            case 202://赎回失败
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_failed);
                                                mTxtDealResult.setText(getResources().getString(R.string.redeem_deal_filed));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.orange_red));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.orange_red));
                                                break;
                                            default://赎回中
                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_wait);
                                                mTxtDealResult.setText(getResources().getString(R.string.redeem_in_the_processing));
                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                break;
                                        }
                                    }
                                }
                            }
                        } else {
                            toastFast("暂无数据");
                            //隐藏金额的layout
                            mAmountLayout.setVisibility(View.GONE);
                            //隐藏交易的信息的layout
                            mTransDetailLayout.setVisibility(View.GONE);
                            //隐藏退款or赎回详情金额的layout
                            mRefundLayout.setVisibility(View.GONE);
                            orderMessageItem.setEnabled(false);
                        }
                    }
                }

                @Override
                public void error(Return json) {
                    if (preDialog != null) {
                        if (preDialog.isShowing()) {
                            preDialog.dismiss();
                        }
                    }
                }
            });

        } else
        //TODO 提现 、支付运费、代收货款支出、代收货款收入的详情数据
        {
            preDialog = Tools.getDialog(baseActivity);
            preDialog.setCanceledOnTouchOutside(false);
            HttpParams params = new HttpParams(API.BANKCARD.RECORDDETAIL);
            params.addParam("id", detailId);
            BackcardApi.getDetailApi(baseActivity, baseActivity, params, new IJsonResult<RecordDetailInfo>() {
                        @Override
                        public void ok(RecordDetailInfo json) {
                            if (preDialog != null) {
                                if (preDialog.isShowing()) {
                                    preDialog.dismiss();
                                }
                            }
                            //成功
                            if (!Guard.isNull(json)) {
                                Result = GsonHelper.toJson(json);
                                if (!Guard.isNull(json.AccountLog)) {
                                    //交易金额显示
                                    if (!Guard.isNullOrEmpty(json.AccountLog.Change + "")) {
                                        mAmountLayout.setVisibility(View.VISIBLE);
                                        double amount = json.AccountLog.Change;
                                        if (amount > 0) {
                                            SpannableString spanText = new SpannableString("+" + amount + getString(R.string.unit_rmb));
                                            spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_blue)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                            textAmount.setText(spanText);
                                            textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                        } else if (amount < 0) {
                                            SpannableString spanText = new SpannableString(amount + getString(R.string.unit_rmb));
                                            spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_red)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                            textAmount.setText(spanText);
                                            textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                        } else {
                                            SpannableString spanText = new SpannableString(amount + getString(R.string.unit_rmb));
                                            spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.bg_blue)), 0, spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                                            textAmount.setText(spanText);
                                            textAmount.setMovementMethod(LinkMovementMethod.getInstance());
                                        }
                                    } else {
                                        //隐藏交易金额layout
                                        mAmountLayout.setVisibility(View.GONE);
                                    }
                                    if (!Guard.isNullOrEmpty(json.AccountLog.Type + "")) {
                                        Type = json.AccountLog.Type;
                                        //TODO 根据不同的Type判断交易的类型
                                        switch (Type) {
                                            case 1001:
                                                //提现记录
                                                //显示提现金额text（ 提现金额 ）
                                                mTxtAmountName.setText(getResources().getString(R.string.withdrawal_mount));
                                                //隐藏交易的信息的layout
                                                mTransDetailLayout.setVisibility(View.GONE);
                                                //显示提现信息的layout
                                                mRefundLayout.setVisibility(View.VISIBLE);
                                                //提现说明
                                                mTxtInstructionOne.setText(getResources().getString(R.string.withdrawal_instructions_one));
                                                mTxtInstructionTwo.setText(getResources().getString(R.string.withdrawal_instructions_two));
                                                mTxtApply.setText(getResources().getString(R.string.withdrawal_application));//（提交提现申请）
                                                mTxtDeal.setText(getResources().getString(R.string.withdrawal_deal));//（提现处理）
                                                RecordDetailInfo.AccountLogBean.OwnerDrawBean ownerDrawBean = json.AccountLog.OwnerDraw;
                                                if (!Guard.isNull(ownerDrawBean)) {
                                                    //提交申请的时间
                                                    if (!Guard.isNullOrEmpty(ownerDrawBean.ApplyTime)) {
                                                        Date date = new Date(Long.parseLong(String.valueOf(ownerDrawBean.ApplyTime)) * 1000);
                                                        String time = Utils.getCurrentTime("yyyy.MM.dd  HH:mm", date);
                                                        mTxtApplyTime.setText(time);
                                                    }
                                                    //处理时间
                                                    if (!Guard.isNullOrEmpty(ownerDrawBean.TransactionTime)) {
                                                        Date date = new Date(Long.parseLong(String.valueOf(ownerDrawBean.TransactionTime)) * 1000);
                                                        String time = Utils.getCurrentTime("yyyy.MM.dd  HH:mm", date);
                                                        mTxtDealTime.setText(time);
                                                    }
                                                    /**<Row Value="0">已申请</Row>
                                                     <Row Value="1">正在处理中</Row>
                                                     <Row Value="3">退回原账户</Row>
                                                     <Row Value="8">提现处理成功</Row>
                                                     提现状态*/

                                                    if (!Guard.isNull(ownerDrawBean.Status)) {
                                                        //提现处理中
                                                        switch (ownerDrawBean.Status) {
                                                            case 0:
                                                            case 1://提现处理中
                                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_wait);
                                                                mTxtDealResult.setText(getResources().getString(R.string.withdrawal_in_the_processing));
                                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                                break;
                                                            case 3://提现失败
                                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_failed);
                                                                mTxtDealResult.setText(getResources().getString(R.string.withdrawal_deal_filed));
                                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.orange_red));
                                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.orange_red));
                                                                break;
                                                            case 8://提现成功
                                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_complete);
                                                                mTxtDealResult.setText(getResources().getString(R.string.withdrawal_deal_success));
                                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                                break;
                                                            default://提现处理中
                                                                mImageDealIcon.setImageResource(R.mipmap.refund_icon_wait);
                                                                mTxtDealResult.setText(getResources().getString(R.string.withdrawal_in_the_processing));
                                                                mTxtDealResult.setTextColor(getResources().getColor(R.color.bg_blue));
                                                                mTxtDealReason.setTextColor(getResources().getColor(R.color.bg_blue));
                                                                break;
                                                        }
                                                    }
                                                    //原因
                                                    if (!Guard.isNullOrEmpty(ownerDrawBean.Reason)) {
                                                        mViewLine.setVisibility(View.VISIBLE);
                                                        mReasonLayout.setVisibility(View.VISIBLE);
                                                        mTxtDealReason.setText("原因：" + ownerDrawBean.Reason);
                                                    } else {
                                                        mViewLine.setVisibility(View.GONE);
                                                        mReasonLayout.setVisibility(View.GONE);
                                                    }
                                                }
                                                break;
                                            case 10004:
                                            case 10002:
                                                //显示交易金额text（交易金额）
                                                mTxtAmountName.setText(getResources().getString(R.string.transaction_amount));
                                                //显示交易的信息的layout
                                                mTransDetailLayout.setVisibility(View.VISIBLE);
                                                //隐藏提现or退款or赎回信息layout
                                                mRefundLayout.setVisibility(View.GONE);
                                                orderMessageItem.setEnabled(true);
                                                //TODO 运费订单支付的相关信息 (查看司机的信息)
                                                RecordDetailInfo.AccountLogBean.OrderBean orderBean = json.AccountLog.Order;
                                                if (!Guard.isNull(orderBean)) {
                                                    if (!Guard.isNull(orderBean.BearerUser)) {
                                                        //头像or图片显示
                                                        if (!Guard.isNullOrEmpty(orderBean.BearerUser.Avatar)) {
                                                            ImageLoader.getInstance().displayImage(orderBean.BearerUser.Avatar, shipperPic);
                                                        }
                                                        //电话号码
                                                        if (!Guard.isNullOrEmpty(orderBean.BearerUser.Mobile)) {
                                                            BtnCall.setVisibility(View.VISIBLE);
                                                            textNumber.setText(orderBean.BearerUser.Mobile);
                                                        }
                                                        //名字
                                                        if (!Guard.isNull(orderBean.BearerUser.CompanyStatus)) {
                                                            if (orderBean.BearerUser.CompanyStatus == 1) {
                                                                if (!Guard.isNull(orderBean.BearerCompany)) {
                                                                    if (!Guard.isNullOrEmpty(orderBean.BearerCompany.CompanyName)) {
                                                                        textName.setText(orderBean.BearerCompany.CompanyName + "");
                                                                    }
                                                                }
                                                            } else {
                                                                if (!Guard.isNull(orderBean.Bearer)) {
                                                                    if (!Guard.isNullOrEmpty(orderBean.Bearer.Name)) {
                                                                        textName.setText(orderBean.Bearer.Name);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    //订单信息
                                                    if (!Guard.isNullOrEmpty(orderBean.From + "")) {
                                                        if (orderBean.From == 0) {
                                                            FromArea = "未知";
                                                        } else {
                                                            FromArea = areaHelper.getProvinceName(orderBean.From + "");
                                                        }
                                                    }

                                                    if (!Guard.isNullOrEmpty(orderBean.To + "")) {
                                                        if (orderBean.To == 0) {
                                                            ToArea = "未知";
                                                        } else {
                                                            ToArea = areaHelper.getProvinceName(orderBean.To + "");
                                                        }
                                                    }
                                                    //地址信息  货物  数目  单位
                                                    orderInformation.setText(FromArea + "至" + ToArea + " " + orderBean.GoodsType + " " + orderBean.GoodsAmount + orderBean.GoodsUnit.trim());
                                                }
                                                RecordDetailInfo.AccountLogBean.OrderPayBean orderPayBean = json.AccountLog.OrderPay;
                                                if (!Guard.isNull(orderPayBean)) {
                                                    //付款明细（支付方式）
                                                    if (!Guard.isNullOrEmpty(orderPayBean.TransactionFrom)) {
                                                        if (orderPayBean.TransactionFrom.equals("alipay")) {
                                                            paymentList.setText("支付宝");
                                                        } else if (orderPayBean.TransactionFrom.equals("huifu")) {
                                                            paymentList.setText("银行卡");
                                                        } else if (orderPayBean.TransactionFrom.equals("weixin")) {
                                                            paymentList.setText("微信");
                                                        }else if (orderPayBean.TransactionFrom.equals("owneraccount")) {
                                                            paymentList.setText("余额支付");
                                                        }
                                                    }
                                                    //流水号
                                                    if (!Guard.isNullOrEmpty(orderPayBean.TransactionId)) {
                                                        serialNumber.setText(orderPayBean.TransactionId);
                                                    }
                                                    //交易时间
                                                    if (!Guard.isNullOrEmpty(String.valueOf(orderPayBean.CreateTime))) {
                                                        Date date = new Date(Long.parseLong(String.valueOf(orderPayBean.CreateTime)) * 1000);
                                                        String time = Utils.getCurrentTime("yyyy.MM.dd  HH:mm", date);
                                                        tradingHours.setText(time);
                                                    }
                                                }
                                                break;
                                            case 20004:
                                            case 20002:
                                                //显示交易金额text（交易金额）
                                                mTxtAmountName.setText(getResources().getString(R.string.transaction_amount));
                                                //显示交易的信息的layout
                                                mTransDetailLayout.setVisibility(View.VISIBLE);
                                                //隐藏提现or退款or赎回信息layout
                                                mRefundLayout.setVisibility(View.GONE);
                                                orderMessageItem.setEnabled(true);
                                                //TODO 代收货款支出的相关信息（查看支付对象货主的信息）
                                                RecordDetailInfo.AccountLogBean.OrderBean orderEntity = json.AccountLog.Order;
                                                if (!Guard.isNull(orderEntity)) {
                                                    if (!Guard.isNull(orderEntity.OwnerUser)) {
                                                        //头像or图片显示
                                                        if (!Guard.isNullOrEmpty(orderEntity.OwnerUser.Avatar)) {
                                                            ImageLoader.getInstance().displayImage(orderEntity.OwnerUser.Avatar, shipperPic);
                                                        }
                                                        //电话号码
                                                        if (!Guard.isNullOrEmpty(orderEntity.OwnerUser.Mobile)) {
                                                            BtnCall.setVisibility(View.VISIBLE);
                                                            textNumber.setText(orderEntity.OwnerUser.Mobile);
                                                        }
                                                        //名字
                                                        if (!Guard.isNull(orderEntity.OwnerUser.CompanyStatus)) {
                                                            if (orderEntity.OwnerUser.CompanyStatus == 1) {
                                                                if (!Guard.isNull(orderEntity.OwnerCompany)) {
                                                                    if (!Guard.isNullOrEmpty(orderEntity.OwnerCompany.CompanyName)) {
                                                                        textName.setText(orderEntity.OwnerCompany.CompanyName + "");
                                                                    }
                                                                }
                                                            } else {
                                                                if (!Guard.isNull(orderEntity.Owner)) {
                                                                    if (!Guard.isNullOrEmpty(orderEntity.Owner.Name)) {
                                                                        textName.setText(orderEntity.Owner.Name);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                    //订单信息
                                                    if (!Guard.isNullOrEmpty(orderEntity.From + "")) {
                                                        if (orderEntity.From == 0) {
                                                            FromArea = "未知";
                                                        } else {
                                                            FromArea = areaHelper.getProvinceName(orderEntity.From + "");
                                                        }
                                                    }

                                                    if (!Guard.isNullOrEmpty(orderEntity.To + "")) {
                                                        if (orderEntity.To == 0) {
                                                            ToArea = "未知";
                                                        } else {
                                                            ToArea = areaHelper.getProvinceName(orderEntity.To + "");
                                                        }
                                                    }
                                                    //地址信息  货物  数目  单位
                                                    orderInformation.setText(FromArea + "至" + ToArea + " " + orderEntity.GoodsType + " " + orderEntity.GoodsAmount + orderEntity.GoodsUnit.trim());
                                                }
                                                if (!Guard.isNull(json.AccountLog.OrderPayeePay)) {
                                                    //付款明细（支付方式）
                                                    if (!Guard.isNullOrEmpty(json.AccountLog.OrderPayeePay.TransactionFrom)) {
                                                        if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("alipay")) {
                                                            paymentList.setText("支付宝");
                                                        } else if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("huifu")) {
                                                            paymentList.setText("银行卡");
                                                        } else if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("weixin")) {
                                                            paymentList.setText("微信");
                                                        }else if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("owneraccount")) {
                                                            paymentList.setText("余额支付");
                                                        }
                                                    }
                                                    //流水号
                                                    if (!Guard.isNullOrEmpty(json.AccountLog.OrderPayeePay.TransactionId)) {
                                                        serialNumber.setText(json.AccountLog.OrderPayeePay.TransactionId);
                                                    }
                                                    //交易时间
                                                    if (!Guard.isNullOrEmpty(String.valueOf(json.AccountLog.OrderPayeePay.CreateTime))) {
                                                        Date date = new Date(Long.parseLong(String.valueOf(json.AccountLog.OrderPayeePay.CreateTime)) * 1000);
                                                        String time = Utils.getCurrentTime("yyyy.MM.dd  HH:mm", date);
                                                        tradingHours.setText(time);
                                                    }
                                                }
                                                break;
                                            case 20003:
                                                //显示交易金额text（交易金额）
                                                mTxtAmountName.setText(getResources().getString(R.string.transaction_amount));
                                                //显示交易的信息的layout
                                                mTransDetailLayout.setVisibility(View.VISIBLE);
                                                //隐藏提现or退款or赎回信息的layout
                                                mRefundLayout.setVisibility(View.GONE);
                                                orderMessageItem.setEnabled(true);
                                                //TODO 代收货款收入的相关信息（查看付款者的信息）
                                                if (!Guard.isNull(json.AccountLog.Payer)) {
                                                    if (!Guard.isNull(json.AccountLog.Payer.OwnerUser)) {
                                                        //头像or图片显示
                                                        if (!Guard.isNullOrEmpty(json.AccountLog.Payer.OwnerUser.Avatar)) {
                                                            ImageLoader.getInstance().displayImage(json.AccountLog.Payer.OwnerUser.Avatar, shipperPic);
                                                        }
                                                        //电话号码
                                                        if (!Guard.isNullOrEmpty(json.AccountLog.Payer.OwnerUser.Mobile)) {
                                                            BtnCall.setVisibility(View.VISIBLE);
                                                            textNumber.setText(json.AccountLog.Payer.OwnerUser.Mobile);
                                                        }
                                                        //名字
                                                        if (!Guard.isNull(json.AccountLog.Payer.OwnerUser.CompanyStatus)) {
                                                            if (json.AccountLog.Payer.OwnerUser.CompanyStatus == 1) {
                                                                if (!Guard.isNull(json.AccountLog.Payer.OwnerCompany)) {
                                                                    if (!Guard.isNullOrEmpty(json.AccountLog.Payer.OwnerCompany.CompanyName)) {
                                                                        textName.setText(json.AccountLog.Payer.OwnerCompany.CompanyName + "");
                                                                    }
                                                                }
                                                            } else {
                                                                if (!Guard.isNull(json.AccountLog.Payer.Owner)) {
                                                                    if (!Guard.isNullOrEmpty(json.AccountLog.Payer.Owner.Name)) {
                                                                        textName.setText(json.AccountLog.Payer.Owner.Name);
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                } else {
                                                    //如果付款者为空或者未知，则隐藏
                                                    orderMessageItem.setVisibility(View.GONE);
                                                }
                                                if (!Guard.isNull(json.AccountLog.Order)) {
                                                    //订单信息
                                                    if (!Guard.isNullOrEmpty(json.AccountLog.Order.From + "")) {
                                                        if (json.AccountLog.Order.From == 0) {
                                                            FromArea = "未知";
                                                        } else {
                                                            FromArea = areaHelper.getProvinceName(json.AccountLog.Order.From + "");
                                                        }
                                                    }

                                                    if (!Guard.isNullOrEmpty(json.AccountLog.Order.To + "")) {
                                                        if (json.AccountLog.Order.To == 0) {
                                                            ToArea = "未知";
                                                        } else {
                                                            ToArea = areaHelper.getProvinceName(json.AccountLog.Order.To + "");
                                                        }
                                                    }
                                                    //地址信息  货物  数目  单位
                                                    orderInformation.setText(FromArea + "至" + ToArea + " " + json.AccountLog.Order.GoodsType + " " + json.AccountLog.Order.GoodsAmount + json.AccountLog.Order.GoodsUnit.trim());
                                                }

                                                if (!Guard.isNull(json.AccountLog.OrderPayeePay)) {
                                                    //付款明细（支付方式）
                                                    if (!Guard.isNullOrEmpty(json.AccountLog.OrderPayeePay.TransactionFrom)) {
                                                        if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("alipay")) {
                                                            paymentList.setText("支付宝");
                                                        } else if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("huifu")) {
                                                            paymentList.setText("银行卡");
                                                        } else if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("weixin")) {
                                                            paymentList.setText("微信");
                                                        }else if (json.AccountLog.OrderPayeePay.TransactionFrom.equals("owneraccount")) {
                                                            paymentList.setText("余额支付");
                                                        }
                                                    }
                                                    //流水号
                                                    if (!Guard.isNullOrEmpty(json.AccountLog.OrderPayeePay.TransactionId)) {
                                                        serialNumber.setText(json.AccountLog.OrderPayeePay.TransactionId);
                                                    }
                                                    //交易时间
                                                    if (!Guard.isNullOrEmpty(String.valueOf(json.AccountLog.OrderPayeePay.CreateTime))) {
                                                        Date date = new Date(Long.parseLong(String.valueOf(json.AccountLog.OrderPayeePay.CreateTime)) * 1000);
                                                        String time = Utils.getCurrentTime("yyyy.MM.dd  HH:mm", date);
                                                        tradingHours.setText(time);
                                                    }
                                                }
                                                break;
                                            default:
                                                break;
                                        }
                                    }

                                }
                            }
                        }

                        @Override
                        public void error(Return json) {
                            if (preDialog != null) {
                                if (preDialog.isShowing()) {
                                    preDialog.dismiss();
                                }
                            }
                        }
                    }

            );

        }

    }

    String Result;
    int Type;

    @OnClick({R.id.BtnCall, R.id.orderMessageItem})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.BtnCall:
                //司机电话
                phoneNumber = textNumber.getText().toString();
                if (!Guard.isNullOrEmpty(phoneNumber)) {
                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
                    startActivity(nIntent);
                }
                break;
            case R.id.orderMessageItem:
                //司机信息
                if (!Guard.isNull(Result)) {
                    Intent intent = new Intent(this, UserBeaerActivity.class);
                    intent.putExtra("Type", Type);
                    intent.putExtra("BeaerInfo", Result);
                    startActivity(intent);
                }
                break;
            default:
                break;
        }
    }
}
