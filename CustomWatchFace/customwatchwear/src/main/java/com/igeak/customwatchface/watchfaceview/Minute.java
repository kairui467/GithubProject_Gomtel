package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;

import com.igeak.customwatchface.MyWatchFace;


/**
 * Created by tangyi on 15-8-17.
 */
public class Minute extends   BaseElement {



    public Minute(Bitmap bitmap) {
        super(bitmap);
        window_level = DisPlayLevel.LEVEL_MINUTE;
        mType = MyWatchFace.Type.MINUTE;
    }


}
