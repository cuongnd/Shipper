package org.csware.ee.model;

import java.util.List;

/**
 * 右上角的提示提示内容缓存
 * Created by Yu on 2015/7/10.
 */
public class BadgerInfo {



    /**盖显示的资源，如果存在则显示，显示完后移除*/
    public List<Badger> badgers;






    public static class Badger{
        /**资源Id*/
        public int resId;

        /**右上角的提示信息，可以是数字也可以是文本等*/
        public String tip;

        /**文本颜色，大于0时即显示  Color.BLUE*/
        public int txtColor;
/**大于0时即显示  Color.BLUE*/
        public int bgColor;
        /**文本大小*/
        public int textSize;

    }

}
