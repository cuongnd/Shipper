package org.csware.ee.app;

import android.app.AlertDialog;
import android.content.Context;

/**
 * Created by Yu on 2015/6/5.
 */
public class CommonDialog extends AlertDialog {

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int theme) {
        super(context, theme);
    }

    public CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    /**设定layout*/
    public void setLayoutId(int layoutResId){
        setContentView(layoutResId);
    }



}
