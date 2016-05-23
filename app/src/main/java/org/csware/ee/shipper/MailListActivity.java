package org.csware.ee.shipper;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RatingBar;
import android.widget.TextView;

import com.baoyz.actionsheet.ActionSheet;
import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.Return;
import org.csware.ee.utils.pinyin.AssortPinyinList;
import org.csware.ee.utils.pinyin.LanguageComparator_CN;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.AssortView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/9.
 */
public class MailListActivity extends FragmentActivityBase {
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.btnFriend)
    LinearLayout btnFriend;
    @InjectView(R.id.elist)
    ExpandableListView elist;
    @InjectView(R.id.assort)
    AssortView assort;
    @InjectView(R.id.LinSearch)
    LinearLayout LinSearch;
    @InjectView(R.id.LinAddFreind)
    LinearLayout LinAddFreind;
    private PinyinAdapter adapter;
    DbCache dbCache;
    boolean isMail = false;
    boolean isShow = false;
    List<ContactListReturn.ContactsEntity> contactsList = new ArrayList<>();
    List<ContactListReturn.ContactsEntity> modelList = new ArrayList<>();
    ContactListReturn contactsModel;
    public static Activity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);
        ButterKnife.inject(this);
        instance = this;
        isMail = getIntent().getBooleanExtra("isMail", false);
        isShow = getIntent().getBooleanExtra("isShow", false);

        dbCache = new DbCache(this);
//        model = dbCache.GetObject(ContactsModel.class);
//        if (model ==null){
//            model = new ContactsModel();
//        }
        topBar.setTitle("联系人");
        if (isShow) {
            topBar.hideMenuIcon();
            topBar.setMenuTitle("确定");
            topBar.setMenuClickListener(new TopActionBar.MenuClickListener() {
                @Override
                public void menuClick() {
                    dbCache.save(contactsModel);
                    finish();
                }
            });
        }else {
            topBar.setMenu(R.drawable.w_cysj_icon_bj, new TopActionBar.MenuClickListener() {
                @Override
                public void menuClick() {
                    baseActivity.setTheme(R.style.ActionSheetStyle);
                    ActionSheet.createBuilder(baseActivity, getSupportFragmentManager())
                            .setCancelButtonTitle("取消")
                            .setOtherButtonTitles("删除司机")
                            .setCancelableOnTouchOutside(true)
                            .setListener(actionSheetListener).show();
                }
            });
        }

