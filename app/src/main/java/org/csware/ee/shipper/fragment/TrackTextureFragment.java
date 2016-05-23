package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;

import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.Location;
import org.csware.ee.model.TrackListOrder;
import org.csware.ee.model.OrderListType;
import org.csware.ee.model.OrderTrackReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.TrackListOrder;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ScrollViewForListView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Created by Yu on 2015/5/29.
 * 订单跟踪
 */
public class TrackTextureFragment extends FragmentBase implements BaiduMap.OnMapClickListener ,
        OnGetRoutePlanResultListener {

    final static String TAG = "TrackTextureFragment";

    public TrackTextureFragment() {
        super();
    }

    // 标志位，标志已经初始化完成。
    protected boolean getIsPrepared() {
        return true;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.track_texture_fragment;
    }

    DbCache _dbCache;
    Location _location;
    TopActionBar topBar;
    MapView _bdMapView = null, fmapView = null;
    TextView labFrom, labTo, labGoodsType, labAmount, labUnit;
    BaiduMap _bdMap;
    LocationClient _locationClient;//百度定位服务
    LinearLayout LinOrderInfo,LinWrapMap;
    RelativeLayout RelTextureTrack, RelFullMap;
    FrameLayout FragMapTip;
    BDLocationListenerImpl bdListener = new BDLocationListenerImpl();
    boolean _isFirstLoc = true;
    int onResume = 0;
    OrderListType listType = OrderListType.SHIPPING;
    ChinaAreaHelper areaHelper;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
//    private CommonAdapter<TrackListOrder.OrdersEntity> adapter;
    OrderListAdapter orderListAdapter;
    private List<TrackListOrder.OrdersEntity> shippingList;

    //路径规划
    RoutePlanSearch mSearch = null;
    boolean isFullScreen = false;
    List<LatLng> pointsFull = new ArrayList<LatLng>();
    @Override
    public void init() {
        _bdMapView = (MapView) rootView.findViewById(R.id.bmapView);
        fmapView = (MapView) rootView.findViewById(R.id.fmapView);
        topBar = (TopActionBar) rootView.findViewById(R.id.topBar);
        LinOrderInfo = (LinearLayout) rootView.findViewById(R.id.LinOrderInfo);
        LinWrapMap = (LinearLayout) rootView.findViewById(R.id.LinWrapMap);
        RelTextureTrack = (RelativeLayout) rootView.findViewById(R.id.RelTextureTrack);
        RelFullMap = (RelativeLayout) rootView.findViewById(R.id.RelFullMap);
        FragMapTip = (FrameLayout) rootView.findViewById(R.id.FragMapTip);
        labFrom = (TextView) rootView.findViewById(R.id.labFrom);
        labTo = (TextView) rootView.findViewById(R.id.labTo);
        labGoodsType = (TextView) rootView.findViewById(R.id.labGoodsType);
        labAmount = (TextView) rootView.findViewById(R.id.labAmount);
        labUnit = (TextView) rootView.findViewById(R.id.labUnit);
        topBar.setTitle("货物跟踪");
        _bdMap = _bdMapView.getMap();
        topBar.setMenu(R.mipmap.hwgz_icon_zoomin, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
                    _bdMap.clear();
                if (isFullScreen){
                    isFullScreen = false;
                    topBar.setMenuIcon(R.mipmap.hwgz_icon_zoomin);
                    RelFullMap.setVisibility(View.GONE);
                    LinWrapMap.setVisibility(View.VISIBLE);
                    FragMapTip.setVisibility(View.VISIBLE);
                    _bdMap = _bdMapView.getMap();
                }else {
                    isFullScreen = true;
                    topBar.setMenuIcon(R.mipmap.hwgz_icon_zoomout);
                    RelFullMap.setVisibility(View.VISIBLE);
                    LinWrapMap.setVisibility(View.GONE);
                    FragMapTip.setVisibility(View.GONE);
                    _bdMap = fmapView.getMap();
                    if (pointsFull.size()>0){
                        drawMaps(pointsFull);
                    }else {
                        showMap();
                    }

                }
            }
        });
        areaHelper = new ChinaAreaHelper(baseActivity);
        _dbCache = new DbCache(baseActivity);
        _location = _dbCache.GetObject(Location.class);
        if (_location == null) {
            //Log.w(TAG, "Location is null and no to init it");
            _location = new Location();
        }

        // 初始化搜索模块，注册事件监听
        mSearch = RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener((OnGetRoutePlanResultListener) baseFragment);

        shippingList = new ArrayList<>(); //TODO:是否必须要初始化呢？
        swipeLayout = (SwipeRefreshLayout) rootView.findViewById(R.id.swipeRefresh);
        swipeLayout.setOnRefreshListener(refreshListener);
        swipeLayout.setColorSchemeResources(android.R.color.holo_green_light, android.R.color.holo_blue_bright); //刷新的样式

        listView = (ListView) rootView.findViewById(R.id.listView);
