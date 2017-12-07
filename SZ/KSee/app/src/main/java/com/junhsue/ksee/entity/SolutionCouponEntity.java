package com.junhsue.ksee.entity;

/**
 * 方案包兑换券
 * Created by longer on 17/9/27.
 */


public class SolutionCouponEntity extends BaseEntity {


    /**
     *       "id": 25,
     "create_at": 1505977797,
     "deadline_time": 1507187397,
     "status": 2,
     "is_overdue": false,
     "title": "办学方案包",
     "description": "仅适用于方案包的兑换",
     "business_id": 11,
     "business_name": "方案包"

     */

    public String id;
    //2 表示 这个兑换券 可用  ,1  已经被使用
    public int status;
    //
    public long create_at;
    //
    public long deadline_time;
    //是否已兑换
    public boolean is_overdue;
    //
    public String title;
    //
    public String description;
    //
    public int business_id;
    //
    public String business_name;

    //是否选择
    public boolean isSelected;
}