//        adapter = new PinyinAdapter(this, contactsList,isMail, isShow);
//        elist.setAdapter(adapter);

    }

    ActionSheet.ActionSheetListener actionSheetListener = new ActionSheet.ActionSheetListener() {
        @Override
        public void onOtherButtonClick(ActionSheet actionSheet, int index) {
//            Log.d(TAG, "click item index = " + index);
            if (index == 0) {
                //添加司机
//                swipeLayout.setVisibility(View.GONE);
//                LinAddFreind.setVisibility(View.VISIBLE);
//                hideMenu();
                startActivity(new Intent(baseActivity,UserFriendFragmentActivity.class));
            }

        }

        @Override
        public void onDismiss(ActionSheet actionSheet, boolean isCancle) {
//            Log.d(TAG, "dismissed isCancle = " + isCancle);
        }
    };

    @Override
    protected void onResume() {
        setData();
        super.onResume();
    }

    @OnClick(R.id.LinSearch)
    void setLinSearch() {
        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "contact_search");
        Intent intent = new Intent(this, MailSearchActivity.class);
        intent.putExtra("action", "formContacts");
        if (isShow) {
            intent.putExtra("isShow", true);
        }
        startActivityForResult(intent, 1001);
    }
    @OnClick(R.id.LinAddFreind)
    void setLinAddFreind() {
        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "contact_add");
        Intent intent = new Intent(this, UserFriendAddActivity.class);
        startActivity(intent);
    }
    @OnClick(R.id.btnFriend)
    void setBtnFriend() {
        HashMap<String,String> map = new HashMap<>();
        MobclickAgent.onEvent(baseActivity, "contact_list");
        startActivity(new Intent(this, MailFromListActivity.class));
    }

    void setData() {
        ContactApi.list(baseActivity, baseActivity, new IJsonResult<ContactListReturn>() {
            @Override
            public void ok(ContactListReturn json) {
                if (json.Contacts != null) {
                    modelList.clear();
                    contactsList.clear();
                    modelList.addAll(json.Contacts);
                    getmData(contactsList);

//                    if (adapter == null) {
                        adapter = new PinyinAdapter(baseActivity, contactsList, isMail, isShow);
                        elist.setAdapter(adapter);
//                    }

                    //展开所有
                    for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
                        elist.expandGroup(i);
                    }
                    adapter.notifyDataSetChanged();
                        Tracer.e("Trace", "OnResume:size" + contactsList.size());
                    //字母按键回调
                    assort.setOnTouchAssortListener(new AssortView.OnTouchAssortListener() {

                        View layoutView = LayoutInflater.from(MailListActivity.this)
                                .inflate(R.layout.alert_dialog_menu_layout, null);
                        TextView text = (TextView) layoutView.findViewById(R.id.content);
                        PopupWindow popupWindow;

                        public void onTouchAssortListener(String str) {
                            int index = adapter.getAssort().getHashList().indexOfKey(str);
                            if (index != -1) {
                                elist.setSelectedGroup(index);
                            }
                            if (popupWindow != null) {
                                text.setText(str);
                            } else {
                                popupWindow = new PopupWindow(layoutView,
                                        getResources().getDimensionPixelSize(R.dimen.submit_margin_top), getResources().getDimensionPixelSize(R.dimen.submit_margin_top),
                                        false);
                                // 显示在Activity的根视图中心
                                popupWindow.showAtLocation(getWindow().getDecorView(),
                                        Gravity.CENTER, 0, 0);
                            }
                            text.setText(str);
                        }

                        public void onTouchAssortUP() {
                            if (popupWindow != null)
                                popupWindow.dismiss();
                            popupWindow = null;
                        }
                    });
                }
            }

            @Override
            public void error(Return json) {

            }
        });
    }

    private void getmData(List<ContactListReturn.ContactsEntity> mDatas) {
        mDatas.addAll(modelList);
    }

    public class PinyinAdapter extends BaseExpandableListAdapter {

        // 字符串
        private List<String> strList;
        List<ContactListReturn.ContactsEntity> contactsList;
        private AssortPinyinList assort = new AssortPinyinList();
        List<List<ContactListReturn.ContactsEntity>> childList = new ArrayList<>();
        private Context context;
        boolean isMailList;
        boolean isShow;
        private LayoutInflater inflater;
        // 中文排序
        private LanguageComparator_CN cnSort = new LanguageComparator_CN();

        List<ContactListReturn.ContactsEntity> putList;
        DbCache dbCache;

        public PinyinAdapter(Context context, List<ContactListReturn.ContactsEntity> contactsList, boolean isMail, boolean isShow) {
            super();
            this.context = context;
            this.inflater = LayoutInflater.from(context);
            this.strList = strList;
            this.isMailList = isMail;
            this.isShow = isShow;
            this.contactsList = contactsList;
            if (strList == null) {
                strList = new ArrayList<String>();
            }
            dbCache = new DbCache(context);
            contactsModel = dbCache.GetObject(ContactListReturn.class);
            if (Guard.isNull(contactsModel)) {
                contactsModel = new ContactListReturn();
                contactsModel.Contacts = new ArrayList<>();
            }
            putList = contactsModel.Contacts;
            if (contactsList == null) {
                contactsList = new ArrayList<>();
            }
            long time = System.currentTimeMillis();
            // 排序
            if (contactsList.size()>0) {
                sort();
                add();
                Tracer.e("PinyinAdapter", childList.size() + "");
            }
//		Toast.makeText(context,String.valueOf(System.currentTimeMillis() - time), Toast.LENGTH_SHORT).show();

        }

        private void add() {
            for (int i = 0; i < assort.getHashList().size(); i++) {
                List<ContactListReturn.ContactsEntity> list = new ArrayList<>();
                for (int j = 0; j < contactsList.size(); j++) {
                    String Name = "";
                    if (!Guard.isNull(contactsList.get(j).BearerCompany)){
                        if (!Guard.isNullOrEmpty(contactsList.get(j).BearerCompany.CompanyName)){
                            Name = contactsList.get(j).BearerCompany.CompanyName;
                        }
                    }else {
                        Name = contactsList.get(j).Name;
                    }
                    String Acl = assort.getFirstChar(Name);
                    if (Acl.equals(assort.getHashList().getKeyIndex(i))) {
                        list.add(contactsList.get(j));
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
            try {
                for (int i = 0; i < contactsList.size(); i++) {
                    String Name = "";
                    if (!Guard.isNull(contactsList.get(i).BearerCompany)) {
                        if (!Guard.isNullOrEmpty(contactsList.get(i).BearerCompany.CompanyName)) {
                            Name = contactsList.get(i).BearerCompany.CompanyName;
                        }
                    } else {
                        Name = contactsList.get(i).Name;
                    }
                    assort.getHashList().add(Name);
                }
                assort.getHashList().sortKeyComparator(cnSort);
                for (int i = 0, length = assort.getHashList().size(); i < length; i++) {
                    Collections.sort((assort.getHashList().getValueListIndex(i)), cnSort);
                }
            }catch (OutOfMemoryError error){
                toastFast("内存不足，请结束一些应用后再试");
                error.printStackTrace();
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

        public boolean isContactSame(ContactListReturn.ContactsEntity entity, List<ContactListReturn.ContactsEntity> list) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Name.equals(entity.Name) || list.get(i).Mobile.equals(entity.Mobile)) {
//                car_diver_list.set(i,carDriverInfo);
                    return true;
                }
            }
            return false;
        }

        public boolean isSelect(ContactListReturn.ContactsEntity entity, List<ContactListReturn.ContactsEntity> list) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Name.equals(entity.Name) || list.get(i).Mobile.equals(entity.Mobile)) {
                    return list.get(i).isSelect;
                }
            }
            return false;
        }

        public int pos(ContactListReturn.ContactsEntity entity, List<ContactListReturn.ContactsEntity> list) {
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).Name.equals(entity.Name) || list.get(i).Mobile.equals(entity.Mobile)) {
                    return i;
                }
            }
            return -1;
        }

        public View getChildView(final int group, final int child, boolean arg2,
                                 View contentView, ViewGroup arg4) {
            // TODO Auto-generated method stub
            ViewHolder viewHolder = null;
//            final String info = (String) getChild(group,child);
            if (contentView == null) {
                contentView = inflater.inflate(R.layout.list_contact_item, null);
                viewHolder = new ViewHolder();
                viewHolder.textView = (TextView) contentView.findViewById(R.id.txtConComp);
                viewHolder.txtPlate = (TextView) contentView.findViewById(R.id.txtPlate);
                viewHolder.txtMailName = (TextView) contentView.findViewById(R.id.txtMailName);
                viewHolder.txtMailTip = (TextView) contentView.findViewById(R.id.txtMailTip);
                viewHolder.txtMailPhone = (TextView) contentView.findViewById(R.id.txtMailPhone);
                viewHolder.txtMailBtn = (TextView) contentView.findViewById(R.id.txtMailBtn);
                viewHolder.imgPhoneCall = (ImageView) contentView.findViewById(R.id.imgPhoneCall);
                viewHolder.imgMailHead = (ImageView) contentView.findViewById(R.id.imgMailHead);
                viewHolder.imgGameHead = (ImageView) contentView.findViewById(R.id.imgGameHead);
                viewHolder.imgSelectContact = (ImageView) contentView.findViewById(R.id.imgSelectContact);
                viewHolder.rate_rating = (RatingBar) contentView.findViewById(R.id.rate_rating);
                viewHolder.LinMailList = (LinearLayout) contentView.findViewById(R.id.LinMailList);
                viewHolder.LinContacts = (LinearLayout) contentView.findViewById(R.id.LinContacts);
                viewHolder.LinSelect = (LinearLayout) contentView.findViewById(R.id.LinSelect);
                contentView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) contentView.getTag();
                if (childList.get(group).get(child).isSelect) {
                    viewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
                } else {
                    viewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
                }

            }
