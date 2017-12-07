package com.junhsue.ksee.entity;

import java.util.List;

/**
 * Created by hunter_J on 17/4/25.
 */

public class ClassRoomListEntity extends BaseEntity {

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

  public String roomid;

  public String name;

  public String poster;

  public int business_id;

  public int from_id;

  public String hx_roomid;

  public String hx_message;

  public long hx_lastmsg_time;

  public boolean is_read;

}
