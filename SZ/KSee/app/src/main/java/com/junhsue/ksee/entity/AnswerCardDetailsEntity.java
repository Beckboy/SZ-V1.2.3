package com.junhsue.ksee.entity;

import com.junhsue.ksee.entity.AnswerCardDetailEntity;
import com.junhsue.ksee.entity.BaseEntity;

/**
 * Created by hunter_J on 2017/7/26.
 */

public class AnswerCardDetailsEntity extends BaseEntity {

    /**
     * {
         "id": 1649,
         "title": null,
         "description": null,
         "content": null,
         "poster": null,
         "initial_time": 1501035061,
         "finish_time": 1508811061,
         "status": 0,
         "type_id": 4,
         "type_name": "我的答案被点赞",
         "list":

     },
     */

    // 问题id
    public long id;

    // 问题标题
    public String title;

    // 问题描述
    public String description;

    // 问题内容
    public String content;

    //问题海报
    public String poster;

    //发布时间
    public long initial_time;

    // 结束时间
    public long finish_time;

    //状态
    public int status;

    //卡片类型
    public int type_id;

    // 卡片标题
    public String type_name;

    // 消息列表
    public AnswerCardDetailEntity list;


}
