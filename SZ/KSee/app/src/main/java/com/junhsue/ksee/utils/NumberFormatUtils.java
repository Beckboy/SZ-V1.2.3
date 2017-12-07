package com.junhsue.ksee.utils;

import java.text.DecimalFormat;

/**
 * 数字格式化输出
 * Created by longer on 17/4/6.
 */

public class NumberFormatUtils {

    /**
     * 保留两位小数
     */
    public static String formatPointTwo(double value) {
        DecimalFormat fnum = new DecimalFormat("##0.00");
        return fnum.format(value);
    }

    /**
     * 保留一位小数
     */
    public static String formatPointOne(double value) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        return fnum.format(value);
    }


    /**
     * 取整
     */
    public static String formatPoint(double value) {
        if(value<1){
            return formatPointOne(value);
        }
        DecimalFormat fnum = new DecimalFormat("##0");

        return fnum.format(value);
    }
}
