package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子内容实体
 * Created by Sugar on 17/10/19.
 */

public class PostDetailEntity extends BaseEntity {

    public String id = "";


    public String is_publish = "";

    /**
     * 帖子创建时间
     */
    public long publish_at = 0;

    /**
     * 帖子类型id
     */
    public String post_type_id = "";

    /**
     * 分享次数
     */
    public String sharecount = "";

    /**
     * 是否是匿名
     */
    public boolean is_anonymous = false;

    public String user_id = "";
    /**
     * 用户头像
     */
    public String avatar = "";
    /**
     * 昵称
     */
    public String nickname = "";

    public String post_content_id = "";

    /**
     * 标题
     */
    public String title = "";
    /**
     * 描述
     */
    public String description = "";

    /**
     * 帖子评论 业务id
     */
    public static final String BUSINESS_ID = "14";

    /**
     * 九宫格图片列表
     */
    public ArrayList<String> posters = new ArrayList<>();

    /**
     * 圈子海报
     */
    public String circle_poster;

    /**
     * 圈子公告
     */
    public String circle_notice;

    /**
     * 圈子描述
     */
    public String circle_description;

    /**
     * 是否点赞
     */
    public boolean is_approval;

    /**
     * 点赞数量
     */
    public String approvalcount = "";

    /**
     * 是否收藏
     */
    public boolean is_favorite;

    /**
     * 收藏数量
     */
    public String favoritecount = "";

    /**
     * 是否评论
     */
    public boolean is_comment;

    /**
     * 评论数量
     */
    public String commentcount = "";

    /**
     * 是否精华帖
     */
    public boolean is_top;

    /**
     * 精华帖时间
     */
    public String top_at = "";
    /**
     * 圈子id
     */
    public String circle_id = "";

    /**
     * 圈子名字
     */
    public String circle_name = "";

    /**
     * 是否关注了相应的圈子
     */
    public boolean is_concern;


}
