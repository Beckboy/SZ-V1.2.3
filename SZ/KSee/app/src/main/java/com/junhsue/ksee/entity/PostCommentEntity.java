package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;


/**
 * 帖子评论的第一层用户modle
 * Created by Sugar on 17/10/20.
 */

public class PostCommentEntity extends BaseEntity {

    /**
     * {
     * "id": 2,
     * "content": "dfkshgkshgksfhgjksfhgsfgsfg",
     * "create_at": 1508380715,
     * "nickname": "钬星人06678020",
     * "avatar": "http://omxx7cyms.bkt.clouddn.com/pic_Default_Avatar@2x.png",
     * "approval_count": 0,
     * "repply": [
     * {
     * "id": 6,
     * "content": "dfkshgkshgksfhgjksfhgsfgsfg",
     * "create_at": 1508380833,
     * "approval_count": 0,
     * "nickname": "钬星人06678020"
     * },
     * {
     * "id": 7,
     * "content": "dfkshgkshgksfhgjksfhgsfgsfg",
     * "create_at": 1508380834,
     * "approval_count": 0,
     * "nickname": "钬星人06678020"
     * }
     * ]
     * }
     */


    public String id = "";
    /**
     * 一层用户的评论内容
     */
    public String content = "";
    /**
     * 评论发布时间
     */
    public long create_at;
    /**
     * 一层评论的用户的名字
     */
    public String nickname = "";

    /**
     * 用户id
     */
    public String user_id = "";

    /**
     * 一层评论的用户头像
     */
    public String avatar = "";
    /**
     * 点赞数量
     */
    public long approval_count;
    /**
     * 是点赞过
     */
    public boolean is_approval;


    /**
     * 业务id
     */
    public static final String POST_COMMENT_BUSINESS_ID = "15";
    /**
     * 二层评论列表
     */
    ;
    public List<PostSecondCommentEntity> repply = new ArrayList<>();
}
