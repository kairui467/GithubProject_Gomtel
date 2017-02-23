package com.igeak.customwatchface;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by xuqiang on 16-5-18.
 */
public class WatchFaceBean implements Parcelable {

    private String name;
    private String background;
    private String dialScale;
    private String hour;
    private String minute;
    private String second;
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

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getDialScale() {
        return dialScale;
    }

    public void setDialScale(String dialScale) {
        this.dialScale = dialScale;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(background);
        dest.writeString(dialScale);
        dest.writeString(hour);
        dest.writeString(minute);
        dest.writeString(second);
        dest.writeInt(isAmPm ? 1 : 0);
        dest.writeInt(haveTemperature ? 1 : 0);
        dest.writeInt(showDate ? 1 : 0);
        dest.writeInt(showWeek ? 1 : 0);
    }


    private WatchFaceBean(Parcel in){
        name= in.readString();
        background= in.readString();
        dialScale= in.readString();
        hour= in.readString();
        minute= in.readString();
        second= in.readString();
        isAmPm = in.readInt()==1;
        haveTemperature = in.readInt()==1;
        showDate = in.readInt()==1;
        showWeek = in.readInt()==1;
    }

    public static final Creator<WatchFaceBean> CREATOR = new Creator<WatchFaceBean>(){

        @Override
        public WatchFaceBean createFromParcel(Parcel source) {
            return new WatchFaceBean(source);
        }

        @Override
        public WatchFaceBean[] newArray(int size) {
            return new WatchFaceBean[size];
        }
    };


}
