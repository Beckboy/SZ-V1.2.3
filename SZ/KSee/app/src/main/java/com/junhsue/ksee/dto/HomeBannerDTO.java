package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.HomeBannerEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/9/7.
 */

public class HomeBannerDTO extends BaseEntity {

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

    /**
     * 总数
     */
    public int size;

    /**
     * 数据
     */
    public List<HomeBannerEntity> list = new ArrayList<>();

}
