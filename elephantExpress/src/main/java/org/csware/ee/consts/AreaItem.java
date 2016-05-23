package org.csware.ee.consts;

import org.csware.ee.utils.ParamTool;

/**
 * Created by Yu on 2015/5/26.
 */
public class AreaItem {
    //初始化
    public AreaItem(int parentId, int areaId, String name) {
        this.parentId = parentId;
        this.areaId = areaId;
        this.name = name;
    }

    /**
     * 区域Id
     */
    public int areaId;
    /**
     * 区域名称
     */
    public String name;
    /**
     * 父Id，没有时为0即一级分类
     */
    public int parentId;

    /**重载toString*/
    @Override
    public String toString(){
        return ParamTool.cutString(name,5);
    }
}
