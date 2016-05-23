package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;

import net.tsz.afinal.core.AsyncTask;

import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.csware.ee.Guard;
import org.csware.ee.api.AddressApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.AddressReturn;
import org.csware.ee.model.Code;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.Return;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.InputMethodLinearLayout;
import org.csware.ee.widget.PopupWindowFuzzy;
import org.csware.ee.widget.ScrollViewForListView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/8/17.
 */
public class DeliverSelectorAddressActivity extends FragmentActivityBase implements
        InputMethodLinearLayout.OnSizeChangedListenner {

    @InjectView(R.id.listview)
    ScrollViewForListView listview;
    @InjectView(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    @InjectView(R.id.labArea)
    TextView labArea;
    @InjectView(R.id.optArea)
    LinearLayout optArea;
    @InjectView(R.id.txtAddress)
    AutoCompleteTextView txtAddress;
    @InjectView(R.id.optAmount)
    LinearLayout optAmount;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @InjectView(R.id.topBar)
    TopActionBar topBar;

    final static String TAG = "SelectorAddress";
    ChinaAreaHelper ah;
    DbCache _dbCache;
    DeliverInfo _info;
    @InjectView(R.id.cboxSave)
    CheckBox cboxSave;
    private boolean _isFromArea;
    InputMethodManager manager;
    PopupWindowFuzzy fuzzy;
    private static Handler sHandler;
    int itemPos = 0;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_address_manage);
        ButterKnife.inject(this);
        manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        _isFromArea = getIntent().getBooleanExtra("isFrom", true);
        type = getIntent().getIntExtra("type",1);
        _dbCache = new DbCache(baseActivity);
        ah = new ChinaAreaHelper(baseContext);
        swipeRefresh.setColorSchemeResources(R.color.green, R.color.skyblue);
        try {
            _info = _dbCache.GetObject(DeliverInfo.class);
            if (_info == null) {
                Tracer.e(TAG, "NULL IFON");
                _info = new DeliverInfo();
            }
        } catch (Exception e) {
            Tracer.e(TAG, e.getMessage());
            //序列化错，重新创建对象
            _info = new DeliverInfo();
        }

        //隐藏底部Navigation Bar
        sHandler = new Handler();

        sHandler.post(mHideRunnable); // hide the navigation bar
        final View decorView = getWindow().getDecorView();
        decorView.setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                sHandler.post(mHideRunnable); // hide the navigation bar
            }
        });

        topBar.setTitle("选择地址");
        topBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                finish();
            }
        });
        adapter = new CommonAdapter<AddressReturn.AddressesEntity>(baseActivity, addressList, R.layout.item_listview) {
            /**重写，返回了序列元素的标识Id
             * 返回0时表示没有主键Id
             * */
            @Override
            public long getItemId(int position) {
                AddressReturn.AddressesEntity item = addressList.get(position);
                if (item != null)
                    return item.Id;
                return 0;
            }

            // 出价列表数据填充
            @Override
            public void convert(ViewHolder helper, AddressReturn.AddressesEntity item) {
                if (item == null) {

                } else {
                    //Tracer.e(TAG, "ID:" + item.Area);
                    helper.setText(R.id.item_name, item.Detail + "");
                    String[] names = ah.getName(item.Area);
                    helper.setText(R.id.item_name, ParamTool.join(names, ",") + " " + item.Detail);
                    helper.setImageResource(R.id.ivCheck,R.drawable.wyfh_dz_icon_djmr);
                }

            }
        };
        listview.setAdapter(adapter);
//        listview.setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        //android:fitsSystemWindows="true"
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                baseActivity.setTheme(R.style.ActionSheetStyle);
//                String[] names = ah.getName(addressList.get(position).Area);
//                labArea.setText(ParamTool.join(names, ",") + "");
//                _tmpCode = addressList.get(position).Area;
//                txtAddress.setText(addressList.get(position).Detail + "");
                cboxSave.setChecked(false);
                view.setEnabled(false);
