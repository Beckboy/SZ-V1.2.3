package com.junhsue.ksee.net.api;

import android.content.Context;
import android.net.Uri;

import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.OKHttpUtils;
import com.junhsue.ksee.net.request.BaseRequestCall;
import com.junhsue.ksee.net.request.RequestCall;

import java.util.Map;

/**
 *
 * Created by longer on 17/3/16 in Junhsue.
 */

public abstract class BaseClientApi {


    private Context mContext;


    public BaseClientApi() {
        mContext = MyApplication.getApplication();
    }


    //提交请求
    protected <T> void submitRequset(BaseRequestCall<T> requestCall) {
        OKHttpUtils.getInstance().excuteRequest(requestCall);
    }


    protected final String appendParameter(String url, Map<String, ?> params) {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());

        }
        return builder.build().getQuery();
    }


}
