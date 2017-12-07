package com.junhsue.ksee.entity;

/**
 * 消息卡片直播
 * Created by longer on 17/6/13.
 */

public class MsgLiveEntity extends BaseEntity {


    /**
     * "live_id": 1,
     "title": "信息中心第五次直播",
     "start_time": 1497232800,
     "end_time": 1497240000,
     "channel_number": 102935,
     "hx_roomid": "15799293116418"
     */

    public String live_id;
    //
    public String title;
    //直播开始的时间
    public long start_time;
    //直播结束的时间
    public long end_time;
    //直播通道编号
    public long channel_number;
    //教室id
    public long hx_roomid;
}
