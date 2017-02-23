package com.igeak.customwatchface.util;

import android.content.Context;

import com.google.gson.Gson;
import com.igeak.customwatchface.Bean.WatchFaceBean;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.StringReader;

/**
 * Created by xuqiang on 16-5-27.
 */
public class AssertFileUtil {
    /**
     * 读取本地文件中字符串
     *
     * @param fileName
     * @return
     */
    public static String assetsFile2String(Context context, String fileName) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                context.getAssets().open(fileName)));
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }


    public static WatchFaceBean jsonToJavaBean(String json) throws FileNotFoundException {
        BufferedReader bf = new BufferedReader(new StringReader(json));
        Gson gson = new Gson();
        WatchFaceBean watchFaceBean = gson.fromJson(bf, WatchFaceBean.class);
        return watchFaceBean;
    }


}
