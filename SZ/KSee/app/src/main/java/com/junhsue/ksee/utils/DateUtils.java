package com.junhsue.ksee.utils;

import android.annotation.SuppressLint;
import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("SimpleDateFormat")
public class DateUtils {

    /**
     * 秒
     */
    private static String seconds = "秒前";
    /**
     * 分
     */
    private static String minutes = "分钟前";
    /**
     * 时
     */
    private static String hours = "小时前";

    /**
     * 天
     */
    private static String days = "天前";
    /**
     * 月
     */
    private static String months = "月";
    /**
     * 刚刚
     */
    private static String before = "刚刚";


    public static String getFormatData(long time, String pattern) {
        try {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat sdf = new SimpleDateFormat(pattern);

            return sdf.format(date);
        } catch (Exception e) {
            return String.valueOf(time);
        }
    }

    public static String getFormatData(long time) {
        try {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            return String.valueOf(time);
        }
    }

    public static String formatDate(long time) {
        try {
            Date date = new Date();
            date.setTime(time);
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            return sdf.format(date);
        } catch (Exception e) {
            return String.valueOf(time);
        }

    }

    /**
     * 计算距离当前时间.
     *
     * @param timestamp  当时时间戳.
     * @param systemTime 服务器时间戳.
     * @return 若距离当前时间小于1分钟则返回xx秒之前，若距离当前时间小于1小时则返回xx分钟之前，若距离当前时间小于1天则返回xx小时前，
     * 若时间为今年则返回MM-dd，否则返回yyyy-MM-dd.
     */
    public static String fromTheCurrentTime(long systemTime, long timestamp) {
        if (timestamp < 1000000000000l) {
            timestamp = timestamp * 1000;
        }
        if (systemTime < 1000000000000l) {
            systemTime = systemTime * 1000;
        }
        //
        long timeDifferenceMillisecond = (timestamp - systemTime) / 1000;

        if (timeDifferenceMillisecond > 0 && timeDifferenceMillisecond < 60) {
            return before;
        } else if (timeDifferenceMillisecond / 60 >= 1 && timeDifferenceMillisecond / 60 <= 60) {
            return String.valueOf(timeDifferenceMillisecond / 60) + minutes;
        } else if (timeDifferenceMillisecond / 3600 >= 1 && timeDifferenceMillisecond / 3600 < 24) {
            return String.valueOf(timeDifferenceMillisecond / 3600) + hours;
        } else if (timeDifferenceMillisecond / 86400 >= 1 && timeDifferenceMillisecond / 86400 < 30) {
            //return timestampToPatternTime(systemTime, "MM-dd HH:mm");
            return String.valueOf(timeDifferenceMillisecond / 86400) + days;
        } else if (timeDifferenceMillisecond / 2592000 >= 1 && timeDifferenceMillisecond / 2592000 < 12) {
            //return timestampToPatternTime(systemTime, "yyyy-MM-dd");
            return String.valueOf(timeDifferenceMillisecond / 2592000) + months;
        } else if (timestampToPatternTime(timestamp, "yyyy").equals(getCurrentTime(systemTime, "yyyy"))) {
            return timestampToPatternTime(systemTime, "yyyy-MM-dd");
        } else {
            return timestampToPatternTime(systemTime, "yyyy-MM-dd");
        }
    }

    public static String formatCurrentTime(long systemTime, long timestamp) {

        if (timestamp < 1000000000000l) {
            timestamp = timestamp * 1000;
        }
        if (systemTime < 1000000000000l) {
            systemTime = systemTime * 1000;
        }
        long timeDifferenceMillisecond = (timestamp - systemTime) / 1000;
        if (timeDifferenceMillisecond >= 0 && timeDifferenceMillisecond < 60) {
            return before;
        } else if (timeDifferenceMillisecond / 60 >= 1 && timeDifferenceMillisecond / 60 <= 60) {
            return String.valueOf(timeDifferenceMillisecond / 60) + minutes;
        } else if (timeDifferenceMillisecond / 3600 >= 1 && timeDifferenceMillisecond / 3600 < 24) {
            return String.valueOf(timeDifferenceMillisecond / 3600) + hours;
        } else if (timeDifferenceMillisecond / 3600 >= 24 && timeDifferenceMillisecond / 3600 < 48) {
            if (!isSameDay(systemTime/1000,timestamp/1000 - 3600*24)){
                return "前天 " + timestampToPatternTime(systemTime, "HH:mm");
            }
            return "昨天 " + timestampToPatternTime(systemTime, "HH:mm");

        } else if (timeDifferenceMillisecond / 86400 >= 2 && timeDifferenceMillisecond / 86400 < 3) {
            if (!isSameDay(systemTime/1000,timestamp/1000 - 3600*24*2)){
                return timestampToPatternTime(systemTime, "MM-dd HH:mm");
            }
            return "前天 " + timestampToPatternTime(systemTime, "HH:mm");
        } else{
            String Now = timestampToPatternTime(timestamp,"yyyy");
            String Sys = timestampToPatternTime(systemTime,"yyyy");
            if (Now.equals(Sys)) {
                return timestampToPatternTime(systemTime,"MM-dd HH:mm");
            }
            return timestampToPatternTime(systemTime, "yy-MM-dd");
        }


    }


