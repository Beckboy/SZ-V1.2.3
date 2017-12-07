package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MycollectList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/4/20.
 */

public class MycollectDTO extends BaseEntity {

  /**
   *  我的问答-我的收藏列表
   {
   "errno": 0,
   "page": "0",
   "pagesize": "3",
   "totalnum": 5,
   "msg": {
           "result": []
         }
   }


   {
   "errno": 0,
   "msg": {
       "page": "0",
       "pagesize": "3",
       "totalnum": 5,
       "result": [
           {
           "id": 2,
           "content_id": 38,
           "business_id": 1,
           "create_at": 1490256561,
           "title": "this is a test 3333",
           "description": "dfhkjrshkrhf",
           "poster": null,
           "favoritecount": 0,
           "answercount": 0,
           "is_favorite": true,
           "avatar": "http://omxx7cyms.bkt.clouddn.com/JK_1492408223500",
           "nickname": "Beck",
           "tag": "招生招生招生招生招生招生"
           },
       ]
       }
   }
   */

  /**
   * 当前页
   */
  public int page;

  /**
   * 当前页的展示的最大条目数
   */
  public int pagesize;

  /**
   * 总条目数
   */
  public long totalnum;

  public List<MycollectList> result = new ArrayList<>();

}
