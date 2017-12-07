package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 17/4/20.
 */

public class MyaskList extends BaseEntity {

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
       }
   }
   */

  /**
   * 各发布话题对应的id号
   */
  public long id;

  /**
   * 话题的标题
   */
  public String title;

  /**
   * 话题回复数
   */
  public String reply;

  /**
   * 话题收藏数
   */
  public String collect;

  /**
   * 话题描述
   */
  public String content;

  public String business_id;

  public String business_name;

  /**
   * 话题发布时间
   */
  public long publish_time;

  /**
   * 发布者昵称
   */
  public String nickname;

  /**
   * 发布者头像
   */
  public String avatar;

  /**
   * 话题类型标签
   */
  public String fromtopic;

}
