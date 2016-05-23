package org.csware.ee.api;

import android.app.Activity;
import android.content.Context;

import org.csware.ee.component.HttpAjax;
import org.csware.ee.component.IAjaxResult;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.KeyCheckResult;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.SigninResult;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.Md5Encryption;

/**
 * 用户相关API
 * Created by Yu on 2015/8/7.
 */
public class UserApi {


    public static void checkKey(Context context, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.KEY_CHECK);
        new HttpAjax(context).beginRequest(pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        KeyCheckResult o = GsonHelper.fromJson(res, KeyCheckResult.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void signin(Context context, String username, String pwd, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.SIGNIN);
        pms.addParam("mobile", username);
        pms.addParam("password", Md5Encryption.md5(username + pwd));
        new HttpAjax(context).beginRequest(pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        SigninResult o = GsonHelper.fromJson(res, SigninResult.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void signupSMS(final Activity activity, Context context, String mobile,  final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.SIGNUP_SMS);
        pms.addParam("mobile", mobile);
        new HttpAjax(context).beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{") && res.endsWith("}")) {
                        Return o = GsonHelper.fromJson(res, Return.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void signup(Context context,  String username, String pwd, String code, String invitation, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.SIGNUP);
        //pms.addParam("device", AppStatus.getDeviceId());
        pms.addParam("mobile", username);
        pms.addParam("verifycode", code);
        pms.addParam("invitation", invitation);
        pms.addParam("password", Md5Encryption.md5(username + pwd));
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{") && res.endsWith("}")) {
                        SigninResult o = GsonHelper.fromJson(res, SigninResult.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void resetPasswordSMS(final Activity activity, Context context, String mobile,  final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.RESET_PASSWORD_SMS);
        pms.addParam("mobile", mobile);
        new HttpAjax(context).beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{") && res.endsWith("}")) {
                        Return o = GsonHelper.fromJson(res, Return.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void resetPassword(final Activity activity, Context context,  String username, String pwd,String code, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.RESET_PASSWORD);
        pms.addParam("mobile", username);
        pms.addParam("verifycode", code);
        pms.addParam("password", Md5Encryption.md5(username + pwd));
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{") && res.endsWith("}")) {
                        SigninResult o = GsonHelper.fromJson(res, SigninResult.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }


    public static void notifyToken(Context context,  String token,  final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.NOTIFY_TOKEN);
        pms.addParam("notifytoken", token);
        pms.addParam("platform","Android");
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        Return o = GsonHelper.fromJson(res, Return.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

//    public static void info(Context context,    final IJsonResult json) {
//        HttpParams pms = new HttpParams(API.INFO);
//        final HttpAjax ajax =    new HttpAjax(context);
//        ajax.beginRequest(pms, new IAjaxResult() {
//            @Override
//            public void notify(boolean result, String res) {
//                if (result) {
//                    if (res.startsWith("{")&&res.endsWith("}")) {
//                        MeReturn o = GsonHelper.fromJson(res, MeReturn.class);
//                        o.notify(json);
//                    }
//                } else {
//                    json.error(null);
//                }
//            }
//        });
//    }

    public static void info(final Activity activity, Context context, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.INFO);
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(activity,  pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        MeReturn o = GsonHelper.fromJson(res, MeReturn.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }

    public static void edit(final Activity activity, Context context,  String avatar,  final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.EDIT);
        pms.addParam("avatar", avatar);
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        Return o = GsonHelper.fromJson(res, Return.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }



    public static void verify(final Activity activity, Context context,  String name,String photoUrl,String identity,String backphoto,  final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.VERIFY);
        pms.addParam("name", name);
        pms.addParam("identity", identity.toUpperCase());
        pms.addParam("identitybackphoto", backphoto);
        pms.addParam("identityphoto", photoUrl);
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        Return o = GsonHelper.fromJson(res, Return.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }
    public static void enterpriseVerify(final Activity activity, Context context,  String legalperson,String legalpersonid,String legalpersonfront,String legalpersonback, String companyname, String businesslicence, final IJsonResult json) {
        HttpParams pms = new HttpParams(API.USER.ENERPRISE_VERIFY);
        pms.addParam("legalperson", legalperson);
        pms.addParam("legalpersonid", legalpersonid.toUpperCase());
        pms.addParam("legalpersonfront", legalpersonfront);
        pms.addParam("legalpersonback", legalpersonback);
        pms.addParam("companyname", companyname);
        pms.addParam("businesslicence", businesslicence);
        final HttpAjax ajax =    new HttpAjax(context);
        ajax.beginRequest(activity, pms, new IAjaxResult() {
            @Override
            public void notify(boolean result, String res) {
                if (result) {
                    if (res.startsWith("{")&&res.endsWith("}")) {
                        Return o = GsonHelper.fromJson(res, Return.class);
                        o.notify(json);
                    }
                } else {
                    json.error(null);
                }
            }
        });
    }



}
