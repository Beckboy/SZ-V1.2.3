package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.RequestCall;

/**
 * Created by longer on 17/3/27.
 */

public interface ICourse {

    public interface ICourseType {

        //系统课
        public static int COURSE_TYPE_SYSTEM = 8;
        //主题课
        public static int COURSE_TYPE_SUJECT = 9;

    }

    /**
     * 获取课程列表
     *
     * @param business_id 业务ID
     * @param page_size
     * @param page_num
     * @param callback
     * @param <T>
     */
    public <T> void getCourseList(String business_id, String page_size, String page_num, RequestCallback<T> callback);


    /**
     * 获取系统课
     *
     * @param business_id
     * @param page_size
     * @param page_num
     * @param callback
     * @param <T>
     */
    public <T> void getCourseSystem(String business_id, String page_size, String page_num, RequestCallback<T> callback);


    /**
     * 检查用户信息是否完整
     *
     * @param token
     * @param callback
     * @param <T>
     */
    public <T> void checkUserInfo(String token, RequestCallback<T> callback);


    /**
     * 创建订单
     *
     * @param token
     * @param business_id 业务id
     * @param amount      单价
     * @param good_id     商品id
     * @param name        标题
     * @param count       数量
     * @param callback
     * @param <T>
     */
    public <T> void createOrder(String token, String poster, String business_id, String amount,
                                String good_id, String name, String count,
                                String is_receipt, String receipt_type_id,
                                String receipt_content_id, String organization,
                                String contact, String contact_phone,
                                String contact_address, String uniform_code,
                                String register_address, String register_phone,
                                String bank, String bank_account, RequestCallback<T> callback);


    /**
     * 获取订单详情
     *
     * @param order_id 订单id
     * @param callback
     * @param <T>
     */
    public <T> void getOrderDetails(String order_id, RequestCallback<T> callback);


    /**
     * 主题课报名
     *
     * @param course_id      课程id
     * @param count
     * @param syscoursecount
     * @param callback
     * @param <T>
     */
    public <T> void courseApply(String course_id, String count, String syscoursecount, RequestCallback<T> callback);


    /**
     * 获取直播列表
     */
    public <T> void getLiveList(RequestCallback<T> callback);


    /**
     * 获取直播详情
     */
    public <T> void getLiveDetails(String live_id, RequestCallback<T> callback);

    /**
     * 活动列表
     *
     * @param page
     * @param pagesize
     * @param callback
     * @param <T>
     */
    public <T> void getActivities(int page, int pagesize, RequestCallback<T> callback);


    /**
     * 评论公共接口
     *
     * @param content_id
     * @param business_id
     * @param content
     * @param callback
     * @param <T>
     */
    public <T> void createComment(String content_id, String business_id, String content, RequestCallback<T> callback);


    /**
     * @param business_id 业务id
     * @param page_num
     * @param page_size   页面大小
     * @param content_id  被评论的内容id
     * @param callback
     * @param <T>
     */
    public <T> void getCommentList(String page_num, String page_size, String business_id,
                                   String content_id, RequestCallback<T> callback);


    /**
     * 获取消息卡片
     */

    public <T> void getMsgList(String page, String page_size, RequestCallback<T> callback);


    /**
     * 获取活动详情
     */
    public <T> void getActivitiesDetails(String activity_id, RequestCallback<T> callback);


    /**
     * 获取主题课详情
     *
     * @param course_id 课程id
     * @param callback
     * @param <T>
     */
    public <T> void getCourseSubjectDetails(String course_id, RequestCallback<T> callback);

    /**
     * 获取系统课详情
     */
    public <T> void getCourseSystemDetails(String course_id, RequestCallback<T> callback);


    /**
     * 卡片消息隐藏
     *
     * @param id       卡片id
     * @param status   2隐藏
     * @param type     2 系统卡片 ,0 是普通卡片
     * @param callback
     * @param <T>
     */
    public <T> void ignoreMsg(String id, String status, String type, RequestCallback<T> callback);


    /**
     * 订单支付
     *
     * @param order_id    订单id
     * @param pay_type_id 1 支付宝 2 微信支付,  3 applePay
     * @param callback
     * @param <T>
     */
    <T> void orderPay(String order_id, int pay_type_id, RequestCallback<T> callback);


    public <T> void getVideoList(int page, int pagesize, RequestCallback<T> callback);

    public <T> void getLiveList(int page, int pagesize, RequestCallback<T> callback);


    /**
     * 获取录播详情
     */
    public <T> void getVideoDetails(String video_id, RequestCallback<T> callback);

    /**
     * 直播点赞
     * @param live_id  直播id
     * @param count 点赞数
     */
    public <T> void liveApproval(String live_id,String count,RequestCallback<T> callback);
}
