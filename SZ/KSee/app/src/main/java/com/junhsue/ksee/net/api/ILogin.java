package com.junhsue.ksee.net.api;

import com.junhsue.ksee.net.callback.RequestCallback;
import com.junhsue.ksee.net.request.RequestCall;
import com.junhsue.ksee.net.url.RequestUrl;

/**
 * Created by hunter_J on 17/3/24.
 */

public interface ILogin {

    //微信登录获取AccessToken接口(1000)
    public <T> void getAccessTokenFormWeChat(String requestUrl, RequestCallback<T> callback);

    //获取微信用户请求
    public <T> void getWeChatUserInfo(String requestUrl, RequestCallback<T> callback);

    //手机号登录接口(1000)
    public <T> void loginByPhonenumber(String logintype, String unionid, String phonenumber, String password, RequestCallback<T> requestCallback);

    //验证码登录接口
    public <T> void loginByVerityCode(String phonenumber, String msg_id, String code, RequestCallback<T> requestCallback);

    //自动登录的token验证
    public <T> void autologinByToken(String token, RequestCallback<T> requestCallback);

    //微信登录与已注册的手机号绑定
    public <T> void wechatBindPhoneNumber(String unionid, String phonenumber, String avatar, String nickname, RequestCallback<T> requestCallback);

    //发送验证码接口(1001)
    public <T> void registerSendsms(String phonenumber, RequestCallback<T> requestCallback);

    //验证验证码接口(1002)
    public <T> void registerVerifysms(String msg_id, String code, RequestCallback<T> requestCallback);

    //检查手机号账号是否存在(1003)
    public <T> void loginSearchAccount(String phonenumber, RequestCallback<T> requestCallback);

    //七牛云存储token获取(1004)
    public <T> void GetQNToken(RequestCallback<T> requestCallback);

    //使用手机号注册账号(1006)
    public <T> void registerByPhonenumberOrWechat(String logintype, String unionid, String phonenumber, String password, String nickname, String organization, String avatar, String tags, RequestCallback<T> requestCallback);

    //修改密码
    public <T> void loginForgetPassword(String phonenumber, String password, RequestCallback<T> requestCallback);

    //领域列表
    public <T> void registerGetTags(RequestCallback<T> requestCallback);

    //修改昵称
    public <T> void settingAvatar(String token, String value, RequestCallback<T> requestCallback);

    //修改昵称
    public <T> void settingNickName(String token, String value, RequestCallback<T> requestCallback);

    //修改学校/机构
    public <T> void settingOrganization(String token, String value, RequestCallback<T> requestCallback);

    //修改联系地址
    public <T> void settingAddress(String token, String province, String city, String district, String address, RequestCallback<T> requestCallback);

    //获取职务列表
    public <T> void settingGetJobList(RequestCallback<T> requestCallback);

    //修改职务
    public <T> void settingJob(String token, String value, RequestCallback<T> requestCallback);

    //修改性别
    public <T> void settingSex(String token, String value, RequestCallback<T> requestCallback);

    //修改出生年月
    public <T> void settingBirthday(String token, String value, RequestCallback<T> requestCallback);

    //我的问答 —— 被邀请的
    public <T> void myanswerInvite(String token, String page, String pagesize, RequestCallback<T> requestCallback);

    //我的问答 —— 被邀请的
    public <T> void myanswerMyask(String token, String page, String pagesize, RequestCallback<T> requestCallback);

    //我的问答 —— 我的收藏
    public <T> void myanswerMycollect(String token, String business_id, String page, String pagesize, RequestCallback<T> requestCallback);

    //吾 —— 意见反馈
    public <T> void myFeedback(String token, String content, String platform_id, RequestCallback<T> requestCallback);

    //吾 —— 我的订单列表
    public <T> void myOrderList(String token, String pagenum, String pagesize, RequestCallback<T> requestCallback);

    //熟 —— 教室列表
    public <T> void colleageClassRoomList(String token, RequestCallback<T> requestCallback);

    //熟 -- 教室人员详情
    public <T> void colleageClassRoomDetails(String roomid, RequestCallback<T> requestCallback);

    //识 -- 单向历
    public <T> void knowCalendarQuery(String token, String version, RequestCallback<T> requestCallback);

    //识 -- 单向历点赞
    public <T> void knowCalendarPrise(String token, String calendar_id, String type, RequestCallback<T> requestCallback);

    //我的问答 —— 被邀请的
    public <T> void myanswerCardList(String token, String page, String pagesize, RequestCallback<T> requestCallback);

    //我的问答 —— 被邀请的
    public <T> void getRealizeArticleDetail(String id, RequestCallback<T> requestCallback);

    //知：文章详情页点赞
    public <T> void realizeArticleDetailPrise(String business_id, RequestCallback<T> requestCallback);

    //吾:邀请人员
    public <T> void inviteList(String source_user_id, RequestCallback<T> requestCallback);

    //吾:我的兑换券
    public <T> void myCoin(RequestCallback<T> requestCallback);

    //验证版本更新接口
    public <T> void verityVersionUpdate(RequestCallback<T> requestCallback);

    //消息中心——收到的回复
    public <T> void msgCenterReceiveReply(String id, String page, String pagesize, RequestCallback<T> requestCallback);

    //消息中心——修改某条信息状态置为已读
    public <T> void msgCenterReceiveReplyIsRead(String message_id, String status, RequestCallback<T> requestCallback);

    /**
     * 消息中心——修改所有信息状态置为已读
     * @param type_id 16:点赞消息；17:收藏消息；18:回复消息；19:系统消息；
     * @param status 指定修改状态：0:未读；1:已读；2:删除；
     * @param requestCallback
     * @param <T>
     */
    public <T> void msgCenterReceiveReplyAllRead(String type_id, String status, RequestCallback<T> requestCallback);

}
