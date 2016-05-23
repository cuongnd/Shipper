package org.csware.ee.model;

import net.tsz.afinal.annotation.sqlite.Id;

import java.util.Date;

/**
 * Created by Atwind on 2015/5/14.
 * 用户相关属性
 */
public class User {

    private int userId;
    private String name;
    private String token;
    private String phoneNum;
    private String password;


    /////////////getter and setter 不能省略哦///////////////
    public int getUserId() {
        return userId;
    }
    public void setUserId(int userId) {
        this.userId = userId;
    }
    public String getToken(){
        return token;
    }
    public void setToken(String token){
        this.token = token;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password = password;
    }
    public String getPhoneNum(){
        return phoneNum;
    }
    public void setPhoneNum(String phoneNum){
        this.phoneNum = phoneNum;
    }
    /*
    * 获取用户名称
    * */
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }






}
