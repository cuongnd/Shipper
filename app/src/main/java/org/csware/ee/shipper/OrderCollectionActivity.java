package org.csware.ee.shipper;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ScrollViewForListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/9/8.
 */
public class OrderCollectionActivity extends ActivityBase {

    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.not_txt)
    TextView notTxt;
    @InjectView(R.id.yet_txt)
    TextView yetTxt;
    @InjectView(R.id.not_list)
    ScrollViewForListView notList;
    @InjectView(R.id.yet_list)
    ScrollViewForListView yetList;

    private List<OrderDetailChangeReturn.OrderEntity.PayeesEntity> payeeList = new ArrayList<>();
    private List<OrderDetailChangeReturn.OrderEntity.PayeesEntity> payingList = new ArrayList<>();
    private List<OrderDetailChangeReturn.OrderEntity.PayeesEntity> payedList = new ArrayList<>();
    private CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity> PayeeAdapter;
    private CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity> PayingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_collection);
        ButterKnife.inject(this);
        String json = getIntent().getStringExtra("json");
        OrderDetailChangeReturn result = GsonHelper.fromJson(json, OrderDetailChangeReturn.class);
        //代收货款收货地址集
        if (!Guard.isNull(result)){
            if (!Guard.isNull(result.Order)){
                if (!Guard.isNull(result.Order.Payees)){
                    payeeList = result.Order.Payees;
                }
            }
        }

        if (payeeList.size()>0) {
            for (int i=0;i<payeeList.size();i++){
                if (payeeList.get(i).Status!=1){
                    payingList.add(payeeList.get(i));
                }else {
                    payedList.add(payeeList.get(i));
                }
            }
            PayeeAdapter = new org.csware.ee.shipper.adapter.CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity>(baseActivity, payeeList, R.layout.dshk_wsk_item) {

                /**
                 * 重写，返回了序列元素的标识Id
                 * 返回0时表示没有主键Id
                 */
                @Override
                public long getItemId(int position) {
                    OrderDetailChangeReturn.OrderEntity.PayeesEntity item = payeeList.get(position);
                    if (item != null)
                        return item.Id;
                    return 0;
                }

                // 出价列表数据填充
                @Override
                public void convert(ViewHolder helper, final OrderDetailChangeReturn.OrderEntity.PayeesEntity item, int position) {

                    helper.setText(R.id.name, item.Name + "");
                    helper.setText(R.id.mobile, item.Mobile + "");
                    helper.setText(R.id.amount_txt, item.Price + "");
                    helper.setText(R.id.prepaid_text,item.Paid+"");
                    helper.getView(R.id.view).setBackgroundColor(getResources().getColor(R.color.orange_red));
                    if (item.Status==1) {
                        helper.getView(R.id.img_jt).setVisibility(View.GONE);
                    }else{
                        helper.getView(R.id.img_jt).setVisibility(View.VISIBLE);
                    }
                }
            };
            PayingAdapter = new org.csware.ee.shipper.adapter.CommonAdapter<OrderDetailChangeReturn.OrderEntity.PayeesEntity>(baseActivity, payingList, R.layout.dshk_wsk_item) {

                /**
                 * 重写，返回了序列元素的标识Id
                 * 返回0时表示没有主键Id
                 */
                @Override
                public long getItemId(int position) {
                    OrderDetailChangeReturn.OrderEntity.PayeesEntity item = payingList.get(position);
                    if (item != null)
                        return item.Id;
                    return 0;
                }

                // 出价列表数据填充
                @Override
                public void convert(ViewHolder helper, final OrderDetailChangeReturn.OrderEntity.PayeesEntity item, int position) {

                    helper.setText(R.id.name, item.Name + "");
                    helper.setText(R.id.mobile, item.Mobile + "");
                    helper.setText(R.id.amount_txt, item.Price + "");
                    if (item.Status==1) {
                        helper.getView(R.id.img_jt).setVisibility(View.GONE);
                    }else{
                        helper.getView(R.id.img_jt).setVisibility(View.VISIBLE);
                    }
                }
            };
            notList.setAdapter(PayeeAdapter);
            yetList.setAdapter(PayingAdapter);

        }
    }
}
