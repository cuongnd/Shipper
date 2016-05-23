package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;

import org.csware.ee.Guard;
import org.csware.ee.api.GameApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.QCloudService;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.Return;
import org.csware.ee.model.WinnerInfo;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.Tools;
import org.csware.ee.utils.Utils;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;
import org.csware.ee.widget.ProgressWebView;
import org.csware.ee.widget.ScrollViewForListView;
//import org.xwalk.core.XWalkView;
//import org.xwalk.core.XWalkWebChromeClient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/12.
 */
public class WebViewGameActivity extends ActivityBase {


    @InjectView(R.id.topBar)
    TopActionBar topBar;
//    @InjectView(R.id.xWalkView)
//    XWalkView xWalkView;
    @InjectView(R.id.LinXWalk)
    LinearLayout LinXWalk;
    @InjectView(R.id.proView)
    ProgressWebView proView;
    @InjectView(R.id.LinProView)
    LinearLayout LinProView;
    @InjectView(R.id.listViewXWalk)
    ScrollViewForListView listViewXWalk;
    @InjectView(R.id.listViewPro)
    ScrollViewForListView listViewPro;
    private List<WinnerInfo.LogEntity> list = new ArrayList<>();
    private String url;
    CommonAdapter<WinnerInfo.LogEntity> adapter;

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview_game);
        ButterKnife.inject(this);
//        xWalkView = (XWalkView) findViewById(R.id.xWalkView);
//        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) xWalkView.getLayoutParams();
//        params.height = Tools.getScreenHeight(this) * 2 / 3;
//        xWalkView.setLayoutParams(params);
        // ~~~ 获取参数
        url = getIntent().getStringExtra("url");

        if (isSDKView()) {
            LinProView.setVisibility(View.GONE);
            LinXWalk.setVisibility(View.VISIBLE);
//            xWalkView.load(url, null);
//            xWalkView.getSettings().setJavaScriptEnabled(true);
//            xWalkView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//            xWalkView.getSettings().setAllowContentAccess(true);
//            xWalkView.getSettings().setAllowFileAccess(true);
//            xWalkView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//            xWalkView.getSettings().setLoadsImagesAutomatically(true);
//            xWalkView.getSettings().setImagesEnabled(true);
//            xWalkView.getSettings().setDomStorageEnabled(true);
//            xWalkView.setXWalkWebChromeClient(new XWalkWebChromeClient(){
//
//            });
            asyncLoadingData();
//            xWalkView.setXWalkClient(new XWalkClient(xWalkView){
//
//            });
        } else {
            LinProView.setVisibility(View.VISIBLE);
            LinXWalk.setVisibility(View.GONE);
            WebSettings webSettings = proView.getSettings();
            webSettings.setJavaScriptEnabled(true);
            webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
            webSettings.setSupportZoom(true);
            webSettings.setDisplayZoomControls(false);
            webSettings.setAllowFileAccess(true);
            webSettings.setUseWideViewPort(true);
            webSettings.setLoadWithOverviewMode(true);
            webSettings.setBuiltInZoomControls(true);
            webSettings.setDomStorageEnabled(true);
            webSettings.setDatabaseEnabled(true);
            webSettings.setAppCacheMaxSize(1024 * 1024 * 10);//设置缓冲大小，10M
            webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);//用缓存用缓存,没有用网络
            webSettings.setRenderPriority(WebSettings.RenderPriority.HIGH);
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
            ProgressWebView.WebChromeClient wvcc = new ProgressWebView.WebChromeClient() {
                @Override
                public void onReceivedTitle(WebView view, String title) {
                    super.onReceivedTitle(view, title);
                    topBar.setTitle(title);
                }
            };
            proView.setWebChromeClient(wvcc);
            proView.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    return false;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    //                Loading.show();
                }

                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);
                    asyncLoadingData();
                }
            });
            proView.loadUrl(url);
        }
//        try
//        {
//            if (Integer.parseInt(android.os.Build.VERSION.SDK) >= 11)
//            {
//                getWindow().setFlags(android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
//                        android.view.WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED);
//            }
//        }
//        catch (Exception e)
//        {
//            e.printStackTrace();
//        }





//        webview.setInitialScale(15);
        // 设置setWebChromeClient对象


//        webview.setDownloadListener(new DownloadListener() {
//            @Override
//            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
//                if (url != null && url.startsWith("http://"))
//                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
//            }
//        });
//        webview.getSettings().setJavaScriptEnabled(true);

        topBar.setMenu(R.mipmap.icon_refresh, new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
               if (isSDKView()) {
//                    xWalkView.reload(XWalkView.RELOAD_NORMAL);
                } else {
                    proView.reload();
                }
            }
        });

        adapter = new CommonAdapter<WinnerInfo.LogEntity>(this, list, R.layout.list_item_winner) {
            @Override
            public void convert(ViewHolder helper, WinnerInfo.LogEntity item, int position) {
                helper.setText(R.id.txtGameName, item.Name);
                int Type = item.Item.Type;
                String winMore = "";
                switch (Type) {
                    case 1:
                        winMore = item.Item.Amount + " 元";
                        break;
                    case 2:
                        winMore = item.Item.Amount + "";
                        break;
                    case 3:
                        winMore = item.Item.Desc + "";
                        break;
                    default:
                        winMore = item.Item.Amount + " 元";
                        break;
                }
                helper.setText(R.id.txtWinMoney, winMore);
                Date date = new Date(Long.parseLong(String.valueOf(item.Time)) * 1000);
                String time = Utils.getCurrentTime("MM-dd HH:mm", date);
                helper.setText(R.id.txtGameTime, time);
                if (!Guard.isNullOrEmpty(item.Avatar)) {
                    QCloudService.asyncDisplayImage(WebViewGameActivity.this, item.Avatar, (ImageView) helper.getView(R.id.imgGameHead));
                }else {
                    helper.setImageResource(R.id.imgGameHead,R.drawable.w_icon_tjzp);
                }
            }
        };
        if (isSDKView()) {
            listViewXWalk.setAdapter(adapter);
        } else {
            listViewPro.setAdapter(adapter);
        }
    }

    void asyncLoadingData() {
        Tracer.e("WebViewGame", url);
        HttpParams params = new HttpParams(API.GAME.REDPACKET);
        if (url.contains("Packet")) {
            params = new HttpParams(API.GAME.REDPACKET);
        } else if (url.contains("Card")) {
            params = new HttpParams(API.GAME.SCRATCHCARD);
        } else if (url.contains("Wheel")) {
            params = new HttpParams(API.GAME.FORTUNEWHEEL);
        }

        GameApi.confirm(baseActivity, baseActivity, params, new IJsonResult<WinnerInfo>() {
            @Override
            public void ok(WinnerInfo json) {
                //成功
                List<WinnerInfo.LogEntity> logEntities = json.Log;
                if (logEntities == null || logEntities.size() == 0) {
                    adapter.notifyDataSetChanged();
                } else {
                    list.addAll(logEntities);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void error(Return json) {

            }
        });

    }
    public boolean isSDKView(){
        //Build.VERSION.SDK_INT <= 19
        return false;
    }

}
