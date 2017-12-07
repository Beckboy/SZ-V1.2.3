package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.VideoEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 录播的dto
 * Created by Sugar on 17/8/10.
 */

public class VideoDTO extends BaseEntity {

    public List<VideoEntity> list = new ArrayList<>();
}
