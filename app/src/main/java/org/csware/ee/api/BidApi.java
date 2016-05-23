package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.Return;
import org.csware.ee.utils.GsonHelper;

/**
 * Created by Yu on 2015/8/11.
 */
public class BidApi {


    public static void confirm(final Activity activity, Context context, int bidId, double price, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.BID.CONFIRM);
        pms.addParam("bidid", bidId);
        pms.addParam("price", price);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Return o = GsonHelper.fromJson(res, Return.class);
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
}
