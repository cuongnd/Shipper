package org.csware.ee.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.csware.ee.Guard;
import org.csware.ee.R;
import org.csware.ee.api.ToolApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AddressModel;
import org.csware.ee.model.CarInfo;
import org.csware.ee.model.CarLengthModel;
import org.csware.ee.model.Return;
import org.csware.ee.utils.FileUtil;
import org.csware.ee.utils.GsonHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

/**
 * 城市Picker
 *
 * @author zihao
 */
public class CarDynamicPicker extends LinearLayout {
    private static final String TAG = "CarDynamicPicker";
    /**
     * 滑动控件
     */
    private ScrollerNumberPicker provincePicker;
    private ScrollerNumberPicker cityPicker;
    private ScrollerNumberPicker counyPicker;
    /**
     * 选择监听
     */
    private OnSelectingListener onSelectingListener;
    /**
     * 刷新界面
     */
    private static final int REFRESH_VIEW = 0x001;
    /**
     * 临时日期
     */
    private int tempProvinceIndex = -1;
    private int temCityIndex = -1;
    private int tempCounyIndex = -1;
    private Context context;
    private List<CarInfo> model_list = new ArrayList<CarInfo>();
    private HashMap<String, List<CarInfo>> length_map = new HashMap<String, List<CarInfo>>();

    private CitycodeUtil citycodeUtil;
    private String city_code_string;
    private String province_string, city_string, district_string;

    DbCache dbCache;
    AddressModel addressModel;

