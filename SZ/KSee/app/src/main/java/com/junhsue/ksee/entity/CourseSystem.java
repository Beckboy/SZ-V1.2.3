package com.junhsue.ksee.entity;

/**
 * 系统课
 * Created by longer on 17/3/30.
 */

public class CourseSystem extends BaseEntity {

    /**
     * "id": "1",
     * "title": "ATC  王牌课程",
     * "description": "这是一个神奇的课程",
     * "content": "《ATC学校运营管理系统》是君学书院为培训产业的终端学校机构量身定制、精心打造的集实战、实用、实效于一体的学校运营管理体系。ATC是学校运营管理系统的简称。ATC学校运营管理系统由学校组织管理系统、学校营销系统、学校教学业务系统和学校战略决策系统四大管理系统模块构成。\n· ATC构建了学校运营管理系统的理论体系；\n· ATC为学校投资人与高管提供管理系统理论的面授学习与辅导；\n· ATC为学校管理系统建设提供全面的系统管理工具与系统解决方案；\n· ATC为学校管理子系统建设提供总部点对点部门对接指导服务；\n· ATC为学校的管理系统建设提供登门一对一个案诊断与操作方案指导。\n",
     * "amount": "9999",
     * "poster": "http://www.junhsue.com/img/atc-logo.jpg",
     * "link": "",
     * "business_id": "8",
     * "create_at": 1489653847
     */

    public String id;
    //标题
    public String title;
    //描述
    public String description;
    //内容
    public String content;
    //金额
    public String amount;
    //图片
    public String poster;
    public String link;
    public int business_id;
    public long create_at;//创建时间


}
