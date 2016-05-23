package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.JsResult;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.csware.ee.Guard;
import org.csware.ee.api.BackcardApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.BindStatusInfo;
import org.csware.ee.model.Code;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.OilCardInfo;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.model.Shipper;
import org.csware.ee.service.BankInfoService;
import org.csware.ee.utils.ChinaAreaHelper;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.ProgressWebView;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/12.
 */
public class WebViewOilActivity extends ActivityBase {

    private String TAG = "WebViewOil";
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.webview)
    ProgressWebView webview;
    private String url;
    public static Activity instance;
    BindStatusInfo bindStatusInfo;
    ProgressDialog dialog;
    Shipper shipper;
    ChinaAreaHelper areaHelper;
    int status;
    long orderId;
    double dealPrice;
    String subject;
    OrderDetailChangeReturn.OrderEntity orderEntity;
    OrderDetailChangeReturn.BidsEntity bidsEntity;
    DbCache _dbCache;
    MeReturn userInfo;
    List<String> images = new ArrayList<>();
    List<String> rptImages = new ArrayList<>();
    String rptJson = "Oil";
    String money;
    private String createTime, cardNumber, orderNO, oilTitle="";
    final Handler myHandler = new Handler();
    OilCardInfo info = new OilCardInfo();
    boolean isLoadUrl = false;

//    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        ButterKnife.inject(this);
        instance = this;
        init();

        topBar.setBackTitle("");
        topBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                if (webview.canGoBack()){
                    webview.goBack();
                }else {
                    finish();
                }
//                webview.loadUrl("javascript:jsBack()");

            }
        });

        JavaScriptInterface myJavaScriptInterface = new JavaScriptInterface(this);
        goBackInterface goBackInterface = new goBackInterface(this);
        clearCache clearCache = new clearCache(this);
        webview.addJavascriptInterface(goBackInterface,"goBack");
        webview.addJavascriptInterface(clearCache,"clearAndroid");

        // ~~~ 获取参数
        url = getIntent().getStringExtra("url");
        if (Guard.isNull(url)){
            url = "";
        }
        WebSettings webSettings = webview.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        if (url.contains("Flash")) {
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
        }
        Tracer.e("WebView", webview.getScale() + "" + Tools.getScreenWidth(this)+" IP:"+ ClientStatus.getLocalIpAddress());
//        int screenDensity = getResources().getDisplayMetrics().densityDpi ;
//        WebSettings.ZoomDensity zoomDensity = WebSettings.ZoomDensity.MEDIUM ;
//        switch (screenDensity){
//            case DisplayMetrics.DENSITY_LOW :
//                zoomDensity = WebSettings.ZoomDensity.CLOSE;
//                break;
//            case DisplayMetrics.DENSITY_MEDIUM:
//                zoomDensity = WebSettings.ZoomDensity.MEDIUM;
//                break;
//            case DisplayMetrics.DENSITY_HIGH:
//                zoomDensity = WebSettings.ZoomDensity.FAR;
//                break ;
//        }
//        webSettings.setDefaultZoom(zoomDensity);
//        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
//        if (url.contains("RouletGame")) {
//            webview.setInitialScale(30);
//        }
//        WebView webView = new WebView(this);
//        WebChromeClient  webChromeClient = new WebChromeClient(){
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//            }
//        };
        // 设置setWebChromeClient对象
