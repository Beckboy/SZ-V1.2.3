package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MsgQuestionReplyEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 消息卡片 提出对问题被回答
 * Created by longer on 17/6/5.
 */

public class MsgQuestionReplyDTO extends BaseEntity {


    /**
     * "list": {
     "question_id": 183,
     "content": "你来了啊",
     "description": "留下",
     "answer_user_list": [
     "ben"
     ]
     */

    public String question_id;
    //问题标题
    public String content;
    //问题描述
    public String description;


    public List<String> answer_user_list = new ArrayList<String>();

}
