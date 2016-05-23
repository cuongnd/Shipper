package org.csware.ee.shipper;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.HuifuCityModel;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.fragment.UserWithdrawFragment;
import org.csware.ee.utils.ClientCheck;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.DialogUtil;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.PayPasswordView;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/3.
 */
public class BindBankCardActivity extends ActivityBase {
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.txtBindName)
    EditText txtBindName;
    @InjectView(R.id.txtBindCard)
    EditText txtBindIDCard;
    @InjectView(R.id.txtSetPassword)
    EditText txtSetPassword;
    @InjectView(R.id.txtPasswordConfirm)
    EditText txtPasswordConfirm;
    @InjectView(R.id.btnSetPass)
    Button btnSetPass;
    @InjectView(R.id.Lin_SetPass)
    LinearLayout LinSetPass;
    @InjectView(R.id.txtBankName)
    TextView txtBankName;
    @InjectView(R.id.Lin_select_bank)
    LinearLayout LinSelectBank;
    @InjectView(R.id.txtBankCard)
    EditText txtBankCard;
    @InjectView(R.id.txtBankPhone)
    EditText txtBankPhone;
    @InjectView(R.id.txtBankPaper)
    TextView txtBankPaper;
    @InjectView(R.id.btnBindBank)
    Button btnBindBank;
    @InjectView(R.id.Lin_BindCard)
    LinearLayout LinBindCard;
    @InjectView(R.id.Lin_PassNull)
    LinearLayout LinPassNull;
    @InjectView(R.id.txtPasswordSure)
    EditText txtPasswordSure;

    DbCache dbCache;
    AccountReturn ac;
    MeReturn userInfo;
    BindStatusInfo bankinfo;
    Shipper shipper;
    //    BankModel bankModel;
    @InjectView(R.id.LinPassSure)
    LinearLayout LinPassSure;
    public static Activity instanse;
    boolean isFirstCg = false;
    double price;
    String money;
    long orderId;
    String action = "", payeeid = "", addBank = "", mode = "";
    static String orederNo;
    ProgressDialog dialog;
    @InjectView(R.id.LinBindTwo)
    LinearLayout LinBindTwo;
    @InjectView(R.id.LinBindOne)
    LinearLayout LinBindOne;
    @InjectView(R.id.LinBindName)
    LinearLayout LinBindName;
    String acPassword;
    @InjectView(R.id.txtBankAddress)
    TextView txtBankAddress;
//    private DialogWidget mDialogWidget;
    OilCardInfo info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_bind_bankcard);
        ButterKnife.inject(this);
        instanse = this;
        dlg = new AlertDialog.Builder(baseActivity).create();
        topBar.setTitle("创建银行卡密码");
        dbCache = new DbCache(baseActivity);
        ac = dbCache.GetObject(AccountReturn.class);
        userInfo = dbCache.GetObject(MeReturn.class);
        shipper = dbCache.GetObject(Shipper.class);
        info = dbCache.GetObject(OilCardInfo.class);
        if (Guard.isNull(ac)) {
            ac = new AccountReturn();
        }

        asyncLoadAccount();
        ac = dbCache.GetObject(AccountReturn.class);
        if (!Guard.isNull(ac.Account)) {
            if (!Guard.isNull(ac.Account.Password)) {
                acPassword = ac.Account.Password;
            }
        }
        Tracer.e("bindBank", Guard.isNullOrEmpty(acPassword) + "" + acPassword);
        if (Guard.isNull(userInfo)) {
            userInfo = new MeReturn();
        }
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
        if (Guard.isNull(info)) {
            info = new OilCardInfo();
        }
        bankinfo = dbCache.GetObject(BindStatusInfo.class);
        if (Guard.isNull(bankinfo)) {
            bankinfo = new BindStatusInfo();
        }
        asyncBindStatus(false);
        shipper = dbCache.GetObject(Shipper.class);
        isFirstCg = shipper.isFirstCg;
        Tracer.e("bindBank", isFirstCg + " isFirst");
        action = getIntent().getStringExtra("action");
        addBank = getIntent().getStringExtra("addBank");
        mode = getIntent().getStringExtra("mode");
        if (Guard.isNull(addBank)) {
            addBank = "";
        }