//        adapter = new CommonAdapter<TrackListOrder.OrdersEntity>(baseActivity, shippingList, R.layout.order_track_list_item) {
//
//            /**重写，返回了序列元素的标识Id
//             * 返回0时表示没有主键Id
//             * */
//            @Override
//            public long getItemId(int position) {
//                TrackListOrder.OrdersEntity item = shippingList.get(position);
//                //Log.e(TAG,"itemId="+item.getId());
//                if (item != null)
//                    return item.getId();
//                return 0;
//            }
//
//            // 出价列表数据填充
//            @Override
//            public void convert(ViewHolder helper, TrackListOrder.OrdersEntity item) {
//                if (item == null) item = new TrackListOrder.OrdersEntity();
//                helper.setText(R.id.labFromArea, areaHelper.getCityName(Integer.toString(item.getFrom())));
//                String toArea = "";
//                if (item.getTo()>0){
//                    toArea =  areaHelper.getCityName(Integer.toString(item.getTo()));
//                }else {
//                    String f_str =  item.getPayees().get(0).getArea()+"";
//                    toArea =areaHelper.getCityName(f_str);
//                    if (item.getPayees().size()>1){
//                        toArea = toArea+"等";
//                    }
//                }
//                helper.setText(R.id.labToArea,toArea );
//                helper.setText(R.id.labPublishTime, ParamTool.fromTimeSeconds(item.getCreateTime()));
//                helper.setText(R.id.labGoodsType, item.getGoodsType());
//                helper.setText(R.id.labAmount, item.getGoodsAmount()+"");
//                helper.setText(R.id.labUnit, item.getGoodsUnit());
//                helper.setText(R.id.labBearName,item.getBearer().getName().trim());
//                helper.setText(R.id.labBearPlate,item.getBearer().getPlate().trim());
//                final TrackListOrder.OrdersEntity finalItem = item;
//                helper.getView(R.id.imgTrackCall).setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        if (!Guard.isNullOrEmpty(finalItem.getBearerUser().getMobile())) {
//                            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + finalItem.getBearerUser().getMobile()));
//                            startActivity(nIntent);
//                        }
//                    }
//                });
//            }
//        };
        orderListAdapter = new OrderListAdapter(shippingList);
        listView.setAdapter(orderListAdapter);
        listView.setOnItemClickListener(itemClickListener);

        asyncLoadingData();
