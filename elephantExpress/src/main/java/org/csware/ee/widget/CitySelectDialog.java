package org.csware.ee.widget;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gghl.view.wheelcity.OnWheelChangedListener;
import com.gghl.view.wheelcity.WheelView;
import com.gghl.view.wheelcity.adapters.AbstractWheelTextAdapter;
import com.gghl.view.wheelcity.adapters.ArrayWheelAdapter;

import org.csware.ee.Guard;
import org.csware.ee.R;
import org.csware.ee.consts.AreaIndexInfo;
import org.csware.ee.consts.AreaItem;
import org.csware.ee.consts.ChinaArea;
import org.csware.ee.utils.ParamTool;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Yu on 2015/5/26.
 * 弹出式City选择器，模拟IOS
 */
public class CitySelectDialog {

    /**
     * 以下为对话框支持相关*
     */

    public CitySelectDialog(Context context) {
        this.context = context;
    }

    Context context;

    AreaItem currentProvince;
    AreaItem currentCity;
    AreaItem currentArea;
    AreaIndexInfo areaIndexInfo = new AreaIndexInfo();

    public AreaIndexInfo getAreaIndexInfo(){
        return areaIndexInfo;
    }

    /**
     * 返回选中 的具体地区
     * 必须为第三级
     */
    public String getText() {
//        AreaItem item = currentArea;
//        if (item == null) item = currentCity;
        AreaItem item = currentCity;
        if (item == null) item = currentProvince;
        if (item == null) return null;
        return ParamTool.cutString(item.name, 7);
    }

    /**返回省市县*/
    public String getDetailText(){
        String address = currentProvince.name;
        if(currentCity!=null && !Guard.isNullOrEmpty(currentCity.name)) address += ","+currentCity.name;
        if(currentArea!=null && !Guard.isNullOrEmpty(currentArea.name)) address += ","+currentArea.name;
        return address;
    }

    final ChinaArea chinaArea = new ChinaArea();
    final List<AreaItem> provinceList = chinaArea.getProvinces();
    final List<AreaItem> cityList = chinaArea.getCities();
    final List<AreaItem> areaList = chinaArea.getAreas();

    //当前选中省的市级集合
    List<AreaItem> currentCities = new ArrayList<>();
    //当前选中的地区集合
    List<AreaItem> currentAreas = new ArrayList<>();

    /**
     * 弹出选择框
     */
    public View popDialog(AreaIndexInfo areaIndex) {
       if(areaIndex!=null) this.areaIndexInfo = areaIndex;

        View contentView = LayoutInflater.from(context).inflate(
                R.layout.wheelcity_cities_layout, null);

        //省级选择
        final WheelView provinceView = (WheelView) contentView.findViewById(R.id.wheelcity_country);
        //provinceView.setVisibleItems(5); //默认可见数量
        provinceView.setViewAdapter(new ProvinceAdapter(context));

        //市级选择
        final WheelView cityView = (WheelView) contentView.findViewById(R.id.wheelcity_city);
        //cityView.setVisibleItems(5);

        // 地区选择
        final WheelView areaView = (WheelView) contentView.findViewById(R.id.wheelcity_ccity);
        //areaView.setVisibleItems(5);

        // 省变动事件
        provinceView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //Log.d("省变动事件", "oldValue:" + oldValue + "   newValue:" + newValue + "     getCurrentItem:" + provinceView.getCurrentItem());
                areaIndexInfo.setProvinceIndex(provinceView.getCurrentItem());
                currentProvince = provinceList.get(provinceView.getCurrentItem());
                if (currentProvince == null) return;
//                Log.d("当前省：", "areaId=" + currentProvince.areaId + " ;name=" + currentProvince.name);
                currentCity = null;
                currentArea = null;
                currentCities = null;
                currentAreas = null;

                currentCities = getListByParentId(cityList, currentProvince.areaId);
                ArrayWheelAdapter<AreaItem> adapter = new ArrayWheelAdapter<AreaItem>(context, currentCities.toArray(new AreaItem[currentCities.size()]));
                //ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(context, toArray(currentCities));
                adapter.setTextSize(14);
                cityView.setViewAdapter(adapter);
                cityView.setCurrentItem(0);
            }
        });

        // 市变动事件
        cityView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
