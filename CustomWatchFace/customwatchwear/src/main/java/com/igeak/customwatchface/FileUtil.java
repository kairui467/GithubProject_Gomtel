package com.igeak.customwatchface;

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by xuqiang on 16-5-23.
 */
public class FileUtil {

    public static File getFileFromBytes(byte[] bytes) throws IOException {
        String filename = Const.ZIPFILE_NAME;
        String folderstring = new StringBuffer()
                .append(getExternalStoragePath())
                .append(File.separator)
                .append(Const.FOLDER_NAME)
                .toString();

        File file = null;
        file = new File(folderstring);
        if (!file.exists()) {
            file.mkdirs();
        }
        file = new File(folderstring, filename);
        if (!file.exists()) {
            file.createNewFile();
        }
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(bytes);
        fos.close();

        return file;
    }

    public static String getExternalStoragePath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            return Environment.getExternalStorageDirectory()
                    .getPath();

        }
        return null;
    }

    public static void unZipFile(File file) throws IOException {
        ZipInputStream inZip = null;
        try {
            String path = file.getParent();
            inZip = new ZipInputStream(new FileInputStream(file));
            ZipEntry zipEntry;
            while ((zipEntry = inZip.getNextEntry()) != null) {
                String filename = zipEntry.getName();
                File newFile = new File(path, filename);
                FileOutputStream output = null;
                try {
                    output = new FileOutputStream(newFile);
                    int len = 0;
                    byte[] buffer = new byte[4096];
                    while ((len = inZip.read(buffer)) > 0) {
                        output.write(buffer, 0, len);
                    }
                } finally {
                    // we must always close the output file
                    if (output != null) {
                        output.close();
                    }
                }
            }
        } finally {
            if (inZip != null) {
                inZip.close();
            }
        }
    }


    /**
     * 从assets目录中复制整个文件夹内容
     *
     * @param context Context 使用CopyFiles类的Activity
     * @param oldPath String  原文件路径  如：/aa
     * @param newPath String  复制后路径  如：xx:/bb/cc
     */
    public static void assets2Files(Context context, String oldPath, String newPath) throws
            Exception {
        String fileNames[] = context.getAssets().list(oldPath);//获取assets目录下的所有文件及目录名
        if (fileNames.length > 0) {//如果是目录
            File file = new File(newPath);
            file.mkdirs();//如果文件夹不存在，则递归
            for (String fileName : fileNames) {
                assets2Files(context, oldPath + "/" + fileName, newPath + "/" + fileName);
            }
        } else {//如果是文件
            InputStream is = context.getAssets().open(oldPath);
            FileOutputStream fos = new FileOutputStream(new File(newPath));
            byte[] buffer = new byte[1024];
            int byteCount = 0;
            while ((byteCount = is.read(buffer)) != -1) {//循环从输入流读取 buffer字节
                fos.write(buffer, 0, byteCount);//将读取的输入流写入到输出流
            }
            fos.flush();//刷新缓冲区
            is.close();
            fos.close();
        }

    }


}
