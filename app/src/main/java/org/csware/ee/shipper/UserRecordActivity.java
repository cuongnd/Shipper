package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.RecordInfo;
import org.csware.ee.model.Return;
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
 * 交易明细列表
 * Created by zhou on 2016/4/7.
 */
public class UserRecordActivity extends ActivityBase {

    private static final String TAG = "UserRecordActivity";
    int _pageSize = 20;
    int _pageIndex = 1;
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.layoutOnRecord)
    LinearLayout layoutOnRecord;
    @InjectView(R.id.listViewRecord)
    PullToRefreshListView listViewRecord;
    private CommonAdapter<RecordInfo.AccountLogEntity> _jsonAdapter;
    private List<RecordInfo.AccountLogEntity> _orders = new ArrayList<>();
    boolean isfirst = true;
    String _Title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_list);
        ButterKnife.inject(this);
        topBar.setTitle("交易明细");
        listViewRecord.setMode(PullToRefreshBase.Mode.BOTH);
        //获取交易记录数据
        asyncLoadingData();
        listViewRecord.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                //Log.e("TODO-60", "下拉：回到首页，重获数据，并且如果有新的就加载到前面。");
                _pageIndex = 1; //回到首页重新加载数据
                _orders.clear();
                isfirst = false;
                asyncLoadingData();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                _pageIndex++;
                //Log.e("TODO", "到底：下一页PageIndex=" + _pageIndex);
                isfirst = false;
                asyncLoadingData();
            }
        });

        _jsonAdapter = new CommonAdapter<RecordInfo.AccountLogEntity>(this, _orders, R.layout.record_list_item) {
            @Override
            public void convert(ViewHolder helper, final RecordInfo.AccountLogEntity item, int position) {
                Date date = new Date(Long.parseLong(String.valueOf(item.Time)) * 1000);
                String time = Utils.getCurrentTime("yyyy.MM.dd  HH:mm", date);
                helper.setText(R.id.txtRecordTime, time);
                TextView itemTitle = helper.getView(R.id.txtRecordName);
                double recordMoney = item.Change;
                //交易金额 Change
                if (recordMoney > 0) {
                    helper.setText(R.id.txtRecordAmount, "+" + recordMoney, R.style.BlueFont);
                } else if (recordMoney < 0) {
                    helper.setText(R.id.txtRecordAmount, recordMoney + "", R.style.RedFont);
                } else {
                    helper.setText(R.id.txtRecordAmount, recordMoney + "", R.style.BlueFont);
                }
                //title 显示
                int type = item.Type;
                switch (type) {
                    case 0:
                        _Title = "未知";
                        itemTitle.setText("未知");
                        break;
                    case 1001:
                        _Title = "提现";
                        itemTitle.setText("提现");
                        break;
                    case 1002:
                        _Title = "提现";
                        itemTitle.setText("提现");
                        break;
                    case 1003:
                        _Title = "提现退回";
                        itemTitle.setText("提现退回");
                        break;
                    case 1004:
                        _Title = "提现退回";
                        itemTitle.setText("提现退回");
                        break;
                    case 10001:
                        _Title = "未知";
                        itemTitle.setText("未知");
                        break;
                    case 10002:
                        _Title = "支付运费";
                        itemTitle.setText("支付运费");
                        break;
                    case 10003:
                        _Title = "收到运费";
                        itemTitle.setText("收到运费");
                        break;
                    case 10004:
                        _Title = "支付运费";
                        itemTitle.setText("支付运费");
                        break;
                    case 20001:
                        _Title = "未知";
                        itemTitle.setText("未知");
                        break;
                    case 20002:
                        _Title = "代收货款支付";
                        itemTitle.setText("代收货款支付");
                        break;
                    case 20003:
                        _Title = "代收货款收入";
                        itemTitle.setText("代收货款收入");
                        break;
                    case 20004:
                        _Title = "代收货款支付";
                        itemTitle.setText("代收货款支付");
                        break;
                    case 20101:
                        _Title = "代收货款补贴";
                        itemTitle.setText("代收货款补贴");
                        break;
                    case 20102:
                        _Title = "代收货款补贴";
                        itemTitle.setText("代收货款补贴");
                        break;
                    case 20103:
                        _Title = "代收货款补贴";
                        itemTitle.setText("代收货款补贴");
                        break;
                    case 80001:
                        _Title = "油卡充值";
                        itemTitle.setText("油卡充值");
                        break;
                    case 80002:
                        _Title = "油卡充值退款";
                        itemTitle.setText("油卡充值退款");
                        break;
                    case 80003:
                        _Title = "油卡充值赎回";
                        itemTitle.setText("油卡充值赎回");
                        break;
                    case 80004:
                        _Title = "油卡充值";
                        itemTitle.setText("油卡充值");
                        break;
                    case 90001:
                        _Title = "红包";
                        itemTitle.setText("红包");
                        break;
                    case 90002:
                        _Title = "刮刮乐";
                        itemTitle.setText("刮刮乐");
                        break;
                    case 90003:
                        _Title = "大转盘";
                        itemTitle.setText("大转盘");
                        break;
                    default:
                        _Title = "未知";
                        itemTitle.setText("未知");
                        break;
                }
                /**
                 [EnumExt("其它")]
                 None = 0,

                 [EnumExt("货主提现")]
                 DrawOwner = 1001,
                 [EnumExt("车主提现")]
                 DrawBearer = 1002,
                 [EnumExt("货主提现退回")]
                 BacktrackOwner = 1003,
                 [EnumExt("车主提现退回")]
                 BacktrackBearer = 1004,


                 [EnumExt("订单充值")]
                 OrderRecharge = 10001,
                 [EnumExt("货主支付运费")]
                 OrderPay = 10002,
                 [EnumExt("车主收到运费")]
                 OrderIncome = 10003,

                 [EnumExt("代收货款充值")]
                 PayeeRecharge = 20001,
                 [EnumExt("支付代收货款")]
                 PayeePay = 20002,
                 [EnumExt("货主收到代收货款")]
                 PayeeIncome = 20003,

                 [EnumExt("货主代收货款奖励")]
                 PayeeBonusOwner = 20101,
                 [EnumExt("司机代收货款奖励")]
                 PayeeBonusBearer = 20102,
                 [EnumExt("付款方代收货款奖励")]
                 PayeeBonusPayer = 20103,

                 [EnumExt("油卡充值")]
                 OilCardPay = 80001,

                 [EnumExt("红包")]
                 GameRedPacket = 90001,
                 [EnumExt("刮刮卡")]
                 GameScratchCard = 90002,
                 [EnumExt("大转盘")]
                 GameFortuneWheel = 90003,
                 */
                //跳转到提现(1001)、支付运费(10002、10004)、代收货款支付(20002、20004)、代收货款收入(20003)、油卡充值(80001、80004)、油卡退款(80002)、油卡赎回(80003) 的详情页面
//                if ( type == 10002 ||  type == 20002 || type == 20003) {
                if (type == 1001 || type == 10002 || type==10004 || type == 20002 || type == 20004 || type == 20003 || type == 80001 || type == 80002 || type == 80003 || type == 80004 ) {
                    helper.getView(R.id.list_item_layout).setEnabled(true);
                    helper.getView(R.id.layoutIcon).setVisibility(View.VISIBLE);
                } else {
                    helper.getView(R.id.list_item_layout).setEnabled(false);
                    helper.getView(R.id.layoutIcon).setVisibility(View.INVISIBLE);
                }
                helper.getView(R.id.list_item_layout).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(UserRecordActivity.this, UserRecordDetailActivity.class);
                        intent.putExtra("DetailId", item.Id);
                        intent.putExtra("TypeID", item.Type);
                        intent.putExtra("Addon", item.Addon);
                        startActivity(intent);
                    }
                });
            }
        };
        listViewRecord.setAdapter(_jsonAdapter);
    }


    void asyncLoadingData() {
        HttpParams params = new HttpParams(API.BANKCARD.RECORD);
        params.addParam("page", _pageIndex);
        params.addParam("pagesize", _pageSize);
        BackcardApi.getRecordApi(baseActivity, baseActivity, params, new IJsonResult<RecordInfo>() {
            @Override
            public void ok(RecordInfo json) {
                //成功
                List<RecordInfo.AccountLogEntity> list = json.AccountLog;
                if (list == null || list.size() == 0) {
                    if (isfirst) {
                        listViewRecord.setVisibility(View.GONE);
                        layoutOnRecord.setVisibility(View.VISIBLE);
                    } else {
                        listViewRecord.setVisibility(View.VISIBLE);
                        layoutOnRecord.setVisibility(View.GONE);
                    }
                }
                if (list == null || list.size() == 0) {
                    _pageIndex--;
                    _jsonAdapter.notifyDataSetChanged();
                } else {
                    _orders.addAll(list); //将新数据加入
                    _jsonAdapter.notifyDataSetChanged();
                }
                Tracer.e(TAG, "Size : " + list.size() + "_orderSize" + _orders.size());
                listViewRecord.onRefreshComplete(); //已经刷新完成
            }

            @Override
            public void error(Return json) {
                listViewRecord.onRefreshComplete(); //已经刷新完成
            }
        });

    }

}