    public CarDynamicPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        dbCache = new DbCache(context);
        Tracer.e(TAG, "attrs one");
        asyncCompareVersion();
        // TODO Auto-generated constructor stub
    }

    public CarDynamicPicker(Context context) {
        super(context);
        this.context = context;
        Tracer.e(TAG, "attrs two");
        asyncCompareVersion();
        // TODO Auto-generated constructor stub
    }

    // 获取城市信息
    private void getaddressinfo() {
        // TODO Auto-generated method stub\
        // 读取城市信息string
        String area_str = FileUtil.readAssets(context, "json/carlengthmodel.json");
        setData(area_str);
    }

    void setData(String area_str) {
        JSONParser parser = new JSONParser();
        Tracer.e(TAG, "attrs Analytical data");
        model_list = parser.getJsonObject(area_str);
        length_map = parser.getJsonHashMap(area_str);
        Tracer.e(TAG, "attrs Analytical data size:" + model_list.size() + " length:" + length_map.size());
    }

    // 获取城市信息
    private void asyncCompareVersion() {
        // TODO Auto-generated method stub
        HttpParams httpParams = new HttpParams(API.CONFIG.VERSION);
        ToolApi.getVersion(context, httpParams, new IJsonResult<AddressModel>() {
            @Override
            public void ok(AddressModel json) {
                addressModel = dbCache.GetObject(AddressModel.class);
                if (Guard.isNull(addressModel)) {
                    addressModel = new AddressModel();
                }
                if (!Guard.isNull(json)) {
                    if (!Guard.isNull(json.VehicleInfo)) {
                        if (json.VehicleInfo > addressModel.VehicleInfo || Guard.isNullOrEmpty(addressModel.length)) {
                            //TODO 更新网络数据并保存在本地
                            Tracer.e(TAG, "version: get net data");
                            asynLoadAddress(json.VehicleInfo);
                        } else {
                            //TODO 获取本地数据
                            if (!Guard.isNullOrEmpty(addressModel.length)) {
                                Tracer.e(TAG, "get local data");
                                setData(addressModel.length);
                            } else {
                                Tracer.e(TAG, "local null:get default data");
                                getaddressinfo();
                            }
                        }

                    } else {
                        Tracer.e(TAG, "areainfo null: get default data");
                        getaddressinfo();
                    }
                }
            }

            @Override
            public void error(Return json) {
            }
        });
    }

    void asynLoadAddress(final double VehicleInfo) {
        HttpParams httpParams = new HttpParams(API.CONFIG.VehicleInfo);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(httpParams, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Tracer.e(TAG, "is ok");
                    addressModel = dbCache.GetObject(AddressModel.class);
                    if (Guard.isNull(addressModel)) {
                        addressModel = new AddressModel();
                    }
                    if (!Guard.isNullOrEmpty(res)) {
                        if (res.contains("\"Result\":0,")) {
                            //TODO 更新网络数据并保存在本地
                            String js = res.replace("\"Result\":0,", "");
                            Tracer.e(TAG, "get net data");
                            addressModel.length = js;
                            addressModel.VehicleInfo = VehicleInfo;
//                            FileUtil.writeFile("carFromNet.txt", js);
                            dbCache.save(addressModel);
                            setData(addressModel.length);
                        } else {
                            getaddressinfo();
                        }
                    } else {
                        getaddressinfo();
                    }
//						//TODO:tip the json is error
                } else {
                }
            }
        });
    }


    public static class JSONParser {
        public List<CarInfo> getJsonObject(String json) {
            List<CarInfo> provinceList = new ArrayList<>();
            CarLengthModel carLength = GsonHelper.fromJson(json, CarLengthModel.class);
            List<CarLengthModel.VehicleTypesBean> list = carLength.VehicleTypes;
            for (int i = 0; i < list.size(); i++) {
                CarInfo carInfo = new CarInfo();
                carInfo.setCity_name(list.get(i).Vehicle);
                carInfo.setId(list.get(i).Vehicle);
                provinceList.add(carInfo);
            }
            return provinceList;
        }

        public HashMap<String, List<CarInfo>> getJsonHashMap(String json) {
            HashMap<String, List<CarInfo>> hashMap = new HashMap<>();
            CarLengthModel carLength = GsonHelper.fromJson(json, CarLengthModel.class);
            List<CarLengthModel.VehicleTypesBean> list = carLength.VehicleTypes;
            for (int i = 0; i < list.size(); i++) {
                List<CarInfo> cityList = new ArrayList<>();
                for (int j = 0; j < list.get(i).Lengths.size(); j++) {
                    CarInfo carInfo = new CarInfo();
                    carInfo.setCity_name(list.get(i).Lengths.get(j));
                    cityList.add(carInfo);
                    hashMap.put(list.get(i).Vehicle, cityList);
                }
            }
            return hashMap;
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        Tracer.e(TAG, "attrs three");
        addressModel = dbCache.GetObject(AddressModel.class);
        if (Guard.isNull(addressModel)) {
            addressModel = new AddressModel();
        }
        if (model_list.size() <= 0
                && length_map.size() <= 0) {
            if (!Guard.isNullOrEmpty(addressModel.length)) {
                Tracer.e(TAG, "onFinishInflate local data");
                setData(addressModel.length);
            } else {
                Tracer.e(TAG, "onFinishInflate null:get default data");
                getaddressinfo();
            }
        }
        if (model_list.size() > 0
                && length_map.size() > 0) {
            LayoutInflater.from(getContext()).inflate(R.layout.city_picker1, this);
            citycodeUtil = CitycodeUtil.getSingleton();
            // 获取控件引用
            provincePicker = (ScrollerNumberPicker) findViewById(R.id.province1);

            cityPicker = (ScrollerNumberPicker) findViewById(R.id.city1);
            counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny1);
            counyPicker.setVisibility(GONE);
            provincePicker.setData(citycodeUtil.getCarType(model_list));
            provincePicker.setDefault(0);
            cityPicker.setData(citycodeUtil.getCarLength(length_map, citycodeUtil.getProvince_list_code().get(0)));
            cityPicker.setDefault(0);

            provincePicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

                @Override
                public void endSelect(int id, String text) {
                    // TODO Auto-generated method stub
                    System.out.println("id-->" + id + "text----->" + text);
                    if (text.equals("") || text == null)
                        return;
                    if (tempProvinceIndex != id) {
                        System.out.println("endselect");
                        String selectDay = cityPicker.getSelectedText();
                        if (selectDay == null || selectDay.equals(""))
                            return;
                        // 城市数组
                        cityPicker.setData(citycodeUtil.getCarLength(length_map,
                                citycodeUtil.getProvince_list_code().get(id)));
                        cityPicker.setDefault(0);


                        int lastDay = Integer.valueOf(provincePicker.getListSize());
                        if (id > lastDay) {
                            provincePicker.setDefault(lastDay - 1);
                        }
                    }
                    tempProvinceIndex = id;
                    Message message = new Message();
                    message.what = REFRESH_VIEW;
                    handler.sendMessage(message);
                }

                @Override
                public void selecting(int id, String text) {
                    // TODO Auto-generated method stub
                }
            });
            cityPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

                @Override
                public void endSelect(int id, String text) {
                    // TODO Auto-generated method stub
                    if (text.equals("") || text == null)
                        return;
                    if (temCityIndex != id) {
                        String selectDay = provincePicker.getSelectedText();
                        if (selectDay == null || selectDay.equals(""))
                            return;
                        String selectMonth = counyPicker.getSelectedText();
                        if (selectMonth == null || selectMonth.equals(""))
                            return;
                        int lastDay = Integer.valueOf(cityPicker.getListSize());
                        if (id > lastDay) {
                            cityPicker.setDefault(lastDay - 1);
                        }
                    }
                    temCityIndex = id;
                    Message message = new Message();
                    message.what = REFRESH_VIEW;
                    handler.sendMessage(message);
                }

                @Override
                public void selecting(int id, String text) {
                    // TODO Auto-generated method stub

                }
            });
        }
    }

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            super.handleMessage(msg);
            switch (msg.what) {
                case REFRESH_VIEW:
                    if (onSelectingListener != null)
                        onSelectingListener.selected(true);
                    break;
                default:
                    break;
            }
        }

    };

    public void setOnSelectingListener(OnSelectingListener onSelectingListener) {
        this.onSelectingListener = onSelectingListener;
    }

    public String getCity_code_string() {
        return city_code_string;
    }

    public String getProvince_string() {
        province_string = provincePicker.getSelectedText();

        return province_string;
    }

    public String getCity_string() {

        city_string = cityPicker.getSelectedText();
//		city_string = provincePicker.getSelectedText()
//				+ cityPicker.getSelectedText() + counyPicker.getSelectedText();
        return city_string;
    }

    public String[] getArea() {
        return new String[]{
                provincePicker.getSelectedText(),
                cityPicker.getSelectedText()
//				counyPicker.getSelectedText()
        };
    }


    public String getDistict_string() {

        district_string = counyPicker.getSelectedText();
        return district_string;
    }

    public interface OnSelectingListener {

        public void selected(boolean selected);
    }
}
