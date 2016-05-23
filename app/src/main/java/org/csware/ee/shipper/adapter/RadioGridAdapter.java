package org.csware.ee.shipper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RadioButton;

import org.csware.ee.shipper.R;

/**
 * Created by Yu on 2015/6/12.
 */

public class RadioGridAdapter extends BaseAdapter {

    private LayoutInflater layoutInflater;

    public RadioGridAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public RadioGridAdapter(Context context, int dataResId,String initValue) {
        layoutInflater = LayoutInflater.from(context);
        arrData = context.getResources().getStringArray(dataResId);
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
//        Log.w("X", "," + position);
        return arrData[position];
        //return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        View view = convertView;
        if (view == null) {
            view = layoutInflater.inflate(R.layout.radio_grid_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.radioButton = (RadioButton) view.findViewById(R.id.radio_view);
            viewHolder.radioButton.setText(arrData[position]);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }
        return view;
    }

    static class ViewHolder {
        RadioButton radioButton;
    }
}
