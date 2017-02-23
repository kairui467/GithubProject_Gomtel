package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;

import com.igeak.customwatchface.model.WatchFace;
import com.igeak.customwatchface.util.PicUtil;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


public class WatchPreviewView extends View {


    private Context context;
    private static final String TAG = "WatchPreviewView";
    private Map<Type, BaseElement> mElements = null;
    public float view_width = 0;

    public float view_height = 0;

    private PaintFlagsDrawFilter paintFlter = new PaintFlagsDrawFilter(0,
            Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG); //设置抗锯齿


    public WatchPreviewView(Context context) {
        this(context, null);
    }

    public WatchPreviewView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WatchPreviewView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        this.context = context;
        mElements = new HashMap<Type, BaseElement>();
    }

    public void setElements(WatchFace watchface) {


        mElements.clear();
        mElements.put(Type.BACKGROUND, new BackGround(context, watchface
                .getBackground(), (int) view_width, (int) view_height));
        mElements.put(Type.DIALSCALE, new DialScale(context, watchface
                .getDialScale(), (int) view_width, (int) view_height));
        mElements.put(Type.HOUR, new Hour(context, watchface.getHour(), (int) view_width, (int)
                view_height));
        mElements.put(Type.MINUTE, new Minute(context, watchface.getMinute(), (int) view_width,
                (int) view_height));
        mElements.put(Type.SECOND, new Second(context, watchface.getSecond(), (int) view_width,
                (int) view_height));
        //requestLayout();
        //invalidate();
    }


    public void setPoint(Map<PointView.Type, Bitmap> map) {
        mElements.put(Type.HOUR, new Hour(context, map.get(PointView.Type.HOUR)));
        mElements.put(Type.MINUTE, new Minute(context, map.get(PointView.Type.MINUTE)));
        mElements.put(Type.SECOND, new Second(context, map.get(PointView.Type.SECOND)));
        invalidate();
    }



    public void setBackground(Bitmap bitmap) {
        mElements.put(Type.BACKGROUND, new BackGround(context, bitmap));
        //requestLayout();
        invalidate();
    }


    public void setScale(Bitmap bitmap) {
        mElements.put(Type.DIALSCALE, new DialScale(context, bitmap));
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
        super.onDraw(canvas);
        view_width = this.getWidth();
        view_height = this.getHeight();
        drawElement(canvas, mElements.get(Type.BACKGROUND));
        drawElement(canvas, mElements.get(Type.DIALSCALE));
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
        HOUR, MINUTE, SECOND, BACKGROUND, DIALSCALE, DATE, WEEK, NULL
    }
}
