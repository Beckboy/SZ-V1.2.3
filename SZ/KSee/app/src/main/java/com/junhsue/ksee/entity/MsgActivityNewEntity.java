package com.junhsue.ksee.entity;

/**
 * 新的活动提醒
 * Created by longer on 17/6/13.
 */

public class MsgActivityNewEntity extends BaseEntity {

    /**
     * "activity_id": 1,
     * "offline_activity_id": 1,
     * "title": "春季游学",
     * "poster": "http://photocdn.sohu.com/20150910/mp31403414_1441888943372_19.jpeg",
     * "city": "杭州",
     * "signup_deadline": 1493827200,
     * "is_free": false,
     * "price": "0.01"
     */

    //活动id
    public String activity_id;
    //
    public String offline_activity_id;
    //活动标题
    public String title;
    //图片地址
    public String poster="";
    //城市
    public String city;
    //
    public long signup_deadline;
    //是否免费
    public boolean is_free;
    //
    public double price;
}
