package com.igeak.customwatchface.model;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import java.io.InputStream;

/**
 * Created by xuqiang on 16-5-18.
 */
public class WatchFace {

    private String name;
    private InputStream background;
    private InputStream dialScale;
    private InputStream hour;
    private InputStream minute;
    private InputStream second;
    private boolean isAmPm;
    private boolean haveTemperature;
    private boolean showDate;
    private boolean showWeek;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public InputStream getBackground() {
        return background;
    }

    public void setBackground(InputStream background) {
        this.background = background;
    }

    public InputStream getDialScale() {
        return dialScale;
    }

    public void setDialScale(InputStream dialScale) {
        this.dialScale = dialScale;
    }

    public InputStream getHour() {
        return hour;
    }

    public void setHour(InputStream hour) {
        this.hour = hour;
    }

    public InputStream getMinute() {
        return minute;
    }

    public void setMinute(InputStream minute) {
        this.minute = minute;
    }

    public InputStream getSecond() {
        return second;
    }

    public void setSecond(InputStream second) {
        this.second = second;
    }

    public boolean isAmPm() {
        return isAmPm;
    }

    public void setAmPm(boolean amPm) {
        isAmPm = amPm;
    }

    public boolean isHaveTemperature() {
        return haveTemperature;
    }

    public void setHaveTemperature(boolean haveTemperature) {
        this.haveTemperature = haveTemperature;
    }

    public boolean isShowDate() {
        return showDate;
    }

    public void setShowDate(boolean showDate) {
        this.showDate = showDate;
    }

    public boolean isShowWeek() {
        return showWeek;
    }

    public void setShowWeek(boolean showWeek) {
        this.showWeek = showWeek;
    }


}