//                Log.d("市变动事件", "oldValue:" + oldValue + "   newValue:" + newValue + "     ProvinceIndex:" + provinceView.getCurrentItem());
                areaIndexInfo.setCityIndex(cityView.getCurrentItem());
                currentCity = currentCities.get(cityView.getCurrentItem());
                if (currentCity == null) return;
//                Log.d("当前市：", "areaId=" + currentCity.areaId + " ;name=" + currentCity.name);
                currentArea = null;
                currentAreas = null;

                currentAreas = getListByParentId(areaList, currentCity.areaId);
                ArrayWheelAdapter<AreaItem> adapter = new ArrayWheelAdapter<AreaItem>(context, currentAreas.toArray(new AreaItem[currentAreas.size()]));
                //ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<>(context, toArray(currentAreas));
                adapter.setTextSize(14);
                areaView.setViewAdapter(adapter);
                areaView.setCurrentItem(0);
            }
        });


        // 区域变动事件
        areaView.addChangingListener(new OnWheelChangedListener() {
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                //Log.d("区域变动事件", "oldValue:" + oldValue + "   newValue:" + newValue);
                areaIndexInfo.setAreaIndex(areaView.getCurrentItem());
                currentArea = currentAreas.get(areaView.getCurrentItem());
                if (currentArea == null) return;
//                Log.d("当前区：", "areaId=" + currentArea.areaId + " ;name=" + currentArea.name);
            }
        });

        provinceView.setCurrentItem(areaIndexInfo.getProvinceIndex());//设置默认数据
        cityView.setCurrentItem(areaIndexInfo.getCityIndex());
        areaView.setCurrentItem(areaIndexInfo.getAreaIndex());
        return contentView;
    }

    /**
     * 返回父Id对应的集合
     */
    List<AreaItem> getListByParentId(List<AreaItem> items, int areaId) {
        List<AreaItem> list = new ArrayList<AreaItem>();
        int max = items.size();
        for (int i = 0; i < max; i++) {
            AreaItem item = items.get(i);
            //Log.d("LOOP","ParentId:"+item.areaId);
            if (item.parentId == areaId) list.add(item);
        }
        return list;
    }

//    String[] toArray(List<AreaItem> items) {
//        List<String> list = new ArrayList<>();
//        for (int i = 0; i < items.size(); i++) {
//            list.add(items.get(i).name);
//        }
//        return list.toArray(new String[0]);
//    }

//    /**
//     * 根据areaId返回一个实例，可能为null
//     */
//    AreaItem findItemByAreaId(List<AreaItem> items, int areaId) {
//        int max = items.size();
//        for (int i = 0; i < max; i++) {
//            AreaItem item = items.get(i);
//            if (item.areaId == areaId) {
//                return item;
//                //break;
//            }
//        }
//        return null;
//    }


    /**
     * Adapter for Provinces
     */
    class ProvinceAdapter extends AbstractWheelTextAdapter {
        /**
         * Constructor
         */
        protected ProvinceAdapter(Context context) {
            super(context, R.layout.wheelcity_country_layout, NO_RESOURCE);
            setItemTextResource(R.id.wheelcity_country_name);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            View view = super.getItem(index, cachedView, parent);
            return view;
        }

        @Override
        public int getItemsCount() {
            return provinceList.size();
        }

        @Override
        protected CharSequence getItemText(int index) {
            AreaItem item = provinceList.get(index);
            if (item != null) {
                return item.toString(); //返回名称
            }
            return null;
        }

    }


}
