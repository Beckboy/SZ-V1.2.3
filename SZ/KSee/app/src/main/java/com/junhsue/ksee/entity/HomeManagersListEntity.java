package com.junhsue.ksee.entity;

import com.junhsue.ksee.dto.MsgAnswerFavouriteDTO;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hunter_J on 2017/9/8.
 */

public class HomeManagersListEntity extends BaseEntity {

    /**
     * {
     "msg": {
     "list": [
         {
             "id": 4,
             "name": "初次办学",
             "poster": null,
             "description": null,
             "tagx": [
                 {
                 "id": 23,
                 "name": "办学资质",
                 "poster": null,
                 "description": null
                 },
             ],
             "article": [
                 {
                 "id": 65,
                 "title": "优等生都不是事儿，关键是面对差生如何提高续班率！",
                 "description": "不管何种类型的培训学校，注重新学员招生的同时对在校学员的续报率也是尤为重视的，如何让家长或者是学员继续在该校学习进行续报呢?",
                 "poster": "http://omxx7cyms.bkt.clouddn.com/gregerge.jpg"
                 }
             ]
        },
     ]
     }
     }
     */

    /**
     * 标签id
     */
    public int id;

    /**
     * 标签名
     */
    public String name;

    /**
     * 标签海报
     */
    public String poster;

    /**
     * 标签描述
     */
    public String description;

    /**
     * 下级目录标签列表
     */
    public List<TagsEnitity> tagx = new ArrayList<>();

    /**
     * 下级目录文章列表
     */
    public List<ArticlesEnitity> article = new ArrayList<>();



    public class TagsEnitity extends BaseEntity{

        public String id;

        public String name;

        public String poster;

        public String description;

    }


    public class ArticlesEnitity extends BaseEntity{

        public String id;

        public String title;

        public String poster;

        public String description;

    }

    public enum  ManagerType {

        NEW_SCHOOL(4),

        ENGLISH_SCHOOL(6),

        SUPER_TEACHER(18),

        MIDDLE_MANAGER(35),

        PROFESSIONALISM(36);


        private int id;

        private ManagerType(int id){
            this.id=id;
        }

        public int getId() {
            return id;
        }
    }

    public interface   IManagerType{

        public final static int NEW_SCHOOL=4;//初次办学

        public final static int ENGLISH_SCHOOL=6;//英语学校

        public final  static int SUPER_TEACHER=18;//特级老师

        public final  static int MIDDLE_MANAGER=35;//中层管理

        public final  static int PROFESSIONALISM=36;//职业化素养

    }

    /**
     *
     * 标签点击事件监听*/

    public interface IManagersClickListener{


        /** 跳转到文章详情页*/
        void jumpArticlePage(int article_id);

        /** 跳转到标签列表*/
        void jumpTagsListPage(String tag_id, String title);

    }
}
