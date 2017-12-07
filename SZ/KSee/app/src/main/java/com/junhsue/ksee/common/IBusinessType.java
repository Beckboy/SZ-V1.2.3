package com.junhsue.ksee.common;

/**
 * 业务id
 * Created by longer on 17/4/1.
 */

public interface IBusinessType {

//    QUESTION(1, "问题"),
//    ANSWER(2, "答案"),
//    VOTE_Activity(3, "投票活动"),
//    ON_LINE_ACTIVITY(4, "线上活动"),
//    off_line_Activity(5, "线下活动"),
//    classroom_ACTIVITY(6, "教室"),
//    LIVE(7, "直播"),
//    SYSTEM_COURSE(8, "系统课"),
//    SUBJECT_COURSE(9, "主题课");

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
    //帖子详情
    public final static  int POST_DETAILS=14;



}
