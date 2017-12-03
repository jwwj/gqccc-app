package com.guessmusic.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by len on 2017/8/29.
 */

public class JsonUtil {
    //将str转换成JSONObjct格式
    public static JSONObject fromString(String str){
        //  String str = "{\"result\":\"success\",\"message\":\"成功！\"}";
        JSONObject json = null;
        try {
            json = new JSONObject(str);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.i("json",json.toString());
        return json;
    }
}
