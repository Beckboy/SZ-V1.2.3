package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.ActivityEntity;
import com.junhsue.ksee.entity.BaseEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 活动
 * Created by Sugar on 17/4/25.
 */

public class ActivityDTO extends BaseEntity {

    //数据总大小
    public int totalsize;
    public List<ActivityEntity> list = new ArrayList<>();
}
