package org.csware.ee.component;

import org.csware.ee.model.Return;

/**
 * 返回的Json实例
 * Created by Yu on 2015/8/7.
 */
public interface IJsonResult<T extends Return> {


    /**
     * Result == 0时 的成功解析后的Json实例
     *
     * @param json
     */
    void ok(T json);


    /**
     * Result != 0 返回的非期望结果
     *
     * @param json 当Json为null时 表示没有获取到服务端的数据
     */
    void error(Return json);

}
