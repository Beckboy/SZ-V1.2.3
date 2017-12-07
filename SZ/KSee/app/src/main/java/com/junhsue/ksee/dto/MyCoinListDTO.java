package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MyCoinListEntity;

import java.util.ArrayList;

/**
 * 我的兑换券
 * Created by hunter_J on 2017/9/23.
 */

public class MyCoinListDTO extends BaseEntity {

    public ArrayList<MyCoinListEntity> result = new ArrayList<MyCoinListEntity>();

}
