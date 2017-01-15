package com.huyn.projectbuilder.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings.Secure;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Properties;

public class AppUtil {

    public static boolean isAppOnForeground(Context context) {
        return isAppOnForeground(context, context.getPackageName());
    }

    public static boolean isAppInstalled(Context context, String pkg) {
        PackageManager pm = context.getPackageManager();
        boolean installed = false;
        try {
            pm.getPackageInfo(pkg, PackageManager.GET_ACTIVITIES);
            installed = true;
        } catch (Exception e) {
            installed = false;
        }
        return installed;
    }

    /**
     * 判断应用是否在前台显示
     *
     * @param context
     * @param packageName
     * @return true:表示在前台显示
     */
    public static boolean isAppOnForeground(Context context, String packageName) {
        try {
            if (packageName == null || context == null) {
                return false;
            }
            ActivityManager activityManager = (ActivityManager) context
                    .getSystemService(Context.ACTIVITY_SERVICE);
            List<RunningAppProcessInfo> appProcesses = activityManager
                    .getRunningAppProcesses();
            if (appProcesses == null)
                return false;
            for (RunningAppProcessInfo appProcess : appProcesses) {
                if (appProcess.processName.equals(packageName)
                        && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    return true;
                }
            }
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 得到netconfig.properties配置文件中的所有配置属性
     *
     * @return Properties对象 用法：String IP =
     * Utils.getNetConfigProperties().getProperty("IP");
     */
    public static Properties getProperties(Context context) {
        Properties props = new Properties();
//		InputStream in = Utils.class.getResourceAsStream("/gewara.properties");
        try {
            InputStream in = context.getAssets().open("gewara.properties");
            props.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return props;
    }

    public static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
        }
        return null;

    }

    public static boolean isWiFiActive(Context context) {
        WifiManager wm = null;
        try {
            wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (wm == null || wm.isWifiEnabled() == false) return false;
        return true;


    }

    /**
     * 判断某个应用是否正在运行
     *
     * @param context
     * @param packageName
     * @return
     */
    public static boolean isAppOnRunning(Context context, String packageName) {
        if (packageName == null || context == null) {
            return false;
        }
        ActivityManager activityManager = (ActivityManager) context
                .getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;
        for (RunningAppProcessInfo appProcess : appProcesses) {
            if (appProcess.processName.equals(packageName)) {
                return true;
            }
        }
        return false;
    }


    /**
     * 判断Network是否开启(包括移动网络和wifi)
     *
     * @return
     */
    public static boolean isNetworkEnabled(Context mContext) {
        return (isNetEnabled(mContext) || isWIFIEnabled(mContext));
    }

    /**
     * 判断Network是否连接成功(包括移动网络和wifi)
     *
     * @return
     */

    public static boolean isNetworkConnected(Context mContext) {
        return (isWifiContected(mContext) || isNetContected(mContext));
    }

    /**
     * 判断移动网络是否开启
     *
     * @return
     */
    public static boolean isNetEnabled(Context context) {

        boolean enable = false;
        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        if (telephonyManager != null) {
            if (telephonyManager.getNetworkType() != TelephonyManager.NETWORK_TYPE_UNKNOWN) {
                enable = true;
                Log.i(Thread.currentThread().getName(), "isNetEnabled");
            }
        }
        return enable;
    }


    /**
     * 判断wifi是否开启
     */
    public static boolean isWIFIEnabled(Context context) {
        boolean enable = false;
        WifiManager wifiManager = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        if (wifiManager.isWifiEnabled()) {
            enable = true;
            Log.i(Thread.currentThread().getName(), "isWifiEnabled");
        }
        Log.i(Thread.currentThread().getName(), "isWifiDisabled");
        return enable;
    }

    /**
     * 判断移动网络是否连接成功！
     *
     * @param context
     * @return
     */
    public static boolean isNetContected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobileNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetworkInfo.isConnected()) {
            Log.i(Thread.currentThread().getName(), "isNetContected");
            return true;
        }
        Log.i(Thread.currentThread().getName(), "isNetDisconnected");
        return false;
    }

