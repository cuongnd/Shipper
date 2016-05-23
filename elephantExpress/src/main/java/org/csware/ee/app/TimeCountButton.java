package org.csware.ee.app;


import android.graphics.drawable.Drawable;
import android.os.CountDownTimer;
import android.widget.Button;

/**
 * Created by Atwind on 2015/5/17.
 * 计时按钮
 */
public class TimeCountButton extends CountDownTimer {

    Button timerButton;
    String reSendCodeText;
    String reSendText;

    public TimeCountButton(Button timerButton, long millisInFuture, long countDownInterval) {
        super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        this.timerButton = timerButton;
    }

    public void setText(String reSendCodeText, String reSendText ) {
        this.reSendCodeText = reSendCodeText;
        this.reSendText = reSendText;
    }

    int enableTextColor;
    int enableBgColor;
    int disableTextColor;
    int disableBgColor;

    public void setStatusColor(int enableTextColor,int enableBgColor,int disableTextColor,int disableBgColor){
        this.enableTextColor = enableTextColor;
        this.enableBgColor = enableBgColor;
        this.disableTextColor = disableTextColor;
        this.disableBgColor = disableBgColor;
    }

    @Override
    public void onFinish() {// 计时完毕时触发
        timerButton.setEnabled(true);
        timerButton.setText(reSendCodeText);
        timerButton.setTextColor(enableTextColor);
        timerButton.setBackgroundColor(enableBgColor);
        timerButton.setClickable(true);
    }

    @Override
    public void onTick(long millisUntilFinished) {// 计时过程显示
        timerButton.setEnabled(false);
        timerButton.setClickable(false);
        timerButton.setTextColor(disableTextColor);
        timerButton.setBackgroundColor(disableBgColor);
        timerButton.setText(reSendText + "（" + millisUntilFinished / 1000 + "）");

    }
}