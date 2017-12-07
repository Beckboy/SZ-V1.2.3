package com.junhsue.ksee.net.request;

import com.junhsue.ksee.net.OKHttpUtils;

import okhttp3.Call;
import okhttp3.Request;

/**
 * Created by longer on 17/5/25.
 */

public abstract class BaseRequestCall<T> {


    protected abstract Call buildCall();


    public abstract void requestExcute();

}
