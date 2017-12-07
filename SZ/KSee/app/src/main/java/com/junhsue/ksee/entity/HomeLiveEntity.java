package com.junhsue.ksee.entity;

/**
 * Created by longer on 17/8/11.
 */

public class HomeLiveEntity {


    /**
     *    "id": 23,
     "title": "解放校长 | 如何有效激励中层干部团队？",
     "poster": "http://omxx7cyms.bkt.clouddn.com/tuanduiguanli1.jpg",
     "content": "\n企业最大的资产是人，\n企业管理就是利用人去实现组织的目标。\n那么，如何激发团队的工作动力呢？\n\n一个领导善不善于激励，\n结果有天壤之别；\n一个组织有没有建立一种激励型的文化，\n直接关系到员工的精神风貌与工作状态，\n进而直接影响到企业的经营效益与发展。\n\n激励真的有这么大的效果吗？\n对于员工该如何激励才有效？\n听听黑龙江大合教育郝曲文校长的分享。\n",
     "speaker": "郝曲文",
     "start_time": 1502188200,
     "end_time": 1504179000,
     "status": 1,
     "channel_number": 102935,
     "channel_pwd": null,
     "channel_stream": null,
     "hx_roomid": "19334408765441",
     "msg_notify_time": "2017-08-08 18:15:00",
     "business_id": 7,
     "create_at": "2017-08-03 10:34:13",
     "create_by": null,
     "update_at": null,
     "update_by": null,
     "is_valid": 1,
     "sharecount": 0,
     "share_h_count": 0,
     "clicknumber": 0,
     "applepay_price_id": 1,
     "publish_time": 1502261343
     */


    public String id;
    //标题
    public String title;
    //
    public String poster;
    //
    public String content;
    //直播开始
    public long start_time;
    //主讲人
    public String speaker;
    /**
     * 1:正在直播
     * 2：直播已结束
     * 3：直播未开始
     */
    public int status;
}
