package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * 方案包
 * Created by longer on 17/10/12.
 */

public class SolutionList extends BaseEntity {

    /**
     * "group_id": 1,
     * "group_title": "秋季招生荒，试试团购营销？",
     * "group_description": "金秋团购，团“够”今秋 ！从团购营销活动的前期宣传、中期执行、后期复盘，你能想到的所有环节，都能在这里找到方案支持。",
     * "group_is_publish": 1,
     * "group_publish_time": 1506682307,
     * "solutions": [
     * {
     * "id": 10,
     * "title": "宣传海报不用愁 | 团购营销海报模板包",
     * "description": "活动有趣却不会宣传？学校有料却无法外显？快看看这一套团购营销海报模板包。",
     * "content": null,
     * "poster": "http://omxx7cyms.bkt.clouddn.com/456.jpg",
     * "resource_link": "http://omxx7cyms.bkt.clouddn.com/poster.zip",
     * "is_publish": 1,
     * "publish_time": 1506702019,
     * "readcount": 671,
     * "sharecount": 52,
     * "approvalcount": null,
     * "favoritecount": null,
     * "tag_name": "宣传"
     * },
     */

    public String group_id;
    public String group_title;
    public String group_description;
    //
    public String group_is_publish;
    //

    public ArrayList<SolutionListItem> solutions = new ArrayList<SolutionListItem>();
}
