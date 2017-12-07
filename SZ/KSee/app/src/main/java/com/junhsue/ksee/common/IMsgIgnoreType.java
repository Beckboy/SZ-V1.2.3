package com.junhsue.ksee.common;

/**
 * 卡片忽略类型
 * Created by longer on 17/8/9.
 */

public interface IMsgIgnoreType {

    /**
     * 忽略卡片类型分为
     * <p>
     * 系统卡片和普通消息卡片
     * <p>
     * 系统卡片为 2
     * <p>
     * 消息卡片为 0
     */
    public static final int TYPE_SYSTEM = 2;
    //
    public static final int TYPE_NORMAL = 0;

}
