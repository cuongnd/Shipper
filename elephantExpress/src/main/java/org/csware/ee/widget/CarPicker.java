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

import org.csware.ee.R;
import org.csware.ee.model.CarInfo;
import org.csware.ee.utils.FileUtil;

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
public class CarPicker extends LinearLayout {
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
	private List<CarInfo> model_list = new ArrayList<CarInfo>();
	private HashMap<String, List<CarInfo>> length_map = new HashMap<String, List<CarInfo>>();
//	private HashMap<String, List<Cityinfo>> city_map = new HashMap<String, List<Cityinfo>>();
//	private HashMap<String, List<Cityinfo>> couny_map = new HashMap<String, List<Cityinfo>>();

	private CitycodeUtil citycodeUtil;
	private String city_code_string;
	private String province_string, city_string, district_string;

	public CarPicker(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	public CarPicker(Context context) {
		super(context);
		this.context = context;
		getaddressinfo();
		// TODO Auto-generated constructor stub
	}

	// 获取城市信息
	private void getaddressinfo() {
		// TODO Auto-generated method stub
		// 读取城市信息string
		JSONParser parser = new JSONParser();
		String area_str = FileUtil.readAssets(context, "json/carmodel.json");
		model_list = parser.getJSONParserResult(area_str, "carmodel");
		length_map = parser.getJSONParserResultArray(area_str, "carLength");
	}

	public static class JSONParser {
		public ArrayList<String> province_list_code = new ArrayList<String>();
		public ArrayList<String> city_list_code = new ArrayList<String>();

		public List<CarInfo> getJSONParserResult(String JSONString, String key) {
			List<CarInfo> list = new ArrayList<CarInfo>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator<?> iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("unchecked")
				Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
						.next();
				CarInfo cityinfo = new CarInfo();

				cityinfo.setCity_name(entry.getValue().getAsString());
				cityinfo.setId(entry.getKey());
				province_list_code.add(entry.getKey());
				list.add(cityinfo);
			}
			System.out.println(province_list_code.size());
			return list;
		}

		public HashMap<String, List<CarInfo>> getJSONParserResultArray(
				String JSONString, String key) {
			HashMap<String, List<CarInfo>> hashMap = new HashMap<String, List<CarInfo>>();
			JsonObject result = new JsonParser().parse(JSONString)
					.getAsJsonObject().getAsJsonObject(key);

			Iterator<?> iterator = result.entrySet().iterator();
			while (iterator.hasNext()) {
				@SuppressWarnings("unchecked")
				Entry<String, JsonElement> entry = (Entry<String, JsonElement>) iterator
						.next();
				List<CarInfo> list = new ArrayList<CarInfo>();
				JsonArray array = entry.getValue().getAsJsonArray();
				for (int i = 0; i < array.size(); i++) {
					CarInfo cityinfo = new CarInfo();
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
			return hashMap;
		}
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
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
//					String selectMonth = counyPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
					// 城市数组
					cityPicker.setData(citycodeUtil.getCarLength(length_map,
							citycodeUtil.getProvince_list_code().get(id)));
					cityPicker.setDefault(0);

//					city_code_string = citycodeUtil.getCouny_list_code().get(0);

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
//					counyPicker.setData(citycodeUtil.getCouny(couny_map,
//							citycodeUtil.getCity_list_code().get(id)));
//					counyPicker.setDefault(0);
//					city_code_string = citycodeUtil.getCouny_list_code().get(0);
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
//		counyPicker.setOnSelectListener(new ScrollerNumberPicker.OnSelectListener() {
//
//			@Override
//			public void endSelect(int id, String text) {
//				// TODO Auto-generated method stub
//
//				if (text.equals("") || text == null)
//					return;
//				if (tempCounyIndex != id) {
//					String selectDay = provincePicker.getSelectedText();
//					if (selectDay == null || selectDay.equals(""))
//						return;
//					String selectMonth = cityPicker.getSelectedText();
//					if (selectMonth == null || selectMonth.equals(""))
//						return;
//					// 城市数组
//					city_code_string = citycodeUtil.getCouny_list_code().get(id);
//					int lastDay = Integer.valueOf(counyPicker.getListSize());
//					if (id > lastDay) {
//						counyPicker.setDefault(lastDay - 1);
//					}
//				}
//				tempCounyIndex = id;
//				Message message = new Message();
//				message.what = REFRESH_VIEW;
//				handler.sendMessage(message);
//			}
//
//			@Override
//			public void selecting(int id, String text) {
//				// TODO Auto-generated method stub
//
//			}
//		});
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

	public String[] getArea(){
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
