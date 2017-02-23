package com.igeak.customwatchface;

import android.graphics.Bitmap;

/**
 * Created by xuqiang on 16-5-18.
 */
public class WatchFace {

    private String name;
    private Bitmap background;
    private Bitmap dialScale;
    private Bitmap hour;
    private Bitmap minute;
    private Bitmap second;
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

    public Bitmap getBackground() {
        return background;
    }

    public void setBackground(Bitmap background) {
        this.background = background;
    }

    public Bitmap getDialScale() {
        return dialScale;
    }

    public void setDialScale(Bitmap dialScale) {
        this.dialScale = dialScale;
    }

    public Bitmap getHour() {
        return hour;
    }

    public void setHour(Bitmap hour) {
        this.hour = hour;
    }

    public Bitmap getMinute() {
        return minute;
    }

    public void setMinute(Bitmap minute) {
        this.minute = minute;
    }

    public Bitmap getSecond() {
        return second;
    }

    public void setSecond(Bitmap second) {
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
