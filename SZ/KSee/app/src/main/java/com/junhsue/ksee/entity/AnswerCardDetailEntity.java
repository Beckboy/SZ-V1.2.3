package com.junhsue.ksee.entity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/7/26.
 */

public class AnswerCardDetailEntity extends BaseEntity {

    //==========  提问的问题被回答  ============
    /**
     * "list": {
         "question_id": 80,
         "content": "信息中心叼不叼",
         "description": "谁最屌",
         "answer_user_list": [
             "NullPoint"
            ]
         }
     */
    //==========  关注的问题有新的回答  ============
    /**
     * "list": {
         "question_id": 80,
         "content": "信息中心叼不叼",
         "description": "谁最屌",
         "answer_user_list": [
             "NullPoint"
             ]
     }
     */
    //==========  我的答案被点赞  ============
    /**
     * "list": {
     "data":
        [
             {
             "question_id": 80,
             "content": "信息中心叼不叼",
             "description": "谁最屌",
             "answer_id": 401,
             "answer_content": "很叼",
             "answer_link": null,
             "answer_type_id": 1,
             "answer_user_nickname": [
                 "本本"
                 ]
             }
         ]
     }
     */
    //==========  被邀请回答  ============
    /**
     * "list": {
         "question_id": 85,
         "content": "设计之理念",
         "description": "无敌之真",
         "nickname": "本本"
     }
     */

    public String question_id;

    public String content;

    public String description;

    public String nickname;

    public List<String> answer_user_list = new ArrayList<String>();

    public List<AnswerCardPraiseEntity> data = new ArrayList<AnswerCardPraiseEntity>();



    public class AnswerCardPraiseEntity{

        /**
         * "data":
             [
                 {
                     "question_id": 80,
                     "content": "信息中心叼不叼",
                     "description": "谁最屌",
                     "answer_id": 401,
                     "answer_content": "很叼",
                     "answer_link": null,
                     "answer_type_id": 1,
                     "answer_user_nickname": [
                         "本本"
                         ]
                 }
             ]
            }
         */

        public String question_id;

        public String content;

        public String description;

        public String answer_id;

        public String answer_content;

        public String answer_link;

        public String answer_type_id;

        public List<String> answer_user_nickname = new ArrayList<String>();


    }


}
