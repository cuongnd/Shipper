package org.csware.ee.Api;

import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.SignReturn;
import org.csware.ee.utils.GsonHelper;

/**
 * Created by Yu on 2015/8/11.
 */
public class QCloudApi {


    public static void sign(Context context, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.UTILITY.QQ.IMAGE.SIGN);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    SignReturn o = GsonHelper.fromJson(res, SignReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


}
