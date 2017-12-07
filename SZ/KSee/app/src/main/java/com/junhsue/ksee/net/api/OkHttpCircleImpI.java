package com.junhsue.ksee.net.api;

import com.junhsue.ksee.dto.CircleListDTO;
import com.junhsue.ksee.dto.HomeBannerDTO;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;

/**
 *
 * Created by longer on 17/10/25.
 */

public class OkHttpCircleImpI extends BaseClientApi implements  ICircle{


    private static  OkHttpCircleImpI mInstance;


    public static  OkHttpCircleImpI getInstance(){

        if(null==mInstance){
            synchronized (OkHttpRealizeImpl.class){
                mInstance=new OkHttpCircleImpI();
            }
        }
        return mInstance;
    }


    @Override
    public <T> void getCircleList(RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        //
        String url = RequestUrl.CIRCLE_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CircleListDTO> requestCall = new RequestCall<CircleListDTO>(postFormRequest,
                (RequestCallback<CircleListDTO>) requestCallback, CircleListDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getCircleList(String parent_id, RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("parent_id",parent_id);
        //
        String url = RequestUrl.CIRCLE_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CircleListDTO> requestCall = new RequestCall<CircleListDTO>(postFormRequest,
                (RequestCallback<CircleListDTO>) requestCallback, CircleListDTO.class);
        submitRequset(requestCall);

    }

    @Override
    public <T> void myFavouriteCircle(RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        String url = RequestUrl.CIRCLE_MY_FAVOURITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CircleListDTO> requestCall = new RequestCall<CircleListDTO>(postFormRequest,
                (RequestCallback<CircleListDTO>) requestCallback, CircleListDTO.class);
        submitRequset(requestCall);

    }

    @Override
    public <T> void circleRecommend(String page, String pagesize, RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("page",page);
        params.put("pagesize",pagesize);
        String url = RequestUrl.CIRCLE_RECOMMEND;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CircleListDTO> requestCall = new RequestCall<CircleListDTO>(postFormRequest,
                (RequestCallback<CircleListDTO>) requestCallback, CircleListDTO.class);
        submitRequset(requestCall);

    }

    @Override
    public <T> void circleFavourite(String circle_id, RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("circle_id",circle_id);
        String url = RequestUrl.CIRCLE_FAVOURITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CommonResultEntity> requestCall = new RequestCall<CommonResultEntity>(postFormRequest,
                (RequestCallback<CommonResultEntity>) requestCallback, CommonResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void circleUnFavourite(String circle_id, RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("circle_id",circle_id);
        String url = RequestUrl.CIRCLE_UN_FAVOURITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CommonResultEntity> requestCall = new RequestCall<CommonResultEntity>(postFormRequest,
                (RequestCallback<CommonResultEntity>) requestCallback, CommonResultEntity.class);
        submitRequset(requestCall);
    }
}
