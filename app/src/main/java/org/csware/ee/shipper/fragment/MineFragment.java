package org.csware.ee.shipper.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Envir;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.UserVerifyType;
import org.csware.ee.shipper.AuthenticationActivity;
import org.csware.ee.shipper.CropImageFragmentActivity;
import org.csware.ee.shipper.DeliverCollectionExtraFragmentActivity;
import org.csware.ee.shipper.DeliverCollectionFragmentActivity;
import org.csware.ee.shipper.DeliverFragmentActivity;
import org.csware.ee.shipper.MailListActivity;
import org.csware.ee.shipper.MainTabFragmentActivity;
import org.csware.ee.shipper.MessageActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserAuthActivity;
import org.csware.ee.shipper.UserAuthComPanyActivity;
import org.csware.ee.shipper.UserFriendFragmentActivity;
import org.csware.ee.shipper.UserGameActivity;
import org.csware.ee.shipper.UserScoreShopActivity;
import org.csware.ee.shipper.UserHelpFragmentActivity;
import org.csware.ee.shipper.UserSettingFragmentActivity;
import org.csware.ee.shipper.UserShipperFragmentActivity;
import org.csware.ee.shipper.UserToolFragmentActivity;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.utils.EnumHelper;
import org.csware.ee.utils.ImageUtil;
import org.csware.ee.utils.Tools;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/5/29.
 */
public class MineFragment extends FragmentBase {

    final static String TAG = "MineFragment";

    @InjectView(R.id.labName)
    TextView labName;
    @InjectView(R.id.ivSFRZ)
    ImageView ivSFRZ;
    @InjectView(R.id.labSFRZ)
    TextView labSFRZ;
    @InjectView(R.id.ivLevel)
    ImageView ivLevel;
    @InjectView(R.id.boxUserInfo)
    LinearLayout boxUserInfo;
    @InjectView(R.id.ivHeadPic)
    ImageView ivHeadPic;
    @InjectView(R.id.frameLayout)
    FrameLayout frameLayout;
    @InjectView(R.id.btnFriend)
    LinearLayout btnFriend;
    @InjectView(R.id.btnWallet)
    LinearLayout btnWallet;
    @InjectView(R.id.btnSetting)
    LinearLayout btnSetting;
    @InjectView(R.id.btnHelp)
    LinearLayout btnHelp;
    @InjectView(R.id.btnTool)
    LinearLayout btnTool;
    @InjectView(R.id.btnMessage)
    LinearLayout btnMessage;

    public static String headUrl;
    @InjectView(R.id.img_update_tip)
    ImageView imgUpdateTip;
    @InjectView(R.id.txtScore)
    TextView txtScore;
    @InjectView(R.id.btnGame)
    LinearLayout btnGame;
    @InjectView(R.id.btnScoreShop)
    LinearLayout btnScoreShop;

    @Override
    protected int getLayoutId() {
        return R.layout.mine_fragment;
    }


    // 标志位，标志已经初始化完成。
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

        if (!getIsPrepared() || !isVisible) {
            return;
        }
        //填充各控件的数据
//        Log.e(TAG, "延迟加载开始");
    }

    @Override
    public void init() {
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class);
        if (userInfo == null) {
            userInfo = new MeReturn();
        }
