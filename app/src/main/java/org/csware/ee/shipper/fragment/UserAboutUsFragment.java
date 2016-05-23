package org.csware.ee.shipper.fragment;

import android.content.Context;
import android.content.res.AssetManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.csware.ee.app.FragmentBase;
import org.csware.ee.shipper.R;
import org.csware.ee.shipper.UserSettingFragmentActivity;
import org.csware.ee.widget.MyTextView2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * 关于我们
 * Created by Yu on 2015/6/30.
 */
public class UserAboutUsFragment extends FragmentBase {

final static String TAG = "UserAboutUs";

    TextView text_a,text_b,text_c;
    MyTextView2 view;
    MyTextView2 view1;
    MyTextView2 view2;

    @Override
    protected boolean getIsPrepared() {
        return true;
    }


    @Override
    protected void lazyLoad() {


    }

    @Override
    protected int getLayoutId() {
        return R.layout.user_about_us_fragment;
    }

    @Override
    public void init() {
        findViews();
        ((UserSettingFragmentActivity) getActivity()).back(getString(R.string.title_about_us));
    }


    private void findViews(){
//        text_a = (TextView)findViewById(R.id.text_a);
//        text_b = (TextView)findViewById(R.id.text_b);
//        text_c = (TextView)findViewById(R.id.text_c);


//        view = (MyTextView2) rootView.findViewById(R.id.text_a);
//        view.setText(getAssetsString(baseActivity, "introduction"));
//        view.setMovementMethod(ScrollingMovementMethod.getInstance());
//
//        view1 = (MyTextView2) rootView.findViewById(R.id.text_b);
//        view1.setText(getAssetsString(baseActivity, "introduction_b"));
//        view1.setMovementMethod(ScrollingMovementMethod.getInstance());
//
//        view2 = (MyTextView2) rootView.findViewById(R.id.text_c);
//        view2.setText(getAssetsString(baseActivity, "introduction_c"));
//        view2.setMovementMethod(ScrollingMovementMethod.getInstance());



//        Log.i("Text", getAssetsString(baseActivity, "introduction"));


//        text_a.setText("\u3000" + getResources().getString(R.string.lulang_introduce).trim());
//        text_b.setText("\u3000"+ getResources().getString(R.string.lulang_team_introduce).trim());
//        text_c.setText("\u3000"+getResources().getString(R.string.lulang_company_introduce).trim());

        ((UserSettingFragmentActivity) getActivity()).back(getString(R.string.icon_setting));
    }
    public String getAssetsString(Context context,String fileName){
        StringBuffer sb = new StringBuffer();
        //根据语言选择加载
        try {
            AssetManager am = context.getAssets();
            InputStream in = am.open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while((line = reader.readLine())!=null){
                line += ("\n");
                sb.append(line);
            }
            reader.close();
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
