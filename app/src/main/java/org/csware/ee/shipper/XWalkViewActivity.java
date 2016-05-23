package org.csware.ee.shipper;

import android.os.Bundle;

import org.csware.ee.app.ActivityBase;
//import org.xwalk.core.XWalkView;

/**
 * Created by Administrator on 2015/12/3.
 */
public class XWalkViewActivity extends ActivityBase {
    private final static String INITIAL_URL = "http://demo.xx3700.com/games/ScratchCard/?deviceid=d134069e-ae13-415f-9684-201a28ea7191&verkey=lbdw2himywmd8wdn&api=owner";
//    XWalkView xWalkWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwalk);
//        xWalkWebView = (XWalkView) findViewById(R.id.xWalkView);
//        xWalkWebView.getSettings().setJavaScriptEnabled(true);
//        xWalkWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
//        xWalkWebView.getSettings().setAllowContentAccess(true);
//        xWalkWebView.getSettings().setAllowFileAccess(true);
//        xWalkWebView.getSettings().setMediaPlaybackRequiresUserGesture(false);
//        xWalkWebView.getSettings().setLoadsImagesAutomatically(true);
//        xWalkWebView.getSettings().setImagesEnabled(true);
//        xWalkWebView.getSettings().setDomStorageEnabled(true);
//        xWalkWebView.load(INITIAL_URL, null);/*url must include http*/
    }
}
