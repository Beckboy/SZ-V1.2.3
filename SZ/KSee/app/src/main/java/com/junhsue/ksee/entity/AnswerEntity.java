package com.junhsue.ksee.entity;


/**
 * 回答详情实体
 * Created by Sugar on 17/3/28.
 */

public class AnswerEntity extends BaseEntity {

    /**
     * "id": 8,
     * "content_type_id": 2,
     * "publish_time": 1490221648,
     * "count": 7,
     * "nickname": "拾知小编",
     * "avatar": "http://omn2drzeo.bkt.clouddn.com/Ft-tiBmvf_2AYagXIhDJw1qaQrEF",
     * "content": "www.baidu.com",
     * "approval_count": 1,
     * "is_approval": 1
     */

    public String id = "";
    //回复内容类型 1：文本；2：语音
    public int content_type_id;
    //发布时间
    public long publish_time;

    public int count;
    //用户名
    public String nickname = "";
    //头像
    public String avatar ="";
    //组织机构
    public String organization = "";
    //是否点赞
    public boolean is_approval;
    //回答内容
    public String content;
    //点赞量
    public long approval_count;
    //类型ID
    public String business_id = "";
    //类型名
    public String business_name = "";
    //回答内容对应的问题标题
    public String questionTitleOfAnswer = "";
    //语音下载存放路径
    public String playFile = "";
    //语音时长
    public int duration;
    //是否播放完毕
    public boolean play_status;//播放状态

    public static int VOICE_REPLY_TYPE_VALUE = 2;

    public static int TXT_REPLY_TYPE_VALUE = 1;

    /**
     * 商品类型类
     */
    public enum AnswerType {

        TEXT(1, "文字"),
        VOICE(2, "语音");

        private int value;
        private String typeName;//类型名称

        private AnswerType(int value, String typeName) {
            this.value = value;
            this.typeName = typeName;
        }


        /**
         * 获取类型名称
         */
        public static String getTypeName(int value) {
            for (AnswerEntity.AnswerType answerType : AnswerEntity.AnswerType.values()) {
                if (answerType.value == value) {
                    return answerType.typeName;
                }
            }
            return "";
        }


        /**
         * 获取类型
         *
         * @param value
         * @return
         */
        public static AnswerEntity.AnswerType getType(int value) {
            for (AnswerEntity.AnswerType answerType : AnswerEntity.AnswerType.values()) {
                if (answerType.value == value) {
                    return answerType;
                }
            }
            return null;

        }

        public int getValue() {
            return value;
        }

        public String getTypeName() {
            return typeName;
        }
    }


}
