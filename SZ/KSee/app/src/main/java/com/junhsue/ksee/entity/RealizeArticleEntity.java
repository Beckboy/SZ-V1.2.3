package com.junhsue.ksee.entity;

import java.util.ArrayList;

/**
 * 知文章实体
 * Created by longer on 17/8/3.
 */

public class RealizeArticleEntity extends BaseEntity {
    /**
     * "id": 26,
     * "title": "官方!内马尔正式加盟巴黎 支付巴萨2.22亿违约金",
     * "description": "北京时间8月4日凌晨，西甲豪门巴塞罗那官方宣布，内马尔的法律顾问支付了2.22亿欧元的解约金，内马尔与巴萨的合同正式终止，随后，巴黎圣日耳曼那官方宣布内马尔加盟，双方签约5年。测试数据",
     * "content": "<p><strong>周四下午，内马尔的法律代表来到俱乐部办公室并以球员名义支付了2.22亿欧元的解约金，双方的合同正式终止。俱乐部已将操作细节上报至欧足联，以便明确可能产生的法律责任。</strong></p><p><br></p>",
     * "poster": "http://omxx7cyms.bkt.clouddn.com/wpdL-fyitapv7457621.png",
     * "publish_at": 1501828118,
     * "author": "ABBY",
     * "sharecount": 0,
     * "readcount": 0,
     * "approvalcount": 0,
     * "favoritecount": 0,
     * "tags": [
     * "品牌"
     * ]
     *
     *
     */

    public String id;
    //标题
    public String title;
    //标题
    public String description;
    //
    public String content;
    //
    public String poster="";
    //
    public String publish_at;
    //
    public String author;
    //分享数量
    public int sharecount;
    //点赞数
    public int approvalcount;
    //点赞数
    public int favoritecount;
    //阅读数量
    public String readcount;
    //标签
    public ArrayList<String> tags = new ArrayList<>();
}
