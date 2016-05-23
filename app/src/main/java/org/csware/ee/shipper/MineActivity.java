package org.csware.ee.shipper;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;


public class MineActivity extends Activity {

    TabHost tabhost;
    TabHost.TabSpec tabQuoting;
    TabHost.TabSpec tabShipping;
    TabHost.TabSpec tabFinished;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine);




        //得到TabHost对象实例
        tabhost =(TabHost) findViewById(R.id.tabOrder);
        //调用 TabHost.setup()
        tabhost.setup();

//        //将View直接传入setIndicator即可使用自定样式
//        View v1 = loadView(R.layout.tab_indicator);
//        TextView t1 =(TextView) v1.findViewById(R.id.title);
//        t1.setText("Test");

        //创建Tab标签
        tabQuoting = tabhost.newTabSpec("Q").setIndicator(getString(R.string.tab_lab_quoting)).setContent(R.id.orderQuoting);
        tabhost.addTab(tabQuoting);
        tabShipping = tabhost.newTabSpec("S").setIndicator(getString(R.string.tab_lab_shipping)).setContent(R.id.orderShipping);
        tabhost.addTab(tabShipping);
        tabFinished = tabhost.newTabSpec("F").setIndicator(getString(R.string.tab_lab_finished)).setContent(R.id.orderFinished);
        tabhost.addTab(tabFinished);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu, menu);
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
