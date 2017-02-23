package com.igeak.customwatchface;

import android.content.Context;
import android.graphics.Bitmap;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.InputStreamReader;

/**
 * Created by xuqiang on 16-6-1.
 */
public class FileOperation {


    /**
     * 将需要发送的表盘先复制到T卡
     *
     * @param context
     * @throws Exception
     */
    public static void assert2Folder(Context context) throws Exception {
        String assetsPath = Const.FOLDER_NAME;
        String filePath = FileOperation.getCustomWatchfacesFolderPath();
        FileUtil.assets2Files(context,assetsPath,filePath);

    }


    /**
     * 获取 javabean
     * @return
     * @throws Exception
     */
    public static WatchFaceBean getWatchFaceBean()
            throws Exception {
        File watchface = getCustomWatchfacesFolder();

            File[] jsonfiles = watchface.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(Const.JSON_EXNAME);
                }
            });
            File jsonfile = jsonfiles[0];

        return jsonToJavaBean(jsonfile);
    }


    public static WatchFaceBean jsonToJavaBean(File jsonfile) throws FileNotFoundException {
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                new FileInputStream(jsonfile)));
        Gson gson = new Gson();
        WatchFaceBean watchFaceBean = gson.fromJson(bf, WatchFaceBean.class);
        return watchFaceBean;
    }

    /**
     * 外置表盘
     *
     * @return sdcard/custom_watchface/
     */
    public static File getCustomWatchfacesFolder() throws Exception {
        return new File(getCustomWatchfacesFolderPath());
    }

    /**
     * 外置表盘位置
     *
     * @return sdcard/custom_watchface/
     */
    public static String getCustomWatchfacesFolderPath() throws Exception {
        String filepath = new StringBuffer()
                .append(FileUtil.getExternalStoragePath())
                .append(File.separator)
                .append(Const.FOLDER_NAME)
                .toString();

        return filepath;
    }



    /**
     * 获取表盘元素的drawable
     *
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static Bitmap getWatchfacesElementImg(String
            faceElement) throws Exception {

        return PicUtil.file2Bitmap(getWatchfacesElementFile(faceElement));
    }

    /**
     * 获取表盘元素的drawable
     *
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static File getWatchfacesElementFile(String
            faceElement) throws Exception {

        File drawableFile = null;
        File facedir = getCustomWatchfacesFolder();

        if (facedir != null && facedir.exists()) {
            drawableFile = new File(facedir, faceElement + Const.PNG_EXNAME);
        }
        return drawableFile;
    }


}
