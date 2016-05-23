package org.csware.ee.widget;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import com.gghl.view.wheelview.JudgeDate;
import com.gghl.view.wheelview.ScreenInfo;
import com.gghl.view.wheelview.WheelMain;
import com.zf.iosdialog.widget.MyAlertDialog;

import org.csware.ee.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by Yu on 2015/5/27.
 * 时间选择弹窗
 */
public class TimePickerDialog {

    public TimePickerDialog(Activity context) {
        this.context = context;
    }

    Activity context;

    DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    /**
     * 弹出选择框
     */
    public void popDialog(final EditText view) {

        LayoutInflater inflater1 = LayoutInflater.from(context);
        final View timepickerview1 = inflater1.inflate(R.layout.timepicker,
                null);
        final ScreenInfo screenInfo1 = new ScreenInfo(context);
        final WheelMain wheelMain = new WheelMain(timepickerview1);
        wheelMain.screenheight = screenInfo1.getHeight();
        String time1 = view.getText().toString();
        Calendar calendar1 = Calendar.getInstance();
        if (JudgeDate.isDate(time1, "yyyy-MM-dd")) {
            try {
                calendar1.setTime(dateFormat.parse(time1));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        int year1 = calendar1.get(Calendar.YEAR);
        int month1 = calendar1.get(Calendar.MONTH);
        int day1 = calendar1.get(Calendar.DAY_OF_MONTH);
        wheelMain.initDateTimePicker(year1, month1, day1);
        final MyAlertDialog dialog = new MyAlertDialog(context)
                .builder()
                .setTitle(view.getHint().toString())
                        // .setMsg("确定退出？")
                        // .setEditText("1111111111111")
                .setView(timepickerview1)
                .setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                });
        dialog.setPositiveButton("保存", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(getApplicationContext(),
//                        wheelMain.getTime(), 1).show();
                view.setText(wheelMain.getTime());
            }
        });
        dialog.show();
    }


}
