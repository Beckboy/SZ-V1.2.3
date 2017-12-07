package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * Created by hunter_J on 2017/10/23.
 */

public class CircleCircleListEntity extends BaseEntity {

    /**
     * "msg": {
         "list":
         [
             {
             "id": 6,
             "name": "师范生了没？",
             "description": null,
             "poster": null,
             "is_auth": false,
             "order": 1,
             "circles": [],
             "is_concern": false
             }
         ]
     }
     */

    public int id;

    public String name;

    public String description;

    public String poster;

    public String is_auth;

    public String order;

    public ArrayList<BaseEntity> circles = new ArrayList<>();

    public boolean is_concern;

}
