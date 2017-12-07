package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MyaskList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/4/20.
 */

public class MyaskDTO extends BaseEntity {

  /**
   * {
     "errno": 0,
     "msg": {
         "page": "0",
         "pagesize": "3",
         "total": 4,
         "list": [
             {
               "id": 27,
               "title": "1",
               "reply": 0,
               "collect": 1,
               "content": "1",
               "publish_time": 1498187181,
               "business_id": 1,
               "business_name": "问题",
               "nickname": "Hunter",
               "avatar": "http://omxx7cyms.bkt.clouddn.com/18297926372_1498206432180",
               "fromtopic": "资本"
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
   * 当前页展示的条目数
   */
  public int pagesize;

  /**
   * 发布的总数
   */
  public int total;

  /**
   * 发布列表的详情
   */
  public List<MyaskList> list = new ArrayList<>();

}
