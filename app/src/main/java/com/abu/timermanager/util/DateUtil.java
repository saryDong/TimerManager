package com.abu.timermanager.util;

import java.util.Calendar;

/**
 * 日期工具
 */
public class DateUtil {

    /**
     * 获取当前日期
     * @return  返回日期字符串
     */
    public static String getCurrentTime(){
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH) + 1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    }
}
