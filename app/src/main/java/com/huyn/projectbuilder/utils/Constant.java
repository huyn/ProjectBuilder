package com.huyn.projectbuilder.utils;

import android.graphics.Bitmap;

/**
 * Created by huyaonan on 15/10/5.
 */
public class Constant {

    public static boolean DEBUG = false;
    public static boolean IMG_DEBUG = false;
    public static final Bitmap.Config BITMAP_CONFIG = Bitmap.Config.ARGB_8888;

    /** Gewara存储目录/文件 **/
    public static final String GEWARA_DIR = "/qianzu";
    public static final String CACHE_CACHE = GEWARA_DIR + "/cache";
    public static final String GEWARA_TEMP = GEWARA_DIR + "/temp";
    public static final String CACHE_DIR = GEWARA_DIR + "/images";
    public static final String UPDATE_APK_DIR = GEWARA_DIR + "/download";
    public static final String PLUGIN_DIR = GEWARA_DIR + "/plugin";
    public static final String APATCH_PATH = GEWARA_DIR + "/fix.apatch";

    public static final String UPDATE_APK_NAME_PREFIX = "AubeV";
    public static final String UPDATE_APK_NAME_SUFIX = ".apk";

    public static final String SHARED="AUBE";

    public static final String SPECIAL_IMEI="000000000000000";

    public static final String CHARSET_UTF8 = "UTF-8";

    /** 不需要更新*/
    public static final int UPDATE_NOTING = 0;
    /** 用户自己选择是否更新*/
    public static final int UPDATE_SPECIFIC = 1;
    /** 强制更新*/
    public static final int UPDATE_FORC = 2;

    public static final String SD_CARK_ROOT = "/sdcard";

    public static final String API_REQUEST_METHOD = "method";

    public static String getPluginDir() {
        if(DEBUG)
            return CACHE_CACHE;
        else
            return PLUGIN_DIR;
    }

    public static final String MEMBERENCODE_ILLEGAL = "MEMBERENCODE_ILLEGAL";
    public static final String AUTH_SUCCESS_FROM_WX = "AUTH_SUCCESS_FROM_WX";
    public static final String AUTH_CODE = "AUTH_CODE";
    public static final String AUTH_URL = "AUTH_URL";
    public static final String AUTH_STATE = "AUTH_STATE";

    //应用来源
    public static String APP_SOURCE = "AS01";

    public static String APP_MOBILETYPE = "";
    public static String APP_IMEI = "";
    public static String APP_DEVICEID = "";
    public static String APP_VERSION = "";
    public static String APP_TRUE_VERSION = "";

    public static final String APP_KEY_MOBILETYPE = "mobileType";
    public static final String APP_KEY_DEVICEID = "deviceid";
    public static final String APP_KEY_IMEI = "imei";
    public static final String APP_KEY_VERSION = "appVersion";
    public static final String APP_KEY_TRUE_VERSION = "trueVersion";
    public static final String APP_KEY_MEMBERID = "memberId";
    public static final String APP_KEY_MEMBERTOKEN = "memberToken";
    public static final String APP_KEY_APPKEY = "appkey";
    public static final String APP_KEY_APIVERSION = "v";
    public static final String APP_KEY_OSVERSION = "osVersion";
    public static final String APP_KEY_OSTYPE = "osType";
    public static final String APP_KEY_APPSOURCE = "appSource";

    public static final String OS_TYPE = "ANDROID";

    public static String APP_KEY = "";
    public static String PRIVATE_KEY = "";
    public static String APP_ID = "wx8bfda2ed68ac48e3";

    public static String getPrivateKey() {
        return PRIVATE_KEY;
    }

    public static void setAppKeyAndSecret(String key, String secret, String appId) {
        APP_KEY = key;
        PRIVATE_KEY = secret;
        APP_ID = appId;
    }

    /** CC视频 **/
    public static final String CC_API_KEY = "l7l7PX9hVOwkxx7gKg8GeAeFBebx8Tau";
    public static final String CC_USERID = "E778404B771671CE";

    public static final String IM_USERNAME = "supertv_username";
    public static final String IM_USERLOGO = "supertv_userlogo";

}