//        String name = Guard.isNull(userInfo.Owner) ? "" :userInfo.Owner.Name;
//        labName.setText(name);
        SharedPreferences preferences = baseActivity.getSharedPreferences("UpdateTip", baseActivity.MODE_PRIVATE);
        boolean isShowUpdateTip = preferences.getBoolean("isShowTip", false);
        if (isShowUpdateTip) {
            imgUpdateTip.setVisibility(View.VISIBLE);
        }
    }

    MeReturn userInfo;
    DbCache dbCache;

    void initData() {

        UserApi.info(baseFragment.getActivity(), baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                userInfo = json;
                dbCache.save(userInfo);
                QCloudService.asyncDisplayImage(baseActivity, userInfo.OwnerUser.Avatar, ivHeadPic);
                if (userInfo.Owner != null && labName != null) {
                    int stauts = -1;
                    if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                        if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                            stauts = userInfo.OwnerUser.Status;
                        } else {
                            stauts = userInfo.OwnerUser.CompanyStatus;
                        }
                    }else {
                        stauts = userInfo.OwnerUser.Status;
                    }
                    if (userInfo.OwnerUser.CompanyStatus==-2){
                        stauts = userInfo.OwnerUser.CompanyStatus;
                    }
                    if (userInfo.OwnerUser.Status ==-2){
                        stauts = userInfo.OwnerUser.Status;
                    }
                    String name =Guard.isNull(userInfo.Owner) ? "" :userInfo.Owner.Name+"";
                    String staTip = Guard.isNull(userInfo.OwnerUser.Status) ? "" : EnumHelper.getCnName(userInfo.OwnerUser.Status, UserVerifyType.class);
                    Tracer.e(TAG,staTip+userInfo.OwnerUser.Status);
                    labSFRZ.setText(staTip);
                    if (userInfo.OwnerUser.CompanyStatus==1){
                        labSFRZ.setText("企业认证");
                        if (!Guard.isNull(userInfo.OwnerCompany)){
                            name = userInfo.OwnerCompany.CompanyName;
                        }
                    }
                    labName.setText(name + "");
                    txtScore.setText(json.Owner.Score + "");

                }else {
                    if (!Guard.isNull(labSFRZ)){
                        labSFRZ.setText("未验证");
                    }
                }
            }

            @Override
            public void error(Return json) {

            }
        });

    }


    //TODO:在TABHOST中滑动过快时会崩溃，回头要改成只有页面真的加载成功后才载入内容
    @Override
    public void onResume() {
        super.onResume();
//        Log.w(TAG, "onResume");
        if (!Guard.isNullOrEmpty(headUrl)) {
            QCloudService.asyncDisplayImage(baseActivity, headUrl, ivHeadPic);
            headUrl = null;
        }

        initData();
    }


    @OnClick({R.id.boxUserInfo})
    void openSelfInfo(View v) {
//        Log.d(TAG, "openSelfInfo");

        Intent intent = new Intent(baseActivity, UserShipperFragmentActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.btnFriend})
    void openFriendActivity(View v) {
//        Log.d(TAG, "openFriendActivity");
//        Intent intent = new Intent(baseActivity, UserFriendFragmentActivity.class);
        getUserInfo(false, 3);
    }

    @OnClick({R.id.btnWallet})
    void openWalletActivity(View v) {
//        Log.d(TAG, "openWalletActivity");
//        getUserInfo(false, 4);
        Tracer.d(TAG, "我的钱包");
        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "wallet_manage");
        baseActivity.startActivity(new Intent(baseActivity, UserWalletFragmentActivity.class));
    }
    ProgressDialog dialog;
    void getUserInfo(final boolean getUserId,final int what){
        if (Tools.getNetWork(baseActivity)) {
            dialog = Tools.getDialog(baseActivity);
            dialog.setCanceledOnTouchOutside(false);
            Activity activity = MainTabFragmentActivity.instance;
            UserApi.info(activity, baseActivity, new IJsonResult<MeReturn>() {
                @Override
                public void ok(MeReturn json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                    userInfo = json;
                    if (!getUserId) {
                        if (Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                            userInfo.OwnerUser.CompanyStatus = -1;
                        }
                        dbCache.save(userInfo);
                        int Status;
                        if (!Guard.isNull(userInfo.OwnerUser.CompanyStatus)) {
                            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                                Status = userInfo.OwnerUser.Status;
                            } else {
                                Status = userInfo.OwnerUser.CompanyStatus;
                            }
                        } else {
                            Status = userInfo.OwnerUser.Status;
                        }
                        if (Status == UserVerifyType.VERIFIED.toValue()) {
//                            if (DeliverCollectionFragmentActivity.activity!=null) {
//                                if (DeliverCollectionFragmentActivity.activity.isFinishing()) {
                            if (what==2) {
                                HashMap<String,String> map = new HashMap<>();
                                MobclickAgent.onEvent(baseActivity, "payment_manage");
                                Intent intent = new Intent(baseActivity, DeliverCollectionExtraFragmentActivity.class);
//                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.app_name));
                                baseActivity.startActivity(intent);
                            }else if (what==1){
                                HashMap<String,String> map = new HashMap<>();
                                MobclickAgent.onEvent(baseActivity, "ship_manage");
                                Intent intent = new Intent(baseActivity, DeliverFragmentActivity.class);
//                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.app_name));
                                baseActivity.startActivity(intent);
                            }else if (what ==3){
                                Tracer.d(TAG, "联系人");
                                Intent intent = new Intent(baseActivity, MailListActivity.class);
                                startActivity(intent);

                            }
//                            else if (what == 4){
//                                Tracer.d(TAG, "我的钱包");
//                                HashMap<String,String> map = new HashMap<>();
//                                MobclickAgent.onEvent(baseActivity, "wallet_manage");
//                                baseActivity.startActivity(new Intent(baseActivity, UserWalletFragmentActivity.class));
//                            }
//                                }
//                            }else {
//                                toastFast("网络不给力  请稍候重试");
//                            }
                        }else {
                            intentAction(Status);
                        }
                    }
                    else {
                        String UserId = "";
                        if (!Guard.isNull(userInfo)){
                            if (!Guard.isNull(userInfo.OwnerUser)){
                                if (!Guard.isNull(userInfo.OwnerUser.Id)) {
                                    UserId = userInfo.OwnerUser.Id + "";
                                }
                            }
                        }
                        if (Guard.isNullOrEmpty(UserId)){
                            toastFast("获取用户信息失败");
//                            MainTabFragmentActivity.instance.finish();
                        }else {
//                            intentOil(UserId);
                        }
                    }
//                QCloudService.asyncDisplayImage(baseActivity, userInfo.OwnerUser.Avatar, ivHeadPic);
                }

                @Override
                public void error(Return json) {
                    if (dialog != null) {
                        if (dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }
                }
            });
        }else {
            toastFast(org.csware.ee.R.string.tip_need_net);
        }
    }

    @OnClick(R.id.btnMessage)
    void setBtnMessage() {
        SharedPreferences preferences = baseActivity.getSharedPreferences("UpdateTip", baseActivity.MODE_PRIVATE);
//        boolean isShowUpdateTip = preferences.getBoolean("isShowTip",false);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean("isShowTip", false).commit();
        imgUpdateTip.setVisibility(View.GONE);
        Intent intent = new Intent(baseActivity, MessageActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.btnSetting})
    void openSettingActivity(View v) {
//        Log.d(TAG, "openSettingActivity");
        Intent intent = new Intent(baseActivity, UserSettingFragmentActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.btnHelp})
    void openHelpActivity(View v) {
//        Log.d(TAG, "openHelpActivity");
        Intent intent = new Intent(baseActivity, UserHelpFragmentActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.btnTool})
    void openToolActivity(View v) {
//        Log.d(TAG, "openToolActivity");
        Intent intent = new Intent(baseActivity, UserToolFragmentActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btnGame)
    void setBtnGame(){
        if (!Guard.isNull(userInfo.OwnerUser)) {
            int Status;
            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                Status = userInfo.OwnerUser.Status;
            } else {
                Status = userInfo.OwnerUser.CompanyStatus;
            }
            intentAction(Status);
            if (Status == UserVerifyType.VERIFIED.toValue()) {
                Intent intent = new Intent(baseActivity, UserGameActivity.class);
                startActivity(intent);
            }
        }
    }
    void intentAction(int status){
        if (userInfo.Owner == null || status == UserVerifyType.NOT_VERIFIED.toValue()) {
            Intent intent;
            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                intent = new Intent(baseActivity, UserAuthActivity.class); //TODO:modfiy to  the croect activity
            } else {
                intent = new Intent(baseActivity, UserAuthComPanyActivity.class); //TODO:modfiy to  the croect activity
            }
            startActivity(intent);
        } else if (status == UserVerifyType.VERIFYING.toValue() || userInfo.OwnerUser.Status == UserVerifyType.VERIFIEDFAIL.toValue()) {
            Intent intent = new Intent(baseActivity, AuthenticationActivity.class); //TODO:modfiy to  the croect activity
            intent.putExtra("Status", status);//userInfo.OwnerUser.Status
            intent.putExtra("Name", userInfo.Owner.Name);
            startActivity(intent);
        }
    }
    @OnClick(R.id.btnScoreShop)
    void setBtnScoreShop(){
        if (!Guard.isNull(userInfo.OwnerUser)) {
            int Status;
            if (userInfo.OwnerUser.Status >= userInfo.OwnerUser.CompanyStatus) {
                Status = userInfo.OwnerUser.Status;
            } else {
                Status = userInfo.OwnerUser.CompanyStatus;
            }
            intentAction(Status);
            if (Status == UserVerifyType.VERIFIED.toValue()) {
                Intent intent = new Intent(baseActivity, UserScoreShopActivity.class);
                startActivity(intent);
            }
        }
    }

    private Uri outputUri;
    private ImageView resultView;

    @OnClick({R.id.ivHeadPic})
    void selectHeadPic(View v) {

        openSelfInfo(v);
        //修改图片数据不在这儿啦！
//        Log.d(TAG, "vID=" + v.getId() + "    R.id.ivHeadPicId=" + R.id.ivHeadPic);
//        baseActivity.setTheme(R.style.ActionSheetStyle);
//        ActionSheet.createBuilder(baseActivity, getFragmentManager())
//                .setCancelButtonTitle("取消")
//                .setOtherButtonTitles("摄像头", "相册")
//                .setCancelableOnTouchOutside(true)
//                .setListener(actionSheetListener).show();
    }

    String imgFile;
    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//            Log.d(TAG, "click item index = " + index);
            if (index == 0) {

                Uri imgUri = Envir.getImageUri(ParamKey.MINE_HEADPIC_TEMP);
                imgFile = imgUri.getPath();
//                Log.i(TAG, "imageFileString=" + imgFile);

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imgUri);
                cameraIntent.putExtra(ParamKey.FILE_PATH, imgFile);
                startActivityForResult(cameraIntent, Code.CAMERA_REQUEST.toValue());

            } else if (index == 1) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent, "选择图片"), Code.ALBUM_REQUEST.toValue());
            }

        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
