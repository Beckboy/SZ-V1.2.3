package com.junhsue.ksee.net.url;

/**
 * 网络Ul参数类
 * Created by longer on 17/3/20.
 */

public interface RequestUrl {

    String TAG = "";
    /**
     * 主地址
     * <p>
     * 线上环境 http://116.62.65.102:9000 ,http://api.10knowing.com/
     * <p>
     * 测试环境 :http://test1.api.10knowing.com
     * <p>
     * 统计地址
     * http://116.62.65.102:8000/statistics/statistics/upload
     */

    //测试地址
    public static final String BASE_API = "http://test1.api.10knowing.com";


    // 生产地址
    public static final String BASH_API_102 = "http://test1.api.10knowing.com";

    public static final String STATICS_HOST_URL = "http://116.62.65.102:8000";

    /**
     * 七牛云存储主地址
     */
    public static final String BASE_QINIU_API = "http://omxx7cyms.bkt.clouddn.com/";

    /**
     * 回答列表地址
     */
    public static final String QUESTION_LIST_URL = BASE_API + "/question/question/list";

    /**
     * 搜索结果回答列表地址 - 问答
     */
    public static final String SEARCH_SOCIALCIRCLE_URL = BASE_API + "/question/question/search";

    /**
     * 搜索结果回答列表地址 - 干货
     */
    public static final String SEARCH_REALIZE_URL = BASE_API + "/article/article/search";


    /**
     * 最新列表集合地址
     */
    public static final String QUESTION_NEWEST_URL = BASE_API + "/question/question/newest";

    //主题课
    public static final String COLLEAGE_COURSE_SYSTEM = BASE_API + "/course/course/list";
    public static final String COLLEAGE_COURSE_SUBJECT = BASE_API + "/course/course/off_list";

    //======= 登录注册模块 =======
    /**
     * 微信请求主地址
     */
    public static final String WEIXIN_HOST = "https://api.weixin.qq.com/sns";

    /**
     * 获取微信的access_token地址
     */
    public static final String URL_GET_ACCESS_TOKEN = WEIXIN_HOST + "/oauth2/access_token";

    /**
     * 获取用户信息地址
     */
    public static final String URL_GET_USER_INFO = WEIXIN_HOST + "/userinfo";

    /**
     * 登录 -- 手机号登录（1009）
     */
    public static final String LOGIN_BY_PHONENUMBER = BASE_API + "/account/baseuser/login";

    /**
     * 登录 -- 验证码登录（1009）
     */
    public static final String LOGIN_BY_VERITYCODE = BASE_API + "/account/baseuser/login";


    /**
     * 自动登录 -- token（1000）
     */
    public static final String LOGIN_AUTOLOGIN = BASE_API + "/account/baseuser/autologin";

    /**
     * 微信登录 —— 绑定手机号
     */
    public static final String REGISTER_WXLOGIN_BIND_PHONENUMBER = BASE_API + "/account/baseuser/wechatbindphonenumber";

    /**
     * 注册 -- 发送短信验证码（1001）
     */
    public static final String REGISTER_GET_VERIFYCODE = BASE_API + "/common/sms/sendsms";

    /**
     * 注册 -- 验证短信验证码（1002）
     */
    public static final String REGISTER_VERIFY_VERIFYCODE = BASE_API + "/common/sms/verifysms";

    /**
     * 注册 -- 验证手机号账号是否存在（1003）
     */
    public static final String REGISTER_VERIFY_PHONENUMBER_CHECKACCOUNT = BASE_API + "/account/baseuser/checkaccount";

    /**
     * 注册 -- 获取头像上传的Token（1004）
     */
    public static final String REGISTER_HEADIMG_GETTOKEN = BASE_API + "/common/upload/gettoken";

    /**
     * 注册 -- 使用手机号注册（1006）
     */
    public static final String REGISTER_BY_PHONENUMBER = BASE_API + "/account/baseuser/register";

    /**
     * 登录 -- 修改账号密码（1008）
     */
    public static final String LOGIN_FORGETPASSWORD = BASE_API + "/account/baseuser/updatepassword";

    /**
     * 注册 -- 获取擅长领域列表（1005）
     */
    public static final String REGISTER_GET_TAGS = BASE_API + "/common/tag/list";

    /**
     * 问答详情页地址
     */
    public static final String QUESTION_DETAIL_URL = BASE_API + "/question/question/detail";

    /**
     * 问答详情页的回答列表的请求地址
     */
    public static final String QUESTION_DETAIL_ANSWER_LIST_URL = BASE_API + "/question/question/anlist";


    /**
     * 意见反馈
     */
    public final static String MY_FEEDBACK = BASE_API + "/common/feedback/create";

