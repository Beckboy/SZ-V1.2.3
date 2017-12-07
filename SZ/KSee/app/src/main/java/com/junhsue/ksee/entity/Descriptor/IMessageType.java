package com.junhsue.ksee.entity.Descriptor;

/**
 * 消息卡片类型
 * Created by longer on 17/5/23
 */

public interface IMessageType {


    //问卷调查
    public final static int QUESTIONNAIRE = 1;
    //新手任务
    public final static int TASK = 2;


    /**
     * 社模块四类消息提醒卡片*/
    //提问的问题被回答
    public final static int QUESITON_REPLY = 3;
    //回答被点赞
    public final static int ANSWER_FAVOURITE = 4;
    //被邀请回答
    public final static int QUESTION_INVITE = 5;
    //关注的问题有新的回答
    public final static int QUESTION_FAVOURTE_REPLY = 6;


    /**
     * 活动消息两类*/
    //新的活动通知
    public final static int ACTIVITY_NEW = 7;
    //已报名的活动开始
    public final static int ACTIVITY_START = 8;

    /**
     * 教室*/
    //教室邀请
    public final static int CLASSROOM_JOIN = 9;


    /**
     * 直播*/
    //直播上架
    public final static  int LIVE_NEW=10;

    //已报名直播即将开始通知
    public final  static int LIVE_START=11;


    /**
     * 课程*/
    //月初课程表通知
    public final  static  int COLLEAGE_TABLE=12;
    //新发布的课程
    public final  static  int COLLEAGE_NEW=13;
    //已报名的课程即将开始
    public final   static  int COLLEAGE_START=14;
}
