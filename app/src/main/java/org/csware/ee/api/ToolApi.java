package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AddressModel;
import org.csware.ee.model.FreightResult;
import org.csware.ee.model.KeyCheckResult;
import org.csware.ee.model.Return;
import org.csware.ee.model.UpdataInfo;
import org.csware.ee.utils.GsonHelper;

/**
 * Created by Yu on 2015/8/11.
 */
public class ToolApi {


    public static void freightCalculation(final Activity activity, Context context, String fromCode, String toCode, String ton, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.TOOL.FREIGHT_CALCULATION);
        pms.addParam("from", fromCode);
        pms.addParam("to", toCode);
        pms.addParam("amount", ton);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    FreightResult o = GsonHelper.fromJson(res, FreightResult.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void update(final Activity activity, Context context, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.UTILITY.UPDATER);
//        pms.addParam("bidid", "");
//        pms.addParam("price", "");
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    UpdataInfo o = GsonHelper.fromJson(res, UpdataInfo.class);
                    o.notify(json);
                    if (o.isError()) {
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void feedback(final Activity activity, Context context, String message, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.FEEDBACK);
        pms.addParam("message", message);
//        pms.addParam("price", "");
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Return r = GsonHelper.fromJson(res,Return.class);
                    r.notify(json);
                    if (r.isError()) {
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void getTime(final Activity activity, Context context, final IJsonResult json){
        final HttpParams pms = new HttpParams(API.USER.SERVICETIME);
//        pms.addParam("price", "");
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    KeyCheckResult r = GsonHelper.fromJson(res, KeyCheckResult.class);
                    r.notify(json);
                    if (r.isError()) {
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void getVersion(Context context, HttpParams params,  final IJsonResult json){
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(params, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    AddressModel r = GsonHelper.fromJson(res, AddressModel.class);
                    r.notify(json);
                    if (r.isError()) {
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

}
