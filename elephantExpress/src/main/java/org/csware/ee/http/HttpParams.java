package org.csware.ee.http;

import android.util.Log;

import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.Md5Encryption;

import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Atwind on 2015/5/15.
 * 对Http参数进宪处理
 */
public class HttpParams {

    final static String TAG = "HttpParams";
    private static String ENCODING = "UTF-8";

    final Map<String, String> map = new HashMap<String, String>(); //HashMap 就是字典，相同的键会覆盖
    String timeStampParamName = "timestamp";
    String signaturePramName = "signature";
    boolean isAutoPlusParam = true;
    String token;
    String baseUrl;

//    //TODO:DEL
//    public HttpParams(){
//        String token = ClientStatus.getToken();
//        if (!Guard.isNullOrEmpty(token))
//            this.token = token;
//
////        自动加入devvice参数
//        String deviceId = AppStatus.getDeviceId();
//        if (!Guard.isNullOrEmpty(deviceId))
//            addParam("device", AppStatus.getDeviceId());
//    }

    /**
     * 默认就加上Token
     */
    public HttpParams(String apiUri) {
        baseUrl = apiUri;

        String token = ClientStatus.getToken();
        if (!Guard.isNullOrEmpty(token))
            this.token = token;

//        自动加入devvice参数
        String deviceId = AppStatus.getDeviceId();
        if (!Guard.isNullOrEmpty(deviceId))
            addParam("device", AppStatus.getDeviceId());
    }

//    public HttpParams(String token) {
//        this.token = token;
//    }

    /**
     * 是否自动增加相关参数，默认增加
     */
    public HttpParams(boolean isAutoPlusParam) {
        this.isAutoPlusParam = isAutoPlusParam;
    }


    public void addParam(String key, String value) {
        if (Guard.isNull(value)) value = "";
        map.put(key, value);
    }

    public void addParam(String key, Object obj) {
        if (obj == null) return;
        addParam(key, obj.toString());
    }

    public void addParam(String key, int value) {
        addParam(key, Integer.toString(value));
    }

    public void addParam(String key, double value) {
        addParam(key, Double.toString(value));
    }

    public void addParam(String key, long value) {
        addParam(key, Long.toString(value));
    }


    public String getQueryString(long unitTimeLag) {
        if (isAutoPlusParam) {
            if (!map.containsKey(timeStampParamName)) {
                String unitTime = createTimestamp();
                String TimeLag = (Long.valueOf(unitTime)+unitTimeLag)+"";
                Tracer.e(TAG,"TimeLag:"+TimeLag+" unitTime:"+unitTime+" unitTimeLag:"+unitTimeLag);
                addParam(timeStampParamName, TimeLag+"");
            }
            //TODO:是否要自动加入其它变量？
        }
        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(map);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        List<String> arrPm = new ArrayList<String>();
        for (String key : sortedParams.keySet()) {
            arrPm.add(key.concat("=").concat(sortedParams.get(key)));
        }

        String tmpStr = join("&", arrPm.toArray(new String[arrPm.size()]));
        if (!Guard.isNullOrEmpty(token)) {
            tmpStr += "&" + token;
        } else {
//            Log.e(TAG, "the token where is server given key is null. please check it.");
        }

        if (isAutoPlusParam && !map.containsKey(signaturePramName)) {
            //String encodeParams = urlEncoding(tmpStr);
//            Tracer.d("signaturePramName", tmpStr+"\n"+Md5Encryption.md5(tmpStr));
//            Log.d("md5_encode_Source", tmpStr);
            arrPm.add(signaturePramName.concat("=").concat(Md5Encryption.md5(tmpStr)));
        }
        return join("&", arrPm.toArray(new String[arrPm.size()]));
    }
    public String getQueryString(Map<String, String> hashMap,String urls) {
        String url = "";
        if (isAutoPlusParam) {
            if (!hashMap.containsKey(timeStampParamName)) {
                hashMap.put(timeStampParamName, createTimestamp());
            }
            //TODO:是否要自动加入其它变量？
        }

        if (!hashMap.containsKey("device")){
            //        自动加入devvice参数
            String deviceId = AppStatus.getDeviceId();
            if (!Guard.isNullOrEmpty(deviceId))
                hashMap.put("device", AppStatus.getDeviceId());
        }

        // 先将参数以其参数名的字典序升序进行排序
        Map<String, String> sortedParams = new TreeMap<String, String>(hashMap);
        // 遍历排序后的字典，将所有参数按"key=value"格式拼接在一起
        List<String> arrPm = new ArrayList<String>();
        for (String key : sortedParams.keySet()) {
            if (sortedParams.get(key)!=null) {
//            Log.e(TAG, sortedParams.get(key));
                arrPm.add(key.concat("=").concat(sortedParams.get(key)));
            }
        }

        String tmpStr = join("&", arrPm.toArray(new String[arrPm.size()]));
        if (!Guard.isNullOrEmpty(token)) {
            tmpStr += "&" + token;
        } else {
//            Log.e(TAG, "the token where is server given key is null. please check it.");
        }
        String signnatrue = "";
        if (isAutoPlusParam && !hashMap.containsKey(signaturePramName)) {
            //String encodeParams = urlEncoding(tmpStr);
            //Log.d("md5_Source", tmpStr);
//            Log.d("md5_encode_Source", tmpStr);
            signnatrue = Md5Encryption.md5(tmpStr);
            arrPm.add(signaturePramName.concat("=").concat(Md5Encryption.md5(tmpStr)));
        }
        String deviceId = hashMap.get("device");
//        String timestamp = hashMap.get("timestamp");
        url = urls+"device="+deviceId+"&"+signaturePramName+"="+signnatrue;
        return url;
    }

    public static String join(String join, String[] strAry) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < strAry.length; i++) {
            if (i == (strAry.length - 1)) {
                sb.append(strAry[i]);
            } else {
                sb.append(strAry[i]).append(join);
            }
        }
        return new String(sb);
    }


    public String getUrl(long unitTimeLag){
        return getUrl(baseUrl, unitTimeLag);
    }

    /**
     * 独得请求路径
     */
    public String getUrl(String baseQueryUrl, long unitTimeLag) {
        String urlStr = baseQueryUrl + getQueryString(unitTimeLag);
        URL url = null;
        try {
            url = new URL(urlStr);
            URI uri = new URI(url.getProtocol(), url.getUserInfo(), url.getHost(), url.getPort(), url.getPath(), url.getQuery(), url.getRef());
            url = uri.toURL();
            return url.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return urlStr;
        }
    }

    public void addTimestamp(String paramName) {
        map.put(paramName, createTimestamp());
    }

    /**
     * 创建秒级时间戳
     */
    public static String createTimestamp() {
        String timestamp = Long.toString(System.currentTimeMillis() / 1000);
        String timestamp1 = Calendar.getInstance().getTimeInMillis()  / 1000+"";
        String timestamp2 = new Date().getTime() / 1000+ "";
//        Tracer.e(TAG,timestamp+" 1:"+timestamp1 +" 2:"+timestamp2);
        return timestamp;
    }


}
