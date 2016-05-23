package org.csware.ee.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/8/18.
 */
public class BankInfo implements Serializable {

    int Id;
    public int BankResId;
    public String bankcode;
    String Name;
    public String dayLimit;
    public String singleLimit;

    public String getDayLimit() {
        return dayLimit;
    }

    public void setDayLimit(String dayLimit) {
        this.dayLimit = dayLimit;
    }

    public String getSingleLimit() {
        return singleLimit;
    }

    public void setSingleLimit(String singleLimit) {
        this.singleLimit = singleLimit;
    }

    public int getBankResId() {
        return BankResId;
    }

    public void setBankResId(int bankResId) {
        BankResId = bankResId;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public String getCardNo() {
        return CardNo;
    }

    public void setCardNo(String cardNo) {
        CardNo = cardNo;
    }

    public String getBankAddress() {
        return BankAddress;
    }

    public void setBankAddress(String bankAddress) {
        BankAddress = bankAddress;
    }

    public boolean isDefault() {
        return IsDefault;
    }

    public void setIsDefault(boolean isDefault) {
        IsDefault = isDefault;
    }

    String BankName;
    String BankAddress;
    String CardNo;
    boolean IsDefault;




}
