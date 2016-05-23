package org.csware.ee.shipper;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.api.OrderApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.InsuranceModel;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.ImageByAndroid;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.FlowLayout;
import org.csware.ee.widget.ScrollViewForGridView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import me.xiaopan.switchbutton.SwitchButton;

/**
 * Created by Administrator on 2015/9/16.
 */
public class InsuranceActivity extends ActivityBase {

    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.chkInsurance)
    SwitchButton chkInsurance;
    @InjectView(R.id.list)
    ScrollViewForGridView gridView;
    @InjectView(R.id.Lin_goods_type)
    LinearLayout LinGoodsType;
    CommonAdapter<InsuranceModel.InsuranceTypesEntity> parentGridAdapter;
    CommonAdapter<InsuranceModel.InsuranceTypesEntity.ChildrenEntity.ChildrenChEntity> adapter;
    List<InsuranceModel.InsuranceTypesEntity> insuranceModels = new ArrayList<>();
    List<InsuranceModel.InsuranceTypesEntity.ChildrenEntity.ChildrenChEntity> insuranceList = new ArrayList<>();
    @InjectView(R.id.listItem)
    GridView listItem;
    //    @InjectView(R.id.recycler_view)
//    RecyclerView recyclerView;
    DbCache _dbCache;
    DeliverInfo _info;
    MasonryAdapter recyclerAdapter;
    @InjectView(R.id.flowlayout)
    FlowLayout flowlayout;

    private CompoundButton.OnCheckedChangeListener checkChangeListener = new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                LinGoodsType.setVisibility(View.VISIBLE);
