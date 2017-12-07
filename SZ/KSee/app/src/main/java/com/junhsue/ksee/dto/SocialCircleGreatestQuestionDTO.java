package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.QuestionEntity;

import java.util.ArrayList;

/**
 * 精选问答集合
 * Created by Sugar on 17/3/21 in Junhsue.
 */

public class SocialCircleGreatestQuestionDTO extends BaseEntity {
    public ArrayList<QuestionEntity> Greatests = new ArrayList<>();
}
