package org.csware.ee.component;

/**
 *  HttpAjax 请求的回调
 * Created by Yu on 2015/8/7.
 */
public interface IAjaxResult {


    /** 异步通知
     * @param result 结果false表示没有成功返回
     * @param res  Http的Response ,如果成功获得的话
     */
    void notify(boolean result,String res);

}
