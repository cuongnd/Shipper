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
import org.csware.ee.model.Return;
import org.csware.ee.utils.GsonHelper;

/**
 * 地址API
 * Created by Yu on 2015/8/7.
 */
public class AddressApi {


    /**
     * @param context
     * @param type    1:源,2:目的,3:both
     * @param json
     */
    public static void list(final Activity activity, Context context, int type, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.ADDRESS.LIST);
        pms.addParam("type", type);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    AddressReturn o = GsonHelper.fromJson(res, AddressReturn.class);
                    o.notify(json);
//                    if (o.getOk()) {
//                        json.ok(o);
//                    } else {
//                        //TODO:send error toast;
//                        json.error(o);
//                    }
                } else {
                    json.error(null);
                }
            }
        });
    }


    /**
     * @param context
     * @param action
     * @param areaCode 二级或三级行政编码
     * @param detail   不含省市县的地址信息
     * @param type     1:源,2:目的,3:both
     * @param json
     */
    public static void edit(final Activity activity, Context context, ActionType action, String areaCode, String detail, int type, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.ADDRESS.EDIT);
        pms.addParam("action", action.toName());
        pms.addParam("area", areaCode);
        pms.addParam("detail", detail);
        pms.addParam("type", type);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
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


    public static void edit(final Activity activity, Context context, int id, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.ADDRESS.EDIT);
        pms.addParam("action", ActionType.DELETE.toName());
        pms.addParam("id", id);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
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
