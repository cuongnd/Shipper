package org.csware.ee.utils;

import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Atwind on 2015/5/14.
 */
public class TypeHelper {

    public static String GetTypeFullName(Object o) {
        //TODO:这儿可以改为Hashcode为主键来存储，提升效率。目前先不改。
        String key = o.getClass().getName();
//        Log.d("TypeHelper","获得实例对象全名:["+key+"],hascode="+key.hashCode());
        return key;
    }

    public static <T> String GetTypeFullName(Class<T> clazz) {
        //TODO:这儿要用反射来真正的获取一下，目前为了进度先这样吧。
        //        String key = "";
//        key  = clazz.toString();
        //clazz = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
        //System.out.println(clazz.getClass().getSimpleName());
        //String key = clazz.getGenericSuperclass().getClass().toString();
//        Type type = clazz.getClass().getGenericSuperclass();
//        Type trueType = ((ParameterizedType) type).getActualTypeArguments()[0];
//        key = trueType.toString();
//        key =  ((ParameterizedType) getClass()
//                .getGenericSuperclass())
//                .getActualTypeArguments()[0];
        //key = clazz.getClass().getName();
        //Log.d("EEEE","KEY="+key);
        String key =  clazz.toString().replace("class ","");
        //key = key.substring(5,key.length()-6);
//        Log.d("TypeHelper","获得泛型全名:["+key+"],hascode="+key.hashCode());
        return key;
    }


    /**
     * 根据属性名获取属性值
     */
    private Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            //log.error(e.getMessage(),e);
            return null;
        }
    }

    /**
     * 获取属性名数组
     */
    private String[] getFiledName(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            System.out.println(fields[i].getType());
            fieldNames[i] = fields[i].getName();
        }
        return fieldNames;
    }

    /**
     * 获取属性类型(type)，属性名(name)，属性值(value)的map组成的list
     */
    private List getFiledsInfo(Object o) {
        Field[] fields = o.getClass().getDeclaredFields();
        String[] fieldNames = new String[fields.length];
        List list = new ArrayList();
        Map infoMap = null;
        for (int i = 0; i < fields.length; i++) {
            infoMap = new HashMap();
            infoMap.put("type", fields[i].getType().toString());
            infoMap.put("name", fields[i].getName());
            infoMap.put("value", getFieldValueByName(fields[i].getName(), o));
            list.add(infoMap);
        }
        return list;
    }

    /**
     * 获取对象的所有属性值，返回一个对象数组
     */
    public Object[] getFiledValues(Object o) {
        String[] fieldNames = this.getFiledName(o);
        Object[] value = new Object[fieldNames.length];
        for (int i = 0; i < fieldNames.length; i++) {
            value[i] = this.getFieldValueByName(fieldNames[i], o);
        }
        return value;
    }
}
