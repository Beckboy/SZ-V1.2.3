package com.junhsue.ksee.net.api;

import com.junhsue.ksee.dto.CircleCircleListDTO;
import com.junhsue.ksee.dto.PostDetailListDTO;
import com.junhsue.ksee.dto.PostSecondDTO;
import com.junhsue.ksee.dto.ReportSelectDTO;
import com.junhsue.ksee.dto.SendPostResultDTO;
import com.junhsue.ksee.dto.PostCommentDTO;
import com.junhsue.ksee.entity.PostCommentEntity;
import com.junhsue.ksee.entity.PostDetailEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * 新的社圈
 * Created by Sugar on 17/10/20.
 */

public class OKHttpNewSocialCircle extends BaseClientApi implements INewSocialCircle {

    private static OKHttpNewSocialCircle mInstance;

    public static OKHttpNewSocialCircle getInstance() {
        if (null == mInstance) {
            synchronized (OKHttpNewSocialCircle.class) {
                if (null == mInstance)
                    mInstance = new OKHttpNewSocialCircle();
            }
        }
        return mInstance;
    }

    @Override
    public <T> void getPostDetail(String id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id + "");
        String url = RequestUrl.CIRCLE_BAR_DETAIL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostDetailEntity> requestCall = new RequestCall<PostDetailEntity>(postFormRequest, (RequestCallback<PostDetailEntity>) requestCallback, PostDetailEntity.class);
        submitRequset(requestCall);
    }

    /**
     * 圈子列表
     *
     * @param layer 圈子层级 0：一级圈子 1：二级圈子
     * @param <T>
     */
    @Override
    public <T> void getCircleList(String layer, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("layer", layer);
        String url = RequestUrl.CIRCLE_CIRCLE_LAYERLIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CircleCircleListDTO> requestCall = new RequestCall<CircleCircleListDTO>(postFormRequest, (RequestCallback<CircleCircleListDTO>) requestCallback, CircleCircleListDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 圈子详情——帖子列表
     *
     * @param circle_id
     * @param page
     * @param pagesize
     * @param top
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getCircleBarList(String circle_id, String page, String pagesize, String top, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        if (null != circle_id) {
            params.put("circle_id", circle_id);
        }
        params.put("page", page);
        params.put("pagesize", pagesize);
        if (null != top) {
            params.put("top", top);
        }
        String url = RequestUrl.CIRCLE_BAR_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostDetailListDTO> requestCall = new RequestCall<PostDetailListDTO>(postFormRequest, (RequestCallback<PostDetailListDTO>) requestCallback, PostDetailListDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getCircleBarList(String page, String pagesize, String top, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page);
        params.put("pagesize", pagesize);
        if (null != top) {
            params.put("top", top);
        }
        String url = RequestUrl.CIRCLE_BAR_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostDetailListDTO> requestCall = new RequestCall<PostDetailListDTO>(postFormRequest, (RequestCallback<PostDetailListDTO>) requestCallback, PostDetailListDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 发布帖子
     *
     * @param title           标题
     * @param description     描述
     * @param is_anonymous    是否匿名 0：不匿名 1：匿名
     * @param circle_id       发布到的圈子
     * @param poster          发布的图片
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void sendPost(String title, String description, String is_anonymous, String circle_id, String poster, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("title", title);
        params.put("description", description);
        params.put("is_anonymous", is_anonymous);
        params.put("circle_id", circle_id);
        params.put("poster", poster);
        String url = RequestUrl.CIRCLE_BAR_CIRCLE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<SendPostResultDTO> requestCall = new RequestCall<SendPostResultDTO>(postFormRequest, (RequestCallback<SendPostResultDTO>) requestCallback, SendPostResultDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 删除帖子
     *
     * @param id              帖子id
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void deletePost(String id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        String url = RequestUrl.CIRCLE_BAR_DELETE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<SendPostResultDTO> requestCall = new RequestCall<SendPostResultDTO>(postFormRequest, (RequestCallback<SendPostResultDTO>) requestCallback, SendPostResultDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 发布的帖子列表
     *
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getPostList(String page, String pagesize, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page);
        params.put("pagesize", pagesize);
        String url = RequestUrl.CIRCLE_BAR_MYLIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostDetailListDTO> requestCall = new RequestCall<PostDetailListDTO>(postFormRequest, (RequestCallback<PostDetailListDTO>) requestCallback, PostDetailListDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 取消帖子收藏
     *
     * @param business_id     业务id
     * @param id              帖子id
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void unfavoritePostCollect(String business_id, String id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("business_id", business_id);
        String url = RequestUrl.CIRCLE_BAR_UNFAVORITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<SendPostResultDTO> requestCall = new RequestCall<SendPostResultDTO>(postFormRequest, (RequestCallback<SendPostResultDTO>) requestCallback, SendPostResultDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 收藏的帖子列表
     *
     * @param business_id     收藏业务id
     * @param page
     * @param pagesize
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getPostCollect(String business_id, String page, String pagesize, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("business_id", business_id);
        params.put("page", page);
        params.put("pagesize", pagesize);
        String url = RequestUrl.CIRCLE_BAR_MYFAVORITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostDetailListDTO> requestCall = new RequestCall<PostDetailListDTO>(postFormRequest, (RequestCallback<PostDetailListDTO>) requestCallback, PostDetailListDTO.class);
        submitRequset(requestCall);
    }

    /**
     * @param page
     * @param pagesize
     * @param id
     * @param requestCallback
     * @param <T>
     */
    @Override
    public <T> void getPostCommentList(int page, int pagesize, String id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        params.put("id", id + "");
        String url = RequestUrl.CIRCLE_BAR_COMMENT_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostCommentDTO> requestCall = new RequestCall<PostCommentDTO>(postFormRequest, (RequestCallback<PostCommentDTO>) requestCallback, PostCommentDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void collectPost(String id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id + "");
        params.put("business_id", business_id + "");
        String url = RequestUrl.CIRCLE_BAR_FAVORITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void cancelCollectPost(String id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id + "");
        params.put("business_id", business_id + "");
        String url = RequestUrl.CIRCLE_BAR_UNFAVORITE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void postCommentApproval(String content_id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id + "");
        params.put("business_id", business_id + "");
        String url = RequestUrl.CIRCLE_BAR_APPROVAL_CREATE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void postCommentCancelApproval(String content_id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id + "");
        params.put("business_id", business_id + "");
        String url = RequestUrl.CIRCLE_BAR_APPROVAL_DELETE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void postCommentReplyCreate(String content_id, String business_id, String content, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id + "");
        params.put("business_id", business_id + "");
        params.put("content", content + "");
        String url = RequestUrl.CIRCLE_BAR_POST_COMMENT_REPLY_CREATE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostSecondDTO> requestCall = new RequestCall<PostSecondDTO>(postFormRequest, (RequestCallback<PostSecondDTO>) requestCallback, PostSecondDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void postReportList(RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        String url = RequestUrl.CIRCLE_BAR_REPORT_LIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ReportSelectDTO> requestCall = new RequestCall<ReportSelectDTO>(postFormRequest, (RequestCallback<ReportSelectDTO>) requestCallback, ReportSelectDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void senderReportToServer(String report_type_id, String content_id, String business_id, String content, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("report_type_id", report_type_id);
        params.put("content_id", content_id + "");
        params.put("business_id", business_id + "");
        params.put("content", content + "");
        String url = RequestUrl.CIRCLE_BAR_REPORT_SEND;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadCommentDetail(String id, int page, int pagesize, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        String url = RequestUrl.CIRCLE_BAR_COMMENT_DETAIL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostCommentEntity> requestCall = new RequestCall<PostCommentEntity>(postFormRequest, (RequestCallback<PostCommentEntity>) requestCallback, PostCommentEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void deleteComment(String id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        String url = RequestUrl.CIRCLE_BAR_COMMENT_DELETE;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadRecommendlist(String id, int page, int pagesize, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("id", id);
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        String url = RequestUrl.CIRCLE_BAR_RECOMMENDLIST;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<PostDetailListDTO> requestCall = new RequestCall<PostDetailListDTO>(postFormRequest, (RequestCallback<PostDetailListDTO>) requestCallback, PostDetailListDTO.class);
        submitRequset(requestCall);
    }

}
