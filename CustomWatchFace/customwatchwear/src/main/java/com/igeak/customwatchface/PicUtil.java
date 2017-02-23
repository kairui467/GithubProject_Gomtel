package com.igeak.customwatchface;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.TypedValue;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by tangyi on 15-8-20.
 */
public class PicUtil {
    private static final String TAG = "PicUtil";

    public static BitmapDrawable readRawDrawable(Context context, int resourceId) {
        return bitmap2Drawable(decodeResource(context.getResources(), resourceId));
    }


    public static Drawable file2Drawable(File file) throws FileNotFoundException {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(file));
        return inputStream2Drawable(bf);
    }

    public static Bitmap file2Bitmap(File file) throws FileNotFoundException {
        BufferedInputStream bf = new BufferedInputStream(new FileInputStream(file));
        return InputStream2Bitmap(bf);
    }


    public static Drawable inputStream2Drawable(InputStream is) {
        Bitmap bitmap = InputStream2Bitmap(is);
        return bitmap2Drawable(bitmap);
    }


    public static BitmapDrawable bitmap2Drawable(Bitmap bitmap) {
        BitmapDrawable bd = new BitmapDrawable(bitmap);
        return bd;
    }


    public static Bitmap InputStream2Bitmap(InputStream is) {
        return BitmapFactory.decodeStream(is);

    }


    private static Bitmap decodeResource(Resources resources, int id) {
        TypedValue value = new TypedValue();
        resources.openRawResource(id, value);
        BitmapFactory.Options opts = new BitmapFactory.Options();
        opts.inTargetDensity = value.density;
        return BitmapFactory.decodeResource(resources, id, opts);
    }


    public static Bitmap drawable2Bitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap
                .createBitmap(
                        drawable.getIntrinsicWidth(),
                        drawable.getIntrinsicHeight(),
                        drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }


    public static boolean saveBitmapToFile(Bitmap bm, File file) throws IOException {
        if (bm != null && file != null) {
            FileOutputStream output = null;
            Bitmap.CompressFormat format = Bitmap.CompressFormat.PNG;
            if (file.getPath().endsWith(".png")) {
                format = Bitmap.CompressFormat.PNG;
            }

            if (file.exists()) {
                file.delete(); //如果已经有文件，先删除
            }
            output = new FileOutputStream(file);
            bm.compress(format, 100, output);
            output.close();
            return true;

        }
        return false;
    }

    /**
     * save bitmap to file
     *
     * @param bm
     * @param filePath
     * @return
     */
    public static boolean saveBitmapToFile(Bitmap bm, String filePath) throws IOException {
        if (bm != null && filePath != null) {
            FileOutputStream output = null;
            Bitmap.CompressFormat format = Bitmap.CompressFormat.JPEG;
            if (filePath.endsWith(".png")) {
                format = Bitmap.CompressFormat.PNG;
            }
            if (filePath.startsWith("file://")) {
                filePath = filePath.substring(7);
            }
            File f = new File(filePath);
            if (f.exists()) {
                f.delete(); //如果已经有文件，先删除
            }
            output = new FileOutputStream(f);
            bm.compress(format, 100, output);
            output.close();
            return true;

        }
        return false;
    }


    /**
     * draw to round
     */
    public static Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left, top, right, bottom, dst_left, dst_top, dst_right, dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }

        Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int) left, (int) top, (int) right,
                (int) bottom);
        final Rect dst = new Rect((int) dst_left, (int) dst_top,
                (int) dst_right, (int) dst_bottom);
        final RectF rectF = new RectF(dst);

        paint.setAntiAlias(true);

        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }


    //图片合成
    public static Bitmap addFrameToImageTwo(Bitmap bmp, Bitmap frameBitmap) //bmp原图
    // frameBitmap资源图片(边框)
    {
        //bmp原图 创建新位图
        int width = bmp.getWidth();
        int height = bmp.getHeight();
        Bitmap drawBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        //对边框进行缩放
        int w = frameBitmap.getWidth();
        int h = frameBitmap.getHeight();
        float scaleX = width * 1F / w;        //缩放比 如果图片尺寸超过边框尺寸 会自动匹配
        float scaleY = height * 1F / h;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleX, scaleY);   //缩放图片
        Bitmap copyBitmap = Bitmap.createBitmap(frameBitmap, 0, 0, w, h, matrix, true);

        int pixColor = 0;
        int layColor = 0;
        int newColor = 0;

        int pixR = 0;
        int pixG = 0;
        int pixB = 0;
        int pixA = 0;

        int newR = 0;
        int newG = 0;
        int newB = 0;
        int newA = 0;

        int layR = 0;
        int layG = 0;
        int layB = 0;
        int layA = 0;

        float alpha = 0.8F;
        float alphaR = 0F;
        float alphaG = 0F;
        float alphaB = 0F;

        for (int i = 0; i < width; i++) {
            for (int k = 0; k < height; k++) {
                pixColor = bmp.getPixel(i, k);
                layColor = copyBitmap.getPixel(i, k);
                // 获取原图片的RGBA值
                pixR = Color.red(pixColor);
                pixG = Color.green(pixColor);
                pixB = Color.blue(pixColor);
                pixA = Color.alpha(pixColor);
                // 获取边框图片的RGBA值
                layR = Color.red(layColor);
                layG = Color.green(layColor);
                layB = Color.blue(layColor);
                layA = Color.alpha(layColor);
                // 颜色与纯黑色相近的点
                if (layR < 20 && layG < 20 && layB < 20) {
                    alpha = 1F;
                } else {
                    alpha = 0.3F;
                }
                alphaR = alpha;
                alphaG = alpha;
                alphaB = alpha;
                // 两种颜色叠加
                newR = (int) (pixR * alphaR + layR * (1 - alphaR));
                newG = (int) (pixG * alphaG + layG * (1 - alphaG));
                newB = (int) (pixB * alphaB + layB * (1 - alphaB));
                layA = (int) (pixA * alpha + layA * (1 - alpha));
                // 值在0~255之间
                newR = Math.min(255, Math.max(0, newR));
                newG = Math.min(255, Math.max(0, newG));
                newB = Math.min(255, Math.max(0, newB));
                newA = Math.min(255, Math.max(0, layA));
                //绘制
                newColor = Color.argb(newA, newR, newG, newB);
                drawBitmap.setPixel(i, k, newColor);
            }
        }
        return drawBitmap;
    }


    public static Bitmap createBitmap(Bitmap src, Bitmap watermark) {
        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        int ww = watermark.getWidth();
        int wh = watermark.getHeight();
        //create the new blank bitmap
        Bitmap newb = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);//创建一个新的和SRC长度宽度一样的位图
        Canvas cv = new Canvas(newb);
        //draw src into
        cv.drawBitmap(src, 0, 0, null);//在 0，0坐标开始画入src
        //draw watermark into
        cv.drawBitmap(watermark, w - ww + 5, h - wh + 5, null);//在src的右下角画入水印
        //save all clip
        cv.save(Canvas.ALL_SAVE_FLAG);//保存
        //store
        cv.restore();//存储
        return newb;
    }


}