//        if (shippingList.size()>0){
//            UpdateUI(0);
//            LinOrderInfo.setVisibility(View.VISIBLE);
//        }else {
//            LinOrderInfo.setVisibility(View.GONE);
//        }
        OnMap();

    }
    class OrderListAdapter extends BaseAdapter {

        final static String TAG = "OrderListAdapter";
        List<TrackListOrder.OrdersEntity> infoList = new ArrayList<>();
        public OrderListAdapter(List<TrackListOrder.OrdersEntity> infoList) {
            this.infoList = infoList;
            mLastPosition = -1;
        }
        private View mLastView;
        private int mLastPosition;
        private int mLastVisibility;
        @Override
        public int getCount() {
            return infoList.size();
        }

        @Override
        public Object getItem(int position) {
            return infoList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            View view = convertView;
            if (view == null) {
                view = LayoutInflater.from(baseActivity).inflate(
                        R.layout.order_track_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.labFromArea = (TextView) view.findViewById(R.id.labFromArea);
                viewHolder.labToArea = (TextView) view.findViewById(R.id.labToArea);
                viewHolder.labPublishTime = (TextView) view.findViewById(R.id.labPublishTime);
                viewHolder.labGoodsType = (TextView) view.findViewById(R.id.labGoodsType);
                viewHolder.labAmount = (TextView) view.findViewById(R.id.labAmount);
                viewHolder.labUnit = (TextView) view.findViewById(R.id.labUnit);
                viewHolder.labBearName = (TextView) view.findViewById(R.id.labBearName);
                viewHolder.labBearPlate = (TextView) view.findViewById(R.id.labBearPlate);
                viewHolder.imgTrackCall = (ImageView) view.findViewById(R.id.imgTrackCall);
                viewHolder.imgCompanyIcon = (ImageView) view.findViewById(R.id.imgCompanyIcon);
                viewHolder.LinShowMore = (LinearLayout) view.findViewById(R.id.LinShowMore);
                viewHolder.LinHideMore = (LinearLayout) view.findViewById(R.id.LinHideMore);
                viewHolder.LinBeaerList = (LinearLayout) view.findViewById(R.id.LinBeaerList);
                viewHolder.listViewBeaers = (ScrollViewForListView) view.findViewById(R.id.listViewBeaers);
                view.setTag(viewHolder);

            } else {
                viewHolder = (ViewHolder) view.getTag();
                viewHolder.LinBeaerList.setVisibility(View.GONE);
                viewHolder.LinShowMore.setVisibility(View.VISIBLE);

            }
            if(mLastPosition == position){
                viewHolder.LinBeaerList.setVisibility(mLastVisibility);
                viewHolder.LinShowMore.setVisibility(View.GONE);
            }else{
                viewHolder.LinBeaerList.setVisibility(View.GONE);
                viewHolder.LinShowMore.setVisibility(View.VISIBLE);
            }
            TrackListOrder.OrdersEntity item = infoList.get(position);
            if (item == null) item = new TrackListOrder.OrdersEntity();
            viewHolder.labFromArea.setText(areaHelper.getCityName(Integer.toString(infoList.get(position).From)));
            String toArea = "";
            if (infoList.get(position).To>0){
                toArea =  areaHelper.getCityName(Integer.toString(infoList.get(position).To));
            }else {
                if (!Guard.isNull(infoList.get(position).Payee)){
                    String f_str =  infoList.get(position).Payee.get(0).Area+"";
                    toArea =areaHelper.getCityName(f_str);
                    if (infoList.get(position).Payee.size()>1){
                        toArea = toArea+"等";
                    }
                }
            }
            if (Guard.isNull(item.OwnerUser.CompanyStatus)){
                viewHolder.imgCompanyIcon.setVisibility(View.GONE);
            }else {
                if (item.OwnerUser.CompanyStatus==1){
                    viewHolder.imgCompanyIcon.setVisibility(View.VISIBLE);
                    viewHolder.labBearPlate.setVisibility(View.VISIBLE);
                }else {
                    viewHolder.imgCompanyIcon.setVisibility(View.GONE);
                }
            }
            viewHolder.labToArea.setText(toArea);
            viewHolder.labPublishTime.setText(ParamTool.fromTimeSeconds(item.CreateTime));
            viewHolder.labGoodsType.setText(item.GoodsType);
            viewHolder.labAmount.setText(item.GoodsAmount+"");
            viewHolder.labUnit.setText(item.GoodsUnit+"");
            String Name = "";
            if (!Guard.isNull(item.BearerCompany)){
                if (!Guard.isNullOrEmpty(item.BearerCompany.CompanyName)){
                    Name = item.BearerCompany.CompanyName;
                    viewHolder.labBearPlate.setVisibility(View.GONE);
                }
            }else {
                Name = item.Bearer.Name;
                viewHolder.labBearPlate.setVisibility(View.VISIBLE);
            }
            viewHolder.labBearName.setText(Name.trim()+"");
            viewHolder.labBearPlate.setText(item.Bearer.Plate.trim()+"");
            viewHolder.LinBeaerList.setVisibility(View.GONE);
            final TrackListOrder.OrdersEntity finalItem = item;
            if (!Guard.isNull(finalItem.SubOrders)) {
                Tracer.e(TAG,""+finalItem.SubOrders.size());
                final List<TrackListOrder.OrdersEntity.SubOrdersEntity> tmpList = finalItem.SubOrders;
                viewHolder.LinShowMore.setVisibility(View.VISIBLE);
                viewHolder.adapter = new org.csware.ee.shipper.adapter.CommonAdapter<TrackListOrder.OrdersEntity.SubOrdersEntity>(baseActivity, tmpList, R.layout.list_contact_item) {
                    @Override
                    public void convert(org.csware.ee.view.ViewHolder helper, final TrackListOrder.OrdersEntity.SubOrdersEntity itemChild, final int childPosition) {
                        switch (itemChild.Status){
                            case 3:
                                helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_ysz);
                                break;
                            case 5:
                                helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_dhwqr);
                                break;
                            case 6:
                                if (itemChild.BearerRate){
                                    helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_ywc);
                                }else {
                                    helper.setImageResource(R.id.imgBeaerStatus, R.drawable.dd_xj_wpj);
                                }
                                break;
                        }
                        helper.setText(R.id.txtConComp, itemChild.Bearer.Name + "");
                        helper.setText(R.id.txtPlate, itemChild.Bearer.Plate + "");
                        helper.setRating(R.id.rate_rating, (float) itemChild.Bearer.Rate);
                        helper.getView(R.id.imgBeaerStatus).setVisibility(View.VISIBLE);
                        helper.getView(R.id.imgPhoneCall).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (!Guard.isNullOrEmpty(itemChild.BearerUser.Mobile)) {
                                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + itemChild.BearerUser.Mobile));
                                    startActivity(nIntent);
                                }
                            }
                        });
                        if (!Guard.isNullOrEmpty(itemChild.BearerUser.Avatar)) {
                            ImageLoader.getInstance().displayImage(itemChild.BearerUser.Avatar, (ImageView) helper.getView(R.id.imgGameHead));
                        }

                    }
                };
                viewHolder.listViewBeaers.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int childListPosition, long id) {
                        getTrack(tmpList.get(childListPosition).Id);
                        UpdateUI(position);
                    }
                });
                viewHolder.listViewBeaers.setAdapter(viewHolder.adapter);
            }else {
                viewHolder.LinShowMore.setVisibility(View.GONE);
            }
            viewHolder.LinShowMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.LinBeaerList.setVisibility(View.VISIBLE);
                    viewHolder.LinShowMore.setVisibility(View.GONE);
                }
            });
            viewHolder.LinHideMore.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.LinBeaerList.setVisibility(View.GONE);
                    viewHolder.LinShowMore.setVisibility(View.VISIBLE);
                }
            });
