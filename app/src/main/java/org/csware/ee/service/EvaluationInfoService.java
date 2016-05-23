package org.csware.ee.service;


import org.csware.ee.model.EvaluationInfo;
import org.csware.ee.utils.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arche on 2015/8/27.
 */
public class EvaluationInfoService {

    public static List<EvaluationInfo> getEvaluationInfoService(String json) {
        List<EvaluationInfo> list = new ArrayList<>();
        JSONArray jsonArray = Tools.getJsonArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                EvaluationInfo info = new EvaluationInfo();
                info.setName(jsonObject.getString("Name"));
                info.setStar(jsonObject.getDouble("Star"));
                String msg = jsonObject.getString("Message");
                try {
                     msg = URLDecoder.decode(msg,"UTF-8");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                info.setMessage(msg);
                info.setTime(jsonObject.getString("Time"));
                info.setUserId(jsonObject.getInt("UserId"));
                info.setOrderId(jsonObject.getInt("OrderId"));
                list.add(info);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }






}
