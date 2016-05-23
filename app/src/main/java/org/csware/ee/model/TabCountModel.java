package org.csware.ee.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Administrator on 2016/4/28.
 */
public class TabCountModel extends Return {


    public TypeCountEntity TypeCount;

    public static class TypeCountEntity {
        @SerializedName("new")
        public int newX;
        public int shipping;
        public int done;
    }
}
