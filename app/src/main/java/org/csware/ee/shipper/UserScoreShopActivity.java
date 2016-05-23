package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.consts.API;
import org.csware.ee.model.MeReturn;
import org.csware.ee.view.TopActionBar;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/19.
 */
public class UserScoreShopActivity extends ActivityBase {


    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.labScore)
    TextView labScore;
    @InjectView(R.id.Lin_calculation)
    LinearLayout LinCalculation;
    @InjectView(R.id.btnScoreDetail)
    LinearLayout btnScoreDetail;
    @InjectView(R.id.btnExchange)
    LinearLayout btnExchange;
    @InjectView(R.id.btnGift)
    LinearLayout btnGift;
    DbCache dbCache;
    MeReturn userInfo;

    @Override
    protected void onResume() {
        super.onResume();
        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class); //重新载入缓存的用户数据
        if (userInfo == null) {
            finish();
        }
        String score = "";
        if (!Guard.isNull(userInfo)){
            if(!Guard.isNull(userInfo.Owner)){
                if (!Guard.isNull(userInfo.Owner.Score)){
                    score = userInfo.Owner.Score;
                }
            }
        }
        labScore.setText(score);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        ButterKnife.inject(this);

        topBar.setTitle("积分商城");
        topBar.setMenuTitle("积分规则");
        topBar.hideMenuIcon();
        topBar.setMenuClickListener(new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
                Intent intent = new Intent(UserScoreShopActivity.this, WebViewActivity.class);
                intent.putExtra("url", API.GAME.SCOREROLE);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.btnScoreDetail)
    void setBtnScoreDetail() {
        Intent intent = new Intent(this, GameExchangeActivity.class);
        intent.putExtra("Title", "积分明细");
        startActivity(intent);
    }

    @OnClick(R.id.btnExchange)
    void setBtnExchange() {
        Intent intent = new Intent(this, GameExchangeActivity.class);
        intent.putExtra("Title", "兑换记录");
        startActivity(intent);
    }

}
