package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;

import com.igeak.customwatchface.MyWatchFace;


/**
 * Created by tangyi on 15-8-17.
 */
public class Second extends   BaseElement {


    public Second(Bitmap bitmap) {
        super(bitmap);
        window_level = DisPlayLevel.LEVEL_SECOND;
        mType = MyWatchFace.Type.SECOND;
    }

}
