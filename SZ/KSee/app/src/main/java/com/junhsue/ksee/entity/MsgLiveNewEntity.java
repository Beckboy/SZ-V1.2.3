package com.junhsue.ksee.entity;

/**
 * 新直播开始
 * Created by longer on 17/6/13.
 */

public class MsgLiveNewEntity extends BaseEntity {


    /**
     *      "live_id": 20,
     "title": "召唤师峡谷发生的命案",
     "poster": "http://omxx7cyms.bkt.clouddn.com/head.jpeg",
     "amount": "0",
     "applepay_price_id": "com.10knowing.ShiZhi0",
     "is_allowed": false,
     "is_free": true,
     "start_time": 1501504200,
     "end_time": 1501655340,
     "channel_number": 102935,
     "hx_roomid": "13641588998145",
     "speaker": "瑞兹先生"
     */

    public String live_id;
    //
    public String title;
    //
    public String poster="";
    //直播价格
    public double amount;
    //直播是否免费
    public boolean is_free;
    //直播开始的时间
    public long start_time;
    //
    public long end_time;
    //直播通道号
    public long channel_number;
    //教室id
    public long hx_roomid;
    //判断是否已经购买,免费除外
    public boolean is_allowed;
    //演讲者
    public String speaker="";
}
