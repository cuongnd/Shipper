package org.csware.ee.model;

import org.csware.ee.Guard;
import org.csware.ee.utils.AppStatus;
import org.csware.ee.utils.ClientStatus;

/**
 * Created by Atwind on 2015/5/14.
 * 货主相关信息
 * 赋值TOKEN与DEVICEID时交于环境变量
 */
public class Shipper {

    /**
     * 服务器当前时间
     * */
    public long ServerTime;

    /**
     * 服务器时间与手机本地时间差
     * */
    public long unitTimeLag;

    public int userId;

    /**
     * 是否第一次修改身份信息
     * */
    public boolean isFirstCg = true;
    /**
     * 默认支付方式状态
     * */
    public boolean havedPayMethod = false;
    /**
     * 默认支付方式
     * */
    public String payment;
    /**
     * 支付cardId
     * */
    public int cardid = -1;
    /**
     * 手机号
     */
    public String phone;

    /**
     * 交换Key，每次加密时都会用到
     */
    String token;

    /**
     * 设备Id号
     */
    String deviceId;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        if (!Guard.isNullOrEmpty(token))
            ClientStatus.setToken(token);
        this.token = token;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        if (!Guard.isNullOrEmpty(deviceId))
            AppStatus.setDeviceId(deviceId);
        this.deviceId = deviceId;
    }
}
