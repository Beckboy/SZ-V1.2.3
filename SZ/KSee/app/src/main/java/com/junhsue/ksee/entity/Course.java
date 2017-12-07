package com.junhsue.ksee.entity;

/**
 * 课程
 * Created by longer on 17/3/27.
 */

public class Course extends BaseEntity {


    public String id;

    //标题
    public String title;
    //简介
    public String description;
    //内容
    public String content;
    //图片
    public String poster="";
    //城市
    public String city;
    //地址
    public String address;
    //课程价格
    public double amount;
    //首页卡片提醒的时间
    public String msg_notify_time;
    /**
     * 业务id
     * @see  com.junhsue.ksee.entity.GoodsInfo.GoodsType
     * */
    public int business_id;
    //上架状态
    public int status;
    //
    public String is_valid;
    //开课时间
    public long start_time;
    //结束时间
    public long end_time;

    public String create_at;

    public String create_by;

    public String update_at;

    public String update_by;

    public String link;

    public String signup_deadline;

    public String publish_time;

    public int shelf_status;
}
