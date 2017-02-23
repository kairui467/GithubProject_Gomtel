package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.InputStream;


/**
 * Created by tangyi on 15-8-17.
 */
public class Hour extends   BaseElement {


    public Hour(Context context, InputStream bitmap, int view_width, int view_height) {
        super(context, bitmap,view_width,view_height);
        window_level = DisPlayLevel.LEVEL_HOUR;
        mType = WatchPreviewView.Type.HOUR;
    }

    public Hour(Context context, Bitmap bitmap) {
        super(context, bitmap);
        window_level = DisPlayLevel.LEVEL_HOUR;
        mType = WatchPreviewView.Type.HOUR;

    }

}
