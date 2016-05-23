package org.csware.ee.service;


import org.csware.ee.model.MessageInfo;
import org.csware.ee.utils.Tools;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Arche on 2015/9/16.
 */
public class MessageInfoService {

    public static List<MessageInfo> getMessageInfoService(String json) {
        List<MessageInfo> list = new ArrayList<>();
        JSONArray jsonArray = Tools.getJsonArray(json);
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);
                MessageInfo messageInfo = new MessageInfo();
                messageInfo.setId(jsonObject.getInt("Id"));
                messageInfo.setUserId(jsonObject.getInt("UserId"));
                messageInfo.setTitle(jsonObject.getString("Title"));
                messageInfo.setMessage(jsonObject.getString("Message"));
                messageInfo.setCreateTime(jsonObject.getString("CreateTime"));
                list.add(messageInfo);
            }catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return list;
    }

}
