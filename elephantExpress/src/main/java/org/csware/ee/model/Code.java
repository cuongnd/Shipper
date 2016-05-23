package org.csware.ee.model;

/**
 * Created by Yu on 2015/6/18.
 * <p/>
 * 不同Activity来回传递的参数识别码
 */
public enum Code {

    CANCEL("Cancel", -9),
    FAILED("Failed", -1),
    DEFAULT("Default", 0),
    OK("ok", 1),

    PAY_REQUEST("Pay_Request", 10),

    ADD("Add",11),
    EDIT("Edit",12),


    CAMERA_REQUEST("Camera_Request", 120),
    ALBUM_REQUEST("Album_Request", 101),

    IMAGE_CROP("Image_Crop", 103),

    AREA("Area", 10000),
    FROM_AREA("FromArea", 10001),
    TO_AREA("ToArea", 10002),


    COMMENT("Comment", 10005),


    ;

    // 成员变量
    private String name;
    private int index;

    // 构造方法
    Code(String name, int index) {
        this.name = name;
        this.index = index;
    }

    //覆盖方法
    @Override
    public String toString() {
        return this.index + "_" + this.name;
    }

    /**
     * 获得名称
     */
    public String toName() {
        return this.name;
    }

    /**
     * 获得int值
     */
    public int toValue() {
        return this.index;
    }


}
