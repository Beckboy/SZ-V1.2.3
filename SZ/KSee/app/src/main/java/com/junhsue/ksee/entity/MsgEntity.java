package com.junhsue.ksee.entity;

import com.junhsue.ksee.dto.MsgAnswerFavouriteDTO;

import java.util.List;

/**
 * Created by longer on 17/5/23.
 */

public class MsgEntity extends BaseEntity {

    /**
     *
     * "id": 6,MsgEntity
     "title": "被邀请回答",
     "description": "被邀请回答",
     "content": "被邀请回答\n",
     "poster": "http://pic.baidu.com/AFDAFJIOWEFAF",
     "initial_time": 1488366671,
     "finish_time": 1498907471,
     "status": 1,
     "type_id": 5,
     "type_name": "被邀请回答",
     *
     */

    public String id;
    //
    public String title;
    //
    public String description;
    //
    public String content;
    //
    public String poster;
    //
    public long initial_time;
    //
    public long finish_time;
    //
    public int status;
    //类型id
    public int type_id;
    //类型名称
    public String type_name;

    public Object list=new Object();



    public enum  MsgType {

        QUESTIONNAIRE(1),

        TASK(2),

        QUESITON_REPLY(3),

        ANSWER_FAVOURITE(4),

        QUESTION_INVITE(5),

        QUESTION_FAVOURTE_REPLY(6),

        ACTIVITY_NEW(7),

        ACTIVITY_START(8),

        CLASSROOM_JOIN(9),

        LIVE_NEW(10),

        LIVE_START(11),

        COLLEAGE_TABLE(12),

        COLLEAGE_NEW(13),

        COLLEAGE_START(14),

        SYSTEM_UPDATE(15);


        private int typeId;

        private MsgType(int typeId){
            this.typeId=typeId;
        }

        public int getTypeId() {
            return typeId;
        }
    }




    public interface   IMsgType{


        public final static int  QUESTIONNAIRE=1;//问卷调查

        public final static int  TASK=2;//新手任务

        public final  static int  QUESITON_REPLY=3;//提问的问题被回答

        public final  static  int ANSWER_FAVOURITE=4;//回答被点赞

        public final  static  int QUESTION_INVITE=5;//邀请被回答

        public final  static  int QUESTION_FAVOURTE_REPLY=6;//关注的问题有新的回答

        public final   static  int ACTIVITY_NEW=7;//新的活动通知

        public final  static  int ACTIVITY_START=8;//已报名的活动开始

        public final  static  int CLASSROOM_JOIN=9;//教室邀请

        public final   static  int LIVE_NEW=10;//直播上架

        public final   static  int LIVE_START=11;//已报名直播即将开始通知

        public final   static  int COLLEAGE_TABLE=12;//月初课程表通知

        public final   static  int COLLEAGE_NEW=13;//新发布的课程

        public final   static  int COLLEAGE_START=14;//已报名的课程即将开始
        //
        public final   static  int SYSTEM_UPDATE=15;//系统更新
    }


    /**
     *
     * 卡片点击事件监听*/

    public interface IMsgClickListener{


        void  onClick(int cardType,String id);


        /** 跳转到点赞列表*/
        void jumpFavoritePage(MsgAnswerFavouriteDTO msgAnswerFavouriteDTO);

        /** 跳转到教室*/
        void jumpClassRoom(ClassRoomListEntity classRoomListEntity);

        /** 跳转到直播详情*/
        void jumpLiveDetails(String liveId,String liveTitle);


        /** 跳转到课程详情*/
        void jumpCourseDetails(int type,String courseId);


        /** 忽略消息*/
        void ignoreMsg(int cardType,String msgId,int ignoreType);

        /**  系统升级*/
        void update(String url);
    }



}
