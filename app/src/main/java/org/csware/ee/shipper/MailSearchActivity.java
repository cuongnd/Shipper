package org.csware.ee.shipper;

import android.app.ProgressDialog;
import android.content.AsyncQueryHandler;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.api.ContactApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.Return;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.pinyin.AssortPinyinList;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ScrollViewForListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/12/10.
 */
public class MailSearchActivity extends ActivityBase {
    @InjectView(R.id.edtSearch)
    EditText edtSearch;
    @InjectView(R.id.LinSearch)
    LinearLayout LinSearch;
    @InjectView(R.id.btnCancel)
    TextView btnCancel;
    @InjectView(R.id.listVSearch)
    ScrollViewForListView listVSearch;
    List<ContactListReturn.ContactsEntity> models = new ArrayList<>();
    List<ContactListReturn.ContactsEntity> modelList = new ArrayList<>();
    String action = "";
    CommonAdapter<ContactListReturn.ContactsEntity> adapter;
    Handler myhandler = new Handler();
    boolean isFromMail = false;
    boolean isShow = false;
    AssortPinyinList assort = new AssortPinyinList();
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    ContactListReturn ContactListReturn;
    DbCache dbCache;
    List<ContactListReturn.ContactsEntity> putList;
    ProgressDialog dialog;
    public boolean isContactSame(ContactListReturn.ContactsEntity entity,List<ContactListReturn.ContactsEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).Name.equals(entity.Name) || list.get(i).Mobile.equals(entity.Mobile)) {
//                car_diver_list.set(i,carDriverInfo);
                return true;
            }
        }
        return false;
    }
    public boolean isSelect(ContactListReturn.ContactsEntity entity,List<ContactListReturn.ContactsEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).Name.equals(entity.Name) || list.get(i).Mobile.equals(entity.Mobile)) {
                return list.get(i).isSelect;
            }
        }
        return false;
    }
    public int pos(ContactListReturn.ContactsEntity entity,List<ContactListReturn.ContactsEntity> list) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).Name.equals(entity.Name) || list.get(i).Mobile.equals(entity.Mobile)) {
                return i;
            }
        }
        return -1;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.inject(this);
        action = getIntent().getStringExtra("action");
        isShow = getIntent().getBooleanExtra("isShow", false);
        dbCache = new DbCache(this);
        ContactListReturn = dbCache.GetObject(ContactListReturn.class);
        if (Guard.isNull(ContactListReturn)){
            ContactListReturn = new ContactListReturn();
            ContactListReturn.Contacts = new ArrayList<>();
        }
        putList = ContactListReturn.Contacts;
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        if (Guard.isNullOrEmpty(action)){
            action = "";
        }
        if (action.equals("formContacts")) {
//            models = GsonHelper.fromJson(json, ContactListReturn.class).Contacts;
            setData();
            if (isShow){
                btnConfirm.setVisibility(View.VISIBLE);
            }
            isFromMail = false;
        } else {
            isFromMail = true;
            btnConfirm.setVisibility(View.GONE);
//            ContactListReturn listReturn = dbCache.GetObject("ContactList",ContactListReturn.class);
//            if (Guard.isNull(listReturn)){
//                listReturn = new ContactListReturn();
//            }
//            modelList = listReturn.Contacts;
//            if (!Guard.isNull(modelList)) {
//                if (modelList.size()>0) {
//                    models.addAll(modelList);
//                }else {
                    getPhoneContacts(this, modelList);
                    int absent = TelephonyManager.SIM_STATE_ABSENT;
                    if (1 == absent) {
                        //Log.d(TAG,"请确认sim卡是否插入或者sim卡暂时不可用！");
                    } else {
//                getSIMContacts(this, contactsList);
                        getSIMContacts(this, modelList);
                    }

            Tracer.e("MailSearch",modelList.size()+"");
//                }
//            }else {
//                modelList = new ArrayList<>();
//            }
        }


        adapter = new CommonAdapter<ContactListReturn.ContactsEntity>(this, modelList, R.layout.list_contact_item) {
            @Override
            public void convert(final ViewHolder helper, final ContactListReturn.ContactsEntity item, final int position) {
                if (isFromMail) {
                    helper.getView(R.id.LinContacts).setVisibility(View.GONE);
                    helper.getView(R.id.LinMailList).setVisibility(View.VISIBLE);
                    helper.setText(R.id.txtMailName, item.Name);
                    helper.setText(R.id.txtMailPhone, item.Mobile);
//                    if (!Guard.isNull(item.bitmap)) {
//                        helper.setImageBitmap(R.id.imgMailHead, item.bitmap);
//                    }
                    if (!Guard.isNullOrEmpty(item.Avatar)){
                        QCloudService.asyncDisplayImage(baseActivity,item.Avatar, (ImageView) helper.getView(R.id.imgGameHead));
                    }else {
                        helper.setImageResource(R.id.imgGameHead,R.drawable.w_icon_tjzp);
                    }
                    helper.getView(R.id.txtMailBtn).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //发出请求
                        }
                    });
                } else {

                    helper.getView(R.id.LinContacts).setVisibility(View.VISIBLE);
                    helper.getView(R.id.LinMailList).setVisibility(View.GONE);
                    helper.setText(R.id.txtConComp, item.Name);
                    helper.setText(R.id.txtPlate, item.Mobile);
                    helper.getView(R.id.imgPhoneCall).setVisibility(View.GONE);
                    helper.getView(R.id.LinRatItem).setVisibility(View.GONE);

                    if (isShow){
                        View.OnClickListener selectListener = new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                if (putList==null){
                                    putList = new ArrayList<>();
                                }
                                if (putList.size()>0){
                                    if (isContactSame(item,putList)){
                                        if (isSelect(item,putList)){
                                            helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_unselected);
                                            item.isSelect = false;
                                            int pos = pos(item,putList);
                                            if (pos>=0) {
                                                putList.remove(pos);
                                            }
                                        }else {
                                            helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_selected);
                                            item.isSelect = true;
                                            putList.add(item);
                                        }

                                    }else {
                                        helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_selected);
                                        item.isSelect = true;
                                        putList.add(item);
                                    }
                                }else {
                                    helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_selected);
                                    item.isSelect = true;
                                    putList.add(item);
                                }
                                ContactListReturn.Contacts = putList;
                            }
                        };
                        helper.getView(R.id.imgSelectContact).setVisibility(View.VISIBLE);
                        helper.getView(R.id.LinSelect).setOnClickListener(selectListener);
                        helper.getView(R.id.imgSelectContact).setOnClickListener(selectListener);
                        helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_unselected);
                        if (ContactListReturn.Contacts == null)
                        {
                            ContactListReturn.Contacts = new ArrayList<>();
                        }
                        if (isContactSame(item,ContactListReturn.Contacts)){
                            if (isSelect(item,ContactListReturn.Contacts)){

                                helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_selected);
                            }else {
                                helper.setImageResource(R.id.imgSelectContact, R.mipmap.lxr_icon_unselected);
                            }
                        }
