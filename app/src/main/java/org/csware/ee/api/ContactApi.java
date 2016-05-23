package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.AddressReturn;
import org.csware.ee.model.ContactListReturn;
import org.csware.ee.model.ContactsModel;
import org.csware.ee.model.Return;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.URLUtils;

import java.util.HashMap;

/**
 * Created by Yu on 2015/8/10.
 */
public class ContactApi {


    public static void list(final Activity activity, Context context, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.CONTACT.LIST);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    ContactListReturn o = GsonHelper.fromJson(res, ContactListReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void edit(final Activity activity, Context context,ActionType action,String phone, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.CONTACT.EDIT);
        final HttpAjax ajax = new HttpAjax(context);
        pms.addParam("action", action.toName());
        pms.addParam("mobile", phone);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    AddressReturn o = GsonHelper.fromJson(res, AddressReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void delete(final Activity activity, Context context,String id, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.CONTACT.EDIT);
        pms.addParam("action", ActionType.DELETE.toName());
        pms.addParam("id", id);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    AddressReturn o = GsonHelper.fromJson(res, AddressReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void compare(final Activity activity, Context context, String mobiles, final IJsonResult json){
        HttpParams pms = new HttpParams(API.CONTACT.CONTRAST);
        pms.addParam("mobiles",mobiles);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    ContactsModel o = GsonHelper.fromJson(res, ContactsModel.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void comparePost(Context context, String mobiles, final IJsonResult json){
        HashMap<String,String> hashMap = new HashMap<>();
        hashMap.put("mobiles",mobiles);
        final HttpParams pms = URLUtils.getMapValue(API.CONTACT.CONTRAST, hashMap);
//        hashMap = URLUtils.getEncodeHashMap(hashMap);
        final HttpAjax ajax = new HttpAjax(context);
        String url = pms.getQueryString(hashMap, API.CONTACT.CONTRAST);
        ajax.beginPostRequest(url, hashMap, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    ContactsModel o = GsonHelper.fromJson(res.trim(), ContactsModel.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void sendInvate(final Activity activity, Context context, HttpParams params, final IJsonResult json){
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, params, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    Return o = GsonHelper.fromJson(res, Return.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

}
