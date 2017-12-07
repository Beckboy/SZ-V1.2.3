package com.junhsue.ksee.entity;

/**
 * 被邀请回答
 * Created by longer on 17/6/12.
 */

public class MsgQuestionInviteEntity extends BaseEntity {

    /**
     *"question_id": 192,
     "content": "顶焦度计手机壳外壳",
     "description": "顶焦度计时间代扣代缴",
     "nickname": "langlang,"
     */

    //问题id
    public String question_id;
    //问题标题
    public String content;
    //
    public String description;
    //用户昵称
    public String nickname;

}
