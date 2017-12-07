package com.junhsue.ksee.interfaces;

import com.junhsue.ksee.entity.UserDefinedStatisticModel;


/**
 * 自定义的统计接口
 * Created by Sugar on 17/8/8.
 */

public interface IDefinedStatisticsManager {

    /**
     * 统计事件操作
     *
     * @param statisticModel 统计内容的实体对象
     */
    void countActionNum(UserDefinedStatisticModel statisticModel);

    /**
     * 统计页面
     *
     * @param statisticModel 统计内容的实体对象
     */
    void pageViewWithName(UserDefinedStatisticModel statisticModel);


}
