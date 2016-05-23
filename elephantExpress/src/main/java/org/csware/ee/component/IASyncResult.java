package org.csware.ee.component;

/**
 * 异步通知
 * Created by Yu on 2015/7/21.
 */
public interface IASyncResult {


    /**
     * @param result   上传到Q云的结果
     */
    void notify(boolean result);

}
