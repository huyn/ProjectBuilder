package com.huyn.projectbuilder;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by huyaonan on 16/10/8.
 */
public class HttpRequestLoader {
    
    private static HttpRequestLoader mInstance = null;
    private RequestQueue mRequestQueue;
    private Context mContext;

    private HttpRequestLoader(Context context) {
        mContext = context;
        mRequestQueue = Volley.newRequestQueue(context);
    }

    public static HttpRequestLoader getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new HttpRequestLoader(context.getApplicationContext());
        }
        return mInstance;
    }

    public void stop() {
        try {
            if (mRequestQueue != null)
                mRequestQueue.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void start() {
        try {
            if (mRequestQueue != null)
                mRequestQueue.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void cancelRequest(Object tag) {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(tag);
    }

    public void cancelAllRequest() {
        if (mRequestQueue != null)
            mRequestQueue.cancelAll(new RequestQueue.RequestFilter() {

                @Override
                public boolean apply(Request<?> request) {
                    return true;
                }
            });
    }

    public void startHttpLoader(Request<?> request) {
        mRequestQueue.add(request);
    }

    public void release() {
        this.mRequestQueue = null;
        mInstance = null;
    }
}