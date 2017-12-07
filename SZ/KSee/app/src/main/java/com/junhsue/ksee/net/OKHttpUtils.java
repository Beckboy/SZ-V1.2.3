package com.junhsue.ksee.net;


import android.content.Context;

import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.request.BaseRequestCall;
import com.junhsue.ksee.net.request.RequestCall;

import okhttp3.OkHttpClient;

/**
 * Created by longer on 17/3/19.
 */

public class OKHttpUtils  {


    private static OkHttpClient mOkHttpClient;

    private static OKHttpUtils mInstance;


    public OKHttpUtils(OkHttpClient okHttpClient) {
        if (okHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        } else {
            mOkHttpClient = okHttpClient;
        }

    }

    public static OKHttpUtils initClient(OkHttpClient okHttpClient) {
        if (mInstance == null) {
            synchronized (OKHttpUtils.class) {
                if (mInstance == null) {
                    mInstance = new OKHttpUtils(okHttpClient);
                }
            }
        }
        return mInstance;
    }

    public OkHttpClient getOkHttpClient() {
        return mOkHttpClient;
    }


    public static OKHttpUtils getInstance() {
        return initClient(null);
    }


    public static Context getContext(){return MyApplication.getApplication();}


    public <T> void excuteRequest(BaseRequestCall<T> requestCall) {
        requestCall.requestExcute();
    }

}

