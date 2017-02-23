package com.igeak.customwatchface.util;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqiang on 16-5-18.
 */
public class JsonUtil {

    public static List<JSONObject> getJsonArray(String json) throws JSONException {

        JSONTokener jsonParser = new JSONTokener(json);
        JSONObject object = null;
        List<JSONObject> objectList = new ArrayList<JSONObject>();
        object = (JSONObject) jsonParser.nextValue();
        JSONArray jsonArray = object.getJSONArray("watchlist");
        for (int i = 0; i < jsonArray.length(); i++) {
            objectList.add(jsonArray.getJSONObject(i));
        }


        return objectList;

    }

    public static JSONObject string2Json(String json) throws JSONException {
        JSONTokener jsonParser = new JSONTokener(json);
        JSONObject object = null;
        object = (JSONObject) jsonParser.nextValue();
        return object;
    }


}
