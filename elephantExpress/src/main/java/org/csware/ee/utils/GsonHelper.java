package org.csware.ee.utils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Yu on 2015/6/3.
 */
public class GsonHelper {


    public static <T> T fromJson(String json, Class<T> classOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, classOfT);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        Gson gson = new Gson();
        return gson.fromJson(json, typeOfT);
    }


    public static String toJson(Object obj) {
        Gson gson = new Gson();
        return gson.toJson(obj);
    }
}
