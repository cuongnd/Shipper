package org.csware.ee.shipper;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.ContactsModel;
import org.csware.ee.model.Return;
import org.csware.ee.utils.PinyinAdapter;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.AssortView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/11.
 */
public class MailFromListActivity extends ActivityBase {
    List<ContactListReturn.ContactsEntity> contactsList = new ArrayList<>();
    List<ContactListReturn.ContactsEntity> tmpList = new ArrayList<>();
    public static final String[] PHONES_PROJECTION = new String[]{
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME, ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Photo.PHOTO_ID, ContactsContract.CommonDataKinds.Phone.CONTACT_ID};
    public static final int PHONES_DISPLAY_NAME_INDEX = 0;
    public static final int PHONES_NUMBER_INDEX = 1;
    public static final int PHONES_PHOTO_ID_INDEX = 2;
    public static final int PHONES_CONTACT_ID_INDEX = 3;

    @InjectView(R.id.LinSearch)
    LinearLayout LinSearch;
    @InjectView(R.id.elist)
    ExpandableListView elist;
    @InjectView(R.id.assort)
    AssortView assort;
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.LinContacts)
    LinearLayout LinContacts;
    @InjectView(R.id.LinJurisdiction)
    LinearLayout LinJurisdiction;

    private PinyinAdapter adapter;
    public Activity instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maillist);
        ButterKnife.inject(this);
        instance = this;
        LinContacts.setVisibility(View.VISIBLE);
        LinJurisdiction.setVisibility(View.GONE);
        topBar.setTitle("通讯录");
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        getPhoneContacts(this, contactsList);
        int absent = TelephonyManager.SIM_STATE_ABSENT;

        if (1 == absent){
            //Log.d(TAG,"请确认sim卡是否插入或者sim卡暂时不可用！");
        }else{
                getSIMContacts(this, contactsList);
        }

    }
    void initData(){
        if (contactsList.size()>0) {
            LinContacts.setVisibility(View.VISIBLE);
            LinJurisdiction.setVisibility(View.GONE);
//            DbCache dbCache = new DbCache(baseActivity);
//            ContactListReturn listReturn = new ContactListReturn();
//            listReturn.Contacts = contactsList;
//            dbCache.save("ContactList",listReturn);
            compare();
//            getPhoneContacts();
//            setData();
        } else {
            if (dialog != null) {
                if (dialog.isShowing()) {
                    dialog.dismiss();
                }
            }
//            toastFast("木有获得通讯录访问权限");
            LinContacts.setVisibility(View.GONE);
            LinJurisdiction.setVisibility(View.VISIBLE);
        }
    }
    ProgressDialog dialog;
    private void compare() {

        String mobiles = "";
        StringBuffer buffer = new StringBuffer();
        for (int i=0;i<contactsList.size();i++){
//            if (contactsList.get(i).Mobile.length()==11){
                buffer.append(contactsList.get(i).Mobile.trim());
                if (i!=(contactsList.size()-1)){
                    buffer.append(",");
                }
//            }
        }
        mobiles = buffer.toString();

        ContactApi.comparePost(baseActivity, mobiles, new IJsonResult<ContactsModel>() {
            @Override
            public void ok(ContactsModel json) {
                if (json.Result == 0) {
                    for (int i = 0; i < contactsList.size(); i++) {
                        if (json.Contacts.get(i).Mobile.equals(contactsList.get(i).Mobile)) {
                            ContactListReturn.ContactsEntity contactsEntity = new ContactListReturn.ContactsEntity();
                            contactsEntity.Mobile = contactsList.get(i).Mobile;
                            contactsEntity.Name = contactsList.get(i).Name;
                            contactsEntity.Plate = contactsList.get(i).Plate;
                            contactsEntity.Rate = contactsList.get(i).Rate;
                            contactsEntity.Invited = json.Contacts.get(i).Invited;
                            contactsEntity.Notified = json.Contacts.get(i).Notified;
                            contactsEntity.Registed = json.Contacts.get(i).Registed;
                            contactsEntity.Verified = json.Contacts.get(i).Verified;
                            contactsEntity.InContact = json.Contacts.get(i).InContact;
                            tmpList.add(contactsEntity);
                        }
                    }
                    setData();
                }
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
            }
        });
    }

    @OnClick(R.id.LinSearch)
    void setLinSearch(){
        Intent intent = new Intent(this, MailSearchActivity.class);
        intent.putExtra("action", "formMailList");
        startActivityForResult(intent, 1001);
    }

    boolean permission(){
        PackageManager pm = getPackageManager();
        boolean permission = (PackageManager.PERMISSION_GRANTED == pm.checkPermission("android.permission.READ_CONTACTS", "packageName"));
        return permission;
    }
    private void setData() {
        adapter = new PinyinAdapter(baseActivity, this, tmpList,true, false);
        elist.setAdapter(adapter);
        //展开所有
        for (int i = 0, length = adapter.getGroupCount(); i < length; i++) {
            elist.expandGroup(i);
        }
        if (dialog != null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
        //字母按键回调
        assort.setOnTouchAssortListener(new AssortView.OnTouchAssortListener() {

            View layoutView = LayoutInflater.from(MailFromListActivity.this)
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
                            180, 180,
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



    @Override
    protected void onResume() {
        super.onResume();
//        if (permission()) {
//            getPhoneContacts();
//        } else {
//            toastFast("木有获得通讯录访问权限");
//            LinContacts.setVisibility(View.GONE);
//            LinJurisdiction.setVisibility(View.VISIBLE);
//        }
    }

    public void getPhoneContacts(Context context, final List<ContactListReturn.ContactsEntity> list) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri =ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        AsyncQueryHandler(resolver,uri, list);

    }
    public void AsyncQueryHandler(ContentResolver resolver, Uri uri, final List<ContactListReturn.ContactsEntity> list){
        AsyncQueryHandler queryHandler = new AsyncQueryHandler(resolver) {
            @Override
            public void startQuery(int token, Object cookie, Uri uri, String[] projection, String selection, String[] selectionArgs, String orderBy) {
                super.startQuery(token, cookie, uri, projection, selection, selectionArgs, orderBy);
            }

            @Override
            protected void onQueryComplete(int token, Object cookie, Cursor cursor) {
                super.onQueryComplete(token, cookie, cursor);
                    if (cursor != null) {
                        if(token==1) {
                            while (cursor.moveToNext()) {

                                //得到手机号码
                                String phoneNumber = cursor.getString(PHONES_NUMBER_INDEX);
                                //当手机号码为空的或者为空字段 跳过当前循环
                                if (Guard.isNullOrEmpty(phoneNumber))
                                    continue;
                                //得到联系人名称
                                String contactName = cursor.getString(PHONES_DISPLAY_NAME_INDEX);
                                //得到联系人ID
//                                Long contactid = cursor.getLong(PHONES_CONTACT_ID_INDEX);
                                //得到联系人头像ID
//                                Long photoid = cursor.getLong(PHONES_PHOTO_ID_INDEX);
                                //得到联系人头像Bitamp
                                Bitmap contactPhoto = null;
                                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
//                            if (photoid > 0) {
//                        Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
//                        InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
//                        contactPhoto = BitmapFactory.decodeStream(input);
//                            } else {
//                        contactPhoto = BitmapFactory.decodeResource(context.getResources(), R.drawable.w_icon_tjzp);
//                            }
                                if (phoneNumber.replace(" ", "").replace("\n", "").trim().length() == 11 && phoneNumber.startsWith("1")) {
                                    ContactListReturn.ContactsEntity ContactListReturn = new ContactListReturn.ContactsEntity();
                                    ContactListReturn.Name = contactName;
                                    ContactListReturn.Mobile = phoneNumber.replace(" ", "").replace("\n", "").trim();
//                                    ContactListReturn.bitmap = contactPhoto;
                                    list.add(ContactListReturn);
                                }
                            }
                        }
                        cursor.close();
                        initData();
                    }
            }
        };

        queryHandler.startQuery(1, null, uri, PHONES_PROJECTION, null, null, null);
    }

    /**得到手机SIM卡联系人人信息**/
    public void getSIMContacts(Context context, List<ContactListReturn.ContactsEntity> list) {
        ContentResolver resolver = context.getContentResolver();
// 获取Sims卡联系人

        Uri uri = Uri.parse("content://icc/adn");
        AsyncQueryHandler(resolver, uri, list);
    }

}
