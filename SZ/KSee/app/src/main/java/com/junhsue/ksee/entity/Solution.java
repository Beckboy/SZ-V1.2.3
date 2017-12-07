package com.junhsue.ksee.entity;

/**
 * Created by longer on 17/9/22.
 */

public class Solution extends BaseEntity {

    /**
     *        "id": 9,
     "title": "习近平总书记重磅发声！中国主张，世界回响！",
     "description": "（央视财经讯）实践证明，开放是国家繁荣发展的必由之路。面对世界经济格局的深刻调整，中国的态度是响亮的。",
     "poster": "http://omxx7cyms.bkt.clouddn.com/21a4462309f79052d1a480170ef3d7ca7bcbd564.jpg",
     "readcount": 241,
     "content": null,
     "tag_name": "校园文化"
     */

    public String id;
    public String title;
    public String description;
    //图片
    public String poster="";
    //
    public String content;
    //阅读数量
    public int readcount;
    //
    public String tag_name;
}