    /**
     * 设置 -- 更新头像（1021）
     */
    public static final String SETTING_AVATAR = BASE_API + "/account/baseuser/updateavatar";

    /**
     * 设置 -- 更新昵称（1022）
     */
    public static final String SETTING_NICKNAME = BASE_API + "/account/baseuser/updatenickname";

    /**
     * 设置 -- 更新组织结构（1023）
     */
    public static final String SETTING_ORGANIZATION = BASE_API + "/account/baseuser/updateorganization";

    /**
     * 设置 -- 更新地址（1024）
     */
    public static final String SETTING_ADDRESS = BASE_API + "/account/baseuser/updateaddress";

    /**
     * 设置 -- 获取职位列表（）
     */
    public static final String SETTING_JOB_LIST = BASE_API + "/common/position/list";

    /**
     * 设置 -- 更新职位（1025）
     */
    public static final String SETTING_JOB = BASE_API + "/account/baseuser/updateposition";

    /**
     * 设置 -- 更新性别（1026）
     */
    public static final String SETTING_SEX = BASE_API + "/account/baseuser/updategender";


    /**
     * 设置 -- 更新出生年月（1027）
     */
    public static final String SETTING_BIRTHDAY = BASE_API + "/account/baseuser/updatebirthday";

    /**
     * 我的问答 —— 被邀请的
     */
    public static final String MYANSWER_MYINVITE = BASE_API + "/question/question/myinvite";

    /**
     * 我的问答 —— 我提问的
     */
    public static final String MYANSWER_MYASK = BASE_API + "/question/question/myask";

    /**
     * 我的问答 —— 收藏
     */
    public static final String MYANSWER_MYCOLLECT = BASE_API + "/common/favorite/list";


    /**
     * 检查用户资料是否填写完整
     */
    public static final String CHECK_USER_INFO = BASE_API + "/account/baseuser/checkinfo";


    /**
     * 创建订单
     */
    public static final String ORDER_CREATE = BASE_API + "/pay/order/create";

    /**
     * 获取订单详情
     */
    public static final String ORDER_DETAILS = BASE_API + "/pay/order/detail";

    /**
     * 获取我的订单列表
     */
    public static final String ORDER_LIST = BASE_API + "/pay/order/list";

    /**
     * 系统课报名
     */

    public static final String COURSE_APPLY = BASE_API + "/course/course/create_sys_record";
    /**
     * 问题回答
     */
    public static final String QUESTION_REPLY = BASE_API + "/question/question/reply";
    /**
     * 邀请专家列表
     */
    public static final String ACCOUNT_RANDOMUSER = BASE_API + "/account/user/randomuser";

    /**
     * 邀请人员回答
     */
    public static final String QUESTION_ASK_URL = BASE_API + "/question/question/ask";

    /**
     * 收藏请求地址
     */
    public static final String COMMON_FAVORITE_URL = BASE_API + "/common/favorite/create";

    /**
     * 取消收藏地址
     */
    public static final String DELETE_FAVORITE_URL = BASE_API + "/common/favorite/delete";

    /**
     * 点赞
     */
    public static final String COMMON_APPROVAL_URL = BASE_API + "/common/approval/create";

    /**
     * 取消点赞
     */
    public static final String DELETE_APPROVAL_URL = BASE_API + "/common/approval/delete";

    /**
     * 获取直播列表
     */
    public static final String LIVE_LIST = BASE_API + "/live/live/lists";

    /**
     * 直播详情页
     */
    public static final String LIVE_DETAILS = BASE_API + "/live/live/detail";
    /**
     * 获取活动列表
     */
    public static final String ACTIVITIES_LIST_URL = BASE_API + "/activity/activity/list";


    /**
     * 熟 —— 教室列表
     */
    public static final String CLASSROOM_LIST = BASE_API + "/classroom/classroom/list";

    /**
     * 熟 —— 教室详情页
     */
    public static final String CLASSROOM_DETAILS = BASE_API + "/classroom/classroom/members";

    /**
     * 活动详情
     */
    public static final String ACTIVITY_DETAIL_URL = BASE_API + "/activity/activity/detail";

    /**
     * 评论公共接口
     */
    public static final String COMMON_COMMENT_URL = BASE_API + "/common/comment/create";


    /**
     * 活动评论列表
     */
    public static final String ACTIVITY_COMMENT_LIST = BASE_API + "/common/comment/list";

    /**
     * 活动详情
     */
    public static final String ACTIVITY_DETAILS = BASE_API + "/activity/activity/detail";

    /**
     * 创建活动评论
     */
    public static final String ACTIIVTY_COMMENT_CREATE = BASE_API + "/common/comment/create";

