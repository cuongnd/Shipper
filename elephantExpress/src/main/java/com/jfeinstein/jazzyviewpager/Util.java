package com.jfeinstein.jazzyviewpager;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * 辅助类
* */
public class Util {

    public static int dpToPx(Resources res, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, res.getDisplayMetrics());
    }

    public static String[] effects = {"Standard", "Tablet", "CubeIn", "CubeOut", "FlipVertical", "FlipHorizontal", "Stack", "ZoomIn", "ZoomOut", "RotateUp", "RotateDown", "Accordion" };

}