//        ProgressWebView.WebChromeClient wvcc = new ProgressWebView.WebChromeClient() {
//            @Override
//            public void onReceivedTitle(WebView view, String title) {
//                super.onReceivedTitle(view, title);
//                topBar.setTitle(title);
//                oilTitle = title;
//                Tracer.e(TAG,oilTitle);
//            }
//        };
//        webview.setWebChromeClient(wvcc);
        webview.setWebViewClient(new WebViewClient(){
            public boolean shouldOverrideUrlLoading(WebView  view, String url) {
                Tracer.e("WebViewOil",url);
                Tracer.e("WebViewOil","oilTitle:"+oilTitle);
                view.loadUrl(url);
                if (url.startsWith("http")||url.startsWith("https")){
                    isLoadUrl = true;
                }else {
                    isLoadUrl = false;
                }
                topBar.setBackClickListener(new TopActionBar.BackClickListener() {
                    @Override
                    public void backClick() {
                        if (webview.canGoBack()){
                            if (isLoadUrl) {
                                topBar.setTitle("加油卡充值");
                            }
                            webview.goBack();
                        }else {
                            finish();
                        }

                    }
                });
                return true;
            }
        });
        webview.addJavascriptInterface(myJavaScriptInterface, "android");
//        String htmlText = getFromAssets("");tet.html
//        webview.addJavascriptInterface(new Object(){
//            @
//            public String activityListTest() {//将被js调用
//                myHandler.post(new Runnable() {
//                    public void run() {
//
//                    }
//                });
////                return fileFullName;
//            }
//        }, "demo");
//        webview.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                if (url != null && url.startsWith("http://"))
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            }
//        });
        webview.loadUrl(url);
//        webview.loadData(htmlText, "text/html", "utf-8");

    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences preferences = getSharedPreferences("RefreshOil", MODE_PRIVATE);
        boolean isRefresh = preferences.getBoolean("isRefresh", false);
        if (isRefresh) {
            if (webview != null && !Guard.isNullOrEmpty(url)) {
//                webview.loadUrl(url);
                if(webview.canGoBack()){
                    webview.goBack();
                }
            }
            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("isRefresh",false).clear().commit();
        }

        ProgressWebView.WebChromeClient wvcc = new ProgressWebView.WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                topBar.setTitle(title);
                oilTitle = title;
                Tracer.e(TAG,"onResume"+title);
            }
        };
        webview.setWebChromeClient(wvcc);
