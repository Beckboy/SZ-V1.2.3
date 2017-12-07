package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MyinviteList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MyinviteDTO extends BaseEntity {

  /**
   "errno": 0,
   "msg": {
   "page": "1",
   "pagesize": "3",
   "total": 4,
   "list": [
   {
   "create_at": "2017-04-19 10:29:36",
   "id": 109,
   "title": "",
   "nickname": "疯子一样",
   "avatar": "http://omxx7cyms.bkt.clouddn.com/suger_1492414765613"
   }
   ]
   }
   */

  /**
   * 当前页
   */
  public int page;

  /**
   * 当前页展示的数据数
   */
  public int pagesize;

  /**
   * 被邀请的条目总数
   */
  public int total;

  public List<MyinviteList> list = new ArrayList<>();

}
