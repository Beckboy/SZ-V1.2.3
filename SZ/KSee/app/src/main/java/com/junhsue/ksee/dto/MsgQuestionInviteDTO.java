package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MsgQuestionEntity;

/**
 * Created by longer on 17/6/7.
 */

public class MsgQuestionInviteDTO extends BaseEntity {


    /**
     *
     *"user_id": 1,
     "question_id": 1,
     "qustion": {
     "title": "招⽣生问题我们要怎么解决问题"
     },
     "nickname": "Beck",
     "avatar": "http://omxx7cyms.bkt.clouddn.com/JK_1492408223500",
     "organization": "上海海雁传书⽂文化传媒有限公司"
     }
     */
    //用户id
    public String user_id;
    //
    public String question_id;
    //用户昵称
    public String nickname;
    //
    public String avatar;
    //组织机构
    public String organization;
    //
    public MsgQuestionEntity question=new MsgQuestionEntity();
}
