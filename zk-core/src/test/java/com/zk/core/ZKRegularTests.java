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
            String patternStr = "^[a-zA-Z][a-zA-Z0-9_]{4,15}$";
            Pattern attern = null;
            Matcher matcher = null;
            String testStr = null;
            boolean testResult = false;
            attern = Pattern.compile(patternStr);
            System.out.println("[^_^:2023122-0100-001] case1: 不匹配，不是字母开头");
            testStr = "1sdfD要";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertFalse(testResult);
            System.out.println("[^_^:2023122-0100-001] case2: 不匹配，长度不够");
            testStr = "v1";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertFalse(testResult);
            System.out.println("[^_^:2023122-0100-001] case3: 不匹配，长度超长");
            testStr = "abcde123456789012";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertFalse(testResult);
            System.out.println("[^_^:2023122-0100-001] case4: 匹配");
            testStr = "vinson123";
            matcher = attern.matcher(testStr);
            testResult = matcher.matches();
            TestCase.assertTrue(testResult);
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
        String patternStr = "^[\u4e00-\u9fa5_a-zA-Z0-9]+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-002] case1: 匹配");
        testStr = "1sdfD要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-002] case2: 不匹配，特殊字符");
        testStr = "1%sdfD要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
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
        String patternStr = "^[\u0391-\uFFE5]+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-003] case1: 不匹配，有字母");
        testStr = "1sdfD要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-003] case2: 匹配");
        testStr = "要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^[1-9]\\d{5}$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-002] case1: 不匹配，有字母");
        testStr = "519000要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-002] case2: 匹配");
        testStr = "519000";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-005] case1: 匹配");
        testStr = "18902861991";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-005] case2: 不匹配");
        testStr = "28902861991";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
    }

    /**
     * 6. 英文字符数字串：^[A-Za-z0-9]+$
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
        String patternStr = "^[A-Za-z0-9]+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-006] case1: 匹配");
        testStr = "1sdfD";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-006] case2: 匹配");
        testStr = "ad";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-006] case3: 匹配");
        testStr = "123";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^[\\w_]+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-007] case1: 不匹配，有中文");
        testStr = "1sdfD要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-007] case2: 匹配");
        testStr = "sdfss";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-008] case1: 不匹配，没 @ 符号");
        testStr = "1sdfD要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-008] case2: 匹配");
        testStr = "binary_space@126.com";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^-?[0-9]\\d*$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-009] case1: 不匹配，字母");
        testStr = "1sdf";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-009] case2: 不匹配，小数");
        testStr = "1.2";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-009] case3: 匹配");
        testStr = "1";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^[0-9]\\d*$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-010] case1: 不匹配，");
        testStr = "1sdfD要";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
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
        String patternStr = "^-[0-9]\\d*$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-011] case1: 不匹配，字母");
        testStr = "13d";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-011] case2: 不匹配，正数");
        testStr = "13";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-011] case3: 匹配");
        testStr = "-13";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^([+-]?)\\d*\\.?\\d+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-012] case1: 不匹配");
        testStr = "3123.232.23";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-013] case2: 匹配");
        testStr = "3123.232";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^\\d*\\.?\\d+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-013] case1: 不匹配，负数");
        testStr = "-1";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-013] case2: 匹配，0");
        testStr = "0";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-013] case3: 匹配");
        testStr = "1.3432";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^-\\d*\\.?\\d+$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-014] case1: 不匹配，正数");
        testStr = "1";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-014] case2: 不匹配，0");
        testStr = "0";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-014] case3: 匹配");
        testStr = "-2.2332";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^http[s]?:\\/\\/([\\w-]+\\.)+[\\w-]+([\\w-./?%&=]*)?$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-015] case1: 匹配");
        testStr = "http://www.hao123.com";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-015] case2: 匹配");
        testStr = "https://www.hao123.com";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)\\.(25[0-5]|2[0-4]\\d|[0-1]\\d{2}|[1-9]?\\d)$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-016] case1: 不匹配");
        testStr = "256.0..1";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-016] case2: 匹配");
        testStr = "156.0.12.1";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^(\\d{15}$|^\\d{18}$|^\\d{17}(\\d|X|x))$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-017] case1: 不匹配，长度不够");
        testStr = "要12323";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-017] case2: 匹配");
        testStr = "431924199103243316";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^(-|\\+)?(180\\.0{0,14}|(\\d{1,2}|1([0-7]\\d))\\.\\d{0,14})$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-018] case1: 不匹配");
        testStr = "180.222";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-018] case2: 匹配");
        testStr = "22.26203426829344";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-018] case3: 匹配");
        testStr = "113.58802367661283";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
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
        String patternStr = "^(-|\\+)?(90\\.0{0,14}|(\\d|[1-8]\\d)\\.\\d{0,14})$";
        Pattern attern = null;
        Matcher matcher = null;
        String testStr = null;
        boolean testResult = false;
        attern = Pattern.compile(patternStr);
        System.out.println("[^_^:2023122-0100-019] case1: 不匹配");
        testStr = "90.222";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertFalse(testResult);
        System.out.println("[^_^:2023122-0100-019] case2: 匹配");
        testStr = "22.26204419741155";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
        System.out.println("[^_^:2023122-0100-019] case3: 匹配");
        testStr = "22.262";
        matcher = attern.matcher(testStr);
        testResult = matcher.matches();
        TestCase.assertTrue(testResult);
    }

}
