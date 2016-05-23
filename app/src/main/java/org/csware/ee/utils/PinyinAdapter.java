package org.csware.ee.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.pinyin.AssortPinyinList;
import org.csware.ee.utils.pinyin.LanguageComparator_CN;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

public class PinyinAdapter extends BaseExpandableListAdapter {

	// 字符串
	private List<String> strList;
	List<ContactListReturn.ContactsEntity> contactsList;
	private AssortPinyinList assort = new AssortPinyinList();
	List<List<ContactListReturn.ContactsEntity>> childList = new ArrayList<>();
	private Context context;
	Activity activity;
	boolean isMailList;
	boolean isShow;
	private LayoutInflater inflater;
	// 中文排序
	private LanguageComparator_CN cnSort = new LanguageComparator_CN();
	ContactListReturn ContactListReturn;
	List<ContactListReturn.ContactsEntity> putList;
	DbCache dbCache;
	public PinyinAdapter(Activity activity, Context context, List<ContactListReturn.ContactsEntity> contactsList, boolean isMail, boolean isShow) {
		super();
		this.context = context;
		this.activity = activity;
		this.inflater = LayoutInflater.from(context);
		this.strList = strList;
		this.isMailList = isMail;
		this.isShow = isShow;
		this.contactsList = contactsList;
		if (strList == null) {
			strList = new ArrayList<String>();
		}
		dbCache = new DbCache(context);
		ContactListReturn = dbCache.GetObject(ContactListReturn.class);
		if (Guard.isNull(ContactListReturn)){
			ContactListReturn = new ContactListReturn();
		}
		putList = ContactListReturn.Contacts;
		if (contactsList == null){
			contactsList = new ArrayList<>();
		}
		long time = System.currentTimeMillis();
		// 排序
		if (contactsList.size()>0) {
			sort();
//			add();
		}
//		Toast.makeText(context,String.valueOf(System.currentTimeMillis() - time), Toast.LENGTH_SHORT).show();

	}

	private void add(){
		for (int i=0;i<assort.getHashList().size();i++) {
				List<ContactListReturn.ContactsEntity> list = new ArrayList<>();
				for (int j=0;j<contactsList.size();j++) {
					String Acl = assort.getFirstChar(contactsList.get(j).Name);
					if (Acl.equals(assort.getHashList().getKeyIndex(i))) {
						list.add(contactsList.get(j));
						Tracer.e("PinyinAdapter", i + " j:" + j + " " + contactsList.get(j).Name + " " + contactsList.get(j).Mobile);
					}
			}
			childList.add(list);
		}
	}

	private void sort() {
		// 分类
//		for (String str : strList) {
//			assort.getHashList().add(str);
//		}
		for (int i=0;i<contactsList.size();i++){
			assort.getHashList().add(contactsList.get(i).Name);
		}
		assort.getHashList().sortKeyComparator(cnSort);
		add();
		for(int i=0,length=assort.getHashList().size();i<length; i++) {
			Collections.sort((assort.getHashList().getValueListIndex(i)), cnSort);
		}

	}

	public Object getChild(int group, int child) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueIndex(group, child);
	}

	public long getChildId(int group, int child) {
		// TODO Auto-generated method stub
		return child;
	}

	public boolean isContactSame(ContactListReturn.ContactsEntity entity) {
		for (int i = 0; i < putList.size(); i++) {
			if (putList.get(i).Name.equals(entity.Name) || putList.get(i).Mobile.equals(entity.Mobile)) {
//                car_diver_list.set(i,carDriverInfo);
				return true;
			}
		}
		return false;
	}

	public View getChildView(final int group, final int child, boolean arg2,
							 View contentView, ViewGroup arg4) {
		// TODO Auto-generated method stub
		ViewHolder viewHolder = null;
		final String info = (String) getChild(group,child);
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.list_contact_item, null);
			viewHolder = new ViewHolder();
			viewHolder.textView = (TextView) contentView.findViewById(R.id.txtConComp);
			viewHolder.txtMailName = (TextView) contentView.findViewById(R.id.txtMailName);
			viewHolder.txtMailTip = (TextView) contentView.findViewById(R.id.txtMailTip);
			viewHolder.txtMailPhone = (TextView) contentView.findViewById(R.id.txtMailPhone);
			viewHolder.txtMailBtn = (TextView) contentView.findViewById(R.id.txtMailBtn);
			viewHolder.txtPlate = (TextView) contentView.findViewById(R.id.txtPlate);
			viewHolder.imgPhoneCall = (ImageView) contentView.findViewById(R.id.imgPhoneCall);
			viewHolder.imgMailHead = (ImageView) contentView.findViewById(R.id.imgMailHead);
			viewHolder.imgSelectContact = (ImageView) contentView.findViewById(R.id.imgSelectContact);
			viewHolder.rate_rating = (RatingBar) contentView.findViewById(R.id.rate_rating);
			viewHolder.LinMailList = (LinearLayout) contentView.findViewById(R.id.LinMailList);
			viewHolder.LinContacts = (LinearLayout) contentView.findViewById(R.id.LinContacts);
			viewHolder.LinSelect = (LinearLayout) contentView.findViewById(R.id.LinSelect);
			contentView.setTag(viewHolder);
		}else {
			viewHolder = (ViewHolder) contentView.getTag();
			if (childList.get(group).get(child).isSelect){
				viewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
			}else {
				viewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
			}

		}
		viewHolder.imgSelectContact.setTag(info);
		viewHolder.textView.setText(childList.get(group).get(child).Name);
