package org.csware.ee.utils;

import org.csware.ee.Guard;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

public class HttpParamsLoad {

    private static String ENCODING = "UTF-8";

    final Map<String, String> map = new HashMap<String, String>(); //HashMap 就是字典，相同的键会覆盖
    String timeStampParamName = "timestamp";
    String signaturePramName = "signature";
    boolean isAutoPlusParam = true;
    String token;
    public HttpParamsLoad() {
        String token = ClientStatus.getToken();
        if(!Guard.isNullOrEmpty(token))
            this.token = token;

//        自动加入devvice参数
        String deviceId = AppStatus.getDeviceId();
        if(!Guard.isNullOrEmpty(deviceId))
            addParam("device", AppStatus.getDeviceId());
    }

    public HttpParamsLoad(String token) {
        this.token = token;
    }

    public HttpParamsLoad(boolean isAutoPlusParam) {
        this.isAutoPlusParam = isAutoPlusParam;
    }


    public void addParam(String key, String value) {
        if(Guard.isNull(value)) value = "";
        map.put(key, value);
    }

    public void addParam(String key, int value) {
        addParam(key, Integer.toString(value));
    }

    public void addParam(String key, double value) {
        addParam(key, Double.toString(value));
    }

    public void addParam(String key,long value){
        addParam(key,Long.toString(value));
    }
    public void addParam(String key,Object object){
        addParam(key,object);
    }

    public void addTimestamp() {
        addTimestamp(timeStampParamName);
    }


    public String getQueryString() {
        if (isAutoPlusParam) {
            if (!map.containsKey(timeStampParamName)) {
                addParam(timeStampParamName, createTimestamp());
            }
        }
        Map<String, String> sortedParams = new TreeMap<String, String>(map);

        List<String> arrPm = new ArrayList<String>();
        for (String key : sortedParams.keySet()) {
            arrPm.add(key.concat("=").concat(sortedParams.get(key)));
        }

        String tmpStr = join("&", arrPm.toArray(new String[arrPm.size()]));
        if (!Guard.isNullOrEmpty(token)) {
            tmpStr += "&" + token;
        }

        if (isAutoPlusParam && !map.containsKey(signaturePramName)) {
            //String encodeParams = urlEncoding(tmpStr);
            //Log.d("md5_Source", tmpStr);
            //Log.d("md5_encode_Source", encodeParams);
            arrPm.add(signaturePramName.concat("=").concat(Md5Encryption.md5(tmpStr)));
        }
        return join("&", arrPm.toArray(new String[arrPm.size()]));
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

    public String getUrl(String baseQueryUrl) {
        String urlStr =  baseQueryUrl + getQueryString();
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

    public static String createTimestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }


    private static String byteToHex(final byte[] hash) {
        Formatter formatter = new Formatter();
        for (byte b : hash) {
            formatter.format("%02x", b);
        }
        String result = formatter.toString();
        formatter.close();
        return result;
    }

    private static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }


    String urlEncoding(String str){
        if(Guard.isNull(str)) return "";
        try {
            return  URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return "";
        }
    }

}
