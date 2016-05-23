package org.csware.ee.shipper.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

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
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Location;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.OrderListType;
import org.csware.ee.model.OrderTrackReturn;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Yu on 2015/5/29.
 * 订单跟踪
 */
public class TrackFragment extends FragmentBase implements BaiduMap.OnMapClickListener ,
        OnGetRoutePlanResultListener {

    final static String TAG = "TrackFragment";

    public TrackFragment() {
        super();
    }

    // 标志位，标志已经初始化完成。
    protected boolean getIsPrepared() {
        return true;
    }


    @Override
    protected int getLayoutId() {
        return R.layout.track_fragment;
    }

    DbCache _dbCache;
    Location _location;
    MapView _bdMapView = null;
    BaiduMap _bdMap;
    LocationClient _locationClient;//百度定位服务
    LinearLayout mapTrack;
    BDLocationListenerImpl bdListener = new BDLocationListenerImpl();
    boolean _isFirstLoc = true;
    int onResume = 0;
    OrderListType listType = OrderListType.SHIPPING;
    ChinaAreaHelper areaHelper;
    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private CommonAdapter<OrderListReturn.OrdersEntity> adapter;
    private List<OrderListReturn.OrdersEntity> shippingList;

    //路径规划
    RouteLine route = null;
//    OverlayManager routeOverlay = null;
    RoutePlanSearch mSearch = null;
    @Override
    public void init() {
        _bdMapView = (MapView)  rootView.findViewById(R.id.bmapView);
        _bdMap = _bdMapView.getMap();
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
        adapter = new CommonAdapter<OrderListReturn.OrdersEntity>(baseActivity, shippingList, R.layout.order_track_list_item) {

            /**重写，返回了序列元素的标识Id
             * 返回0时表示没有主键Id
             * */
            @Override
            public long getItemId(int position) {
                OrderListReturn.OrdersEntity item = shippingList.get(position);
                //Log.e(TAG,"itemId="+item.getId());
                if (item != null)
                    return item.getId();
                return 0;
            }

            // 出价列表数据填充
            @Override
            public void convert(ViewHolder helper, OrderListReturn.OrdersEntity item) {
                if (item == null) item = new OrderListReturn.OrdersEntity();
                helper.setText(R.id.labFromArea, areaHelper.getCityName(Integer.toString(item.getFrom())));
                String toArea = "";
                if (item.getTo()>0){
                    toArea =  areaHelper.getCityName(Integer.toString(item.getTo()));
                }else {
                    String f_str =  item.getPayees().get(0).getArea()+"";
                    toArea =areaHelper.getCityName(f_str);
                    if (item.getPayees().size()>1){
                        toArea = toArea+"等";
                    }
                }
                helper.setText(R.id.labToArea,toArea );
                helper.setText(R.id.labPublishTime, ParamTool.fromTimeSeconds(item.getCreateTime()));
                helper.setText(R.id.labGoodsType, item.getGoodsType());
                helper.setText(R.id.labAmount, item.getGoodsAmount()+"");
                helper.setText(R.id.labUnit, item.getGoodsUnit());
                helper.setText(R.id.labBearName,item.getBearer().getName().trim());
                helper.setText(R.id.labBearPlate,item.getBearer().getPlate().trim());
                final OrderListReturn.OrdersEntity finalItem = item;
                helper.getView(R.id.imgTrackCall).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!Guard.isNullOrEmpty(finalItem.getBearerUser().getMobile())) {
                            Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + finalItem.getBearerUser().getMobile()));
                            startActivity(nIntent);
                        }
                    }
                });
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);

        asyncLoadingData();
        OnMap();

    }

    private void OnMap() {
        _bdMapView.showZoomControls(false);
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
//            Log.w(TAG, "itemId = " + id + "  Position :" + position);
            //TODO:路径画线
            dialog = Tools.getDialog(baseActivity);
            dialog.setCanceledOnTouchOutside(false);
            OrderApi.track(baseFragment.getActivity(), baseActivity, id, new IJsonResult<OrderTrackReturn>() {
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
                        // 添加折线  SW:[39.87923,110.437428]  NE:[39.94923,116.397428]
//                        LatLng p1 = new LatLng(39.94923, 115.397428);
//                        LatLng p2 = new LatLng(39.94923, 116.397428);
//                        LatLng p3 = new LatLng(39.87923, 110.437428);
                        LatLng latLng = new LatLng(_location.latitude, _location.longitude);
                        points.add(latLng);
//                        points.add(p2);
//                        points.add(p3);
                        if (Tracer.isRelease()) {
                            return;
                        }
                        toastSlow("该订单暂无跟踪数据");
                    }
                    _locationClient.stop();
//                    Log.e(TAG,"points size"+points.size()+"");
                    if (points.size()>1&&points.size()<10000) {
                        _bdMap.clear();
                        int pix = 10;//划线宽度大小

//                        MapStatus mMapStatus = new MapStatus.Builder(_bdMap.getMapStatus()).target(points.get(0)).zoom(13).build();
//                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
//                                .newMapStatus(mMapStatus);
//                        _bdMap.animateMapStatus(mMapStatusUpdate);
                    //TODO:自动定位与缩放位置
//                        BitmapDescriptor be = BitmapDescriptorFactory.fromResource(R.drawable.dw_icon_wz);
//                        BitmapDescriptor bs = BitmapDescriptorFactory.fromResource(R.drawable.dw_icon_zdwz);
//                        OverlayOptions ooA = new MarkerOptions().position(points.get(0)).icon(bs).zIndex(9).draggable(true);
//                        OverlayOptions ooB = new MarkerOptions().position(points.get(points.size()-1)).icon(be).zIndex(9).draggable(true);
//                        _bdMap.addOverlay(ooA);
//                        _bdMap.addOverlay(ooB);
//                        MapCenterPointInfo centerPointInfo = new MapCenterPointInfo(points);
//                        Tracer.w(TAG, "SW:[" + centerPointInfo.southWest.latitude + "," + centerPointInfo.southWest.longitude + "]  NE:[" + centerPointInfo.northEast.latitude + "," + centerPointInfo.northEast.longitude + "]");
////                        Log.e(TAG,"lat:"+points.get(0).toString()+" end:"+points.get(points.size()-1).toString());
//                        LatLng Latnou = centerPointInfo.northEast,Latsou =centerPointInfo.southWest;
//                        if (centerPointInfo.northEast.latitude==0.0&&centerPointInfo.northEast.longitude==0.0){
//                            Latnou = points.get(0);
//                        }
//                        if (centerPointInfo.southWest.latitude==0.0&&centerPointInfo.southWest.longitude==0.0){
//                            Latsou = points.get(points.size()-1);
//                        }
//                        LatLngBounds bounds = new LatLngBounds.Builder().include(Latnou)
//                                .include(Latsou).build();
//
//                        MapStatusUpdate u = MapStatusUpdateFactory
//                                .newLatLngBounds(bounds);
//                        _bdMap.setMapStatus(u);
//                        OverlayOptions ooPolyline = new PolylineOptions().width(pix)
//                                .color(0xAA0ad40a).points(points);
//                        _bdMap.addOverlay(ooPolyline);
                        showSearch(points);
                        _bdMap.setMyLocationEnabled(false);
                    }else if (points.size()==1){
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
                    }else if (points.size()>10000){
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
    };

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

        OrderApi.list(baseFragment.getActivity(), baseActivity, listType, 20, 1, new IJsonResult<OrderListReturn>() {
            @Override
            public void ok(OrderListReturn json) {
                //成功
                List<OrderListReturn.OrdersEntity> list = json.getOrders();
                swipeLayout.setRefreshing(false);
                shippingList.clear();
                shippingList.addAll(list);
//                Log.w(TAG, "获取记录：" + shippingList.size());
                adapter.notifyDataSetChanged();
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
            _bdMapView = null;
        }
        super.onDestroy();

    }

    @Override
    public void onPause() {
        _bdMapView.onPause();
        super.onPause();
    }
    @Override
    public void onResume() {
//        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        onResume ++;
        if (_bdMapView != null)
            _bdMapView.onResume();
        if (_bdMap!=null) {
            if (onResume>1) {
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
//                _bdMap.addOverlay(ooA);
//                Log.e("TrackFragment", "isOnResume" + onResume);
            }
        }else {
//            Log.e("TrackFragment", "isOnResume _bdMap is Null");
        }
//        locate();
//        onMap();
        super.onResume();
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

            // map view 销毁后不在处理新接收的位置
            if (location == null || _bdMapView == null) {
                return;
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
