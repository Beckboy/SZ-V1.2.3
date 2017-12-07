package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.PostCommentEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 帖子评论集合
 * Created by Sugar on 17/10/20.
 */

public class PostCommentDTO extends BaseEntity {
    /**
     * 评论数
     */
    public long totalsize;
    /**
     * 评论列表集合
     */
    public List<PostCommentEntity> result = new ArrayList<>();
}
