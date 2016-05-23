package org.csware.ee.shipper.fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BankcardResult;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.BankCardInfoActivity;
import org.csware.ee.shipper.BindBankCardActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserWalletFragmentActivity;
import org.csware.ee.shipper.adapter.MyAdapter;
import org.csware.ee.utils.DialogUtil;
import org.csware.ee.utils.Tools;
import org.csware.ee.widget.ActionSheet;
import org.csware.ee.widget.DelSlideListView;
import org.csware.ee.widget.ListViewonSingleTapUpListenner;
import org.csware.ee.widget.OnDeleteListioner;

import java.util.ArrayList;
import java.util.List;

import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.Optional;

/**
 * 不用下拉的银行卡列表
 * Created by Yu on 2015/6/30.
 */
public class UserBankCardFragment extends FragmentBase implements View.OnClickListener,OnDeleteListioner, ListViewonSingleTapUpListenner,
        ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener {
    final static String TAG = "UserFriend";
    @InjectView(R.id.listview)
    DelSlideListView mDelSlideListView;
//    RelativeLayout btn_Back;

    @InjectView(R.id.btnAddBankCard)
    @Optional
    LinearLayout btnAddBankCard;
    Shipper shipper;

    @Optional
    @OnClick({R.id.btnAddBankCard})
    void openActionSheet(View v) {
//        ((UserWalletFragmentActivity) getActivity()).replace(new UserBankcardAddFragment(), "添加银行卡");
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
        return R.layout.user_bankcard_fragment;
    }


    /**
     * ListView
     */
    private ListView listView;
    /**
     * ListView适配器
     */
    //private ListViewAdapter adapter;
    MyAdapter mMyAdapter;
    List<BankInfo> mlist = new ArrayList<BankInfo>();
    List<BankInfo> bankList = new ArrayList<BankInfo>();
    List<BankInfo> adapterList = new ArrayList<>();
    private List<BankcardResult.BankCardsEntity> infoList;
    int delID = 0;
    Dialog dlg;
    ProgressDialog dialog;
    ProgressDialog preDialog;
//    BankcardResult.BankCardsEntity bankCard;
    DbCache dbCache;

    @Override
    public void init() {
        ((UserWalletFragmentActivity) getActivity()).back(getString(R.string.mine_bankCard));
        dbCache = new DbCache(baseActivity);
//        bankCard = dbCache.GetObject(BankcardResult.BankCardsEntity.class);
        bankList = BankInfoService.getBankData();
//        Url();
        shipper = dbCache.GetObject(Shipper.class);
        if (Guard.isNull(shipper)){
            shipper = new Shipper();
        }
        alertDialog  = new AlertDialog.Builder(baseActivity).create();
        this.mDelSlideListView = (DelSlideListView) this.rootView.findViewById(R.id.listview);

//        getBindStatus();
//        mMyAdapter = new MyAdapter(baseActivity, mlist, true);
//        mDelSlideListView.setAdapter(mMyAdapter);
//        mDelSlideListView.setDeleteListioner(this);
//        mDelSlideListView.setSingleTapUpListenner(this);
//        mMyAdapter.setOnDeleteListioner(this);

//        btn_Back = (RelativeLayout)rootView.findViewById(R.id.btn_Back);
//        btn_Back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                ((UserWalletFragmentActivity) getActivity()).replace(new UserWalletFragment(), "");
//            }
//        });
    }
    BindStatusInfo bindStatusInfo;
    void getBindStatus(){
        preDialog = Tools.getDialog(baseActivity);
        preDialog.setCanceledOnTouchOutside(false);
        //TODO:获取绑卡状态
        HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
        BackcardApi.bindStatus(baseFragment.getActivity(), baseActivity, pms, new IJsonResult<BindStatusInfo>() {
            @Override
            public void ok(BindStatusInfo json) {
                if (preDialog != null) {
                    if (preDialog.isShowing()) {
                        preDialog.dismiss();
                    }
                }

                bindStatusInfo = json;
                dbCache.save(bindStatusInfo);
                if (Guard.isNull(bindStatusInfo.PnrInfo)){
                    shipper.isFirstCg = true;
                }else {
                    shipper.isFirstCg = false;
                }
                dbCache.save(shipper);
                if (!Guard.isNull(bindStatusInfo)){
                    if (bindStatusInfo.PnrCards.size()>0){
                        for (int i = 0; i < bindStatusInfo.PnrCards.size(); i++) {
                            for (int j=0;j<bankList.size();j++){
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
                if (!Guard.isNull(mDelSlideListView)) {
                    mMyAdapter = new MyAdapter(baseActivity, adapterList, true);
                    mDelSlideListView.setAdapter(mMyAdapter);
                    mDelSlideListView.setDeleteListioner(UserBankCardFragment.this);
                    mDelSlideListView.setSingleTapUpListenner(UserBankCardFragment.this);
                    mMyAdapter.setOnDeleteListioner(UserBankCardFragment.this);
                    mDelSlideListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(baseActivity,BankCardInfoActivity.class);
                            intent.putExtra("bankInfo",adapterList.get(position));
                            startActivityForResult(intent, 100);
                        }
                    });
                }else {
                    Tracer.e(TAG,Guard.isNull(mDelSlideListView)+"");
                }
//                mMyAdapter.notifyDataSetChanged();
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
    public void onResume() {
        super.onResume();
        adapterList.clear();
        if (Tools.getNetWork(baseActivity)) {
            getBindStatus();
        }else {
            toastFast(R.string.tip_need_net);
        }
//        Url();
    }

//    private void Url(){
//        DbCache dbCache = new DbCache(baseActivity);
//        //   try {
//        Shipper shipper = dbCache.GetObject(Shipper.class);
//        if (shipper != null) {
//            HashMap<String,String> map = new HashMap<>();
//            getURLData(URLUtils.getURL(getActivity(), map, API.BANKCARD.BINDER), 101);
//
//        }
//    }

//    private void getURLData(final String url,final int load) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String jsonStr = Tools.GetMethod(url);
//                Tracer.d(TAG + "BankList", url + "\n" + jsonStr);
//                Message message = new Message();
//                message.arg1 = load;
//                message.what = 100;
//                message.obj = jsonStr;
//                handler.sendMessage(message);
//            }
//        }).start();
//    }

//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String json = (String) msg.obj;
//            if (json == null||json.equals("网络异常"))
//            {
//                //onLoad();
//                toastSlow(R.string.tip_server_error);
//            }else {
//                try {
//                    List<BankInfo> data = new ArrayList<BankInfo>();
//                    JSONObject jsonObject = new JSONObject(json);
//                    int result = jsonObject.getInt(JSONKey.RESULT);
//
//                    if (result == 0) {
//                        //....
////                        Log.e("BankCard",json);
//                        String lines = jsonObject.getString("BankCards");
//                        data = BankInfoService.getBankInfoService(lines);
//                        //mMyAdapter = new MyAdapter(SetRoute.this,mlist);
//                        List<BankInfo> tmpList = BankInfoService.getBankData();
//                        if (tmpList.size()>data.size()&&data.size()>0) {
//                            mlist.clear();
//                            mlist.addAll(isContains(data, tmpList));
//                            mMyAdapter.notifyDataSetChanged();
//                        }
//                        //data.get(mlist.g)
//
//                    }
//                    else {
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//    };
//
//    public List<BankInfo> isContains(List<BankInfo> infoList1, List<BankInfo> infoList2){
//        List<BankInfo> tmpList = new ArrayList<>();
//        for (int i=0; i<infoList1.size(); i++){
//            for (int j=0; j<infoList2.size(); j++){
//                if (infoList1.get(i).getBankName().contains(infoList2.get(j).getBankName())){
//                    infoList2.get(j).setCardNo(infoList1.get(i).getCardNo());
//                    infoList2.get(j).setId(infoList1.get(i).getId());
//                    tmpList.add(infoList2.get(j));
//                }
//            }
//        }
//        return tmpList;
//    }

    @Override
    public void onSingleTapUp() {

    }


    @Override
    public void onClick(int whichButton) {

        switch (whichButton) {
            case 0:
                mlist.remove(delID);
                mDelSlideListView.deleteItem();
                mMyAdapter.notifyDataSetChanged();

                break;
            case 1:

                break;
        }

    }

    @Override
    public boolean isCandelete(int position) {
        return true;
    }

    @Override
    public void onDelete(final int ID) {
//        delete(ID);
        huifuDel(ID);
    }

    void huifuDel(final int ID){
        delID = ID;
        dlg = new Dialog(getActivity(), R.style.MyDialogStyleBottom);
        ActionSheet.showSheet(getActivity(), this, this, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dlg.dismiss();
                dialog = createDialog(R.string.dialog_loading);
//                Log.e("BankCard"+delID,mlist.size()+" name:"+mlist.get(ID).getBankName());
                final HttpParams pms = new HttpParams(API.BANKCARD.DELETECARD);
                pms.addParam("cardid", adapterList.get(ID).getId());
                BackcardApi.delete(baseFragment.getActivity(), baseActivity, pms, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        //成功
                        toastFast("删除成功");
                        adapterList.remove(delID);
                        mDelSlideListView.deleteItem();
                        mMyAdapter.notifyDataSetChanged();
                        dialog.dismiss();

//                        ((FragmentActivityBase) baseFragment.getActivity()).delayedFinish(50); //TODO:这个有问题呀，关到首页去了
                    }

                    @Override
                    public void error(Return json) {
                        //失败
                        toastFast("删除失败");
                        dialog.dismiss();
                    }
                });
            }
        }, dlg);

    }

//    void delete(final int ID){
//        delID = ID;
//        dlg = new Dialog(getActivity(), R.style.MyDialogStyleBottom);
//        ActionSheet.showSheet(getActivity(), this, this, new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                dlg.dismiss();
//                dialog = createDialog(R.string.dialog_loading);
////                Log.e("BankCard"+delID,mlist.size()+" name:"+mlist.get(ID).getBankName());
//                final HttpParams pms = new HttpParams(API.BANKCARD.EDIT);
//                pms.addParam("action", ActionType.DELETE.toName());
//                pms.addParam("name", mlist.get(ID).getName());
//                pms.addParam("bankname", mlist.get(ID).getBankName());
//                pms.addParam("bankaddress", mlist.get(ID).getBankAddress());
//                pms.addParam("cardno", mlist.get(ID).getCardNo());
//                pms.addParam("id", mlist.get(ID).getId()+"");
//
//                BackcardApi.delete(baseActivity, pms, new IJsonResult() {
//                    @Override
//                    public void ok(Return json) {
//                        //成功
//                        toastFast("删除成功");
//                        mlist.remove(delID);
//                        mDelSlideListView.deleteItem();
//                        mMyAdapter.notifyDataSetChanged();
//                        dialog.dismiss();
////                        ((FragmentActivityBase) baseFragment.getActivity()).delayedFinish(50); //TODO:这个有问题呀，关到首页去了
//                    }
//
//                    @Override
//                    public void error(Return json) {
//                        //失败
//                        toastFast("删除失败");
//                        dialog.dismiss();
//                    }
//                });
////                DbCache dbCache = new DbCache(baseActivity);
////                //   try {
////                Shipper shipper = dbCache.GetObject(Shipper.class);
////                if (shipper != null) {
////
////                    HashMap<String,String> map = new HashMap<>();
////                    map.put("action", "delete");
////                    map.put("name",mlist.get(ID).getName());
////                    map.put("bankname", mlist.get(ID).getBankName());
////                    map.put("bankaddress", mlist.get(ID).getBankAddress());
////                    map.put("cardno", mlist.get(ID).getCardNo());
////                    map.put("id", mlist.get(ID).getId()+"");
////                    Log.i("Setid "+ID," **");
////
////                    GetDelete(URLUtils.getURL(getActivity(), map, API.BANKCARD.EDIT), 101);
////
////                }
//            }
//        },dlg);
//    }

//    public void GetDelete(final String url, final int load){
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                String jsonStr = Tools.GetMethod(url);
//                //Log.e(TAG, jsonStr+"\n"+url);
////                Log.d(TAG+"Delete", url + "\n" + jsonStr);
//                Message message = new Message();
//                message.arg1 = load;
//                message.what = 100;
//                message.obj = jsonStr;
//                handlerDelete.sendMessage(message);
//            }
//        }).start();
//    }


//    Handler handlerDelete = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            String json = (String) msg.obj;
//            if(dialog.isShowing()) {
//                dialog.dismiss();
//            }
//            if (json.startsWith("{")&&json.length()>0) {
//                try {
//                    JSONObject jsonObject = new JSONObject(json);
////                    Log.i("Addjson",json+"");
//                    int result = jsonObject.getInt(JSONKey.RESULT);
//                    if (result == 0) {
//                        mlist.clear();
//
//                        HashMap<String,String> map = new HashMap<>();
//                        getURLData(URLUtils.getURL(getActivity(), map, API.BANKCARD.EDIT), 101);
//                        Toast.makeText(getActivity(), getResources().getString(R.string.delete_a_success), Toast.LENGTH_SHORT).show();
//
//                        //actionSheet.dlg.dismiss();
//                    }else {
//                        Toast.makeText(getActivity(), "删除失败", Toast.LENGTH_SHORT).show();
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }else {
//                Toast.makeText(getActivity(), getResources().getString(R.string.tip_server_error), Toast.LENGTH_SHORT).show();
//            }
//        }
//    };




    @Override
    public void onBack() {

    }



    @Override
    public void onClick(View v) {

    }

    @Override
    public void onCancel(DialogInterface dialog) {

    }
}
