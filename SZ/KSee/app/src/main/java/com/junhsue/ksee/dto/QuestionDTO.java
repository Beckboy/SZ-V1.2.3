package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.QuestionEntity;

import java.util.ArrayList;

/**
 * 最新问答DTO
 * Created by Sugar on 17/3/21 in Junhsue.
 */

public class QuestionDTO extends BaseEntity {
    /**
     * 总共页数
     */
    public int countPage;
    /**
     * 当前页
     */
    public int currentPage;
    /**
     * 当前页的最新问答集合列表
     */
    public ArrayList<QuestionEntity> newests = new ArrayList<>();
}
