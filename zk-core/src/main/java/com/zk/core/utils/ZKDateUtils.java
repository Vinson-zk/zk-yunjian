/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKDateUtils.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:40:02 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Pattern;

import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.commons.lang3.time.DateUtils;

/**
 * @ClassName: ZKDateUtils
 * @Description: 本类功能:
 * 
 *               1、日期转为字符串
 * 
 *               2、字符串车为日期
 * 
 *               3、星期 操作
 * 
 * @author Vinson
 * @version 1.0
 */
public class ZKDateUtils extends DateUtils {

    private static String[] parsePatterns = { "yyyy-MM-dd", "yyyyMMdd", "yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd HH:mm",
            "yyyy-MM", "yyyy/MM/dd", "yyyy/MM/dd HH:mm:ss", "yyyy/MM/dd HH:mm", "yyyy/MM", "yyyy.MM.dd",
            "yyyy.MM.dd HH:mm:ss", "yyyy.MM.dd HH:mm", "yyyy.MM" };

    /**
     * 年-月-天 小时:分钟:秒.6位毫秒
     */
    public static final String DF_yyyy_MM_dd_HH_mm_ss_SSSSSS = "yyyy-MM-dd HH:mm:ss.SSSSSS";

//    /**
//     * 年-月-天 小时:分钟:秒+08:00
//     */
//    public static final String DF_yyyy_MM_dd_HH_mm_ssZZ = "yyyy-MM-dd HH:mm:ssZZ";

    /**
     * 年-月-天 小时:分钟:秒+08:00
     */
    public static final String DF_yyyy_MM_ddTHH_mm_ssZZ = "yyyy-MM-dd'T'HH:mm:ssZZ";

    /**
     * 年-月-天 小时:分钟:秒
     */
    public static final String DF_yyyy_MM_dd_HH_mm_ss = "yyyy-MM-dd HH:mm:ss";

    /**
     * 年-月-天 小时:分钟
     */
    public static final String DF_yyyy_MM_dd_HH_mm = "yyyy-MM-dd HH:mm";

    /**
     * 年-月-天
     */
    public static final String DF_yyyy_MM_dd = "yyyy-MM-dd";

    /**
     * 年-月
     */
    public static final String DF_yyyy_MM = "yyyy-MM";

    /**
     * 年
     */
    public static final String DF_yyyy = "yyyy";

    /**
     * 月
     */
    public static final String DF_MM = "MM";

    /**
     * 天
     */
    public static final String DF_dd = "dd";

    /**
     * 小时:分钟
     */
    public static final String DF_HH_mm = "HH:mm";

    /**
     * 小时
     */
    public static final String DF_HH = "HH";

    /**
     * 分钟
     */
    public static final String DF_mm = "mm";

    public static String formatDate(String pattern) {
        return DateFormatUtils.format(getToday(), pattern);
    }

    /**
     * 日期转化为时间字符串
     * 
     * @param date
     *            源日期
     * @param pattern
     *            格式字符串
     * @return 返回转换后的字符串
     */
    public static String formatDate(Date date, String pattern) {
        return DateFormatUtils.format(date, pattern);
    }

    public static String formatDate(Date date, String pattern, String timeZone) {
        return formatDate(date, pattern, TimeZone.getTimeZone(timeZone));
    }

    public static String formatDate(Date date, String pattern, TimeZone timeZone) {
        return DateFormatUtils.format(date, pattern, timeZone);
    }

    /**
     * 日期转化为时间字符串, 默认格式字符串：yyyy-MM-dd
     * 
     * @param date
     *            源日期
     * @return 返回转换后的字符串
     */
    public static String formatDate(Date date) {
        return DateFormatUtils.format(date, DF_yyyy_MM_dd);
    }

    public static Date parseDate(long time) {
        return new Date(time);
    }

    /**
     * 字符串转换为日期, 默认格式字符串：yyyy-MM-dd
     * 
     * @param str
     *            源日期字符串
     * @return 返回转换后的日期
     * @throws ParseException
     */
    public static Date parseDate(String str) throws ParseException {
        return parseDate(str, parsePatterns);
    }

    /**
     * @return 取今天的日期
     */
    public static Date getToday() {
        return new Date(System.currentTimeMillis());
    }

