package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.ActivityCommentEntity;
import com.junhsue.ksee.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;


/**
 * 活动评论
 * Created by longer on 17/5/4.
 */

public class ActivityCommentDTO extends BaseEntity {


    //总条数
    public int totalsize;

    public List<ActivityCommentEntity> result=new ArrayList<ActivityCommentEntity>();
}
