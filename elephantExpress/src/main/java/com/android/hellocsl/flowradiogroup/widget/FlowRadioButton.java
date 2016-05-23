package com.android.hellocsl.flowradiogroup.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RadioButton;


/**
 * Created by hellocsl on 2015/4/22 0022.
 */
public class FlowRadioButton extends RadioButton {
    private OnCheckedChangeListener mOnCheckedChangeWidgetListener;
    private boolean mBroadcasting;


    public FlowRadioButton(Context context) {
        super(context);
    }

    public FlowRadioButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowRadioButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setChecked(boolean checked) {
        super.setChecked(checked);
        if (mBroadcasting) {
            return;
        }
        mBroadcasting = true;
        if (mOnCheckedChangeWidgetListener != null) {
            mOnCheckedChangeWidgetListener.onCheckedChanged(this, checked);
        }
        mBroadcasting = false;
    }

    void setOnCheckedChangeWidgetListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeWidgetListener = listener;
    }
}

