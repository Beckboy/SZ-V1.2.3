package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MsgAnswerFavouriteEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 问题回答被点赞
 * Created by longer on 17/6/8.
 */


public class MsgAnswerFavouriteDTO extends BaseEntity {


    public List<MsgAnswerFavouriteEntity> data = new ArrayList<MsgAnswerFavouriteEntity>();


}
