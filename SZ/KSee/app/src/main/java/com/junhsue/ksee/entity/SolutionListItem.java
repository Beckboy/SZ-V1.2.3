package com.junhsue.ksee.entity;

/**
 * 方案包项
 * Created by longer on 17/10/12.
 */

public class SolutionListItem extends BaseEntity {

    /**
     *        "id": 10,
     "title": "宣传海报不用愁 | 团购营销海报模板包",
     "description": "活动有趣却不会宣传？学校有料却无法外显？快看看这一套团购营销海报模板包。",
     "content": null,
     "poster": "http://omxx7cyms.bkt.clouddn.com/456.jpg",
     "resource_link": "http://omxx7cyms.bkt.clouddn.com/poster.zip",
     "is_publish": 1,
     "publish_time": 1506702019,
     "readcount": 671,
     "sharecount": 52,
     "approvalcount": null,
     "favoritecount": null,
     "tag_name": "宣传"
     */

    public String id;
    //
    public String title;
    //描述
    public String description;
    //
    public String poster="";
    //
    public String resource_link="";
    //
    public long publish_time;
    //浏览数量
    public int readcount;
    //
    public String tag_name;

}


