package com.junhsue.ksee.entity;

/**
 * 方案包详情
 * Created by longer on 17/9/25.
 */

public class SolutionDetails extends BaseEntity {

    /**
     *       "id": 1,
     "title": "1",
     "description": "2",
     "content": "3",
     "poster": "http://omxx7cyms.bkt.clouddn.com/123121.jpg",
     "resource_link": "5",
     "resource_type_id": 1,
     "is_publish": 1,
     "publish_time": "2017-09-21 09:51:33",
     "readcount": 105,
     "sharecount": 7,
     "approvalcount": null,
     "favoritecount": null,
     "group_id": 1,
     "tag_name": null,
     "is_auth": false
     */

    public String id;
    //标题
    public String title;
    //描述
    public String description;
    //方案包链接
    public String resource_link;
    //
    public String content;
    //图片
    public String poster="";
    //
    public String group_id;
    //是否已兑换, is_auth  true 为已兑换
    public boolean is_auth;
}
