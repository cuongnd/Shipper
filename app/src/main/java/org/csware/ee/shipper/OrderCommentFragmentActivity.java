package org.csware.ee.shipper;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;

import com.umeng.analytics.MobclickAgent;

import org.csware.ee.Guard;
import org.csware.ee.api.OrderApi;
import org.csware.ee.app.FragmentActivityBase;
import org.csware.ee.app.Tracer;
import org.csware.ee.component.IJsonResult;
import org.csware.ee.consts.ParamKey;
import org.csware.ee.model.Code;
import org.csware.ee.model.Return;
import org.csware.ee.utils.Tools;
import org.csware.ee.view.TopActionBar;

import java.util.HashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class OrderCommentFragmentActivity extends FragmentActivityBase implements View.OnTouchListener,GestureDetector.OnGestureListener{

    final static String TAG = "OrderCommentAct";
    @InjectView(R.id.rateStar)
    RatingBar rateStar;
    @InjectView(R.id.txtComment)
    EditText txtComment;
    @InjectView(R.id.btnConfirm)
    Button btnConfirm;
    @InjectView(R.id.topBar)
    TopActionBar topBar;
    GestureDetector detector = new GestureDetector(this);
    private int screenW,screenH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_comment_fragment);
        ButterKnife.inject(this);
//        detector = new GestureDetector(this);
        detector.setIsLongpressEnabled(true);
        screenW = Tools.getScreenWidth(baseActivity);
        screenH = Tools.getScreenHeight(baseActivity);
        topBar.setBackClickListener(new TopActionBar.BackClickListener() {
            @Override
            public void backClick() {
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
//                InputMethodManager imm = ( InputMethodManager ) txtComment.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
//                if ( imm.isActive( ) ) {
//                    imm.hideSoftInputFromWindow( txtComment.getApplicationWindowToken( ) , 0 );
//
//                }
                finish();
            }
        });
//        Log.d(TAG, "onCreate");
        init();
    }

    long orderId;


    void init() {
        Intent intent = getIntent();
        orderId = intent.getLongExtra(ParamKey.ORDER_ID, 0);

        btnConfirm.setOnClickListener(clickConfirm);
        txtComment.setOnTouchListener(this);
//        txtComment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
////                InputMethodManager imm = ( InputMethodManager ) txtComment.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
////                imm.showSoftInput(txtComment,InputMethodManager.SHOW_FORCED);
//            }
//        });

//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
    }


    View.OnClickListener clickConfirm = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
//            Log.d(TAG, "onClick");
            clickDo(v);
        }
    };

//    @OnClick({R.id.btnConfirm})
//    void openDone(View v) {
//        Log.d(TAG, "openDone");
//        clickDo(v);
//    }

    void clickDo(View v) {
        double rate = rateStar.getRating();
//        Log.d(TAG, "btnConfirm rate"+rate);
        String comt = txtComment.getText().toString();
        if (rate == 0d || Guard.isNullOrEmpty(comt)) {
            toastFast("请给出服务评价并填写评价详情");
            return;
        }

        HashMap<String,String> map = new HashMap<>();
        map.put("orderId",orderId+"");
        MobclickAgent.onEvent(baseActivity, "myorder_comment_submit", map);
        OrderApi.rate(baseActivity, baseContext, orderId, rate, comt, new IJsonResult() {
            @Override
            public void ok(Return json) {
                //成功
                toastFast("评价成功");
                setResult(Code.OK.toValue(), getIntent());
                finish();
            }

            @Override
            public void error(Return json) {
                toastFast("评价失败");
            }
        });

    }

    @Override
    public boolean onDown(MotionEvent e) {
//        toastFast("onDown");
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
//        InputMethodManager imm = ( InputMethodManager ) txtComment.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
//        imm.showSoftInput(txtComment,InputMethodManager.SHOW_FORCED);
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
//        InputMethodManager imm = ( InputMethodManager ) txtComment.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
//        imm.showSoftInput(txtComment,InputMethodManager.SHOW_FORCED);
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        float x = e2.getX() - e1.getX();
        float y = e2.getY() - e1.getY();
        //限制必须得划过屏幕的1/4才能算划过
        float x_limit = screenW / 4;
        Tracer.i("screen",x_limit+"");
        float y_limit = screenH / 4;
        float x_abs = Math.abs(x);
        float y_abs = Math.abs(y);
        if(x_abs < y_abs){
            if(y > y_limit || y < -y_limit){
                if(y>0){
//                    toastFast("down");
                    InputMethodManager imm = ( InputMethodManager ) txtComment.getContext( ).getSystemService( Context.INPUT_METHOD_SERVICE );
                    if ( imm.isActive( ) ) {
                        imm.hideSoftInputFromWindow( txtComment.getApplicationWindowToken( ) , 0 );

                    }
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                    //down
//                    show("down");
//                    ImageDetailActivity.this.finish();
                }
//                else if(y<=0){
//                    //up
//                    show("up");
//                }
            }
        }
        return false;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        detector.onTouchEvent(event);
        return true;
    }
}
