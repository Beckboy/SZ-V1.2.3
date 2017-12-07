package com.junhsue.ksee.common;

/**
 * 文章标签
 * Created by longer on 17/8/14.
 */

public interface IArticleTag {


    /**
     * {
     * "id": 9,品牌,
     * "id": 10,name": "招生",
     * "id": 11, "name": "宣传",
     * "id": 29,  "name": "咨询回访",
     **/
    //招生
    public static final String ENTREPRENEURS_IDS = "9,10,11,29";

    public static final int ENTREPRENEURS_ID=25;
    /**
     * "id": 1, "招聘面试",
     * <p>
     * "id": 2,  "培训制度",
     * "id": 3,"name": "薪酬福利",
     * //
     * "id": 4,"name": "绩效管理",
     * "id": 17,"name": "合同协议",
     * "id": 18,"name": "校区装修",
     * "id": 19,"name": "前台接待",
     * "id": 30,"name": "校园文化",
     */
    //行政人事
    public static final String HR_IDS = "1,2,3,4,17,18,19,30";
    public static final int HR_ID=23;


    /**
     * "id": 5,"name": "领导力",
     * "id": 6,"name": "财务管理",
     * "id": 7,"name": "连锁经营",
     * "id": 8,"name": "战略管理",
     */
    //"校长核心
    public static final String PRINCIPAL_IDS = "5,6,7,8";
    public static final int PRINCIPAL_ID=24;
    /**
     * "id": 12,"name": "课程研发",
     * "id": 13,"name": "续班续费",
     * "id": 14,"name": "学员管理",
     * "id": 15,"name": "教务管理",
     * "id": 16,"name": "教学技能",
     */
    //教学教务
    public static final String TEACHING = "12,13,14,15,16";
    public static final String TEACHING_ID="26";
    /**
     * "id": 32,name": "创业访谈",
     */
    //创业者
    public static final String RECRUIT_STUDENTS_IDS = "32";
    public static final int RECRUIT_STUDENTS_ID=31;
    /**
     * "id": 20,"name": "政策解读",
     * "id": 21,"name": "产品项目",
     * "id": 22,"name": "生活情感",
     */

    //项目政策
    public static final String PROJECT_POLICY = "20,21,22";
    public static final int PROJECT_POLICY_ID=28;
}

