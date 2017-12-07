package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.RequestCall;

/**
 * Created by Sugar on 17/10/20.
 */

public interface INewSocialCircle {

    /**
     * 帖子详情页
     *
     * @param id
     * @param requestCallback
     * @param <T>
     */
    public <T> void getPostDetail(String id, RequestCallback<T> requestCallback);

    /**
     * 圈子列表
     *
     * @param parent_id
     * @param requestCallback
     * @param <T>
     */
    public <T> void getCircleList(String parent_id, RequestCallback<T> requestCallback);

    /**
     * 帖子列表
     *
     * @param circle_id
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    public <T> void getCircleBarList(String circle_id, String page, String pagesize, String top, RequestCallback<T> requestCallback);


    /**
     * 帖子列表
     *
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    public <T> void getCircleBarList(String page, String pagesize, String top, RequestCallback<T> requestCallback);


    /**
     * 发布帖子
     *
     * @param title
     * @param description
     * @param is_anonymous
     * @param circle_id
     * @param poster
     * @param requestCallback
     * @param <T>
     */
    public <T> void sendPost(String title, String description, String is_anonymous, String circle_id, String poster, RequestCallback<T> requestCallback);

    /**
     * 删除帖子
     *
     * @param id
     * @param requestCallback
     * @param <T>
     */
    public <T> void deletePost(String id, RequestCallback<T> requestCallback);

    /**
     * 我的发言列表
     *
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    public <T> void getPostList(String page, String pagesize, RequestCallback<T> requestCallback);

    /**
     * 取消收藏(帖子)
     *
     * @param business_id
     * @param id
     * @param requestCallback
     * @param <T>
     */
    public <T> void unfavoritePostCollect(String business_id, String id, RequestCallback<T> requestCallback);

    /**
     * 我的收藏列表(帖子)
     *
     * @param business_id
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    public <T> void getPostCollect(String business_id, String page, String pagesize, RequestCallback<T> requestCallback);


    /**
     * @param page
     * @param pagesize
     * @param id
     * @param requestCallback
     * @param <T>
     */
    public <T> void getPostCommentList(int page, int pagesize, String id, RequestCallback<T> requestCallback);

    /**
     * 帖子收藏
     *
     * @param id
     * @param business_id
     * @param requestCallback
     * @param <T>
     */
    public <T> void collectPost(String id, String business_id, RequestCallback<T> requestCallback);

    /**
     * 帖子取消收藏
     *
     * @param id
     * @param business_id
     * @param requestCallback
     * @param <T>
     */
    public <T> void cancelCollectPost(String id, String business_id, RequestCallback<T> requestCallback);

    /**
     * 帖子点赞
     *
     * @param content_id
     * @param business_id
     * @param requestCallback
     * @param <T>
     */
    public <T> void postCommentApproval(String content_id, String business_id, RequestCallback<T> requestCallback);

    /**
     * 帖子取消点赞
     *
     * @param content_id
     * @param business_id
     * @param requestCallback
     * @param <T>
     */
    public <T> void postCommentCancelApproval(String content_id, String business_id, RequestCallback<T> requestCallback);

    /**
     * 帖子评论或者评论回复
     *
     * @param content_id
     * @param business_id
     * @param content
     * @param requestCallback
     * @param <T>
     */
    public <T> void postCommentReplyCreate(String content_id, String business_id, String content, RequestCallback<T> requestCallback);

    /**
     * 举报的选项列表
     *
     * @param requestCallback
     * @param <T>
     */
    public <T> void postReportList(RequestCallback<T> requestCallback);

    /**
     * 举报发布
     */
    public <T> void senderReportToServer(String report_type_id, String content_id, String business_id, String content, RequestCallback<T> requestCallback);

    /**
     * 评论详情列表
     *
     * @param id
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    public <T> void loadCommentDetail(String id, int page, int pagesize, RequestCallback<T> requestCallback);

    /**
     * 删除帖子
     *
     * @param <T>
     */
    public <T> void deleteComment(String id, RequestCallback<T> requestCallback);

    /**
     * 推荐帖子
     * @param id
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    public <T> void loadRecommendlist(String id,int page, int pagesize, RequestCallback<T> requestCallback);


}
