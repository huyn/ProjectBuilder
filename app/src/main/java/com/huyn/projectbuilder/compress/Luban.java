package com.huyn.projectbuilder.compress;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;

import com.huyn.projectbuilder.utils.Constant;
import com.huyn.projectbuilder.utils.FileMD5Verify;
import com.huyn.projectbuilder.utils.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.huyn.projectbuilder.compress.Preconditions.checkNotNull;

public class Luban {

    private static final int FIRST_GEAR = 1;
    public static final int THIRD_GEAR = 3;

    private static final String TAG = "Luban";

    private OnCompressListener compressListener;
    private File mFile;
    private int gear = THIRD_GEAR;
    private String filePath;

    private Handler mHandler;
    private static final int CODE_SUCCESS = 20001;
    private static final int CODE_FAIL = 20002;
    private static final int CODE_START = 20003;

    private Luban(Context context) {
        mHandler = new Handler(context.getMainLooper()) {
            @Override
            public void handleMessage(Message msg) {
                if(compressListener != null) {
                    switch (msg.what) {
                        case CODE_SUCCESS:
                            compressListener.onSuccess((String) msg.obj);
                            break;
                        case CODE_FAIL:
                            compressListener.onError();
                            break;
                        case CODE_START:
                            compressListener.onStart();
                            break;
                    }
                }
            }
        };
    }

    private void sendHandlerMessage(int code, String value) {
        if(mHandler != null) {
            if(StringUtils.isNotBlank(value)) {
                Message message = mHandler.obtainMessage(CODE_SUCCESS);
                message.obj = value;
                mHandler.sendMessage(message);
            } else {
                mHandler.sendEmptyMessage(code);
            }
        }
    }

    public static Luban get(Context context) {
        return new Luban(context);
    }

