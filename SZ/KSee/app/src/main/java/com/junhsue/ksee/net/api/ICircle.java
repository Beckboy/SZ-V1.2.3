package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;


/**
 * 社首页圈子相关接口
 * Created by longer on 17/10/25.
 */

public interface ICircle {


    /**
     * 获取所有圈子
     *
     * @param requestCallback
     * @param <T>
     */
    <T> void getCircleList(RequestCallback<T> requestCallback);


    /**
     * 根据子集获取圈子列表
     *
     * @param parent_id       圈子父集id
     * @param requestCallback
     * @param <T>
     */
    <T> void getCircleList(String parent_id, RequestCallback<T> requestCallback);


    /**
     * 我关注的圈子
     */
    <T> void myFavouriteCircle(RequestCallback<T> requestCallback);


    /**
     * 推荐的圈子
     */
    <T> void circleRecommend(String page,String pagesize,RequestCallback<T> requestCallback);


    /**
     * 关注圈子
     */

    <T> void circleFavourite(String circle_id, RequestCallback<T> requestCallback);


    /**
     * 取消关注圈子
     */
    <T> void circleUnFavourite(String circle_id, RequestCallback<T> requestCallback);

}

