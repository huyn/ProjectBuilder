package com.huyn.projectbuilder.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.Uri;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import junit.framework.Assert;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.Random;


public class Utils {

    public static final String sTAG = Utils.class.getSimpleName();

    private static long sLastClickTimeMills;

    /**
     * 300ms 内的点击认为是连续点击
     */
    private static final long FAST_CLICK_ELAPSED_TIME = 300l;

    public static enum LogType {
        DEBUG,
        ERROR,
        INFO,
        WARE
    }

    public static void sysout(String msg) {
        if (Constant.DEBUG) {
            System.out.println(msg);
        }
    }

    public static void showToast(Context context, String msg) {
        makeToast(context, msg).show();
    }

    public static void showToast(Context context, int resId) {
        makeToast(context, context.getResources().getString(resId)).show();
    }

    public static Toast mToast;

    private static Toast makeToast(Context context, String txt) {
        if (mToast == null)
            mToast = Toast.makeText(context.getApplicationContext(), txt, Toast.LENGTH_SHORT);
        else
            mToast.setText(txt);
        return mToast;
    }

    public static void Log(String msg) {
        if (!Constant.DEBUG) {
            return;
        }
        Log.i("com.aube", msg);
    }

    public static void Log(String TAG, String msg) {
        if (null == TAG) {
            TAG = sTAG;
        }
        if (!Constant.DEBUG) {
            return;
        }
        Log.i(TAG, msg);
    }

    public static void Log(LogType logType, String TAG, String msg) {
        if (null == TAG) {
            TAG = sTAG;
        }
        if (!Constant.DEBUG) {
            return;
        }
        if (LogType.DEBUG.compareTo(logType) == 0) {
            Log.d(TAG, msg);
        } else if (LogType.ERROR.compareTo(logType) == 0) {
            Log.e(TAG, msg);
        } else if (LogType.INFO.compareTo(logType) == 0) {
            Log.i(TAG, msg);
        } else if (LogType.WARE.compareTo(logType) == 0) {
            Log.w(TAG, msg);
        } else {
            Log.i(TAG, msg);
        }
    }

    /*
     * getRandom for user
     */
    private static Random rdm = new Random(System.currentTimeMillis());

    public static int getRandom(int min, int max) {

        int myRdm = (rdm.nextInt() & 0x7FFFFFFF) % (max + 1);

        while (myRdm < min) {
            myRdm = (rdm.nextInt() & 0x7FFFFFFF) % (max + 1);
        }

        return myRdm;
    }


    /**
     * @param birthday
     * @return 1983-2-22
     */
    public static int[] getBirthday(String birthday) {
        int[] date = new int[3];
        if (StringUtils.isNotEmpty(birthday)) {
            String str[] = birthday.split("-");
            date[0] = Integer.valueOf(str[0]);
            date[1] = Integer.valueOf(str[1]);
            date[2] = Integer.valueOf(str[2]);
        }
        return date;
    }

    private static final String TIMER_ZERO = "00:00";

    public static String getLeftTime(int leftTime) {

        if (leftTime <= 0) {
            return TIMER_ZERO;
        }

        int scd = leftTime / 1000;//得到多少秒;
        if (leftTime < 1000) {
            scd = 1;
        }
        int min = scd / 60; //得到多少分
        scd = scd % 60;      //得到多少秒
        StringBuffer sb = new StringBuffer();
        if (min < 10) {
            sb.append("0");
        }
        sb.append(min);
        sb.append(":");
        if (scd < 10) {
            sb.append("0");
        }
        sb.append(scd);
        return sb.toString();
    }

