/** 
* Copyright (c) 2012-2023 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: RegularTests.java 
* @author Vinson 
* @Package com.zk.core 
* @Description: TODO(simple description this file what to do. ) 
* @date Dec 21, 2023 10:36:28 PM 
* @version V1.0 
*/
package com.zk.core;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKRegularTests 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKRegularTests {
    
    public static class RC {
        public String str;
        public boolean result;
        public String desc;

        public RC(String str, boolean result, String desc) {
            this.str = str;
            this.result = result;
            this.desc = desc;
        }

        public static RC as(String str, boolean result, String desc) {
            return new RC(str, result, desc);
        }
    }

    @Test
    public void test() {
        try {
            String patternStr = "^[a-zA-Z0-9_.]*$";
            Pattern attern = null;
            Matcher matcher = null;
            String testStr = null;
            boolean testResult = false;
            attern = Pattern.compile(patternStr);
            System.out.println("[^_^:2023122-0100-001] case1: 匹配，内容是: 大小写字母、数字、下划线、点");
            testStr = "1sdfD._...adfASDF123";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
            System.out.println("[^_^:2023122-0100-001] case2: 匹配，内容是: 数字");
            testStr = "0123124331";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
            System.out.println("[^_^:2023122-0100-001] case4: 匹配，内容是: 小写字母");
            testStr = "abcdefghijklmnopqrstuvwxyz";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
            System.out.println("[^_^:2023122-0100-001] case5: 匹配，内容是: 大写字母");
            testStr = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
            System.out.println("[^_^:2023122-0100-001] case6: 匹配，内容是: 下划线");
            testStr = "_____";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
            System.out.println("[^_^:2023122-0100-001] case7: 匹配，内容是: 点");
            testStr = "....";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
            System.out.println("[^_^:2023122-0100-001] case8: 不匹配，长度超长");
            testStr = "a-01_.2";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertFalse(testResult);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 1. 帐号(字母开头，允许5-16字节，允许字母数字下划线)：^[a-zA-Z][a-zA-Z0-9_]{4,15}$
     *
     * @Title: testMatcherAccount
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 3:12:02 PM
     * @return void
     */
    @Test
    public void testMatcherAccount() {
        try {

            String patternStr = null;
            Pattern attern = null;
            Matcher matcher = null;
            RC[] rcs;

            patternStr = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
            attern = Pattern.compile(patternStr);
            rcs = new RC[] { //
                    RC.as("1sdfD要", false, "不匹配，不是字母开头"), //
                    RC.as("v1要", false, "不匹配，长度不够"), //
                    RC.as("abcde123456789012", false, "不匹配，长度超长"), //
                    RC.as("vinson123", true, "匹配"), //
            };
            for (int i = 0; i < rcs.length; ++i) {
                System.out.println(String.format("[^_^:20240716-0023-001] case[%02d] testStr: %s; resultDesc: %s", i,
                        rcs[i].str, rcs[i].desc));
                matcher = attern.matcher(rcs[i].str);
                TestCase.assertEquals(rcs[i].result, matcher.matches());
            }

        }catch (Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * 2. 中文、英文、数字及下划线：^[\u4e00-\u9fa5_a-zA-Z0-9]+$
     *
     * @Title: testMatcherChar
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 3:12:09 PM
     * @return void
     */
    @Test
    public void testMatcherChar() {

        String patternStr = null;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^[\u4e00-\u9fa5_a-zA-Z0-9]+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1sdfD要", true, "匹配"), //
                RC.as("1%sdfD要", false, "不匹配，特殊字符"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-002] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 3. 中文：^[\u0391-\uFFE5]+$ （这里使用了+表示至少一个，即是非空且只能中文，可空时+可改为*）
     *
     * @Title: testMatcherChinese
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 3:12:14 PM
     * @return void
     */
    @Test
    public void testMatcherChinese() {
        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^[\u0391-\uFFE5]+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1sdfD要", false, "不匹配，有字母"), //
                RC.as("要", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-003] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 4. 邮政编码：^[1-9]\d{5}$
     *
     * @Title: testMatcherPostcode
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 3:12:18 PM
     * @return void
     */
    @Test
    public void testMatcherPostcode() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^[1-9]\\d{5}$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("519000要", false, "不匹配，有字母"), //
                RC.as("519000", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-004] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 5. 手机号码：^((13[0-9])|(15[^4,\D])|(18[0,5-9]))\d{8}$
     * 
     * @Title: testMatcherPhoneNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:54:03 PM
     * @return void
     */
    @Test
    public void testMatcherPhoneNum() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;
        
        // 简化通用：
        patternStr = "^\\+?[1-9]\\d{1,14}$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("18902861991", true, "匹配"), //
                RC.as("28902861991", true, "匹配"), //
                RC.as("8612312341234", true, "匹配"), //
                RC.as("+8622312341234", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-005.1] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }

        // 中国手机号：
        patternStr = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("18902861991", true, "匹配"), //
                RC.as("28902861991", false, "不匹配，不是以1开头"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-005.2] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }

        // 中国手机号：1开头 加 一个非0/1/2的数字 加 8位数字
        patternStr = "^1[3-9]\\d{9}$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("10312341234", false, "不匹配，10开头"), //
                RC.as("11312341234", false, "不匹配，11开头"), //
                RC.as("12312341234", false, "不匹配，12开头"), //
                RC.as("1391234123", false, "不匹配，长度不够"), //
                RC.as("139123412345", false, "不匹配，超长"), //
                RC.as("13912341234", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-005.3] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }

        // 国际通用手机号：+开头 加 1-4位国际区号 加 中杠 加 7-11位手机号
        patternStr = "^\\+[1-9]\\d{1,3}-\\d{7,11}$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("+886-12312341234", true, "匹配"), //
                RC.as("+86-12312341234", true, "匹配"), //
                RC.as("+886-123123412345", false, "不匹配-手机号超长"), //
                RC.as("+88694-123123412345", false, "不匹配-国家区号超长"), //
                RC.as("+8612312341234", false, "不匹配-没有中杠"), //
                RC.as("86-12312341234", false, "不匹配-没有加号"), //
                RC.as("12312341234", false, "不匹配-没有国际区号"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-005.4] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }

        // 国际通用手机号或中国手机号：+开头 加 1-4位国际区号 加 中杠 加 7-11位手机号，1开头 加 一个非0/1/2的数字 加 8位数字
        patternStr = "(^\\+[1-9]\\d{1,3}-\\d{7,11}$)|(^1[3-9]\\d{9}$)";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("10312341234", false, "不匹配，10开头"), //
                RC.as("11312341234", false, "不匹配，11开头"), //
                RC.as("12312341234", false, "不匹配，12开头"), //
                RC.as("1391234123", false, "不匹配，长度不够"), //
                RC.as("139123412345", false, "不匹配，超长"), //
                RC.as("13912341234", true, "匹配"), //
                RC.as("+886-12312341234", true, "匹配"), //
                RC.as("+86-12312341234", true, "匹配"), //
                RC.as("+886-123123412345", false, "不匹配-手机号超长"), //
                RC.as("+88694-123123412345", false, "不匹配-国家区号超长"), //
                RC.as("+8612312341234", false, "不匹配-没有中杠"), //
                RC.as("86-12312341234", false, "不匹配-没有加号"), //
                RC.as("12312341234", false, "不匹配-没有国际区号"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-005.5] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 6. 英文,数字，字符数字串：^[A-Za-z0-9]+$
     * 
     * 这个比较简单明了，我使用的也是这个，\w也是等效的，我还没试过。
     *
     * @Title: testMatcherEnglishNum
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:52:55 PM
     * @return void
     */
    @Test
    public void testMatcherEnglishNum() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^[A-Za-z0-9]+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1sdfD", true, "匹配"), //
                RC.as("ad", true, "匹配"), //
                RC.as("123", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-006] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 7. 英文数字加下划线串：^[\w_]+$
     *
     * @Title: testMatcherEnglish
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:49:19 PM
     * @return void
     */
    @Test
    public void testMatcherEnglish() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^[\\w_]+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1sdfD要", false, "不匹配，有中文"), //
                RC.as("sdfss", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-007] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 8. 邮箱字段验证：^\s*\w+(?:\.{0,1}[\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\.[a-zA-Z]+\s*$
     * 
     * 可以直接使用 @Email(message = "邮箱格式有误")
     *
     * @Title: testMatcherMail
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:48:38 PM
     * @return void
     */
    @Test
    public void testMatcherMail() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        // /^(([^<>()\[\]\\.,;:\s@"]+(\.[^<>()\[\]\\.,;:\s@"]+)*)|(".+"))@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}])|(([a-zA-Z\-0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+\.)+[a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]{2,}))$/
        // "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+\\.)+[a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]{2,}))$";
        patternStr = "^(([^<>()\\[\\]\\\\.,;:\\s@\"]+(\\.[^<>()\\[\\]\\\\.,;:\\s@\"]+)*)|(\".+\"))@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}])|(([a-zA-Z\\-0-9\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+\\.)+[a-zA-Z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]{2,}))$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1sdfD要", false, "不匹配，没 @ 符号"), //
                RC.as("binary_space@126.com", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-008] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 9. 验证整数：^-?[0-9]\d*$
     *
     * @Title: testMatcherInteger
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:46:59 PM
     * @return void
     */
    @Test
    public void testMatcherInteger() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^-?[0-9]\\d*$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1sdf", false, "不匹配，有字母"), //
                RC.as("1.2", false, "不匹配，小数"), //
                RC.as("1", true, "匹配"), //
                RC.as("-23", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-009] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 10. 正整数：^[0-9]\d*$
     *
     * @Title: testMatcherPositiveInteger
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:43:40 PM
     * @return void
     */
    @Test
    public void testMatcherPositiveInteger() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^[0-9]\\d*$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("18902861991", true, "匹配"), //
                RC.as("1sdfD要", false, "不匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-010] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 11. 负整数：^-[0-9]\d*$
     *
     * @Title: testMatcherNegativeInteger
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:41:50 PM
     * @return void
     */
    @Test
    public void testMatcherNegativeInteger() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^-[0-9]\\d*$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("13d", false, "不匹配，字母"), //
                RC.as("13", false, "不匹配，正数"), //
                RC.as("-13", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-011] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 12. 数字：^([+-]?)\d*\.?\d+$
     *
     * @Title: testMatcherNumber
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:40:04 PM
     * @return void
     */
    @Test
    public void testMatcherNumber() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^([+-]?)\\d*\\.?\\d+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("3123.232.23", false, "不匹配"), //
                RC.as("3123.232", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-012] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 13. 正数：^\d*\.?\d+$
     *
     * @Title: testMatcherPositiveNo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:37:44 PM
     * @return void
     */
    @Test
    public void testMatcherPositiveNo() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^\\d*\\.?\\d+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("-1", false, "不匹配，负数"), //
                RC.as("0", true, "匹配"), //
                RC.as("1.3432", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-013] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 14. 负数：^-\d*\.?\d+$
     *
     * @Title: testMatcherNegativeNo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:36:37 PM
     * @return void
     */
    @Test
    public void testMatcherNegativeNo() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^-\\d*\\.?\\d+$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("1", false, "不匹配，正数"), //
                RC.as("0", false, "不匹配，0"), //
                RC.as("-2.2332", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-014] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 15. URL：^http[s]?:\/\/([\w-]+\.)+[\w-]+([\w-./?%&=]*)?$
     *
     * @Title: testMatcherUrl
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:34:48 PM
     * @return void
     */
    @Test
    public void testMatcherUrl() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("http://www.hao123.com", true, "匹配"), //
                RC.as("https://www.hao123.com", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-015] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 16. IP地址：^(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)\.(25[0-5]|2[0-4]\d|[0-1]\d{2}|[1-9]?\d)$
     * 
     * IP地址这个如果用字符串明文存的话，可以试下这个正则，我见有地方建议将IP转为long型数字存储的，不过我还没试过。
     *
     * @Title: testMatcherIp
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:30:47 PM
     * @return void
     */
    @Test
    public void testMatcherIp() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("256.0..1", false, "不匹配"), //
                RC.as("156.0.12.1", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-016] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 17. 身份证号：^(\d{15}$|^\d{18}$|^\d{17}(\d|X|x))$
     * 
     * 这个只是初步验证，关于身份证号，一般都有专门的工具类，这个可以用在注解里面进行初步验证。
     *
     * @Title: testMatcherIdentificationCardNo
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:30:29 PM
     * @return void
     */
    @Test
    public void testMatcherIdentificationCardNo() {

        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("要12323", false, "不匹配，长度不够"), //
                RC.as("431924199103243316", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-017] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 18. GPS经度：^(-|\+)?(180\.0{0,14}|(\d{1,2}|1([0-7]\d))\.\d{0,14})$
     *
     * @Title: testMatcherGpsLongitude
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:30:23 PM
     * @return void
     */
    @Test
    public void testMatcherGpsLongitude() {
        // 113.58802367661283
        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^(-|\\+)?(180\\.0{0,14}|(\\d{1,2}|1([0-7]\\d))\\.\\d{0,14})$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("180.222", false, "不匹配"), //
                RC.as("22.26203426829344", true, "匹配"), //
                RC.as("113.58802367661283", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-018] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

    /**
     * 19. GPS纬度：^(-|\+)?(90\.0{0,14}|(\d|[1-8]\d)\.\d{0,14})$
     *
     * @Title: testMatcherGpslatitude
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 22, 2023 2:30:08 PM
     * @return void
     */
    @Test
    public void testMatcherGpslatitude() {
        // 22.26204419741155
        String patternStr;
        Pattern attern = null;
        Matcher matcher = null;
        RC[] rcs;

        patternStr = "^(-|\\+)?(90\\.0{0,14}|(\\d|[1-8]\\d)\\.\\d{0,14})$";
        attern = Pattern.compile(patternStr);
        rcs = new RC[] { //
                RC.as("90.222", false, "不匹配"), //
                RC.as("22.26204419741155", true, "匹配"), //
                RC.as("22.262", true, "匹配"), //
        };
        for (int i = 0; i < rcs.length; ++i) {
            System.out.println(String.format("[^_^:20240716-0023-019] case[%02d] testStr: %s; resultDesc: %s", i,
                    rcs[i].str, rcs[i].desc));
            matcher = attern.matcher(rcs[i].str);
            TestCase.assertEquals(rcs[i].result, matcher.matches());
        }
    }

}
