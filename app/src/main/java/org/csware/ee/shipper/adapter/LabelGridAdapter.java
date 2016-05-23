package org.csware.ee.shipper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.shipper.R;

/**
 * Created by Yu on 2015/6/12.
 */

public class LabelGridAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    public LabelGridAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public LabelGridAdapter(Context context, int dataResId, String initValue) {
        layoutInflater = LayoutInflater.from(context);
        arrData = context.getResources().getStringArray(dataResId);
        value = initValue;
    }

    public LabelGridAdapter(Context context,String[] arrData, String initValue) {
        layoutInflater = LayoutInflater.from(context);
       this.arrData = arrData;
        value = initValue;
    }

    String value;

    String[] arrData;

    @Override
    public int getCount() {
        return arrData.length;
    }

    @Override
    public Object getItem(int position) {
        return arrData[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        //Log.w("getView", ":" + arrData[position] + "-" + value);    arrData[position].equals(value) ||
        if ((Guard.isNullOrEmpty(value)) && position == arrData.length - 1) {
            //Log.w("e", "有相关等或最后一个了");
            view = layoutInflater.inflate(R.layout.label_grid_item_on, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.textView.setText(arrData[position]);
            view.setTag(viewHolder);
        } else {
            if (view == null) {
                view = layoutInflater.inflate(R.layout.label_grid_item, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
                viewHolder.textView.setText(arrData[position]);
                view.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) view.getTag();
            }
        }
        return view;
    }

    static class ViewHolder {
        TextView textView;
    }
}
