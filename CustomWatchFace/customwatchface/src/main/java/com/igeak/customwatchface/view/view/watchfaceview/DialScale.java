package com.igeak.customwatchface.view.view.watchfaceview;

import android.content.Context;
import android.graphics.Bitmap;

import java.io.InputStream;

/**
 * Created by tangyi on 15-8-26.
 */
public class DialScale extends BaseElement {


    public DialScale(Context context, InputStream bitmap, int view_width, int view_height) {
        super(context, bitmap,view_width,view_height);
        window_level = DisPlayLevel.LEVEL_DIALSCALE;
        mType = WatchPreviewView.Type.DIALSCALE;

    }

    public DialScale(Context context, Bitmap bitmap) {
        super(context, bitmap);
        window_level = DisPlayLevel.LEVEL_DIALSCALE;
        mType = WatchPreviewView.Type.DIALSCALE;

    }


}
