package org.csware.ee.component;

import android.support.v4.app.Fragment;

/**
 * Fragment相关操作的接口
 * Created by Yu on 2015/7/29.
 */
public interface IFragment {

    /**
     * 替换操作
     */
    void replace(Fragment fragment, String title);
}
