package com.junhsue.ksee.net.callback;

/**
 * 接口回调类
 * Created by longer on 17/3/19.
 */


public interface RequestCallback<T> {


    void onError(int errorCode, String errorMsg);

    void onSuccess(T response);
}
