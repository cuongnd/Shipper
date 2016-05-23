package org.csware.ee.shipper;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.consts.API;
import org.csware.ee.model.JSONKey;
import org.csware.ee.model.MessageInfo;
import org.csware.ee.service.MessageInfoService;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.URLUtils;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.ViewHolder;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Arche on 2015/9/16.
 */
public class MessageActivity extends ActivityBase {

    RelativeLayout btnBack;
    private PullToRefreshListView listview_env;
    public static final int LoadMore = 1;
    public static final int Refresh = 0;
    private int page = 1,pagesize=10;
    private Handler mHandler;
    List<MessageInfo> list = new ArrayList<MessageInfo>();
    CommonAdapter<MessageInfo> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.system_message);
        findViews();
        getURLData(getURL(page, pagesize), Refresh);
        mHandler = new Handler();

        mAdapter = new CommonAdapter<MessageInfo>(MessageActivity.this, list, R.layout.system_message_item) {
            @Override
            public void convert(ViewHolder helper, final MessageInfo item, final int postion) {
                helper.setText(R.id.title_txt, item.getTitle());
                helper.setText(R.id.message_txt, item.getMessage());
                Date e_date = new Date(Long.parseLong(item.getCreateTime()) * 1000);
                helper.setText(R.id.create_time, Utils.getCurrentTime("yyyy年MM月dd日", e_date));
            }
        };
        listview_env.setAdapter(mAdapter);
    }



    public String getURL(int page,int pagesize) {
        String url="";
        HashMap<String, String> map = new HashMap<>();
        map.put("pagesize", pagesize + "");
        map.put("page", page + "");
        url = URLUtils.getURL(MessageActivity.this, map, API.MESSAGE);

        return url;
    }

    private void getURLData(final String url,final int load) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String jsonStr = Tools.GetMethod(url);
//                Log.d("SysMessage", url + "\n" + jsonStr);
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
            if (json == null)
            {
                listview_env.onRefreshComplete();
                toastSlow(R.string.tip_server_error);
            }else {
                try {
                    JSONObject jsonObject = new JSONObject(json);
                    int result = jsonObject.getInt(JSONKey.RESULT);
                    if (result == 0) {
                        //....
                        List<MessageInfo> data = new ArrayList<MessageInfo>();
                        String message = jsonObject.getString("Messages");
                        data = MessageInfoService.getMessageInfoService(message);
                        switch (msg.arg1) {
                            case LoadMore:
                                list.addAll(data);
                                break;
                            case Refresh:
                                list.clear();
                                list.addAll(data);
                        }
                        listview_env.onRefreshComplete();
                        mAdapter.notifyDataSetChanged();
                    }else {
                        String resMesg = jsonObject.getString(JSONKey.MESSAGE);
                        String msgs = Guard.isNullOrEmpty(resMesg) ? getString(R.string.tip_unknown_error) : resMesg;
                        toastSlow(msgs);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };



    private void findViews(){
        btnBack = (RelativeLayout)findViewById(R.id.btnBack);
        listview_env = (PullToRefreshListView) findViewById(R.id.listview);
        listview_env.setMode(PullToRefreshBase.Mode.BOTH);
        btnBack.setOnClickListener(this);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        listview_env.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //Log.e("TODO-60", "下拉：回到首页，重获数据，并且如果有新的就加载到前面。");
                page = 1; //回到首页重新加载数据
                getURLData(getURL(1, pagesize), Refresh);
//                asyncLoadingData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                page++;
                //Log.e("TODO", "到底：下一页PageIndex=" + _pageIndex);
                getURLData(getURL(page, pagesize), LoadMore);
            }
        });
    }


}
