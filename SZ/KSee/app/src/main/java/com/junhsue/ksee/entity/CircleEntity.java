package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * 圈子
 * Created by longer on 17/10/24.
 */

public class CircleEntity extends BaseEntity {

    /**
     *    "id": 6,
     "name": "师范生了没？",
     "description": null,
     "poster": null,
     "is_auth": false,
     "order": 1,
     "is_concern": false
     */

    public String id;
    //圈子名称
    public String name="";
    //
    public String description;
    //描述
    public String poster="";
    //
    public boolean is_auth;
    //
    public int order;
    //是否已关注
    public boolean is_concern;
    //圈子公告
    public String notice;
    //
    public ArrayList<CircleEntity> circles = new ArrayList<>();

}

