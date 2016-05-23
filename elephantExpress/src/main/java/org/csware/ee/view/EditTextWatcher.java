package org.csware.ee.view;

import android.text.TextWatcher;
import android.view.View;

/**
 * Created by Yu on 2015/5/28.
 * 针对EditText进行扩展
 */
public abstract class EditTextWatcher implements TextWatcher {

    protected View view;
    public EditTextWatcher(View v){
        this.view = v;
    }

}
