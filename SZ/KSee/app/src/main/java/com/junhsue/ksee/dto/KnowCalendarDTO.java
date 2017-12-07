package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;

/**
 * Created by hunter_J on 2017/5/18.
 */

public class KnowCalendarDTO extends BaseEntity {
  /**
   * {
   * "errno": 0,
   * "msg": {
   *    "id": 8,
   *    "url": "https://ss3.bdstatic.com/70cFv8Sh_Q1YnxGkpoWK1HF6hhy/it/u=1964925782,2901320879&fm=23&gp=0.jpg",
   *    "url_share":  用于分享的单向历图片
   *    "publish_time": 1495080875,
   *    "share_count": 0,
   *    "islike": 0
   *    like_count:12
   * }
   */

  public String id;

  public String url;

  public String url_share;

  public long share_count;

  public long publish_time;

  //  0 是不喜欢 1是喜欢
  public int is_liked;

  public int like_count;
}
