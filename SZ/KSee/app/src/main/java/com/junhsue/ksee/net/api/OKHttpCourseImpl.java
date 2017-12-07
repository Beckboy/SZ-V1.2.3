package com.junhsue.ksee.net.api;

import com.junhsue.ksee.dto.ActivityCommentDTO;
import com.junhsue.ksee.dto.ActivityDTO;
import com.junhsue.ksee.dto.CourseDTO;
import com.junhsue.ksee.dto.CourseSystemDTO;
import com.junhsue.ksee.dto.LiveDTO;
import com.junhsue.ksee.dto.MessageDTO;
import com.junhsue.ksee.dto.OrderPayDTO;
import com.junhsue.ksee.dto.VideoDTO;
import com.junhsue.ksee.entity.ActivityEntity;
import com.junhsue.ksee.entity.CommonResultEntity;
import com.junhsue.ksee.entity.Course;
import com.junhsue.ksee.entity.CourseSystem;
import com.junhsue.ksee.entity.CourseSystemApplyEntity;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.OrderDetailsEntity;
import com.junhsue.ksee.entity.OrderEntity;
import com.junhsue.ksee.entity.ResultEntity;
import com.junhsue.ksee.entity.VideoEntity;
import com.junhsue.ksee.entity.WechatPayEntity;
import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.PostFormRequest;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

import java.util.HashMap;

/**
 * Created by longer on 17/3/27.
 */
public class OKHttpCourseImpl extends BaseClientApi implements ICourse {

    private static OKHttpCourseImpl mInstance;

    public static OKHttpCourseImpl getInstance() {
        if (null == mInstance) {
            synchronized (OKHttpCourseImpl.class) {
                if (null == mInstance)
                    mInstance = new OKHttpCourseImpl();
            }
        }
        return mInstance;
    }


