package org.csware.ee.shipper;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.csware.ee.api.GameApi;
import org.csware.ee.api.UserApi;
import org.csware.ee.app.ActivityBase;
import org.csware.ee.app.DbCache;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.API;
import org.csware.ee.http.HttpParams;
import org.csware.ee.model.GameInfo;
import org.csware.ee.model.MeReturn;
import org.csware.ee.model.Return;
import org.csware.ee.service.DownloadService;
import org.csware.ee.shipper.adapter.CommonAdapter;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientStatus;
import org.csware.ee.utils.ParamTool;
import org.csware.ee.view.TopActionBar;
import org.csware.ee.view.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

/**
 * Created by Administrator on 2015/11/19.
 */
public class UserGameActivity extends ActivityBase {

    @InjectView(R.id.topBar)
    TopActionBar topBar;
    @InjectView(R.id.labScore)
    TextView labScore;
    @InjectView(R.id.btnScoreDetail)
    LinearLayout btnScoreDetail;
    @InjectView(R.id.listGame)
    ListView listGame;

    String TAG = "UserGameAct";

    List<GameInfo.GamesEntity> list;
    CommonAdapter<GameInfo.GamesEntity> adapter;

    DbCache dbCache;
    MeReturn userInfo;
    @InjectView(R.id.img_integra_detail)
    ImageView imgIntegraDetail;

    @Override
    protected void onResume() {
        super.onResume();
        initData();

    }

    void initData() {

        UserApi.info(baseActivity, baseActivity, new IJsonResult<MeReturn>() {
            @Override
            public void ok(MeReturn json) {
                userInfo = json;
                dbCache.save(userInfo);
                labScore.setText(userInfo.Owner.Score);
            }

            @Override
            public void error(Return json) {

            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        ButterKnife.inject(this);

        dbCache = new DbCache(baseActivity);
        userInfo = dbCache.GetObject(MeReturn.class); //重新载入缓存的用户数据
        if (userInfo == null) {
            finish();
        }

        topBar.setTitle("游戏");
        topBar.hideMenuIcon();
        topBar.setMenuTitle("积分规则");
        topBar.setMenuClickListener(new TopActionBar.MenuClickListener() {
            @Override
            public void menuClick() {
                Intent intent = new Intent(UserGameActivity.this, WebViewActivity.class);
                intent.putExtra("url", API.GAME.SCOREROLE);
                startActivity(intent);
            }
        });
        list = new ArrayList<>();
        asyncLoadingData();

        adapter = new CommonAdapter<GameInfo.GamesEntity>(this, list, R.layout.list_item_game) {
            @Override
            public void convert(ViewHolder helper, GameInfo.GamesEntity item, int position) {
                String url = API.GAME.GAMELIST + list.get(position).Name;
                int drawableId = 0;
                if (item.Enabled) {

                    drawableId = R.style.GameFocusFont;
                    url = url + "/img_s.png";
                } else {
                    drawableId = R.style.GreyBigFont;
                    url = url + "/img.png";
                }
                helper.setText(R.id.txtGameName, item.Title, drawableId);
//                QCloudService.asyncDisplayImage(UserGameActivity.this, url, (ImageView) helper.getView(R.id.imgGame), false);
                DownloadService.getInstance().init(url, (ImageView) helper.getView(R.id.imgGame), UserGameActivity.this);
                String stime = ParamTool.fromTimeSeconds(item.StartDay, "yyyy-MM-dd");
                String etime = ParamTool.fromTimeSeconds(item.EndDay, "yyyy-MM-dd");
                helper.setText(R.id.txtBeginTime, "开放时间：" + item.StartTime + ":00-" + item.EndTime + ":00");
            }
        };
        listGame.setAdapter(adapter);
        listGame.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String url = "", gameName = list.get(position).Name + "/";
                Intent intent = new Intent(baseActivity, WebViewGameActivity.class);
                String key = "?deviceid=" + AppStatus.getDeviceId(baseActivity) + "&verkey=" + ClientStatus.getToken() + "&api=owner";
                switch (position) {
                    case 0:
//                        url = "http://192.168.1.73/RouletGameFlash/RouletGame.html?"+ key;
                        break;
                    case 1:
//                        url = "http://192.168.1.73/LuckyCardFlash/index.html?"+ key;
                        break;
                    case 2:
//                        url = "http://192.168.1.73/MoneyBagFlash/MoneyBag.html?" + key;
                        break;
                }
                url = API.GAME.GAMELIST + gameName + key;
                Tracer.e(TAG, url);
                intent.putExtra("url", url);
                startActivity(intent);
            }
        });

    }

    @OnClick(R.id.img_integra_detail)
    void setImgIntegraDetail(){
        Intent intent = new Intent(this, GameExchangeActivity.class);
        intent.putExtra("Title", "积分明细");
        startActivity(intent);
    }

    private void asyncLoadingData() {
        String res = "{GameInfo : [{\"ImgSrc\":R.mipmap.game_bg_turntable_default,\"ImgDefSrc\":R.mipmap.game_bg_turntable_disabled,\"BeganTime\":\"10:00-12:00\",\"GameName\":\"幸运大转盘\",\"Id\":1,\"Status\":true},\n" +
                "{\"ImgSrc\":R.mipmap.game_bg_scratch_default,\"ImgDefSrc\":R.mipmap.game_bg_scratch_disabled,\"BeganTime\":\"12:00-15:00\",\"GameName\":\"幸运刮刮卡\",\"Id\":2,\"Status\":false},\n" +
                "{\"ImgSrc\":R.mipmap.game_bg_redbag_default,\"ImgDefSrc\":R.mipmap.game_bg_redbag_disabled,\"BeganTime\":\"18:00-20:00\",\"GameName\":\"开红包\",\"Id\":3,\"Status\":false}]}";
//        GameInfo gameInfo = GsonHelper.fromJson(res, GameInfo.class);
//        list = gameInfo.GameInfo;
        GameInfo.GameInfoEntity entity = new GameInfo.GameInfoEntity();
        entity.setBeganTime("10:00-12:00");
        entity.setGameName("幸运大转盘");
        entity.setImgSrc(R.mipmap.game_bg_turntable_default);
        entity.setImgDefSrc(R.mipmap.game_bg_turntable_disabled);
        entity.setStatus(true);
        GameInfo.GameInfoEntity entity1 = new GameInfo.GameInfoEntity();
        entity1.setBeganTime("12:00-15:00");
        entity1.setGameName("幸运刮刮卡");
        entity1.setImgSrc(R.mipmap.game_bg_scratch_default);
        entity1.setImgDefSrc(R.mipmap.game_bg_scratch_disabled);
        entity1.setStatus(false);
        GameInfo.GameInfoEntity entity2 = new GameInfo.GameInfoEntity();
        entity2.setBeganTime("18:00-20:00");
        entity2.setGameName("开红包");
        entity2.setImgSrc(R.mipmap.game_bg_redbag_default);
        entity2.setImgDefSrc(R.mipmap.game_bg_redbag_disabled);
        entity2.setStatus(false);
//        list.add(entity);
//        list.add(entity1);
//        list.add(entity2);
        HttpParams params = new HttpParams(API.GAME.OPEN);
        GameApi.open(baseActivity, baseActivity, params, new IJsonResult<GameInfo>() {
            @Override
            public void ok(GameInfo json) {
                //成功
                List<GameInfo.GamesEntity> logEntities = json.Games;
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
}
