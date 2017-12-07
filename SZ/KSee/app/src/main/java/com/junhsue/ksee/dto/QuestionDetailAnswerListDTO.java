package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.AnswerEntity;
import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.QuestionEntity;

import java.util.ArrayList;

/**
 * 问题详情DTO
 * Created by Sugar on 17/3/28 in Junhsue.
 */

public class QuestionDetailAnswerListDTO extends BaseEntity {

    /**
     *
     *
     "page": "1",
     "pagesize": "10",
     "total": 11,
     "list": [
     {
     "id": 11,
     "content_type_id": 2,
     "publish_time": 1490010117,
     "count": 5,
     "nickname": "whoami",
     "avatar": "http://storage.pic.qiniu.com/877382683242432",
     "organization": null,
     "content": "www.baidu.com",
     "approval_count": 0,
     "is_approval": 1
     }
     ]
     */

    public int page;
    public int pagesize;
    public int total;


    public ArrayList<AnswerEntity> list = new ArrayList<>();//问题对应的回答列表

}
