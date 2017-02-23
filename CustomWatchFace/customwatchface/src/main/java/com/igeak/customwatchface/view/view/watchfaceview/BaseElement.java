package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.Transformation;

import com.igeak.customwatchface.Const;
import com.igeak.customwatchface.model.PicOperation;
import com.igeak.customwatchface.util.PicUtil;
import com.orhanobut.logger.Logger;

import java.io.InputStream;


/**
 * Created by tangyi on 15-8-17.
 */
public abstract class BaseElement {

    public WatchPreviewView.Type mType = WatchPreviewView.Type.NULL;

    public BitmapDrawable mDrawable;

    public InputStream inputStream;

    public float mRotate = 0.0f;
    public float start_x = 0;
    public float start_y = 0;
    public boolean isEnable = false;

    public int window_level = 0;

    public Context mContext;

    protected Matrix matrix = new Matrix();

    private float length;
    private float width;
    private float height;

    public BaseElement(Context context, InputStream bitmap, int view_width, int view_height) {
        mContext = context;
        isEnable = true;
        //mDrawable = PicUtil.bitmap2Drawable(bitmap);
        inputStream = bitmap;

        try {
            Bitmap bitmap2 = PicOperation.InputStream2Bitmap(bitmap,  view_width, view_height);
            mDrawable = new BitmapDrawable(bitmap2);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public BaseElement(Context context, Bitmap bitmap) {
        mContext = context;
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


    public void layout(float view_width, float view_height) {


//起始坐标为中心点
        start_x = view_width / 2;
        start_y = view_height / 2;

        length = Math.min(view_width, view_height);
        width = view_width;
        height = view_height;

        if (mType == WatchPreviewView.Type.HOUR) {
            mRotate = 320.0f;
        } else if (mType == WatchPreviewView.Type.MINUTE) {
            mRotate = 40.0f;
        } else if (mType == WatchPreviewView.Type.SECOND) {
            mRotate = 180.0f;
        } else {
            mRotate = 0.0f;
        }
    }

    public void setDrawable(BitmapDrawable drawable) {
        mDrawable = drawable;
    }

    public void setBitmap(Bitmap bitmap) {
        mDrawable = PicUtil.bitmap2Drawable(bitmap);
    }

    public String getPngName() {
        return mType.toString().toLowerCase() + ".png";
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

//            drawable.setBounds((int) (x - (length / 2)), (int) (y - (length / 2)), (int) (x + (length / 2)),
//                    (int) (y + (length / 2)));
            drawable.setBounds((int) (x - (width / 2)), (int) (y - (height / 2)), (int) (x + (width / 2)),
                    (int) (y + (height / 2)));
            drawable.draw(canvas);
        }

        canvas.restore();

    }
}
