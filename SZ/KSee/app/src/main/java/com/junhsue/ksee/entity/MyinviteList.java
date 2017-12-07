package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 17/4/19.
 */

public class MyinviteList extends BaseEntity {

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
   * 创建时间
   */
  public long create_at;

  /**
   * 业务号
   */
  public int id;

  /**
   * 标题
   */
  public String title;

  /**
   * 昵称
   */
  public String nickname;

  /**
   * 头像uri
   */
  public String avatar;

}
