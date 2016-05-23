package org.csware.ee.shipper.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Looper;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.SupportMapFragment;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
//import com.baidu.trace.LBSTraceClient;
//import com.baidu.trace.OnEntityListener;
//import com.baidu.trace.OnTrackListener;
//import com.baidu.trace.TraceLocation;

import org.csware.ee.api.OrderApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.Location;
import org.csware.ee.model.MapCenterPointInfo;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.OrderListType;
import org.csware.ee.model.OrderTrackReturn;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.utils.trackutils.GsonService;
import org.csware.ee.utils.trackutils.HistoryTrackData;
import org.csware.ee.view.CommonAdapter;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yu on 2015/5/29.
 * 订单跟踪
 */
public class TrackNewFragment extends FragmentBase {

    final static String TAG = "TrackFragment";

    public TrackNewFragment() {
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

    //百度鹰眼相关
    protected static long serviceId = 109086;
    protected static String entityName = null;
//    protected static LBSTraceClient client = null;
//    protected static OnTrackListener trackListener = null;
//    protected static OnEntityListener entityListener = null;

    DbCache _dbCache;
    Location _location;
    MapView _bdMapView = null;
    BaiduMap _bdMap;
    SupportMapFragment map;
    LocationClient _locationClient;//百度定位服务
    BDLocationListenerImpl bdListener = new BDLocationListenerImpl();

    boolean _isFirstLoc = true;
    int onResume = 0;
    OrderListType listType = OrderListType.SHIPPING;
    ChinaAreaHelper areaHelper;

    private SwipeRefreshLayout swipeLayout;
    private ListView listView;
    private CommonAdapter<OrderListReturn.OrdersEntity> adapter;
    private List<OrderListReturn.OrdersEntity> shippingList;

    @Override
    public void init() {
        _bdMapView = (MapView)  rootView.findViewById(R.id.bmapView);
        areaHelper = new ChinaAreaHelper(baseActivity);
        _dbCache = new DbCache(baseActivity);
        _location = _dbCache.GetObject(Location.class);
        if (_location == null) {
            //Log.w(TAG, "Location is null and no to init it");
            _location = new Location();
        }

        // 初始化轨迹服务客户端
//        client = new LBSTraceClient(baseActivity.getApplicationContext());
        // 初始化entity标识
        entityName = 100001+"";
        // 初始化OnTrackListener
        initOnTrackListener();
//        client.setOnTrackListener(trackListener);

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
                helper.setText(R.id.labFromArea, areaHelper.getAreaName(Integer.toString(item.getFrom())));
                helper.setText(R.id.labToArea, areaHelper.getAreaName(Integer.toString(item.getTo())));
                helper.setText(R.id.labPublishTime, ParamTool.fromTimeSeconds(item.getCreateTime()));
                helper.setText(R.id.labGoodsType, item.getGoodsType());
                helper.setText(R.id.labAmount, item.getGoodsAmount()+"");
                helper.setText(R.id.labUnit, item.getGoodsUnit());
            }
        };
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(itemClickListener);

