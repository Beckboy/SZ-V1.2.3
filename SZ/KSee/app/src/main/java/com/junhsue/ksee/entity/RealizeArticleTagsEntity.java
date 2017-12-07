package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * Created by hunter_J on 2017/8/10.
 */

public class RealizeArticleTagsEntity extends BaseEntity {

    /**
     * "result": [
     {
     "id": 23,
     "name": "HR",
     "poster": null,
     "sub": [
     {
     "id": 1,
     "name": "招聘面试",
     "poster": null
     },
     ]
     },
     */

    public String id;

    public String name;

    public String poster;

    public ArrayList<TagsEnitity> sub = new ArrayList<>();


    public class TagsEnitity extends BaseEntity{

        public String id;

        public String name;

        public String poster;

    }



}