//        bankModel = dbCache.GetObject(BankModel.class);
        if (!Guard.isNullOrEmpty(action)) {
            if (action.equals("freight")) {
                price = getIntent().getDoubleExtra("price", -1);
                orderId = getIntent().getLongExtra("orderId", -1);
            } else if (action.equals("scan")) {
                payeeid = getIntent().getStringExtra("payeeid");
            } else if (action.equals("Oil")) {
                money = info.getMoney();
                orederNo = info.getOrderNO();

            }
        }
//        if (userInfo == null) {
//            userInfo = new MeReturn();
//        }
//        if (bankModel == null) {
//            bankModel = new BankModel();
//        }
//        asyncBindStatus(true);
        init();
    }

    void asyncLoadAccount() {
        dialog = Tools.getDialog(this);
        dialog.setCanceledOnTouchOutside(false);
        BackcardApi.account(baseActivity, baseActivity, new IJsonResult<AccountReturn>() {
            @Override
            public void ok(AccountReturn json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                //成功
                ac = json;
//                acPassword = json.Account.Password;
                dbCache.save(json);
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

    }

    private void asyncBindStatus(final boolean isBindCard) {
        HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
        BackcardApi.bindStatus(baseActivity, baseActivity, pms, new IJsonResult<BindStatusInfo>() {
            @Override
            public void ok(BindStatusInfo json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                if (Guard.isNull(bankinfo.PnrInfo)) {
                    shipper.isFirstCg = true;
                    isFirstCg = true;
                } else {
                    shipper.isFirstCg = false;
                    isFirstCg = false;
                }
                dbCache.save(shipper);
                Tracer.e("bindBank", isFirstCg + " isFirst");
                bankinfo = json;
                dbCache.save(bankinfo);
                if (isBindCard) {
                    toastFast("恭喜您，绑卡成功！");

                    if (!addBank.equals("addBank") || mode.equals("pay")) {
                        if (bankinfo.PnrCards.size() > 0) {
                            String number = "", cardNum = bankinfo.PnrCards.get(0).CardNo;
                            Tracer.e("userPayMethod", cardNum);
                            if (cardNum.length() > 4) {
                                number = cardNum.substring(cardNum.length() - 4, cardNum.length());
                            }
                            String bankName = "";
                            for (int i = 0; i < BankInfoService.getBankData().size(); i++) {
                                if (BankInfoService.getBankData().get(i).getBankcode().equals(bankinfo.PnrCards.get(0).BankCode)) {
                                    bankName = BankInfoService.getBankData().get(i).getBankName();
                                }
                            }
                            String payment = "使用\t\t" + bankName + "(" + number + ")\t\t" + "付款";
                            shipper.payment = payment;
                            shipper.cardid = bankinfo.PnrCards.get(0).Id;
                            dbCache.save(shipper);
                        }

                        if (action.equals("freight")) {
                            Intent intent = new Intent(BindBankCardActivity.this, UserPayToDriverActivity.class);
                            intent.putExtra("action", "freight");
                            intent.putExtra("orderId", orderId);
                            intent.putExtra("price", price);
                            intent.putExtra("info", getIntent().getStringExtra("info"));
                            startActivity(intent);
                        } else if (action.equals("scan")) {
                            Intent intent = new Intent(BindBankCardActivity.this, UserPayToDriverActivity.class);
                            intent.putExtra("action", "scan");
                            intent.putExtra("payeeid", payeeid);
                            startActivity(intent);
                        } else if (action.equals("Oil")) {
                            Intent intent = new Intent(BindBankCardActivity.this, UserPayToBankActivity.class);
                            intent.putExtra("action", "Oil");
                            startActivity(intent);
                        }
                        finish();
                        if (mode.equals("pay")) {
                            if (UserPayToPlatformFragmentActivity.instanse != null) {
                                UserPayToPlatformFragmentActivity.instanse.finish();
                            }
                        }
                    }
                    if (addBank.equals("addBank") || mode.equals("add")) {
                        Intent intent = new Intent();
                        intent.putExtra("methodAction", "add");
                        setResult(Code.OK.toValue(), intent);
                        finish();
                    }
                }

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
    }

    private void init() {
        txtSetPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
        txtPasswordConfirm.setTransformationMethod(PasswordTransformationMethod.getInstance());
        txtPasswordSure.setTransformationMethod(PasswordTransformationMethod.getInstance());
        String IDCard = "", Name = "";
        if (Guard.isNull(userInfo.Owner)) {
            toastFast("账户出错，请退出重新登录");
            return;
        }
//        IDCard = IDCard.substring(0,IDCard.length()-4)+eIDCard;
        if (!Guard.isNull(bankinfo)) {
            if (!Guard.isNull(bankinfo.PnrInfo)) {
                IDCard = bankinfo.PnrInfo.Identity;
                Name = bankinfo.PnrInfo.Name;
            }

        } else {
            if (!Guard.isNullOrEmpty(userInfo.Owner.Identity)) {
                IDCard = userInfo.Owner.Identity;
            } else if (!Guard.isNull(userInfo.OwnerCompany)) {
                if (!Guard.isNull(userInfo.OwnerCompany.LegalPersonId)) {
                    IDCard = userInfo.OwnerCompany.LegalPersonId;
                }
            }
            if (!Guard.isNullOrEmpty(userInfo.Owner.Name)) {
                Name = userInfo.Owner.Name;
            } else if (!Guard.isNull(userInfo.OwnerCompany)) {
                if (!Guard.isNull(userInfo.OwnerCompany.LegalPerson)) {
                    Name = userInfo.OwnerCompany.LegalPerson;
                }
            }
        }
        txtBindName.setText(Name);
        txtBindIDCard.setText(IDCard + "");

        if (Guard.isNullOrEmpty(acPassword)) {
            showSetPass();
        } else {
//            topBar.setTitle("密码核实");
//            LinPassSure.setVisibility(View.VISIBLE);
//            LinPassNull.setVisibility(View.GONE);
            showBind();
        }
//        if (addBank.equals("addBank")) {
//            showBind();
//        }
    }

    void showSetPass() {
        LinSetPass.setVisibility(View.VISIBLE);
        LinPassSure.setVisibility(View.GONE);
        LinBindCard.setVisibility(View.GONE);
        LinBindName.setVisibility(View.GONE);
        btnBindBank.setVisibility(View.GONE);
    }

    void showBind() {
        LinSetPass.setVisibility(View.GONE);
        LinBindCard.setVisibility(View.VISIBLE);
        LinBindName.setVisibility(View.VISIBLE);
        btnBindBank.setVisibility(View.VISIBLE);
        topBar.setTitle("绑定银行卡");
        if (isFirstCg) {
//            LinBindOne.setVisibility(View.VISIBLE);
//            LinBindTwo.setVisibility(View.VISIBLE);
        } else {
            txtBindName.setFocusable(false);
            txtBindIDCard.setFocusable(false);
            txtBindName.setEnabled(false);
            txtBindIDCard.setEnabled(false);
        }
    }

    String passSure;

    @Override
    protected void onResume() {
        super.onResume();
        HuifuCityModel cityModel = dbCache.GetObject(HuifuCityModel.class);
        if (Guard.isNull(cityModel)){
            cityModel = new HuifuCityModel();
        }
        String pName = "";
        String cName = "";
        if (!Guard.isNullOrEmpty(cityModel.getProvinceName())){
            pName = cityModel.getProvinceName();
            Tracer.e("BindBank",cityModel.getProvinceId()+" pId");
        }
        if (!Guard.isNullOrEmpty(cityModel.getCityName())){
            cName = cityModel.getCityName();
            Tracer.e("BindBank",cityModel.getCityId()+" cId");
        }
        String cp = pName+cName;
        if (!Guard.isNullOrEmpty(cp)) {
            cp = pName+"\t\t"+cName;
            txtBankAddress.setText(cp);
        }else {
            txtBankAddress.setHint("开户地址信息");
        }
    }

    @OnClick(R.id.txtBankAddress)
    void setTxtBankAddress(){
        startActivity(new Intent(this,AddHuifuCityModelFragmentActivity.class));
    }

    @OnClick(R.id.btnSetPass)
    void setBtnSetPass() {
        String password = txtPasswordConfirm.getText().toString();
        passSure = txtPasswordSure.getText().toString();
        bindName = txtBindName.getText().toString().trim();
        bindIDCard = txtBindIDCard.getText().toString().trim();
        Tracer.e("BindBank", txtPasswordSure.getText().toString() + "");

        if ("".equals(txtSetPassword.getText().toString()) && Guard.isNullOrEmpty(acPassword)) {
            toastFast("请设置您的交易密码");
            return;
        }
        if (!password.equals(txtSetPassword.getText().toString())) {
            toastFast("2次密码不一致");
            return;
        }
        AccountReturn ac = dbCache.GetObject(AccountReturn.class);
        String pass = "";
        if (!Guard.isNull(ac.Account)) {
            pass = ac.Account.Password;
        }
        if (Guard.isNullOrEmpty(pass)) {
            if (!password.equals("") && password.length() == 6) {
                //TODO 判断是否第一次修改信息
//                if (isFirstCg) {
////                    LinBindOne.setVisibility(View.VISIBLE);
////                    LinBindTwo.setVisibility(View.VISIBLE);
//                    dlg.setCanceledOnTouchOutside(false);
//                    DialogUtil.showUpdateAlert("", getString(R.string.dialog_change), getString(R.string.btn_cancel), getString(R.string.btn_confirm), getString(R.string.dialog_change_sure), listener, cancelListener, dlg);
//                } else {
                setPassWord(password);
//                }

            } else {
                toastFast("请输入正确的交易密码");
            }
        }
//        else {
//            if (passSure.equals("")||passSure.length()<6){
//                toastFast("请输入正确的交易密码");
//                return;
//            }
//        }
//        else {
//            dialog = Tools.getDialog(baseActivity);
//            dialog.setCanceledOnTouchOutside(false);
//            HttpParams pms = new HttpParams(API.BANKCARD.CONFIRM);
//            pms.addParam("password",passSure);
//            BackcardApi.confirm(this, pms, new IJsonResult(){
//
//                @Override

//                public void ok(Return json) {
//                    if (dialog != null) {
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                    }
//                    LinSetPass.setVisibility(View.GONE);
//                    LinBindCard.setVisibility(View.VISIBLE);
//                    topBar.setTitle("绑定银行卡");
//
//
//                }
//
//                @Override
//                public void error(Return json) {
//                    if (dialog != null) {
//                        if (dialog.isShowing()) {
//                            dialog.dismiss();
//                        }
//                    }
//                }
//            });
        //TODO 有密码时密码确认
//            if (passSure.equals("111111")){
//                if (bankModel.isBindBank){
//                    //TODO 跳转到支付页面
//                }else {
//                    //TODO 绑定银行卡操作
//                    LinSetPass.setVisibility(View.GONE);
//                    LinBindCard.setVisibility(View.VISIBLE);
//                    topBar.setTitle("绑定银行卡");
//                }
//            }
//        }
    }

    @OnClick(R.id.txtBankPaper)
    void setTxtBankPaper() {
        //TODO 跳转限额网页
        Intent intent = new Intent(this, WebViewActivity.class);
        intent.putExtra("url", API.BANKCARD.BANKQUOTA);
        startActivity(intent);
    }

    String mobile, bankCard, bindName, bindIDCard;

    @OnClick(R.id.btnBindBank)
    void setBtnBindBank() {
        //TODO 绑定银行卡接口
        mobile = txtBankPhone.getText().toString();
        bankCard = txtBankCard.getText().toString();
        bindName = txtBindName.getText().toString().trim();
        bindIDCard = txtBindIDCard.getText().toString().trim();

        HuifuCityModel cityModel = dbCache.GetObject(HuifuCityModel.class);
        if (Guard.isNull(cityModel)){
            cityModel = new HuifuCityModel();
        }

        if (Guard.isNull(bankInfo)) {
            bankInfo = new BankInfo();
        }
        if (Guard.isNullOrEmpty(bankInfo.getBankName())) {
            toastFast("请选择银行");
            return;
        }
        if (Guard.isNullOrEmpty(bankCard)) {
            toastFast("请输入银行卡号");
            return;
        }
        if (Guard.isNullOrEmpty(cityModel.getProvinceId())||Guard.isNullOrEmpty(cityModel.getCityId())){
            toastFast("请选择省市");
            return;
        }
        if (!ClientCheck.isValidLength(baseActivity, mobile, getResources().getString(R.string.tip_need_phone))) {
            return;
        }
        AccountReturn ac = dbCache.GetObject(AccountReturn.class);
        String pass = "";
        if (!Guard.isNull(ac.Account)) {
            pass = ac.Account.Password;
        }
        if (Guard.isNullOrEmpty(pass)) {
            toastFast("请先设置密码");
            showSetPass();
        } else {
            if (Guard.isNull(bankinfo.PnrInfo)) {
                payTo();
            } else {
                if (ClientStatus.getNetWork(baseActivity)) {
                    showPayDialog();
                }
            }
        }
    }

    void setPassWord(String pass) {
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
        pms.addParam("password", pass);
        BackcardApi.accountBank(baseActivity, baseActivity, pms, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                //成功
                AccountReturn ac = dbCache.GetObject(AccountReturn.class);
                ac.Account.Password = "*";
                acPassword = "*";
                dbCache.save(ac);
                toastFast("设置成功");
                showBind();
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
    }


    AlertDialog dlg;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dlg.dismiss();
            dlg.cancel();
//            setPassWord(txtPasswordConfirm.getText().toString());
            showPayDialog();

        }
    };
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            dlg.dismiss();
            dlg.cancel();
        }
    };

    void showPayDialog() {
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
        UserWithdrawFragment.popPasswordBox(baseActivity, "请输入交易密码", new IAsyncPassword() {
            @Override
            public void notify(String password) {
                asyncDraw(password);
            }
        });
//        mDialogWidget = new DialogWidget(BindBankCardActivity.this, getDecorViewDialog());
//        mDialogWidget.show();
    }

    /**
     * 提现密码弹窗
     */

    private View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayPasswordView.getInstance("请输入交易密码", "", BindBankCardActivity.this, new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(String password) {
                // TODO Auto-generated method stub

                asyncDraw(password);

            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub

            }
        }).getView();
    }

    void payTo() {
        if (ClientStatus.getNetWork(baseActivity)) {
            dlg.setCanceledOnTouchOutside(false);
            DialogUtil.showBindCardAlert(bindName, bindIDCard, bankCard, listener, cancelListener, dlg);
        }
    }

    private void asyncDraw(final String password) {//确认交易密码
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        bindName = txtBindName.getText().toString().trim();
        bindIDCard = txtBindIDCard.getText().toString().trim();
        HttpParams pms = new HttpParams(API.BANKCARD.CONFIRM);
        pms.addParam("password", password);
        pms.addParam("name", bindName);
        pms.addParam("idcard", bindIDCard);
        BackcardApi.confirm(baseActivity, this, pms, new IJsonResult() {
            @Override
            public void ok(Return json) {

                BindBank(password);
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
//                if (mDialogWidget != null) {
//                    if (mDialogWidget.isShowing()) {
//                        mDialogWidget.dismiss();
//                        mDialogWidget.cancel();
//                    }
//                }
//                BindBank(password);
            }
        });
    }

    void BindBank(String password) {
        HuifuCityModel cityModel = dbCache.GetObject(HuifuCityModel.class);
        if (Guard.isNull(cityModel)){
            cityModel = new HuifuCityModel();
        }
        HttpParams pms = new HttpParams(API.BANKCARD.BINDCARD);
        pms.addParam("password", password);
        pms.addParam("mobile", mobile);
        pms.addParam("bankcode", bankInfo.getBankcode());
        pms.addParam("cardno", bankCard);
        pms.addParam("province", cityModel.getProvinceId());
        pms.addParam("city", cityModel.getCityId());
        BackcardApi.bindcard(baseActivity, this, pms, new IJsonResult() {

            @Override
            public void ok(Return json) {
//                if (mDialogWidget != null) {
//                    if (mDialogWidget.isShowing()) {
//                        mDialogWidget.dismiss();
//                        mDialogWidget.cancel();
//                    }
//                }
                asyncBindStatus(true);
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
//                if (mDialogWidget != null) {
//                    if (mDialogWidget.isShowing()) {
//                        mDialogWidget.dismiss();
//                        mDialogWidget.cancel();
//                    }
//                }
                //TODO 当绑卡失败时弹框提示用户（身份证已经被使用）
            }
        });
    }

    static final int SELECT_BANK = 100;

    @OnClick(R.id.Lin_select_bank)
    void setLinSelectBank() {
        Intent intent = new Intent(this, SelectBankActivity.class);
        startActivityForResult(intent, SELECT_BANK);
    }

    BankInfo bankInfo;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == SELECT_BANK && resultCode == 111) {
                bankInfo = (BankInfo) data.getSerializableExtra("BankInfo");
                txtBankName.setText(bankInfo.getBankName());
                Tracer.e("BindBank", "id:" + bankInfo.getBankcode());
            }
        }
    }
}