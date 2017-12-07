package com.junhsue.ksee.entity;

/**
 * 活动实体
 * Created by Sugar on 17/4/25.
 */

public class ActivityEntity extends BaseEntity {
    /**
     * "id": 1,
     * "publish_time": "2017-04-25 00:00:00",
     * "business_id": 5,
     * "business_name": "线下活动",
     * "offline_activity_id": 1,
     * "title": "春季游学",
     * "description": "春季游学大爆炸",
     * "content": "First spring",
     * "content_link": "http://www.baidu.com/SDFAKLFJDAL",
     * "poster": "http://www.baidu.com/SDFAKLFJDAL",
     * "start_time": "2017-05-04 00:00:00",
     * "end_time": "2017-05-05 00:00:00",
     * "signup_deadline": "2017-05-04 00:00:00",
     * "create_at": "2017-04-05 13:54:02"
     */

    public String id = "";
    public String publish_time = "";
    public int business_id;
    public String business_name = "";
    public String offline_activity_id = "";
    public String title = "";
    public String description = "";
    public String content = "";
    public String content_link = "";//大图链接
    public String poster = "";//
    public String start_time = "";
    //活动结束的时间
    public long end_time;
    public long signup_deadline ;
    public String create_at = "";
    public boolean is_approval;
    //商品单价
    public double price;
    //收藏数
    public int approvalcount;
    //活动地址
    public String address;
    //城市
    public String city;
}
