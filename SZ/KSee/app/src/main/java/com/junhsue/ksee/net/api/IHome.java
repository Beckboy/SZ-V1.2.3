package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.RequestCall;

/**
 * 首页
 * Created by longer on 17/8/11.
 */

public interface IHome {


    /**
     * 获取首页内容区域*/
    public <T> void getHomeContent(RequestCallback<T> requestCallback);

    /**
     * 获取轮播图
     */
    public <T> void getHomeBanner(String version, RequestCallback<T> requestCallback);


    /**
     * 获取办学标签列表
     */
    public <T> void getHomeManagerTags(RequestCallback<T> requestCallback);

    /**
     * 获取文章列表
     * @param page
     * @param pagesize
     * @param tag_id
     * @param requestCallback
     * @param <T>
     */
    public <T> void getArticleList(String page, String pagesize, String tag_id, RequestCallback<T> requestCallback);


    /**
     * 获取方案包详情
     *
     */
    public <T> void getSolutiondDetails(String id,RequestCallback<T> requestCallback);


    /**
     *方案包兑换
     * @param coupon_id
     * @param solution_id
     * @param email
     * @param link
     * @param title
     * @param nickname
     * @param requestCallback
     * @param <T>
     */
    public <T> void solutionConvert(String coupon_id,String solution_id,String email,String link,String title,
                                   String nickname,RequestCallback<T> requestCallback);

    /**
     * 获取优惠券列表
     * @param requestCallback
     * @param <T>
     */
    public <T> void getSolutionCouponList(RequestCallback<T> requestCallback);


    /**
     * 发送方案包
     * @param  link 方案啊包链接
     * @param  email 邮箱
     * @param  title 标题 方案包标题
     * @param  nickname 用户名
     */
    public <T> void sendSolutionEmail(String link, String email, String title, String nickname
            , RequestCallback<T> requestCallback);


    /**
     * 获取方案包列表
     * @param requestCallback
     * @param <T>
     */
    public <T> void getSolutionList(RequestCallback<T> requestCallback);


    /**
     * 获取快捷入口
     * @param size  大小
     */
    public <T> void getMenuTab(String size,RequestCallback<T> requestCallback);


    /**
     * 获取消息中心数量
     */
    public <T> void getMsgCount(RequestCallback<T> requestCallback);


    /**
     * 标记信息已读功能
     *
     * @param type_id
     * 16 点赞消息
     * 17 收藏消息
     * 18 回复消息
     * 19 系统消息
     *
     * @param  status
     * 0 未读1 已读2 删除
     */
    public <T> void updateMsgStatus(String type_id,String status,RequestCallback<T> requestCallback);


    /**
     * 获取热门贴
     *@param page
     *@param pagesize  分页数量
     *
     */
    public <T> void getHomePostsHot(String page,String pagesize,RequestCallback<T> requestCallback);
}
