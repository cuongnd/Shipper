package org.csware.ee.utils;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;


import org.csware.ee.Guard;
import org.csware.ee.shipper.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2014/12/22.
 */
public class DialogUtil {


        public static Date stringToDate(String dateString) {
            ParsePosition position = new ParsePosition(0);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            Date dateValue = simpleDateFormat.parse(dateString, position);
            return dateValue;
        }

    public static void showExitAlert(View.OnClickListener listener, final AlertDialog dlg) {
//        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.dialog_style);
//        TextView icon_title = (TextView) window.findViewById(R.id.icon_title);
//        TextView information = (TextView) window.findViewById(R.id.information);
//        icon_title.setText(title);
//        information.setText(info);
        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.save);
        ok.setOnClickListener(listener);
        // 关闭alert对话框架
//        Button cancel = (Button) window.findViewById(R.id.cancel);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dlg.cancel();
//            }
//        });
    }
    public static void showExitAlert(Context context,String title,String info, final View.OnClickListener listener,final View.OnClickListener cancel_listener, final AlertDialog dlg) {
//        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.dialog_style);
        TextView icon_title = (TextView) window.findViewById(R.id.icon_title);
//        TextView information = (TextView) window.findViewById(R.id.information);
        icon_title.setText(title);
//        information.setText(info);
        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.save);
        ok.setOnClickListener(listener);
        // 关闭alert对话框架
//        Button cancel = (Button) window.findViewById(R.id.cancel);
//        cancel.setOnClickListener(cancel_listener);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dlg.cancel();
//            }
//        });
    }
    public static void showUpdateAlert(String version,String info, String cancelStr, String okStr, String titleStr, final View.OnClickListener listener,final View.OnClickListener cancel_listener, AlertDialog dlg) {
//        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.dialog_update);
        TextView title_txt = (TextView) window.findViewById(R.id.title_txt);
        TextView versionCode = (TextView) window.findViewById(R.id.versionCode);
        TextView updatContent = (TextView) window.findViewById(R.id.updat_content);
//        TextView information = (TextView) window.findViewById(R.id.information);
        if (Guard.isNullOrEmpty(version)){
            versionCode.setVisibility(View.GONE);
        }
        versionCode.setText(version);
        updatContent.setText(info);
        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.confirm_btn);
        Button cancel = (Button) window.findViewById(R.id.cancel_btn);
        cancel.setText(cancelStr);
        ok.setText(okStr);
        title_txt.setText(titleStr);
        ok.setOnClickListener(listener);
        cancel.setOnClickListener(cancel_listener);
        // 关闭alert对话框架
//        cancel.setOnClickListener(cancel_listener);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dlg.cancel();
//            }
//        });
    }


    public static void showBindCardAlert(String strBindCardName,String strBindIDCard, String strBindBankCardNumb, final View.OnClickListener listener,final View.OnClickListener cancel_listener, AlertDialog dlg) {
//        final AlertDialog dlg = new AlertDialog.Builder(context).create();
        dlg.show();
        Window window = dlg.getWindow();
        // *** 主要就是在这里实现这种效果的.
        // 设置窗口的内容页面,shrew_exit_dialog.xml文件中定义view内容
        window.setContentView(R.layout.dialog_bind);
        TextView txtBindCardName = (TextView) window.findViewById(R.id.txtBindCardName);
        TextView txtBindIDCard = (TextView) window.findViewById(R.id.txtBindIDCard);
        TextView txtBindBankCardNumb = (TextView) window.findViewById(R.id.txtBindBankCardNumb);
//        TextView information = (TextView) window.findViewById(R.id.information);
//        if (Guard.isNullOrEmpty(version)){
//            versionCode.setVisibility(View.GONE);
//        }
        txtBindCardName.setText(strBindCardName);
        txtBindIDCard.setText(strBindIDCard);
        txtBindBankCardNumb.setText(strBindBankCardNumb);
        // 为确认按钮添加事件,执行退出应用操作
        Button ok = (Button) window.findViewById(R.id.confirm_btn);
        Button cancel = (Button) window.findViewById(R.id.cancel_btn);
//        cancel.setText(cancelStr);
//        ok.setText(okStr);
//        title_txt.setText(titleStr);
        ok.setOnClickListener(listener);
        cancel.setOnClickListener(cancel_listener);
        // 关闭alert对话框架
//        cancel.setOnClickListener(cancel_listener);
//        cancel.setOnClickListener(new View.OnClickListener() {
//            public void onClick(View v) {
//                dlg.cancel();
//            }
//        });
    }

}
