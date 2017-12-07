package com.junhsue.ksee.entity;

/**
 * 首页最热添加标签实体
 * Created by longer on 17/11/29.
 */

public class HomePostsTagEntity extends BaseEntity {

    /**
     *  "id": 1,
     "catagory": "最热门",
     "boardtitle": "近一周热门 TOP 6"
     */

    public String id;
    //分类
    public String catagory="";
    //
    public String boardtitle="";
}
