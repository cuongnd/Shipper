package org.csware.ee.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;

/**
 * 资源查找帮助
 * Created by Yu on 2015/7/2.
 */
public class ResourceHelper {

    /**
     * 通过两个配置的数组源中查找图片资源
     */
    public static Drawable getByName(Context context, int arrFromResId, int arrToResId, String fromName) {
        String[] arrFrom = context.getResources().getStringArray(arrFromResId);
        String[] arrTo = context.getResources().getStringArray(arrToResId);
        int index = 0;
        for (String item : arrFrom) {
//            if (item.equals(fromName))
            if (fromName.contains(item))
                break;
            index++;
        }
        int iconId = context.getResources().getIdentifier(arrTo[index], "drawable", context.getPackageName());
        return context.getResources().getDrawable(iconId);
    }

    public static Drawable getByName(Context context, String[] arrFrom, String[] arrTo, String fromName) {
        int index = 0;
        for (String item : arrFrom) {
            if (item.equals(fromName))
                break;
            index++;
        }
        int iconId = context.getResources().getIdentifier(arrTo[index], "drawable", context.getPackageName());
        return context.getResources().getDrawable(iconId);
    }

    public static Drawable getByIconName(Context context, String iconName) {
        int iconId = context.getResources().getIdentifier(iconName, "drawable", context.getPackageName());
        return context.getResources().getDrawable(iconId);
    }


}
