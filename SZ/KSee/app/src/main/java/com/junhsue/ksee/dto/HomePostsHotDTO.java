package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.HomePostsTagEntity;
import com.junhsue.ksee.entity.PostDetailEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页热门帖子
 * Created by longer on 17/11/28.
 */

public class HomePostsHotDTO extends BaseEntity {

    /**
     * "billboard": {
     * "id": 1,
     * "catagory": "最热门",
     * "boardtitle": "近一周热门 TOP 6"
     * },
     * "list": [
     * {
     * "id": 1168,
     * "is_publish": true,
     * "publish_at": 1511400615,
     * "post_type_id": 1,
     * "sharecount": null,
     * "is_anonymous": false,
     * "user_id": 4182,
     * "weight": 39.8,
     * "weightrecommend": 10.4,
     * "avatar": "http://omxx7cyms.bkt.clouddn.com/pic_Default_Avatar@2x.png",
     * "nickname": "刘捷",
     * "post_content_id": 1168,
     * "title": "又见平遥",
     * "description": "《又见平遥》是一种精神，一种文化，一种情怀，那里有我们世代相传的根的文化",
     * "posters": [
     * "http://omxx7cyms.bkt.clouddn.com/15713558166_1511400611359_1256",
     * "http://omxx7cyms.bkt.clouddn.com/15713558166_1511400612473_3537",
     * "http://omxx7cyms.bkt.clouddn.com/15713558166_1511400613378_216"
     * ],
     * "is_approval": false,
     * "approvalcount": 25,
     * "is_favorite": false,
     * "favoritecount": 0,
     * "is_comment": false,
     * "commentcount": 1,
     * "is_top": false,
     * "top_at": null,
     * "circle_id": 44,
     * "circle_name": "校园摄影师",
     * "circle_poster": "http://omxx7cyms.bkt.clouddn.com/xysys@3x.png",
     * "circle_description": "校园的美，一起去发现",
     * "circle_notice": "第一届最“钬”校园摄影师评选大赛即将隆重开幕！！！\n\n▼活动时间：11月20日-11月30日\n\n▼活动流程ლ(╹◡╹ლ)\n\n①升级钬花，发布照片到「校园摄影师」\n②分享帖子，让好友帮忙点赞\n③联系官方小钬花儿（微信号：huohua_edu）\n④按规则领取1-1000元不等的现金红包\n\n▼发帖5步走(◍´꒳`◍)\n\n写标题、写详情、上传图片、选择圈子、点击发布\n\n▼红包领取规则（睁大眼睛看清楚哦）\n✧(≖ ◡ ≖✿ \n\n1.阶梯式现金红包。集齐3个赞1元，15个赞15元，50个赞100元，100个赞1000元。\n\n2.集齐100个赞获1000元大奖的名额是20个，其他奖项无名额限制。\n\n3.一个用户只可领取一次奖励。同一个帖子可补差价。（如先集满了3个赞领取了1元红包，然后又集满了15个赞，应该拿到15元红包。此时客服将补给您14元的差价。）\n\n4.奖金所产生的20%税收由获得者本人承担。",
     * "is_concern": false,
     * "talks": null
     * },
     */


    public HomePostsTagEntity billboard;
    //
    public List<PostDetailEntity> list = new ArrayList<PostDetailEntity>();


}


