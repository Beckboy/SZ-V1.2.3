package com.junhsue.ksee.common;


import com.junhsue.ksee.file.FileUtil;

/**
 * 常量
 */


public class Constants {

    /**
     * 版本更新
     * 路径；
     * 拒绝更新次数：refue；
     * 上次拒绝更新时间：lastTime；
     */
    public static final String APP_UPDATE_FOLDER = "app_update_folder";
    public static final String APP_UPDATE_REFUSE_NUM = "app_update_refuse_number";
    public static final String APP_UPDATE_REFUSE_TIME = "app_update_refuse_time";

    /**
     *
     */
    public static final String APP_UPDATE_NAME = "ksee_update.apk";

    /**
     * 是否为首次登陆
     */
    public static final String ISFIRST_LOGIN = "SZ_is_first_Login";

    /**
     * 是否为首次启动APP
     */
    public static final String ISFIRST_START = "huo_hua_is_first_start";

    /**
     * 进入
     */
    public static final int PROFESSOR_RESULT = 102;


    /**
     * 赞同结果
     */
    public static final int APPROVAL_RESULT = 103;

    /**
     * 收藏结果
     */
    public static final int FAVORITE_RESULT = 104;


    /**
     * 微信登录的appkey
     */
    public static final String WEIXIN_APP_ID = "wx000c7fdd00dba35e";

    /**
     * 微信登录的appsecret
     */
    public static final String WEIXIN_APP_SECRET = "131599c1a668de70e75a148e121753d2";

    /**
     * 应用宝统计的appkey
     */

    public static final String YINGYONGBAO_APP_KEY = "Aqc1106326900";

    /**
     * 悬浮窗参数shared preference
     */
    public static final String KEY_SUSPEND_PARAMETER_X = "x";
    public static final String KEY_SUSPEND_PARAMETER_Y = "y";


    /**
     * 编辑类型
     */
    public static final String COMMON_EDIT_TYPE = "common_edit_type";

    /**
     * 是否关闭
     */
    public static final String IS_FINISH = "is_finish";

    /**
     * 问题ID
     */
    public static final String QUESTION_ID = "question_id";

    /**
     * 回答实体
     */
    public static final String ANSWER = "answer";

    /**
     * 问题
     */
    public static final String QUESTION = "question";

    /**
     * 活动
     */
    public static final String ACTIVITY = "activity";

    /**
     * 刷新问答列表广播
     */
    public static final String ACTION_REFRESH_QUESTION = "action_refresh_question";

    /**
     * 刷新问答列表的收藏状态和收藏数量或者回答数量等元素状态
     */
    public static final String ACTION_REFRESH_QUESTION_ELEMENT_STATUS = "action_refresh_question_element_status";


    /**
     * 环信
     */
    public static final String MESSAGE_ATTR_IS_VOICE_CALL = "is_voice_call";
    public static final String MESSAGE_ATTR_IS_VIDEO_CALL = "is_video_call";

    public static final String MESSAGE_ATTR_IS_BIG_EXPRESSION = "em_is_big_expression";

    public static final String VOICE_SAVE_FILE = "/voice_record/";

    public static final String VOICE_SAVE_ROOT = FileUtil.ROOTPATH + VOICE_SAVE_FILE;

    public static final String BIG_IMAGE_FILE = "/huohua_images/";


    /**
     * 更新用户信息广播
     */
    public static final String BROAD_ACTION_USERINFO_UPDATE = "userInfo_update";

    public static final String INTNET_NOT_CONNCATION = "网络连接异常";

    /**
     * 注册的用户信息
     */
    public static final String REG_LOGINTYPE = "logintype";
    public static final String REG_TYPE_P = "1";
    public static final String REG_TYPE_W = "2";
    public static final String REG_NICKNAME = "wechat_name";
    public static final String REG_AVATAR = "wechat_avatar";
    public static final String REG_SEX = "wechat_sex";
    public static final String REG_UNIONID = "wechat_unionid";
    public static final String REG_PHONENUMBER = "wechat_phonenumber";
    public static final String REG_PASSWORD = "wechat_password";

    public static final String VIDEO_ENTITY = "video_entity";
    public static final String VIDEO_TITLE = "video_title";
    public static final String VIDEO_ID = "video_id";


    //书籍
    public final static int BOOK = 10;

    //方案包
    public final static int SOLUTION_PACK = 11;

    //文章(干货)
    public final static int ARTICLE = 12;

    //上传广播
    public final static String STATISTICS_INFO = "statistics_info";
    //统计计时
    public final static String STATISTICS_LAST_TIME = "statistics_last_time";
    //统计启动次数时长间隔
    public final static String STATISTICS_LAST_START_TIME = "statistics_last_start_time";


    public final static String JUNHSUE = "2";
    public final static String WAN_DOU_JIA = "3";
    //百度--
    public final static String BAI_DU = "4";
    public final static String C360 = "5";
    public final static String YING_YONG_BAO = "6";
    public final static String HUA_WEI = "7";
    public final static String OPPO = "8";
    //------
    public final static String VIVO = "9";
    public final static String PP_ASSISTANCE = "10";
    public final static String XIAO_MI = "11";

    public final static String APP_RESTART_LAUNCH = "1";
    public final static String APP_RESTART_GO_BACK_FOREGROUND = "0";


    public final static String SF_KEY_EMAIL = "email";
    //帖子id
    public final static String POST_DETAIL_ID = "post_detail_id";

    //帖子举报ID
    public final static String REPORT_CONTENT_ID = "report_content_id";
    //帖子的业务id
    public final static String REPORT_BUSINESS_ID = "report_business_id";

    public final static String ACTION_REFRESH_POST = "action_refresh_post";
    public final static String ACTION_ADD_MYPOST = "action_add_mypost";

    public final static String POST_COMMENT_ID = "post_comment_id";

    public final static int POST_COMMENT_RESULT = 105;

    //刷新帖子列表
    public final static String ACTION_REFRESH_POST_LIST = "action_refresh_post_list";

    /**
     * 裁剪图片尾部链接
     */
    public final static String IMAGE_TAILOR_URL = "?imageView2/1/w/200/h/200";
    public final static String IMAGE_TAILOR_URL_NORMAL = "?imageView2/1/w/500/h/500";

    public final static String POST_IS_ALL_READ="post_is_all_read";
}


