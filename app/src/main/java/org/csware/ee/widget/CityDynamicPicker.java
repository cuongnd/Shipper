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
 * 
 */
public class CityDynamicPicker extends LinearLayout {
	/** 滑动控件 */
	private ScrollerNumberPicker provincePicker;
	private ScrollerNumberPicker cityPicker;
	private ScrollerNumberPicker counyPicker;
	/** 选择监听 */
	private OnSelectingListener onSelectingListener;
	/** 刷新界面 */
	private static final int REFRESH_VIEW = 0x001;
	/** 临时日期 */
	private int tempProvinceIndex = -1;
	private int temCityIndex = -1;
	private int tempCounyIndex = -1;
	private Context context;
	private List<Cityinfo> province_list = new ArrayList<Cityinfo>();
	private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
	private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();

	private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String province_string, city_string, district_string;
	DbCache dbCache;
	AddressModel addressModel;
	String TAG = "CityDynamicPicker";

	public CityDynamicPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		dbCache = new DbCache(context);
//		getaddressinfo();
		Tracer.e(TAG, "attrs one");
		asyncCompareVersion();
		// TODO Auto-generated constructor stub
	}

	public CityDynamicPicker(Context context) {
		super(context);
		this.context = context;
//		getaddressinfo();
		Tracer.e(TAG, "attrs two");
		asyncCompareVersion();
		// TODO Auto-generated constructor stub
	}

	// 获取城市信息
	private void getaddressinfo() {
		// TODO Auto-generated method stub
		// 读取城市信息string

		String area_str = FileUtil.readAssets(context, "json/area.json");
		setData(area_str);
	}

	void setData(String area_str){
		JSONParser parser = new JSONParser();
		Tracer.e(TAG, "attrs Analytical data");
		province_list = parser.getJSONParserResult(area_str, "area0");
		city_map = parser.getJSONParserResultArray(area_str, "area1");
		couny_map = parser.getJSONParserResultArray(area_str, "area2");
		Tracer.e(TAG, "attrs Analytical data size:"+province_list.size() + " city:"+city_map.size()+" conuny:"+couny_map.size());
	}

	// 获取城市信息
	private void asyncCompareVersion() {
		// TODO Auto-generated method stub
		HttpParams httpParams = new HttpParams(API.CONFIG.VERSION);
		ToolApi.getVersion(context, httpParams, new IJsonResult<AddressModel>() {
			@Override
			public void ok(AddressModel json) {
				addressModel = dbCache.GetObject(AddressModel.class);
				if (Guard.isNull(addressModel)){
					addressModel = new AddressModel();
				}
				if (!Guard.isNull(json)){
					if (!Guard.isNull(json.AreaInfo)){
						if (json.AreaInfo > addressModel.AreaInfo || Guard.isNullOrEmpty(addressModel.areas)){
							//TODO 更新网络数据并保存在本地
							Tracer.e(TAG, "version: get net data");
							asynLoadAddress(json.AreaInfo);
						}else {
							//TODO 获取本地数据
							if (!Guard.isNullOrEmpty(addressModel.areas)){
								Tracer.e(TAG, "get local data");
								setData(addressModel.areas);
							}else {
								Tracer.e(TAG, "local null:get default data");
								getaddressinfo();
							}
						}

					}else {
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

	void asynLoadAddress(final double AreaInfo){
		HttpParams httpParams = new HttpParams(API.CONFIG.ADDRESS);
		final HttpAjax ajax = new HttpAjax(context);
		ajax.beginRequest(httpParams, new IAjaxResult() {
			@Override
			public void notify(boolean result, String res) {
				if (result) {
					//成功
					Tracer.e(TAG,"is ok");
					addressModel = dbCache.GetObject(AddressModel.class);
					if (Guard.isNull(addressModel)){
						addressModel = new AddressModel();
					}
					if (!Guard.isNullOrEmpty(res)){
						if (res.contains("\"Result\":0,")) {
								//TODO 更新网络数据并保存在本地
							String js = res.replace("\"Result\":0,","");
							Tracer.e(TAG, "get net data");
							addressModel.areas = js;
							addressModel.AreaInfo = AreaInfo;
//							FileUtil.writeFile("areaFromNet.txt", js);
							dbCache.save(addressModel);
							setData(addressModel.areas);
						}else {
							getaddressinfo();
						}
					}else {
						getaddressinfo();
					}
//					r.notify(json);
//					if (r.isError()) {
//						//TODO:tip the json is error
//					}
				} else {
//					json.error(null);
				}
			}
		});
	}

	public static class JSONParser {
		public ArrayList<String> province_list_code = new ArrayList<String>();
		public ArrayList<String> city_list_code = new ArrayList<String>();

		public List<Cityinfo> getJSONParserResult(String JSONString, String key) {
			List<Cityinfo> list = new ArrayList<Cityinfo>();
			try {
				JsonObject result = new JsonParser().parse(JSONString)
						.getAsJsonObject().getAsJsonObject(key);

				Iterator<?> iterator = result.entrySet().iterator();
				while (iterator.hasNext()) {
					@SuppressWarnings("unchecked")
					Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
							.next();
					Cityinfo cityinfo = new Cityinfo();

					cityinfo.setCity_name(entry.getValue().getAsString());
					cityinfo.setId(entry.getKey());
					province_list_code.add(entry.getKey());
					list.add(cityinfo);
				}
				System.out.println(province_list_code.size());
			}catch (NullPointerException e){
				e.printStackTrace();
			}
			return list;
		}

		public HashMap<String, List<Cityinfo>> getJSONParserResultArray(
				String JSONString, String key) {
			HashMap<String, List<Cityinfo>> hashMap = new HashMap<String, List<Cityinfo>>();
			try {
				JsonObject result = new JsonParser().parse(JSONString)
						.getAsJsonObject().getAsJsonObject(key);

				Iterator<?> iterator = result.entrySet().iterator();
				while (iterator.hasNext()) {
					@SuppressWarnings("unchecked")
					Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
							.next();
					List<Cityinfo> list = new ArrayList<Cityinfo>();
					JsonArray array = entry.getValue().getAsJsonArray();
					for (int i = 0; i < array.size(); i++) {
						Cityinfo cityinfo = new Cityinfo();
						cityinfo.setCity_name(array.get(i).getAsJsonArray().get(1)
								.getAsString());
						cityinfo.setId(array.get(i).getAsJsonArray().get(0)
								.getAsString());
						city_list_code.add(array.get(i).getAsJsonArray().get(0)
								.getAsString());
						list.add(cityinfo);
					}
					hashMap.put(entry.getKey(), list);
				}
			}catch (NullPointerException e){
				e.printStackTrace();
			}
			return hashMap;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		Tracer.e(TAG, "attrs three");
		addressModel = dbCache.GetObject(AddressModel.class);
		if (Guard.isNull(addressModel)){
			addressModel = new AddressModel();
		}
		if (province_list.size() <= 0
				&& city_map.size() <= 0
				&& couny_map.size() <= 0){
			if (!Guard.isNullOrEmpty(addressModel.areas)){
				Tracer.e(TAG, "onFinishInflate local data");
				setData(addressModel.areas);
			}else {
				Tracer.e(TAG, "onFinishInflate null:get default data");
				getaddressinfo();
			}
		}
		if (province_list.size() > 0
				&& city_map.size() > 0
				&& couny_map.size() > 0) {
			LayoutInflater.from(getContext()).inflate(R.layout.city_picker1, this);
			citycodeUtil = CitycodeUtil.getSingleton();
			// 获取控件引用
			provincePicker = (ScrollerNumberPicker) findViewById(R.id.province1);

			cityPicker = (ScrollerNumberPicker) findViewById(R.id.city1);
			counyPicker = (ScrollerNumberPicker) findViewById(R.id.couny1);
			provincePicker.setData(citycodeUtil.getProvince(province_list));
			provincePicker.setDefault(0);
			cityPicker.setData(citycodeUtil.getCity(city_map, citycodeUtil
					.getProvince_list_code().get(0)));
			cityPicker.setDefault(0);
			counyPicker.setData(citycodeUtil.getCouny(couny_map, citycodeUtil
					.getCity_list_code().get(0)));
			counyPicker.setDefault(0);
//		city_code_string = citycodeUtil.getCouny_list_code().get(0);
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
						String selectMonth = counyPicker.getSelectedText();
						if (selectMonth == null || selectMonth.equals(""))
							return;
						// 城市数组
						cityPicker.setData(citycodeUtil.getCity(city_map,
								citycodeUtil.getProvince_list_code().get(id)));
						cityPicker.setDefault(0);
						counyPicker.setData(citycodeUtil.getCouny(couny_map,
								citycodeUtil.getCity_list_code().get(0)));
						city_code_string = citycodeUtil.getCouny_list_code().get(0);
						counyPicker.setDefault(0);
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
						counyPicker.setData(citycodeUtil.getCouny(couny_map,
								citycodeUtil.getCity_list_code().get(id)));
						counyPicker.setDefault(0);
						city_code_string = citycodeUtil.getCouny_list_code().get(0);
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
			counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {

				@Override
				public void endSelect(int id, String text) {
					// TODO Auto-generated method stub

					if (text.equals("") || text == null)
						return;
					if (tempCounyIndex != id) {
						String selectDay = provincePicker.getSelectedText();
						if (selectDay == null || selectDay.equals(""))
							return;
						String selectMonth = cityPicker.getSelectedText();
						if (selectMonth == null || selectMonth.equals(""))
							return;
						// 城市数组
						city_code_string = citycodeUtil.getCouny_list_code().get(id);
						int lastDay = Integer.valueOf(counyPicker.getListSize());
						if (id > lastDay) {
							counyPicker.setDefault(lastDay - 1);
						}
					}
					tempCounyIndex = id;
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

//		city_string = cityPicker.getSelectedText();
		city_string = "";
		if (!Guard.isNull(provincePicker)
				&& !Guard.isNull(cityPicker)
				&& !Guard.isNull(counyPicker)) {
			city_string = provincePicker.getSelectedText()
					+ cityPicker.getSelectedText()
					+ counyPicker.getSelectedText();
		}
		return city_string;
	}

	public String[] getArea(){
		String pArea ="";
		String cArea ="";
		String cyArea ="";
		if (!Guard.isNull(provincePicker)
				&& !Guard.isNull(cityPicker)
				&& !Guard.isNull(counyPicker)){
			pArea = provincePicker.getSelectedText();
			cArea = cityPicker.getSelectedText();
			cyArea = counyPicker.getSelectedText();
		}
		return new String[]{
				pArea,
				cArea,
				cyArea
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
