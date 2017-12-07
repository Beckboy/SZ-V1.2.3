package com.junhsue.ksee.entity;

import android.content.Entity;

import java.util.ArrayList;

/**
 * Created by hunter_J on 2017/8/4.
 */

public class ArticleDetail extends BaseEntity {

    /**
     * "errno": 0,
     * "msg": {
     *    "id": 1,
     *    "title": "钢铁是怎样炼成的1",
     *    "description": "千锤百炼",
     *    "content": "116.62.65.102:9000",
     *    "poster": "http://oqwd1ol5s.bkt.clouddn.com/0%281%29.jpg",
     *    "publish_at": 1501755720,
     *    "author": "eason",
     *    "sharecount": 0,
     *    "readcount": 6,
     *    "approvalcount": 1,
     *    "favoritecount": 0,
     *    "tags": [
     *        "招聘面试",
     *        "培训制度"
     *       ]
     * }
     */

    /**
     * 文章id
     */
    public int id;

    /**
     * 文章标题
     */
    public String title;

    /**
     * 文章描述
     */
    public String description;

    /**
     * 文章内容
     */
    public String content;

    /**
     * 文章海报
     */
    public String poster;

    /**
     * 发布时间
     */
    public long publish_at;

    /**
     * 文章作者
     */
    public String author;

    /**
     * 分享数
     */
    public int sharecount;

    /**
     * 阅读数
     */
    public int readcount;

    /**
     * 点赞状态
     */
    public boolean is_approval;

    /**
     * 点赞量
     */
    public int approvalcount;

    /**
     * 收藏状态
     */
    public boolean is_favorite;

    /**
     * 收藏量
     */
    public int favoritecount;

    /**
     * 标签
     */
    public ArrayList<String> tags = new ArrayList<String>();

}
