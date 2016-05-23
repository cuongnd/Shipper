package org.csware.ee.model;

import org.csware.ee.component.IJsonResult;

/**
 * Created by Atwind on 2015/5/18.
 * JSON 返回代码
 */
public class Return {

    public void notify(IJsonResult json) {
        if (Result == 0) {
            json.ok(this);
        } else {
            json.error(this);
        }
    }

    /**Json格式与期望不符，可能要提示客主端*/
    public boolean isError() {
        return Result != 0;
    }


    public int Result;

    public String Message;
    public String Validator;
    public String RawMessage;

}
