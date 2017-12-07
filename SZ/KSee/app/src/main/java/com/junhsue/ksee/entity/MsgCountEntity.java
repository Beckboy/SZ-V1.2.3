package com.junhsue.ksee.entity;

/**
 * 消息数量
 * Created by longer on 17/11/15.
 */

public class MsgCountEntity extends BaseEntity {
    /**
     *    "message_type_id_0": 2, 全部消息数量
     *
     "message_type_id_16": 2,  对应被点赞消息数量量
     "message_type_id_17": 0,  业务id为17  对应被收藏消息数量量
     "message_type_id_18": 0,  业务id为18  对应被回复消息数量量
     "message_type_id_19": 0   对应系统消息数量量

     */

    //全部消息数量
    public int message_type_id_0;

    public int message_type_id_16;    //对应被点赞消息数量量

    public int message_type_id_17;    //对应被收藏消息数量量

    public int message_type_id_18;    //对应被回复消息数量量

    public int message_type_id_19;    //对应系统消息数量量
}
