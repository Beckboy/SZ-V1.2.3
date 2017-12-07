package com.junhsue.ksee.entity;

/**
 * 消息课程实体
 * Created by longer on 17/6/14.
 */

public class MsgCourseEntity extends BaseEntity {

    /**
     *    "cour_id": 23,
     "title": "课程方案管理",
     "business_id": 9,
     "poster": "http://omxx7cyms.bkt.clouddn.com/FullOfHolesLetteringDeeezy1.jpg",
     "city": "上海",
     "amount": "0.1"
     */
    //课程id
    public String cour_id;
    //课程标题
    public String title;

    //业务id
    public int business_id;
    //背景图
    public String poster="";
    //
    public String city="";
    //价格
    public double amount;
}
