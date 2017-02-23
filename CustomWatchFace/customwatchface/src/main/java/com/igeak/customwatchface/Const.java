package com.igeak.customwatchface;

/**
 * Created by xuqiang on 16-5-17.
 */
public class Const {
    public static final String TAG = "Element";
    public static final String MESSAGE_DATA_PATH = "/ZIP_WATCH";
    public static final String INTENT_EXTRA_KEY_WATCHFACE = "watchface";
    public static final String INTENT_EXTRA_KEY_ISCUSTOM = "iscustom";
    public static final String ASSETS_WATCH_START_NAME = "assetwatchface";
    public static final String WATCH_START_NAME = "customwatchface";
    public static final String FOLDER_NAME = "custom_watchface";
    public static final String WATCHFACEJSON = "watchface.json";
    public static final String ZIPFILE_NAME = "watchface.zip";
    public static final String JSON_EXNAME = ".json";
    public static final String PNG_EXNAME = ".png";
    public static final String BACK_FOLDER_NAME = "custom_background";
    public static final String POINT_FOLDER_NAME = "custom_point";
    public static final String HOUR_NAME = "hour";
    public static final String MINUTE_NAME = "minute";
    public static final String SECOND_NAME = "second";


    public static final String SCALE_FOLDER_NAME = "custom_scale";

    public static final int REQUEST_MENU = 1001;
    public static final int RESULT_CODE_BASE = 2000;
    public static final int RESULT_CODE_EDIT = RESULT_CODE_BASE;
    public static final int RESULT_CODE_RELEASE = RESULT_CODE_BASE + 1;
    public static final int RESULT_CODE_RENAME = RESULT_CODE_BASE + 2;
    public static final int RESULT_CODE_SHARE = RESULT_CODE_BASE + 3;
    public static final int RESULT_CODE_DELETE = RESULT_CODE_BASE + 4;

    public static final int REQUEST_EDIT = 1100;

    public static final String EXCEPTION_CHECK_CONNECT = "please_connect_watch";
    public static final String EXCEPTION_CHECK_PHONESYNC = "please_open_phone_sync";

}
