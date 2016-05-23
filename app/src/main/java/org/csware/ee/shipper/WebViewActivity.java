package org.csware.ee.shipper;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.widget.ProgressWebView;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by Administrator on 2015/10/12.
 */
public class WebViewActivity extends ActivityBase {


    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.webview)
    ProgressWebView webview;
    private String url;
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.webview);
        ButterKnife.inject(this);

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
        Tracer.e("WebView",webview.getScale()+"" + Tools.getScreenWidth(this));
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
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
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
        ProgressWebView.WebChromeClient wvcc = new ProgressWebView.WebChromeClient(){
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                topBar.setTitle(title);
            }
        };
        webview.setWebChromeClient(wvcc);
        webview.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }
        });
        webview.loadUrl(url);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (webview!=null) {
            if ((keyCode == KeyEvent.KEYCODE_BACK && webview.canGoBack())) {
                webview.goBack();
                return false;
            } else {
                return super.onKeyDown(keyCode, event);
            }
        }
        return false;
    }

}
