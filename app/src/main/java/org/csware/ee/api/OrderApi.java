package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.EvaluationModel;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.RegistModel;
import org.csware.ee.model.DeliverInfo;
import org.csware.ee.model.FlashInfo;
import org.csware.ee.model.GetIdReturn;
import org.csware.ee.model.InsuranceModel;
import org.csware.ee.model.OrderListReturn;
import org.csware.ee.model.OrderListType;
import org.csware.ee.model.OrderTrackReturn;
import org.csware.ee.model.PromoteActionType;
import org.csware.ee.model.Return;
import org.csware.ee.model.TabCountModel;
import org.csware.ee.model.TrackListOrder;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.URLUtils;

import java.util.HashMap;

/**
 * Created by Yu on 2015/8/11.
 */
public class OrderApi {

    public static void getId(final Activity activity, Context context, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.GETID);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    GetIdReturn o = GsonHelper.fromJson(res, GetIdReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void getCount(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    TabCountModel o = GsonHelper.fromJson(res, TabCountModel.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void getFlash(final Activity activity, final Context context,final IJsonResult json){
        String url = API.FLASH;
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, new HttpParams(url), new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result){
                    FlashInfo flashInfo = GsonHelper.fromJson(res,FlashInfo.class);
                    flashInfo.notify(json);
                }else {
                    json.error(null);
                }
            }
        });
    }

    public static void getInsurance(final Activity activity, final Context context, final IJsonResult json){
        String url = API.ORDER.INSURANCE;
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, new HttpParams(url), new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    InsuranceModel o = GsonHelper.fromJson(res, InsuranceModel.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void create(final Activity activity, final Context context, final long id, final DeliverInfo info, final IJsonResult json) {

        final HttpParams pms = new HttpParams(API.ORDER.CREATE);
        pms.addParam("id", id);
        pms.addParam("fromarea", info.fromId);
        pms.addParam("fromdetail", info.fromDetail);
        pms.addParam("fromtime", info.beginTime);
        pms.addParam("toarea", info.toId);
        pms.addParam("todetail", info.toDetail);
        pms.addParam("toname", info.name);
        pms.addParam("tonumber", info.phone);
        pms.addParam("totime", info.endTime);
        pms.addParam("goodstype", info.goodsStyle);
        pms.addParam("goodsunit", info.goodsUnit);
        pms.addParam("goodsamount", info.amount);
        pms.addParam("trucktype", info.truckStyle);
        pms.addParam("trucklength", info.truckLength);
        pms.addParam("pricecaltype", info.transStyle);
        String priceType = info.priceStyle;
        if (!Guard.isNullOrEmpty(priceType)&&priceType.equals("包车价")){
            priceType = "元/包车";
        }
        pms.addParam("pricetype", priceType);
        pms.addParam("price", info.price);
        pms.addParam("tax", info.invoiceStyle);
        pms.addParam("message", info.memo);
        pms.addParam("paypoint", info.payPoint);
        pms.addParam("paymethod", info.payMethod);
        pms.addParam("bearermobile", info.hackmanPhone);
        String insurance=info.insurance;
        if (Guard.isNullOrEmpty(insurance)) {
            insurance = "0";
        }
        pms.addParam("insurance", insurance);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Return o = GsonHelper.fromJson(res, Return.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
//        //先获取订单Id
//        getId(context, new IJsonResult<GetIdReturn>() {
//            @Override
//            public void ok(GetIdReturn order) {
//
//
//            }
//
//            @Override
//            public void error(Return json) {
//
//            }
//        });
    }
    public static void create(final Context context, final long id, final DeliverInfo info,final HashMap<String,String> map, String images, String action, final IJsonResult json) {

        HashMap<String,String> hashMap = null;
        if (action.equals("deliver")) {
            hashMap  = new HashMap<>();
            hashMap.put("toarea", info.toId+"");
            hashMap.put("todetail", info.toDetail+"");
            hashMap.put("toname", info.name.trim()+"");
            hashMap.put("tonumber", info.phone+"");
        }else {
            hashMap = map;
        }
        hashMap.put("id", id + "");
        hashMap.put("fromarea", info.fromId.trim()+"");
        hashMap.put("fromdetail", info.fromDetail.trim()+"");
        hashMap.put("fromtime", info.beginTime + "");
        String toTime = info.endTime+"";
        if (toTime.equals("0")){
            toTime = "";
        }
        hashMap.put("totime", toTime + "");
        hashMap.put("goodstype", info.goodsStyle.trim()+"");
        hashMap.put("goodsunit", info.goodsUnit.trim()+"");
        hashMap.put("goodsamount", info.amount.trim()+"");
        hashMap.put("trucktype", info.truckStyle.trim()+"");
        hashMap.put("trucklength", info.truckLength.trim()+"");
        hashMap.put("pricecaltype", info.transStyle.trim()+"");
        String priceType = info.priceStyle;
        if (!Guard.isNullOrEmpty(priceType)&&priceType.equals("包车价")){
            priceType = "元/包车";
        }
        if (Guard.isNullOrEmpty(priceType)){
            priceType = "";
        }
        hashMap.put("pricetype", priceType);
        String price  = info.price+"";
        if (Guard.isNullOrEmpty(price)){
            price = "";
        }
        hashMap.put("price", price);
        String tax  = info.invoiceStyle;
        if (Guard.isNullOrEmpty(tax)){
            tax = "";
        }
        hashMap.put("tax", tax);
        hashMap.put("message", info.memo.trim()+"");
        hashMap.put("images", images.trim()+"");
        hashMap.put("paypoint", 0 + "");
        hashMap.put("paymethod", 2 + "");
//        hashMap.put("paypoint", info.payPoint + "");
//        hashMap.put("paymethod", info.payMethod + "");
        hashMap.put("bearermobile", info.hackmanPhone.trim()+"");
        hashMap.put("bearerids", info.bearerids + "");
        String insurance=info.insurance;
        if (Guard.isNullOrEmpty(insurance)) {
            insurance = "0";
        }
        hashMap.put("insurance", insurance);
        Tracer.e("OrderApi",""+hashMap.toString());
        final HttpParams pms = URLUtils.getMapValue(API.ORDER.CREATE,hashMap);
//        hashMap = URLUtils.getEncodeHashMap(hashMap);
        final HttpAjax ajax = new HttpAjax(context);
        String url = pms.getQueryString(hashMap, API.ORDER.CREATE);
        ajax.beginPostRequest(url, hashMap, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Return o = GsonHelper.fromJson(res.trim(), Return.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
//        //先获取订单Id
//        getId(context, new IJsonResult<GetIdReturn>() {
//            @Override
//            public void ok(GetIdReturn order) {
//
//
//            }
//
//            @Override
//            public void error(Return json) {
//
//            }
//        });
    }

    public static void detail(final Activity activity, Context context, long orderId, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.DETAIL);
        pms.addParam("id", orderId);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    OrderDetailChangeReturn o = GsonHelper.fromJson(res, OrderDetailChangeReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void list(final Activity activity, Context context, OrderListType listType, int pageSize, int pageIndex, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.LIST);
        pms.addParam("type", listType.toName());
        pms.addParam("pagesize", pageSize);
        pms.addParam("page", pageIndex);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    OrderListReturn o = GsonHelper.fromJson(res, OrderListReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }
    public static void tracklist(final Activity activity, Context context, OrderListType listType, int pageSize, int pageIndex, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.TRACK_LIST);
        pms.addParam("type", listType.toName());
        pms.addParam("pagesize", pageSize);
        pms.addParam("page", pageIndex);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    TrackListOrder o = GsonHelper.fromJson(res, TrackListOrder.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }
    public static void delorder(final Activity activity, Context context, int orderId, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.DELORDER);
        pms.addParam("id", orderId);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    RegistModel o = GsonHelper.fromJson(res, RegistModel.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void promote(final Activity activity, Context context, PromoteActionType actionType, long orderId, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.PROMOTE);
        pms.addParam("action", actionType.toName());
        pms.addParam("id", orderId);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Return o = GsonHelper.fromJson(res, Return.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void track(final Activity activity, Context context, long orderId, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.TRACK);
        pms.addParam("id", orderId);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    OrderTrackReturn o = GsonHelper.fromJson(res, OrderTrackReturn.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void rate(final Activity activity, Context context, long orderId, double rate, String comt, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.ORDER.RATE);
        pms.addParam("id", orderId);
        pms.addParam("star", (int) rate);
        pms.addParam("message", comt);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    Return o = GsonHelper.fromJson(res, Return.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void rateRecord(final Activity activity, Context context, HttpParams params, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, params, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    EvaluationModel o = GsonHelper.fromJson(res, EvaluationModel.class);
                    o.notify(json);
                } else {
                    json.error(null);
                }
            }
        });
    }

}
