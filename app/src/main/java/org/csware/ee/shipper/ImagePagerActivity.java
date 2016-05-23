package org.csware.ee.shipper;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.app.Tracer;
import org.csware.ee.model.OrderDetailChangeReturn;
import org.csware.ee.shipper.fragment.ImageDetailFragment;
import org.csware.ee.utils.GsonHelper;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.widget.HackyViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/2/24.
 */
public class ImagePagerActivity extends FragmentActivity implements View.OnClickListener {
    private static final String STATE_POSITION = "STATE_POSITION";
    public static final String EXTRA_IMAGE_INDEX = "image_index";
    public static final String EXTRA_IMAGE_URLS = "image_urls";

    private HackyViewPager mPager;
    private int pagerPosition;
    private TextView indicator, txtSuper, txtUpdataTime;
    ImageView imgPrev, imgNext;
    String rptJson;
    List<OrderDetailChangeReturn.OrderEntity.EvidencesEntity> evidencesList = new ArrayList<>();
    int pos;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_detail_pager);

        pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
        pos = pagerPosition;
        String[] urls = getIntent().getStringArrayExtra(EXTRA_IMAGE_URLS);
        rptJson = getIntent().getStringExtra("info");
        OrderDetailChangeReturn result = GsonHelper.fromJson(rptJson, OrderDetailChangeReturn.class);
        if (!Guard.isNull(result)) {
            if (!Guard.isNull(result.Order.Evidences)) {
                evidencesList = result.Order.Evidences;
            }
        }
        mPager = (HackyViewPager) findViewById(R.id.pager);
        ImagePagerAdapter mAdapter = new ImagePagerAdapter(
                getSupportFragmentManager(), urls);
        mPager.setAdapter(mAdapter);
        indicator = (TextView) findViewById(R.id.indicator);
        txtSuper = (TextView) findViewById(R.id.txtSuper);
        txtUpdataTime = (TextView) findViewById(R.id.txtUpdataTime);
        imgPrev = (ImageView) findViewById(R.id.imgPrev);
        imgNext = (ImageView) findViewById(R.id.imgNext);
        imgPrev.setOnClickListener(this);
        imgNext.setOnClickListener(this);

        CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
                .getAdapter().getCount());
        indicator.setText(text);
        if (evidencesList.size()>0){
            txtSuper.setText(evidencesList.get(pagerPosition).BearerName);
            String time = ParamTool.fromTimeSeconds(Integer.parseInt(evidencesList.get(pagerPosition).CreateTime));
            txtUpdataTime.setText(time);
        }
        // 更新下标
        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrollStateChanged(int arg0) {
            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
            }

            @Override
            public void onPageSelected(int arg0) {
                CharSequence text = getString(R.string.viewpager_indicator,
                        arg0 + 1, mPager.getAdapter().getCount());
                indicator.setText(text);
                if (evidencesList.size()>0){
                    txtSuper.setText(evidencesList.get(arg0).BearerName);
                    String time = ParamTool.fromTimeSeconds(Integer.parseInt(evidencesList.get(pagerPosition).CreateTime));
                    txtUpdataTime.setText(time);
                }
            }

        });
        if (savedInstanceState != null) {
            pagerPosition = savedInstanceState.getInt(STATE_POSITION);
        }

        mPager.setCurrentItem(pagerPosition);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putInt(STATE_POSITION, mPager.getCurrentItem());
    }

    @Override
    public void onClick(View v) {
        int size = mPager.getAdapter().getCount();

        switch (v.getId()){
            case R.id.imgPrev:
                if (pos>0){
                    pos--;
                    mPager.setCurrentItem(pos);
                    CharSequence text = getString(R.string.viewpager_indicator,
                            pos+1, size);
                    indicator.setText(text);
                }
                break;
            case R.id.imgNext:
                if (pos+1<size){
                    pos++;
                    mPager.setCurrentItem(pos);
                    CharSequence text = getString(R.string.viewpager_indicator,
                            pos+1, size);
                    indicator.setText(text);
                }
                break;
        }
    }

    private class ImagePagerAdapter extends FragmentStatePagerAdapter {

        public String[] fileList;

        public ImagePagerAdapter(FragmentManager fm, String[] fileList) {
            super(fm);
            this.fileList = fileList;
        }

        @Override
        public int getCount() {
            return fileList == null ? 0 : fileList.length;
        }

        @Override
        public Fragment getItem(int position) {
            String url = fileList[position];
            return ImageDetailFragment.newInstance(url);
        }

    }
}