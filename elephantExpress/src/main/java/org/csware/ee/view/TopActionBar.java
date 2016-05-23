package org.csware.ee.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.Guard;
import org.csware.ee.R;
import org.csware.ee.consts.ParamKey;

/**
 * Created by Yu on 2015/6/19.
 * 自定义顶部标题栏
 */
public class TopActionBar extends LinearLayout {

    final static String TAG = "TopActionBar";

    //左右按钮
    LinearLayout btnBack, btnMenu;

    //三个文本
    TextView labBack, labTitle, labMenu;
    String backText, titleText, menuText;

    //左右两个图片
    ImageView ivBack, ivMenu;
    Drawable backImage, menuImage;

    boolean canClose;

    Activity refActivity;

    /**
     * 返回按钮
     */
    BackClickListener backClickListener;

    public interface BackClickListener {
        public void backClick();
    }

    public void setBackClickListener(BackClickListener listener) {
        this.backClickListener = listener;
        btnBack.setVisibility(VISIBLE);
    }

    /**
     * 菜单按钮
     */
    MenuClickListener menuClickListener;

    public interface MenuClickListener {
        public void menuClick();
    }



    /**
    * 同时设置两个侦听器
    */
    public void setClickListener(BackClickListener backClickListener, MenuClickListener menuClickListener) {
        this.backClickListener = backClickListener;
        this.menuClickListener = menuClickListener;
        btnMenu.setVisibility(VISIBLE);
        btnBack.setVisibility(VISIBLE);
    }



    String defaultName;
    String defaultBackName;

//    public TopActionBar(Context context){
//        TopActionBar(context, null);
//    }

    public TopActionBar(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (isInEditMode()) {
            //IDE编辑模式
            return;
        } else {
            //真实模式
            refActivity = (Activity) context;
            defaultName = refActivity.getTitle().toString();
//            Log.d(TAG, "defaultName=" + defaultName);
            //TODO:不可以在构造时获得Intent等信息
            //setBackTitle(refActivity.getIntent());
        }


        //获取自定义属性和值的映射集合
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.TopActionBar);

        //取出左中右文本信息
        backText = ta.getString(R.styleable.TopActionBar_backText);
        titleText = ta.getString(R.styleable.TopActionBar_titleText);
        menuText = ta.getString(R.styleable.TopActionBar_menuText);

        //取出图标资源信息
        backImage = ta.getDrawable(R.styleable.TopActionBar_backImage);
        menuImage = ta.getDrawable(R.styleable.TopActionBar_menuImage);

        //是否能返回
        canClose = ta.getBoolean(R.styleable.TopActionBar_canClose, false);

        //回收TypedArray（避免浪费资源，避免因为缓存导致的错误）
        ta.recycle();
    }

    public void setTitle(String title) {
        labTitle.setText(title);
    }

    public void setBackTitle(String title) {
        labBack.setText(title);
        labBack.setVisibility(VISIBLE);
    }

    public void setBackTitle(Intent intent) {
        String backTitle = intent.getStringExtra(ParamKey.BACK_TITLE);
        if (!Guard.isEmpty(backTitle)) setBackTitle(backTitle);
    }

    public void setMenuTitle(String title) {
        labMenu.setText(title);
        labMenu.setVisibility(VISIBLE);
    }
    /**设置菜单按钮*/
    public void setMenuIcon(int resId){
        ivMenu.setImageDrawable(getResources().getDrawable(resId));
    }
    /**隐藏菜单图标*/
    public void hideMenuIcon(){
        ivMenu.setVisibility(GONE);
    }
    /**设置右侧的菜单与点击事件*/
    public void setMenu(int resId,MenuClickListener listener){
        setMenuIcon(resId);
        setMenuClickListener(listener);
    }
    /**设置菜单事件*/
    public void setMenuClickListener(MenuClickListener listener) {
        if(listener == null){
            btnMenu.setVisibility(INVISIBLE);
        }else{
            this.menuClickListener = listener;
            btnMenu.setVisibility(VISIBLE);
        }
    }
    /**隐藏菜单*/
    public void hideMenu(){
        setMenuClickListener(null);
    }

    /**
     * 自定义XML布局
     */
    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        LayoutInflater.from(getContext()).inflate(R.layout.top_action_bar, this);

        btnBack = getView(R.id.btnBack);
        labBack = getView(R.id.labBack);
        ivBack = getView(R.id.ivBack);
        labBack.setVisibility(INVISIBLE);
        btnBack.setVisibility(INVISIBLE);

        labTitle = getView(R.id.labTitle);

        btnMenu = getView(R.id.btnMenu);
        labMenu = getView(R.id.labMenu);
        ivMenu = getView(R.id.ivMenu);
        labMenu.setVisibility(INVISIBLE);
        btnMenu.setVisibility(INVISIBLE);


        btnBack.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                backClickListener.backClick();
            }
        });

        btnMenu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                menuClickListener.menuClick();
            }
        });


//        默认的Activity名称
        if (!Guard.isNullOrEmpty(defaultName)) {
            setTitle(defaultName);
        }
        if (backImage != null) {
            ivBack.setImageDrawable(backImage);
        }
        if (menuImage != null) {
            ivMenu.setImageDrawable(menuImage);
        }

        if (canClose) {
            if (refActivity != null) {
                //可以关闭的操作
                setBackClickListener(new BackClickListener() {
                    @Override
                    public void backClick() {
                        refActivity.finish();
                    }
                });
                setBackTitle(refActivity.getIntent());

//                Intent x = refActivity.getIntent();
//                Log.e(TAG, "x:" + GsonHelper.toJson(x.getAction()));
                //Log.e(TAG, "XX:" + defaultBackName);
                //setBackTitle(refActivity.getIntent());
            }
        }


    }

    <T extends View> T getView(int resId) {
        View view = findViewById(resId);
        return (T) view;
    }
}