    @Override
    public <T> void getCourseList(String business_id, String page_size, String page_num, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("business_id", business_id);
        params.put("pagesize", page_size);
        params.put("pagenum", page_num);
        //
        String url = RequestUrl.COLLEAGE_COURSE_SUBJECT;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CourseDTO> requestCall = new RequestCall<CourseDTO>(postFormRequest,
                (RequestCallback<CourseDTO>) callback, CourseDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getCourseSystem(String business_id, String page_size, String page_num, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("business_id", business_id);
        params.put("pagesize", page_size);
        params.put("pagenum", page_num);
        //
        String url = RequestUrl.COLLEAGE_COURSE_SYSTEM;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CourseSystemDTO> requestCall = new RequestCall<CourseSystemDTO>(postFormRequest,
                (RequestCallback<CourseSystemDTO>) callback, CourseSystemDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void checkUserInfo(String token, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        //
        String url = RequestUrl.CHECK_USER_INFO;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CommonResultEntity> requestCall = new RequestCall<CommonResultEntity>(postFormRequest,
                (RequestCallback<CommonResultEntity>) callback, CommonResultEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void createOrder(String token, String poster, String business_id, String amount,
                                String good_id, String name, String count,
                                String is_receipt, String receipt_type_id,
                                String receipt_content_id, String organization,
                                String contact, String contact_phone,
                                String contact_address, String uniform_code,
                                String register_address, String register_phone,
                                String bank, String bank_account, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("token", token);
        if (null != poster) {
            params.put("poster", poster);
        }
        params.put("business_id", business_id);
        params.put("amount", amount);
        params.put("good_id", good_id);
        params.put("name", name);
        params.put("count", count);
        params.put("is_receipt", is_receipt);
        if (!"0".equals(is_receipt)) {
            params.put("receipt_type_id", receipt_type_id);
            params.put("organization", organization);
            params.put("contact", contact);
            params.put("contact_phone", contact_phone);
            params.put("contact_address", contact_address);
            if (!"1".equals(receipt_type_id)) {
                params.put("receipt_content_id", receipt_content_id);
                params.put("uniform_code", uniform_code);
            }
            if ("3".equals(receipt_type_id)) {
                params.put("register_address", register_address);
                params.put("register_phone", register_phone);
                params.put("bank", bank);
                params.put("bank_account", bank_account);
            }
        }
        //
        String url = RequestUrl.ORDER_CREATE;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<OrderEntity> requestCall = new RequestCall<OrderEntity>(postFormRequest,
                (RequestCallback<OrderEntity>) callback, OrderEntity.class);
        submitRequset(requestCall);


    }

    @Override
    public <T> void getOrderDetails(String order_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("order_id", order_id);
        //
        String url = RequestUrl.ORDER_DETAILS;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<OrderDetailsEntity> requestCall = new RequestCall<OrderDetailsEntity>(
                postFormRequest, (RequestCallback<OrderDetailsEntity>) callback, OrderDetailsEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void courseApply(String course_id, String count, String syscoursecount, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("course_id", course_id);
        params.put("count", count);
        params.put("syscoursecount", syscoursecount);
        //
        String url = RequestUrl.COURSE_APPLY;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CourseSystemApplyEntity> requestCall = new RequestCall<CourseSystemApplyEntity>(postFormRequest,
                (RequestCallback<CourseSystemApplyEntity>) callback, CourseSystemApplyEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getLiveList(RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<String, String>();
        //url
        String url = RequestUrl.LIVE_LIST;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<LiveDTO> requestCall = new RequestCall<LiveDTO>(postFormRequest,
                (RequestCallback<LiveDTO>) callback, LiveDTO.class);
        submitRequset(requestCall);

    }

    @Override
    public <T> void getLiveDetails(String live_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("live_id", live_id);
        //
        String url = RequestUrl.LIVE_DETAILS;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<LiveEntity> requestCall = new RequestCall<>(postFormRequest,
                (RequestCallback<LiveEntity>) callback, LiveEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getActivities(int page, int pagesize, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        //
        String url = RequestUrl.ACTIVITIES_LIST_URL;

        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ActivityDTO> requestCall = new RequestCall<ActivityDTO>(postFormRequest,
                (RequestCallback<ActivityDTO>) callback, ActivityDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void createComment(String content_id, String business_id, String content, RequestCallback<T> callback) {

        HashMap<String, String> params = new HashMap<>();
        params.put("content_id", content_id);
        params.put("business_id", business_id);
        params.put("content", content);
        //
        String url = RequestUrl.COMMON_COMMENT_URL;

        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<ResultEntity>(postFormRequest,
                (RequestCallback<ResultEntity>) callback, ResultEntity.class);
        submitRequset(requestCall);
    }


    public <T> void getCommentList(String page_num, String page_size, String business_id, String content_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page_num);
        params.put("pagesize", page_size);
        params.put("business_id", business_id);
        params.put("content_id", content_id);
        //
        String url = RequestUrl.ACTIVITY_COMMENT_LIST;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ActivityCommentDTO> requestCall = new RequestCall<ActivityCommentDTO>(postFormRequest,
                (RequestCallback<ActivityCommentDTO>) callback, ActivityCommentDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getMsgList(String page, String page_size, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page);
        params.put("pagesize", page_size);
        //
        String url = RequestUrl.MSG_LIST;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<MessageDTO> requestCall = new RequestCall<MessageDTO>(postFormRequest, (RequestCallback<MessageDTO>) callback,
                MessageDTO.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void getActivitiesDetails(String activity_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("activity_id", activity_id);
        //
        String url = RequestUrl.ACTIVITY_DETAILS;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ActivityEntity> requestCall = new RequestCall<ActivityEntity>(postFormRequest,
                (RequestCallback<ActivityEntity>) callback,
                ActivityEntity.class);
        //
        submitRequset(requestCall);
    }

    @Override
    public <T> void getCourseSubjectDetails(String course_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("business_id", String.valueOf(ICourseType.COURSE_TYPE_SUJECT));
        params.put("course_id", course_id);
        //
        String url = RequestUrl.COLLEAGE_COURSE_DETAILS;
        //
        PostFormRequest postFormReques = new PostFormRequest(url, params);
        RequestCall<Course> requestCall = new RequestCall<Course>(postFormReques, (RequestCallback<Course>) callback, Course.class);
        //
        submitRequset(requestCall);
    }

    @Override
    public <T> void getCourseSystemDetails(String course_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("business_id", String.valueOf(ICourseType.COURSE_TYPE_SYSTEM));
        params.put("course_id", course_id);
        //
        String url = RequestUrl.COLLEAGE_COURSE_DETAILS;
        //
        PostFormRequest postFormReques = new PostFormRequest(url, params);
        RequestCall<CourseSystem> requestCall = new RequestCall<CourseSystem>(postFormReques, (RequestCallback<CourseSystem>) callback,
                CourseSystem.class);
        //
        submitRequset(requestCall);
    }

    @Override
    public <T> void ignoreMsg(String id, String status, String type, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("message_id", id);
        params.put("status", status);
        params.put("type", type);
        //
        String url = RequestUrl.MSG_IGNORE;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<ResultEntity> requestCall = new RequestCall<>(postFormRequest,
                (RequestCallback<ResultEntity>) callback, ResultEntity.class);
        //
        submitRequset(requestCall);
    }

    @Override
    public <T> void orderPay(String order_id, int pay_type_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("order_id", order_id);
        params.put("pay_type_id", String.valueOf(pay_type_id));
        //
        String url = RequestUrl.ORDER_PAY;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<OrderPayDTO> requestCall = new RequestCall<>(postFormRequest, (RequestCallback<OrderPayDTO>) callback, OrderPayDTO.class);
        //
        submitRequset(requestCall);
    }

    @Override
    public <T> void getVideoList(int page, int pagesize, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        //
        String url = RequestUrl.VIDEO_LIST_URL;
        //
        PostFormRequest postFormReques = new PostFormRequest(url, params);
        RequestCall<VideoDTO> requestCall = new RequestCall<VideoDTO>(postFormReques, (RequestCallback<VideoDTO>) callback, VideoDTO.class);
        //
        submitRequset(requestCall);
    }

    @Override
    public <T> void getLiveList(int page, int pagesize, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("page", page + "");
        params.put("pagesize", pagesize + "");
        String url = RequestUrl.LIVE_LIST;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<LiveDTO> requestCall = new RequestCall<LiveDTO>(postFormRequest,
                (RequestCallback<LiveDTO>) callback, LiveDTO.class);
        submitRequset(requestCall);
    }

    /**
     * 获取录播详情
     * @param video_id
     * @param callback
     * @param <T>
     */
    @Override
    public <T> void getVideoDetails(String video_id, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("id", video_id);
        //
        String url = RequestUrl.VIDEO_DETAIL_URL;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<VideoEntity> requestCall = new RequestCall<>(postFormRequest,
                (RequestCallback<VideoEntity>) callback, VideoEntity.class);
        submitRequset(requestCall);
    }

    @Override
    public <T> void liveApproval(String live_id, String count, RequestCallback<T> callback) {
        HashMap<String, String> params = new HashMap<>();
        params.put("live_id",live_id);
        params.put("count",count);
        //
        String url=RequestUrl.LIVE_APPROVAL;
        //
        PostFormRequest postFormRequest = new PostFormRequest(url, params);
        RequestCall<CommonResultEntity> requestCall = new RequestCall<>(postFormRequest,
                (RequestCallback<CommonResultEntity>) callback, CommonResultEntity.class);
        submitRequset(requestCall);

    }

}
