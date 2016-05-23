package org.csware.ee.component;

/**
 * 上传到云成功后，通过本地接口进行相关参数变更
 * Created by Yu on 2015/7/21.
 */
public interface IASyncQCloudResult {


    /**
     * @param result   上传到Q云的结果
     * @param urlOrPath     成功上传后返回的URL地址 或 成功下载后的本地路径
     */
    void notify(boolean result,String urlOrPath);

}
