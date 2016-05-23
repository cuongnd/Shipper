package org.csware.ee.shipper.fragment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.component.IAsyncPassword;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BankcardResult;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.BindBankCardActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.DialogUtil;
import org.csware.ee.utils.LayoutHelper;
import org.csware.ee.utils.Tools;
import org.csware.ee.widget.PayPasswordView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * 不用下拉的银行卡列表
 * Created by Yu on 2015/6/30.
 */
public class UserWithdrawBankcardFragment extends FragmentBase {
    final static String TAG = "WithdrawBankcard";


    @InjectView(R.id.listview)
    ListView listview;
    @InjectView(R.id.txtMoney)
    EditText txtMoney;
    @InjectView(R.id.btnCoupon)
    LinearLayout btnCoupon;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @InjectView(R.id.btnAddBankCard)
    LinearLayout btnAddBankCard;
//    private DialogWidget mDialogWidget;

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {
//        Log.d(TAG, "lazyLoad");


    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_withdraw_bankcard_fragment;
    }

    DbCache dbCache;
    AccountReturn ac;


    @OnClick(R.id.btnAddBankCard)
    void setBtnAddBankCard(){
        if (Tools.getNetWork(baseActivity)) {
            if (adapterList.size()<10) {
                Intent intent = new Intent(baseActivity, BindBankCardActivity.class);
                intent.putExtra("addBank", "addBank");
                intent.putExtra("mode", "add");
                startActivity(intent);
            }else {
                alertDialog.setCanceledOnTouchOutside(false);
                DialogUtil.showUpdateAlert("", getString(R.string.dialog_bind_limit), getString(R.string.btn_cancel), getString(R.string.btn_confirm), "提示", listener, cancelListener, alertDialog);
            }
        }else {
            toastFast(R.string.tip_need_net);
        }
    }
    AlertDialog alertDialog;
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.dismiss();
            alertDialog.cancel();

        }
    };
    View.OnClickListener cancelListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alertDialog.dismiss();
            alertDialog.cancel();
        }
    };

    /**
     * ListView
     */
    private ListView listView;

    /**
     * ListView适配器
     */
    private ListViewAdapter adapter;

    //    private List<BankcardResult.BankCardsEntity> infoList;
    List<BankInfo> bankList = new ArrayList<BankInfo>();
    List<BankInfo> adapterList = new ArrayList<>();

    int bankid;
    ProgressDialog preDialog;
    Shipper shipper;

    @Override
    public void init() {
        bankList = BankInfoService.getBankData();
//        infoList = new ArrayList<>();
        listView = (ListView) rootView.findViewById(R.id.listview);
        //listview.addFooterView(LayoutHelper.loadView(baseActivity, R.layout.sub_item_withdraw));//
        alertDialog  = new AlertDialog.Builder(baseActivity).create();
        adapter = new ListViewAdapter(baseActivity, adapterList);
        listView.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                friendItemOnC(parent, view, position);
            }
        });
        dbCache = new DbCache(baseActivity);
        ac = dbCache.GetObject(AccountReturn.class);
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)) {
            shipper = new Shipper();
        }
        if (ac != null) {
            if (!Guard.isNull(ac.Account.Amount) && ac.Account.Amount >= 100) {
                txtMoney.setHint("可提现金额" + ac.Account.Amount + "元");
            } else {
                txtMoney.setHint("提款金额必须不小于100元");
            }
        } else {
            txtMoney.setHint("可提现金额0元");
        }

//        syncLoadFriends();
        if (Tools.getNetWork(baseActivity)) {
            getBindStatus();
        } else {
            toastFast(R.string.tip_need_net);
        }
    }

    void friendItemOnC(AdapterView<?> parent, View view, int position) {
//        BankcardResult.BankCardsEntity bankInfo = infoList.get(position);
        BankInfo bankInfo = adapterList.get(position);
        bankid = bankInfo.getId();

        ArrayList<View> list = parent.getTouchables();
        for (View v : list) {
            //Log.e(TAG,"id:"+v.getId());
            LinearLayout chkBlue = (LinearLayout) v.findViewById(R.id.chkBlue);
            ImageView ivCheck = (ImageView) v.findViewById(R.id.ivCheck);
            chkBlue.setVisibility(View.GONE);
            ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.wyfh_dz_icon_djmr));
        }

