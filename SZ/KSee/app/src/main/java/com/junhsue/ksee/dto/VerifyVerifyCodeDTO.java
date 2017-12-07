package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;

import java.util.List;

/**
 * 验证验证码
 * Created by hunter_J on 17/3/23.
 */

public class VerifyVerifyCodeDTO extends BaseEntity {

  /**
   * {  "errno": 0,"msg": { "msg": { "body": "{\"is_v alid\":t rue}", "http_co de": 200}}
   */
  public String body;
  public String http_code;

}
