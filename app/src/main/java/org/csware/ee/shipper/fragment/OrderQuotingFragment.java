package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.OrderListType;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.OrderBidFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yu on 2015/5/29.
 * 订单页 - 询价  报价中
 */
public class OrderQuotingFragment extends FragmentBase {

    final static String TAG = "OrderQuoting";

    @Override
    protected int getLayoutId() {
        return R.layout.order_quoting_fragment;
    }

    PullToRefreshListView _listView;
    private List<OrderListReturn.OrdersEntity> _orders = new ArrayList<>();
    private CommonAdapter<OrderListReturn.OrdersEntity> _jsonAdapter;

    int _pageSize = 10;
    int _pageIndex = 1;
    OrderListType _listType = OrderListType.NEW;

    ChinaAreaHelper areaHelper;

    // 标志位，标志已经初始化完成。
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

        if (!getIsPrepared() || !isVisible) {
            return;
        }
        isLoadData();
        //填充各控件的数据

    }

    void isLoadData(){
        SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isRefresh", false);
        Tracer.w(TAG, "onResume" +isRefresh);
        if (isRefresh) {
            _orders.clear();
            asyncLoadingData();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        isLoadData();

    }
    ProgressDialog dialog;
    @Override
    public void init() {

        areaHelper = new ChinaAreaHelper(baseActivity);
        _pageIndex = 1;
        SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isRefresh", false);
//        Log.w(TAG, "init"+isRefresh);
        if (!isRefresh) {
            asyncLoadingData();
        }
        _listView = (PullToRefreshListView) rootView.findViewById(R.id.listView);
        _listView.setMode(PullToRefreshBase.Mode.BOTH);

        _listView.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ListView>() {
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


        //set click
        _listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                Log.e(this.toString(), "position:" + position + "   id:" + id);
                dialog = Tools.getDialog(baseActivity);
                dialog.setCanceledOnTouchOutside(false);
                OrderApi.detail(baseFragment.getActivity(), baseActivity, (int) id, new IJsonResult<OrderDetailChangeReturn>() {
                    @Override
                    public void ok(OrderDetailChangeReturn json) {
                        if (dialog != null) {
                            if (dialog.isShowing()) {
                                dialog.dismiss();
                            }
                        }
                        String orderDetail = "";
                        if (!Guard.isNull(json)){
                            //成功
                            if (json.Result==0) {
                                orderDetail = GsonHelper.toJson(json);
                                Intent intent = new Intent(baseActivity, OrderBidFragmentActivity.class);
                                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
                                intent.putExtra(ParamKey.ORDER_DETAIL, orderDetail);
                                startActivity(intent);
                            }else {
                                toastFast(R.string.result_failed_net);
                            }
                        }else {
                            toastFast(R.string.result_failed_net);
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


//                if (ClientStatus.getNetWork(baseActivity)) {
//                    HttpParams pms = new HttpParams();
//                    pms.addParam("device", AppStatus.getDeviceId());
//                    pms.addParam("id", id);
//                    String url = pms.getUrl(API.ORDER.DETAIL);
//                    Log.d(TAG, url);
//                    final ProgressDialog dialog;
//                    dialog = createDialog(R.string.dialog_loading);
//                    FinalHttp fh = new FinalHttp();
//                    fh.get(url, new AjaxCallBack<String>() {
//
//                        @Override
//                        public void onSuccess(String res) {
//                            //Log.e(ParamKey.ORDER_DETAIL + "-107", "" + res);
//                            Return json = GsonHelper.fromJson(res, Return.class);
//                            if (json.Result == 0) {
//                                //成功
//                                Intent intent = new Intent(baseActivity, OrderBidFragmentActivity.class);
//                                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
//                                intent.putExtra(ParamKey.ORDER_DETAIL, res);
//                                startActivity(intent);
//                            } else {
//                                toastSlow(R.string.tip_inner_error);
//                            }
//                            dialog.dismiss();
//                        }
//
//                        @Override
//                        public void onFailure(Throwable t, int errCode, String strMsg) {
//                            //加载失败的时候回调
//                            Log.e("OrderQuotingFragment", "[errCode=" + errCode + "][strMsg=" + strMsg + "]");
//                            dialog.dismiss();
//                            toastSlow(R.string.tip_server_error);
//                        }
//                    });
//                }


            }
        });

        _jsonAdapter = new CommonAdapter<OrderListReturn.OrdersEntity>(
                getActivity(), _orders, R.layout.list_item_order_quoting) {

            /**重写，返回了序列元素的标识Id
             * 返回0时表示没有主键Id
             * */
            @Override
            public long getItemId(int position) {
                OrderListReturn.OrdersEntity item = _listData.get(position);
                if (item != null)
                    return item.getId();
                return 0;
            }

            @Override
            public void convert(ViewHolder helper, OrderListReturn.OrdersEntity item) {
                int FromId = item.getFrom();
                int ToId = item.getTo();
                String FromAdress = "",ToAdress = "";
                if (FromId>0) {
                    FromAdress = areaHelper.getCityName(Integer.toString(FromId));
                }
//                else if (FromId==0&&item.getPayees().size()>0)
//                {
//                    String f_str =  item.getPayees().get(0).getArea()+"";
//                    FromAdress =areaHelper.getCityName(f_str);
//                }

                if (item.getPayees()!=null) {
                    if (item.getPayees().size()>0) {
                        helper.getView(R.id.Lin_conllection_item).setVisibility(View.VISIBLE);
                        helper.setText(R.id.txt_collection_numbs, item.getPayees().size() + "");
                        String f_str = item.getPayees().get(0).getArea() + "";
                        ToAdress = areaHelper.getCityName(f_str);
                        if (item.getPayees().size() > 1) {
                            ToAdress = ToAdress + "等";
                        }
                    }
                }else
                {
                    ToAdress = areaHelper.getCityName(Integer.toString(ToId));
                    helper.getView(R.id.Lin_conllection_item).setVisibility(View.GONE);
                }
                if (Guard.isNull(item.getOwnerUser().getCompanyStatus())){
                    helper.getView(R.id.imgCompanyIcon).setVisibility(View.GONE);
                }else {
                    if (item.getOwnerUser().getCompanyStatus()==1){
                        helper.getView(R.id.imgCompanyIcon).setVisibility(View.VISIBLE);
                    }else {
                        helper.getView(R.id.imgCompanyIcon).setVisibility(View.GONE);
                    }
                }
                helper.setText(R.id.labFromArea,FromAdress );
                helper.setText(R.id.labToArea, ToAdress);
                helper.setText(R.id.labPublishTime, ParamTool.fromTimeSeconds(item.getFromTime()));
                helper.setText(R.id.labManAmount, Integer.toString(item.getBidsAmount()));
                helper.setText(R.id.labGoodsType, item.getGoodsType());
                helper.setText(R.id.labGoodsAmount, item.getGoodsAmount()+"");
                helper.setText(R.id.labUnit, item.getGoodsUnit());
                //司机报价人数处理
                if (item.getBidsAmount()==0){
                    helper.getView(R.id.txt_noManQuate).setVisibility(View.VISIBLE);
                    helper.getView(R.id.Lin_quateNum).setVisibility(View.GONE);
                }else {
                    helper.getView(R.id.txt_noManQuate).setVisibility(View.GONE);
                    helper.getView(R.id.Lin_quateNum).setVisibility(View.VISIBLE);
                }
                //Log.e(TAG, "item.getPriceCalType()=" + item.getPriceCalType());
                //TODO:零担时才显示表达式
                //getString(R.string.lab_price_style_truck).equals(item.getPriceType())||getString(R.string.lab_price_style_rmb_truck).equals(item.getPriceType())
                if (item.getPriceType().contains("包车")) {
                    //包车价
                    helper.setText(R.id.labTotalPrice, ParamTool.toString(item.getPrice()));
                    helper.setText(R.id.labPriceExpress, "");

                } else {
                    //零担计算价格
                    helper.setText(R.id.labTotalPrice, FormatHelper.toMoney(item.getPrice() * Double.valueOf(item.getGoodsAmount()))+"");

                }

                helper.setText(R.id.labQuotePrice, ParamTool.toString(item.getPrice()));
                String priceType = item.getPriceType();
                if (Guard.isNullOrEmpty(priceType)){
                    priceType = "元";
                }
                helper.setText(R.id.labQuoteUnit, priceType);

            }

        };

        // set adapter
        _listView.setAdapter(_jsonAdapter);


    }


    //这里必须要回调窗口
    void asyncLoadingData() {

        OrderApi.list(baseFragment.getActivity(), baseActivity, _listType, _pageSize, _pageIndex, new IJsonResult<OrderListReturn>() {
            @Override
            public void ok(OrderListReturn json) {
                //成功
                List<OrderListReturn.OrdersEntity> list = json.getOrders();
                if (list == null || list.size() == 0) {
//                    Log.d("OrderQuotingFramgment", "没有找到下一页数据");
                    _pageIndex--;
                    _jsonAdapter.notifyDataSetChanged();
                } else {
                    _orders.addAll(list); //将新数据加入
                    _jsonAdapter.notifyDataSetChanged();
                }
                _listView.onRefreshComplete(); //已经刷新完成
            }

            @Override
            public void error(Return json) {
                _listView.onRefreshComplete(); //已经刷新完成
            }
        });

    }


}
