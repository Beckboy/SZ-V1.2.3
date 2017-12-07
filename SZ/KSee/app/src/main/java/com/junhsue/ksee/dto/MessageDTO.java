package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.MsgEntity;

import java.util.ArrayList;

/**
 * Created by longer on 17/5/27.
 */

public class MessageDTO extends BaseEntity {

    public ArrayList<MsgEntity> result = new ArrayList<MsgEntity>();

}
