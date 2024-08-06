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
 * @Title: ZKStringUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:05:03 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.Map;

import org.junit.Test;

import com.google.common.collect.Maps;

import junit.framework.TestCase;

/**
 * @ClassName: ZKStringUtilsTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKStringUtilsTest {

    @Test // 左补位
    public void testLeftFill() {
        long length = 5;
        char fillChar = 't';
        String source;

        source = null;
        TestCase.assertTrue("ttttt".equals(ZKStringUtils.leftFill(source, length, fillChar)));
        source = "";
        TestCase.assertTrue("ttttt".equals(ZKStringUtils.leftFill(source, length, fillChar)));
        source = "12";
        TestCase.assertTrue("ttt12".equals(ZKStringUtils.leftFill(source, length, fillChar)));
        source = "12";
        TestCase.assertFalse("12ttt".equals(ZKStringUtils.leftFill(source, length, fillChar)));
        source = "12345";
        TestCase.assertTrue("12345".equals(ZKStringUtils.leftFill(source, length, fillChar)));
    }

    @Test // 右补位
    public void testRightFill() {
        long length = 5;
        char fillChar = 't';
        String source;

        source = null;
        TestCase.assertTrue("ttttt".equals(ZKStringUtils.rightFill(source, length, fillChar)));
        source = "";
        TestCase.assertTrue("ttttt".equals(ZKStringUtils.rightFill(source, length, fillChar)));
        source = "12";
        TestCase.assertFalse("ttt12".equals(ZKStringUtils.rightFill(source, length, fillChar)));
        source = "12";
        TestCase.assertTrue("12ttt".equals(ZKStringUtils.rightFill(source, length, fillChar)));
        source = "12345";
        TestCase.assertTrue("12345".equals(ZKStringUtils.rightFill(source, length, fillChar)));
    }

    @Test // str 字符串是否包含在 strs 字符串数组中；
    public void testInString() {

        String[] strs = new String[] { "a", "bb" };
        String str = "";

        str = ""; // 不在
        TestCase.assertFalse(ZKStringUtils.inString(str, strs));

        str = "aa"; // 不在
        TestCase.assertFalse(ZKStringUtils.inString(str, strs));

        str = "b"; // 不在
        TestCase.assertFalse(ZKStringUtils.inString(str, strs));

        str = "a"; // 在
        TestCase.assertTrue(ZKStringUtils.inString(str, strs));

        str = "bb"; // 在
        TestCase.assertTrue(ZKStringUtils.inString(str, strs));

    }

    @Test // 验证密码字符串
    public void testCheckPassword() {
        try {

            int result = -1;
            char[] pwcs = null;
            int minLength = 6;
            int maxLength = 20;
            char[] passwordSourceChar = ZKStringUtils.passwordSourceChar;
            passwordSourceChar = "qwertyuiopasdfghjklzxcvbnmQWERTYUIOPASDFGHJKLZXCVBNM1234567890`-=[]\\;',./~!@#$%^&*()_+{}|:\"<>?"
                    .toCharArray();

            pwcs = null;
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(4, result);

            pwcs = "".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(4, result);

            pwcs = "123456".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "12345".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(1, result);

            pwcs = "12345678901234567890".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "123456789012345678901".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(2, result);

            pwcs = "12345 6".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(3, result);

            pwcs = "qwertyuiopasdfghjk".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "lzxcvbnmQWERTYUIOP".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "ASDFGHJKLZXC".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "VBNM123456".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "7890`-=[]\\;',.".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "/~!@#$%^&*()_+{}".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "|:\"<>?".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(0, result);

            pwcs = "12345；".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(3, result);

            pwcs = "12345；ww".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(3, result);

            pwcs = "从从".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(1, result);

            pwcs = "从从从从从从从从".toCharArray();
            result = ZKStringUtils.checkPassword(pwcs, passwordSourceChar, minLength, maxLength);
            TestCase.assertEquals(3, result);

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testIsNumber() {
        String str = null;

        str = "1232";
        TestCase.assertEquals(true, ZKStringUtils.isNumber(str));
        str = "-1232";
        TestCase.assertEquals(true, ZKStringUtils.isNumber(str));
        str = "+1232";
        TestCase.assertEquals(true, ZKStringUtils.isNumber(str));
        str = "1232.";
        TestCase.assertEquals(false, ZKStringUtils.isNumber(str));
        str = "-1232.";
        TestCase.assertEquals(false, ZKStringUtils.isNumber(str));
        str = "+1232.";
        TestCase.assertEquals(false, ZKStringUtils.isNumber(str));
        str = "1232.231";
        TestCase.assertEquals(true, ZKStringUtils.isNumber(str));
        str = "-1232.123";
        TestCase.assertEquals(true, ZKStringUtils.isNumber(str));
        str = "+1232.123";
        TestCase.assertEquals(true, ZKStringUtils.isNumber(str));

        str = "asd";
        TestCase.assertEquals(false, ZKStringUtils.isNumber(str));
        str = "1+232.2";
        TestCase.assertEquals(false, ZKStringUtils.isNumber(str));
        str = "1-232.2";
        TestCase.assertEquals(false, ZKStringUtils.isNumber(str));

    }

    @Test
    public void testJsonStrToStructStr() {
        String source = "[{\"origin\" : { \"id\" : 16 , \"name\" : \"Yantian\" , \"code\" : \"CNYTN\"} , \"dest\" : { \"id\" : \"P441\" , \"name\" : \"Seattle\" , \"code\" : \"USSEA\"} , \"ppKey\" : \"CNYTN,USSEA\",test:[{\"origin\" : \"Yantian\",\"dest\":\"Shanghai\"},{\"origin\" : \"Yantian1\",\"dest\":\"Shanghai1\"},{\"origin\" : \"Yantian2\",\"dest\":\"Shanghai2\"}]}]";
//      source = "\"sourcesourcesource\"";
//      String target = "array<struct<origin:struct<id:string,name:string,code:string>,dest:struct<id:string,name:string,code:string>,ppKey:string,test:array<struct<origin:string,dest:string>>>>";
        String target = "array<struct<ppKey:string,test:array<struct<origin:string,dest:string>>,origin:struct<code:string,name:string,id:string>,dest:struct<code:string,name:string,id:string>>>";
        try {
            System.out.println("[^_^ 201705-1854-001]" + target);
            System.out.println("[^_^ 201705-1854-002]" + ZKStringUtils.jsonStrToStructStr(source));
            TestCase.assertEquals(target, ZKStringUtils.jsonStrToStructStr(source));

            TestCase.assertTrue(true);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    @Test
    public void testReplaceByRegex() {
        try {
            String regex = "";
            String[] params = null;
            String expectStr = "郭偃之法曰：论至德者不和于俗，成大功者不谋于众。法者，所以爱民也；礼者，所以便事也。是以圣人苟可以强国，不法其故；苟可以利民，不循其礼。";
            String str = "${regex.begin-0_*}论至德者不和于俗，成大功者不谋于众。${3}是以圣人苟可以强国，${1}不循其礼。";
            String resultStr = "";
            String expectStrTemp = "";

            resultStr = "";
            params = new String[] { "郭偃之法曰：", "法者，所以爱民也；礼者，所以便事也。", "不法其故；苟可以利民，" };
            regex = "\\$\\{(\\S+?)\\}"; // 非贪婪模式匹配
            resultStr = ZKStringUtils.replaceByRegex(str, regex, params);
            System.out.println("[^_^:20190901-1233-001] resultStr: " + resultStr);
            TestCase.assertEquals(expectStr, resultStr);

            resultStr = "";
            params = new String[] { "郭偃之法曰：", "法者，所以爱民也；礼者，所以便事也。" };
            regex = "\\$\\{(\\S+?)\\}"; // 非贪婪模式匹配
            resultStr = ZKStringUtils.replaceByRegex(str, regex, params);
            System.out.println("[^_^:20190901-1233-002] resultStr: " + resultStr);
            expectStrTemp = "郭偃之法曰：论至德者不和于俗，成大功者不谋于众。法者，所以爱民也；礼者，所以便事也。是以圣人苟可以强国，${1}不循其礼。";
            TestCase.assertEquals(expectStrTemp, resultStr);

            str = "${regex.begin-0_*}论至德者不和于俗，成大功者不谋于众。${3}是以圣人苟可以强国，${3}不循其礼。";
            resultStr = "";
            params = new String[] { "郭偃之法曰：", "法者，所以爱民也；礼者，所以便事也。", "不法其故；苟可以利民，" };
            regex = "\\$\\{(\\S+?)\\}"; // 非贪婪模式匹配
            resultStr = ZKStringUtils.replaceByRegex(str, regex, params);
            System.out.println("[^_^:20190901-1233-002] resultStr: " + resultStr);
            TestCase.assertEquals(expectStr, resultStr);

            str = "${regex.begin-0_*}论至德者不和于俗，成大功者不谋于众。${3}是以圣人苟可以强国，${3}";
            resultStr = "";
            params = new String[] { "郭偃之法曰：", "法者，所以爱民也；礼者，所以便事也。", "不法其故；苟可以利民，" };
            regex = "\\$\\{(\\S+?)\\}"; // 非贪婪模式匹配
            resultStr = ZKStringUtils.replaceByRegex(str, regex, params);
            System.out.println("[^_^:20190901-1233-002] resultStr: " + resultStr);
            expectStrTemp = "郭偃之法曰：论至德者不和于俗，成大功者不谋于众。法者，所以爱民也；礼者，所以便事也。是以圣人苟可以强国，不法其故；苟可以利民，";
            TestCase.assertEquals(expectStrTemp, resultStr);

            str = expectStr;
            resultStr = "";
            params = new String[] { "郭偃之法曰：", "法者，所以爱民也；礼者，所以便事也。", "不法其故；苟可以利民，" };
            regex = "\\$\\{(\\S+?)\\}"; // 非贪婪模式匹配
            resultStr = ZKStringUtils.replaceByRegex(str, regex, params);
            System.out.println("[^_^:20190901-1233-002] resultStr: " + resultStr);
            TestCase.assertEquals(expectStr, resultStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testReplaceByPoint() {
        try {
            String sourceStr;
            String expectStr;
            String resultStr = "";

            sourceStr = "{1}a{}a{0}a{3}a{1}";

            expectStr = "$a{}a$a{3}a$";
            resultStr = ZKStringUtils.replaceByPoint(sourceStr, "$", "$", "$");
            System.out.println("[^_^:20190901-1327-001] resultStr: " + resultStr);
            TestCase.assertEquals(expectStr, resultStr);
            expectStr = "$a{}a$a$a$";
            resultStr = ZKStringUtils.replaceByPoint(sourceStr, "$", "$", "$", "$");
            System.out.println("[^_^:20190901-1327-002] resultStr: " + resultStr);
            TestCase.assertEquals(expectStr, resultStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testReplaceByName() {
        try {
            String sourceStr;
            String expectStr;
            String resultStr = "";
            Map<String, Object> params = Maps.newHashMap();

            params.put("string", "this is string!$");
            params.put("int", 5);

            sourceStr = "a${int}s${string}{string}d${str${string}ing}fghjkl;l$qwe}~!@#%^()_+}{\":?><";

            expectStr = "a5sthis is string!${string}d${strthis is string!$ing}fghjkl;l$qwe}~!@#%^()_+}{\":?><";
            resultStr = ZKStringUtils.replaceByName(sourceStr, params);
            System.out.println("[^_^:20200805-1021-001] resultStr: " + resultStr);
            TestCase.assertEquals(expectStr, resultStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testToString() {
        try {
            Object obj = null;

            obj = "";
            System.out.println("[^_^:20200805-1005-001] obj-\"\": " + ZKStringUtils.toString(obj));
            TestCase.assertEquals("", ZKStringUtils.toString(obj));

            obj = 1;
            System.out.println("[^_^:20200805-1005-002] obj-1: " + ZKStringUtils.toString(obj));
            TestCase.assertEquals("1", ZKStringUtils.toString(obj));
            
            obj = Maps.newHashMap();
            System.out.println("[^_^:20200805-1005-003] obj-Map: " + ZKStringUtils.toString(obj));
            TestCase.assertEquals("{}", ZKStringUtils.toString(obj));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
