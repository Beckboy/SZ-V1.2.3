package com.junhsue.ksee.entity;

/**
 * 自定义统计实体(路径标识对象 或者 埋点标识对象)
 * Created by Sugar on 17/8/8.
 */

public class UserDefinedStatisticModel extends BaseEntity {

    /**
     * 类型(1.页面 2.埋点)
     */
    public int type;
    /**
     * 埋点或者进入页面的时间
     */
    public String time = "";
    /**
     * 统计路径(1.1.2)
     */
    public String path = "";

}