        asyncLoadingData();

    }

    /**
     * 初始化OnTrackListener
     */
    private void initOnTrackListener() {

//        trackListener = new OnTrackListener() {
//
//            // 请求失败回调接口
//            @Override
//            public void onRequestFailedCallback(String arg0) {
//                // TODO Auto-generated method stub
//                Looper.prepare();
//                Toast.makeText(baseActivity, "track请求失败回调接口消息 : " + arg0, Toast.LENGTH_SHORT).show();
//                Looper.loop();
//            }
//
//            // 查询历史轨迹回调接口
//            @Override
//            public void onQueryHistoryTrackCallback(String arg0) {
//                // TODO Auto-generated method stub
//                super.onQueryHistoryTrackCallback(arg0);
//                showHistoryTrack(arg0);
//            }
//
//        };
    }

    /**
     * 显示历史轨迹
     *
     * @param historyTrack
     */
    protected void showHistoryTrack(String historyTrack) {
        Tracer.e(TAG,historyTrack);
        HistoryTrackData historyTrackData = GsonService.parseJson(historyTrack,
                HistoryTrackData.class);

        List<LatLng> latLngList = new ArrayList<LatLng>();
        if (historyTrackData != null && historyTrackData.getStatus() == 0) {
            if (historyTrackData.getListPoints() != null) {
                latLngList.addAll(historyTrackData.getListPoints());
            }

            // 绘制历史轨迹
            drawHistoryTrack(latLngList, historyTrackData.distance);

        }

    }

    // 起点图标
    private static BitmapDescriptor bmStart;
    // 终点图标
    private static BitmapDescriptor bmEnd;

    // 起点图标覆盖物
    private static MarkerOptions startMarker = null;
    // 终点图标覆盖物
    private static MarkerOptions endMarker = null;
    // 路线覆盖物
    private static PolylineOptions polyline = null;

    private MapStatusUpdate msUpdate = null;

    /**
     * 绘制历史轨迹
     *
     * @param points
     */
    private void drawHistoryTrack(final List<LatLng> points, final double distance) {
        // 绘制新覆盖物前，清空之前的覆盖物
        _bdMap.clear();
        _bdMap.setOnMapClickListener(null);
        _bdMap.setMyLocationEnabled(false);
        if (points == null || points.size() == 0) {
            Looper.prepare();
            Toast.makeText(getActivity(), "当前查询无轨迹点", Toast.LENGTH_SHORT).show();
            Looper.loop();
            resetMarker();
        } else if (points.size() > 1) {

            LatLng llC = points.get(0);
            LatLng llD = points.get(points.size() - 1);
            LatLngBounds bounds = new LatLngBounds.Builder()
                    .include(llC).include(llD).build();

            msUpdate = MapStatusUpdateFactory.newLatLngBounds(bounds);

            bmStart = BitmapDescriptorFactory.fromResource(R.drawable.icon_st);
            bmEnd = BitmapDescriptorFactory.fromResource(R.drawable.icon_en);

            // 添加起点图标
            startMarker = new MarkerOptions()
                    .position(points.get(points.size() - 1)).icon(bmStart)
                    .zIndex(9).draggable(true);

            // 添加终点图标
            endMarker = new MarkerOptions().position(points.get(0))
                    .icon(bmEnd).zIndex(9).draggable(true);

            // 添加路线（轨迹）
            polyline = new PolylineOptions().width(10)
                    .color(Color.RED).points(points);

            addMarker();

            Looper.prepare();
            Toast.makeText(getActivity(), "当前轨迹里程为 : " + (int) distance + "米", Toast.LENGTH_SHORT).show();
            Looper.loop();

        }

    }

    /**
     * 添加覆盖物
     */
    protected void addMarker() {

        if (null != msUpdate) {
            _bdMap.setMapStatus(msUpdate);
        }

        if (null != startMarker) {
            _bdMap.addOverlay(startMarker);
        }

        if (null != endMarker) {
            _bdMap.addOverlay(endMarker);
        }

        if (null != polyline) {
            _bdMap.addOverlay(polyline);
        }

    }

    /**
     * 重置覆盖物
     */
    private void resetMarker() {
        startMarker = null;
        endMarker = null;
        polyline = null;
    }


    private int startTime = 1453737600;
    private int endTime = 1453823999;
    /**
     * 查询历史轨迹
     */
    private void queryHistoryTrack() {

        // entity标识
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;
        Tracer.e(TAG, entityName + " sTime:" + startTime + " eTime:" + endTime);
//        client.queryHistoryTrack(serviceId, entityName, simpleReturn, startTime, endTime,
//                pageSize,
//                pageIndex,
//                trackListener);
    }

    /**
     * 查询纠偏后的历史轨迹
     */
    private void queryProcessedHistoryTrack() {

        // entity标识
        // 是否返回精简的结果（0 : 否，1 : 是）
        int simpleReturn = 0;
        // 是否返回纠偏后轨迹（0 : 否，1 : 是）
        int isProcessed = 1;
        // 开始时间
        if (startTime == 0) {
            startTime = (int) (System.currentTimeMillis() / 1000 - 12 * 60 * 60);
        }
        if (endTime == 0) {
            endTime = (int) (System.currentTimeMillis() / 1000);
        }
        // 分页大小
        int pageSize = 1000;
        // 分页索引
        int pageIndex = 1;

//        client.queryProcessedHistoryTrack(serviceId, entityName, simpleReturn, isProcessed,
//                startTime, endTime,
//                pageSize,
//                pageIndex,
//                trackListener);
    }

    SwipeRefreshLayout.OnRefreshListener refreshListener = new SwipeRefreshLayout.OnRefreshListener() {
        public void onRefresh() {
            asyncLoadingData();
        }
    };

    AdapterView.OnItemClickListener itemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            Log.w(TAG, "itemId = " + id + "  Position :" + position);
            //TODO:路径画线
            queryProcessedHistoryTrack();
