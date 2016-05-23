package org.csware.ee.app;

import android.content.Context;
import android.util.Log;

import net.tsz.afinal.FinalDb;

import org.csware.ee.Guard;
import org.csware.ee.utils.AppCacheInfo;
import org.csware.ee.utils.DbManager;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.TypeHelper;

import java.util.List;

/**
 * Created by Atwind on 2015/5/14.
 * <p/>
 * 程序相关信息信息
 */
public class DbCache {


    Context context;

    public DbCache(Context context) {
        this.context = context;
    }

    /**
     * 保存内容缓存至DB中，有则更新，没有新加
     */
    public void save(String key, String value) {
        //Guard.NotNull("key", key);
        if (Guard.isNullOrEmpty(value)) return;
        if (Guard.isNullOrEmpty(key)) {
            key = value.hashCode() + "";
        }
        FinalDb db = DbManager.OpenDb(context);
        //查询是否存在
        AppCacheInfo cs = GetObject(db, key);
        if (cs == null) {
            cs = new AppCacheInfo();
            cs.setKey(key);
            cs.setValue(value);
            db.save(cs);
//            Log.d("DbCache", "缓存项增加[Key=" + key + "][value:" + value + "]");
        } else {
            //TODO:是存储的效率低还是equals的效率低这个有待验证，目前先这样。
            if (!cs.getValue().equals(value)) {
                cs.setValue(value);
                db.update(cs);
//                Log.d("DbCache", "缓存项更新[Key=" + key + "][value:" + value + "]");
            }
        }
    }

    /**
     * 增加对象缓存
     * 如果value为null时啥也不做
     */
    public void save(String key, Object value) {
        //Guard.NotNull("value", value);
        if (value == null) return;
        if (key == null) key = TypeHelper.GetTypeFullName(value);
        String jsonStr = GsonHelper.toJson(value);
        save(key, jsonStr);
    }

    /**
     * 将对象直接缓存，键名为其类型全名
     */
    public void save(Object value) {
        String key = TypeHelper.GetTypeFullName(value);
        String jsonStr = GsonHelper.toJson(value);
        save(key, jsonStr);
    }


    /**
     * 获得缓存的实例对象
     */
    public <T> T GetObject(String key, Class<T> clazz) {
        String json = GetValue(key);
        if (Guard.isNullOrEmpty(json)) return null;
        return GsonHelper.fromJson(json, clazz);
    }

    /**
     * 直接获取对象的缓存实例
     */
    public <T> T GetObject(Class<T> clazz) {
        String key = TypeHelper.GetTypeFullName(clazz);
        return GetObject(key, clazz);
    }

    /**
     * 根据键获得值
     */
    public String GetValue(String key) {
        if (Guard.isNullOrEmpty(key)) return null;
        FinalDb db = DbManager.OpenDb(context);
        AppCacheInfo cs = GetObject(db, key);
        return cs == null ? null : cs.getValue();
    }
    public Object GetObject(String key){
        if (Guard.isNullOrEmpty(key)) return null;
        FinalDb db = DbManager.OpenDb(context);
        AppCacheInfo cs = GetObject(db, key);
        return cs == null ? null : cs.getObject();
    }

    private AppCacheInfo GetObject(FinalDb db, String key) {
        //查询是否存在
        List<AppCacheInfo> cs = db.findAllByWhere(AppCacheInfo.class, "key='" + key + "'");
//        Log.d("DbCache", "从DB缓存中获取[Key=" + key + "]的值，结果为:" + (Guard.IsNullOrEmpty(cs) ? "null" : cs.get(0).getValue()));
        if (Guard.IsNullOrEmpty(cs)) {
            return null;
        } else {
            return cs.get(0);
        }
    }


}


