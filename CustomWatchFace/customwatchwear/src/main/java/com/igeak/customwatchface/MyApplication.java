package com.igeak.customwatchface;

import android.app.Application;

import org.xerrard.util.CrashHandler;

/**
 * Created by xuqiang on 16-6-3.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}
