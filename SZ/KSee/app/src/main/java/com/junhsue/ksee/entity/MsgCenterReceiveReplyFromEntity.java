package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/7/26.
 */

public class MsgCenterReceiveReplyFromEntity extends BaseEntity {

    /**
     * "data": {
     * "business_id": 15,
     * "business_name": "帖子评论",
     * "reply_user_id": 2,
     * "reply_nickname": "本本 💹",
     * "reply_avatar": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKibqX2nPhYJDWZcaQuxpoSvgjvZeB9OUBNKFAOYYVHic2c8SRcdpCmM6ibShv1q08PWaskZJ9V5Feibg/0",
     * "debug_msg": "评论 被回复 ",
     * "comment_content": "你好，朱师傅",
     * "comment_business_id": 15,
     * "comment_content_title": "测试一下评论帖子",
     * "post_bar_id": 97
     * }
     * ==============
     * <p>
     * {
     * "business_id": 14,
     * "business_name": "帖子",
     * "favorite_user_id": 999999,
     * "favorite_nickname": null,
     * "favorite_avatar": null,
     * "debug_msg": "帖子 被收藏",
     * "post_content_title": "123",
     * "post_bar_id": 12
     * }
     * <p>
     * =======
     * {
     * "business_id": 15,
     * "business_name": "帖子评论",
     * "approval_user_id": 2,
     * "approval_nickname": "本本 💹",
     * "approval_avatar": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKibqX2nPhYJDWZcaQuxpoSvgjvZeB9OUBNKFAOYYVHic2c8SRcdpCmM6ibShv1q08PWaskZJ9V5Feibg/0",
     * "debug_msg": "评论的评论 被点赞",始培养我们解决问题的能力，下午的课启发我很多，也越来越期待接下来几天的培训。",
     * "my_comment_my_user_id": 14226,
     * "my_comment_my_user_nickname": "佘富阳",
     * "my_comment_my_user_avatar": "http://omxx7cyms.bkt.clouddn.com/18396210839_1510582805880",
     * "comment_to_business_id": 15,
     * "comment_to_others_comment_content": "评论内容",
     * "comment_to_others_comment_user_id": 14255,
     * "comment_to_others_comment_user_nickname": "钬星人10537208",
     * "comment_to_others_comment_user_avatar": "http://omxx7cyms.bkt.clouddn.com/pic_Default_Avatar@2x.png",
     * "post_bar_id": 697
     * }
     * <p>
     * <p>
     * {
     * "business_id": 14,
     * "business_name": "帖子",
     * "approval_user_id": 2,
     * "approval_nickname": "本本 💹",
     * "approval_avatar": "http://wx.qlogo.cn/mmopen/vi_32/Q0j4TwGTfTKibqX2nPhYJDWZcaQuxpoSvgjvZeB9OUBNKFAOYYVHic2c8SRcdpCmM6ibShv1q08PWaskZJ9V5Feibg/0",
     * "debug_msg": "帖子 被点赞",
     * "post_content_title": "好像阿汤举行如何",
     * "post_bar_id": 100,
     * "my_post_my_user_id": 13466,
     * "my_post_my_user_nickname": "张婷",
     * "my_post_my_user_avatar": "http://omxx7cyms.bkt.clouddn.com/pic_Default_Avatar@2x.png"
     * }
     */

    // 业务id
    public String business_id;

    // 业务名称
    public String business_name;

    // 评论用户id
    public String reply_user_id;

    // 评论用户昵称
    public String reply_nickname;

    // 评论用户头像
    public String reply_avatar;

    // 消息type
    public String debug_msg;

    // 消息内容
    public String comment_content;

    // 消息类型业务id
    public String comment_business_id;

    // 消息标题
/*    public String comment_content_title;

    public String post_bar_id;

    //帖子内容
    public String post_content_title;*/
    //收藏了你的帖子用户id
    public String favorite_user_id;
    //收藏了你的帖子的用户名
    public String favorite_nickname;
    //收藏了你的帖子的用户头像
    public String favorite_avatar;

    /*
     * 被赞的帖子评论的发帖人的用户id
     */
    public String approval_user_id;
    /*
     *
     * 被赞的帖子评论的发帖人的用户名
     */
    public String approval_nickname;
    /*
     * 被赞的帖子评论的发帖人的用户头像
     */
    public String approval_avatar;

    /**
     * 帖子评论的用户id
     */
    public String my_comment_my_user_id;
    /**
     * 帖子评论的用户名
     */
    public String my_comment_my_user_nickname;

    /**
     * 帖子评论的用户名头像
     */
    public String my_comment_my_user_avatar;
    /**
     * 帖子评论的业务id
     */
    public String comment_to_business_id;

    /**
     * 评论内容
     */
    public String comment_to_others_comment_content;

    /**
     * 评论内容的用户id
     */
    public String comment_to_others_comment_user_id;

    /**
     *
     */
    public String comment_to_others_comment_user_nickname;

    /**
     *
     */
    public String comment_to_others_comment_user_avatar;

    /**
     *
     */
    public String my_post_my_user_id;

    public String my_post_my_user_nickname;
    // 回复的评论内容
    public String comment_content_title;

    // 帖子标题
    public String post_content_title;
    //帖子id
    public String post_bar_id;
    //是否是加精帖子  value=1为加精帖子，否则为其他消息
    public int bar_info_type_id;

    //内容
    public String bar_info_type_name;
    //附属信息
    public String infocomment;


    public String my_post_my_user_avatar;

}

