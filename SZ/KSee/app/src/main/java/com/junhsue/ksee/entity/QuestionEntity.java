package com.junhsue.ksee.entity;

/**
 * 社板块精选问答实体类
 * Created by Sugar on 17/3/21 in Junhsue.
 */

public class QuestionEntity extends BaseEntity {

    /**
     * * "id": 4,
     * "title": "民办经营问题",
     * "content": null,
     * "zan": 6,
     * "reply": 7,
     * "publish_time": 1489405300,
     * "collect": 4,
     * "nickname": "拾知管理员",
     * "avatar": "http://omn2drzeo.bkt.clouddn.com/Ft-tiBmvf_2AYagXIhDJw1qaQrEF",
     * "name": "招生"
     */

    /**
     * "id": 1,
     * "title": "招生问题我们要怎么解决问题",
     * "content": "",
     * "reply": 0,
     * "publish_time": 1489981317,
     * "collect": 5,
     * "nickname": "拾知小编",
     * "avatar": "http://omn2drzeo.bkt.clouddn.com/Ft-tiBmvf_2AYagXIhDJw1qaQrEF",
     * "organization": "上海雁传书文化传媒有限公司",
     * "is_favorite": false,
     * "fromtopic": "招生"
     */

    public String id = "";

    /*标题*/
    public String title = "";

    /*描述*/
    public String content = "";

    public String zan = "";
    /*回答量*/
    public String reply = "";
    /*发布时间*/
    public long publish_time;
    /*收藏量*/
    public long collect;
    /*发表问题的用户昵称*/
    public String nickname = "";
    /*发表问题的用户头像*/
    public String avatar = "";
    /*用户是否收藏过*/
    public boolean is_favorite;
    /*来自话题*/
    public String fromtopic = "";
    /*发表问题的用户的组织机构*/
    public String organization = "";
    /*业务类型ID*/
    public String business_id = "";
    /*业务类型名*/
    public String business_name = "";
    /*问答的热度*/
    public long is_hot;

    public QuestionType questionType = QuestionType.TYPE_NEWEST;//默认最新

    public enum QuestionType { //问题类型
        TYPE_SELECTED,//精选问题

        TYPE_NEWEST,//最新问题
    }


}
