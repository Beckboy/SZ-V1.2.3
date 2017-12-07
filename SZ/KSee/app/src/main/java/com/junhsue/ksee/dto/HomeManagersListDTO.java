package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.HomeManagersListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/9/8.
 */

public class HomeManagersListDTO extends BaseEntity {

    /**
     * {
     "msg": {
         "list": [
             {
             "id": 4,
             "name": "初次办学",
             "poster": null,
             "description": null,
             "tagx": [
                     {
                     "id": 23,
                     "name": "办学资质",
                     "poster": null,
                     "description": null
                     },
                 ],
             "article": [
                     {
                     "id": 65,
                     "title": "优等生都不是事儿，关键是面对差生如何提高续班率！",
                     "description": "不管何种类型的培训学校，注重新学员招生的同时对在校学员的续报率也是尤为重视的，如何让家长或者是学员继续在该校学习进行续报呢?",
                     "poster": "http://omxx7cyms.bkt.clouddn.com/gregerge.jpg"
                     }
                 ]
             },
         ]
         }
     }
     */


    public List<HomeManagersListEntity> list = new ArrayList<>();
}
