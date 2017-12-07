package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.InviteEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * 邀请人员注册集合
 * Created by Sugar on 17/9/22.
 */

public class InviteDTO extends BaseEntity {
    public List<InviteEntity> list = new ArrayList<>();
}
