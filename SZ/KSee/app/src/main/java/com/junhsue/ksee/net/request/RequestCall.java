package com.junhsue.ksee.net.request;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.common.Trace;
import com.junhsue.ksee.frame.ScreenManager;
import com.junhsue.ksee.net.OKHttpUtils;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.error.NetErrorParser;
import com.junhsue.ksee.net.error.NetResultCode;
import com.junhsue.ksee.utils.PopWindowTokenErrorUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Console;
import java.io.IOException;
import java.util.Stack;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by longer on 17/3/19.
 */

public class RequestCall<T> extends BaseRequestCall<T> implements Callback {


    private static String TAG = "RequestCall";

    //请求返回编码
    private static String PARAMS_RESPONSE_CODE = "params_response_code";
    //请求返回信息
    private static String PARAMS_RESPONSE_BODY = "params_response_body";

    private Gson mGson = new Gson();

    private Class<T> mClass;


    //是否解析网络返回请求体
    private boolean isParser = true;

    private OKHttpRequest mOkHttpRequest;

    public RequestCallback<T> mRequestCallBack;

    public RequestCall(OKHttpRequest okHttpRequest, RequestCallback<T> callBack, Class<T> clazz) {
        this.mOkHttpRequest = okHttpRequest;
        this.mRequestCallBack = callBack;
        this.mClass = clazz;
    }


    public Call buildCall() {
        Request request = mOkHttpRequest.generateRequest();
        return OKHttpUtils.getInstance().getOkHttpClient().newCall(request);
    }


    /**
     * 执行网络请求
     */
    public void requestExcute() {
        buildCall().enqueue(this);
    }


    @Override
    public void onFailure(Call call, IOException e) {
        Log.d(TAG, "onResponse is time out");
        String errorMsg = NetErrorParser.parserCode(NetResultCode.CODE_ERROR_HTTP);
        sendResponseUI(NetResultCode.CODE_ERROR_HTTP, errorMsg);
    }


    @Override
    public void onResponse(Call call, Response response) throws IOException {
        String strResponse = response.body().string();
        Log.w(TAG, "onResponse=" + strResponse);
        if (isParser) {
            responseParser(strResponse);
        } else {
            sendResponseUI(NetResultCode.CODE_SUCCESS_HTTP, strResponse);
        }

    }


    /**
     * 解析服务器返回来的字符串
     *
     * @param response
     */
    private void responseParser(String response) {
        JSONObject jsonObject = null;
        int code = 0;
        String responseStr = "";
        try {
//            //去掉字符串中的\
//            if(response.contains("\\")){
//                response=response.replaceAll("\\\\","");
//            }
            if (!response.contains("msg")) {
                return;
            }
            jsonObject = new JSONObject(response);
            code = jsonObject.getInt("errno");
            responseStr = jsonObject.getString("msg");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        sendResponseUI(code, responseStr);
    }


    private void sendResponseUI(final int responseCode, final String responseBody) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                Message message = Message.obtain();
                Bundle bundle = message.getData();
                bundle.putInt(PARAMS_RESPONSE_CODE, responseCode);
                bundle.putString(PARAMS_RESPONSE_BODY, responseBody);
                handler.handleMessage(message);
            }
        });

    }


    private Handler handler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            if (null == bundle) return;
            int responseCode = bundle.getInt(PARAMS_RESPONSE_CODE);

            if (NetResultCode.CODE_SUCCESS == responseCode || NetResultCode.CODE_SUCCESS_HTTP == responseCode) {
                String response = bundle.getString(PARAMS_RESPONSE_BODY);
                if (null != mRequestCallBack) {
                    mRequestCallBack.onSuccess(mGson.fromJson(response, mClass));
                }
            } else {
                if (null != mRequestCallBack) {
                    String errorMsg = NetErrorParser.parserCode(responseCode);
                    mRequestCallBack.onError(responseCode, errorMsg);
                }
            }

        }
    };

    public void setParser(boolean parser) {
        isParser = parser;
    }


}
