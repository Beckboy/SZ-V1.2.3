package com.junhsue.ksee.common;

/**
 * 系统消息类型
 * Created by longer on 17/11/16.
 */

public interface MsgType {
    /**
     * 16 点赞消息17 收藏消息18 回复消息19 系统消息
     */

    //帖子点赞
    public static final  int POSTS_LIKE=16;
    //收藏
    public static final  int POSTS_FAVOURITE=17;
    //帖子回复
    public static final  int POSTS_REPLY=18;
    //系统消息
    public static final  int MSG_SYSTEM_NOTICLE=19;
}
