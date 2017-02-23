package com.igeak.customwatchface.model;

import android.content.Context;

import com.igeak.customwatchface.Bean.WatchFaceBean;
import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.util.AssertFileUtil;
import com.igeak.customwatchface.util.FileUtil;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by xuqiang on 16-5-19.
 */
public class AssetsOperation {

    /**
     * 获取 javabean列表
     *
     * @param context
     * @return
     * @throws Exception
     */
    public static List<WatchFaceBean> getWatchFaceBeanList(Context context)
            throws Exception {
        List<WatchFaceBean> watchFaceBeanList = new ArrayList<>();
        String[] watchFaces = context.getAssets().list(Const.FOLDER_NAME);
        for (String str : watchFaces) {
            String filepath = Const.FOLDER_NAME + "/" + str + "/" + Const
                    .WATCHFACEJSON;
            String json = AssertFileUtil.assetsFile2String(context, filepath);
            WatchFaceBean watchFaceBean = AssertFileUtil.jsonToJavaBean(json);
            watchFaceBeanList.add(watchFaceBean);
        }
        return watchFaceBeanList;
    }


    /**
     * 获取表盘元素的drawable
     *
     * @param context
     * @param faceItem    表盘名
     * @param faceElement 表盘元素
     * @return
     * @throws Exception
     */
    public static InputStream getWatchFaceElementStream(Context context, final String faceItem,
                                                        String
            faceElement) throws Exception {

        String filepath = Const.FOLDER_NAME + "/" + faceItem + "/" + faceElement + Const.PNG_EXNAME;
        InputStream is = context.getAssets().open(filepath);
        return is;
    }


    /**
     * 将需要发送的表盘打包
     * assert打包的过程比较特别，需要先复制到T卡，然后打包，得到压缩后的数据后，将复制到T卡的表盘删除
     *
     * @param context
     * @param watchFaceName
     * @return
     * @throws Exception
     */
    public static byte[] zipFolder(Context context, final String
            watchFaceName) throws Exception {
        String assetsPath = Const.FOLDER_NAME + "/" + watchFaceName;
        String filePath = FileOperation.getCustomWatchfacesFolderPath() + "/" + watchFaceName;
        FileUtil.assets2Files(context, assetsPath, filePath);
        byte[] bytes = FileOperation.zipFolder(watchFaceName);
        FileOperation.deleteFolder(watchFaceName); //压缩好之后，
        return bytes;
    }

    /**
     * 将需要发送的表盘打包
     * assert打包的过程比较特别，需要先复制到T卡，然后打包
     *
     * @param context
     * @param watchFaceName
     * @throws Exception
     */
    public static void assert2Folder(Context context, final String
            watchFaceName) throws Exception {
        String assetsPath = Const.FOLDER_NAME + "/" + watchFaceName;
        String filePath = FileOperation.getCustomWatchfacesFolderPath() + "/" + watchFaceName;
        FileUtil.assets2Files(context, assetsPath, filePath);
    }

}
