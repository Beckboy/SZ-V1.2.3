package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.AnswerCardDetailsEntity;
import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MsgCenterReceiveReplyEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/7/26.
 */

public class MsgCenterReceiveReplyDTO extends BaseEntity {

    /**
     * {
         "id": 1649,
         "title": null,
         "description": null,
         "content": null,
         "poster": null,
         "initial_time": 1501035061,
         "finish_time": 1508811061,
         "status": 0,
         "type_id": 4,
         "type_name": "我的答案被点赞",
         "list":

     },
     */

    // 消息列表
    public List<MsgCenterReceiveReplyEntity> result = new ArrayList<MsgCenterReceiveReplyEntity>();


}
