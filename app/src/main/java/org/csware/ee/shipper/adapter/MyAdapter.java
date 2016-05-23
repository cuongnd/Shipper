package org.csware.ee.shipper.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.csware.ee.model.BankInfo;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.ResourceHelper;
import org.csware.ee.widget.OnDeleteListioner;

import java.util.List;


public class MyAdapter extends BaseAdapter {

	private Context mContext;
	private List<BankInfo> mlist = null;
	private OnDeleteListioner mOnDeleteListioner;
	ChinaAreaHelper areaHelper;
	private boolean delete = false;
	private boolean isShowCard = false;
//	areaHelper = new ChinaAreaHelper(mContext);

	// private Button curDel_btn = null;
	// private UpdateDate mUpdateDate = null;

	public MyAdapter(Context mContext, List<BankInfo> mlist, boolean isShowCard) {
		this.mContext = mContext;
		this.mlist = mlist;
		this.isShowCard = isShowCard;
		areaHelper = new ChinaAreaHelper(mContext);

	}

	public void setDelete(boolean delete) {
		this.delete = delete;
	}

	public boolean isDelete() {
		return delete;
	}

	public void setOnDeleteListioner(OnDeleteListioner mOnDeleteListioner) {
		this.mOnDeleteListioner = mOnDeleteListioner;
	}

	public int getCount() {

		return mlist.size();
	}

	public Object getItem(int pos) {
		return mlist.get(pos);
	}

	public long getItemId(int pos) {
		return pos;
	}

	public View getView(final int pos, View convertView, ViewGroup p) {

		final ViewHolder viewHolder;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.list_item_bankcard, null);
			viewHolder = new ViewHolder();
			//viewHolder.name = (TextView) convertView.findViewById(R.id.item_text);
			viewHolder.delete_action = (TextView) convertView.findViewById(R.id.delete_action);
			viewHolder.labName = (TextView) convertView.findViewById(R.id.labName);
			viewHolder.labCarNo = (TextView) convertView.findViewById(R.id.labCarNo);
			viewHolder.labStyle = (TextView) convertView.findViewById(R.id.labStyle);
			viewHolder.ivIcon = (ImageView) convertView.findViewById(R.id.ivIcon);
			convertView.setTag(viewHolder);

		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}

		final OnClickListener mOnClickListener = new OnClickListener() {

			@Override
			public void onClick(View view) {
				if (mOnDeleteListioner != null)
					mOnDeleteListioner.onDelete(pos);
					//删除URL
			}
		};
		viewHolder.delete_action.setOnClickListener(mOnClickListener);
		viewHolder.labName.setText(mlist.get(pos).getBankName());
		if (isShowCard) {
			viewHolder.labCarNo.setText(mlist.get(pos).getCardNo());
			viewHolder.labCarNo.setVisibility(View.VISIBLE);
//			viewHolder.ivIcon.setImageDrawable(ResourceHelper.getByName(mContext, R.array.bankcard_names, R.array.bankcard_icons, mlist.get(pos).getBankName()));
		}else {
			viewHolder.labCarNo.setVisibility(View.GONE);

		}
		viewHolder.ivIcon.setImageResource(mlist.get(pos).getBankResId());
//		if (isShowCard) {
//		}else {
//		}
		//银行卡类型
		//viewHolder.labStyle.setText(mlist.get(pos).get);

		return convertView;
	}

	public static class ViewHolder {
		public TextView labName,labCarNo,labStyle;
		public TextView delete_action;
		public ImageView ivIcon;
	}

}
