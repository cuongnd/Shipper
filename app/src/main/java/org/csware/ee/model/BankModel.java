package org.csware.ee.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2015/11/3.
 */
public class BankModel extends Return  implements Serializable {

    public int BankResId;
    public String BanksId;
    public  String BankName;
    public boolean isBindBank = false;

    public int getBankResId(Integer integer) {
        return BankResId;
    }

    public void setBankResId(int bankResId) {
        BankResId = bankResId;
    }

    public String getBanksId() {
        return BanksId;
    }

    public void setBanksId(String banksId) {
        BanksId = banksId;
    }

    public String getBankName() {
        return BankName;
    }

    public void setBankName(String bankName) {
        BankName = bankName;
    }

    public boolean isBindBank() {
        return isBindBank;
    }

    public void setIsBindBank(boolean isBindBank) {
        this.isBindBank = isBindBank;
    }
}
