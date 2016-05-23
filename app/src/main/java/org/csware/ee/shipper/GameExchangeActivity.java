package org.csware.ee.shipper;

import android.os.Bundle;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.csware.ee.Guard;
import org.csware.ee.api.GameApi;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.ScoreInfo;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/11/19.
 */
public class GameExchangeActivity extends ActivityBase {

    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.listView)
    PullToRefreshListView listView;

    int _pageSize = 10;
    int _pageIndex = 1;
    private CommonAdapter<ScoreInfo.ScoreLogsEntity> _jsonAdapter;
    private List<ScoreInfo.ScoreLogsEntity> _orders = new ArrayList<>();
    String title="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange);
        ButterKnife.inject(this);

        title = getIntent().getStringExtra("Title");
        if (!Guard.isNullOrEmpty(title)){
            topBar.setTitle(title);
        }
        listView.setMode(PullToRefreshBase.Mode.BOTH);
        asyncLoadingData();
        listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //Log.e("TODO-60", "下拉：回到首页，重获数据，并且如果有新的就加载到前面。");
                _pageIndex = 1; //回到首页重新加载数据
                _orders.clear();
                asyncLoadingData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                _pageIndex++;
                //Log.e("TODO", "到底：下一页PageIndex=" + _pageIndex);
                asyncLoadingData();
            }
        });

        _jsonAdapter = new CommonAdapter<ScoreInfo.ScoreLogsEntity>(this, _orders, R.layout.ac_integral_detail) {
            @Override
            public void convert(ViewHolder helper, ScoreInfo.ScoreLogsEntity item, int position) {
                helper.setText(R.id.txtGameName,item.Addon);
                Date date = new Date(Long.parseLong(String.valueOf(item.Time))* 1000) ;
                String time  = Utils.getCurrentTime("yyyy年MM月dd日 HH:mm", date);
                helper.setText(R.id.txtGameTime,time);
                double score = item.Change;
                String res = "";
                if (score>0){
                    res = "+" +(int) score;
                    helper.setText(R.id.txtGameAmount, res, R.style.RedFont);
                }else {
                    res = (int) score+"";
                    helper.setText(R.id.txtGameAmount, res, R.style.BlueFont);
                }
            }
        };
        listView.setAdapter(_jsonAdapter);
    }




    void asyncLoadingData() {
        HttpParams params = null;
        if (title.contains("积分")){
            params = new HttpParams(API.GAME.SCORELOG);
        }else {
            params = new HttpParams(API.GAME.SCORELOG);
        }
        params.addParam("page",_pageIndex);
        params.addParam("pagesize",_pageSize);
        GameApi.score(baseActivity, baseActivity, params, new IJsonResult<ScoreInfo>() {
            @Override
            public void ok(ScoreInfo json) {
                //成功
                List<ScoreInfo.ScoreLogsEntity> list = json.ScoreLogs;
                if (list == null || list.size() == 0) {
//                    Log.d("OrderQuotingFramgment", "没有找到下一页数据");
                    _pageIndex--;
                    _jsonAdapter.notifyDataSetChanged();
                } else {
                    _orders.addAll(list); //将新数据加入
                    _jsonAdapter.notifyDataSetChanged();
                }
                listView.onRefreshComplete(); //已经刷新完成
            }

            @Override
            public void error(Return json) {
                listView.onRefreshComplete(); //已经刷新完成
            }
        });

    }

}
