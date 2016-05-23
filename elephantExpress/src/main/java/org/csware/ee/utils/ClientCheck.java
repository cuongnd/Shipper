package org.csware.ee.utils;

import android.content.Context;

import org.csware.ee.app.Tracer;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Atwind on 2015/5/15.
 * 客户端相关判断与检查
 */
public class ClientCheck {



    public static boolean isValidStrnig(Context context,String s,int min,int max,String errTip)
    {
        if(s == null || s.equals("") || s.length() < min || s.length() > max)
        {
            AppStatus.ToastShowS(context,errTip);
            return false;
        }
        return true;
    }

    public static boolean isValidPhone(Context context,String s,String errTip)
    {
        if(s.equals("")||!isPhoneNO(s))
        {
            AppStatus.ToastShowS(context,errTip);
            return false;
        }
        return true;
    }
    public static boolean isValidLength(Context context,String s,String errTip)
    {
        if(s.length()!=11||!isPhoneNO(s))
        {
            AppStatus.ToastShowS(context,errTip);
            return false;
        }
        return true;
    }

    public  static boolean isValidPassword(Context context,String s,String errTip){
        if(s.equals("")||s.length()<6){
            AppStatus.ToastShowS(context,errTip);
            return false;
        }
        return true;
    }

    public static boolean isValiIDCard(Context context,String idCard, String errTip){
        ///^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$/  ^\\d{15}|^\\d{17}([0-9]|X|x)$  ^\\d{15}|^\\d{17}([0-9]|X|x)$
        Pattern p = Pattern.compile("^\\d{15}|^\\d{17}([0-9]|X)$");
        Matcher m = p.matcher(idCard);
        Tracer.e("ClientCheck",m.matches()+"");
        if (!m.matches()){
            AppStatus.ToastShowS(context,errTip);
            return false;
        }
        return true;
    }

    // 手机格式正则表达
     static  boolean isPhoneNO(String phone) {
        Pattern p = Pattern.compile("1[3|5|7|8|][0-9]{9}");
        Matcher m = p.matcher(phone);
        return m.matches();
    }

}
