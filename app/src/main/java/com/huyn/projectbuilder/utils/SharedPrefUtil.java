package com.huyn.projectbuilder.utils;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPrefUtil {

    private static SharedPrefUtil instance;
    private static SharedPreferences sharedPrefs;

    //for test
    public static final String API_PATH = "API_PATH";
    public static final String API_FOR_UPLOAD_PATH = "API_FOR_UPLOAD_IMAGE_PATH";

    private SharedPrefUtil(Context context) {
        sharedPrefs = context.getSharedPreferences(Constant.SHARED, Context.MODE_PRIVATE);
    }

    public static SharedPrefUtil getInstance(Context context) {
        if (sharedPrefs == null)
            instance = new SharedPrefUtil(context instanceof Application ? context : context.getApplicationContext());
        return instance;
    }

    public String getString(String key, String defaultValue) {
        return sharedPrefs.getString(key, defaultValue);
    }

    public String getString(String key) {
        return sharedPrefs.getString(key, "");
    }

    public boolean getBool(String key) {
        return sharedPrefs.getBoolean(key, false);
    }

    public boolean getBool(String key, boolean defaultValue) {
        return sharedPrefs.getBoolean(key, defaultValue);
    }

    public int getInt(String key, int defaultValue) {
        return sharedPrefs.getInt(key, defaultValue);
    }

    public void putString(String key, String value) {
        Editor edit = sharedPrefs.edit();
        edit.putString(key, value);
        edit.commit();
    }

    public void putBool(String key, boolean value) {
        Editor edit = sharedPrefs.edit();
        edit.putBoolean(key, value);
        edit.commit();
    }

    public void putInt(String key, int value) {
        Editor edit = sharedPrefs.edit();
        edit.putInt(key, value);
        edit.commit();
    }

    public void clear() {
        Editor edit = sharedPrefs.edit();
        edit.clear();
        edit.commit();
    }

}