//            QCloudService.asyncDisplayImage(baseActivity, infoList.get(position).Avatar, viewHolder.ivHeadPic);

            return view;
        }

        class ViewHolder {
            TextView labToArea,labPublishTime,labGoodsType, labAmount, labUnit, labBearName, labBearPlate, labFromArea;
            ImageView imgTrackCall, imgCompanyIcon;
            LinearLayout LinShowMore, LinHideMore, LinBeaerList;
            ScrollViewForListView listViewBeaers;
            org.csware.ee.shipper.adapter.CommonAdapter<TrackListOrder.OrdersEntity.SubOrdersEntity> adapter;
        }

        public void changeImageVisable(View view,int position) {
            if(mLastView != null && mLastPosition != position ) {
                ViewHolder holder = (ViewHolder) mLastView.getTag();
                switch(holder.LinBeaerList.getVisibility()) {
                    case View.VISIBLE:
                        holder.LinBeaerList.setVisibility(View.GONE);
                        mLastVisibility = View.GONE;
                        break;
                    default :
                        break;
                }
            }
            mLastPosition = position;
            mLastView = view;
            ViewHolder holder = (ViewHolder) view.getTag();
            switch(holder.LinBeaerList.getVisibility()) {
                case View.GONE:
                    holder.LinBeaerList.setVisibility(View.VISIBLE);
                    holder.LinShowMore.setVisibility(View.GONE);
                    if (Guard.isNull(infoList.get(position).SubOrders)){
                        holder.LinHideMore.setVisibility(View.GONE);
                    }else {
                        holder.LinHideMore.setVisibility(View.VISIBLE);
                    }
                    mLastVisibility = View.VISIBLE;
                    break;
                case View.VISIBLE:
                    holder.LinBeaerList.setVisibility(View.GONE);
                    if (Guard.isNull(infoList.get(position).SubOrders)){
                        holder.LinShowMore.setVisibility(View.GONE);
                    }else {
                        holder.LinShowMore.setVisibility(View.VISIBLE);
                    }
                    mLastVisibility = View.GONE;
                    break;
            }
        }

    }

    private void OnMap() {
        _bdMapView.showZoomControls(false);
        fmapView.showZoomControls(false);
        // 开启定位图层
        _bdMap.setMyLocationEnabled(true);

        // 定位初始化
        _locationClient = new LocationClient(getActivity().getApplicationContext());
        _locationClient.registerLocationListener(bdListener);
        LocationClientOption option = new LocationClientOption();

        //地图点击事件处理
        _bdMap.setOnMapClickListener(this);

        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);
        option.setIgnoreKillProcess(true);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        _locationClient.setLocOption(option);
        _locationClient.start();
    }
    ProgressDialog dialog;
    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
