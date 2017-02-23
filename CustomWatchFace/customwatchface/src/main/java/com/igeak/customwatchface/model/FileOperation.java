package com.igeak.customwatchface.model;

import com.google.gson.Gson;
import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.FileUtil;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqiang on 16-5-19.
 */
public class FileOperation {


    /**
     * 外置表盘文件夹
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
     * 获取存储表盘数据的文件夹列表
     *
     * @return
     * @throws Exception
     */
    public static File[] getWatchFaceFileList() throws Exception {
        File[] watchFaceFiles;
        watchFaceFiles = getCustomWatchfacesFolder().listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                return pathname.isDirectory();
            }
        });
        return watchFaceFiles;
    }


    /**
     * 获取存储表盘数据的文件夹
     *
     * @return
     * @throws Exception
     */
    public static File getWatchFaceFile(final String watchname) throws Exception {
        File[] watchFaceFiles;
        FilenameFilter filenameFilter = new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.equals(watchname);
            }
        };

        watchFaceFiles = getCustomWatchfacesFolder().listFiles(filenameFilter);
        if (watchFaceFiles.length > 0) {
            return watchFaceFiles[0];
        } else {
            return null;
        }

    }


    /**
     * 获取 javabean列表
     *
     * @return
     * @throws Exception
     */
    public static List<WatchFaceBean> getWatchFaceBeanList()
            throws Exception {
        List<WatchFaceBean> watchFaceBeanList = new ArrayList<WatchFaceBean>();
        File[] watchFaceFiles = getWatchFaceFileList();
        for (File watchFace : watchFaceFiles) {
            File[] jsonFiles = watchFace.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(Const.JSON_EXNAME);
                }
            });
            File jsonFile = jsonFiles[0];
            if (jsonFile != null) {
                watchFaceBeanList.add(json2JavaBean(jsonFile));
            }
        }
        return watchFaceBeanList;
    }

    /**
     * jsonFile转换成javaBean
     *
     * @param jsonFile
     * @return
     * @throws IOException
     */
    public static WatchFaceBean json2JavaBean(File jsonFile) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
                new FileInputStream(jsonFile)));
        Gson gson = new Gson();
        WatchFaceBean watchFaceBean = gson.fromJson(bufferedReader, WatchFaceBean.class);
        bufferedReader.close();
        return watchFaceBean;
    }


    public static void javaBean2Json(WatchFaceBean bean, File jsonFile) throws
            IOException {
        if (jsonFile.exists()) {
            jsonFile.delete();
            jsonFile.createNewFile();
        }
        if (jsonFile.canWrite()) {
            FileWriter fw = new FileWriter(jsonFile);
            BufferedWriter bw = new BufferedWriter(fw);
            Gson gson = new Gson();
            gson.toJson(bean, bw);
            bw.close();
            fw.close();
        }
    }


    /**
     * 获取表盘元素的InputStream
     *
     * @param faceItem    表盘名
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static InputStream getWatchFaceElementStream(final String faceItem, String
            faceElement) throws Exception {

        return new BufferedInputStream(new FileInputStream(getWatchFaceElementFile(faceItem,
                faceElement)));
    }


    /**
     * 获取表盘元素的文件
     *
     * @param faceItem    表盘名
     * @param faceElement 表盘元素
     * @return File
     * @throws Exception
     */
    public static File getWatchFaceElementFile(final String faceItem, String
            faceElement) throws Exception {

        File drawableFile = null;
        File faceDir;
        File[] faceDirs;

        faceDirs = getCustomWatchfacesFolder().listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String filename) {
                return filename.equals(faceItem);
            }
        });

        faceDir = faceDirs[0];
        if (faceDir != null && faceDir.exists()) {
            drawableFile = new File(faceDir, faceElement + Const.PNG_EXNAME);
        }
        return drawableFile;
    }


    /**
     * 将需要发送的表盘打包
     *
     * @param watchFaceName
     * @return
     * @throws Exception
     */
    public static byte[] zipFolder(final String watchFaceName) throws Exception {
        File watchFolder = getWatchFaceFile(watchFaceName);
        FileUtil.zipFolder(watchFolder, getZipFilePath());
        return FileUtil.file2Byte(getZipFilePath());
    }


    public static String getZipFilePath() {
        return new StringBuilder().append(FileUtil.getExternalStoragePath())
                .append(File.separator)
                .append(Const.FOLDER_NAME)
                .append(File.separator)
                .append(Const.ZIPFILE_NAME)
                .toString();
    }


    public static void deleteFolder(String watchFaceName) throws Exception {
        File watchFolder = getWatchFaceFile(watchFaceName);
        FileUtil.deleteDir(watchFolder);
    }


    public static void changeWatchName(WatchFaceBean watchFaceBean, String targetName) throws
            Exception {
        String oriName = watchFaceBean.getName();
        File watchFolder = getWatchFaceFile(oriName);
        File[] files = null;
        if (watchFolder != null && watchFolder.isDirectory()) {
            files = watchFolder.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename.endsWith(Const.JSON_EXNAME);
                }
            });
        }
        File jsonFile = null;
        if (files.length > 0) {
            jsonFile = files[0];
        }
        watchFaceBean.setName(targetName);
        javaBean2Json(watchFaceBean, jsonFile);
        FileUtil.changeDirName(watchFolder, targetName);
    }
}
