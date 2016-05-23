package org.csware.ee.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools
{
    //获取屏幕宽度
    public static int getScreenWidth(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }
    //获取屏幕高度
    public static int getScreenHeight(Activity context) {
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    // 检查网络
    public static boolean getNetWork(Context context)
    {
        if(context == null)
        {
            return false;
        }

        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(manager != null)
        {
            NetworkInfo info = manager.getActiveNetworkInfo();
            if(null != info)
            {
                return true;
            }
        }
        return false;
    }
    public static double add(double v1, double v2)

    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.add(b2).doubleValue();

    }

    public static double subtract(double v1, double v2)

    {

        BigDecimal b1 = new BigDecimal(Double.toString(v1));

        BigDecimal b2 = new BigDecimal(Double.toString(v2));

        return b1.subtract(b2).doubleValue();

    }
    public static double multiply(double v1, double v2)
    {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }


    static String global_cookie1="";
    static HashMap<String,String> global_cookieMap1 = new HashMap<String,String>();
    public static void updateCookie(String newCookie){
        newCookie = newCookie + "";
        String[] newCookies  =newCookie.split(";");
        HashMap<String,String> newCookieMap = new HashMap<String,String>();
        for (int i=0; i<newCookies.length; i++){
            int index =newCookies[i].indexOf("=");
            if (index<0){
                continue;
            }
            String key  =newCookies[i].substring(0,index);
            String value = newCookies[i].substring(index+1,newCookies[i].length());
            global_cookieMap1.put(key,value);
        }
        Object[] keys  = global_cookieMap1.keySet().toArray();
        global_cookie1="";
        for (int i=0; i<keys.length; i++){
            global_cookie1 += keys[i]+"="+global_cookieMap1.get((String)keys[i])+";";
        }
    }
    public static String getGlobal_cookie1(){
        //Log.i("global",global_cookie1);
        return global_cookie1;
    }
    public static void initCookie(){
        global_cookie1="";
        global_cookieMap1 = new HashMap<String, String>();
    }
    public static boolean isVerityNo(String verity){
        Pattern p = Pattern.compile("^\\d{12}$");
        ///\b\d{12}\b/
        Matcher m = p.matcher(verity);
        return m.matches();
    }

    public static boolean istartWithNum(String num){
        Pattern pattern = Pattern.compile("^(\\d+)(.*)");
        Matcher matcher = pattern.matcher(num);
        return matcher.matches();
    }
    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
  /*
   * 可接受的电话格式有：
   */
        String expression = "^\\(?(\\d{3})\\)?[- ]?(\\d{3})[- ]?(\\d{5})$";
  /*
   * 可接受的电话格式有：
   */
        String expression2 = "^\\(?(\\d{3})\\)?[- ]?(\\d{4})[- ]?(\\d{4})$";
        CharSequence inputStr = phoneNumber;
        Pattern pattern = Pattern.compile(expression);
        Matcher matcher = pattern.matcher(inputStr);

        Pattern pattern2 = Pattern.compile(expression2);
        Matcher matcher2 = pattern2.matcher(inputStr);
        if(matcher.matches() || matcher2.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isNumber(String num){
        Pattern p = Pattern.compile("[^0-9]");
        Matcher m = p.matcher(num);
        return m.matches();
    }

    public static String getNumber(String num){
        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(num);
        return m.replaceAll("").trim();
    }

    public static boolean isCardNO(String card) {
        Pattern p = Pattern
                .compile("^([0-9]{15})|([0-9]{18})$");
        Matcher m = p.matcher(card);

        return m.matches();
    }
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((1[3|4|5|8][0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);

        return m.matches();
    }
    //手机格式正则表达
    public static boolean isPhoneNO(String phone) {
        Pattern p = Pattern
                .compile("^1[3|4|5|8][0-9]\\d{4,8}$");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

    /**
     * 从字符串中截取连续6位数字
     * 用于从短信中获取动态密码
     * @param str 短信内容
     * @return 截取得到的6位动态密码
     */
    public static String getDynamicPassword(String str) {
        String regEx = "[^0-9]";
//        Pattern continuousNumberPattern = Pattern.compile("[0-9\\.]+");
        Pattern continuousNumberPattern = Pattern.compile(regEx);
        Matcher m = continuousNumberPattern.matcher(str);
        String dynamicPassword = "";
        dynamicPassword = m.replaceAll("").trim().toString();
//        while(m.find()){
//            if(m.group().length() == 6) {
//                System.out.print(m.group());
//                dynamicPassword = m.group();
//            }
//        }

        return dynamicPassword;
    }

    public static String getEncoderString(String str){
        String newString = "";
        try{
            newString = URLEncoder.encode(str,"UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return newString;
    }
    public static String getDecoderString(String str){
        String newString = "";
        try{
            newString = URLDecoder.decode(str, "UTF-8");
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return newString;
    }

//    String urlEncoding(String str){
//        if(Guard.isNull(str)) return "";
//        try {
//            return  URLEncoder.encode(str, "utf-8");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//            return "";
//        }
//    }





    public static String GetMethod(String url){
        String json = "";
        json = Tools.readParse(url);
        return json;
    }
//    public static String PostMethod(String url,Map<String,String> map){
//        String json = "";
//        json = NetUtil.submitPostData(map, "utf-8", url);
//        return json;
//    }

    //获取网络数据
    public static String getURLData(String path)
    {
        try
        {
            String response = new String(getByteArray(path),"UTF-8");
//            URLDecoder.decode(content,"utf-8")
            return response;
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 把网络数据保存为byte数组
     * @param path 路径
     * @return  byte[]
     * @throws Exception
     */
    public static byte[] getByteArray(String path)
    {
        try
        {
            String boundary = "*****";
//            System.setProperty("http.keepAlive", "false");
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("connection", "Keep-Alive");
//            conn.setRequestProperty("Content-Type",
//                    "multipart/form-data;boundary=" + boundary);
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
//            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
//            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
//            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.setRequestProperty("Host", "pbank.95559.com.cn");
//            conn.setRequestProperty("Origin", "https://creditcardapp.bankcomm.com");
//            conn.setRequestProperty("Referer", "https://pbank.95559.com.cn/personbank/credit/pb0525_apply_result_qry_req.jsp");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");

            if(conn.getResponseCode() == 200)
            {
                InputStream inputStream = conn.getInputStream();
                return streamToByte(inputStream);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }
    public static byte[] getByteArray(String path,String cookie)
    {
        try
        {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(6000);
            conn.setReadTimeout(6000);
            conn.setRequestMethod("GET");
//            conn.setRequestProperty("Cookie",cookie);
            conn.setRequestProperty("Connection", cookie);
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Content-Type", "text/html;charset=UTF-8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.setRequestProperty("Host", "pbank.95559.com.cn");
//            conn.setRequestProperty("Origin", "https://creditcardapp.bankcomm.com");
//            conn.setRequestProperty("Referer", "https://pbank.95559.com.cn/personbank/credit/pb0525_apply_result_qry_req.jsp");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");
            if(conn.getResponseCode() == 200)
            {
                InputStream inputStream = conn.getInputStream();
                return streamToByte(inputStream);
            } else {
                //Log.i("ToolsImg",conn.getResponseCode()+"");
                //Log.i("ToolsImg",conn.getResponseMessage()+"");
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    /**
     * 输入流保存为byte数组
     * @param stream  输入流
     * @return   byte[]
     * @throws Exception
     */
    public static byte[] streamToByte(InputStream stream)
    {
        try
        {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len;
            while(-1 != (len=stream.read(buffer)))
            {
                outputStream.write(buffer, 0 ,len);
            }
            stream.close();
            return outputStream.toByteArray();
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * json字符串转JSONArray
     * @param jsonStr  json字符串
     * @return   JSONArray
     */
    public static JSONArray getJsonArray(String jsonStr)
    {
        JSONArray jsonArray = null;
        try
        {
            jsonArray = new JSONArray(jsonStr);
//            Log.i("[value]","getJsonArray success");
        }
        catch(Exception e)
        {
            e.printStackTrace();
//            Log.i("[value]","getJsonArray error,"+e.getMessage());
        }
        return jsonArray;
    }

    /**
     * 获取网络图片
     * @param imgUrl   路径
     * @return     Bitmap
     * @throws Exception
     */
    public static Bitmap getBitmapFromUrl(String imgUrl)
    {
//         Log.i("[value]", "Tools getBitmapFromUrl" + imgUrl);
        byte[] buffer = getByteArray(imgUrl);
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length, null);
    }

    public static Bitmap getBitmapFromUrl(String imgUrl,String cookie)
    {
//         Log.i("[value]", "Tools getBitmapFromUrl" + imgUrl);
        byte[] buffer = getByteArray(imgUrl,cookie);
//        Log.i("bufff", buffer.length + "...");
        return BitmapFactory.decodeByteArray(buffer, 0, buffer.length, null);
    }

    public static String getDataByHttpClientGET(String path)
    {
        HttpClient client = new DefaultHttpClient();
        StringBuilder sb = new StringBuilder();

        HttpGet get = new HttpGet(path);
        try
        {
            HttpResponse response = client.execute(get);
            BufferedReader reader = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            for (String s = reader.readLine(); s != null; s = reader.readLine())
            {
                sb.append(s);
            }

        } catch(Exception e) {
            e.printStackTrace();

        }
        return sb.toString();
    }
//图片上传
    public static String getStringValue(String path,String business,String tax,String organ,String operate){

        String json = null;
        InputStream is = null;
        HttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("businessLicenseAddress", business));
        formparams.add(new BasicNameValuePair("taxRegistrationAddress", tax));
        formparams.add(new BasicNameValuePair("organizationAddress", organ));
        formparams.add(new BasicNameValuePair("operateAddress", operate));
        HttpPost post = new HttpPost(path);
        UrlEncodedFormEntity entity;
        HttpResponse response = null;
//        HttpPost request = new HttpPost(path);

        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept",
                    "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(entity);
            response = hc.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity e = response.getEntity();
//            System.out.println(EntityUtils.toString(e));
            if (200 == response.getStatusLine().getStatusCode()) {
                if (!e.equals(null)) {
//                    json = EntityUtils.toString(e);
                    json = "SUCCSESS";
                }
                System.out.println("上传完成");

            } else {
                System.out.println("上传失败");
            }
            hc.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
//        request.addHeader("Content-Type", "application/json");

//        try {
//            request.setEntity(new StringEntity(jsonstr.toString()));
//            response = hc.execute(request);
//            if (response.getStatusLine().getStatusCode()!=404){
//                HttpEntity entity = response.getEntity();
//                if (entity!=null){
//                    Log.i("entity",entity+"");
//                    is = entity.getContent();
////                    json = EntityUtils.toString(entity);
//
//                }
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        try {
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    is, "utf-8"), 8);
//            StringBuilder sb = new StringBuilder();
//            String line = null;
//            while ((line = reader.readLine()) != null) {
//                sb.append(line);
//            }
//            is.close();
//            json = sb.toString();
//
//        } catch (Exception e) {
//            Log.e("Buffer Error", "Error converting result " + e.toString());
//        }


        return json;
    }

    public static String getCarValue(String path,String carUrl){

        String json = null;
        InputStream is = null;
        HttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("carUrl", carUrl));

        HttpPost post = new HttpPost(path);
        UrlEncodedFormEntity entity;
        HttpResponse response = null;

        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept",
                    "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(entity);
            response = hc.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity e = response.getEntity();
            if (200 == response.getStatusLine().getStatusCode()) {
                if (!e.equals(null)) {
                    json = EntityUtils.toString(e);
                }
                System.out.println("上传完成");

            } else {
                System.out.println("上传失败");
            }
            hc.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String UpLoadListValue(String path,List<NameValuePair> formparams){

        String json = null;
        InputStream is = null;
        HttpClient hc = new DefaultHttpClient();
        HttpPost post = new HttpPost(path);
        UrlEncodedFormEntity entity;
        HttpResponse response = null;

        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept",
                    "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(entity);
            response = hc.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity e = response.getEntity();
            if (200 == response.getStatusLine().getStatusCode()) {
                if (!e.equals(null)) {
                    json = EntityUtils.toString(e);
                    //Log.i("Tools UpLoadListValue",json);
//                    Log.i("Tools UpLoadListValue ucode", URLDecoder.decode(json, "utf-8"));
                }
                System.out.println("上传完成");

            } else {
                System.out.println("上传失败");
            }
            hc.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String getDriverValue(String path,String carUrl){

        String json = null;
        InputStream is = null;
        HttpClient hc = new DefaultHttpClient();
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("driverUrl", carUrl));

        HttpPost post = new HttpPost(path);
        UrlEncodedFormEntity entity;
        HttpResponse response = null;

        try {
            entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            post.addHeader("Accept",
                    "text/javascript, text/html, application/xml, text/xml");
            post.addHeader("Accept-Charset", "GBK,utf-8;q=0.7,*;q=0.3");
            post.addHeader("Accept-Encoding", "gzip,deflate,sdch");
            post.addHeader("Connection", "Keep-Alive");
            post.addHeader("Cache-Control", "no-cache");
            post.addHeader("Content-Type", "application/x-www-form-urlencoded");
            post.setEntity(entity);
            response = hc.execute(post);
            System.out.println(response.getStatusLine().getStatusCode());
            HttpEntity e = response.getEntity();
            if (200 == response.getStatusLine().getStatusCode()) {
                if (!e.equals(null)) {
                    json = EntityUtils.toString(e);
                }
                System.out.println("上传完成");

            } else {
                System.out.println("上传失败");
            }
            hc.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return json;
    }

    public static String readParse(String path) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        URL url = null;
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            InputStream inputStream = conn.getInputStream();
            while((len = inputStream.read(data))!=-1){
                outputStream.write(data,0,len);
            }
            inputStream.close();

        } catch (MalformedURLException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        } catch (IOException e) {
            e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
        }
        return new String(outputStream.toByteArray());
    }



    // Post方式请求
    public static String reqByPost(String path)
    {
        String info = "";
        try
        {
            // 新建一个URL对象
            URL url = new URL(path);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 允许Input、output不适用Cache
            urlConn.setDoInput(true);
            urlConn.setDoOutput(true);
            urlConn.setUseCaches(false);
            /** 设置传送的method=post */
            urlConn.setRequestMethod("POST");
            urlConn.setRequestProperty("Connection", "Keep-Alive");
            urlConn.setRequestProperty("Charset", "utf-8");
            InputStream in = urlConn.getInputStream();

            try
            {

                byte[] buffer = new byte[65536];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int readLength = in.read(buffer);
                long totalLength = 0;
                while (readLength>= 0){
                    bos.write(buffer,0,readLength);
                    totalLength = totalLength + readLength;
                    readLength = in.read(buffer);

                }
                info = bos.toString();

            }
            finally
            {
                if(in!=null)
                    try{
                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            }
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            // 开始连接
            urlConn.connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return null;
        }
        return info.replaceAll( "\r\n", " ");
    }

    // Post方式请求
    public static String reqByPost(String path,String sessionId) {
        String info = "";
        try{
            // 新建一个URL对象
            URL url = new URL(path);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            urlConn.setRequestProperty("Cookie",sessionId);
            InputStream in = urlConn.getInputStream();

            try
            {
                byte[] buffer = new byte[65536];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int readLength = in.read(buffer);
                long totalLength = 0;
                while (readLength>= 0)
                {
                    bos.write(buffer,0,readLength);
                    totalLength = totalLength + readLength;
                    readLength = in.read(buffer);

                }
                info = bos.toString("UTF-8");
            }
            finally
            {
                if(in!=null)
                    try{
                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            }

            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            // 开始连接
            urlConn.connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
            return "";
        }
        return info.replaceAll( "\r\n", " ");
    }
    public static String GetDataByPost(String httpUrl, String parMap)
    {
        try {
            URL url = new URL(httpUrl);// 创建连接
            HttpURLConnection connection = (HttpURLConnection) url
                    .openConnection();
            connection.setDoOutput(true);
            connection.setDoInput(true);
            connection.setUseCaches(false);
            connection.setInstanceFollowRedirects(true);
            connection.setRequestMethod("POST"); // 设置请求方式
            connection.setRequestProperty("Accept", "application/json"); // 设置接收数据的格式
            connection.setRequestProperty("Content-Type", "application/json"); // 设置发送数据的格式
            connection.connect();
            OutputStreamWriter out = new OutputStreamWriter(
                    connection.getOutputStream(), "UTF-8"); // utf-8编码
            out.append(parMap);
            out.flush();
            out.close();
            // 读取响应
            int length = (int) connection.getContentLength();// 获取长度
            InputStream is = connection.getInputStream();
            if (length != -1) {
                byte[] data = new byte[length];
                byte[] temp = new byte[1024];
                int readLen = 0;
                int destPos = 0;
                while ((readLen = is.read(temp)) > 0) {
                    System.arraycopy(temp, 0, data, destPos, readLen);
                    destPos += readLen;
                }
                String result = new String(data, "UTF-8"); // utf-8编码
                return result;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println("aa:  "+e.getMessage());
        }
        return "error"; // 自定义错误信息
    }
    public static String reqByPostSI(String path,String cookie,String params){
        String info = "";
        String sessionId  = "";
        try
        {
            URL url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setConnectTimeout(20000);
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            // conn.setDoInput(true);

//            conn.setInstanceFollowRedirects(isJump);

            conn.setRequestProperty("Cookie",cookie);
            conn.setRequestProperty("Connection", "keep-alive");
            conn.setRequestProperty("Accept-Encoding", "gzip,deflate,sdch");
            conn.setRequestProperty("Cache-Control", "max-age=0");
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
            conn.setRequestProperty("Accept", "application/json,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
//            conn.setRequestProperty("Host", "creditcardapp.bankcomm.com");
//            conn.setRequestProperty("Origin", "https://creditcardapp.bankcomm.com");
//            conn.setRequestProperty("Referer", "https://creditcardapp.bankcomm.com/member/home/index.html");
            conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/35.0.1916.114 Safari/537.36");

//
            byte[] bypes = params.getBytes();
            OutputStream os = conn.getOutputStream();
            os.write(bypes);// 输入参数
            os.flush();
            os.close();


            InputStream in = conn.getInputStream();
//            System.out.println(conn.getHeaderField("Set-Cookie"));


            try
            {

                byte[] buffer = new byte[65536];
                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                int readLength = in.read(buffer);
                long totalLength = 0;
                while (readLength>= 0){
                    bos.write(buffer,0,readLength);
                    totalLength = totalLength + readLength;
                    readLength = in.read(buffer);

                }
                info =bos.toString("UTF-8");
            }
            finally
            {
                if(in!=null)
                    try{
                        in.close();
                    }catch(Exception e){
                        e.printStackTrace();
                    }
            }
            // 设置连接超时时间
            //conn.setConnectTimeout(5 * 1000);
            // 开始连接
            // conn.connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();

            return "";
        }
        return info;

    }

    public static String getSessionID(String path)
    {
        String[] sessionId = null;
        try
        {
            // 新建一个URL对象
            URL url = new URL(path);
            // 打开一个HttpURLConnection连接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            String session_value = urlConn.getHeaderField("Set-Cookie");
            sessionId = session_value.split(";");
            // 设置连接超时时间
            urlConn.setConnectTimeout(5 * 1000);
            // 开始连接
            urlConn.connect();
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return sessionId[0];
    }


    public static ProgressDialog getDialog(Context context)
    {
        ProgressDialog dialog = ProgressDialog.show(context, null ,"连接中,正在拼命加载,请稍候...",true, true);
//        dialog.getWindow().setContentView(R.layout.refresh);
        return dialog;
    }

    public static void show(final Activity activity,final int id)
    {
        final Context context =  activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("网络错误");
        builder.setMessage("网络连接错误，请先检查网络设置");


        builder.setPositiveButton("设置网络",new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                int version = android.os.Build.VERSION.SDK_INT;
                Intent intent;
                if(version < 11)
                {
                    intent = new Intent();
                    intent.setClassName("com.android.settings", "com.android.settings.WirelessSettings");
                }
                else
                {
                    //3.0以后
                    //intent = new Intent( android.provider.Settings.ACTION_WIRELESS_SETTINGS);
                    intent = new Intent( android.provider.Settings.ACTION_SETTINGS);
                }
                if(id == 1)
                {
                    activity.finish();
                }
                context.startActivity(intent);
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                if(id == 1)
                {
                    activity.finish();
                }
            }
        });
        builder.create().show();
    }
    /**
     * 切换软键盘的状态
     * 如当前为收起变为弹出,若当前为弹出变为收起
     */
    public static void toggleInput(Context context){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }

    /**
     * 强制隐藏输入法键盘
     */
    public static void hideInput(Context context,View view){
        InputMethodManager inputMethodManager =
                (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}
