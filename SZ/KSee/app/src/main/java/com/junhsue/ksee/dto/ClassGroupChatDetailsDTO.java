package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.ClassGroupChatDetailsEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/5/8.
 */

public class ClassGroupChatDetailsDTO extends BaseEntity {

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

  public List<ClassGroupChatDetailsEntity> memberlist = new ArrayList<ClassGroupChatDetailsEntity>();


}
