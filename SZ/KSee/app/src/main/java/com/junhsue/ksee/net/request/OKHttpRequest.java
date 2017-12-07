package com.junhsue.ksee.net.request;

import android.net.Uri;

import java.util.Map;

import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by longer on 17/3/19.
 */

public abstract class OKHttpRequest {


    protected Request.Builder builder = new Request.Builder();


    protected String url;

    protected Map<String, String> params;

    //请求头部
    protected Map<String, String> headers;


    public OKHttpRequest(String url, Map<String, String> params) {
        this.url = url;
        this.params = params;
        initBuidler();
    }


    public OKHttpRequest(String url, Map<String, String> params, Map<String, String> headers) {
        this.url = url;
        this.params = params;
        this.headers = headers;
        initBuidler();
    }


    private void initBuidler() {
       builder.url(url);
       appendHeaders();
    }




    protected abstract RequestBody buildRequestBody();


    protected abstract Request buildRequest(RequestBody requestBody);



    public Request generateRequest()
    {
        RequestBody requestBody = buildRequestBody();
        Request request = buildRequest(requestBody);
        return request;
    }


    protected RequestBody wrapRequestBody(RequestBody requestBody, final Callback callback)
    {
        return requestBody;
    }


    public void setHeaders(Map<String, String> headers) {
        this.headers = headers;
    }



    protected void addParams(FormBody.Builder builder) {
        if (params != null) {
            for (String key : params.keySet()) {
                builder.add(key, params.get(key));
            }
        }
    }


    /**
     * 获取请求参数*/
    public String getParameter() {
        Uri uri = Uri.parse(url);
        Uri.Builder builder = uri.buildUpon();
        for (Map.Entry<String, ?> entry : params.entrySet()) {
            builder.appendQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        return builder.build().getQuery();
    }


    //添加头部
    protected void appendHeaders() {
        Headers.Builder headerBuilder = new Headers.Builder();
         this.headers=getHeads();
        if (headers == null || headers.isEmpty()) return;
        for (String key : headers.keySet()) {
            headerBuilder.add(key, headers.get(key));
        }
        builder.headers(headerBuilder.build());
    }


    /**
     * 获取头部信息
     * @return
     */
    protected  abstract  Map<String,String> getHeads();
}
