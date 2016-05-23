package org.csware.ee.service;


import org.csware.ee.model.BankInfo;
import org.csware.ee.model.BankModel;
import org.csware.ee.shipper.R;
import org.csware.ee.utils.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/8/18.
 */
public class BankInfoService {

    public static List<BankInfo> getBankInfoService(String json) {
        List<BankInfo> list = new ArrayList<>();
        JSONArray jsonArray = Tools.getJsonArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                BankInfo bankInfo = new BankInfo();
                bankInfo.setBankName(jsonObject.getString("BankName"));
                bankInfo.setCardNo(jsonObject.getString("CardNo"));
                bankInfo.setId(jsonObject.getInt("Id"));
                list.add(bankInfo);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

    public static List<BankInfo> getBankData()
    {
        String[] nameArray = {"建设银行","中国银行","农业银行","交通银行","浦发银行","兴业银行","平安银行","光大银行","邮政储蓄", "渤海银行", "中信银行", "上海银行"};
        String[] dayLimitArray = {"50万","50万","10万","50万","0.5万","50万","0.5万","50万","0.5万", "50万", "50万", "5万"};
        String[] singleLimitArray = {"50万","5万","0.5万","50万","0.5万","5万","0.5万","50万","0.5万", "50万", "50万", "0.5万"};
        String[] idArray = {"01050000","01040000","01030000","03010000","03100000","03090000","03134402","03030000","04030000", "03180000", "03020000", "03130031"};
        Integer[] resArray = {R.drawable.bank_iocn_jsyh,R.mipmap.bank_iocn_zgyh,R.drawable.bank_iocn_nyyh,
                R.drawable.bank_iocn_jtyh,R.mipmap.bank_iocn_select_pfyh,R.mipmap.bank_iocn_select_xyyh,
                R.mipmap.bank_iocn_select_payh,R.drawable.bank_iocn_gdyh,R.mipmap.bank_iocn_select_yzcx,
                R.mipmap.bank_iocn_select_bhyh,R.mipmap.bank_iocn_select_zxyh,R.mipmap.bank_iocn_select_shyh};
        List<BankInfo> listItem = new ArrayList<>();
        for(int i = 0; i < nameArray.length; i++)
        {
            BankInfo bankModel = new BankInfo();
            bankModel.setBankName(nameArray[i]);
            bankModel.setBankResId(resArray[i]);
            bankModel.setBankcode(idArray[i]);
            bankModel.setDayLimit(dayLimitArray[i]);
            bankModel.setSingleLimit(singleLimitArray[i]);
//            HashMap<String,Object> map = new HashMap<String, Object>();
//            map.put("name",nameArray[i]);
//            map.put("id",idArray[i]);
            listItem.add(bankModel);
        }
        return listItem;
    }


}
