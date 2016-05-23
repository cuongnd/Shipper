package org.csware.ee.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * Created by Yu on 2015/7/3.
 */
public class LayoutHelper {


    /**
     * 动态载入内容
     */
    public static View loadView(Context context, int layoutId) {
        LayoutInflater factory = LayoutInflater.from(context);
        View view = factory.inflate(layoutId, null);
        return view;
    }

    /**
     * 设置LiveView的高度（当ScrollView套接时）
     */
    public static void setListViewHeight(ListView listView) {
        if (listView == null) return;

        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

}
