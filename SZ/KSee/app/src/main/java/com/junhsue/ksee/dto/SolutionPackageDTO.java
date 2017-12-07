package com.junhsue.ksee.dto;

import com.junhsue.ksee.entity.BaseEntity;
import com.junhsue.ksee.entity.Solution;
import com.junhsue.ksee.entity.SolutionGroup;
import com.junhsue.ksee.entity.SolutionPackage;

import java.util.ArrayList;

/**
 * 方案包
 * Created by longer on 17/9/22.
 */

public class SolutionPackageDTO extends BaseEntity {


    public SolutionGroup group=new SolutionGroup();
    //
    public ArrayList<Solution> list=new ArrayList<Solution>();

}