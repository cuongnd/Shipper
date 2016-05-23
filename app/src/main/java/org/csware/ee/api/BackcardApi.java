package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.AccountReturn;
import org.csware.ee.model.ActionType;
import org.csware.ee.model.BankcardResult;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.CouponListInfo;
import org.csware.ee.model.DrawListModel;
import org.csware.ee.model.GetIdReturn;
import org.csware.ee.model.OilRecordBackDetailInfo;
import org.csware.ee.model.OilRecordDetailInfo;
import org.csware.ee.model.PayeeScanModel;
import org.csware.ee.model.RecordDetailInfo;
import org.csware.ee.model.RecordInfo;
import org.csware.ee.model.Return;
import org.csware.ee.model.WeiXinPayResultInfo;
import org.csware.ee.utils.GsonHelper;

/**
 * Created by Yu on 2015/8/11.
 */
public class BackcardApi {

    public static void account(final Activity activity, Context context, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNT);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    AccountReturn o = GsonHelper.fromJson(res, AccountReturn.class);
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

    public static void drawlist(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    DrawListModel o = GsonHelper.fromJson(res, DrawListModel.class);
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

    public static void accountEdit(final Activity activity, Context context, String password, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
        pms.addParam("password", password);
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

    public static void accountBank(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
//        final HttpParams pms = new HttpParams(API.BANKCARD.ACCOUNTEDIT);
//        pms.addParam("password", password);
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

    public static void list(final Activity activity, Context context, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.BANKCARD.LIST);
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    BankcardResult o = GsonHelper.fromJson(res, BankcardResult.class);
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

    public static void edit(final Activity activity, Context context, ActionType act, BankcardResult.BankCardsEntity bankCard, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.BANKCARD.EDIT);
        pms.addParam("action", act.toName());
        pms.addParam("name", bankCard.Name);
        pms.addParam("bankname", bankCard.BankName);
        pms.addParam("bankaddress", bankCard.BankAddress);
        pms.addParam("cardno", bankCard.CardNo);
        pms.addParam("id", bankCard.Id);
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

    public static void delete(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {

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

    public static void confirm(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
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

    public static void bindStatus(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    BindStatusInfo o = GsonHelper.fromJson(res, BindStatusInfo.class);
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

    public static void bindcard(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
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

    public static void scanpayInfo(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    PayeeScanModel o = GsonHelper.fromJson(res, PayeeScanModel.class);
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

    public static void payBank(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
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

    public static void getPayId(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    GetIdReturn o = GsonHelper.fromJson(res, GetIdReturn.class);
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

    public static void getWeiXinPayId(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    WeiXinPayResultInfo o = GsonHelper.fromJson(res, WeiXinPayResultInfo.class);
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

    public static void getRecordApi(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    RecordInfo o = GsonHelper.fromJson(res, RecordInfo.class);
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

    public static void getDetailApi(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    RecordDetailInfo o = GsonHelper.fromJson(res, RecordDetailInfo.class);
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

    public static void getOilRechargeDetailApi(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    OilRecordDetailInfo o = GsonHelper.fromJson(res, OilRecordDetailInfo.class);
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

    public static void getOilDetailApi(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    try {
                        //成功
                        OilRecordBackDetailInfo o = GsonHelper.fromJson(res, OilRecordBackDetailInfo.class);
                        o.notify(json);
                        if (o.isError()) {
                            //TODO:tip the json is error
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }

                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void getCouponListApi(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    CouponListInfo o = GsonHelper.fromJson(res, CouponListInfo.class);
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

    public static void getCouponOilApi(final Activity activity, Context context, HttpParams pms, final IJsonResult json) {
        final HttpAjax ajax = new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    //成功
                    CouponListInfo o = GsonHelper.fromJson(res, CouponListInfo.class);
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

    /**
     * 提现结果，阿里账号与银行账号二选一
     *
     * @param context
     * @param password
     * @param aliAccount
     * @param aliRealName
     * @param bankid
     * @param money
     * @param json
     */
    public static void draw(final Activity activity, Context context, String password, String aliAccount, String aliRealName, String bankid, String money, final IJsonResult json) {
        final HttpParams pms = new HttpParams(API.BANKCARD.DRAW);
        pms.addParam("password", password);
        pms.addParam("alipayaccount", aliAccount);
        pms.addParam("alipayname", aliRealName);
        pms.addParam("amount", money);
        pms.addParam("bankcardid", bankid);
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
