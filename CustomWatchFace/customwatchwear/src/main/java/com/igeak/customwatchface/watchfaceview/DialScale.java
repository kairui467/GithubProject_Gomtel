package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;

import com.igeak.customwatchface.MyWatchFace;

/**
 * Created by tangyi on 15-8-26.
 */
public class DialScale extends BaseElement {


    public DialScale(Bitmap bitmap) {
        super(bitmap);
        window_level = DisPlayLevel.LEVEL_DIALSCALE;
        mType = MyWatchFace.Type.DIALSCALE;

    }



}
