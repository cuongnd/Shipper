package org.csware.ee.shipper;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

//import org.xwalk.core.XWalkView;

public class MainActivity extends AppCompatActivity {
    private final static String INITIAL_URL = "http://demo.xx3700.com/games/ScratchCard/?deviceid=d134069e-ae13-415f-9684-201a28ea7191&verkey=lbdw2himywmd8wdn&api=owner";
//    XWalkView xWalkWebView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xwalk);
//        xWalkWebView = (XWalkView) findViewById(R.id.webview);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
