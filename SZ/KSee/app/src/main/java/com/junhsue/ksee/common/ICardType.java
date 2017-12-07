package com.junhsue.ksee.common;

/**
 * 消息卡片类型
 * Created by longer on 17/11/6.
 */

public interface ICardType {

    //问题
    public final static int QUESTION = 1;

    //答案
    public final static int ANSWER = 2;

    //投票活动
    public final static int VOTE_ACTIVITY = 3;

    //线上活动
    public final static int ON_LINE_ACTIVITY= 4;

    //线下活动
    public final static int OFF_LINE_ACTIVITY = 5;

    //教室
    public final static int CLASSROOM_ACTIVITY = 6;

    //直播
    public final static int COURSE_LIVE = 7;

    //系统课
    public final static int COURSE_SYSTEM = 8;

    //主题课
    public final static int COURSE_SUJECT = 9;

    //直播上架
    public final static  int LIVE_NEW=10;

    //已报名直播即将开始通知
    public final static  int LIVE_START=11;

    //干货
    public final static  int REALIZE_ARTICLE=12;

    //视频
    public final static  int COLLEAGE_VEDIO=13;

    //已报名的课程即将开始
    public final static  int COLLEAGE_START=14;
    //
    //系统更新
    public final static  int SYSTEM_UPDATE=15;
    //圈子详情
    public final static  int CIRCLE_DETAILS=16;

}

