package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;

/**
 * Created by hunter_J on 2017/9/28.
 */

public class VersionUpdateDTO extends BaseEntity {

    /**
     *  "msg": {
     *    "id": 1,
     *    "platform_id": 2,
     *    "version": "1.0.8",
     *    "address": "http://www.10knowing.com"
     *  }
     */

    public String id;

    public String platform_id;

    public boolean is_new = true;

    public String version;

    public String address;

}
