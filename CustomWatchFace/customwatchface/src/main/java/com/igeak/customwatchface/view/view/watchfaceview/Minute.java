package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.InputStream;


/**
 * Created by tangyi on 15-8-17.
 */
public class Minute extends   BaseElement {



    public Minute(Context context, InputStream bitmap, int view_width, int view_height) {
        super(context, bitmap,view_width,view_height);
        window_level = DisPlayLevel.LEVEL_MINUTE;
        mType = WatchPreviewView.Type.MINUTE;
    }

    public Minute(Context context, Bitmap bitmap) {
        super(context, bitmap);
        window_level = DisPlayLevel.LEVEL_MINUTE;
        mType = WatchPreviewView.Type.MINUTE;

    }

}
