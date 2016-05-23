package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

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
import org.csware.ee.shipper.OrderDetailFragmentActivity;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.FormatHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Yu on 2015/5/29.
 * 订单页 - 运送中
 */
public class NewOrderShippingFragment extends FragmentBase {

    final static String TAG = "NewOrderShipping";
    PullToRefreshListView _listView;
    @InjectView(R.id.LinDataNone)
    LinearLayout LinDataNone;
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
            if (_jsonAdapter != null) {
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
                Tracer.e(TAG, "position:" + position + "   id:" + id);
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
                        if (!Guard.isNull(json)) {
                            //成功
                            if (json.Result == 0) {
                                orderDetail = GsonHelper.toJson(json);
                                Intent intent = new Intent(baseActivity, OrderDetailFragmentActivity.class);
                                if (isAdded()) {
                                    intent.putExtra(ParamKey.BACK_TITLE, getString(R.string.tab_order));
                                }
                                intent.putExtra(ParamKey.ORDER_DETAIL, orderDetail);
                                startActivity(intent);
                            } else {
                                toastFast(R.string.result_failed_net);
                            }
                        } else {
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
            }
        });

        _jsonAdapter = new CommonAdapter<OrderListReturn.OrdersEntity>(
                getActivity(), _orders, R.layout.list_order_item_shipping) {

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
                TextView collection = helper.getView(R.id.txt_collection_numbs);//代收货款人数
                TextView unReadLine = helper.getView(R.id.unReadLine);//可操作标签
                TextView labQuote = helper.getView(R.id.labQuote);//订单的状态
                unReadLine.setVisibility(View.VISIBLE);
                String FromAdress = "", ToAdress = "";
                if (FromId > 0) {
                    FromAdress = areaHelper.getCityName(Integer.toString(FromId));
                }
                if (item.getPayees() != null) {
                    if (item.getPayees().size() > 0) {
                        //显示代后货款
                        helper.getView(R.id.Lin_conllection_item).setVisibility(View.VISIBLE);
                        helper.getView(R.id.Lin_deliver_item).setVisibility(View.GONE);
                        //显示代后货款人数
                        SpannableString spanText = new SpannableString("代收货款：" + item.getPayees().size() + "人");
                        spanText.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.orange_red)), 5,
                                spanText.length() - 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//更改前景色
                        spanText.setSpan(new StyleSpan(Typeface.BOLD), 5, spanText.length() - 1,
                                Spannable.SPAN_INCLUSIVE_EXCLUSIVE);//加粗
                        collection.setText(spanText);
                        collection.setMovementMethod(LinkMovementMethod.getInstance());

                        String f_str = item.getPayees().get(0).getArea() + "";
                        ToAdress = areaHelper.getCityName(f_str);
                        if (item.getPayees().size() > 1) {
                            ToAdress = ToAdress + "等";
                        }
                    }
                } else {
                    if (!Guard.isNull(ToId)) {
                        ToAdress = areaHelper.getCityName(Integer.toString(ToId));
                        if (ToId==0){
                            ToAdress = "未知";
                        }
                    }
                    helper.getView(R.id.Lin_conllection_item).setVisibility(View.GONE);
                    helper.getView(R.id.Lin_deliver_item).setVisibility(View.VISIBLE);
                }
                if (Guard.isNull(item.getOwnerUser().getCompanyStatus())) {
                    helper.getView(R.id.imgCompanyIcon).setVisibility(View.GONE);
                } else {
                    if (item.getOwnerUser().getCompanyStatus() == 1) {
                        helper.getView(R.id.imgCompanyIcon).setVisibility(View.VISIBLE);
                    } else {
                        helper.getView(R.id.imgCompanyIcon).setVisibility(View.GONE);
                    }
                }

                //发货地
                helper.setText(R.id.labFromArea, FromAdress);
                //到达地
                helper.setText(R.id.labToArea, ToAdress);
                //发货时间
                Date date = new Date(Long.parseLong(String.valueOf(item.getFromTime())) * 1000);
                String time = Utils.getCurrentTime("MM月dd日", date);
                helper.setText(R.id.labFromAreaTime, time);
                //到达时间
                Date date2 = new Date(Long.parseLong(String.valueOf(item.getToTime())) * 1000);
                String time2 = Utils.getCurrentTime("MM月dd日", date2);
                helper.setText(R.id.labToAreaTime, time2);
                //货物类型
                helper.setText(R.id.labGoodsType, item.getGoodsType() + "：");
                //货物数量
                helper.setText(R.id.labGoodsAmount, item.getGoodsAmount() + "");
                //货物单位
                helper.setText(R.id.labUnit, item.getGoodsUnit());
                //成交价格
                String price = "";
                if (item.getDealPrice() > 0) {
                    price = FormatHelper.toMoney(item.getDealPrice()) + "";
                } else {
                    price = FormatHelper.toMoney(item.getPrice()) + "";
                }
                helper.setText(R.id.labQuotePrice, price);
                //备注
                if (!Guard.isNullOrEmpty(item.getMessage())) {
                    helper.setText(R.id.txtMemo, item.getMessage() + "");
                } else {
                    helper.setText(R.id.txtMemo, "未填写");
                }

                OrderListReturn.OrdersEntity.BearerEntity bearer = item.getBearer();
                OrderListReturn.OrdersEntity.BearerUserEntity userEntity = item.getBearerUser();
                if (userEntity == null)
                    userEntity = new OrderListReturn.OrdersEntity.BearerUserEntity();
                if (bearer == null) bearer = new OrderListReturn.OrdersEntity.BearerEntity();
                String Name = "";
                if (!Guard.isNull(item.BearerCompany)) {
                    int companyStatus;
                    if (Guard.isNull(userEntity.getCompanyStatus())) {
                        companyStatus = -1;
                    } else {
                        companyStatus = userEntity.getCompanyStatus();
                    }
                    if (!Guard.isNullOrEmpty(item.BearerCompany.CompanyName) && companyStatus == 1) {
                        Name = item.BearerCompany.CompanyName;
                    } else {
                        Name = bearer.getName();
                    }
                } else {
                    Name = bearer.getName();
                }
                helper.setText(R.id.textDriverName, Name);
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
                if (_orders.size() > 0) {
                    if (!Guard.isNull(LinDataNone)) {
                        LinDataNone.setVisibility(View.GONE);
                    }
                } else {
                    if (!Guard.isNull(LinDataNone)) {
                        LinDataNone.setVisibility(View.VISIBLE);
                    }
                }
                _listView.onRefreshComplete(); //已经刷新完成
            }

            @Override
            public void error(Return json) {
                _listView.onRefreshComplete(); //已经刷新完成
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }
}
