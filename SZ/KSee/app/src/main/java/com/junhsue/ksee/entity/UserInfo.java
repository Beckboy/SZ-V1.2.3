package com.junhsue.ksee.entity;

/**
 * Created by longer on 17/3/16.
 */

public class UserInfo extends BaseEntity {
  /**
   *
   * "nickname":"Beck",
   * "avatar":"http://omxx7cyms.bkt.clouddn.com/hunter_1490865918723",
   * "phonenumber":18297926372,
   * "birthday":1491494400,
   * "gender":null,
   * "province":null,
   * "city":null,
   * "district":null,
   * "address":null,
   * "organization":"信息中",
   * "position_id":1,
   * "email":null,
   * "position_name":"校长",
   * "token":"TVRjPSthbVl6T0hWek16Vm9NMnczZW5GdGRHZDJhekF6WlhBd1kyMXBZM1Y2WW5rPQ=="
   *
   * 用户主键——用户绑定环信账号
   "user_id": 57,
   */

    /**
     * 用户主键——用户绑定环信账号
     */
    public String user_id;

    /**
     * 昵称
     */
    public String nickname;

    /**
     * 头像
     */
    public String avatar;

    /**
     * 手机号码
     */
    public String phonenumber;

    /**
     * 出生年月
     */
    public String birthday;

    /**
     * 性别
     */
    public String gender;

    /**
     * 省份
     */
    public String province;

    /**
     * 城市
     */
    public String city;

    /**
     * 地区
     */
    public String district;

    /**
     * 地址
     */
    public String address;

    /**
     * 所属机构或学校
     */
    public String organization;

    /**
     * 职位id
     */
    public String position_id;

    /**
     * 邮箱
     */
    public String email;

    /**
     * 职位名称
     */
    public String position_name;

    /**
     * 用户身份验证信息
     */
    public String token;


}
