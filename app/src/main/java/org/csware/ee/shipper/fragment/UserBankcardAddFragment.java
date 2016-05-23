package org.csware.ee.shipper.fragment;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ListHolder;
import com.orhanobut.dialogplus.OnItemClickListener;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BankcardResult;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.shipper.adapter.BankListAdapter;
import org.csware.ee.shipper.adapter.MyAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Yu on 2015/6/30.
 */
public class UserBankcardAddFragment extends FragmentBase {

    final static String TAG = "UserBankcardAdd";

    @InjectView(R.id.labBankName)
    TextView labBankName;
    @InjectView(R.id.optBankName)
    LinearLayout optBankName;
    @InjectView(R.id.txtOpenName)
    EditText txtOpenName;
    @InjectView(R.id.optOpenName)
    LinearLayout optOpenName;
    @InjectView(R.id.txtCardNo)
    EditText txtCardNo;
    @InjectView(R.id.txtRealname)
    EditText txtRealname;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;

    List<BankInfo> mlist = new ArrayList<>();

    @Override
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_bankcard_add_fragment;
    }

    DbCache dbCache;
    MeReturn userInfo;
    BankcardResult.BankCardsEntity bankCard;
    MyAdapter mMyAdapter;

    @Override
    public void init() {
        dbCache = new DbCache(baseActivity);
        bankCard = dbCache.GetObject(BankcardResult.BankCardsEntity.class);
        userInfo = dbCache.GetObject(MeReturn.class);
        mlist = BankInfoService.getBankData();
        mMyAdapter = new MyAdapter(baseActivity, mlist,false);
        if (bankCard == null) {
            bankCard = new BankcardResult.BankCardsEntity();
        }
        if (userInfo == null){
            userInfo = new MeReturn();
        }

        if (!Guard.isNullOrEmpty(bankCard.BankName)) {
            labBankName.setText(bankCard.BankName);
        }
        if (!Guard.isNullOrEmpty(bankCard.BankAddress)) {
            txtOpenName.setText(bankCard.BankAddress);
        }
        if (!Guard.isNullOrEmpty(bankCard.CardNo)) {
            txtCardNo.setText(bankCard.CardNo);
        }
        if (!Guard.isNull(userInfo.Owner)) {
            if (!Guard.isNullOrEmpty(userInfo.Owner.Name)) {
                txtRealname.setText(userInfo.Owner.Name);
            }
        }
        ((UserWalletFragmentActivity) getActivity()).backBank("添加银行卡");
    }

    @Override
    protected void lazyLoad() {
    }

    @OnClick({R.id.optBankName})
    void openBankList(View v) {

        OnItemClickListener itemClickListener = new OnItemClickListener() {
            @Override
            public void onItemClick(DialogPlus dialog, Object item, View view, int position) {
//                TextView tv = (TextView) view.findViewById(R.id.text_view);
//                if (!Guard.isNull(tv)) {
//                    bankCard.BankName = tv.getText().toString();
                Tracer.e(TAG,""+position);
                if (mlist.size()>0) {
                    bankCard.BankName = mlist.get(position - 1).getBankName();
                    dbCache.save(bankCard);
                    labBankName.setText(bankCard.BankName);
                }
//                }
                dialog.dismiss();
            }
        };

        BankListAdapter adapter = new BankListAdapter(baseActivity, R.array.bankcard_names, "");
        ListHolder holder = new ListHolder();
        final DialogPlus dialog = new DialogPlus.Builder(baseActivity)
                .setContentHolder(holder)
                .setHeader(R.layout.dialog_header)
                .setFooter(R.layout.dialog_footer_empty)
                .setCancelable(true)
                .setGravity(DialogPlus.Gravity.BOTTOM)
                .setAdapter(mMyAdapter)
                .setOnItemClickListener(itemClickListener)
                .create();
        dialog.setHeaderText(R.id.dialogTitle, "选择银行");//必须创建后更改
        dialog.show();
    }


    @OnClick({R.id.btnConfirm})
    void ok(View v) {
        bankCard.BankName = labBankName.getText().toString();
        bankCard.BankAddress = txtOpenName.getText().toString();
        bankCard.CardNo = txtCardNo.getText().toString();
        bankCard.Name = txtRealname.getText().toString();

        if (Guard.isNullOrEmpty(bankCard.BankName)) {
            toastFast("请选择一个银行");
            return;
        }
        if (Guard.isNullOrEmpty(bankCard.BankAddress)) {
            toastFast("请输入您的开户行");
            return;
        }
        if (Guard.isNullOrEmpty(bankCard.CardNo)) {
            toastFast("请输入卡号");
            return;
        }
        if (Guard.isNullOrEmpty(bankCard.Name)) {
            toastFast("请填写绑定人姓名");
            return;
        }

        BackcardApi.edit(baseFragment.getActivity(), baseActivity, ActionType.ADD, bankCard, new IJsonResult() {
            @Override
            public void ok(Return json) {
                //成功
                toastFast("添加成功");
//                ((FragmentActivityBase)baseFragment.getActivity()).delayedFinish(50); //TODO:这个有问题呀，关到首页去了
                ((UserWalletFragmentActivity) getActivity()).replace(new UserBankCardFragment(), "银行卡");
            }

            @Override
            public void error(Return json) {
                //失败
                toastFast("添加失败");
            }
        });



    }


}
