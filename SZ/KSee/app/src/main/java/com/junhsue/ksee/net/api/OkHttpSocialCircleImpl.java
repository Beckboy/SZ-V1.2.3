package com.junhsue.ksee.net.api;

import com.junhsue.ksee.common.IBusinessType;
import com.junhsue.ksee.dto.ProfessorDTO;
import com.junhsue.ksee.dto.QuestionDetailAnswerListDTO;
import com.junhsue.ksee.dto.SelectedQuestionDTO;
import com.junhsue.ksee.entity.QuestionEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;
import java.util.Map;

/**
 * 社板块请求sender
 * Created by Sugar on 17/3/22.
 */

public class OkHttpSocialCircleImpl extends BaseClientApi implements ISocialCircle {

    private static OkHttpSocialCircleImpl mInstance;

    public static OkHttpSocialCircleImpl getInstance() {

        if (null == mInstance) {
            synchronized (OkHttpSocialCircleImpl.class) {
                if (null == mInstance) {
                    mInstance = new OkHttpSocialCircleImpl();
                }
            }
        }
        return mInstance;

    }

    @Override
    public <T> void loadQuestionList(int pagesize, int page, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("pagesize", pagesize + "");
        params.put("page", page + "");
        String url = RequestUrl.QUESTION_LIST_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<SelectedQuestionDTO> requestCall = new RequestCall<SelectedQuestionDTO>(postFormRequest, (RequestCallback<SelectedQuestionDTO>) requestCallback, SelectedQuestionDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadSearchResultQuestionList(String keyResult, int pagesize, int page, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        params.put("searchkey", keyResult);
        String url = RequestUrl.SEARCH_SOCIALCIRCLE_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<SelectedQuestionDTO> requestCall = new RequestCall<SelectedQuestionDTO>(postFormRequest, (RequestCallback<SelectedQuestionDTO>) requestCallback, SelectedQuestionDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadQuestionNewest(String tag_id, int pagesize, int page, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        if (null != tag_id){
            params.put("tag_id", tag_id);
        }
        params.put("pagesize", pagesize + "");
        params.put("page", page + "");
        String url = RequestUrl.QUESTION_NEWEST_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<SelectedQuestionDTO> requestCall = new RequestCall<SelectedQuestionDTO>(postFormRequest, (RequestCallback<SelectedQuestionDTO>) requestCallback, SelectedQuestionDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadQuestionDetail(String questionId, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("question_id", questionId);
        String url = RequestUrl.QUESTION_DETAIL_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<QuestionEntity> requestCall = new RequestCall<QuestionEntity>(postFormRequest, (RequestCallback<QuestionEntity>) requestCallback, QuestionEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadQuestionDetailAnswerList(String questionId, int pagesize, int page, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("question_id", questionId);
        params.put("pagesize", pagesize + "");
        params.put("page", page + "");
        String url = RequestUrl.QUESTION_DETAIL_ANSWER_LIST_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<QuestionDetailAnswerListDTO> requestCall = new RequestCall<QuestionDetailAnswerListDTO>(postFormRequest, (RequestCallback<QuestionDetailAnswerListDTO>) requestCallback, QuestionDetailAnswerListDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void loadAnswerReplay(String questionId, String content, int type, String duration, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("question_id", questionId);
        params.put("content", content);
        params.put("type", type + "");
        params.put("duration", duration);
        String url = RequestUrl.QUESTION_REPLY;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void askForProfessors(int count, String tag_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("count", count + "");
        params.put("tag_id", tag_id);
        String url = RequestUrl.ACCOUNT_RANDOMUSER;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ProfessorDTO> requestCall = new RequestCall<ProfessorDTO>(postFormRequest, (RequestCallback<ProfessorDTO>) requestCallback, ProfessorDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void senderQuestion(String content, String description, String tags, String ids, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content", content);
        params.put("description", description);
        params.put("tags", tags);
        params.put("ids", ids);
        String url = RequestUrl.QUESTION_ASK_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void senderFavorite(String content_id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id);
        params.put("business_id", business_id);
        String url = RequestUrl.COMMON_FAVORITE_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void senderDeleteFavorite(String content_id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id);
        params.put("business_id", business_id);
        String url = RequestUrl.DELETE_FAVORITE_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void senderApproval(String content_id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id);
        params.put("business_id", business_id);
        String url = RequestUrl.COMMON_APPROVAL_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void senderDeleteApproval(String content_id, String business_id, RequestCallback<T> requestCallback) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("content_id", content_id);
        params.put("business_id", business_id);
        String url = RequestUrl.DELETE_APPROVAL_URL;
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest, (RequestCallback<ResultEntity>) requestCallback, ResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void downloadVoice(String voice_url, RequestCallback<T> requestCallback) {
        HashMap<String, String> params = new HashMap<String, String>();
        //url
  /*      String url = voice_url;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<> requestCall = new RequestCall<LiveDTO>(postFormRequest,
                (RequestCallback<>) callback, LiveDTO.class);
        submitRequset(requestCall);*/

    }




}
