package com.igeak.customwatchface.watchfaceview;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import com.igeak.customwatchface.MyWatchFace;

/**
 * Created by tangyi on 15-8-17.
 */
public class BackGround extends   BaseElement{


    public BackGround(Bitmap bitmap) {
        super(bitmap);
        window_level = DisPlayLevel.LEVEL_BACKGROUND;
        mType = MyWatchFace.Type.BACKGROUND;

    }

    public BackGround(Drawable drawable) {
        super(drawable);
        window_level = DisPlayLevel.LEVEL_BACKGROUND;
        mType = MyWatchFace.Type.BACKGROUND;

    }

}
