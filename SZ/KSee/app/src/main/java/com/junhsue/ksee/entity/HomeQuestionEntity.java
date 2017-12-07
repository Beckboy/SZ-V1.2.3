package com.junhsue.ksee.entity;

/**
 * 问答
 * Created by longer on 17/8/11.
 */

public class HomeQuestionEntity extends BaseEntity {

    /**
     *  "question": {
     "id": 28,
     "title": null,
     "description": "如何让办学变得简单",
     "content": "准备办学啦，请教下大家怎么注册教育培训的公司啊！",
     "poster": null,
     "user_id": 55,
     "publish_time": "2017-08-04 19:03:18",
     "repply_deadline": null,
     "status": 1,
     "business_id": 1,
     "approvalcount": 1,
     "answercount": 15,
     "favoritecount": 2,
     "sharecount": null,
     "is_selected": 1,
     "selected_time": "2017-08-08 14:30:37",
     "create_at": "2017-08-04 19:03:18",
     "create_by": null,
     "update_at": null,
     "update_by": null,
     "is_valid": 1
     },
     */
    //问题标题
    public String id;
    //标题
    public String content;
    //问题描述
    public String description;
    /*问答的热度*/
    public long is_hot;

}
