package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.RealizeArticleEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 知模块文章
 * Created by longer on 17/8/4.
 */

public class RealizeArticleDTO {

    /**
     * "page": "0",
     "pagesize": "10",
     "total": 9,
     "list":
     */

    /**
     * 当前页
     */
    public int page;

    /**
     * 当前页数
     */
    public int pagesize;

    /**
     * 总数
     */
    public int total;


    public List<RealizeArticleEntity> list = new ArrayList<RealizeArticleEntity>();
}
