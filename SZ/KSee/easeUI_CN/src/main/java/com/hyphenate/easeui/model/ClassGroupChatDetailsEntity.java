package com.hyphenate.easeui.model;

import java.io.Serializable;

/**
 * Created by hunter_J on 17/5/8.
 */

public class ClassGroupChatDetailsEntity implements Serializable {

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

  /**
   * 首字母
   */
  public String english;

  public String getEnglish() {
    return english;
  }

  public void setEnglish(String english) {
    this.english = english;
  }

  public String getNickname() {
    return nickname;
  }

  public void setNickname(String nickname) {
    this.nickname = nickname;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    ClassGroupChatDetailsEntity that = (ClassGroupChatDetailsEntity) o;

    if (!nickname.equals(that.nickname)) return false;
    return english.equals(that.english);

  }

  @Override
  public int hashCode() {
    int result = nickname.hashCode();
    result = 31 * result + english.hashCode();
    return result;
  }
}
