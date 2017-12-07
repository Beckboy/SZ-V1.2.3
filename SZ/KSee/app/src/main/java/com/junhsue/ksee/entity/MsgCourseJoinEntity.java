package com.junhsue.ksee.entity;

/**
 *加入的课程
 * Created by longer on 17/6/14.
 */

public class MsgCourseJoinEntity extends BaseEntity {

    /**
     *  "cour_id": 95,
     "title": "双十二6--",
     "start_time": 1497492000,
     "end_time": 1498356000,
     "days_left": 1
     */

    public String cour_id;
    //
    public String title;

    //活动开始的时间
    public long start_time;
    //
    public long end_time;

    //剩余天数
    public int days_left;
    //
    public int business_id;
}
