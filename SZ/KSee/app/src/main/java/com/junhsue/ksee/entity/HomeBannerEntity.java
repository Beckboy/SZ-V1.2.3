package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 2017/9/7.
 */

public class HomeBannerEntity extends BaseEntity {

    /**
     * {
     * "size": 6,
     * "list": [
     *          {
     *           "id": 2,
     *           "content_id": 88,
     *           "poster": "http://omxx7cyms.bkt.clouddn.com/005.jpg",
     *           "business_id": 1
     *         },
     *      ]
     * }
     */

    public int id;

    public String content_id;

    public String poster;

    public int business_id;

}
