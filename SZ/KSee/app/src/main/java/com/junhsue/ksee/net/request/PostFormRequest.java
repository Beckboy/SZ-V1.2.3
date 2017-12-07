package com.junhsue.ksee.net.request;

import android.util.Log;

import com.junhsue.ksee.entity.UserInfo;
import com.junhsue.ksee.frame.MyApplication;
import com.junhsue.ksee.net.OKHttpUtils;
import com.junhsue.ksee.profile.UserProfileService;
import com.junhsue.ksee.utils.CommonUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by longer on 17/3/19.
 */

public class PostFormRequest extends OKHttpRequest {

    private  static  String TAG="PostFormRequest";

    public PostFormRequest(String url, Map<String, String> params) {
        super(url, params);
    }


    public PostFormRequest(String url, Map<String, String> params,Map<String,String> headers) {
        super(url, params,headers);
    }


    @Override
    protected RequestBody buildRequestBody() {
        FormBody.Builder builder = new FormBody.Builder();
        addParams(builder);
        Log.w(TAG,"response---url="+url+",params="+getParameter());
        FormBody formBody = builder.build();
        return formBody;
    }


    @Override
    protected Request buildRequest(RequestBody requestBody) {
        return builder.post(requestBody).build();
    }

    @Override
    protected Map<String, String> getHeads() {
        Map<String,String> heads=new HashMap<String,String>();
        heads.put("token", MyApplication.getToken());
        //添加版本号
        String versionName=CommonUtils.getInstance().getAppVersionName();
        heads.put("version", versionName);
        return heads;
    }


}
