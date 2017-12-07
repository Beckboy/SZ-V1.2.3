package com.junhsue.ksee.net.api;

import com.junhsue.ksee.dto.HomeContentDTO;
import com.junhsue.ksee.dto.RealizeArticleDTO;

import com.junhsue.ksee.dto.RealizeArticleTagsDTO;
import com.junhsue.ksee.dto.SelectedQuestionDTO;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.GetFormRequest;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by longer on 17/8/4.
 */

public class OkHttpRealizeImpl extends  BaseClientApi implements  IRealizeArticle {

    private static OkHttpRealizeImpl mInstance;

    public static OkHttpRealizeImpl  newInstance(){
        if (null == mInstance) {
            synchronized (OkHttpRealizeImpl.class) {
                if (null == mInstance)
                    mInstance = new OkHttpRealizeImpl();
            }
        }
        return mInstance;
    }


    /**
     * 获取文章头部标签列表
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getArticleHeadList(RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<String,String>();
        //
        String url = RequestUrl.REALIZE_ARTICLE_TAGS_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url,params);
        RequestCall<RealizeArticleTagsDTO> requestCall = new RequestCall<RealizeArticleTagsDTO>(postFormRequest, (RequestCallback<RealizeArticleTagsDTO>) requestCallback,RealizeArticleTagsDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 获取文章
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getArticleList(String page, String pagesize, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("page",page);
        params.put("pagesize",pagesize);
        //
        String url = RequestUrl.REALIZE_ARTICLE_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url,params);
        RequestCall<RealizeArticleDTO> requestCall = new RequestCall<RealizeArticleDTO>(postFormRequest,
                (RequestCallback<RealizeArticleDTO>) requestCallback,RealizeArticleDTO.class);
        submitRequset(requestCall);
    }


    /**
     * 获取文章列表
     * @param page
     * @param pagesize
     * @param tag_id 筛选文章的id标签
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getArticleList(String page, String pagesize, String tag_id, RequestCallback<T> requestCallback) {
        HashMap<String,String> params=new HashMap<String,String>();
        params.put("page",page);
        params.put("pagesize",pagesize);
        params.put("tag_id",tag_id);
        //
        String url = RequestUrl.REALIZE_ARTICLE_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url,params);
        RequestCall<RealizeArticleDTO> requestCall = new RequestCall<RealizeArticleDTO>(postFormRequest, (RequestCallback<RealizeArticleDTO>) requestCallback,RealizeArticleDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadSearchResultRealizeList(String key, int pagesize, int page, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        params.put("keyword", key);
        String url = RequestUrl.SEARCH_REALIZE_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<RealizeArticleDTO> requestCall = new RequestCall<RealizeArticleDTO>(postFormRequest, (RequestCallback<RealizeArticleDTO>) requestCallback, RealizeArticleDTO.class);
        submitRequset(requestCall);
    }

}
