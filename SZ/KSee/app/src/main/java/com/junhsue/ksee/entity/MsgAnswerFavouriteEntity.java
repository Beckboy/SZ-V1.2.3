package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * Created by longer on 17/6/8.
 */

public class MsgAnswerFavouriteEntity extends BaseEntity {

    /**
     *      "question_id": 187,
     "content": "本本叔",
     "description": "本本叔",
     "answer_id": 496,
     "answer_content": "gf",
     "answer_link": null,
     "answer_type_id": 1,
     "answer_user_nickname": [
     "Hunter"
     ]
     */

    //问题id
    public String question_id;
    //问题标题
    public String content;
    //
    public String description;
    //
    public String answer_id;
    //回答的内容
    public String answer_content;
    //
    public String answer_link;
    //
    public String answer_type_id;

    public ArrayList<String> answer_user_nickname=new ArrayList<String>();
}
