package com.igeak.customwatchface.util;

import android.content.Context;
import android.os.Environment;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by xuqiang on 16-5-24.
 */
public class FileUtil {


    /**
     * 压缩文件,文件夹
     *
     * @param srcFile     要压缩的文件/文件夹
     * @param zipFilePath 指定压缩的目的和名字
     * @throws Exception
     */
    public static void zipFolder(File srcFile, String zipFilePath) throws Exception {
        //创建Zip包
        ZipOutputStream outZip =
                new ZipOutputStream(new FileOutputStream(zipFilePath));
        //压缩
        zipFiles(srcFile, outZip);
        //完成,关闭
        outZip.finish();
        outZip.close();
    }//end of func


    /**
     * 压缩文件
     *
     * @param file   原文件夹
     * @param zipOut 输出的ZIP文件
     * @throws Exception
     */
    private static void zipFiles(File file, ZipOutputStream zipOut) throws Exception {
        if (zipOut == null) {
            return;
        }

        //判断是不是文件
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(file.getName());
            FileInputStream inputStream = new FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            //inputstream 到outputStream的惯常写法，定义一个缓存，循环的读和写，这样比aviliable效率高
            int len;
            byte[] buffer = new byte[4096];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }

            zipOut.closeEntry();
        } else {
            //文件夹的方式,获取文件夹下的子文件
            File[] fileList = file.listFiles();

            //如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry =
                        new ZipEntry(new StringBuilder()
                                .append(file.getName())
                                .append(File.separator)
                                .toString());
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }
            //如果有子文件, 遍历子文件
            for (File srcfile : fileList) {
                zipFiles(srcfile, zipOut);
            }
        }//end of if

    }//end of func


    /**
     * 压缩文件,文件夹
     *
     * @param srcFilePath 要压缩的文件/文件夹名字
     * @param zipFilePath 指定压缩的目的和名字
     * @throws Exception
     */
    public static void zipFolder(String srcFilePath, String zipFilePath) throws Exception {
        //创建Zip包
        ZipOutputStream outZip =
                new ZipOutputStream(new FileOutputStream(zipFilePath));

        //打开要输出的文件
        File file = new File(srcFilePath);

        //压缩
        zipFiles(file.getParent() + File.separator, file.getName(), outZip);

        //完成,关闭
        outZip.finish();
        outZip.close();

    }//end of func


    /**
     * 压缩文件
     *
     * @param Path     文件夹
     * @param filename 文件名
     * @param zipOut   输出的ZIP文件
     * @throws Exception
     */
    private static void zipFiles(String Path, String filename,
                                 ZipOutputStream zipOut) throws Exception {
        if (zipOut == null) {
            return;
        }

        File file = new File(Path, filename);

        //判断是不是文件
        if (file.isFile()) {
            ZipEntry zipEntry = new ZipEntry(filename);
            FileInputStream inputStream = new FileInputStream(file);
            zipOut.putNextEntry(zipEntry);

            //inputstream 到outputStream的惯常写法，定义一个缓存，循环的读和写，这样比aviliable效率高
            int len;
            byte[] buffer = new byte[4096];

            while ((len = inputStream.read(buffer)) != -1) {
                zipOut.write(buffer, 0, len);
            }

            zipOut.closeEntry();
        } else {
            //文件夹的方式,获取文件夹下的子文件
            String fileList[] = file.list();

            //如果没有子文件, 则添加进去即可
            if (fileList.length <= 0) {
                ZipEntry zipEntry =
                        new ZipEntry(new StringBuilder()
                                .append(filename)
                                .append(File.separator)
                                .toString());
                zipOut.putNextEntry(zipEntry);
                zipOut.closeEntry();
            }

            //如果有子文件, 遍历子文件
            for (int i = 0; i < fileList.length; i++) {
                zipFiles(Path
                        , new StringBuilder()
                                .append(filename)
                                .append(File.separator)
                                .append(fileList[i]).toString()
                        , zipOut);
            }//end of for

        }//end of if

    }//end of func


    /**
     * 读取本地文件中字符串
     *
     * @param fileName
     * @return
     */
    public static String file2String(String fileName) throws Exception {

        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bf = new BufferedReader(new InputStreamReader(
                new FileInputStream(fileName)));
        String line;
        while ((line = bf.readLine()) != null) {
            stringBuilder.append(line);
        }

        return stringBuilder.toString();
    }


    public static File jsonObject2File(JSONObject jsonObject, String filename) throws Exception {
        String json = jsonObject.toString();

        File file = new File(filename);
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(json);
        fileWriter.flush();
        fileWriter.close();
        return file;
    }


    public static byte[] file2Byte(String filename) throws Exception {
        FileInputStream fis = new FileInputStream(filename);
        byte[] bis = new byte[fis.available()];
        fis.read(bis);
        fis.close();
        return bis;

    }

    public static String getExternalStoragePath() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state)) {

            return Environment.getExternalStorageDirectory()
                    .getPath();

        }
        return null;
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


    /**
     * 递归删除目录下的所有文件及子目录下所有文件
     *
     * @param dir 将要删除的文件目录
     * @return boolean Returns "true" if all deletions were successful.
     * If a deletion fails, the method stops attempting to
     * delete and returns "false".
     */
    public static boolean deleteDir(File dir) {
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }

    public static void changeDirName(File watchFolder, String tarName) {
        watchFolder.renameTo(new File(watchFolder.getParent(), tarName));
    }

    public static int copy(InputStream input, OutputStream output) throws IOException {

        int IO_BUFFER_SIZE = 1024;
        byte[] buffer = new byte[IO_BUFFER_SIZE];

        int count = 0;

        int n = 0;

        while (-1 != (n = input.read(buffer))) {

            output.write(buffer, 0, n);

            count += n;

        }

        return count;
    }


}
