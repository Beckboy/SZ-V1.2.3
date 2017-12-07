package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;

/**
 * 社板块的接口
 * Created by Sugar on 17/3/22.
 */

public interface ISocialCircle {

    //精选回答接口
    public <T> void loadQuestionList(int pagesize, int page, RequestCallback<T> requestCallback);

    //搜索结果接口
    public <T> void loadSearchResultQuestionList(String key, int pagesize, int page, RequestCallback<T> requestCallback);

    //最新问题集合接口
    public <T> void loadQuestionNewest(String tag_id, int pagesize, int page, RequestCallback<T> requestCallback);

    //问答详情接口
    public <T> void loadQuestionDetail(String questionId, RequestCallback<T> requestCallback);

    //问答详情的回答列表接口
    public <T> void loadQuestionDetailAnswerList(String questionId, int pagesize, int page, RequestCallback<T> requestCallback);

    //回答发送
    public <T> void loadAnswerReplay(String questionId, String content, int type, String duration, RequestCallback<T> requestCallback);

    //邀请专家人员
    public <T> void askForProfessors(int count, String tag_id, RequestCallback<T> requestCallback);

    //提问
    public <T> void senderQuestion(String content, String description, String tags, String ids, RequestCallback<T> requestCallback);

    //收藏
    public <T> void senderFavorite(String content_id, String business_id, RequestCallback<T> requestCallback);

    //取消收藏
    public <T> void senderDeleteFavorite(String content_id, String business_id, RequestCallback<T> requestCallback);

    //点赞
    public <T> void senderApproval(String content_id, String business_id, RequestCallback<T> requestCallback);

    //取消点赞
    public <T> void senderDeleteApproval(String content_id, String business_id, RequestCallback<T> requestCallback);

    public <T> void downloadVoice(String url, RequestCallback<T> requestCallback);



}
