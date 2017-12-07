package com.junhsue.ksee.adapter;

import java.util.List;

/**
 * Created by longer on 17/4/19.
 */

public interface IAdapterOperate<T> {


    /**
     * 修改adapter样式
     */
    void modifyData(List<T> list);

    /**
     * 获取List
     */
    List<T> getList();

    /**
     * 清除List
     */
    void clearList();

}
