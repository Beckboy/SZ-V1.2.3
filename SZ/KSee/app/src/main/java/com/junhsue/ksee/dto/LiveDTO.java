package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.LiveEntity;

import java.util.ArrayList;

/**
 * 直播
 * Created by longer on 17/4/19.
 */

public class LiveDTO extends BaseEntity {


    //当前页直播
    public int  currentpage;

    public ArrayList<LiveEntity> list;

}
