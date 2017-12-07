package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 17/4/20.
 */

public class MycollectList extends BaseEntity {

  /**
   *  我的问答-我的收藏列表
   {
   "errno": 0,
   "msg": {
       "result": [
                   {
                   "id": 2,
                   "content_id": 38,
                   "business_id": 1,
                   "create_at": 1490256561,
                   "title": "this is a test 3333",
                   "description": "dfhkjrshkrhf",
                   "poster": null,
                   "is_favorite": false,
                   "avatar": "http://omxx7cyms.bkt.clouddn.com/JK_1492408223500",
                   "nickname": "Beck",
                   "tag": "招生招生招生招生招生招生"
                   },
   {
   "favoritecount": 0,
   "answercount": 0,
   },
              ]
       }
   }
   */


  public long id;

  public long content_id;

  public int business_id;

  public long create_at;

  public long publish_time;

  public String title;

  public String content;

  public String poster;

  public long collect;

  public long reply;

  public boolean is_favorite;

  public String avatar;

  public String nickname;

  public String fromtopic;





}