//            OrderApi.track(baseActivity, id, new IJsonResult<OrderTrackReturn>() {
//                @Override
//                public void ok(OrderTrackReturn json) {
//                    List<LatLng> points = new ArrayList<LatLng>();
//                    if (json.Track != null && json.Track.Track != null) {
//                        for (OrderTrackReturn.TrackEntity.LastTrackPointEntity item : json.Track.Track) {
//                            points.add(new LatLng(item.Latitude, item.Longitude));
//                        }
//                    } else {
//                        // 添加折线  SW:[39.87923,110.437428]  NE:[39.94923,116.397428]
//                        LatLng p1 = new LatLng(39.94923, 115.397428);
//                        LatLng p2 = new LatLng(39.94923, 116.397428);
//                        LatLng p3 = new LatLng(39.87923, 110.437428);
//                        points.add(p1);
//                        points.add(p2);
//                        points.add(p3);
//                        if (Tracer.isRelease()) {
//                            return;
//                        }
//                        toastSlow("没有跟踪数据，显示的数据为DEMO");
//                    }
//                    if (points.size()>1) {
//                        OverlayOptions ooPolyline = new PolylineOptions().width(5)
//                                .color(0xAAFF0000).points(points);
//                        _bdMap.addOverlay(ooPolyline);
////
////                    //TODO:自动定位与缩放位置
//                        MapCenterPointInfo centerPointInfo = new MapCenterPointInfo(points);
//                        Tracer.w(TAG, "SW:[" + centerPointInfo.southWest.latitude + "," + centerPointInfo.southWest.longitude + "]  NE:[" + centerPointInfo.northEast.latitude + "," + centerPointInfo.northEast.longitude + "]");
//                        LatLngBounds bounds = new LatLngBounds.Builder().include(centerPointInfo.northEast)
//                                .include(centerPointInfo.southWest).build();
//
//                        MapStatusUpdate u = MapStatusUpdateFactory
//                                .newLatLngBounds(bounds);
//                        _bdMap.setMapStatus(u);
//                    }else if (points.size()==1){
//                        BitmapDescriptor bd = BitmapDescriptorFactory
//                                .fromResource(R.drawable.dw_icon_wz);
//                        MapStatus mMapStatus = new MapStatus.Builder(_bdMap.getMapStatus()).target(points.get(0)).zoom(9)
//                                .build();
//                        MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
//                                .newMapStatus(mMapStatus);
//                        _bdMap.animateMapStatus(mMapStatusUpdate);
//                        OverlayOptions ooA = new MarkerOptions().position(points.get(0)).icon(bd)
//                                .zIndex(9).draggable(true);
//                        _bdMap.addOverlay(ooA);
//                    }
////                    _bdMap.setOnMarkerDragListener(new BaiduMap.OnMarkerDragListener() {
////                        public void onMarkerDrag(Marker marker) {
////                        }
////
////                        public void onMarkerDragEnd(Marker marker) {
////                            Toast.makeText(
////                                    baseActivity,
////                                    "拖拽结束，新位置：" + marker.getPosition().latitude + ", "
////                                            + marker.getPosition().longitude,
////                                    Toast.LENGTH_LONG).show();
////                        }
////
////                        public void onMarkerDragStart(Marker marker) {
////                        }
////                    });
//
//                }
//
//                @Override
//                public void error(Return json) {
//
//                }
//            });

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

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        offMap();
    }

    public void onViewStateRestored(Bundle outState) {
        super.onViewStateRestored(outState);

    }


    @Override
    public void onDestroy() {
//        Log.e("TrackFragment","isDestroy");
        if(_locationClient!=null){
            //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
            _locationClient.stop();
            // 关闭定位图层
            if (_bdMap!=null) {
                _bdMap.setMyLocationEnabled(false);
            }
            _bdMapView.onDestroy();
            _bdMapView = null;
        }
        onResume = 0;
//        client.onDestroy();
        super.onDestroy();

        offMap();
    }


    @Override
    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        onResume ++;
