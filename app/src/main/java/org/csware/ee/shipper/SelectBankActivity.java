package org.csware.ee.shipper;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Toast;

import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BankcardResult;
import org.csware.ee.model.JSONKey;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.shipper.adapter.MyAdapter;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.URLUtils;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.ActionSheet;
import org.csware.ee.widget.DelSlideListView;
import org.csware.ee.widget.ListViewonSingleTapUpListenner;
import org.csware.ee.widget.OnDeleteListioner;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/11/3.
 */
public class SelectBankActivity extends ActivityBase implements View.OnClickListener,OnDeleteListioner, ListViewonSingleTapUpListenner,
        ActionSheet.OnActionSheetSelected, DialogInterface.OnCancelListener{
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.listview)
    DelSlideListView listview;
    @InjectView(R.id.btnAddBankCard)
    LinearLayout btnAddBankCard;

    MyAdapter mMyAdapter;
    List<BankInfo> mlist = new ArrayList<BankInfo>();
    private List<BankcardResult.BankCardsEntity> infoList;
    int delID = 0;
    Dialog dlg;
    ProgressDialog dialog;
    BankcardResult.BankCardsEntity bankCard;
    DbCache dbCache;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_bankcard_fragment);
        ButterKnife.inject(this);
        topBar.setVisibility(View.VISIBLE);
        btnAddBankCard.setVisibility(View.GONE);
        topBar.setTitle("选择银行卡");
        init();

    }

    private void init() {
        dbCache = new DbCache(baseActivity);
        bankCard = dbCache.GetObject(BankcardResult.BankCardsEntity.class);
        mlist = BankInfoService.getBankData();
//        Url();

//        this.listview = (DelSlideListView) this.rootView.findViewById(R.id.listview);
        mMyAdapter = new MyAdapter(baseActivity, mlist,false);
        listview.setAdapter(mMyAdapter);
        listview.setDeleteListioner(this);
        listview.setSingleTapUpListenner(this);
        mMyAdapter.setOnDeleteListioner(this);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                //ParamTool.join(names, ",") + "\n" +
                intent.putExtra("BankInfo",mlist.get(position));
                setResult(111, intent);
                finish();
            }
        });
    }

    private void Url(){
        DbCache dbCache = new DbCache(baseActivity);
        //   try {
        Shipper shipper = dbCache.GetObject(Shipper.class);
        if (shipper != null) {
            HashMap<String,String> map = new HashMap<>();
            getURLData(URLUtils.getURL(this, map, API.BANKCARD.LIST), 101);

        }
    }

    private void getURLData(final String url,final int load) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonStr = Tools.GetMethod(url);
//                Log.d(TAG+"BankList", url + "\n" + jsonStr);
                Message message = new Message();
                message.arg1 = load;
                message.what = 100;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        }).start();
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            if (json == null||json.equals("网络异常"))
            {
                //onLoad();
                toastSlow(R.string.tip_server_error);
            }else {
                try {
                    List<BankInfo> data = new ArrayList<BankInfo>();
                    JSONObject jsonObject = new JSONObject(json);
                    int result = jsonObject.getInt(JSONKey.RESULT);

                    if (result == 0) {
                        //....
//                        Log.e("BankCard",json);
                        String lines = jsonObject.getString("BankCards");
                        data = BankInfoService.getBankInfoService(lines);
                        //mMyAdapter = new MyAdapter(SetRoute.this,mlist);
                        mlist.clear();
                        mlist.addAll(data);
                        mMyAdapter.notifyDataSetChanged();
                        //data.get(mlist.g)

                    }
                    else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    @Override
    public void onSingleTapUp() {

    }
    @Override
    public boolean isCandelete(int position) {
        return true;
    }

    @Override
    public void onDelete(final int ID) {
        delID = ID;
//        dlg = new Dialog(this, R.style.MyDialogStyleBottom);
//        ActionSheet.showSheet(this, this, this, new View.OnClickListener() {
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
//                pms.addParam("id", mlist.get(ID).getId() + "");
//
//                BackcardApi.delete(baseActivity, pms, new IJsonResult() {
//                    @Override
//                    public void ok(Return json) {
//                        //成功
//                        toastFast("删除成功");
//                        mlist.remove(delID);
//                        listview.deleteItem();
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
//
//            }
//        }, dlg);

    }

    Handler handlerDelete = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            if(dialog.isShowing()) {
                dialog.dismiss();
            }
            if (json.startsWith("{")&&json.length()>0) {
                try {
                    JSONObject jsonObject = new JSONObject(json);
//                    Log.i("Addjson",json+"");
                    int result = jsonObject.getInt(JSONKey.RESULT);
                    if (result == 0) {
                        mlist.clear();

                        HashMap<String,String> map = new HashMap<>();
                        getURLData(URLUtils.getURL(SelectBankActivity.this, map, API.BANKCARD.EDIT), 101);
                        toastFast(getResources().getString(R.string.delete_a_success));

                        //actionSheet.dlg.dismiss();
                    }else {
                        toastFast("删除失败");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else {
                toastFast(getResources().getString(R.string.tip_server_error));
            }
        }
    };

    @Override
    public void onBack() {

    }


    @Override
    public void onCancel(DialogInterface dialog) {

    }

    @Override
    public void onClick(int whichButton) {

    }
}
