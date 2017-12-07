package com.junhsue.ksee.entity;


/**
 * 问题被回答消息卡片
 * Created by longer on 17/6/5.
 */

public class MsgQuestionReplyEntity extends BaseEntity {

    //问题id
    public String question_id;

    //
    public String content_type_id;
    //
    public String content_id;
    //回答问题的用户
    public MsgAnswerUserEntity answer_user;
    //问题
    public MsgQuestionEntity question;


}

