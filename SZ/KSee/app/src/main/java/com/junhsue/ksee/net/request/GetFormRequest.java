package com.junhsue.ksee.net.request;

import java.util.Map;

import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * get 请求
 *
 * Created by longer on 17/3/19.
 */

public class GetFormRequest extends OKHttpRequest {



    public GetFormRequest(String url, Map<String, String> params) {
        super(url, params);
    }


    @Override
    protected RequestBody buildRequestBody() {
        return null;
    }



    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.get().build();
    }


    /**
     * 头部信息获取
     * @return
     */
    @Override
    protected Map<String, String> getHeads() {
        return null;
    }
}
