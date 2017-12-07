package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *  收藏的问题有了新的回答
 * Created by longer on 17/6/7.
 */

public class MsgQuestionFavourteReplyDTO extends BaseEntity {

    public String question_id;
    //问题标题
    public String content;
    //问题描述
    public String description;


    public List<String> answer_user_list = new ArrayList<String>();

}