    /**
     * 判断wifi是否连接成功
     *
     * @param context
     * @return
     */
    public static boolean isWifiContected(Context context) {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifiNetworkInfo = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetworkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static String getIMEI(Context context) {
        TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei = tm.getDeviceId();
        // no sim: sdk|any pad
        if (null != imei && !Constant.SPECIAL_IMEI.equals(imei)) {
            Utils.Log("requestCount", "imei:" + imei);
            return imei;
        } else {
            String deviceId = Secure.getString(context.getContentResolver(), Secure.ANDROID_ID);
            // sdk: android_id
            if (null != deviceId) {
                Utils.Log("requestCount", "ANDROID_ID:" + deviceId);
                return deviceId;
            }
        }
        return "";
    }

    /**
     * 获取手机的设备号或wifi的mac号，在wifi环境下只返回mac地址，否则返回手机设备号<br>
     * 在模拟器情况下会返回null
     *
     * @return
     */
    public static String getDeviceId(Context context) {
        WifiManager manager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        String macAddress = wifiInfo.getMacAddress();
        if (null != macAddress) {
            Utils.Log("requestCount", "mac:" + macAddress);
            return macAddress.replace(".", "").replace(":", "")
                    .replace("-", "").replace("_", "").replace("+", "").replace("-", "").replace("=", "");
        } else {
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            String imei = tm.getDeviceId();
            // no sim: sdk|any pad
            if (null != imei && !Constant.SPECIAL_IMEI.equals(imei)) {
                Utils.Log("requestCount", "imei:" + imei);
                return imei;
            } else {
                String deviceId = Secure.getString(context.getContentResolver(),
                        Secure.ANDROID_ID);
                // sdk: android_id
                if (null != deviceId) {
                    Utils.Log("requestCount", "ANDROID_ID:" + deviceId);
                    return deviceId.replace("+", "").replace("-", "").replace("=", "");
                }
                return null;
            }
        }
    }

    public static String getDeviceInfo(Context context) {
        String strInfo = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            String appVersion = info.versionName;
            String DevicePRODUCT = Build.PRODUCT;
            String VersionRelease = Build.VERSION.RELEASE;

            if (StringUtils.isNotEmpty(appVersion)) {
                strInfo += "v" + appVersion + "/";
            }

            if (StringUtils.isNotEmpty(VersionRelease)) {
                strInfo += "Android v" + VersionRelease;
            }

            if (StringUtils.isNotEmpty(DevicePRODUCT)) {
                strInfo += " " + DevicePRODUCT;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return strInfo;
    }

    public static String getAPPVersion(Context context) {
        String appVersion = "";
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            return info.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return appVersion;
    }

    public static String getDeviceProduct(Context context) {
        String DevicePRODUCT = Build.PRODUCT;
        return DevicePRODUCT;
    }

    public static String getOsVersion(Context context) {
        String VersionRelease = Build.VERSION.RELEASE;
        return VersionRelease;
    }

    public static int getSDKVersion() {
        return Build.VERSION.SDK_INT;
    }

    public static boolean isFlyme() {
        try {
            // Invoke Build.hasSmartBar()
            final Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (final Exception e) {
            return false;
        }

    }

    public static void exitApp(Activity context, boolean createShorcut) {
//		UserUtil.loginOut(context);
//		if (createShorcut)
//			doShortCutSet(context);

        //如果开发者在App中杀死进程，需要在杀死进程之前调用此方法，以保存统计数据
//		MobclickAgentUtil.onKillProcess(context);

//		System.exit(0);

        context.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }

//	public static void doShortCutSet(Context context) {
//		boolean isShortCut = SharedPrefUtil.getInstance(context).getBool(
//				SharedPrefrence.SHORTCUT_SETTING);
//		if (!isShortCut) {
//			installShortCut(context);
//			SharedPrefUtil.getInstance(context).putBool(
//					SharedPrefrence.SHORTCUT_SETTING, true);
//		}
//	}

    /*
     * 添加快捷方式
     */
    static final String EXTRA_SHORTCUT_DUPLICATE = "duplicate";

//	public static void installShortCut(Context context) {
//		Intent shortcutIntent = new Intent(
//				"com.android.launcher.action.INSTALL_SHORTCUT");
//		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
//				context.getString(R.string.app_name));
//		shortcutIntent.putExtra(EXTRA_SHORTCUT_DUPLICATE, false);
//		Intent intent2 = new Intent(Intent.ACTION_MAIN);
//		intent2.addCategory(Intent.CATEGORY_LAUNCHER);
//
//		intent2.setComponent(new ComponentName(context.getPackageName(),
//				CoverActivity.class.getName()));
//
//		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, intent2);
//		shortcutIntent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE,
//				Intent.ShortcutIconResource.fromContext(context, R.drawable.icon));
//		context.sendBroadcast(shortcutIntent);
//	}

    /**
     * 检测某ActivityUpdate是否在当前Task的栈顶
     */
    public static boolean isTopActivy(Context context, String cmdName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Activity.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> runningTaskInfos = manager.getRunningTasks(1);
        String cmpNameTemp = null;

        if (null != runningTaskInfos) {
            cmpNameTemp = (runningTaskInfos.get(0).topActivity).getClassName();
        }

        if (StringUtils.isBlank(cmpNameTemp)) return false;
        return cmpNameTemp.contains(cmdName);
    }

    @TargetApi(19)
    public static void setTranslucentStatus(Activity activity, boolean on) {
        if (Build.VERSION.SDK_INT >= 19) {
            Window win = activity.getWindow();
            WindowManager.LayoutParams winParams = win.getAttributes();
            final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            if (on) {
                winParams.flags |= bits;
            } else {
                winParams.flags &= ~bits;
            }
            win.setAttributes(winParams);
        }
    }

}
