package org.csware.ee.utils;

import android.content.Context;
import android.util.Log;


import org.csware.ee.app.DbCache;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.Shipper;

import java.util.HashMap;
import java.util.List;

/**
 * Created by WuHaoyu on 2015/7/2.
 */
public class URLUtils {





    public static String getURL(Context context,HashMap<String,String> map,String urlstr) {
        String deviceId;
        final String token;
        String url="";
//        try {
        DbCache dbCache = new DbCache(context);
        Shipper shipper = dbCache.GetObject(Shipper.class);
        if (shipper != null) {
            token = shipper.getToken();
            deviceId = shipper.getDeviceId();
            HttpParamsLoad pms = new HttpParamsLoad(token);
            pms.addParam("device", deviceId);
            for (HashMap.Entry<String,String> entry:map.entrySet()) {
//                pms.addParam((String)list.get(i).get("key"),list.get(i).get("value"));
                pms.addParam(entry.getKey(),entry.getValue());
            }
            url =  pms.getUrl(urlstr);
        }
        else {
            //没有登录信息，可能是首次安装转向登录页面
//                redirectLoginUI();
        }
        return url;
    }

    public static HashMap<String,String> getEncodeHashMap(HashMap<String,String> map){
        HashMap<String,String> hashMap = new HashMap<>();
        for (HashMap.Entry<String,String> entry:map.entrySet()){
            hashMap.put(entry.getKey(),Tools.getEncoderString(entry.getValue()));
        }
        return hashMap;
    }

    public static HttpParams getMapValue(String url,HashMap<String,String> map){
        HttpParams json= new HttpParams(url);
        for (HashMap.Entry<String,String> entry:map.entrySet()){
            json.addParam(entry.getKey(),entry.getValue());
            //Log.d("URLUtils",entry.getKey()+" "+entry.getValue());
        }
        return json;
    }


}
