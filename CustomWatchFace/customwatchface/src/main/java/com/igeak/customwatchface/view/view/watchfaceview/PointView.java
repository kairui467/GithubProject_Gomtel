package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class PointView extends View {


    private Context context;
    private static final String TAG = "PointView";
    public Map<Type, BaseElement> mElements = null;
    public float view_width = 0;
    public float view_height = 0;

    private PaintFlagsDrawFilter paintFlter = new PaintFlagsDrawFilter(0,
            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG); //设置抗锯齿


    public PointView(Context context) {
        this(context, null);
    }

    public PointView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PointView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        mElements = new HashMap<Type, BaseElement>();
    }

    public void setPointElement(Map<Type, InputStream> map) {
        mElements.clear();
        mElements.put(Type.HOUR, new Hour(context, map.get(Type.HOUR), (int) view_width, (int)
                view_height));
        mElements.put(Type.MINUTE, new Minute(context, map.get(Type.MINUTE), (int) view_width,
                (int) view_height));
        mElements.put(Type.SECOND, new Second(context, map.get(Type.SECOND), (int) view_width,
                (int) view_height));
        //requestLayout();
        //invalidate();
    }

    public void setPointElementBitmap(Map<Type, Bitmap> map) {
        mElements.clear();
        mElements.put(Type.HOUR, new Hour(context, map.get(Type.HOUR)));
        mElements.put(Type.MINUTE, new Minute(context, map.get(Type.MINUTE)));
        mElements.put(Type.SECOND, new Second(context, map.get(Type.SECOND)));
        //requestLayout();
        //invalidate();
    }


    public void setHour(InputStream bitmap) {
        mElements.put(Type.HOUR, new DialScale(context, bitmap, (int) view_width, (int)
                view_height));
        invalidate();
    }

    public void setMinute(InputStream bitmap) {
        mElements.put(Type.MINUTE, new DialScale(context, bitmap, (int) view_width, (int)
                view_height));
        invalidate();
    }

    public void setSecond(InputStream bitmap) {
        mElements.put(Type.SECOND, new DialScale(context, bitmap, (int) view_width, (int)
                view_height));
        invalidate();
    }


//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        int widthSpecMode = MeasureSpec.getMode(widthMeasureSpec);
//        int widthSpecSize = MeasureSpec.getSize(widthMeasureSpec);
//        int heightSpecMode = MeasureSpec.getMode(heightMeasureSpec);
//        int heightSpecSize = MeasureSpec.getSize(heightMeasureSpec);
//        if (widthSpecMode == MeasureSpec.AT_MOST
//                && heightSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(200, 200);
//        } else if (widthSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(200, heightSpecSize);
//        } else if (heightSpecMode == MeasureSpec.AT_MOST) {
//            setMeasuredDimension(widthSpecSize, 200);
//        }
//    }

    @Override
    protected void onDraw(Canvas canvas) {
        Log.i(TAG, "onDraw");
        super.onDraw(canvas);
        view_width = this.getWidth();
        view_height = this.getHeight();
        drawElement(canvas, mElements.get(Type.HOUR));
        drawElement(canvas, mElements.get(Type.MINUTE));
        drawElement(canvas, mElements.get(Type.SECOND));

    }

    private void drawElement(Canvas canvas, BaseElement element) {
        if (element == null || canvas == null) {
            return;
        }
        canvas.setDrawFilter(paintFlter);
        element.layout(view_width, view_height);
        element.onDraw(canvas);
    }

    public enum Type {
        HOUR, MINUTE, SECOND
    }
}
