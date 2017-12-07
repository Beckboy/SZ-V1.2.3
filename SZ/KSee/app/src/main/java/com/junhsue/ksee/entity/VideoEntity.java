package com.junhsue.ksee.entity;

/**
 * 录播的实体
 * Created by Sugar on 17/8/10.
 */

public class VideoEntity extends BaseEntity {

    /**
     * {
     * "id": 1,
     * "title": "90后教育人的创业",
     * "description": "90后创业故事",
     * "content": null,
     * "poster": null,
     * "address": "http://v.youku.com/v_show/id_XMjk0NTUzOTY0OA==.html?spm=a2hww.20023042.m_223465.5~5~5~5~5~5~A",
     * "create_at": "2017-08-10 09:04:51",
     * "publish_at": "2017-01-01",
     * "is_publish": 1,
     * "author": null,
     * "sharecount": 0,
     * "readcount": 0,
     * "approvalcount": 0,
     * "favoritecount": 0,
     * "is_del": 0,
     * "update_at": null,
     * "commentcount": 0
     * }
     */

    public String id = "";
    //标题
    public String title = "";
    //描述
    public String description = "";
    //内容
    public String content = "";
    //视频图片链接地址
    public String poster = "";
    //视频链接地址
    public String address = "";
    //创建时间
    public String create_at = "";

    public String publish_at = "";

    public String is_publish = "";//?

    //作者,来源,主讲人
    public String author = "";

    //分享量
    public long sharecount;
    //阅读量
    public long readcount;
    //点赞量
    public long approvalcount;
    //收藏量
    public long favoritecount;
    //
    public String is_del = "";
    //
    public String update_at = "";
    //评论量
    public long commentcount;


}