//            orderListAdapter.changeImageVisable(view, position);
            //TODO:路径画线
            if (Guard.isNull(shippingList.get(position).SubOrders)){
                getTrack(shippingList.get(position).Id);
            }else {
                if (shippingList.get(position).SubOrders.size()>0)
                    getTrack(shippingList.get(position).SubOrders.get(0).Id);
            }
            UpdateUI(position);
//            getTrack(shippingList.get(position).Id);

        }
    };
    void UpdateUI(int position){
        LinOrderInfo.setVisibility(View.VISIBLE);
        labFrom.setText(areaHelper.getCityName(Integer.toString(shippingList.get(position).From)));
        String toArea = "";
        if (shippingList.get(position).To>0){
            toArea =  areaHelper.getCityName(Integer.toString(shippingList.get(position).To));
        }else {
            if (!Guard.isNull(shippingList.get(position).Payee)) {
                String f_str = shippingList.get(position).Payee.get(0).Area + "";
                toArea = areaHelper.getCityName(f_str);
                if (shippingList.get(position).Payee.size() > 1) {
                    toArea = toArea + "等";
                }
            }
        }
        labTo.setText(toArea);
        labGoodsType.setText(shippingList.get(position).GoodsType);
        labAmount.setText(shippingList.get(position).GoodsAmount+"");
        labUnit.setText(shippingList.get(position).GoodsUnit + "");
    }

    void getTrack(long orderId){
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        OrderApi.track(baseFragment.getActivity(), baseActivity, orderId, new IJsonResult<OrderTrackReturn>() {
            @Override
            public void ok(OrderTrackReturn json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                final List<LatLng> points = new ArrayList<LatLng>();
                if (json.Track != null && json.Track.Track != null) {
                    for (OrderTrackReturn.TrackEntity.LastTrackPointEntity item : json.Track.Track) {
                        points.add(new LatLng(item.Latitude, item.Longitude));
                    }
                } else {
                    LatLng latLng = new LatLng(_location.latitude, _location.longitude);
                    points.add(latLng);
                    if (Tracer.isRelease()) {
                        return;
                    }
                    toastSlow("该订单暂无跟踪数据");
                }
                //赋给pointsFull 全屏的时候展示
                pointsFull = points;
                _locationClient.stop();
//                    Log.e(TAG,"points size"+points.size()+"");
                drawMaps(points);
//                    _bdMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
//                        public void onMarkerDrag(Marker marker) {
//                        }
//
//                        public void onMarkerDragEnd(Marker marker) {
//                            Toast.makeText(
//                                    baseActivity,
//                                    "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
//                                            + marker.getPosition().longitude,
//                                    Toast.LENGTH_LONG).show();
//                        }
//
//                        public void onMarkerDragStart(Marker marker) {
//                        }
//                    });

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


    void drawMaps(List<LatLng> points){
        if (points.size() > 1 && points.size() < 10000) {
            _bdMap.clear();
            int pix = 10;//划线宽度大小
            showSearch(points);
            _bdMap.setMyLocationEnabled(false);
        } else if (points.size() == 1) {
            _bdMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory
                    .fromResource(R.drawable.dw_icon_wz);
            MapStatus mMapStatus = new MapStatus.Builder(_bdMap.getMapStatus()).target(points.get(0)).zoom(15)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            _bdMap.animateMapStatus(mMapStatusUpdate);
            OverlayOptions ooA = new MarkerOptions().position(points.get(0)).icon(bd)
                    .zIndex(9).draggable(true);
            _bdMap.addOverlay(ooA);
            _bdMap.setMyLocationEnabled(false);
        } else if (points.size() > 10000) {
            _bdMap.clear();
            BitmapDescriptor bd = BitmapDescriptorFactory
                    .fromResource(R.drawable.dw_icon_wz);
            MapStatus mMapStatus = new MapStatus.Builder(_bdMap.getMapStatus()).target(points.get(0)).zoom(14)
                    .build();
            MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                    .newMapStatus(mMapStatus);
            _bdMap.animateMapStatus(mMapStatusUpdate);
            OverlayOptions ooA = new MarkerOptions().position(points.get(0)).icon(bd)
                    .zIndex(9).draggable(true);
            _bdMap.addOverlay(ooA);
            _bdMap.setMyLocationEnabled(false);
        }
    }

    private void showSearch(List<LatLng> points) {
        List<PlanNode> list = new  ArrayList<>();
        for (int i=0;i<points.size();i++){
            PlanNode planNode = PlanNode.withLocation(points.get(i));
            list.add(planNode);
        }
        PlanNode stNode = PlanNode.withLocation(points.get(0));
//        if ((position+1)<points.size()) {
            PlanNode enNode = PlanNode.withLocation(points.get(points.size()-1));
            mSearch.drivingSearch((new DrivingRoutePlanOption())
                    .from(stNode).passBy(list)
                    .to(enNode));
//        }
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            asyncLoadingData();
        }
    };

    //对窗口进行刷新操作
    void asyncLoadingData() {

        OrderApi.tracklist(baseFragment.getActivity(), baseActivity, listType, 20, 1, new IJsonResult<TrackListOrder>() {
            @Override
            public void ok(TrackListOrder json) {
                //成功
                List<TrackListOrder.OrdersEntity> list = json.Orders;
                swipeLayout.setRefreshing(false);
                shippingList.clear();
                shippingList.addAll(list);
//                Log.w(TAG, "获取记录：" + shippingList.size());
                orderListAdapter.notifyDataSetChanged();
            }

            @Override
            public void error(Return json) {

            }
        });

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    public void onViewStateRestored(Bundle outState) {
        super.onViewStateRestored(outState);

    }


    @Override
    public void onDestroy() {
//        Log.e("TrackFragment","isDestroy");
        onResume = 0;
        if (_locationClient!=null) {
            _locationClient.stop();
        }

        // 关闭定位图层
        if (_bdMap!=null) {
            _bdMap.setMyLocationEnabled(false);
        }
        if (_bdMapView!=null) {
            _bdMapView.onDestroy();
        }
        if (fmapView!=null) {
            fmapView.onDestroy();
        }
        _bdMapView = null;
        fmapView = null;
        super.onDestroy();

    }

    @Override
    public void onPause() {
        if (isFullScreen){
            fmapView.onPause();
            fmapView.setVisibility(View.INVISIBLE);
        }else {
            _bdMapView.onPause();
            _bdMapView.setVisibility(View.INVISIBLE);
        }
        super.onPause();
    }
    @Override
    public void onResume() {
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        onResume ++;
        if (isFullScreen){
            if (fmapView != null)
                fmapView.setVisibility(View.VISIBLE);
            fmapView.onResume();
        }else {
            if (_bdMapView != null)
                _bdMapView.setVisibility(View.VISIBLE);
            _bdMapView.onResume();
        }
        if (_bdMap!=null) {
            if (onResume>1) {
                showMap();
            }
        }else {
//            Log.e("TrackFragment", "isOnResume _bdMap is Null");
        }
        SharedPreferences preferences = baseActivity.getSharedPreferences("isTrackRefresh", Context.MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isTrackRefresh", false);
//        Tracer.e(TAG,"OnResume:"+isRefresh);
        if (isRefresh) {
//            _orders.clear();
            asyncLoadingData();
        }
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear().commit();
//        locate();
//        onMap();
        super.onResume();
    }

    void showMap(){
        BitmapDescriptor bd = BitmapDescriptorFactory
                .fromResource(R.drawable.dw_icon_wz);
        LatLng latLng = new LatLng(_location.latitude, _location.longitude);
        MapStatus mMapStatus = new MapStatus.Builder(_bdMap.getMapStatus()).target(latLng).zoom(15)
                .build();
        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                .newMapStatus(mMapStatus);
        _bdMap.animateMapStatus(mMapStatusUpdate);
        OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bd)
                .zIndex(9).draggable(true);
    }

    @Override
    protected void lazyLoad() {

        if (!getIsPrepared() || !isVisible) {
//            Log.e(TAG, "!isVisible");
            return;
        }
        //填充各控件的数据
//        Log.e(TAG, "延迟加载开始");
    }

    protected void onInvisible() {

    }

    @Override
    public void onMapClick(LatLng latLng) {
        _bdMap.hideInfoWindow();
    }

    @Override
    public boolean onMapPoiClick(MapPoi mapPoi) {
        return false;
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        if (drivingRouteResult == null || drivingRouteResult.error != SearchResult.ERRORNO.NO_ERROR) {
            toastFast("抱歉，未找到结果");
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.AMBIGUOUS_ROURE_ADDR) {
            //起终点或途经点地址有岐义，通过以下接口获取建议查询信息
            //result.getSuggestAddrInfo()
            return;
        }
        if (drivingRouteResult.error == SearchResult.ERRORNO.NO_ERROR) {
            DrivingRouteOverlay overlay = new MyDrivingRouteOverlay(_bdMap);
            _bdMap.setOnMarkerClickListener(overlay);
            overlay.setData(drivingRouteResult.getRouteLines().get(0));
            overlay.addToMap();
            overlay.zoomToSpan();
//            if (j<points.size()) {
//                ++j;
//                showSearch(j);
//            }
        }
    }

    //定制RouteOverly
    private class MyDrivingRouteOverlay extends DrivingRouteOverlay {

        public MyDrivingRouteOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public BitmapDescriptor getStartMarker() {
//            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
//            }
//            return null;
        }

        @Override
        public BitmapDescriptor getTerminalMarker() {
//            if (useDefaultIcon) {
                return BitmapDescriptorFactory.fromResource(R.drawable.icon_en);
//            }
//            return null;
        }
    }

    /**
     * 定位接口，需要实现两个方法
     *
     * @author xiaanming
     */
    public class BDLocationListenerImpl implements BDLocationListener {
        /**
         * 接收异步返回的定位结果，参数是BDLocation类型参数
         */
        @Override
        public void onReceiveLocation(BDLocation location) {
            _locationClient.stop(); //为了省点电，我就不再定位了。TODO：不过最后还是不停的执行呀。奇怪！

            if (isFullScreen){
                // map view 销毁后不在处理新接收的位置
                if (location == null || fmapView == null) {
                    return;
                }
            }else {
                // map view 销毁后不在处理新接收的位置
                if (location == null || _bdMapView == null) {
                    return;
                }
            }
            boolean isChanged = _location.isChanged(location.getLatitude(), location.getLongitude());
            if (isChanged) {
                _location.locationTime = location.getTime();
                _location.locaType = location.getLocType();
                _location.radius = location.getRadius();
                if (location.getLocType() == BDLocation.TypeGpsLocation) {
                    _location.speed = location.getSpeed();
                    _location.satelliteNumber = location.getSatelliteNumber();
                } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {
                    _location.address = location.getAddrStr();
                }
                _dbCache.save(_location);
            }
            _locationClient.stop();
            // 构造定位数据
            MyLocationData locData = new MyLocationData.Builder()
                    .accuracy(_location.radius)
                            // 此处设置开发者获取到的方向信息，顺时针0-360
                    .direction(100).latitude(_location.latitude)
                    .longitude(_location.longitude).build();
            // 设置定位数据
            _bdMap.setMyLocationData(locData);
            if (_isFirstLoc) {
                _isFirstLoc = false;
                LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                //MapStatusUpdate u = MapStatusUpdateFactory.newLatLng(ll); //设置地图新中心点
                float f = _bdMap.getMaxZoomLevel();//19.0 最小比例尺
                //Log.w("ZoomLevel","SIZE:"+f);//Note:虽然最多19级，但是表现在地图上只有16级缩放
                MapStatusUpdate u = MapStatusUpdateFactory.newLatLngZoom(ll, f - 5);
                _bdMap.animateMapStatus(u);
            }

        }

    }

}
