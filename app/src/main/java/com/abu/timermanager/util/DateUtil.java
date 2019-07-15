package com.abu.timermanager.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 日期工具
 */
public class DateUtil {

    /**
     * 获取当前日期
     *
     * @return 返回日期字符串
     */
    public static String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        return format.format(date);
    }

    /**
     * 将date转换为指定格式的字符串
     *
     * @param date 日期
     * @return string类型日期
     */
    public static String date2String(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String s = format.format(date);
        return s;
    }

    /**
     * string类型的日期转换为Date类型
     *
     * @param date 日期
     * @return string日期
     */
    public static Date string2Date(String date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date parse;
        try {
            parse = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            parse = new Date(System.currentTimeMillis());
            LogUtil.e("将string解析成date出错，被解析date已替换成当前时间");
        }
        return parse;
    }

    /**
     * 计算倒计时时间还有多少毫秒
     *
     * @param remindTime 提醒时间
     * @return 倒计时时间
     */
    public static long computationTime(String remindTime) {
        Date currentDate = DateUtil.string2Date(DateUtil.getCurrentTime());
        Date remindDate = DateUtil.string2Date(remindTime);
        long diff = remindDate.getTime() - currentDate.getTime();
        return diff;
    }

    /**
     * 获取下一个小时
     *
     * @param time 当前时间
     * @return 下一个小时时间
     */
    public static String getNextHour(String time) {
        Date utilDate = null;
        try {
            utilDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar cl = Calendar.getInstance();
        cl.setTime(utilDate);
        int hourOfDay = cl.get(Calendar.HOUR_OF_DAY);
        cl.set(Calendar.HOUR_OF_DAY, hourOfDay + 1);
        String nextHour = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(cl.getTime());
        return nextHour;
    }
}