    public Luban launch() {
        checkNotNull(mFile, "the image file cannot be null, please call .load() before this method!");

        if (compressListener != null) compressListener.onStart();

        String md5 = FileMD5Verify.getMD5(mFile.getPath());
        filePath = Constant.SD_CARK_ROOT + "/DCIM/Camera/" + System.currentTimeMillis() + ".jpg";
        if(StringUtils.isNotBlank(md5)) {
            String temp = Constant.SD_CARK_ROOT + "/DCIM/Camera/" + md5 + ".jpg";
            File tempFile = new File(temp);
            if(tempFile.exists()) {
                sendHandlerMessage(CODE_SUCCESS, tempFile.getPath());
                return this;
            }

            filePath = temp;
        }

        if (gear == Luban.FIRST_GEAR) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File target = firstCompress(mFile);
                        if(target != null && target.exists())
                            sendHandlerMessage(CODE_SUCCESS, target.getPath());
                        else
                            sendHandlerMessage(CODE_FAIL, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendHandlerMessage(CODE_FAIL, null);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        sendHandlerMessage(CODE_FAIL, null);
                    }
                }
            }).start();
        } else if (gear == Luban.THIRD_GEAR) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        File target = thirdCompress(mFile);
                        if(target != null && target.exists())
                            sendHandlerMessage(CODE_SUCCESS, target.getPath());
                        else
                            sendHandlerMessage(CODE_FAIL, null);
                    } catch (Exception e) {
                        e.printStackTrace();
                        sendHandlerMessage(CODE_FAIL, null);
                    } catch (OutOfMemoryError e) {
                        e.printStackTrace();
                        sendHandlerMessage(CODE_FAIL, null);
                    }
                }
            }).start();
        }
        return this;
    }

    public Luban load(File file) {
        mFile = file;
        return this;
    }

    public Luban setCompressListener(OnCompressListener listener) {
        compressListener = listener;
        return this;
    }

    public Luban putGear(int gear) {
        this.gear = gear;
        return this;
    }

    private File thirdCompress(@NonNull File file) {
        String thumb = filePath;

        double size;
        String filePath = file.getAbsolutePath();

        int angle = getImageSpinAngle(filePath);
        int width = getImageSize(filePath)[0];
        int height = getImageSize(filePath)[1];
        int thumbW = width % 2 == 1 ? width + 1 : width;
        int thumbH = height % 2 == 1 ? height + 1 : height;

        width = thumbW > thumbH ? thumbH : thumbW;
        height = thumbW > thumbH ? thumbW : thumbH;

        double scale = ((double) width / height);

        if (scale <= 1 && scale > 0.5625) {
            if (height < 1664) {
                if (file.length() / 1024 < 150) return file;

                size = (width * height) / Math.pow(1664, 2) * 150;
                size = size < 60 ? 60 : size;
            } else if (height >= 1664 && height < 4990) {
                thumbW = width / 2;
                thumbH = height / 2;
                size = (thumbW * thumbH) / Math.pow(2495, 2) * 300;
                size = size < 60 ? 60 : size;
            } else if (height >= 4990 && height < 10240) {
                thumbW = width / 4;
                thumbH = height / 4;
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            } else {
                double multiple = height / 1280 == 0 ? 1 : height * 1f / 1280;
                thumbW = (int) (width / multiple);
                thumbH = (int) (height / multiple);
                size = (thumbW * thumbH) / Math.pow(2560, 2) * 300;
                size = size < 100 ? 100 : size;
            }
        } else if (scale <= 0.5625 && scale > 0.5) {
            if (height < 1280 && file.length() / 1024 < 200) return file;

            double multiple = height / 1280 == 0 ? 1 : height * 1f / 1280;
            thumbW = (int) (width / multiple);
            thumbH = (int) (height / multiple);
            size = (thumbW * thumbH) / (1440.0 * 2560.0) * 400;
            size = size < 100 ? 100 : size;
        } else {
            double multiple = Math.ceil(height / (1280.0 / scale));
            thumbW = (int) (width / multiple);
            thumbH = (int) (height / multiple);
            size = ((thumbW * thumbH) / (1280.0 * (1280 / scale))) * 500;
            size = size < 100 ? 100 : size;
        }

        return compress(filePath, thumb, thumbW, thumbH, angle, (long) size);
    }

    private File firstCompress(@NonNull File file) {
        int minSize = 60;
        int longSide = 720;
        int shortSide = 1280;

        String filePath = file.getAbsolutePath();
        String thumbFilePath = filePath;

        long size = 0;
        long maxSize = file.length() / 5;

        int angle = getImageSpinAngle(filePath);
        int[] imgSize = getImageSize(filePath);
        int width = 0, height = 0;
        if (imgSize[0] <= imgSize[1]) {
            double scale = (double) imgSize[0] / (double) imgSize[1];
            if (scale <= 1.0 && scale > 0.5625) {
                width = imgSize[0] > shortSide ? shortSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = minSize;
            } else if (scale <= 0.5625) {
                height = imgSize[1] > longSide ? longSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = maxSize;
            }
        } else {
            double scale = (double) imgSize[1] / (double) imgSize[0];
            if (scale <= 1.0 && scale > 0.5625) {
                height = imgSize[1] > shortSide ? shortSide : imgSize[1];
                width = height * imgSize[0] / imgSize[1];
                size = minSize;
            } else if (scale <= 0.5625) {
                width = imgSize[0] > longSide ? longSide : imgSize[0];
                height = width * imgSize[1] / imgSize[0];
                size = maxSize;
            }
        }

        return compress(filePath, thumbFilePath, width, height, angle, size);
    }

    /**
     * obtain the image's width and height
     *
     * @param imagePath the path of image
     */
    public int[] getImageSize(String imagePath) {
        int[] res = new int[2];

        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inSampleSize = 1;
        BitmapFactory.decodeFile(imagePath, options);

        res[0] = options.outWidth;
        res[1] = options.outHeight;

        return res;
    }

    /**
     * obtain the thumbnail that specify the size
     *
     * @param imagePath the target image path
     * @param width     the width of thumbnail
     * @param height    the height of thumbnail
     * @return {@link Bitmap}
     */
    private Bitmap compress(String imagePath, int width, int height) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imagePath, options);

        int outH = options.outHeight;
        int outW = options.outWidth;
        int inSampleSize = 1;

        if (outH > height || outW > width) {
            int halfH = outH / 2;
            int halfW = outW / 2;

            while ((halfH / inSampleSize) > height && (halfW / inSampleSize) > width) {
                inSampleSize *= 2;
            }
        }

        options.inSampleSize = inSampleSize;

        options.inJustDecodeBounds = false;

        int heightRatio = (int) Math.ceil(options.outHeight / (float) height);
        int widthRatio = (int) Math.ceil(options.outWidth / (float) width);

        if (heightRatio > 1 || widthRatio > 1) {
            if (heightRatio > widthRatio) {
                options.inSampleSize = heightRatio;
            } else {
                options.inSampleSize = widthRatio;
            }
        }
        options.inJustDecodeBounds = false;

        return BitmapFactory.decodeFile(imagePath, options);
    }

    /**
     * obtain the image rotation angle
     *
     * @param path path of target image
     */
    private int getImageSpinAngle(String path) {
        int degree = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 指定参数压缩图片
     * create the thumbnail with the true rotate angle
     *
     * @param largeImagePath the big image path
     * @param thumbFilePath  the thumbnail path
     * @param width          width of thumbnail
     * @param height         height of thumbnail
     * @param angle          rotation angle of thumbnail
     * @param size           the file size of image
     */
    private File compress(String largeImagePath, String thumbFilePath, int width, int height, int angle, long size) {
        Bitmap thbBitmap = compress(largeImagePath, width, height);

        thbBitmap = rotatingImage(angle, thbBitmap);

        return saveImage(thumbFilePath, thbBitmap, size);
    }

    /**
     * 旋转图片
     * rotate the image with specified angle
     *
     * @param angle  the angle will be rotating 旋转的角度
     * @param bitmap target image               目标图片
     */
    private static Bitmap rotatingImage(int angle, Bitmap bitmap) {
        //rotate image
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);

        //create a new image
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    }

    /**
     * 保存图片到指定路径
     * Save image with specified size
     *
     * @param filePath the image file save path 储存路径
     * @param bitmap   the image what be save   目标图片
     * @param size     the file size of image   期望大小
     */
    private File saveImage(String filePath, Bitmap bitmap, long size) {
        checkNotNull(bitmap, TAG + "bitmap cannot be null");

        File result = new File(filePath.substring(0, filePath.lastIndexOf("/")));

        if (!result.exists() && !result.mkdirs()) return null;

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        int options = 100;
        bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);

        while (stream.toByteArray().length / 1024 > size && options > 6) {
            stream.reset();
            options -= 6;
            bitmap.compress(Bitmap.CompressFormat.JPEG, options, stream);
        }

        try {
            FileOutputStream fos = new FileOutputStream(filePath);
            fos.write(stream.toByteArray());
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new File(filePath);
    }
}