//		viewHolder.textView.setText(assort.getHashList().getValueIndex(group, child));
		viewHolder.txtPlate.setText(childList.get(group).get(child).Plate+"");
		if (isMailList){
			viewHolder.LinContacts.setVisibility(View.GONE);
			viewHolder.LinMailList.setVisibility(View.VISIBLE);
//			Tracer.e("PinyinAdapter", assort.getHashList().getValueIndex(group, child) + " mobile:" + childList.get(group).get(child).Mobile + " name:" + childList.get(group).get(child).Name);
//			viewHolder.txtMailName.setText(assort.getHashList().getValueIndex(group, child));
//			viewHolder.txtMailName.setText(childList.get(group).get(child).Name);
			String Name = "";
			if (!Guard.isNull(childList.get(group).get(child).BearerCompany)){
				if (!Guard.isNullOrEmpty(childList.get(group).get(child).BearerCompany.CompanyName)){
					Name = childList.get(group).get(child).BearerCompany.CompanyName;
				}
				viewHolder.txtPlate.setVisibility(View.GONE);
			}else {
				Name = childList.get(group).get(child).Name;
				viewHolder.txtPlate.setVisibility(View.VISIBLE);
			}
			viewHolder.txtMailName.setText(Name);
			viewHolder.txtMailPhone.setText(childList.get(group).get(child).Mobile + "");
			if (childList.get(group).get(child).Mobile.replace(" ","").replace("\n","").trim().length()!=11){
				viewHolder.txtMailBtn.setVisibility(View.GONE);
			}else {
				viewHolder.txtMailBtn.setVisibility(View.VISIBLE);
			}
			if (!Guard.isNull(childList.get(group).get(child).bitmap)){
				viewHolder.imgMailHead.setImageBitmap(childList.get(group).get(child).bitmap);
			}
			final ViewHolder finalViewHolder1 = viewHolder;
			viewHolder.txtMailBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!childList.get(group).get(child).Registed){
						HttpParams params = new HttpParams(API.CONTACT.INVITE);
						params.addParam("mobile",childList.get(group).get(child).Mobile);
						ContactApi.sendInvate(activity, context, params, new IJsonResult() {
							@Override
							public void ok(Return json) {
//								setStyle(finalViewHolder1.txtMailBtn, R.color., R.drawable.btn_blue);
								setStyle(finalViewHolder1.txtMailBtn, R.color.face_grey, R.color.white);
								finalViewHolder1.txtMailBtn.setText("已邀请");
								finalViewHolder1.txtMailBtn.setEnabled(false);
							}

							@Override
							public void error(Return json) {

							}
						});
					}else {
						if (childList.get(group).get(child).Verified){
							if (!childList.get(group).get(child).InContact){
								ContactApi.edit(activity, context, ActionType.ADD, childList.get(group).get(child).Mobile, new IJsonResult() {
									@Override
									public void ok(Return json) {
										setStyle(finalViewHolder1.txtMailBtn, R.color.face_grey, R.color.white);
										finalViewHolder1.txtMailBtn.setEnabled(false);
										finalViewHolder1.txtMailBtn.setText("已添加");
									}
									@Override
									public void error(Return json) {

									}
								});
							}
						}else {
							if (!childList.get(group).get(child).Notified){
								HttpParams params = new HttpParams(API.CONTACT.NOTIFY);
								params.addParam("mobile",childList.get(group).get(child).Mobile);
								ContactApi.sendInvate(activity, context, params, new IJsonResult() {
									@Override
									public void ok(Return json) {
//								setStyle(finalViewHolder1.txtMailBtn, R.color., R.drawable.btn_blue);
										setStyle(finalViewHolder1.txtMailBtn, R.color.face_grey, R.color.white);
										finalViewHolder1.txtMailBtn.setEnabled(false);
										finalViewHolder1.txtMailBtn.setText("已提醒");
									}

									@Override
									public void error(Return json) {

									}
								});
							}
						}
					}
				}
			});
			if (childList.get(group).get(child).Registed) {
				if (childList.get(group).get(child).Verified){
					if (childList.get(group).get(child).InContact){
						viewHolder.txtMailBtn.setText("已添加");
						viewHolder.txtMailBtn.setEnabled(false);
						setStyle(viewHolder.txtMailBtn, R.color.face_grey, R.color.white);
					}else {
						viewHolder.txtMailBtn.setText("添加");
						setStyle(viewHolder.txtMailBtn, R.color.white, R.drawable.btn_blue);
					}
					viewHolder.txtMailTip.setText("");
				}else {
					if (childList.get(group).get(child).Notified){
						setStyle(viewHolder.txtMailBtn, R.color.face_grey, R.color.white);
						viewHolder.txtMailBtn.setText("已提醒");
						viewHolder.txtMailBtn.setEnabled(false);
					}else {
//						viewHolder.txtMailBtn.setTextAppearance(context,R.style.MailOnFont);
						setStyle(viewHolder.txtMailBtn, R.color.bg_blue, R.drawable.btn_blue_dis);
						viewHolder.txtMailBtn.setText("提醒");
					}
					viewHolder.txtMailTip.setText("该用户未认证");
				}

			}else {
				if (childList.get(group).get(child).Invited){
					setStyle(viewHolder.txtMailBtn, R.color.face_grey, R.color.white);
					viewHolder.txtMailBtn.setText("已邀请");
					viewHolder.txtMailBtn.setEnabled(false);
				}else {
//					viewHolder.txtMailBtn.setTextAppearance(context,R.style.MailOnFont);
					setStyle(viewHolder.txtMailBtn,R.color.bg_blue,R.drawable.btn_blue_dis);
					viewHolder.txtMailBtn.setText("邀请");
				}
				viewHolder.txtMailTip.setText("该用户未注册");
			}
		}else {
			if (!Guard.isNull(childList.get(group).get(child).BearerCompany)){
				if (!Guard.isNullOrEmpty(childList.get(group).get(child).BearerCompany.CompanyName)){
				}
				viewHolder.txtPlate.setVisibility(View.GONE);
			}else {
				viewHolder.txtPlate.setVisibility(View.VISIBLE);
			}
			viewHolder.LinContacts.setVisibility(View.VISIBLE);
			viewHolder.LinMailList.setVisibility(View.GONE);
			final ViewHolder finalViewHolder = viewHolder;
			View.OnClickListener selectListener = new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					ContactListReturn.ContactsEntity contactsEntity = childList.get(group).get(child);
					if (putList==null){
						putList = new ArrayList<>();
					}
					if (putList.size()>0){
						if (isContactSame(contactsEntity)){
							finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
							contactsEntity.isSelect = false;
							putList.remove(contactsEntity);
						}else {
							finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
							contactsEntity.isSelect = true;
							putList.add(contactsEntity);
						}
					}else {
							finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
						contactsEntity.isSelect = true;
						putList.add(contactsEntity);
					}
					ContactListReturn.Contacts = putList;
					dbCache.save(ContactListReturn);
				}
			};
			if (isShow){
				viewHolder.imgSelectContact.setVisibility(View.VISIBLE);
				viewHolder.LinSelect.setOnClickListener(selectListener);
				viewHolder.imgSelectContact.setOnClickListener(selectListener);

				if (childList.get(group).get(child).isSelect){
					finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
				}else {
					finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
				}

			}else {
				viewHolder.imgSelectContact.setVisibility(View.GONE);
			}
			viewHolder.rate_rating.setRating((float) childList.get(group).get(child).Rate);
			viewHolder.imgPhoneCall.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (!Guard.isNullOrEmpty(childList.get(group).get(child).Mobile)) {
						Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + childList.get(group).get(child).Mobile));
						context.startActivity(nIntent);
					}
				}
			});
		}

		return contentView;
	}
	private void setStyle(TextView textView,int color, int res){
		textView.setTextColor(context.getResources().getColor(color));
		textView.setBackgroundResource(res);
	}
	private class ViewHolder{
		TextView textView,txtMailName,txtMailTip,txtMailPhone,txtMailBtn, txtPlate;
		ImageView imgPhoneCall,imgMailHead,imgSelectContact;
		RatingBar rate_rating;
		LinearLayout LinMailList,LinContacts,LinSelect;
	}

	public int getChildrenCount(int group) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueListIndex(group).size();
	}

	public Object getGroup(int group) {
		// TODO Auto-generated method stub
		return assort.getHashList().getValueListIndex(group);
	}

	public int getGroupCount() {
		// TODO Auto-generated method stub
		return assort.getHashList().size();
	}

	public long getGroupId(int group) {
		// TODO Auto-generated method stub
		return group;
	}

	public View getGroupView(int group, boolean arg1, View contentView,
							 ViewGroup arg3) {
		if (contentView == null) {
			contentView = inflater.inflate(R.layout.list_group_item, null);
			contentView.setClickable(true);
		}
		TextView textView = (TextView) contentView.findViewById(R.id.name);
		textView.setText(assort.getFirstChar(assort.getHashList()
				.getValueIndex(group, 0)));
		// 禁止伸展

		return contentView;
	}

	public boolean hasStableIds() {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean isChildSelectable(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return true;
	}

	public AssortPinyinList getAssort() {
		return assort;
	}

}
