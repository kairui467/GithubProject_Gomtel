package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.igeak.customwatchface.MyWatchFace;
import com.igeak.customwatchface.PicUtil;


/**
 * Created by tangyi on 15-8-17.
 */
public abstract class BaseElement {

    public MyWatchFace.Type mType = MyWatchFace.Type.NULL;

    public BitmapDrawable mDrawable;


    public float mRotate = 0.0f;


    public float start_x = 0;
    public float start_y = 0;

    public boolean isEnable = false;

    public int window_level = 0;

    private Matrix matrix = new Matrix();

    private float length;
    float view_width;
    float view_height;

    public BaseElement(Drawable drawable) {
        isEnable = true;
        mDrawable = (BitmapDrawable) drawable;
    }

    public BaseElement(Bitmap bitmap) {
        isEnable = true;
        mDrawable = PicUtil.bitmap2Drawable(bitmap);
    }

    public static class DisPlayLevel {

        public static int LEVEL_BACKGROUND = 10;
        public static int LEVEL_DIALSCALE = 15;
        public static int LEVEL_HOUR = 45;
        public static int LEVEL_MINUTE = 50;
        public static int LEVEL_SECOND = 55;

    }


    public void layout(float view_width, float view_height, int hour, int minite, int second, int
            millisecond) {
//起始坐标为中心点

        this.view_height = 360;
        this.view_width = 360;
        start_x = view_width / 2;
        start_y = view_height / 2;
        length = Math.min(view_width, view_height);
        if (mType == MyWatchFace.Type.HOUR) {
            mRotate = 360.0f * (hour - 12.0f + minite / 60.0f) / 12.0f;
        } else if (mType == MyWatchFace.Type.MINUTE) {
            mRotate = 360.0f * (minite + second / 60.0f) / 60.0f;
        } else if (mType == MyWatchFace.Type.SECOND) {
            mRotate = 360.0f * (second + millisecond / 1000.0f) / 60.0f;
        } else {
            mRotate = 0.0f;
        }
    }

    public void setDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
    }


    public void onDraw(Canvas canvas) {
        canvas.save();

        float x = start_x;
        float y = start_y;

        canvas.translate(x, y);

        matrix.setRotate(mRotate);

        canvas.concat(matrix);
        canvas.translate(-x, -y);

        final Drawable drawable = mDrawable;

        if (drawable != null) {

            //BitmapDrawable
//            mDrawable.getBitmap().getDensity();
//            int w = drawable.getIntrinsicWidth();
//            int h = drawable.getIntrinsicHeight();
//            Logger.d(Const.TAG + "onDraw Type = " + mType + "   mRotate = " + mRotate + "   " +
//                    "   width=" + w + "   height =" + h);
            drawable.setBounds((int) (x - (length / 2)), (int) (y - (length / 2)), (int) (x +
                            (length / 2)),
                    (int) (y + (length / 2)));
            drawable.draw(canvas);
        }

        canvas.restore();

    }
}
