package org.csware.ee.utils;

import android.app.AlertDialog;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.csware.ee.R;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by user on 2014/12/22.
 */
public class DialogHelper {


    public static Date stringToDate(String dateString) {
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }

    public static void iosOnlyHintDialog(View.OnClickListener listener, final AlertDialog dialog){

        dialog.show();
        dialog.setCancelable(false);
        Window window = dialog.getWindow();
        window.setContentView(R.layout.ios_onlyhint_dialog);
//        TextView content=(TextView) window.findViewById(R.id.txt_content);
//        TextView title=(TextView) window.findViewById(R.id.txt_title);
//        TextView comfirm=(TextView) window.findViewById(R.id.txt_confirm);
        LinearLayout openBtn=(LinearLayout) window.findViewById(R.id.open_lin);
//        content.setText(Message);
//        title.setText(Title);
//        comfirm.setText(ConfirmBt);

        openBtn.setOnClickListener(listener);

    }

}
