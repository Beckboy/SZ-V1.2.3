package com.junhsue.ksee.net.url;

/**
 * webview  地址
 * Created by longer on 17/3/30.
 */

public class WebViewUrl {


    /**
     * 主地址
     * <p>
     * 线上环境 http://www.10knowing.com
     * <p>
     * 测试环境 :http://test1.api.10knowing.com
     * <p>
     * 统计地址
     * http://116.62.65.102:8000/statistics/statistics/upload
     */
    public final static String BASE_URL = "http://www.fm820.com";

    //public final static String H5_BASE_URL = "http://121.42.247.165";
    //public final static String H5_BASE_URL = "http://www.10knowing.com";
    public final static String H5_BASE_URL = "https://www.huohuacity.com";

    //关于拾知
    public final static String ABOUT_SZ = H5_BASE_URL + "/app/about";

    //新版介绍
    public final static String MY_VERSION_INTRODUCE = H5_BASE_URL + "/app/new_edition?is_new=%s";

    //服务协议
    public final static String MY_HIDE_POLICY = H5_BASE_URL + "/app/policy";

    //我的客服
    public final static String MY_CUSTOMER_SERVER = H5_BASE_URL + "/app/service";

    //活动页的h5分享
    public final static String H5_SHARE_ACTIVITY = H5_BASE_URL + "/h5/activity/%s";

    //问答详情页的h5分享
    public final static String H5_SHARE_QUESTION = H5_BASE_URL + "/h5/question/%s";
    //直播详情页的h5分享
    public final static String H5_SHARE_LIVE = H5_BASE_URL + "/h5/live/%s";
    //课程
    public final static String H5_SHARE_COURSE = H5_BASE_URL + "/h5/course/%s/%s";

    //活动H5内嵌页http://121.42.247.165/app/activity/1?token=XXXXXX
    public final static String H5_ACTIVITY_URL = H5_BASE_URL + "/app/activity/%s?token=%s";

    // 课程详情 http://121.42.247.165/app/course/1/9?token=XXXXXX
    public final static String H5_COURSE_URL = H5_BASE_URL + "/app/course/%s/%s?token=%s";

    //直播间 http://121.42.247.165/app/live/1/watch?user_id=1111&cid=102239&nickname=李四&groupId=15799293116418&avatar=xx.jpg&title=aaaa
    public final static String H5_LIVE_URL = H5_BASE_URL + "/app/live/%s/watch?user_id=%s&cid=%s&nickname=%s&groupId=%s&avatar=%s&title=%s";


    //干货文章详情 %s文章id
    public final static String H5_REALIZE_ARTICLE = H5_BASE_URL + "/app/hardcore/%s";
    //干货文章分享
    public final static String H5_SHARE_REALIZE_ARTICLE = H5_BASE_URL + "/h5/hardcore_share/%s";
    // 作者入驻
    public final static String H5_REALIZE_AUTHOR_DETAILS = H5_BASE_URL + "/app/admission";
    //作者入驻分享
    public final static String H5_SHARE_AUTHOR_DETAILS = H5_BASE_URL + "/h5/admission_share";
    // 企业入驻
    public final static String H5_REALIZE_ORIGAN_DETAILS = H5_BASE_URL + "/app/orz_admission";
    //机构入驻分享
    public final static String H5_SHARE_ORZ_DETAILS = H5_BASE_URL + "/h5/orz_admission";
    //录播详情
    public final static String H5_VIDEO_DETAIL_URL = H5_BASE_URL + "/app/video/%s";

    //录播详情分享
    public final static String H5_VIDEO_SHARE_URL = H5_BASE_URL + "/h5/video/%s";
    /**
     * //直播详情页
     * //内置页面->http://www.10knowing.com/app/live_details/3（user_id）/22(live_id)
     */
    public final static String H5_LIVE_DETAILS = H5_BASE_URL + "/app/live_details/%s/%s";
    //
    /**
     * 直播详情分享页
     * http://www.10knowing.com/h5/live_details/3（user_id）/22(live_id)
     */
    public final static String H5_SHARE_H5_LIVE_DETAILS = H5_BASE_URL + "/h5/live_details/%s/%s";

    /**
     * 直播观看页分享h5
     * <p>
     * <p>
     * http://www.10knowing.com/h5/live_share/:id
     * <p>
     * %s 直播id
     */
    public final static String H5_SHARE_LIVE_LOOK = H5_BASE_URL + "/h5/live_share/%s";

    /**
     * 邀请人员注册H5
     */
    public final static String H5_INVITATION_URL = H5_BASE_URL + "/app/invitation/%s/%s";

    /**
     * 邀请人员注册分享
     */
//    public final static String H5_INVITATION_SHARE = H5_BASE_URL + "/h5/invitation_share/%s";


    /**
     * 方案包详情
     * <p>
     * 第一个参数为用户id ,第二个参数为方案详情
     * www.10knowing.com/app/spg/%s/%s
     */
    public final static String H5_SOLUTION = H5_BASE_URL + "/app/spg/%s/%s";

    /**
     * 方案包h5分享
     * <p>
     * 第一个参数为用户id ,第二个参数为方案详情
     */
    public final static String H5_SHARE_SOLUTION = H5_BASE_URL + "/h5/spg_share/%s/%s";


    public final static String H5_INVITATION_SHARE = H5_BASE_URL + "/h5/invitation_share/%s/%s";

    /**
     * 钬花下载链接
     */
    public final static String H5_INVITATION_DOWNLOAD = H5_BASE_URL + "/h5/download";



//    public final static String H5_INVITATION_SHARE = H5_BASE_URL + "/h5/invitation_share/%s/%s";

    /**
     * 帖子详情分享url
     */
    public final static String H5_POST_SHARE = H5_BASE_URL + "/h5/posting/%s";

}