    /**
     * 计算距离当前时间.
     *
     * @param systemTime 服务器时间戳.
     * @return 若距离当前时间小于1天则返回HH:mm前，
     * 若时间为今年则返回MM-dd，否则返回yyyy-MM-dd.
     */
    public static String fromTheCurrentTime(long systemTime) {
        long timestamp = System.currentTimeMillis();
        if (timestamp < 1000000000000l) {
            timestamp = timestamp * 1000;
        }
        if (systemTime < 1000000000000l) {
            systemTime = systemTime * 1000;
        }
        //
        if (timestampToPatternTime(timestamp, "yyyy-MM-dd").equals(getCurrentTime(systemTime, "yyyy-MM-dd"))) {
            return timestampToPatternTime(systemTime, "HH:mm");
        } else if (timestampToPatternTime(timestamp - 1000 * 3600 * 24, "yyyy-MM-dd").equals(getCurrentTime(systemTime, "yyyy-MM-dd"))) {
            return timestampToPatternTime(systemTime, "昨天");
        } else if (timestampToPatternTime(timestamp, "yyyy").equals(getCurrentTime(systemTime, "yyyy"))) {
            return timestampToPatternTime(systemTime, "MM月dd日");
        } else {
            return timestampToPatternTime(systemTime, "yyyy年");
        }
    }

    /**
     * 获取当前时间(按指定格式).
     *
     * @param pattern 时间格式.
     * @return 当前时间(按指定格式).
     * @version 1.0
     * @createTime 2014年5月14日, 下午3:54:59
     * @updateTime 2014年5月14日, 下午3:54:59
     * @createAuthor paladin
     * @updateAuthor paladin
     * @updateInfo
     */
    public static String getCurrentTime(long systemTime, String pattern) {
        return timestampToPatternTime(systemTime, pattern);
    }


    /**
     * 将时间戳转换为指定时间(按指定格式).
     *
     * @param timestamp 时间戳.
     * @param pattern   时间格式.
     * @return 指定时间(按指定格式).
     * @version 1.0
     * @createTime 2014年5月14日, 下午3:58:22
     * @updateTime 2014年5月14日, 下午3:58:22
     * @createAuthor paladin
     * @updateAuthor paladin
     * @updateInfo
     */
    public static String timestampToPatternTime(long timestamp, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(new Date(timestamp));
    }


    /**
     * 判断是否为同一天
     *
     * @param time1 时间丑
     */
    public static boolean isSameDay(long time1, long time2) {
        Date date1 = new Date();
        date1.setTime(time1 * 1000);

        Date date2 = new Date();
        date2.setTime(time2 * 1000);

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTime(date1);
        //
        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTime(date2);
        if (calendar1.get(Calendar.YEAR) == calendar2.get(Calendar.YEAR) &&
                calendar1.get(Calendar.MONTH) == calendar2.get(Calendar.MONTH) &&
                calendar1.get(Calendar.DAY_OF_MONTH) == calendar2.get(Calendar.DAY_OF_MONTH)) {
            return true;

        }
        return false;
    }

    /**
     * @param timestamp 时间戳
     * @return
     */
    public static String timeStampToTime(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date(timestamp));
    }

    /**
     * @param timestamp 时间戳 毫秒
     * @return
     */
    public static String timeStampToSeconds(long timestamp) {
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        return sdf.format(new Date(timestamp));
    }


    public static Date parseDate(String time, String pattern) {
        try {
            SimpleDateFormat format = new SimpleDateFormat(pattern);
            Date date = format.parse(time);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Date parseDate(String time) {
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date date = format.parse(time);
            return date;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 32. * @param date1 需要比较的时间 不能为空(null),需要正确的日期格式 33. * @param date2 被比较的时间
     * 为空(null)则为当前时间 34. * @param stype 返回值类型 0为多少天，1为多少个月，2为多少年 35. * @return
     * 36.
     */
    public static int compareDate(String date1, String date2, int stype) {
        int n = 0;
        String[] u = {"天", "月", "年"};
        String formatStyle = stype == 1 ? "yyyy-MM" : "yyyy-MM-dd";

        date2 = date2 == null ? getCurrentDate() : date2;

        DateFormat df = new SimpleDateFormat(formatStyle);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        try {
            c1.setTime(df.parse(date1));
            c2.setTime(df.parse(date2));
        } catch (Exception e3) {
            System.out.println("wrong occured");
        }
        // List list = new ArrayList();
        while (!c1.after(c2)) { // 循环对比，直到相等，n 就是所要的结果
            // list.add(df.format(c1.getTime())); // 这里可以把间隔的日期存到数组中 打印出来
            n++;
            if (stype == 1) {
                c1.add(Calendar.MONTH, 1); // 比较月份，月份+1
            } else {
                c1.add(Calendar.DATE, 1); // 比较天数，日期+1
            }
        }

        n = n - 1;

        if (stype == 2) {
            n = (int) n / 365;
        }

        System.out.println(date1 + " -- " + date2 + " 相差多少" + u[stype] + ":" + n);
        return n;
    }

    /**
     * 得到当前日期
     *
     * @return
     */
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        Date date = c.getTime();
        SimpleDateFormat simple = new SimpleDateFormat("yyyy-MM-dd");
        return simple.format(date);
    }

    /**
     * 返回当前时间的格式为 yyyy-MM-dd HH:mm:ss
     *
     * @return
     */
    public static String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        return sdf.format(System.currentTimeMillis());
    }


    /**
     * 时间戳转换
     *
     * @param currentTime 当前时间
     * @return
     */
    public static long getTimesTamp(long currentTime) {
        return (currentTime / 1000);
    }
}
