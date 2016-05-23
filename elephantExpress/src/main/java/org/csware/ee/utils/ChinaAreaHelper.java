package org.csware.ee.utils;

import android.content.Context;

import org.csware.ee.Guard;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.model.AddressModel;
import org.csware.ee.widget.CityPicker1;
import org.csware.ee.widget.Cityinfo;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yu on 2015/6/25.
 */
public class ChinaAreaHelper {

    static String TAG = "ChinaAreaHelper";
    DbCache dbCache;
    AddressModel addressModel;
    public ChinaAreaHelper(Context context) {
        if (areaMap == null || areaMap.size() == 0) {
            CityPicker1.JSONParser parser = new CityPicker1.JSONParser();
            dbCache = new DbCache(context);
            addressModel = dbCache.GetObject(AddressModel.class);
            if (Guard.isNull(addressModel)){
                addressModel = new AddressModel();
            }

            String area_str ="";
            if (!Guard.isNullOrEmpty(addressModel.areas)){
                area_str = addressModel.areas;
            }else {
                area_str = FileUtil.readAssets(context, "json/area.json");
            }
            try {
                proMap = parser.getJSONParserResult(area_str, "area0");
                cityMap = parser.getJSONParserResultArray(area_str, "area1");
                areaMap = parser.getJSONParserResultArray(area_str, "area2");
            }catch (OutOfMemoryError error){
                error.printStackTrace();
            }
            //Log.d("X",areaMap.toString());
        }
    }

    List<Cityinfo> proMap;
    HashMap<String, List<Cityinfo>> cityMap;
    HashMap<String, List<Cityinfo>> areaMap;

    /**
     * 通过AreaId返回，省，市，县 名称数组
     */
    public String[] getName(String areaId) {
        String[] arrName = new String[3];
        if (Guard.isNullOrEmpty(areaId)) return arrName;
        if (areaMap == null || areaMap.size() == 0) return arrName;
        //前三位相等时进一步查找，二段数组信息
        String pId = "";
        if (areaId.length()>2) {
            pId = areaId.substring(0, 2); //前两位省
        }
        pId = pId + "0000";
        for (Cityinfo pro : proMap) {
            if (pId.equals(pro.getId())) {
                arrName[0] = pro.getCity_name();
                Tracer.d(TAG, arrName[0]);
                //Tracer.w("X", "XXXXXXXXX:" + pro.getCity_name() + "      id=" + pro.getId());
                break;
            }
        }
        String cId ="";
        if (areaId.length()>4) {
            cId = areaId.substring(0, 4) + "00";
        }
        for (Map.Entry<String, List<Cityinfo>> a : cityMap.entrySet()) {
            if (pId.equals(a.getKey())) {
                //Tracer.w("X", "XXXXXXXXX:" + a.getKey() + "      id=" + a.getValue());
                for (Cityinfo pro : a.getValue()) {
                    if (cId.equals(pro.getId())) {
                        arrName[1] = pro.getCity_name();
                        Tracer.d(TAG, arrName[1]);
                        break;
                    }
                }
                break;
            }
        }
        for (Map.Entry<String, List<Cityinfo>> entry : areaMap.entrySet()) {
            if (cId.equals(entry.getKey())) {
                for (Cityinfo item : entry.getValue()) {
                    if (areaId.equals(item.getId())) {
                        arrName[2] = item.getCity_name();
                        Tracer.d(TAG, arrName[2]);
                        break;
                    }
                }
                break;
            }

        }


        return arrName;
    }