    /**
     * 识 —— 单向历
     */
    public static final String KNOW_CALENDAR_QUERY = BASE_API + "/home/calendar/query";

    /**
     * 识 —— 点赞
     */
    public static final String KNOW_CALENDAR_PRISE = BASE_API + "/home/calendar/prise";

    /**
     * 知 —— 文章详情页
     */
    public static final String REALIZE_ARTICLE_DETAIL = BASE_API + "/article/article/detail";


    /**
     * 拾消息卡片
     */
    public static final String MSG_LIST = BASE_API + "/message/message/list";


    /**
     * 获取课程详情
     */
    public static final String COLLEAGE_COURSE_DETAILS = BASE_API + "/course/course/detail";

    /**
     * 忽略消息
     */
    public static final String MSG_IGNORE = BASE_API + "/message/message/operate";

    /**
     * 订单支付
     */
    public static final String ORDER_PAY = BASE_API + "/pay/order/pay";

    /**
     * 我的问答卡片 —— 二级列表
     */
    public static final String MYANSWER_CARD_LIST = BASE_API + "/message/message/questionlist";

    /**
     * 知模块文章标签列表
     */
    public static final String REALIZE_ARTICLE_TAGS_LIST = BASE_API + "/common/tag/parentlist";

    /**
     * l
     * 知模块内容区域
     */
    public static final String REALIZE_HOME_CONTENT = BASE_API + "/home/home/list";

    /**
     * 知模块文章列表
     */
    public static final String REALIZE_ARTICLE_LIST = BASE_API + "/article/article/list";

    /**
     * 录播视频列表
     */
    public static final String VIDEO_LIST_URL = BASE_API + "/video/video/list";

    /**
     * 录播视频详情
     */
    public static final String VIDEO_DETAIL_URL = BASE_API + "/video/video/detail";

    /**
     * 自定义的统计接口
     */
    public static final String DEFINED_STATICS_UPLOAD_URL = BASE_API + "/statistics/statistics/upload";

    /**
     * 自定义的统计--首次使用统计接口
     */
    public static final String FIRST_OPEN_RECORDE_URL = BASE_API + "/statistics/statistics/active";

    /**
     * 统计启动次数
     */
    public static final String STATISTICS_START_URL = BASE_API + "/statistics/statistics/start";

    /**
     * 首页轮播
     */
    public static final String HOME_BANNER = BASE_API + "/home/home/banner";

    /**
     * 首页办学标签列表
     */
    public static final String HOME_MANAGER_TAGS = BASE_API + "/common/tagx/listofhome";

    /**
     * 首页办学标签三级文章列表
     */
    public static final String HOME_MANAGER_TAG_ARTICLE_LIST = BASE_API + "/article/article/listbytagxid";

    /**
     * 测试环境,测试完毕记得修改,http://test1.api.10knowing.com 统计单向历的分享次数
     */
    public static final String STATISTICS_CALENDAR_SHARE_COUNT_URL = BASE_API + "/home/calendar/sharecount";

    /**
     * 统计问答分享次数
     */
    public static final String STATISTICS_QUESTION_SHARE_COUNT_URL = BASE_API + "/question/question/sharecount";

    /**
     * 邀请用户-吾板块-兑换
     */
    public static final String INVITE_LIST_URL = BASE_API + "/account/invite/invitelist";

    /**
     * 吾板块-我的兑换卷
     */
    public static final String MY_COIN = BASE_API + "/common/coupon/list";

    /**
     * 方案包详情
     */
    public static final String SOLUTION_DETAILS = BASE_API + "/solution/solution/detail";

    /**
     * 方案包优惠券列表
     */
    public static final String SOLUTION_COUPON = BASE_API + "/common/coupon/list";

    /**
     * 方案包兑换
     */
    public static final String SOLUTION_CONVERT = BASE_API + "/solution/solution/exchange";


    /**
     * 邮箱发送
     */
    public static final String SOLUTION_EMAIL_SEND = BASE_API + "/solution/solution/sendemail";


    /**
     * 邀请好友兑换统计
     */
    public static final String STATISTICS_INVITE_SHARE = BASE_API + "/account/invite/share";


    /**
     * 版本更新
     */
    public static final String MY_VERSION_UPDATE = BASE_API + "/common/system/version";


    public static final String SOLUTION_LIST = BASE_API + "/solution/solution/grouplist";


    /**
     * 直播点赞
     */
    public static final String LIVE_APPROVAL = BASE_API + "/common/approval/h_create";


    /**
     * 圈子详情页
     */
    public static final String CIRCLE_BAR_DETAIL = BASE_API + "/circle/bar/detail";

    /**
     * 圈子数据源
     */
    public static final String CIRCLE_CIRCLE_LAYERLIST = BASE_API + "/circle/circle/layerlist";