    /**
     * 取指定时间的后一天
     *
     * @Title: getNextDay
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 25, 2019 4:07:50 PM
     * @param date
     *            指定时间，默认为当天；
     * @return
     * @return Date
     */
    public static Date getNextDay(Date date) {
        date = date == null ? getToday() : date;
        return addDate(date, 0, 0, 1, 0, 0, 0);
    }

    /**
     * 获取 日期Date 在当周中的星期几
     * 
     * @param date
     * @param weekDay
     *            指定星期几 星期一:1；星期二:2 。。。。星期六:6；星期天:0；依次循环
     * @return
     */
    public static Date getDayOfWeek(Date date, int weekDay) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + weekDay);
        return c.getTime();
    }

    /**
     * 获取 日期Date 是当周中的星期几
     * 
     * @param date
     * @return
     */
    public static int getWeekDay(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        for (int i = 1; i < 8; ++i) {
            if (c.get(Calendar.DAY_OF_WEEK) == i) {
                return (i + 6) - (i + 6) / 8 * 7;
            }
        }
        return -1;
    }

    /**
     * 获取 日期Date 在当周中的第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDateOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek());
        return c.getTime();
    }

    /**
     * 获取 日期Date 当周中的最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDateOfWeek(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
        return c.getTime();

    }

    /**
     * 获取 日期Date 当月开始的第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取 日期Date 当月的最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDateOfMonth(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 获取 日期Date 当年开始的第一天
     * 
     * @param date
     * @return
     */
    public static Date getFirstDateOfYear(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, 0);
        c.set(Calendar.DAY_OF_MONTH, 1);
        return c.getTime();
    }

    /**
     * 获取 日期Date 当年的最后一天
     * 
     * @param date
     * @return
     */
    public static Date getLastDateOfYear(Date date) throws Exception {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        c.set(Calendar.MONTH, 11);
        c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
        return c.getTime();
    }

    /**
     * 在 日期Date 上加上一段时间
     * 
     * @param date
     *            源时间
     * @param years
     *            年数，负数表示几年前
     * @param months
     *            月数，负数表示几月前
     * @param days
     *            天数，负数表示几天前
     * @param hours
     *            小时，负数表示几小时间前
     * @param minutes
     *            分钟，负数表示几分钟前
     * @param seconds
     *            秒数，负数表示几秒钟前
     * @return
     */
    public static Date addDate(Date date, int years, int months, int days, int hours, int minutes, int seconds) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);

        c.add(Calendar.YEAR, years);
        c.add(Calendar.MONTH, months);
        c.add(Calendar.DATE, days);
        c.add(Calendar.HOUR, hours);
        c.add(Calendar.MINUTE, minutes);
        c.add(Calendar.SECOND, seconds);

        return c.getTime();
    }

    /**
     * @param strSource
     *            带着格式的字符串 例 "2016-11-11 08[yyyy-MM-dd HH]"，或为时间的long
     *            型数据字符，默认格式为：yyyy-MM-dd
     * @return
     */
    public static Date getDateByStr(String strSource) {
        if (strSource == null || "".equals(strSource)) {
            return getToday();
        }
        else {
            if (isNumber(strSource)) {
                return new Date(Long.valueOf(strSource));
            }
            else {
                Pattern pattern = Pattern.compile("[\\[\\]]");
                String[] strs = pattern.split(strSource);
                try {
                    if (strs.length == 2 && strs[1].length() > 0) {
                        return parseDate(strs[0], strs[1]);
                    }
                    else {
                        return parseDate(strs[0], DF_yyyy_MM_dd);
                    }
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    /**
     * 获取过去的秒种
     * 
     * @param date
     * @return
     */
    public static long pastSecond(Date date) {
        long t = new Date().getTime() - date.getTime();
        return t / 1000;
    }

    private static final Pattern is_number_pattern = Pattern.compile("((-?)|(\\+?))[0-9]+.?[0-9]+"); // 判断是不是数字的正则表达
                                                                                                     // 式

    /**
     * 判断是不是数字
     * 
     * @param str
     * @return
     */
    private static boolean isNumber(String str) {
        return is_number_pattern.matcher(str).matches();
    }

}