//                if (itemPos>0) {
////                    int fetch;
////                    if(listview.getLastVisiblePosition()>=listview.getChildCount())
////                    {
////                        fetch=listview.getChildCount()-1-(listview.getLastVisiblePosition()-position);
////                    }else{fetch=addressPosition;}
//                    ArrayList<View> list = parent.getTouchables();
//                    for (int i=0;i<list.size();i++){
//                        if (i==addressPosition){
//                            ImageView bcCheck = (ImageView) list.get(i).findViewById(R.id.ivCheck);
//                            bcCheck.setImageResource(R.drawable.wyfh_dz_icon_djmr);
//                        }
//                    }
////                    for (View v : list) {
////                        //Log.e(TAG,"id:"+v.getId());
////                        LinearLayout chkBlue = (LinearLayout) v.findViewById(R.id.chkBlue);
////                        ImageView ivCheck = (ImageView) v.findViewById(R.id.ivCheck);
////                        chkBlue.setVisibility(View.GONE);
////                        ivCheck.setImageDrawable(getResources().getDrawable(R.drawable.wyfh_dz_icon_djmr));
////                    }
////                    View viewImage = listview.getChildAt(addressPosition);
////                    View viewImage = listview.getChildAt(addressPosition - listview.getFirstVisiblePosition());
////                    if (!Guard.isNull(viewImage)) {
////                        ImageView bcCheck = (ImageView) viewImage.findViewById(R.id.ivCheck);
////                        bcCheck.setImageResource(R.drawable.wyfh_dz_icon_djmr);
////                    }
//                }else {
//                    ImageView ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
//                    ivCheck.setImageResource(R.drawable.wyfh_dz_icon_djmr);
//                }
//                ImageView ivCheck = (ImageView) view.findViewById(R.id.ivCheck);
//                ivCheck.setImageResource(R.drawable.wyfh_dz_icon_mr);
//                itemPos++;
                addressPosition = position;
                ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                        .setCancelButtonTitle("取消")
                        .setOtherButtonTitles("选中", "删除")
                        .setCancelableOnTouchOutside(true)
                        .setListener(actionSheetListener).show();

            }
        });

        asyncLoadAddress();
        //地址回传处理
        optArea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(baseContext, AddressFragmentActivity.class), Code.AREA.toValue());
            }
        });


        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        swipeRefresh.setRefreshing(false);
                        asyncLoadAddress();
                    }
                }, 500);
            }
        });
        Rect frame = new Rect();
        getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        statusBarHeight = frame.top;
        lin_title_h = optAmount.getHeight();
//        suggest = new ArrayList<String>();
//        adapterLoc =new NeiLocationSearchAdapter(suggest,getLayoutInflater());
//        view = new LocationSearchtResults(getLayoutInflater(),optAmount.getWidth()-13);
        txtAddress.addTextChangedListener(new TextWatcher() {

            int index = 1;

            public void afterTextChanged(Editable editable) {
                try {
                    if (editable.length() > 1) {
                        String newText = editable.toString();
                        if (Guard.isNullOrEmpty(_tmpCode)) {
                            if (index <= 1) {
                                toastFast("请选择省市");
                            }

                        } else {
//                            ah.getCityName(_tmpCode);
                            if (!Guard.isNullOrEmpty(newText)) {

                                newText = ah.getCityName(_tmpCode) + "," + newText;
                                new getJson().execute(newText);
                            }
                        }
                        index++;
                    } else {
                        index = 1;
                    }
                } catch (IndexOutOfBoundsException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

        });

        txtAddress.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                /*判断是否是“GO”键*/
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    /*隐藏软键盘*/
                    InputMethodManager imm = (InputMethodManager) v
                            .getContext().getSystemService(
                                    Context.INPUT_METHOD_SERVICE);
                    if (imm.isActive()) {
                        imm.hideSoftInputFromWindow(
                                v.getApplicationWindowToken(), 0);
                    }
                    SelectDone();
                    return true;
                }
                return false;
            }
        });

    }

    int statusBarHeight, lin_title_h;
    public List<String> suggest = new ArrayList<String>();
    ProgressDialog dialog;

    public void getData(String detail, String city) {
        try {
            String url = "http://api.map.baidu.com/place/v2/suggestion?ak=8FkdiwtgqWqrkoXz9IQ9E7Vx&query=" + detail + "&region=" + city + "&output=json&qq-pf-to=pcqq.discussion";
            HttpClient hClient = new DefaultHttpClient();
            HttpGet hGet = new HttpGet(url);
            ResponseHandler<String> rHandler = new BasicResponseHandler();
            String data = "";
            try {
                data = hClient.execute(hGet, rHandler);
                if (!Guard.isNullOrEmpty(data)) {
                    JSONObject json;
                    //开始解析与保存信息
                    json = new JSONObject(data);
                    int result = json.getInt("status");
                    if (result == 0) {
                        String res = json.getString("result");
                        JSONArray jArray = new JSONArray(res);
                        for (int i = 0; i < jArray.length(); i++) {
                            JSONObject object = (JSONObject) jArray.get(i);
                            String SuggestKey = object.getString("name");
                            suggest.add(SuggestKey);
                        }
//                            Tracer.e(TAG,"sugest size:"+suggest.size());
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
//                Log.w("Error", e.getMessage());
        }
    }

    public ArrayAdapter<String> aAdapter;

    class getJson extends AsyncTask<String, String, String> {

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                aAdapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.text_item, suggest);
                txtAddress.setAdapter(aAdapter);
                txtAddress.setThreshold(1);
                if (suggest.size() > 0)
                    aAdapter.notifyDataSetChanged();
            } catch (IndexOutOfBoundsException error) {
                error.printStackTrace();
            }
        }

        @Override
        protected String doInBackground(String... key) {
            try {
                suggest.clear();
                String detail = "", city = "";
                if (key.length > 0) {
                    String newText = key[0];
                    Tracer.e(TAG, newText);
                    String[] strings = newText.split(",");
                    if (strings.length>0) {
                        city = strings[0];
                    }
                    if (strings.length > 0) {
                        detail = strings[1];
                    }
                    getData(detail, city);
                }
            } catch (IndexOutOfBoundsException error) {
                error.printStackTrace();
            }
            return null;
        }

    }

    /**
     * 隐藏底部栏Bar
     */
    Runnable mHideRunnable = new Runnable() {
        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        @Override
        public void run() {
            int flags;
            int curApiVersion = Build.VERSION.SDK_INT;
            // This work only for android 4.4+
            if (curApiVersion >= Build.VERSION_CODES.KITKAT) {
                // This work only for android 4.4+
                // hide navigation bar permanently in android activity
                // touch the screen, the navigation bar will not show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_IMMERSIVE;
//                        | View.SYSTEM_UI_FLAG_FULLSCREEN;

            } else {
                // touch the screen, the navigation bar will show
                flags = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            }

            // must be executed in main thread :)
            getWindow().getDecorView().setSystemUiVisibility(flags);
        }
    };
    //地址选择与处理开始
    private CommonAdapter<AddressReturn.AddressesEntity> adapter;
    int type = 1;
    List<AddressReturn.AddressesEntity> addressList = new ArrayList<>();

    int addressPosition = 0;
    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//            Log.d(TAG, "click item index = " + index);
            AddressReturn.AddressesEntity address = addressList.get(addressPosition);
            listview.setEnabled(true);
            if (index == 0) {
                if (_isFromArea) {
                    _info.fromId = address.Area;
                    _info.fromDetail = address.Detail;
                    _dbCache.save(_info);
                } else {
                    _info.toId = address.Area;
                    _info.toDetail = address.Detail;
                    _dbCache.save(_info);
                }
                Intent intent = new Intent();
                intent.putExtra("detail", address.Detail);
                intent.putExtra("tmpCode", address.Area);
                setResult(111, intent);
                finish();

            } else if (index == 1) {
                //删除
                AddressApi.edit(baseActivity, baseContext, address.Id, new IJsonResult() {
                    @Override
                    public void ok(Return json) {
                        //TODO:刷新列表
                        dialog = Tools.getDialog(baseActivity);
                        dialog.setCanceledOnTouchOutside(false);
                        asyncLoadAddress();
                    }

                    @Override
                    public void error(Return json) {

                    }
                });
            }
        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
