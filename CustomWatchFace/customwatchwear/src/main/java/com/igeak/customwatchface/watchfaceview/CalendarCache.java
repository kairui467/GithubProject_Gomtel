package com.igeak.customwatchface.watchfaceview;

import java.util.Calendar;

/**
 * Created by xuqiang on 16-7-26.
 */
public class CalendarCache {
    public final static int CURRENT_TIME_MILLS = 20;

    public final static int DEFAULT = -65500;

    public int week = DEFAULT;
    public int day = DEFAULT;
    public int month = DEFAULT;
    public int year = DEFAULT;
    public int ampm = DEFAULT;

    public int hourofday = DEFAULT;
    public int hour = DEFAULT;
    public int minute = DEFAULT;
    public int second = DEFAULT;
    public int millisecond = DEFAULT;

    public long currentTimeMillis = DEFAULT;
    public Calendar ccalendar;

    public CalendarCache(Calendar calendar) {
        ccalendar = calendar;
    }

    public void getInstance() {
        reset();
        ccalendar.setTimeInMillis(currentTimeMillis = System.currentTimeMillis());
    }

    private void reset() {
        week = DEFAULT;
        day = DEFAULT;
        month = DEFAULT;
        year = DEFAULT;
        ampm = DEFAULT;
        hourofday = DEFAULT;
        hour = DEFAULT;
        minute = DEFAULT;
        second = DEFAULT;
        millisecond = DEFAULT;
        currentTimeMillis = DEFAULT;

    }

    public int  get(int field) {
        switch (field) {

            case Calendar.HOUR_OF_DAY:
                if(hourofday == DEFAULT) {
                    hourofday = ccalendar.get(Calendar.HOUR_OF_DAY);
                }
                return hourofday;

            case Calendar.HOUR:
                if(hour == DEFAULT) {
                    hour = ccalendar.get(Calendar.HOUR);
                }
                return hour;

            case Calendar.MINUTE:
                if(minute == DEFAULT) {
                    minute = ccalendar.get(Calendar.MINUTE);
                }
                return minute;

            case Calendar.SECOND:
                if(second == DEFAULT) {
                    second = ccalendar.get(Calendar.SECOND);
                }
                return second;

            case Calendar.MILLISECOND:
                if(millisecond == DEFAULT) {
                    millisecond = ccalendar.get(Calendar.MILLISECOND);
                }
                return millisecond;

            case Calendar.DAY_OF_WEEK:
                if(week == DEFAULT) {
                    week = ccalendar.get(Calendar.DAY_OF_WEEK);
                }
                return week;


            case Calendar.DAY_OF_MONTH:
                if(day == DEFAULT) {
                    day = ccalendar.get(Calendar.DAY_OF_MONTH);
                }
                return day;

            case Calendar.MONTH:
                if(month == DEFAULT) {
                    month = ccalendar.get(Calendar.MONTH);
                }
                return month;

            case Calendar.YEAR:
                if(year == DEFAULT) {
                    year = ccalendar.get(Calendar.YEAR);
                }
                return year;

            case Calendar.AM_PM :
                if(ampm == DEFAULT) {
                    ampm = ccalendar.get(Calendar.AM_PM);
                }
                return ampm;


        }

        return 0;
    }

    public long getCurrentTimeMillis () {
        if(currentTimeMillis == DEFAULT) {
            currentTimeMillis = System.currentTimeMillis();
        }

        return currentTimeMillis;
    }


    public int getFirstDayOfWeek() {
        return ccalendar.getFirstDayOfWeek();
    }

}