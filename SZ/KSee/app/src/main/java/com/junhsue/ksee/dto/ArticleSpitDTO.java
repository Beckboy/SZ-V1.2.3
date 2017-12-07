package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.ArticleSpitComment;
import com.junhsue.ksee.entity.BaseEntity;

/**
 *
 * Created by chenlang on 16/10/24 in Junhsue.
 */
public class ArticleSpitDTO extends BaseEntity {


    public String id;
    public String title;//标题
    public String descs;//描述
    public String post;//图片
    public String weekorder;
    public int dayorder;
    public String publishtime;
    public String block_id;
    public String block_name;

    public boolean isFavorite;
    public String readCount="0";

    public ArticleSpitComment comments;//

    /**
     *    "id": 8,
     "title": "都来吐槽",
     "descs": "都来吐吐槽",
     "content": "吐槽内容",
     "post": "http://www.hooolive.com:/uploads/channelpost10a.jpg",
     "weekorder": 1,
     "dayorder": 6,
     "publishtime": "2016-10-19 11:00:21",
     "block_id": 9,
     "block_name": "吐槽",
     "comments": {
     "mycomments": [],
     "hotcomments": [
     {
     "id": 7,
     "informations": "32",
     "createtime": "2016-10-13 07:14:23",
     "block_id": 1,
     "content_id": 8,
     "user_id": 1,
     "rank": null,
     "valid": 1
     },
     {
     "id": 9,
     "informations": "评论内容",
     "createtime": "2016-10-13 07:17:18",
     "block_id": 1,
     "content_id": 8,
     "user_id": 1,
     "rank": null,
     "valid": 1
     }
     ]
     }
     */
}
