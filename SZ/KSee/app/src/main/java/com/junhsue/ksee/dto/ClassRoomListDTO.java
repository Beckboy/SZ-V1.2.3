package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.ClassRoomListEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 17/4/25.
 */

public class ClassRoomListDTO extends BaseEntity {

  /**
   * {
   * "errno": 0,
   * "msg": {
   *     "roomlist": [
   *         {
   *         "roomid": 2,
   *         "name": "ATC课程17期2班",
   *         "poster": "http://img.ivsky.com/img/tupian/co/201612/26/xihuzui-001.jpg",
   *        "business_id": 9,
   *         "from_id": 2,
   *         "hx_roomid": "14264676974593"
   *         },
   *     ]
   *    }
   * }
   */

  public ArrayList<ClassRoomListEntity> roomlist = new ArrayList<ClassRoomListEntity>();

}
