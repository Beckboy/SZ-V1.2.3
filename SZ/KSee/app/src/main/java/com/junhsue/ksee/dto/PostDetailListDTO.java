package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.PostDetailEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/10/24.
 */

public class PostDetailListDTO extends BaseEntity {

    /**
     * 帖子列表集合
     */
    public List<PostDetailEntity> list = new ArrayList<>();

}
