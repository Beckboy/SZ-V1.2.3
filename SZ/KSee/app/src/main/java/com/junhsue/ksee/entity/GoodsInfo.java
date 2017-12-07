package com.junhsue.ksee.entity;

/**
 * 构造商品信息类
 * <p>
 * 每一个商品包含
 * 图片，商品名，单价，商品类型
 * <p>
 * Created by longer on 17/4/6.
 */

public class GoodsInfo extends BaseEntity {


    //商品id
    public String id;
    //图片
    public String imgUrl;
    //商品名称
    public String goodsName;
    //商品单价
    public double price;
    //商品类型
    public GoodsType goodsType;


    public static GoodsInfo cloneOject(String imgUrl, String goodsId, String goodsName, double price, GoodsType goodsType) {
        GoodsInfo goodsInfo = new GoodsInfo();
        goodsInfo.id = goodsId;
        goodsInfo.imgUrl = imgUrl;
        goodsInfo.goodsName = goodsName;
        goodsInfo.price = price;
        goodsInfo.goodsType = goodsType;
        return goodsInfo;
    }


    /**
     * 商品类型类
     */
    public enum GoodsType {

        QUESTION(1, "问题"),
        ANSWER(2, "答案"),
        VOTE_Activity(3, "投票活动"),
        ON_LINE_ACTIVITY(4, "线上活动"),
        off_line_Activity(5, "线下活动"),
        classroom_ACTIVITY(6, "教室"),
        LIVE(7, "直播"),
        SYSTEM_COURSE(8, "系统课"),
        SUBJECT_COURSE(9, "主题课");

        private int value;
        private String typeName;//类型名称

        private GoodsType(int value, String typeName) {
            this.value = value;
            this.typeName = typeName;
        }


        /**
         * 获取类型名称
         */
        public static String getTypeName(int value) {
            for (GoodsType goodsType : GoodsType.values()) {
                if (goodsType.value == value) {
                    return goodsType.typeName;
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
        public static GoodsType getType(int value) {
            for (GoodsType goodsType : GoodsType.values()) {
                if (goodsType.value == value) {
                    return goodsType;
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