    public String getAreaName(String areaId) {
        if (Guard.isNullOrEmpty(areaId)) return "";
        if (areaMap == null || areaMap.size() == 0) return "";
        //前三位相等时进一步查找，二段数组信息
        String tmpId = "";
        if (areaId.length()>3) {
            tmpId = areaId.substring(0, 3);
            // 遍历map 方法2
            for (Map.Entry<String, List<Cityinfo>> entry : areaMap.entrySet()) {
                //System.out.println(entry.getKey() + "--->" + entry.getValue());
                if (entry.getKey().substring(0, 3).equals(tmpId)) {
                    List<Cityinfo> items = entry.getValue();
                    for (Cityinfo item : items) {
                        if (item.getId().equals(areaId)) {
                            return item.getCity_name();
                        }
                    }
                }

            }
        }
        return "";
    }
    public String getCityName(String areaId){
        String cityId = getCityID(areaId);
        if(Guard.isNullOrEmpty(cityId)) return "";
        if(cityMap == null || cityMap.size() == 0) return "";
        //前三位相等时进一步查找，二段数组信息
        String tmpId = "";
        if (cityId.length()>2) {
            tmpId = cityId.substring(0,2);
            // 遍历map 方法2
            for (Map.Entry<String, List<Cityinfo>> entry : cityMap.entrySet()) {
                //System.out.println(entry.getKey() + "--->" + entry.getValue());
//            Log.e("ChinaArea",areaId+" "+tmpId+" "+entry.getKey().substring(0,3));
                if (entry.getKey().substring(0, 2).equals(tmpId)) {
                    List<Cityinfo> items = entry.getValue();
                    for (Cityinfo item : items) {
                        if (item.getId().equals(cityId)) {
                            return item.getCity_name();
                        }
                    }
                }

            }
        }
        return "";
    }
    public String getCityID(String areaId){
        if(Guard.isNullOrEmpty(areaId)) return "";
        if(areaMap == null || areaMap.size() == 0) return "";
        //前三位相等时进一步查找，二段数组信息
        String tmpId = "";
        if (areaId.length()>4) {
            tmpId = areaId.substring(0, 4);
            // 遍历map 方法2
            for (Map.Entry<String, List<Cityinfo>> entry : areaMap.entrySet()) {
                //System.out.println(entry.getKey() + "--->" + entry.getValue());
                if (entry.getKey().substring(0, 4).equals(tmpId)) {
                    List<Cityinfo> items = entry.getValue();
                    return entry.getKey();
                }

            }
        }
        return "";
    }
    public String getProvinceID(String areaId){
        String cityID = getCityID(areaId);
        if(Guard.isNullOrEmpty(cityID)) return "";
        if(cityMap == null || cityMap.size() == 0) return "";
        //前三位相等时进一步查找，二段数组信息
        String tmpId = "";
        if (cityID.length()>2) {
            tmpId = cityID.substring(0, 2);
            // 遍历map 方法2
            for (Map.Entry<String, List<Cityinfo>> entry : cityMap.entrySet()) {
                //System.out.println(entry.getKey() + "--->" + entry.getValue());
                if (entry.getKey().substring(0, 2).equals(tmpId)) {

                    List<Cityinfo> items = entry.getValue();
                    return entry.getKey();
//                for (Cityinfo item : items){
//                    if(item.getId().equals(areaId))
//                    {
//                        return item.getCity_name();
//                    }
//                }
                }

            }
        }
//        Tracer.e(TAG," tmp:"+tmpId+" areaId:"+areaId+" cityID:"+cityID);
//        for (int i=0;i<proMap.size();i++){
//            if (proMap.get(i).getId().substring(0,2).equals(tmpId)){
//                Tracer.e(TAG,proMap.get(i).getId().substring(0,3));
//                return proMap.get(i).getId();
//            }
//        }
        return "";
    }
    public String getProvinceName(String cityId){
        String provinceID = getProvinceID(cityId);
        if(Guard.isNullOrEmpty(provinceID)) return "";
        if(proMap == null || proMap.size() == 0) return "";
        for (int i=0;i<proMap.size();i++){
            if (provinceID.equals(proMap.get(i).getId())){
                return proMap.get(i).getCity_name();
            }
        }
        return "";
    }
}
