package com.junhsue.ksee.entity;

/**
 *
 *活动评论
 * Created by longer on 17/5/4.
 * */

public class ActivityCommentEntity  extends BaseEntity {


    /**
     *   "id": 28,
     "content_id": 1,
     "business_id": 7,
     "business_name": "直播",
     "content": "“我”",
     "create_at": 1493863144,
     "user_id": 58,
     "avatar": "http://omxx7cyms.bkt.clouddn.com/lang_1492408363944",
     "nickname": "langlang",
     "position": "投资人
     */
    public  String id;
    //文章id
    public  String content_id;
    //业务id
    public  int business_id;
    //评论
    public String content;
    //评论创建的时间
    public long create_at;
    //
    public String user_id;
    //头像
    public String avatar;
    //昵称
    public String nickname;
    //单位
    public String position;
}
