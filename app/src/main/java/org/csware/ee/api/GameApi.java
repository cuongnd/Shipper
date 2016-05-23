package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.GameInfo;
import org.csware.ee.model.Return;
import org.csware.ee.model.ScoreInfo;
import org.csware.ee.model.WinnerInfo;
import org.csware.ee.utils.GsonHelper;

/**
 * Created by Administrator on 2015/11/26.
 */
public class GameApi {

    public static void confirm(final Activity activity, Context context, HttpParams pms, final IJsonResult json){
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    WinnerInfo o = GsonHelper.fromJson(res, WinnerInfo.class);
                    o.notify(json);
                    if(o.isError()){
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void score(final Activity activity, Context context,HttpParams pms, final IJsonResult json){
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    ScoreInfo o = GsonHelper.fromJson(res, ScoreInfo.class);
                    o.notify(json);
                    if(o.isError()){
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void open(final Activity activity, Context context,HttpParams pms, final IJsonResult json){
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    GameInfo o = GsonHelper.fromJson(res, GameInfo.class);
                    o.notify(json);
                    if(o.isError()){
                        //TODO:tip the json is error
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

}
