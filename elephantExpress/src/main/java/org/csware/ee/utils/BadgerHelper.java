package org.csware.ee.utils;

import android.content.Context;

import org.csware.ee.Guard;
import org.csware.ee.app.DbCache;
import org.csware.ee.model.BadgerInfo;

import java.util.ArrayList;

/**
 * 右上角的提示提示内容帮助
 * Created by Yu on 2015/7/10.
 */
public class BadgerHelper {

    Context context;
    DbCache dbCache;
    BadgerInfo badger;

    public BadgerHelper(Context context) {
        this.context = context;
        dbCache = new DbCache(context);
        badger = dbCache.GetObject(BadgerInfo.class);
    }


    /**
     * 通过显示控件的ResId来查找是否有内容
     */
    public BadgerInfo.Badger getByResId(int resId) {
        if (badger == null || badger.badgers == null || badger.badgers.size() == 0)
            return null;
        for (BadgerInfo.Badger item : badger.badgers) {
            if (item.resId == resId)
                return item;
        }
        return null;
    }

    /**
     * 点击过提示的按钮后，提示消息要除去
     */
    public void clickedByResId(int resId) {
        if (badger == null || badger.badgers == null || badger.badgers.size() == 0)
            return;
        for (BadgerInfo.Badger item : badger.badgers) {
            if (item.resId == resId) {
                badger.badgers.remove(item);
            }
        }
        dbCache.save(badger);
    }

    public void addBadger(BadgerInfo.Badger item) {
        if (item.resId == 0 || Guard.isNullOrEmpty(item.tip)) return;
        if (badger == null)
            badger = new BadgerInfo();
        if (badger.badgers == null)
            badger.badgers = new ArrayList<>();

        for (BadgerInfo.Badger o : badger.badgers) {
            //移除所有的已有消息
            if (item.resId == o.resId) {
                badger.badgers.remove(o);
            }
        }
        badger.badgers.add(item);
        dbCache.save(badger);
    }


}