//        Log.e("TrackFragment", "isOnResume"+onResume);
        if (_bdMapView != null)
            _bdMapView.onResume();
            _bdMapView.setVisibility(View.VISIBLE);
        if (_bdMap!=null) {
            if (!_isFirstLoc) {
                BitmapDescriptor bd = BitmapDescriptorFactory
                        .fromResource(R.drawable.dw_icon_wz);
                LatLng latLng = new LatLng(_location.latitude, _location.longitude);
                MapStatus mMapStatus = new MapStatus.Builder(_bdMap.getMapStatus()).target(latLng).zoom(9)
                        .build();
                MapStatusUpdate mMapStatusUpdate = MapStatusUpdateFactory
                        .newMapStatus(mMapStatus);
                _bdMap.animateMapStatus(mMapStatusUpdate);
                OverlayOptions ooA = new MarkerOptions().position(latLng).icon(bd)
                        .zIndex(9).draggable(true);
                _bdMap.addOverlay(ooA);
            }
        }else {
//            Log.e("TrackFragment", "isOnResume _bdMap is Null");

        }
//        locate();
//        onMap();
        super.onResume();
    }


    boolean mapAdded = false;

    @Override
    protected void lazyLoad() {

        if (!getIsPrepared() || !isVisible) {
//            Log.e(TAG, "!isVisible");
            return;
        }
        //填充各控件的数据
//        Log.e(TAG, "延迟加载开始");
        //Log.w(TAG, "map status:" + mapAdded);
        //LinearLayout box = (LinearLayout) rootView.findViewById(R.id.mapBox);

        onMap();

    }

    protected void onInvisible() {

        //Log.e(TAG, "onInvisible");

    }

    //    地图可见
    void onMap() {
//        Log.w(TAG, "0. onMap .  the variable:mapAdded=" + mapAdded);
        if (rootView == null) {
//            Log.e(TAG, "还没有实现的直接点TAB过来的显示");
            return;
        }
        if (!mapAdded) {
//            Log.w(TAG, "1. begin render map.");


//            LinearLayout box = (LinearLayout) rootView.findViewById(R.id.mapBox);
//            box.removeAllViews();
//            box.addView(_bdMapView);

            //百度地图相关设定
            _bdMap = _bdMapView.getMap();
            if (_location.hasPosition())
                _bdMap.setMyLocationData(_location.getData());

            //开启交通图
            //bdMap.setTrafficEnabled(true);

            // 开启定位图层
            _bdMap.setMyLocationEnabled(true);


//        //Fragment方式载入百度地图 .. NOTE:暂时不要用了
//        MapStatus ms = new MapStatus.Builder().overlook(-20).zoom(15).build();
//        BaiduMapOptions bo = new BaiduMapOptions().mapStatus(ms)
//                .compassEnabled(false).zoomControlsEnabled(false);
//        map = SupportMapFragment.newInstance(bo);
//        FragmentManager manager = getFragmentManager();
//        manager.beginTransaction().add(R.id.map, map, "map_fragment").commit();


            mapAdded = true;
        }

        //TODO://开始定位当前地址 //
        locate();
    }

    //    地图移除
    void offMap() {
        //        地图没有了，要重建
        mapAdded = false;
    }


    @Override
    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
//        if (_bdMapView != null)
//            _bdMapView.onPause();
//
//        if (_locationClient != null)
//            _locationClient.stop();
//
        _bdMapView.onPause();
        _bdMapView.setVisibility(View.INVISIBLE);
        super.onPause();
//
//        offMap();
    }


    /**
     * 开始定位位置
     * 注意调用时机，否则会不显示
     */
    void locate() {
//        Log.d(TAG, "9. begin locate your location.");
        //实例化定位服务，LocationClient类必须在主线程中声明
        _locationClient = new LocationClient(getActivity().getApplicationContext());
        _locationClient.registerLocationListener(bdListener);//注册定位监听接口
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);// 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        option.setNeedDeviceDirect(true); //设置方向
        option.setIsNeedAddress(true); //设置是否需要地址信息，默认为无地址
        _locationClient.setLocOption(option);
        _locationClient.start();  // 调用此方法开始定位

    }

    void autoLocation() {

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

//            Log.i(TAG, GsonHelper.toJson(_location));


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


//// 设置定位图层的配置（定位模式，是否允许方向信息，用户自定义定位图标）
//        mCurrentMarker = BitmapDescriptorFactory
//                .fromResource(R.drawable.icon_geo);
//        MyLocationConfiguration config = new MyLocationConfiguration(mCurrentMode, true, mCurrentMarker);
//        bdMap.setMyLocationConfiguration();
//// 当不需要定位图层时关闭定位图层
//        bdMap.setMyLocationEnabled(false);


        }

//        /**
//         * 接收异步返回的POI查询结果，参数是BDLocation类型参数  旧版的没有哟
//         */
//        @Override
//        public void onReceivePoi(BDLocation poiLocation) {
//
//        }


    }


}
