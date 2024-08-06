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
 * @Title: ZKDateUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 5:00:03 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.text.ParseException;
import java.util.Date;
import java.util.TimeZone;

import org.apache.commons.lang3.time.DateUtils;
import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDateUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDateUtilsTest {

    /**
     * 日期转化为时间字符串
     * 
     * @param date
     *            源日期
     * @param pattern
     *            格式字符串
     * @return 返回转换后的字符串
     * @throws Exception
     */
    @Test
    public void testDateToStr() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            String dateStr = ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS);
            System.out.println("[^_^: 201707261914-001] dateStr=" + dateStr);
            TestCase.assertEquals("2016-03-02 11:26:28.000316", dateStr);

            date = ZKDateUtils.parseDate("2020-09-18 13:15");
            dateStr = ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss, "GMT+0");
            System.out.println("[^_^: 20200918-1317-001] dateStr=" + dateStr);
            TestCase.assertEquals("2020-09-18 05:15:00", dateStr);

            date = ZKDateUtils.parseDate("2020-09-18 13:15");
            dateStr = ZKDateUtils.formatDate(date, "yyyy-MM-dd HH:mm:ssZZ", "GMT+8");
            System.out.println("[^_^: 20210219-0939-001] dateStr=" + dateStr);
            TestCase.assertEquals("2020-09-18 13:15:00+08:00", dateStr);

            System.out.println("-----------------------------------");
            date = ZKDateUtils.parseDate("2020-09-18 13:15");
            dateStr = ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ);
            System.out.println("[^_^: 20210219-0939-002] dateStr=" + dateStr);
            TestCase.assertEquals("2020-09-18T13:15:00+08:00", dateStr);
            dateStr = ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ, "GMT+8");
            System.out.println("[^_^: 20210219-0939-002] dateStr=" + dateStr);
            TestCase.assertEquals("2020-09-18T13:15:00+08:00", dateStr);
            dateStr = ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ,
                    TimeZone.getDefault().getID());
            System.out.println("[^_^: 20210219-0939-002] dateStr=" + dateStr);
            TestCase.assertEquals("2020-09-18T13:15:00+08:00", dateStr);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 字符串转换为日期
     * 
     * @param str
     *            源日期字符串
     * @param pattern
     *            格式字符串
     * @return 返回转换后的日期
     * @throws ParseException
     */
    @Test
    public void testStrToDate() {
        try {
            String dateStr = "2016-03-02 11:26:28.000316";
            Date date = DateUtils.parseDate(dateStr, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS);
            TestCase.assertEquals(new Date(1456889188316l), date);

            dateStr = "2021-02-22T17:35:40+08:00";
            date = DateUtils.parseDate(dateStr, "yyyy-MM-dd'T'HH:mm:ssZZ");
            System.out.println(date.getTime());
            System.out.println(ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            TestCase.assertEquals(dateStr, ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_ddTHH_mm_ssZZ));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * @return 取今天的日期
     */
    @Test
    public void testGetToday() {
        try {
            Date date = ZKDateUtils.getToday();
            TestCase.assertEquals(ZKDateUtils.formatDate(new Date(), ZKDateUtils.DF_yyyy_MM_dd),
                    ZKDateUtils.formatDate(date, ZKDateUtils.DF_yyyy_MM_dd));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本周中的星期几
     * 
     * @param date
     * @param weekDay
     * @return
     * @throws Exception
     */
    @Test
    public void testGetDayOfWeek() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316

            Date resultDate = ZKDateUtils.getDayOfWeek(date, 0);
            TestCase.assertEquals("2016-02-29", ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd));

            resultDate = ZKDateUtils.getDayOfWeek(date, 1);
            TestCase.assertEquals("2016-03-01", ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd));

            resultDate = ZKDateUtils.getDayOfWeek(date, 3);
            TestCase.assertEquals("2016-03-03", ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd));

            resultDate = ZKDateUtils.getDayOfWeek(date, 6);
            TestCase.assertEquals("2016-03-06", ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd));

            resultDate = ZKDateUtils.getDayOfWeek(date, 7);
            TestCase.assertEquals("2016-02-29", ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今天是星期几
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetWeekDay() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            TestCase.assertEquals(3, ZKDateUtils.getWeekDay(date));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本周开始的第一天
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetFirstDateOfWeek() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.getFirstDateOfWeek(date);
            TestCase.assertEquals("2016-02-29 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本周的最后一天
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetLastDateOfWeek() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.getLastDateOfWeek(date);
            TestCase.assertEquals("2016-03-06 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本月开始的第一天
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetFirstDateOfMonth() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.getFirstDateOfMonth(date);
            TestCase.assertEquals("2016-03-01 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取本月开始的最后一天
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetLastDateOfMonth() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.getLastDateOfMonth(date);
            TestCase.assertEquals("2016-03-31 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今年开始的第一天
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetFirstDateOfYear() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.getFirstDateOfYear(date);
            System.out.println(ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
            TestCase.assertEquals("2016-01-01 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取今年的最后一天
     * 
     * @param date
     * @return
     * @throws Exception
     */
    @Test
    public void testGetLastDateOfYear() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.getLastDateOfYear(date);
            System.out.println(ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
            TestCase.assertEquals("2016-12-31 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 在当前日期上加上一段时间
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
     * @throws Exception
     */
    @Test
    public void testAddDate() {
        try {
            Date date = new Date(1456889188316l); // 2016-03-02 11:26:28.000316
            Date resultDate = ZKDateUtils.addDate(date, 0, 0, -2, 0, 0, 0);
            System.out.println(ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
            TestCase.assertEquals("2016-02-29 11:26:28.000316",
                    ZKDateUtils.formatDate(resultDate, ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss_SSSSSS));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