    /**
     * 圈子列表
     */
    public static final String CIRCLE_CIRCLE_LIST = BASE_API + "/circle/circle/list";

    /**
     * 帖子列表
     */
    public static final String CIRCLE_BAR_LIST = BASE_API + "/circle/bar/list";

    /**
     * 发布帖子
     */
    public static final String CIRCLE_BAR_CIRCLE = BASE_API + "/circle/bar/create";

    /**
     * 删除帖子
     */
    public static final String CIRCLE_BAR_DELETE = BASE_API + "/circle/bar/delete";

    /**
     * 我收藏的帖子列表
     */
    public static final String CIRCLE_BAR_MYFAVORITE = BASE_API + "/circle/bar/myfavorite";

    /**
     * 我发布的帖子列表
     */
    public static final String CIRCLE_BAR_MYLIST = BASE_API + "/circle/bar/mylist";


    /**
     * 帖子评论列表页
     */
    public static final String CIRCLE_BAR_COMMENT_LIST = BASE_API + "/circle/bar/comment_list";

    /**
     * 帖子收藏
     */

    public static final String CIRCLE_BAR_FAVORITE = BASE_API + "/circle/bar/favorite";

    /**
     * 帖子收藏取消
     */
    public static final String CIRCLE_BAR_UNFAVORITE = BASE_API + "/circle/bar/unfavorite";

    /**
     * 帖子评论的点赞取消
     */
    public static final String CIRCLE_BAR_APPROVAL_DELETE = BASE_API + "/circle/bar/approval_delete";

    /**
     * 帖子评论的点赞
     */
    public static final String CIRCLE_BAR_APPROVAL_CREATE = BASE_API + "/circle/bar/approval_create";


    /**
     * 圈子列表
     **/
    public static final String CIRCLE_LIST = BASE_API + "/circle/circle/list";


    /**
     * 推荐的圈子
     */
    public static final String CIRCLE_RECOMMEND = BASE_API + "/circle/circle/recommend";

    /**
     * 我关注的圈子
     */
    public static final String CIRCLE_MY_FAVOURITE = BASE_API + "/circle/circle/myconcern";

    /**
     * 关注圈子
     */
    public static final String CIRCLE_FAVOURITE = BASE_API + "/circle/circle/like";

    /**
     * 取消关注圈子
     */
    public static final String CIRCLE_UN_FAVOURITE = BASE_API + "/circle/circle/unlike";

    /**
     * 帖子评论的回复
     */
    public static final String CIRCLE_BAR_POST_COMMENT_REPLY_CREATE = BASE_API + "/circle/bar/comment_create";

    /**
     * 举报选择项列表
     */
    public static final String CIRCLE_BAR_REPORT_LIST = BASE_API + "/circle/bar/report_list";

    /**
     * 举报发布
     */
    public static final String CIRCLE_BAR_REPORT_SEND = BASE_API + "/circle/bar/report";

    /**
     * 帖子评论详情
     */
    public static final String CIRCLE_BAR_COMMENT_DETAIL = BASE_API + "/circle/bar/comment_detail";

    /**
     * 帖子评论删除
     */
    public static final String CIRCLE_BAR_COMMENT_DELETE = BASE_API + "/circle/bar/comment_delete";


    /**
     * 首页快捷菜单接口
     * 全部显示为圈子
     */
    public static final String HOME_MENU_TAB = BASE_API + "/circle/circle/layerlist";

    /**
     * 消息中心
     * 收到的回复
     */
    public static final String MSG_CENTER_RECEIVE_REPLY = BASE_API + "/message/message/barlist";

    /**
     * 消息数量获取
     */
    public static final String MSG_CENTER_COUNT = BASE_API + "/message/message/barcount";

    /**
     * 消息中心
     * 收到的回复
     * 修改某条数据的是否已读状态
     */
    public static final String MSG_CENTER_RECEIVE_REPLY_ISREAD = BASE_API + "/message/message/operate";

    /**
     * 消息中心
     * 收到的回复
     * 修改全部数据的是否已读状态
     */
    public static final String MSG_CENTER_RECEIVE_REPLY_ALLREAD = BASE_API + "/message/message/barbatch";


    /**
     * 消息数量更改
     */
    public static final String MSG_UPDATE_STATUS = BASE_API + "/message/message/barbatch";

    /**
     *首页获取精选帖子
     */
    public static final String HOME_POSTS_HOT=BASE_API+"/circle/bar/billboardlist";

    /**
     * 推荐帖子url
     */
    public static final String CIRCLE_BAR_RECOMMENDLIST = BASE_API + "/circle/bar/recommendlist";
}


