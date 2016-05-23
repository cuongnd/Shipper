package org.csware.ee.shipper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import org.csware.ee.view.ViewHolder;

import java.util.List;

/**
 * Created by 2015/9/8.
 * 通用集合适配器
 */
public abstract class CommonAdapter<T> extends BaseAdapter {
    protected LayoutInflater mInflater;
    protected Context mContext;
    protected List<T> _listData;
    protected final int mItemLayoutId;

    public CommonAdapter(Context context, List<T> listData, int itemLayoutId) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(mContext);
        this._listData = listData;
        this.mItemLayoutId = itemLayoutId;
    }

    @Override
    public int getCount() {
        return _listData.size();
    }

    @Override
    public T getItem(int position) {
        return _listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder = getViewHolder(position, convertView,
                parent);
        convert(viewHolder, getItem(position), position);
        return viewHolder.getConvertView();

    }

    public abstract void convert(ViewHolder helper, T item, int position);

    private ViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return ViewHolder.get(mContext, convertView, parent, mItemLayoutId,
                position);
    }

}