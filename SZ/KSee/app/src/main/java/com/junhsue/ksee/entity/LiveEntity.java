package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * 直播
 * Created by longer on 17/4/19.
 */

public class LiveEntity extends BaseEntity {

    /**
     * "live_id": 1,
     * "title": "信息中心首届直播",
     * "poster": "http://pic.baidu.com/FADKLFJA;FIJDAFDA",
     * "description": "新直播开始了",
     * "speaker": "李教官",
     * "content": "<html>\n本次直播大会如您期待\n</html>",
     * "start_time": 1490889600,
     * "end_time": 1491753600,
     * "channel_number": 102935,
     * "channel_stream": null,
     * "hx_roomid": "15799293116418",
     * "msg_notify_time": 1490716800,
     * "price": "10",
     * "rats_images": [],
     * "status": 1,
     * "is_free": 0
     */

    public String live_id;
    //标题
    public String title;
    //图片
    public String poster="";
    //描述
    public String description;
    //主讲人
    public String speaker;
    //内容
    public String content;
    //业务id
    public int business_id = 7;
    //开始时间
    public long start_time;
    //结束时间
    public long end_time;
    //渠道号
    public String channel_number;
    //直播流
    public String channel_stream;
    //直播间ID
    public String hx_roomid = "";
    //通知时间
    public long msg_notify_time;
    //
    public double price;
    //1已支付 ,2未支付
    public int status = 2;
    //是否免费 0收费,1免费
    public int is_free;

    //下架状态 1 是已上架     2是已下架
    public int shelf_status=1;
    /**
     * 1:正在直播
     * 2：直播已结束
     * 3：直播未开始
     */
    public int living_status;
    //1 是专享直播,不能参与报名
    public int is_private;
    //点赞数
    public int clicknumber;
    //图片集锦
    //public ArrayList<String>living_status rat_images;


    /**
     * 直播状态
     */
    public interface LiveStatus {

        //正在直播
        public final static int LIVING = 1;
        //已结束`
        public final static int END = 2;
        //未开始
        public final static int NO_START = 3;
    }


    /**
     * 直播支付状态
     */
    public interface LivePayStatus {

        //未支付
        public final static int PAY_NO = 2;
        //已支付
        public final static int PAY_OK = 1;
    }


}
