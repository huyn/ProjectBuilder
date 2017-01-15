package com.huyn.projectbuilder;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.FutureTarget;
import com.bumptech.glide.request.target.Target;
import com.huyn.projectbuilder.glide.CropCircleTransformation;

import java.io.File;

/**
 * Created by huyaonan on 16/10/8.
 */
public class ImageLoader {

    private Context mContext;
    private static ImageLoader mInstance;
    private static Handler mHandler;
    private static CropCircleTransformation mCircleInstance;

    private ImageLoader(Context context) {
        this.mContext = context.getApplicationContext();
    }

    public static synchronized ImageLoader getInstance(Context context) {
        if(mInstance == null) {
            mInstance = new ImageLoader(context);
            mHandler = new Handler(Looper.getMainLooper());
            mCircleInstance = new CropCircleTransformation(context);
        }
        return mInstance;
    }

    public void preloadBitmap(final String url, final int width, final int height, final IPreloadCallback<Bitmap> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap myBitmap = Glide.with(mContext)
                            .load(url)
                            .asBitmap()
                            .centerCrop()
                            .into(width, height)
                            .get();
                    if(callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(myBitmap);
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail();
                        }
                    });
                }
            }
        }).start();
    }

    public void preloadBitmap(final String url, final IPreloadCallback<Bitmap> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final Bitmap myBitmap = Glide.with(mContext)
                            .load(url)
                            .asBitmap()
                            .centerCrop()
                            .into(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL)
                            .get();
                    if(callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(myBitmap);
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail();
                        }
                    });
                }
            }
        }).start();
    }

    public void preloadBitmapToFile(final String url, final int width, final int height, final IPreloadCallback<String> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(mContext)
                        .load(url)
                        .downloadOnly(width, height);
                try {
                    final File cacheFile = future.get();
                    if(callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(cacheFile.getAbsolutePath());
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail();
                        }
                    });
                }
            }
        }).start();
    }

    public void preloadBitmapToFile(final String url, final IPreloadCallback<String> callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                FutureTarget<File> future = Glide.with(mContext)
                        .load(url)
                        .downloadOnly(Target.SIZE_ORIGINAL, Target.SIZE_ORIGINAL);
                try {
                    final File cacheFile = future.get();
                    if(callback != null) {
                        mHandler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(cacheFile.getAbsolutePath());
                            }
                        });
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if(callback != null) {
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onFail();
                        }
                    });
                }
            }
        }).start();
    }

    public interface IPreloadCallback<T> {
        public void onSuccess(T t);
        public void onFail();
    }

    public void fillImage(ImageView imageView, String url) {
        Glide.with(mContext).load(url).into(imageView);
    }

    public void fillImage(ImageView imageView, Drawable drawable, String url) {
        Glide.with(mContext).load(url).placeholder(drawable).error(drawable).into(imageView);
    }

    public void fillImage(ImageView imageView, String url, int width, int height) {
        Glide.with(mContext).load(url).override(width, height).into(imageView);
    }

    public void fillImage(ImageView imageView, Drawable drawable, String url, int width, int height) {
        Glide.with(mContext).load(url).override(width, height).placeholder(drawable).error(drawable).into(imageView);
    }

    public void fillImage(ImageView imageView, String url, int width, int height, int radius, int sigma) {
        Glide.with(mContext).load(url).override(width, height).into(imageView);
    }

    public void fillImage(ImageView imageView, Drawable drawable, String url, int width, int height, int radius, int sigma) {
        Glide.with(mContext).load(url).override(width, height).placeholder(drawable).error(drawable).into(imageView);
    }

    public void fillRoundImage(ImageView imageView, String url) {
        Glide.with(mContext).load(url).bitmapTransform(mCircleInstance).into(imageView);
    }

    public void fillRoundImage(ImageView imageView, Drawable drawable, String url) {
        Glide.with(mContext).load(url).bitmapTransform(mCircleInstance).placeholder(drawable).error(drawable).into(imageView);
    }

    public void fillRoundImage(ImageView imageView, String url, int width, int height) {
        Glide.with(mContext).load(url).override(width, height).bitmapTransform(mCircleInstance).into(imageView);
    }

    public void fillRoundImage(ImageView imageView, Drawable drawable, String url, int width, int height) {
        Glide.with(mContext).load(url).override(width, height).bitmapTransform(mCircleInstance).placeholder(drawable).error(drawable).into(imageView);
    }

}
