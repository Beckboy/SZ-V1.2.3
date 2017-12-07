package com.junhsue.ksee.utils;

import com.junhsue.ksee.net.api.RequestType;
import com.junhsue.ksee.net.api.RequestVo;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * Created by Sugar on 16/10/9.
 */

public class JHOkHttpUtils {

    private static JHOkHttpUtils mInstance;
    private static OkHttpClient mOkHttpClient;
    private HttpRequestCallBack httpRequestCallBack;


    public synchronized static JHOkHttpUtils getInstance() {
        if (mInstance == null) {
            mInstance = new JHOkHttpUtils();
        }
        return mInstance;
    }


    /**
     * 单例OkHttpClient类
     *
     * @return
     */
    public synchronized static OkHttpClient getOkhttpInstance() {
        if (mOkHttpClient == null) {
            mOkHttpClient = new OkHttpClient();
        }
        return mOkHttpClient;
    }

    public void setHttpRequestCallBack(HttpRequestCallBack callBack) {
        this.httpRequestCallBack = callBack;
    }


    public void executeRequest(RequestVo requestVo) {

        Request request = buildRequest(requestVo);
        deliveryResult(httpRequestCallBack, request);

    }


    public interface HttpRequestCallBack {

        public void onError(Request request, Exception e);

        public void onResponse(Response response);
    }


    public Request buildRequest(RequestVo requestvo) {

        Request request = null;

        if (StringUtils.isBlank(requestvo.url)) {
            return null;
        }

        Set<Map.Entry<String, String>> entries = requestvo.requestParams.entrySet();

        if (requestvo.requestType == RequestType.GET) {//get请求
            String url = requestvo.url;

            for (Map.Entry<String, String> entry : entries) {
                url = url + "?" + entry.getKey() + "=" + entry.getValue();
            }

            return new Request.Builder()
                    .url(url)
                    .build();


        } else if (requestvo.requestType == RequestType.POST) {//post 请求


            FormBody.Builder builder = new FormBody.Builder();

            for (Map.Entry<String, String> entry : entries) {
                builder.add(entry.getKey(), entry.getValue());
            }
            RequestBody requestBody = builder.build();

            return new Request.Builder()
                    .url(requestvo.url)
                    .post(requestBody)
                    .build();

        }


        return request;

    }


    private void deliveryResult(final HttpRequestCallBack requestCallBack, Request request) {

        getOkhttpInstance().newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onError(call.request(), e);

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (requestCallBack == null) {
                    return;
                }
                requestCallBack.onResponse(response);

            }
        });
    }


    private Param[] mapCastToParams(Map<String, String> params) {
        if (params == null) return new Param[0];

        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

}
