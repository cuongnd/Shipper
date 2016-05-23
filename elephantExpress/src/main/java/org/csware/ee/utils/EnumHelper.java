package org.csware.ee.utils;

import android.util.Log;

import org.csware.ee.Guard;
import org.csware.ee.model.ICnEnum;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Yu on 2015/6/25.
 */
public class EnumHelper {

    public static <E extends Enum<E>> Map<Integer, String> getMap(Class<E> enumType) {
        Map<Integer, String> map = new HashMap<>();
        //方法1
        EnumSet<E> set = EnumSet.allOf(enumType);
        for (Enum<E> e : set) {
            map.put(e.ordinal(), e.toString());
        }
//        //方法2
//        E[] enums = enumType.getEnumConstants();
//        for(Enum<E> e : enums){
//            map.put(e.ordinal(), e.toString());
//        }
        return map;
    }


    public static <E extends ICnEnum> List<ICnEnum.CnEnum> getList(Class<E> enumType) {
        List<ICnEnum.CnEnum> list = new ArrayList<>();
        ICnEnum[] enums = enumType.getEnumConstants();
        for (ICnEnum e : enums) {
            //Log.e("T","e:"+e.toCnName()+"   e2:"+e.toName()+"   e3:"+e.toValue()+"  x:"+e.toString());
            list.add(e.toItem());
        }
        return list;
    }

//    /**
//     * 获取枚举信息
//     * 如果为空则返回第一个
//     */
//    public static <E extends ICnEnum> ICnEnum.CnEnum getCnEnum(int value, Class<E> enumType) {
//        List<ICnEnum.CnEnum> items = getList(enumType);
//        for (ICnEnum.CnEnum item : items) {
//            if (item.value == value) break;
//            return item;
//        }
//        return items.get(0);
//    }

    /**
     * 获取枚举值的中文名称
     */
    public static <E extends ICnEnum> String getCnName(int value, Class<E> enumType) {
        // if (value < 1) return null;
        List<ICnEnum.CnEnum> list = getList(enumType);
        for (ICnEnum.CnEnum item : list) {
            if (item.value == value)
                return item.cnName;
        }
        return null;
    }
    public static <E extends ICnEnum> String getCnName(String Name, Class<E> enumType) {
        // if (value < 1) return null;
        List<ICnEnum.CnEnum> list = getList(enumType);
        for (ICnEnum.CnEnum item : list) {
            if (item.name.equals(Name))
                return item.cnName;
        }
        return null;
    }

    public static <E extends ICnEnum> int getValue(String cnName, Class<E> enumType) {
        if (Guard.isNullOrEmpty(cnName)) return 0;
        List<ICnEnum.CnEnum> list = getList(enumType);
        for (ICnEnum.CnEnum item : list) {
            if (item.cnName.equals(cnName))
                return item.value;
        }
        return 0;
    }
    public static <E extends ICnEnum> String getName(String Name, Class<E> enumType) {
        // if (value < 1) return null;
        List<ICnEnum.CnEnum> list = getList(enumType);
        for (ICnEnum.CnEnum item : list) {
            if (item.cnName.equals(Name))
                return item.name;
        }
        return null;
    }

    /**
     * 获取中文本数组名称
     */
    public static <E extends ICnEnum> String[] getCnNameArray(Class<E> enumType) {
        List<ICnEnum.CnEnum> list = getList(enumType);
        List<String> names = new ArrayList<String>();
        for (ICnEnum.CnEnum item : list) {
            names.add(item.cnName);
        }
//        for (int i = 0;i<list.size();i++){
//            ICnEnum.CnEnum item = list.get(i);
//            names.add(item.cnName);
//        }
        return names.toArray(new String[]{});
    }

    /**
     * 获取中文数组名称，并且除去小于1的项
     */
    public static <E extends ICnEnum> String[] getCnNames(Class<E> enumType) {
        List<ICnEnum.CnEnum> list = getList(enumType);
        List<String> names = new ArrayList<String>();
        for (ICnEnum.CnEnum item : list) {
            if (item.value > 0)
                names.add(item.cnName);
        }
        return names.toArray(new String[]{});
    }
    /**
     * 获取中文数组名称，并且除去小于1的项
     */
    public static <E extends ICnEnum> String[] getCnName(Class<E> enumType) {
        List<ICnEnum.CnEnum> list = getList(enumType);
        List<String> names = new ArrayList<String>();
        for (ICnEnum.CnEnum item : list) {
//            if (item.value > 0)
                names.add(item.cnName);
        }
        return names.toArray(new String[]{});
    }
}
