/*
 * Copyright 2014 trinea.cn All right reserved. This software is the confidential and proprietary information of
 * trinea.cn ("Confidential Information"). You shall not disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into with trinea.cn.
 */
package org.csware.ee.shipper.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jakewharton.salvage.RecyclingPagerAdapter;

import org.csware.ee.app.QCloudService;
import org.csware.ee.model.FlashInfo;
import org.csware.ee.shipper.WebViewActivity;

import java.util.List;

import cn.trinea.android.common.util.ListUtils;

/**
 * ImagePagerAdapter
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2014-2-23
 */
public class ImagePagerAdapter extends RecyclingPagerAdapter {

	private Context context;
//	private List<Object> imageIdList;
	List<FlashInfo.SlidersEntity> imageList;
	private boolean isDefaut;
	private int size;
	private boolean isInfiniteLoop;

	public ImagePagerAdapter(Context context, List<FlashInfo.SlidersEntity> imageIdList, boolean isDefaut) {
		this.context = context;
//		this.imageIdList = imageIdList;
		this.imageList = imageIdList;
		this.size = ListUtils.getSize(imageIdList);
		isInfiniteLoop = false;
		this.isDefaut = isDefaut;
	}

	@Override
	public int getCount() {
		// Infinite loop
		return isInfiniteLoop ? Integer.MAX_VALUE : ListUtils
				.getSize(imageList);
	}

	/**
	 * get really position
	 * 
	 * @param position
	 * @return
	 */
	private int getPosition(int position) {
		return isInfiniteLoop ? position % size : position;
	}

	@Override
	public View getView(final int position, View view, ViewGroup container) {
		ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = holder.imageView = new ImageView(context);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		if (imageList.size()>0) {
			if (isDefaut) {
	//				holder.imageView.setImageResource((Integer) imageIdList
	//						.get(getPosition(position)));
			}else {
	//			ImageLoader.getInstance().displayImage((String) imageIdList.get(getPosition(position)), holder.imageView);
				QCloudService.asyncDisplayImage(context,  imageList.get(getPosition(position)).getSource(), holder.imageView, false);
				holder.imageView.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View v) {
//						Toast.makeText(context, ""+imageList.get(getPosition(position)).getTarget(), Toast.LENGTH_SHORT).show();
						String value = "http://www.baidu.com";
						if (imageList.get(getPosition(position)).getTarget().startsWith("http")){
							value = imageList.get(getPosition(position)).getTarget();
						}
						Intent intent = new Intent();
						intent.setClass(context, WebViewActivity.class);
						intent.putExtra("url",value);
						context.startActivity(intent);
					}
				});
			}
		}
		return view;
	}

	private static class ViewHolder {

		ImageView imageView;
	}

	/**
	 * @return the isInfiniteLoop
	 */
	public boolean isInfiniteLoop() {
		return isInfiniteLoop;
	}

	/**
	 * @param isInfiniteLoop
	 *            the isInfiniteLoop to set
	 */
	public ImagePagerAdapter setInfiniteLoop(boolean isInfiniteLoop) {
		this.isInfiniteLoop = isInfiniteLoop;
		return this;
	}
}
