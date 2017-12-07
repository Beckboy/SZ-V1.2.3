package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.RealizeArticleTagsEntity;

import java.util.ArrayList;

/**
 * Created by hunter_J on 2017/8/10.
 */

public class RealizeArticleTagsDTO extends BaseEntity {

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

    public ArrayList<RealizeArticleTagsEntity> result = new ArrayList<>();


}