    public static String getHour(long time) {
        if (time <= 0) {
            return "0";
        }
        long scd = time / 1000;//得到多少秒;
        if (scd <= 3600) {
            return "1";
        }
        int hour = (int) (scd + (long) 3599) / 3600;
        return hour + "";
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        if (context == null)
            return (int) dpValue;
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px2dip
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String numberFormat(String number) {
        if (StringUtils.isEmpty(number)) return "";
        String str = "";
        int idx = number.length();

        while (idx - 3 > 0) {
            str = "," + number.substring(idx - 3, idx) + str;
            idx -= 3;
        }
        str = number.substring(0, idx) + str;
        return str;
    }


    public static String convertStreamToString(InputStream is) {
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8 * 1024);

            String line = null;
            while ((line = reader.readLine()) != null) {
//            	sb.append(line + "\n");
                sb.append(line);

            }
        } catch (IOException e) {
            sb.delete(0, sb.length());
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                return null;
            }
        }

        return sb.toString();
    }

    public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bmp.compress(CompressFormat.PNG, 100, output);
        if (needRecycle) {
            bmp.recycle();
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        // 取 drawable 的长宽
        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);
        return bitmap;
    }

    public static byte[] drawableToByteArray(Drawable drawable) {
        ByteArrayOutputStream output = null;
        ;
        try {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();

            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                    : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);

            output = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.PNG, 100, output);
            bitmap.recycle();

            byte[] result = output.toByteArray();
            output.close();
            return result;
        } catch (Exception e) {
        } finally {
        }
        return null;
    }

    public static byte[] getHtmlByteArray(final String url) {
        URL htmlUrl = null;
        InputStream inStream = null;
        try {
            htmlUrl = new URL(url);
            URLConnection connection = htmlUrl.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                inStream = httpConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] data = inputStreamToByte(inStream);

        return data;
    }

    public static byte[] inputStreamToByte(InputStream is) {
        try {
            ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
            int ch;
            while ((ch = is.read()) != -1) {
                bytestream.write(ch);
            }
            byte imgdata[] = bytestream.toByteArray();
            bytestream.close();
            return imgdata;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static byte[] readFromFile(String fileName, int offset, int len) {
        if (fileName == null) {
            return null;
        }

        File file = new File(fileName);
        if (!file.exists()) {
            Log.i(sTAG, "readFromFile: file not found");
            return null;
        }

        if (len == -1) {
            len = (int) file.length();
        }

        Log.d(sTAG, "readFromFile : offset = " + offset + " len = " + len + " offset + len = " + (offset + len));

        if (offset < 0) {
            Log.e(sTAG, "readFromFile invalid offset:" + offset);
            return null;
        }
        if (len <= 0) {
            Log.e(sTAG, "readFromFile invalid len:" + len);
            return null;
        }
        if (offset + len > (int) file.length()) {
            Log.e(sTAG, "readFromFile invalid file len:" + file.length());
            return null;
        }

        byte[] b = null;
        try {
            RandomAccessFile in = new RandomAccessFile(fileName, "r");
            b = new byte[len];
            in.seek(offset);
            in.readFully(b);
            in.close();

        } catch (Exception e) {
            Log.e(sTAG, "readFromFile : errMsg = " + e.getMessage());
            e.printStackTrace();
        }
        return b;
    }

    private static final int MAX_DECODE_PICTURE_SIZE = 1920 * 1440;

    public static Bitmap extractThumbNail(final String path, final int height, final int width, final boolean crop) {
        Assert.assertTrue(path != null && !path.equals("") && height > 0 && width > 0);

        BitmapFactory.Options options = new BitmapFactory.Options();

        try {
            options.inJustDecodeBounds = true;
            Bitmap tmp = BitmapFactory.decodeFile(path, options);
            if (tmp != null) {
                tmp.recycle();
                tmp = null;
            }

            Log.d(sTAG, "extractThumbNail: round=" + width + "x" + height + ", crop=" + crop);
            final double beY = options.outHeight * 1.0 / height;
            final double beX = options.outWidth * 1.0 / width;
            Log.d(sTAG, "extractThumbNail: extract beX = " + beX + ", beY = " + beY);
            options.inSampleSize = (int) (crop ? (beY > beX ? beX : beY) : (beY < beX ? beX : beY));
            if (options.inSampleSize <= 1) {
                options.inSampleSize = 1;
            }

            // NOTE: out of memory error
            while (options.outHeight * options.outWidth / options.inSampleSize > MAX_DECODE_PICTURE_SIZE) {
                options.inSampleSize++;
            }

            int newHeight = height;
            int newWidth = width;
            if (crop) {
                if (beY > beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            } else {
                if (beY < beX) {
                    newHeight = (int) (newWidth * 1.0 * options.outHeight / options.outWidth);
                } else {
                    newWidth = (int) (newHeight * 1.0 * options.outWidth / options.outHeight);
                }
            }

            options.inJustDecodeBounds = false;

            Log.i(sTAG, "bitmap required size=" + newWidth + "x" + newHeight + ", orig=" + options.outWidth + "x" + options.outHeight + ", sample=" + options.inSampleSize);
            Bitmap bm = BitmapFactory.decodeFile(path, options);
            if (bm == null) {
                Log.e(sTAG, "bitmap decode failed");
                return null;
            }

            Log.i(sTAG, "bitmap decoded size=" + bm.getWidth() + "x" + bm.getHeight());
            final Bitmap scale = Bitmap.createScaledBitmap(bm, newWidth, newHeight, true);
            if (scale != null) {
                bm.recycle();
                bm = scale;
            }

            if (crop) {
                final Bitmap cropped = Bitmap.createBitmap(bm, (bm.getWidth() - width) >> 1, (bm.getHeight() - height) >> 1, width, height);
                if (cropped == null) {
                    return bm;
                }

                bm.recycle();
                bm = cropped;
                Log.i(sTAG, "bitmap croped size=" + bm.getWidth() + "x" + bm.getHeight());
            }
            return bm;

        } catch (final OutOfMemoryError e) {
            Log.e(sTAG, "decode bitmap failed: " + e.getMessage());
            options = null;
        }

        return null;
    }

    public static String getDistance(Double lat1, Double lon1, Double lat2, Double lon2) {
        if (lat1 == null || lon1 == null || lat2 == null || lon2 == null) {
            return "0";
        }
        float[] results = new float[1];
        Location.distanceBetween(lat1, lon1, lat2, lon2, results);
        return new DecimalFormat("0.0").format(results[0] / 1000);
    }

    public static String getDistanceDesc(float distance) {
        if (distance == -1)
            return "未知";
        if (distance < 1.0f) {
            int index = (distance * 1000 + "").indexOf(".");
            String temp = (distance * 1000 + "").substring(0, index);
            return temp + "m";
        } else {
            if (distance >= 100.0f) {
                return ">100km";
            } else {
                return distance + "km";
            }
        }
    }

    /**
     * 将浮点型的字符转化为整数型，即去除小数点位
     *
     * @param value
     * @return
     */
    public static String formatFloatToInt(float value) {
        return new DecimalFormat("0").format(value);
    }

    public static boolean isValidUrl(String url) {
        if (StringUtils.isNotBlank(url) && (url.startsWith("http://") || url.startsWith("https://")))
            return true;
        return false;
    }

    public static String getRealPathFromURI(Uri contentUri, Context context) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(context, contentUri, proj, null, null, null);
            cursor = loader.loadInBackground();
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } catch (Exception e) {
            return contentUri.getPath();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

    }

    /**
     * 检测连续点击
     *
     * @return 300ms 内认为是连续点击
     * @see #FAST_CLICK_ELAPSED_TIME
     */
    public static boolean isFastClick() {
        long currentMills = SystemClock.elapsedRealtime();
        if (currentMills - sLastClickTimeMills < FAST_CLICK_ELAPSED_TIME) {
            return true;
        }
        sLastClickTimeMills = currentMills;
        return false;
    }

    /**
     * 检测点击操作 是否发生在 elpased 毫秒内
     *
     * @param elpased 毫秒
     * @return true: so fast
     */
    public static boolean isFastClick(long elpased) {
        long currentMills = SystemClock.elapsedRealtime();
        if (currentMills - sLastClickTimeMills < elpased) {
            return true;
        }
        sLastClickTimeMills = currentMills;
        return false;
    }

    /**
     * 保证只要有数就不会是0%
     *
     * @param total
     * @param num
     * @return
     */
    public static int calPercentInt(int total, int num) {
        if (total == 0 || num == 0) {
            return 0;
        }
//		int percent = (int)((float)num / total * 100 + 0.5f);
        int percent = Math.round(num * 100f / total);
//		int percent = (int) (num *100f/total);
        if (num < total) {
            return percent == 0 ? 1 : (percent < 100 ? percent : 99);
        } else {
            return percent;
        }
    }

    /**
     * 保证只要有数就不会是0%
     *
     * @param total
     * @param num
     * @return
     */
    public static float calPercentFloat(int total, int num) {
        if (total == 0 || num == 0) {
            return 0;
        }
        float percent = (float) num / total;
        if (num < total) {
            return percent < 0.01f ? 0.01f : (percent < 0.99f ? percent : 0.99f);
        } else {
            return percent;
        }
    }

    public static int getColor(Context context, int res) {
        return context.getResources().getColor(res);
    }

    public static void keepScreenOn(Activity context) {
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static void releaseScreenLock(Activity context) {
        context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    public static String copyToFile(String path) {
        File srcFile = new File(path);
        if (!srcFile.exists())
            return null;
        String md5 = FileMD5Verify.getMD5(path);
        String dest = Constant.SD_CARK_ROOT + "/DCIM/Camera/" + System.currentTimeMillis() + ".jpg";
        if(StringUtils.isNotBlank(md5)) {
            String temp = Constant.SD_CARK_ROOT + "/DCIM/Camera/" + md5 + ".jpg";
            File tempFile = new File(temp);
            if(tempFile.exists()) {
                return tempFile.getPath();
            }

            dest = temp;
        }
//        File targetFile = new File(srcFile.getParentFile(), System.currentTimeMillis() + ".jpg");
        File targetFile = new File(dest);

        //移动文件到缓存
        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            fosfrom = new FileInputStream(srcFile);
            fosto = new FileOutputStream(targetFile);

            byte[] buffer = new byte[1024];

            int count;
            while ((count = fosfrom.read(buffer)) > 0) {
                fosto.write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "ERROR";
        } finally {
            try {
                if (fosfrom != null) {
                    fosfrom.close();
                }

                if (fosto != null) {
                    fosto.flush();
                    fosto.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return targetFile.getPath();
    }

    public static boolean copyToFile(String src, File target) {
        File srcFile = new File(src);
        if (!srcFile.exists())
            return false;

        //移动文件到缓存
        FileInputStream fosfrom = null;
        FileOutputStream fosto = null;
        try {
            fosfrom = new FileInputStream(srcFile);
            fosto = new FileOutputStream(target);

            byte[] buffer = new byte[1024];

            int count;
            while ((count = fosfrom.read(buffer)) > 0) {
                fosto.write(buffer, 0, count);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            try {
                if (fosfrom != null) {
                    fosfrom.close();
                }

                if (fosto != null) {
                    fosto.flush();
                    fosto.close();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        return true;
    }

}