//            viewHolder.imgSelectContact.setTag(info);
            String comName = "";
            if (!Guard.isNull(childList.get(group).get(child).BearerCompany)){
                if (!Guard.isNullOrEmpty(childList.get(group).get(child).BearerCompany.CompanyName)){
                    comName = childList.get(group).get(child).BearerCompany.CompanyName;
                }
                viewHolder.txtPlate.setVisibility(View.GONE);
            }else {
                comName = childList.get(group).get(child).Name;
                viewHolder.txtPlate.setVisibility(View.VISIBLE);
            }
            viewHolder.textView.setText(comName);
//            viewHolder.textView.setText(assort.getHashList().getValueIndex(group, child));
            String Plate = childList.get(group).get(child).Plate+"";
//            if (!Guard.isNullOrEmpty(Plate)) {
            viewHolder.txtPlate.setText(Plate);
//            }
//            Tracer.e("Pinyin",isMailList+"");
            if (isMailList) {
                viewHolder.LinContacts.setVisibility(View.GONE);
                viewHolder.LinMailList.setVisibility(View.VISIBLE);
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
//                viewHolder.txtMailName.setText(assort.getHashList().getValueIndex(group, child));
                viewHolder.txtMailPhone.setText(childList.get(group).get(child).Mobile + "");
                if (!Guard.isNull(childList.get(group).get(child).bitmap)) {
                    viewHolder.imgMailHead.setImageBitmap(childList.get(group).get(child).bitmap);
                }
                if (!Guard.isNullOrEmpty(childList.get(group).get(child).Avatar)) {
                    QCloudService.asyncDisplayImage(baseActivity, childList.get(group).get(child).Avatar, (ImageView) viewHolder.imgGameHead);
                }else {
                    viewHolder.imgGameHead.setImageResource(R.drawable.w_icon_tjzp);
                }
                viewHolder.txtMailBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
//			txtMailTip.setText(contactsList.get(child).Mobile);
            } else {
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
//                        Tracer.e("Pinyin",group+" child:"+child);
                        ContactListReturn.ContactsEntity contactsEntity = childList.get(group).get(child);
                        if (putList == null) {
                            putList = new ArrayList<>();
                        }
                        if (putList.size() > 0) {
                            if (isContactSame(contactsEntity, putList)) {
                                if (isSelect(contactsEntity, putList)) {
                                    finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
                                    contactsEntity.isSelect = false;
                                    int pos = pos(contactsEntity, putList);
                                    if (pos >= 0) {
                                        putList.remove(pos);
                                    }
                                } else {
                                    finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
                                    contactsEntity.isSelect = true;
                                    putList.add(contactsEntity);
                                }

                            } else {
                                finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
                                contactsEntity.isSelect = true;
                                putList.add(contactsEntity);
                            }
                        } else {
                            finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
                            contactsEntity.isSelect = true;
                            putList.add(contactsEntity);
                        }
                        contactsModel.Contacts = putList;
                    }
                };
                if (isShow) {
                    viewHolder.imgSelectContact.setVisibility(View.VISIBLE);
                    viewHolder.LinSelect.setOnClickListener(selectListener);
                    viewHolder.imgSelectContact.setOnClickListener(selectListener);
                    if (contactsModel.Contacts == null) {
                        contactsModel.Contacts = new ArrayList<>();
                    }
                    if (isContactSame(childList.get(group).get(child), contactsModel.Contacts)) {
                        if (isSelect(childList.get(group).get(child), contactsModel.Contacts)) {
                            finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
                        } else {
                            finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
                        }
                    }
//                    if (childList.get(group).get(child).isSelect){
//                        finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
//                    }else {
//                        finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
//                    }

                } else {
                    viewHolder.imgSelectContact.setVisibility(View.GONE);
                }
                if (!Guard.isNullOrEmpty(childList.get(group).get(child).Avatar)) {
                    QCloudService.asyncDisplayImage(baseActivity, childList.get(group).get(child).Avatar, (ImageView) viewHolder.imgGameHead);
                }else {
                    viewHolder.imgGameHead.setImageResource(R.drawable.w_icon_tjzp);
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

        private class ViewHolder {
            TextView textView, txtMailName, txtMailTip, txtMailPhone, txtMailBtn, txtPlate;
            ImageView imgPhoneCall, imgMailHead, imgSelectContact, imgGameHead;
            RatingBar rate_rating;
            LinearLayout LinMailList, LinContacts, LinSelect;
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

}