//                    if (childList.get(group).get(child).isSelect){
//                        finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_selected);
//                    }else {
//                        finalViewHolder.imgSelectContact.setImageResource(R.mipmap.lxr_icon_unselected);
//                    }

                    }else {
                        helper.getView(R.id.imgSelectContact).setVisibility(View.GONE);
                    }

                }
            }
        };

        listVSearch.setAdapter(adapter);
         if (dialog != null) {
             if (dialog.isShowing()) {
                 dialog.dismiss();
             }
         }
        listVSearch.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (!Guard.isNullOrEmpty(modelList.get(position).Mobile)) {
                    Intent nIntent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + modelList.get(position).Mobile));
                    startActivity(nIntent);
                }
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                // TODO Auto-generated method stub
                /**这是文本框改变之后 会执行的动作
                 * 因为我们要做的就是，在文本框改变的同时，我们的listview的数据也进行相应的变动，并且如一的显示在界面上。
                 * 所以这里我们就需要加上数据的修改的动作了。
                 */

                myhandler.post(eChanged);
            }
        });

    }

    public void getPhoneContacts(Context context, final List<ContactListReturn.ContactsEntity> list) {
        ContentResolver resolver = context.getContentResolver();
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
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
                            String phoneNumber = cursor.getString(MailFromListActivity.PHONES_NUMBER_INDEX);
                            //当手机号码为空的或者为空字段 跳过当前循环
                            if (Guard.isNullOrEmpty(phoneNumber))
                                continue;
                            //得到联系人名称
                            String contactName = cursor.getString(MailFromListActivity.PHONES_DISPLAY_NAME_INDEX);
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
                    Tracer.e("MailSearch","size:"+list.size());
                    models.addAll(list);

                }
            }
        };

        queryHandler.startQuery(1, null, uri, MailFromListActivity.PHONES_PROJECTION, null, null, null);
    }

    /**得到手机SIM卡联系人人信息**/
    public void getSIMContacts(Context context, List<ContactListReturn.ContactsEntity> list) {
        ContentResolver resolver = context.getContentResolver();
// 获取Sims卡联系人

        Uri uri = Uri.parse("content://icc/adn");
        AsyncQueryHandler(resolver, uri, list);
    }

    void setData(){
        ContactApi.list(baseActivity, baseActivity, new IJsonResult<ContactListReturn>() {
            @Override
            public void ok(ContactListReturn json) {
                if (json.Contacts != null) {
                    models.clear();
                    models.addAll(json.Contacts);
                    getmData(modelList);
                }
                adapter.notifyDataSetChanged();
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                Tracer.e("MailSearch", models.size() + " " + isShow + " ");
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

    Runnable eChanged = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            String data = edtSearch.getText().toString().toUpperCase();

            modelList.clear();//先要清空，不然会叠加

            getmDataSub(modelList, data);//获取更新数据
            Tracer.e("MailSearch","begin search");
            adapter.notifyDataSetChanged();//更新

        }
    };

    private void getmDataSub(List<ContactListReturn.ContactsEntity> mDataSubs, String data) {
        int length = models.size();
        for (int i = 0; i < length; ++i) {
            String pName = assort.getPinYinHeadChar(models.get(i).Name) + "";
            String fullName =assort.converterToSpell(models.get(i).Name)+"";
            if (models.get(i).Name.contains(data) ||
                    models.get(i).Mobile.contains(data) ||
                    pName.contains(data.toUpperCase()) ||
                    fullName.contains(data.toUpperCase())) {
                mDataSubs.add(models.get(i));
            }
        }
    }

    @OnClick(R.id.btnCancel)
    void setBtnCancel() {
        this.finish();
    }
    @OnClick(R.id.btnConfirm)
    void setBtnConfirm() {
        if (!Guard.isNull(ContactListReturn)) {
            if (!Guard.isNull(ContactListReturn.Contacts)) {
                if (ContactListReturn.Contacts.size() > 0) {
                    dbCache.save(ContactListReturn);
                    finish();

                    if (action.equals("formContacts") && isShow) {
                        if (MailListActivity.instance != null) {
                            MailListActivity.instance.finish();
                        }
                    }
                } else {
                    toastFast("您还没有选择司机哦亲！");
                }
            }
        }
    }
    private void getmData(List<ContactListReturn.ContactsEntity> mDatas) {
        mDatas.addAll(models);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!Guard.isNull(dbCache)){
            ContactListReturn contactListReturn = new ContactListReturn();
            dbCache.save("ContactList",contactListReturn);
        }
    }
}