//        webview.loadUrl(url);

    }

    public class JavaScriptInterface {
        Context mContext;

        JavaScriptInterface(Context c) {
            mContext = c;
        }

        @JavascriptInterface
        public void activityListTest(String toast) {
            final String msgeToast = toast;
            myHandler.post(new Runnable() {
                @Override
                public void run() {

                    //判断是否绑卡进行跳转
                    try {
                        JSONObject jsonObject = new JSONObject(msgeToast);
                        info.setMoney(jsonObject.getString("money"));
                        info.setCardNumber(jsonObject.getString("cardNumber"));
                        info.setOrderNO(jsonObject.getString("orderNO"));
                        info.setCreateTime(jsonObject.getString("createTime"));
                        money = info.getMoney();
                        cardNumber = info.getCardNumber();
                        orderNO = info.getOrderNO();
                        createTime = info.getCreateTime();
                        Tracer.e("WebViewOil", msgeToast);
                        _dbCache.save(info);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    getBindStatus();
                }
            });

        }
    }

    public class goBackInterface{
        Context mContext;
        goBackInterface(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void androidCloseWebView(final String toast) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    switch (toast) {
                        case "0":
                            if (webview.canGoBack()) {
                                webview.goBack();
                            }else {
                                finish();
                            }
                            break;
                        case "1":
                            webview.loadUrl(url);
                            webview.clearCache(true);
                            webview.clearHistory();
                            break;
                        case "2":
                            finish();
                            break;
                        default:
                            break;
                    }
//                    toastSlow(toast);
                }
            });
        }
    }

    public class clearCache{
        Context mContext;
        clearCache(Context c) {
            mContext = c;
        }
        @JavascriptInterface
        public void clearCache(final String toast) {
            myHandler.post(new Runnable() {
                @Override
                public void run() {
                    webview.clearHistory();
                    webview.clearCache(true);
                }
            });
        }
    }

    private void init() {
        _dbCache = new DbCache(baseActivity);
        shipper = _dbCache.GetObject(Shipper.class);
        bindStatusInfo = _dbCache.GetObject(BindStatusInfo.class);
        if (Guard.isNull(bindStatusInfo)) {
            bindStatusInfo = new BindStatusInfo();
        }
    }

    void getBindStatus() {
        dialog = Tools.getDialog(baseActivity);
        dialog.setCanceledOnTouchOutside(false);
        //TODO:获取绑卡状态
        HttpParams pms = new HttpParams(API.BANKCARD.BINDER);
        BackcardApi.bindStatus(baseActivity, this, pms, new IJsonResult<BindStatusInfo>() {
            @Override
            public void ok(BindStatusInfo json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                if (Guard.isNull(json.PnrInfo)) {
                    shipper.isFirstCg = true;
                } else {
                    shipper.isFirstCg = false;
                }
                _dbCache.save(shipper);
                if (!Guard.isNull(json.PnrInfo)) {
                    bindStatusInfo = json;
                    _dbCache.save(bindStatusInfo);

                    if (!shipper.havedPayMethod && shipper.cardid < 0 && bindStatusInfo.PnrCards.size() > 0) {
                        String number = "", cardNum = bindStatusInfo.PnrCards.get(0).CardNo;
                        if (cardNum.length() > 4) {
                            number = cardNum.substring(cardNum.length() - 4, cardNum.length());
                        }
                        String bankName = "";
                        for (int i = 0; i < BankInfoService.getBankData().size(); i++) {
                            if (BankInfoService.getBankData().get(i).getBankcode().equals(json.PnrCards.get(0).BankCode)) {
                                bankName = BankInfoService.getBankData().get(i).getBankName();
                            }
                        }
                        String payment = "使用\t\t" + bankName + "(" + number + ")\t\t" + "付款";
                        shipper.payment = payment;
                        shipper.cardid = bindStatusInfo.PnrCards.get(0).Id;
                        _dbCache.save(shipper);
                    }

                    if (bindStatusInfo.PnrCards.size() <= 0) {
                        payToPlat();//无卡
                    } else {
                        //有卡
                        Intent intent = new Intent(baseActivity, UserPayToBankActivity.class);
                        intent.putExtra("action", "Oil");
                        startActivity(intent);
                    }
                } else {
                    payToPlat();
                }
            }

            @Override
            public void error(Return json) {
                if (dialog != null) {
                    if (dialog.isShowing()) {
                        dialog.dismiss();
                    }
                }
                if (!Guard.isNull(json)) {
                    if (!Guard.isNull(json.Result)) {
                        if (json.Result == -2)
                            payToPlat();
                    }
                }
            }
        });
    }

    void payToPlat() {
        Intent intent = new Intent(baseActivity, UserPayToPlatformFragmentActivity.class);
        intent.putExtra("action", "Oil");
        startActivity(intent);

    }


    private Object getHtmlObject() {
        Object a = new Object() {
            public void aaa() {
                //some code
            }
        };
        return a;
    }

    public String getFromAssets(String fileName) {
        try {
            InputStreamReader inputReader = new InputStreamReader(
                    getResources().getAssets().open(fileName));

            BufferedReader bufReader = new BufferedReader(inputReader);

            String line = "";
            String Result = "";

            while ((line = bufReader.readLine()) != null)
                Result += line;
            if (bufReader != null)
                bufReader.close();
            if (inputReader != null)
                inputReader.close();
            return Result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Tracer.e("WebViewOil","onKeyD"+isLoadUrl);
        if (isLoadUrl){
            isLoadUrl = false;
            topBar.setTitle("加油卡充值");
            goback(keyCode,event);
        }else {
//            webview.loadUrl("javascript:jsBack()");
            goback(keyCode,event);
        }
        return false;
    }
    boolean goback(int keyCode, KeyEvent event){
        if (webview != null) {
            if ((keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack())) {
                webview.goBack();
                return false;
            } else {
                finish();
                return super.onKeyDown(keyCode, event);
            }
        }
        return false;
    }

}