//            Log.d(TAG, "dismissed isCancle = " + isCancle);
        }
    };

    /**
     * 接收intent传回的信息
     */
    public void onActivityResult(int requestCode, int resultCode, Intent result) {


//        Log.d(TAG, "resultCODE:" + resultCode);

        if (resultCode == 0) {
//            Log.i(TAG, "user cancel the operation.");
            return;
        }

        if (requestCode == Code.CAMERA_REQUEST.toValue()) {
            //摄像头图像处理
            Intent intent = new Intent(baseActivity, CropImageFragmentActivity.class);
            intent.putExtra(ParamKey.IMAGE_PATH, imgFile);
            startActivityForResult(intent, Code.IMAGE_CROP.toValue());
        }
        if (requestCode == Code.ALBUM_REQUEST.toValue()) {
            //来源地址图片处理
            //Log.w(TAG,"result:"+ GsonHelper.toJson(result));
            //Log.w(TAG,"imagePath:"+ GsonHelper.toJson(result.getData()));
            Uri uri = result.getData();
            ContentResolver cr = baseActivity.getContentResolver();
            try {
                Bitmap bmp = BitmapFactory.decodeStream(cr.openInputStream(uri));
                //System.out.println("the bmp toString: " + bmp);
                imgFile = Envir.getImagePath(ParamKey.MINE_HEADPIC_TEMP);
                ImageUtil.saveImage(bmp, imgFile);
                bmp.recycle();
                Intent intent = new Intent(baseActivity, CropImageFragmentActivity.class);
                intent.putExtra(ParamKey.IMAGE_PATH, imgFile);
                startActivityForResult(intent, Code.IMAGE_CROP.toValue());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //裁剪完成
        if (requestCode == Code.IMAGE_CROP.toValue()) {
            //setHeadPic();
        }

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
