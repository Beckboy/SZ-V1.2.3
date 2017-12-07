package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.HomeLiveEntity;
import com.junhsue.ksee.entity.HomeQuestionEntity;
import com.junhsue.ksee.entity.HomeVideoEntity;
import com.junhsue.ksee.entity.LiveEntity;
import com.junhsue.ksee.entity.RealizeArticleEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页内容块
 * Created by longer on 17/8/11.
 */

public class HomeContentDTO {


    //直播预告
    public HomeLiveEntity live;
    //问答
//    public HomeQuestionEntity question;
    //问答列表
    public List<HomeQuestionEntity> question = new ArrayList<>();

    //知文章
    public List<RealizeArticleEntity> article = new ArrayList<RealizeArticleEntity>();
    //录播
    public ArrayList<HomeVideoEntity> video = new ArrayList<HomeVideoEntity>();
    //方案包
    public SolutionPackageDTO solution=new SolutionPackageDTO();

}