//                asyncLoadingData();
            } else {
                LinGoodsType.setVisibility(View.GONE);
                _info.insurance = "";
                _dbCache.save(_info);
                finish();
            }
        }
    };

    void asyncLoadingData() {
        insuranceModels.clear();
        insuranceList.clear();
        OrderApi.getInsurance(baseActivity, this, new IJsonResult<InsuranceModel>() {
            @Override
            public void ok(InsuranceModel json) {
                //成功
                insuranceModels.addAll(json.getInsuranceTypes());
                parentGridAdapter.notifyDataSetChanged();

            }

            @Override
            public void error(Return json) {
                toastFast("没有找到数据");
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ac_insurance);
        ButterKnife.inject(this);
//        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        topBar.setTitle("保险");
        chkInsurance.setChecked(true);
        chkInsurance.setOnCheckedChangeListener(checkChangeListener);
        _dbCache = new DbCache(baseActivity);
        try {
            _info = _dbCache.GetObject(DeliverInfo.class);
            if (_info == null) {
                _info = new DeliverInfo();
            }
        } catch (Exception e) {
            //序列化错，重新创建对象
            _info = new DeliverInfo();
        }
        WindowManager manger = getWindowManager();
        Display display = manger.getDefaultDisplay();

        //屏幕高度
        int screenHeight = display.getHeight();
        //屏幕宽度
        final int screenWidth = display.getWidth();
        asyncLoadingData();

        parentGridAdapter = new CommonAdapter<InsuranceModel.InsuranceTypesEntity>(this, insuranceModels, R.layout.tv) {
            @Override
            public void convert(ViewHolder helper, InsuranceModel.InsuranceTypesEntity item, int position) {
                helper.setText(R.id.text_view, item.getContent() + "");
            }
        };
        gridView.setAdapter(parentGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                insuranceList = insuranceModels.get(position).getChildren().get(0).getChildren();
                for(int i=0;i<parent.getCount();i++){
                    View v=parent.getChildAt(i);
                    if (position == i) {//当前选中的Item改变背景颜色

                        ((TextView) v).setTextColor(getResources().getColor(R.color.orange_red));
                        ((TextView) v).setBackgroundResource(R.drawable.opt_orange_red);
                    } else {
                        ((TextView) v).setBackgroundResource(R.drawable.opt_grey);
                        ((TextView) v).setTextColor(getResources().getColor(R.color.grey));
                    }
                }

//                textView.setTypeface(Typeface.DEFAULT, R.style.EdgeOnFont);


                parentGridAdapter.notifyDataSetChanged();
                if (insuranceList.size() > 0) {
                    adapter = new CommonAdapter<InsuranceModel.InsuranceTypesEntity.ChildrenEntity.ChildrenChEntity>(InsuranceActivity.this, insuranceList, R.layout.label_grid_item) {
                        @Override
                        public void convert(ViewHolder helper, InsuranceModel.InsuranceTypesEntity.ChildrenEntity.ChildrenChEntity item, int position) {
                            helper.setText(R.id.text_view, item.getContent());
                        }
                    };
                    listItem.setAdapter(adapter);
                    listItem.setOnItemClickListener(itemListener);
                    flowlayout.removeAllViews();
                    initData();
                } else {
                    flowlayout.removeAllViews();
                    insuranceList.clear();
                    adapter = new CommonAdapter<InsuranceModel.InsuranceTypesEntity.ChildrenEntity.ChildrenChEntity>(InsuranceActivity.this, insuranceList, R.layout.label_grid_item) {
                        @Override
                        public void convert(ViewHolder helper, InsuranceModel.InsuranceTypesEntity.ChildrenEntity.ChildrenChEntity item, int position) {
                            helper.setText(R.id.text_view, item.getContent());
                        }
                    };
                    listItem.setAdapter(adapter);
                    if (insuranceModels.get(position).getContent().equals("矿产")){
                        _info.insurance = insuranceModels.get(position).getChildren().get(0).getId();
                        _dbCache.save(_info);
                        finish();
                    }else {
                        toastFast("该类型暂无保险");
                    }
                    return;
                }
//设置layoutManager
//                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
//                Log.e("Recycler",list.get(4).getGoodsInsurance().size()+"");
//                recyclerAdapter = new MasonryAdapter(InsuranceActivity.this, list.get(4).getGoodsInsurance());
//                recyclerView.setAdapter(recyclerAdapter);
//                // 设置item动画
//                recyclerView.setItemAnimator(new DefaultItemAnimator());
//                //设置item之间的间隔
//                SpacesItemDecoration decoration = new SpacesItemDecoration(16);
//                recyclerView.addItemDecoration(decoration);
//                recyclerView.setHasFixedSize(false);
//                recyclerAdapter.setOnItemClickLitener(new MasonryAdapter.OnItemClickLitener() {
//                    @Override
//                    public void onItemClick(View view, int position) {
//                        Toast.makeText(InsuranceActivity.this,
//                                position + " click", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onItemLongClick(View view, int position) {
//                        Toast.makeText(InsuranceActivity.this,
//                                position + " long click", Toast.LENGTH_SHORT).show();
//                    }
//                });

            }
        });

    }

    AdapterView.OnItemClickListener itemListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Log.e("Insurance item", insuranceList.get(position).getId() + "");
            _info.insurance = insuranceList.get(position).getId();
            _dbCache.save(_info);
            finish();
        }
    };

    public void initData()
    {

        LayoutInflater mInflater = LayoutInflater.from(this);
        for (int i = 0; i < insuranceList.size(); i++)
        {
            final TextView tv = (TextView) mInflater.inflate(R.layout.tv,
                    flowlayout, false);
            tv.setText(insuranceList.get(i).getContent() + "");
            final String insuranceId = insuranceList.get(i).getId();
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    tv.setTypeface(Typeface.DEFAULT,R.style.EdgeOnFont);
                    _info.insurance = insuranceId;
//                    Log.e("InsuranceActivity",insuranceId);
                    _dbCache.save(_info);
                    finish();
                }
            });
            flowlayout.addView(tv);
        }

    }

    public class SpacesItemDecoration extends RecyclerView.ItemDecoration {

        private int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }

    static class MasonryAdapter extends RecyclerView.Adapter<MasonryAdapter.MasonryView> {
        private List<String> products;
        private LayoutInflater mInflater;
        private List<Integer> mHeights;

        public interface OnItemClickLitener {
            void onItemClick(View view, int position);

            void onItemLongClick(View view, int position);
        }

        private OnItemClickLitener mOnItemClickLitener;

        public void setOnItemClickLitener(OnItemClickLitener mOnItemClickLitener) {
            this.mOnItemClickLitener = mOnItemClickLitener;
        }

        public MasonryAdapter(Context context, List<String> list) {
            products = list;
            mInflater = LayoutInflater.from(context);
            mHeights = new ArrayList<Integer>();
            for (int i = 0; i < products.size(); i++) {
                mHeights.add((int) (100 + Math.random() * 300));
            }
        }

        @Override
        public MasonryView onCreateViewHolder(ViewGroup viewGroup, int i) {
//            View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.label_grid_item, viewGroup, false);
            MasonryView view = new MasonryView(mInflater.inflate(
                    R.layout.label_grid_item, viewGroup, false));
            return view;
        }

        @Override
        public void onBindViewHolder(final MasonryView masonryView, int position) {
            ViewGroup.LayoutParams lp = masonryView.textView.getLayoutParams();
            lp.height = mHeights.get(position);
            masonryView.textView.setLayoutParams(lp);
//            masonryView.imageView.setImageResource(products.get(position).getImg());
            masonryView.textView.setText(products.get(position));

            // 如果设置了回调，则设置点击事件
            if (mOnItemClickLitener != null) {
                masonryView.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int pos = masonryView.getLayoutPosition();
                        mOnItemClickLitener.onItemClick(masonryView.itemView, pos);
                    }
                });

                masonryView.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        int pos = masonryView.getLayoutPosition();
                        mOnItemClickLitener.onItemLongClick(masonryView.itemView, pos);
                        removeData(pos);
                        return false;
                    }
                });
            }

        }

        public void removeData(int position) {
            products.remove(position);
            notifyItemRemoved(position);
        }

        @Override
        public int getItemCount() {
            return products.size();
        }

        public class MasonryView extends RecyclerView.ViewHolder {

            ImageView imageView;
            TextView textView;

            public MasonryView(View itemView) {
                super(itemView);
//                imageView= (ImageView) itemView.findViewById(R.id.masonry_item_img );
                textView = (TextView) itemView.findViewById(R.id.text_view);
            }

        }

    }

}
