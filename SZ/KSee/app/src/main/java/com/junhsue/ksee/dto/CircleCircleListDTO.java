package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.CircleCircleListEntity;

import java.util.ArrayList;

/**
 * Created by hunter_J on 2017/10/23.
 */

public class CircleCircleListDTO extends BaseEntity {

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

    public ArrayList<CircleCircleListEntity> list = new ArrayList<>();
}
