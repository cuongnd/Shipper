package org.csware.ee.utils;

import net.tsz.afinal.annotation.sqlite.Id;
import net.tsz.afinal.annotation.sqlite.Table;

/**
 * Created by Atwind on 2015/5/14.
 * 缓存中的键值对，值一般为Json，用于快速查找信息
 * */
@Table(name="app_cache")
public class AppCacheInfo{

    @Id(column="key")
    private String key;
    private String value;
    private Object object;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public  void setValue(String value) {
        this.value = value;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}