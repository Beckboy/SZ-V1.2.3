package com.junhsue.ksee.entity;

/**
 * 录播视频
 * Created by longer on 17/8/11.
 */

public class HomeVideoEntity extends BaseEntity {

    /**
     *       {
     "id": 18,
     "title": "AalaEl-Khani-在战区做家长是怎样的体验呢",
     "description": "AalaEl-Khani-在战区做家长是怎样的体验呢AalaEl-Khani-在战区做家长是怎样的体验呢AalaEl-Khani-在战区做家长是怎样的体验呢",
     "content": null,
     "poster": "http://omxx7cyms.bkt.clouddn.com/timg (1).jpg",
     "address": "http://ougi9h16i.bkt.clouddn.com/AalaEl-Khani-%E5%9C%A8%E6%88%98%E5%8C%BA%E5%81%9A%E5%AE%B6%E9%95%BF%E6%98%AF%E6%80%8E%E6%A0%B7%E7%9A%84%E4%BD%93%E9%AA%8C%E5%91%A2_%E8%B6%85%E6%B8%85.mp4",
     "create_at": "2017-08-12 18:12:20",
     "publish_at": "2017-08-12",
     "is_publish": 1,
     "author": null,
     "sharecount": 0,
     "readcount": 0,
     "approvalcount": 0,
     "favoritecount": 0,
     "is_del": 0,
     "update_at": null,
     "commentcount": 0
     },
     */

    public String id;
    //标题
    public String title;
    //
    public String description;
    //图片
    public String poster;
    //播放次数
    public int readcount;
    //播放时长
    public String duration="";

}
