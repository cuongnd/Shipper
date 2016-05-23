package org.csware.ee.model;

import java.io.Serializable;

/**
 * Created by Arche on 2015/8/27.
 */
public class EvaluationInfo implements Serializable{

    String Name;
    double Star;
    String Message;
    String Time;
    int OrderId;
    int UserId;


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getStar() {
        return Star;
    }

    public void setStar(double star) {
        Star = star;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getTime() {
        return Time;
    }

    public void setTime(String time) {
        Time = time;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }




}
