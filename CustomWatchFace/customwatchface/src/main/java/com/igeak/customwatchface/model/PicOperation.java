package com.igeak.customwatchface.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.igeak.customwatchface.presenter.loader.ImageResizer;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by xuqiang on 16-6-14.
 */
public class PicOperation {

    /**
     * 优化图片加载，根据当前界面的需要，调整inSampleSize
     * @param is   is要求一定要是bufferInputStream（原因：要支持mark/reset方法）
     * @param reqWidth    需要显示的宽度
     * @param reqHeight   需要显示的高度
     * @return Bitmap     得到我们需要的Bitmap
     * @throws Exception
     */
    public static Bitmap InputStream2Bitmap(InputStream is, int reqWidth, int reqHeight) throws Exception {
        is.mark(is.available());
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, options);
        options.inJustDecodeBounds = false;
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);
        options.inPreferredConfig = Bitmap.Config.RGB_565;
        is.reset();
        Bitmap bitmap = BitmapFactory.decodeStream(is, null, options);
        is.close();
        return bitmap;

    }


    /**
     * 根据需要的width和height，算出inSampleSize
     * @param options
     * @param reqWidth
     * @param reqHeight
     * @return inSampleSize
     */
    public static int calculateInSampleSize(BitmapFactory.Options options,
                                            int reqWidth, int reqHeight) {
        if (reqWidth == 0 || reqHeight == 0) {
            return 1;
        }

        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and
            // keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

}
