package com.huyn.projectbuilder.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Environment;

import java.io.File;

/**
 * Created by huyaonan on 16/9/5.
 */
public class StorageUtil {

    public static String cachePath;

    /**
     * Like {@link Build.VERSION#SDK_INT}, but in a place where it can be conveniently
     * overridden for local testing.
     */
    public static final int SDK_INT =
            (Build.VERSION.SDK_INT == 23 && Build.VERSION.CODENAME.charAt(0) == 'N') ? 24
                    : Build.VERSION.SDK_INT;


    public static final void initDiskCacheDir(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            File sdcard = Environment.getExternalStorageDirectory();
            if(sdcard.exists()) {
                init(sdcard);
            }
        }
        cachePath = context.getCacheDir().getPath();
    }

    public static boolean checkSDWritePermission(Context context) {
        PackageManager pm = context.getPackageManager();
        return (PackageManager.PERMISSION_GRANTED ==
                pm.checkPermission("android.permission.WRITE_EXTERNAL_STORAGE", context.getPackageName()));
    }

    private static void init(File sdcard) {
        File gewaraDirectory = new File(sdcard + Constant.GEWARA_DIR);
        if (!gewaraDirectory.exists())
            gewaraDirectory.mkdirs();
        File cacheDirectory = new File(sdcard + Constant.CACHE_DIR);
        if (!cacheDirectory.exists())
            cacheDirectory.mkdirs();
        File updateApkDirectory = new File(sdcard + Constant.UPDATE_APK_DIR);
        if (!updateApkDirectory.exists())
            updateApkDirectory.mkdirs();
        File cacheDir = new File(sdcard + Constant.CACHE_CACHE);
        if (!cacheDir.exists())
            cacheDir.mkdirs();
        File tempDir = new File(sdcard + Constant.GEWARA_TEMP);
        if (!tempDir.exists())
            tempDir.mkdirs();
    }

}