//            Log.d(TAG, "dismissed isCancle = " + isCancle);
        }
    };

    /**
     * 异步获取数据缓存且更新UI
     */
    void asyncLoadAddress() {
        //开始异步同步数据
        AddressApi.list(baseActivity, baseContext, type, new IJsonResult<AddressReturn>() {
            @Override
            public void ok(AddressReturn json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                swipeRefresh.setRefreshing(false);
                addressList.clear();
                addressList.addAll(json.Addresses);
                adapter.notifyDataSetChanged();
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

    String _tmpArea;
    String _tmpCode;

    /**
     * 提交数据返回
     */
    @OnClick(R.id.btnConfirm)
    void setBtnConfirm() {
        SelectDone();
    }

    void SelectDone() {
        String address = txtAddress.getText().toString();
        Tracer.d(TAG, "tmpCode=" + _tmpCode + " tmpArea:" + _tmpArea + "");
        if (Guard.isNullOrEmpty(_tmpCode)) {
            toastFast("请选择城市！");
            return;
        }
        String detail = address.trim() + "";
        if (_isFromArea) {
            _info.fromId = _tmpCode;
            _info.fromDetail = detail;
//            labFrom.setText(_info.fromDetail);
        } else {
            _info.toId = _tmpCode;
            _info.toDetail = detail;
//            labTo.setText(_info.toDetail);
        }
        _dbCache.save(_info);
        if(cboxSave.isChecked()) {
            AddressApi.edit(baseActivity, baseContext, ActionType.ADD, _tmpCode, address.trim(), type, new IJsonResult() {
                @Override
                public void ok(Return json) {
                    //TODO:刷新列表
                    asyncLoadAddress();
                }

                @Override
                public void error(Return json) {
                }
            });
        }
        Intent intent = new Intent();
        intent.putExtra("detail", detail);
        intent.putExtra("tmpCode", _tmpCode);
        setResult(111, intent);
        finish();
    }

    @Override
    public void onSizeChange(boolean flag, int w, int h) {
        if (flag) {// 键盘弹出时
//            swipeRefresh.setVisibility(View.GONE);
        } else { // 键盘隐藏时
//            swipeRefresh.setVisibility(View.VISIBLE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (data != null && resultCode == Code.OK.toValue()) {
            if (requestCode == Code.AREA.toValue()) {
                if (labArea != null) {
                    String[] areas = data.getStringArrayExtra(ParamKey.AREA_ARRAY);
                    _tmpArea = TextUtils.join(",", areas);
                    _tmpCode = data.getStringExtra(ParamKey.AREA_CODE);
                    if (_tmpArea.equals("北京市,北京市,东城区"))
                        _tmpCode = "110101";
                    labArea.setText(_tmpArea);
                    Tracer.d(TAG, "requestCode:" + requestCode + " resultCode:" + resultCode + "   Code:" + data.getExtras().getString(ParamKey.AREA_CODE) + "result:" + GsonHelper.toJson(areas) + " _tmpArea:" + _tmpArea);
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
