package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sugar on 17/10/20.
 */

public class PostSecondCommentEntity extends BaseEntity {
    /**
     * {
     * "id": 6,
     * "content": "dfkshgkshgksfhgjksfhgsfgsfg",
     * "create_at": 1508380833,
     * "approval_count": 0,
     * "nickname": "钬星人06678020"
     * }
     */

    public String id = "";
    /**
     * 文本内容
     */
    public String content = "";
    /**
     * 发布时间
     */
    public long create_at;
    /**
     * 点赞量
     */
    public long approval_count;
    /**
     * 二层评论的用户名
     */
    public String nickname = "";

    public String avatar = "";


}
