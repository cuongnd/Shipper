package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.EvaluationInfo;
import org.csware.ee.model.EvaluationModel;
import org.csware.ee.model.JSONKey;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.EvaluationInfoService;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.URLUtils;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.ViewHolder;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by Arche on 2015/8/17.
 */
public class EvaluationRecordsActivity extends ActivityBase {


    private RelativeLayout btnBack;
    RatingBar rate_rating;
    private PullToRefreshListView listview_env;
    List<EvaluationModel.RatesEntity> list = new ArrayList<>();
    EvaluationModel evaluation;
    CommonAdapter<EvaluationModel.RatesEntity> mAdapter;
    int UserID;
    TextView entry;
    int _pageSize = 10;
    int _pageIndex = 1;
    private static final int Refresh = 120, LoadMore = 121;
    String seeBear = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_evaluation);
        findViews();
        Url(_pageIndex);

        mAdapter = new CommonAdapter<EvaluationModel.RatesEntity>(EvaluationRecordsActivity.this, list, R.layout.evaluation_item) {
            @Override
            public void convert(ViewHolder helper, final EvaluationModel.RatesEntity item) {
                helper.setText(R.id.name_txt, item.Name);
                helper.setText(R.id.message, Tools.getDecoderString(item.Message));

                String tzId = TimeZone.getDefault().getID();
                TimeZone tz = TimeZone.getTimeZone(tzId);

                String str = item.Time;
                String testStr = str.split("T")[0];
                String formatStr = "yyyy-MM-dd";
                String dateFromatStr = "yyyy-MM-dd";
//                String date=StringToDate(testStr, dateFromatStr,formatStr);
                Date e_date = new Date(Long.parseLong(item.Time) * 1000);
                helper.setText(R.id.txt_time, Utils.getCurrentTime("yyyy年MM月dd日 hh时", e_date));
                RatingBar ratingBar = helper.getView(R.id.rate_rating);
                double star = Float.parseFloat(item.Star + "");
                ratingBar.setRating(Float.parseFloat(star + ""));


            }

        };

        listview_env.setAdapter(mAdapter);

    }

    public static String StringToDate(String dateStr, String dateFormatStr, String formatStr) {
        DateFormat sdf = new SimpleDateFormat(dateFormatStr);
        Date date = null;
        try {
            date = sdf.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        SimpleDateFormat s = new SimpleDateFormat(formatStr);

        return s.format(date);
    }

    private void Url(int page) {
        DbCache dbCache = new DbCache(baseActivity);
        Shipper shipper = dbCache.GetObject(Shipper.class);
        if (shipper != null) {
            HttpParams params = null;
            HashMap<String, String> map = new HashMap<>();
            map.put("userid", String.valueOf(UserID));
            map.put("pagesize", String.valueOf(10));
            map.put("page", String.valueOf(page));
            if (Guard.isNullOrEmpty(seeBear)) {
//                getURLData(URLUtils.getURL(EvaluationRecordsActivity.this, map, API.USER.RATING), load);
                params = new HttpParams(API.USER.RATING);
            } else {
                if (seeBear.equals("BearerRate")) {
                    //看司机的评价
//                    getURLData(URLUtils.getURL(EvaluationRecordsActivity.this, map, API.USER.BEARRATING), load);
                    params = new HttpParams(API.USER.BEARRATING);
                } else if (seeBear.equals("OwnerRate")) {
                    //看货主
//                    getURLData(URLUtils.getURL(EvaluationRecordsActivity.this, map, API.USER.RATING), load);
                    params = new HttpParams(API.USER.RATING);
                } else if (seeBear.equals("PayperRate")) {
                    //看支付者
//                    getURLData(URLUtils.getURL(EvaluationRecordsActivity.this, map, API.USER.RATING), load);
                    params = new HttpParams(API.USER.RATING);
                }
            }
            if (!Guard.isNull(params)) {
                params.addParam("userid",UserID+"");
                params.addParam("pagesize","10");
                params.addParam("page",page+"");
                asynLoadingData(params);
            }
        }
    }

    void asynLoadingData(HttpParams params){
        OrderApi.rateRecord(baseActivity, baseActivity, params, new IJsonResult<EvaluationModel>() {
            @Override
            public void ok(EvaluationModel json) {
                List<EvaluationModel.RatesEntity> data = json.Rates;
                if (data == null || data.size() == 0) {
//                    Log.d(TAG, "没有找到下一页数据");
                    _pageIndex--;
                } else {
                    list.addAll(data); //将新数据加入
                    mAdapter.notifyDataSetChanged();
                }
                listview_env.onRefreshComplete(); //已经刷新完成
            }

            @Override
            public void error(Return json) {
                listview_env.onRefreshComplete(); //已经刷新完成
            }
        });
    }

    private void getURLData(final String url, final int load) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonStr = Tools.GetMethod(url);
                Tracer.e("Evaluation" + load, url + "\n" + jsonStr);
                Message message = new Message();
                message.arg1 = load;
                message.what = 100;
                message.obj = jsonStr;
                handler.sendMessage(message);
            }
        }).start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String json = (String) msg.obj;
            if (json == null || json.equals("网络异常")) {
                //onLoad();
                listview_env.onRefreshComplete();
                toastSlow(R.string.tip_server_error);
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    if (msg.what == 100) {
                        int result = jsonObject.getInt(JSONKey.RESULT);
                        if (result == 0) {
                            //chooseAdapter=1;
                            List<EvaluationInfo> data = new ArrayList<EvaluationInfo>();
                            String Rates = jsonObject.getString("Rates");
                            data = EvaluationInfoService.getEvaluationInfoService(Rates);
                            switch (msg.arg1) {
                                case Refresh:
                                    list.clear();
//                                    list.addAll(data);
                                    break;
                                case LoadMore:
//                                    list.addAll(data);
                                    break;
                            }
                            listview_env.onRefreshComplete();
                            mAdapter.notifyDataSetChanged();
                            if (list.size() > 0) {
                                entry.setText("(" + list.size() + ")");
                            } else {
                                entry.setText("(" + getString(R.string.btn_Nocomment) + ")");
                            }

                        } else {
                            String message = jsonObject.getString(JSONKey.MESSAGE);
                            String msgs = Guard.isNullOrEmpty(message) ? getString(R.string.tip_unknown_error) : message;
                            if (msgs.equals("货主状态错误")) {
                                msgs = "暂未认证,不能发货哟";
                            }
                            toastSlow(msgs);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    private void findViews() {
        Intent intent = getIntent();
        UserID = intent.getIntExtra("UsId", 0);
        seeBear = intent.getStringExtra("action");
        btnBack = (RelativeLayout) findViewById(R.id.btnBack);
        listview_env = (PullToRefreshListView) findViewById(R.id.listView);
        listview_env.setMode(PullToRefreshBase.Mode.BOTH);
        rate_rating = (RatingBar) findViewById(R.id.rate_rating);
        entry = (TextView) findViewById(R.id.entry);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview_env.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                _pageIndex = 1; //回到首页重新加载数据
                list.clear();
                Url(_pageIndex);
//                asyncLoadingData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                _pageIndex++;
                Url(_pageIndex);
            }
        });
    }

}
