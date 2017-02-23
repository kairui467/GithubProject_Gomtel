package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;

import com.igeak.customwatchface.MyWatchFace;


/**
 * Created by tangyi on 15-8-17.
 */
public class Hour extends   BaseElement {


    public Hour(Bitmap bitmap) {
        super(bitmap);
        window_level = DisPlayLevel.LEVEL_HOUR;
        mType = MyWatchFace.Type.HOUR;
    }


}
