package com.android.hellocsl.flowradiogroup.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import org.csware.ee.R;


/**
 * Created by hellocsl on 2015/4/22 0022.
 */
public class FlowRadioGroup extends ViewGroup {
    private final String TAG = this.getClass().getSimpleName();
    private final boolean DEBUG = true;
    //default is auto fix
    private final int DEFAULT_COLUMU_NUM = -1;
    private final int[] FLOW_RADIOGROUP_ATTRS = {
            R.attr.radio_columu_num,
            android.R.attr.checkedButton
    };
    private final int COLUMU_INDEX = 0;
    private final int CHECKED_BUTTON_INDEX = 1;

    private int mColumuNum;
    // holds the checked id; the selection is empty by default
    private int mCheckedId = -1;
    // tracks children radio buttons checked state
    private CompoundButton.OnCheckedChangeListener mChildOnCheckedChangeListener;
    // when true, mOnCheckedChangeListener discards events
    private boolean mProtectFromCheckedChange = false;
    private OnCheckedChangeListener mOnCheckedChangeListener;
    private PassThroughHierarchyChangeListener mPassThroughListener;

    public FlowRadioGroup(Context context) {
        this(context, null);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 1);
    }

    public FlowRadioGroup(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray a = context.obtainStyledAttributes(attrs, FLOW_RADIOGROUP_ATTRS);
        mColumuNum = a.getInt(COLUMU_INDEX, DEFAULT_COLUMU_NUM);
        int value = a.getResourceId(CHECKED_BUTTON_INDEX, View.NO_ID);
        if (value != View.NO_ID) {
            mCheckedId = value;
        }
        a.recycle();
        init();
    }

    private void init() {
        if (DEBUG) {
//            Log.d(TAG, "init column:" + mColumuNum);
        }
        mChildOnCheckedChangeListener = new CheckedStateTracker();
        mPassThroughListener = new PassThroughHierarchyChangeListener();
        super.setOnHierarchyChangeListener(mPassThroughListener);
    }

    @Override
    public void setOnHierarchyChangeListener(OnHierarchyChangeListener listener) {
        mPassThroughListener.mOnHierarchyChangeListener = listener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        // checks the appropriate radio button as requested in the XML file
        if (mCheckedId != -1) {
            mProtectFromCheckedChange = true;
            setCheckedStateForView(mCheckedId, true);
            mProtectFromCheckedChange = false;
            setCheckedId(mCheckedId);
        }
    }

    @Override
    public void addView(View child, int index, LayoutParams params) {
        if (child instanceof RadioButton) {
            final FlowRadioButton button = (FlowRadioButton) child;
            if (button.isChecked()) {
                mProtectFromCheckedChange = true;
                if (mCheckedId != -1) {
                    setCheckedStateForView(mCheckedId, false);
                }
                mProtectFromCheckedChange = false;
                setCheckedId(button.getId());
            }
        }

        super.addView(child, index, params);
    }

    /**
     * <p>Sets the selection to the radio button whose identifier is passed in
     * parameter. Using -1 as the selection identifier clears the selection;
     * such an operation is equivalent to invoking {@link #clearCheck()}.</p>
     *
     * @param id the unique id of the radio button to select in this group
     * @see #getCheckedRadioButtonId()
     * @see #clearCheck()
     */
    public void check(int id) {
        // don't even bother
        if (id != -1 && (id == mCheckedId)) {
            return;
        }

        if (mCheckedId != -1) {
            setCheckedStateForView(mCheckedId, false);
        }

        if (id != -1) {
            setCheckedStateForView(id, true);
        }

        setCheckedId(id);
    }

    /**
     * <p>Returns the identifier of the selected radio button in this group.
     * Upon empty selection, the returned value is -1.</p>
     *
     * @return the unique id of the selected radio button in this group
     *
     * @attr ref android.R.styleable#RadioGroup_checkedButton
     * @see #check(int)
     * @see #clearCheck()
     */
    public int getCheckedRadioButtonId() {
        return mCheckedId;
    }

    /**
     * <p>Clears the selection. When the selection is cleared, no radio button
     * in this group is selected and {@link #getCheckedRadioButtonId()} returns
     * null.</p>
     *
     * @see #check(int)
     * @see #getCheckedRadioButtonId()
     */
    public void clearCheck() {
        check(-1);
    }

    private void setCheckedId(int id) {
        mCheckedId = id;
        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, mCheckedId);
        }
    }

    private void setCheckedStateForView(int viewId, boolean checked) {
        View checkedView = findViewById(viewId);
        if (checkedView != null && checkedView instanceof RadioButton) {
            ((RadioButton) checkedView).setChecked(checked);
        }
    }

    public void setColunmNumber(int number) {
        if (mColumuNum != number) {
            this.mColumuNum = number;
            requestLayout();
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int parentDesireWidth = 0;
        int parentDesireHeight = 0;
        int maxChlidWidthWithMargin = 0;
        int maxChlidHeightWithMargin = 0;


        int childCount = getChildCount();
        if (childCount > 0) {

            if (mColumuNum <= 0) {
                //auto fix

            } else {
                int row = (int) Math.ceil(childCount * 1.0f / mColumuNum);
                if (DEBUG) {
                    StringBuilder str = new StringBuilder();
                    str.append("button num:" + childCount);
                    str.append("column num:" + mColumuNum);
                    str.append("row num:" + row);
//                    Log.d(TAG, str.toString());
                }
                MarginLayoutParams mlp;
                View child;
                for (int i = 0; i < childCount; i++) {
                    child = getChildAt(i);
                    mlp = (MarginLayoutParams) child.getLayoutParams();
                    //measure child  taking into  account both the MeasureSpec requirements for this view and its padding and margins.
                    measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                    //需要把margin计算在内
                    maxChlidWidthWithMargin = Math.max(maxChlidWidthWithMargin, child.getMeasuredWidth() + mlp.leftMargin + mlp.rightMargin);
                    maxChlidHeightWithMargin = Math.max(maxChlidHeightWithMargin, child.getMeasuredHeight() + mlp.bottomMargin + mlp.topMargin);
                }
//                int forceWidthSpec = MeasureSpec.makeMeasureSpec(maxChlidWidthWithMargin, MeasureSpec.EXACTLY);
//                int forceHeightSpec = MeasureSpec.makeMeasureSpec(maxChlidHeightWithMargin, MeasureSpec.EXACTLY);
//                if (DEBUG) {
//                    Log.d(TAG, new StringBuilder().append("maxChlidWidth:" + maxChlidWidthWithMargin)
//                            .append("maxChlidHeight:" + maxChlidHeightWithMargin).toString());
//                }
                for (int i = 0; i < childCount; i++) {
                    child = getChildAt(i);
                    mlp = (MarginLayoutParams) child.getLayoutParams();
                    //为了视觉上更好看，强制每个RadioButton大小一致
                    child.measure(MeasureSpec.makeMeasureSpec(maxChlidWidthWithMargin - mlp.rightMargin - mlp.leftMargin, MeasureSpec.EXACTLY),
                            MeasureSpec.makeMeasureSpec(maxChlidHeightWithMargin - mlp.topMargin - mlp.leftMargin, MeasureSpec.EXACTLY));
                }
                parentDesireWidth = getPaddingLeft() + getPaddingRight() + maxChlidWidthWithMargin * mColumuNum;
                parentDesireHeight = getPaddingBottom() + getPaddingTop() + maxChlidHeightWithMargin * row;
                parentDesireWidth = Math.max(parentDesireWidth, getSuggestedMinimumWidth());
                parentDesireHeight = Math.max(parentDesireHeight, getSuggestedMinimumHeight());
            }//end else
        }
//        if (DEBUG)
//            Log.d(TAG, new StringBuilder().append("parentDesireWidth:" + parentDesireWidth)
//                    .append("parentDesireHeight:" + parentDesireHeight).toString());
        //测量ViewGroup大小
        setMeasuredDimension(resolveSize(parentDesireWidth, widthMeasureSpec), resolveSize(parentDesireHeight, heightMeasureSpec));
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        if (DEBUG)
//            Log.d(TAG, new StringBuilder().append("onLayout l:" + l).append("\nt:" + t).append("\nr:" + r).append("\nb:" + b).toString());
        int height = 0;
        int rowHeight = 0;
        View child;
        int childCount = getChildCount();
        int leftStartOffest = getPaddingLeft();
        int topStartOffest = 0;
        MarginLayoutParams mlp;
        if (childCount <= 0) {
            return;
        }
        if (mColumuNum > 0) {
//            if (DEBUG)
//                Log.d(TAG, new StringBuilder().append("parent width:" + getMeasuredWidth())
//                        .append("\nparent height:" + getMeasuredHeight()).toString());
            for (int i = 0; i < childCount; i++) {
                child = getChildAt(i);
                mlp = (MarginLayoutParams) child.getLayoutParams();
                leftStartOffest += mlp.leftMargin;
                topStartOffest = getPaddingTop() + height + mlp.topMargin;
//                if (DEBUG)
//                    Log.d(TAG, new StringBuilder().append("child index:" + i)
//                            .append("\nwidth:" + child.getMeasuredWidth())
//                            .append("\nheight:" + child.getMeasuredHeight())
//                            .append("\nleft:" + leftStartOffest)
//                            .append("\ntop:" + topStartOffest).toString());

                child.layout(leftStartOffest, topStartOffest, leftStartOffest + child.getMeasuredWidth(),
                        topStartOffest + child.getMeasuredHeight());
                //计算当前行最大高度
                rowHeight = Math.max(child.getMeasuredHeight() + mlp.topMargin + mlp.bottomMargin, rowHeight);

                if ((i + 1) % mColumuNum == 0) {
                    leftStartOffest = getPaddingLeft();
                    height += rowHeight;
                    rowHeight = 0;
                } else {
                    leftStartOffest += child.getMeasuredWidth() + mlp.rightMargin;
                }
            }
        } else {

        }


    }


    /**
     * <p>Register a callback to be invoked when the checked radio button
     * changes in this group.</p>
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected boolean checkLayoutParams(LayoutParams p) {
        return p instanceof MarginLayoutParams;
    }

    @Override
    protected MarginLayoutParams generateDefaultLayoutParams() {
        return new MarginLayoutParams(MarginLayoutParams.WRAP_CONTENT,
                MarginLayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(LayoutParams p) {
        return new MarginLayoutParams(p);
    }

    /**
     * <p>Interface definition for a callback to be invoked when the checked
     * radio button changed in this group.</p>
     */
    public interface OnCheckedChangeListener {
        /**
         * <p>Called when the checked radio button has changed. When the
         * selection is cleared, checkedId is -1.</p>
         *
         * @param group     the group in which the checked radio button has changed
         * @param checkedId the unique identifier of the newly checked radio button
         */
        public void onCheckedChanged(FlowRadioGroup group, int checkedId);
    }

    private class CheckedStateTracker implements CompoundButton.OnCheckedChangeListener {
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            // prevents from infinite recursion
            if (mProtectFromCheckedChange) {
                return;
            }
            mProtectFromCheckedChange = true;
            if (mCheckedId != -1) {
                setCheckedStateForView(mCheckedId, false);
            }
            mProtectFromCheckedChange = false;

            int id = buttonView.getId();
            setCheckedId(id);
        }
    }

    /**
     * <p>A pass-through listener acts upon the events and dispatches them
     * to another listener. This allows the table layout to set its own internal
     * hierarchy change listener without preventing the user to setup his.</p>
     */
    private class PassThroughHierarchyChangeListener implements
            OnHierarchyChangeListener {
        private OnHierarchyChangeListener mOnHierarchyChangeListener;

        @Override
        public void onChildViewAdded(View parent, View child) {
            if (parent == FlowRadioGroup.this && child instanceof RadioButton) {
                int id = child.getId();
                // generates an id if it's missing
                if (id == View.NO_ID) {
//                    id = View.generateViewId();
                    id = child.hashCode();
                    child.setId(id);
                }
                ((FlowRadioButton) child).setOnCheckedChangeWidgetListener(
                        mChildOnCheckedChangeListener);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewAdded(parent, child);
            }
        }

        @Override
        public void onChildViewRemoved(View parent, View child) {
            if (parent == FlowRadioGroup.this && child instanceof FlowRadioButton) {
                ((FlowRadioButton) child).setOnCheckedChangeWidgetListener(null);
            }
            if (mOnHierarchyChangeListener != null) {
                mOnHierarchyChangeListener.onChildViewRemoved(parent, child);
            }
        }

    }


}
