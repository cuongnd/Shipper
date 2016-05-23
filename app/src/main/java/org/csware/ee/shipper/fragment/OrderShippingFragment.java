package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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
import org.csware.ee.model.PaymentPointType;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.OrderDetailFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.EnumHelper;
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
 * 订单页 - 运送中
 */
public class OrderShippingFragment extends FragmentBase {

    final static String TAG = "OrderShipping";
    PullToRefreshListView _listView;
    private List<OrderListReturn.OrdersEntity> _orders = new ArrayList<>();
    private CommonAdapter<OrderListReturn.OrdersEntity> _jsonAdapter;

    int _pageSize = 10;
    int _pageIndex = 1;
    OrderListType _listType = OrderListType.SHIPPING;

    ChinaAreaHelper areaHelper;

    @Override
    protected int getLayoutId() {
        return R.layout.order_shipping_fragment;
    }

    // 标志位，标志已经初始化完成。
    protected boolean getIsPrepared() {
        return true;
    }

    @Override
    protected void lazyLoad() {

        if (!getIsPrepared() || !isVisible) {
            return;
        }
        //填充各控件的数据
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isRefresh", false);
        Tracer.e(TAG, "onResume" + isRefresh);
        if (isRefresh) {
            _orders.clear();
            if (_jsonAdapter!=null) {
                _jsonAdapter.notifyDataSetChanged();
            }
            asyncLoadingData();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();

    }

    ProgressDialog dialog;
    @Override
    public void init() {

        areaHelper = new ChinaAreaHelper(baseActivity);
        _pageIndex = 1;


        SharedPreferences preferences = baseActivity.getSharedPreferences("isRefresh", Context.MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isRefresh", false);
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
                                Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
                                intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
                                intent.putExtra(ParamKey.ORDER_DETAIL, orderDetail);
                                startActivity(intent);
                            }else {
                                toastFast(R.string.result_failed_net);
                            }
                        }else {
                            toastFast(R.string.result_failed_net);
                        }
                        //成功
//                        Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
//                        intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
//                        intent.putExtra(ParamKey.ORDER_DETAIL, GsonHelper.toJson(json));
//                        startActivity(intent);
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
//                                Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
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
                getActivity(), _orders, R.layout.list_item_order_shipping) {

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

            /**
             *
             * 列表数据填充
             *
             * */
            @Override
            public void convert(ViewHolder helper, final OrderListReturn.OrdersEntity item) {
//                helper.setText(R.id.labFromArea, areaHelper.getCityName(Integer.toString(item.getFrom())));
//                helper.setText(R.id.labToArea, areaHelper.getCityName(Integer.toString(item.getTo())));
                int FromId = item.getFrom();
                int ToId = item.getTo();
                String FromAdress = "",ToAdress = "";
                if (FromId>0) {
                    FromAdress = areaHelper.getCityName(Integer.toString(FromId));
                }
//                else {
//                    FromAdress = "地址无效";
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
//                else {
//                    ToAdress = "地址无效";
//                }
                helper.setText(R.id.labFromArea,FromAdress );
                helper.setText(R.id.labToArea, ToAdress);
                helper.setText(R.id.labSendTime, ParamTool.fromTimeSeconds(item.getCreateTime()));
                //helper.setText(R.id.labManAmount, Integer.toString(item.getBidsAmount()));
                helper.setText(R.id.labGoodsType, item.getGoodsType());
                helper.setText(R.id.labGoodsAmount, item.getGoodsAmount()+"");
                helper.setText(R.id.labUnit, item.getGoodsUnit());
                helper.setText(R.id.labPayPoint, EnumHelper.getCnName(item.getPayPoint(), PaymentPointType.class));
                helper.setText(R.id.labQuotePrice, FormatHelper.toMoney(item.getDealPrice()));
                helper.setText(R.id.txtMemo,item.getMessage()+"");
//                //TODO:零担时才显示表达式
//                if (getString(R.string.lab_carload).equals(item.getPriceCalType())) {
//                    //零担计算价格
//                    helper.setText(R.id.labTotalPrice,ParamTool.toString((item.getPrice() * item.getGoodsAmount())));
//                } else {
//                    //包车价
//                    helper.setText(R.id.labTotalPrice,ParamTool.toString((item.getPrice())));
//                }
                OrderListReturn.OrdersEntity.BearerEntity bearer = item.getBearer();
                OrderListReturn.OrdersEntity.BearerUserEntity userEntity = item.getBearerUser();
                if (userEntity==null) userEntity = new OrderListReturn.OrdersEntity.BearerUserEntity();
                if (bearer == null) bearer = new OrderListReturn.OrdersEntity.BearerEntity();
                String Name = "";
                if (!Guard.isNull(item.BearerCompany)){
                    int companyStatus;
                    if (Guard.isNull(userEntity.getCompanyStatus())){
                        companyStatus = -1;
                    }else {
                        companyStatus = userEntity.getCompanyStatus();
                    }
                    if (!Guard.isNullOrEmpty(item.BearerCompany.CompanyName)&& companyStatus == 1){
                        Name = item.BearerCompany.CompanyName;
                        helper.getView(R.id.labCPH).setVisibility(View.GONE);
                    }else {
                        Name = bearer.getName();
                        helper.getView(R.id.labCPH).setVisibility(View.VISIBLE);
                    }
                }else {
                    Name = bearer.getName();
                    helper.getView(R.id.labCPH).setVisibility(View.VISIBLE);
                }
                helper.setText(R.id.labName, Name);
                helper.setText(R.id.labCPH, bearer.getPlate());
                final OrderListReturn.OrdersEntity.BearerUserEntity finalUserEntity = userEntity;
                helper.getView(R.id.btnPhoneCall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Guard.isNullOrEmpty(finalUserEntity.getMobile())) {
                            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + finalUserEntity.getMobile()));
                            startActivity(nIntent);
                        }
                    }
                });
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
