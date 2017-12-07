package com.junhsue.ksee.dto;

import com.junhsue.ksee.BaseActivity;
import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.ProfessorEntity;
import com.junhsue.ksee.entity.UserInfo;

import java.util.ArrayList;

/**
 * 被邀请的专家集合
 * Created by Sugar on 17/4/11.
 */

public class ProfessorDTO extends BaseEntity {

    /**
     * 专家集合
     */
   public ArrayList<ProfessorEntity> result = new ArrayList<>();

}
