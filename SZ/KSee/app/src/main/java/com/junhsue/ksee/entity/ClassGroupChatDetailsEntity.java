package com.junhsue.ksee.entity;

/**
 * Created by hunter_J on 17/5/8.
 */

public class ClassGroupChatDetailsEntity extends BaseEntity {

  /**
   * "errno": 0,
   "msg": {
   "memberlist": [
   {
   "user_id": 1,
   "nickname": "Beck",
   "avatar": "http://omxx7cyms.bkt.clouddn.com/JK_1492408223500",
   "org": "上海雁传书文化传媒有限公司"
   },
   ]
   }
   */

  /**
   * 用户id
   */
  public String user_id;

  /**
   * 用户名
   */
  public String nickname;

  /**
   * 用户头像网址
   */
  public String avatar;

  /**
   * 用户学校/组织信息
   */
  public String org;

}