//        BankcardResult.BankCardsEntity item = infoList.get(position);
        //Log.e(TAG, "index:" + position + "   item:" + item.Id);
        LinearLayout chkBlue = (LinearLayout) view.findViewById(R.id.chkBlue);
        ImageView ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
        chkBlue.setVisibility(View.VISIBLE);
        ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.wyfh_dz_icon_mr));
    }


    void syncLoadFriends() {

        BackcardApi.list(baseFragment.getActivity(), baseActivity, new IJsonResult<BankcardResult>() {
            @Override
            public void ok(BankcardResult json) {
                //成功
//                infoList.addAll(json.BankCards);
                adapter.notifyDataSetChanged();
                LayoutHelper.setListViewHeight(listView); //动态计算高度进行调整
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    BindStatusInfo bindStatusInfo;

    void getBindStatus() {
        //TODO:获取绑卡状态
        preDialog = Tools.getDialog(baseActivity);
        preDialog.setCanceledOnTouchOutside(false);
        HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
        BackcardApi.bindStatus(baseFragment.getActivity(), baseActivity, pms, new IJsonResult<BindStatusInfo>() {
            @Override
            public void ok(BindStatusInfo json) {
                if (preDialog != null) {
                    if (preDialog.isShowing()) {
                        preDialog.dismiss();
                    }
                }
                if (Guard.isNull(json.PnrInfo)) {
                    shipper.isFirstCg = true;
                } else {
                    shipper.isFirstCg = false;
                }
                dbCache.save(shipper);
                bindStatusInfo = json;
                dbCache.save(bindStatusInfo);
                if (!Guard.isNull(bindStatusInfo)) {
                    if (bindStatusInfo.PnrCards.size() > 0) {
                        for (int i = 0; i < bindStatusInfo.PnrCards.size(); i++) {
                            for (int j = 0; j < bankList.size(); j++) {
                                if (bindStatusInfo.PnrCards.get(i).BankCode.equals(bankList.get(j).getBankcode())) {
                                    BankInfo bankInfo = new BankInfo();
                                    bankInfo.setBankName(bankList.get(j).getBankName());
                                    bankInfo.setBankcode(bindStatusInfo.PnrCards.get(i).BankCode);
                                    bankInfo.setCardNo(bindStatusInfo.PnrCards.get(i).CardNo);
                                    bankInfo.setId(bindStatusInfo.PnrCards.get(i).Id);
                                    bankInfo.setBankResId(bankList.get(j).getBankResId());
                                    bankInfo.setDayLimit(bankList.get(j).getDayLimit());
                                    bankInfo.setSingleLimit(bankList.get(j).getSingleLimit());
                                    adapterList.add(bankInfo);
                                }
                            }
                        }
                    }
                }
                adapter.notifyDataSetChanged();
                LayoutHelper.setListViewHeight(listView);
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


    class ListViewAdapter extends ArrayAdapter<BankInfo> {

        private LayoutInflater inflater;

        public ListViewAdapter(Context context, List<BankInfo> list) {
            super(context, 0, list);
            inflater = LayoutInflater.from(context);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
//            BankcardResult.BankCardsEntity info = getItem(position);
            BankInfo info = getItem(position);
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.list_item_bankcard_check, null);
            }

            TextView name = (TextView) convertView.findViewById(R.id.labName);
            name.setText(info.getBankName());

            TextView cardNo = (TextView) convertView.findViewById(R.id.labCarNo);
            cardNo.setText(info.getCardNo());

            TextView labStyle = (TextView) convertView.findViewById(R.id.labStyle);
            labStyle.setText("储蓄卡");

            ImageView ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
//            ivIcon.setImageDrawable(ResourceHelper.getByName(baseActivity, R.array.bankcard_names, R.array.bankcard_icons, info.getBankName()));
            ivIcon.setImageResource(info.getBankResId());
            return convertView;
        }

    }


    String money;

    @OnClick({R.id.btnConfirm})
    void doWithdraw(View v) {
//        Log.d(TAG, "开始提现");
        money = txtMoney.getText().toString();
        if (bankid == 0) {
            toastFast("请先选择一张银行卡");
            return;
        }
        if (Guard.isNullOrEmpty(money)) {
            toastFast("请输入提现金额");
            return;
        }

        if (ac == null) {
            toastFast("金额不足，无法提现");
            return;
        }
        double m = 0;
        if (!Guard.isNullOrEmpty(money)) {
            m = Double.valueOf(money);
        }
        if (ac.Account.Amount < 100) {
            toastFast("余额不足100，无法提现");
            return;
        }
        if (m < 100) {
            toastFast("金额不足100，无法提现");
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("money",money+"");
        MobclickAgent.onEvent(baseActivity, "wallet_money_card", map);

        if (Guard.isNullOrEmpty(ac.Account.Password)) {

            UserWithdrawFragment.popPasswordBox(baseActivity, getString(R.string.tip_first_withdraw), new IAsyncPassword() {
                @Override
                public void notify(final String password) {

                    HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
                    pms.addParam("password", password);
                    pms.addParam("idcard", "");
                    pms.addParam("name", "");
                    BackcardApi.accountBank(baseFragment.getActivity(), baseActivity, pms, new IJsonResult() {
                        @Override
                        public void ok(Return json) {
                            //成功
                            //成功
                            ac.Account.Password = "*";
                            dbCache.save(ac);

                            asyncDraw(password);
                        }

                        @Override
                        public void error(Return json) {

                        }
                    });

                }
            });
//            mDialogWidget = new DialogWidget(getActivity(), setDecorViewDialog());
//            mDialogWidget.show();
        } else

            //开始提现
            if (ClientStatus.getNetWork(baseActivity)) {

            UserWithdrawFragment.popPasswordBox(baseActivity, "请输入支付密码", new IAsyncPassword() {
                @Override
                public void notify(String password) {
                    asyncDraw(password);
                }
            });
//                mDialogWidget = new DialogWidget(getActivity(), getDecorViewDialog());
//                mDialogWidget.show();
            }

    }

    /**
     * 设置密码弹窗
     */

    private View setDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayPasswordView.getInstance(getString(R.string.tip_first_withdraw), "", getActivity(), new PayPasswordView.OnPayListener() {

            @Override
            public void onSurePay(final String password) {
                // TODO Auto-generated method stub

                preDialog = Tools.getDialog(baseActivity);
                preDialog.setCanceledOnTouchOutside(false);
                HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
                pms.addParam("password", password);
                pms.addParam("idcard", "");
                pms.addParam("name", "");
                BackcardApi.accountBank(baseFragment.getActivity(), baseActivity, pms, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        if (preDialog != null) {
                            if (preDialog.isShowing()) {
                                preDialog.dismiss();
                            }
                        }
                        //成功
                        ac.Account.Password = "*";
                        dbCache.save(ac);

                        asyncDraw(password);
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


            }

            @Override
            public void onCancelPay() {
                // TODO Auto-generated method stub

            }
        }).getView();
    }

    /**
     * 提现密码弹窗
     */

    private View getDecorViewDialog() {
        // TODO Auto-generated method stub
        return PayPasswordView.getInstance(/*"请输入支付密码"*/getString(R.string.wallet_password), "", getActivity(), new PayPasswordView.OnPayListener() {

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


    /*
    *
    * 检验密码
    * */
    void asyncDraw(String password) {

        preDialog = Tools.getDialog(baseActivity);
        preDialog.setCanceledOnTouchOutside(false);
        BackcardApi.draw(baseFragment.getActivity(), baseActivity, password, null, null, Integer.toString(bankid), money, new IJsonResult() {
            @Override
            public void ok(Return json) {
                if (preDialog != null) {
                    if (preDialog.isShowing()) {
                        preDialog.dismiss();
                    }
                }
                toastSlow(R.string.withdraw_time_tip);

                if (ac.Account.Amount - Float.valueOf(money) >= 100) {
                    txtMoney.setHint("可提现金额" + ac.Account.Amount + "元");
                } else {
                    txtMoney.setHint("提款金额必须100起");
                }
                ac.Account.Amount = ac.Account.Amount - Float.valueOf(money);
                dbCache.save(ac);
                txtMoney.setHint("可提现金额" + ac.Account.Amount + "元");
                ((UserWalletFragmentActivity) getActivity()).finish();
            }

            @Override
            public void error(Return json) {
                if (preDialog != null) {
                    if (preDialog.isShowing()) {
                        preDialog.dismiss();
                    }
                }
//                if (!Guard.isNullOrEmpty(json.Message)) {
//                    toastSlow(json.Message);
//                }else {
//                    toastSlow("提现失败");
//                }
            }
        });

    }


}