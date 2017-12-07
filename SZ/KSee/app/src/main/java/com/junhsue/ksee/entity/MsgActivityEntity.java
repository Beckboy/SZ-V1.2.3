package com.junhsue.ksee.entity;

/**
 * 活动
 * Created by longer on 17/6/13.
 */

public class MsgActivityEntity extends BaseEntity {

    /**
     * "activity_id": 2,
     * "offline_activity_id": 2,
     * "title": "君在雪山等你27",
     * "start_time": 1500652800,
     * "end_time": 1500739200
     */

    //活动id
    public String activity_id;
    //
    public String offline_activity_id;
    //
    public String title;
    //开始时间
    public long start_time;
    //结束时间
    public long end_time;
}
