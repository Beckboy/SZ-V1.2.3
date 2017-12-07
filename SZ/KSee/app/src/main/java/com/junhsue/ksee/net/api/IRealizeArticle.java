package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.RequestCall;

/**
 * 知模块文章相关
 * Created by longer on 17/8/4.
 */

public interface IRealizeArticle {

    /**
     * 获取标签列表
     */
    public <T> void getArticleHeadList(RequestCallback<T> requestCallback);

    /**
     * 获取文章列表
     */
    public <T> void getArticleList(String page, String pagesize, RequestCallback<T> requestCallback);

    /**
     * 根据tag_id筛选文章
     * 获取文章列表
     */
    public <T> void getArticleList(String page, String pagesize, String tag_id, RequestCallback<T> requestCallback);


    //干货关键字搜索结果接口
    public <T> void loadSearchResultRealizeList(String key, int pagesize, int page, RequestCallback<T> requestCallback);

}
