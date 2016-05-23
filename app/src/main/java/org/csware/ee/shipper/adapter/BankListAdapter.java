package org.csware.ee.shipper.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.csware.ee.shipper.R;
import org.csware.ee.utils.ResourceHelper;

/**
 * Created by Yu on 2015/6/12.
 */

public class BankListAdapter extends BaseAdapter {

    final static String TAG = "BankListAdapter";

    private LayoutInflater layoutInflater;

    public BankListAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
    }

    public BankListAdapter(Context context, int dataResId, String initValue) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        arrData = context.getResources().getStringArray(dataResId);
        this.arrIcon = context.getResources().getStringArray(R.array.bankcard_icons);
        value = initValue;
    }

    public BankListAdapter(Context context, String[] arrData, String[] arrIcon, String initValue) {
        layoutInflater = LayoutInflater.from(context);
        this.arrData = arrData;
        this.arrIcon = arrIcon;
        this.context = context;
        value = initValue;
    }

    String value;
    Context context;
    String[] arrData;
    String[] arrIcon;

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
        //Log.w(TAG, ":" + arrData[position] + "-" + value);
        if (view == null) {
            view = layoutInflater.inflate(R.layout.list_item_bankname, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) view.findViewById(R.id.text_view);
            viewHolder.textView.setText(arrData[position]);
            viewHolder.imageView = (ImageView) view.findViewById(R.id.ivIcon);
            viewHolder.imageView.setImageDrawable(ResourceHelper.getByIconName(context, arrIcon[position]));
            view.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        return view;
    }

    static class ViewHolder {
        TextView textView;
        ImageView imageView;
    }
}
