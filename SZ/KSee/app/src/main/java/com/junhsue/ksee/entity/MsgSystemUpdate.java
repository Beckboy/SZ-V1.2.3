package com.junhsue.ksee.entity;

/**
 * 系统更新
 * Created by longer on 17/8/9.
 */

public class MsgSystemUpdate extends BaseEntity {

    /**
     * "upgrade_id": 1,
     "version": "1.0.3",
     "address": "http://www.baidu.com",
     "title": "1.0.2更新来了，快去下载吧",
     "description": "1. 优化页面展示\r\n2. 修复部分问题\r\n3. 优化资源加载提升性能",
     "create_at": 1502265312
     */

    public String upgrade_id;
    //
    public String version;
    //地址
    public String address;
    //标题
    public String title;
    //描述
    public String description;
    // 创建时间

}
