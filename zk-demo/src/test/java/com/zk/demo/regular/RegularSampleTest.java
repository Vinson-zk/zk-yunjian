/** 
* Copyright (c) 2004-2020 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: RegularSampleTest.java 
* @author Vinson 
* @Package com.zk.demo.regular 
* @Description: TODO(simple description this file what to do.) 
* @date Jul 28, 2020 4:29:28 PM 
* @version V1.0 
*/
package com.zk.demo.regular;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: RegularSampleTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class RegularSampleTest {

    /**
     * java 中的一些正则表达式
     */
    @Test
    public void test() {
        // 只匹配以 Java. 开头的字符
        String regularStr = "^Java\\..*";
        boolean result = false;
        Pattern pattern;
        Matcher matcher;

        result = "Java.".matches(regularStr);
        TestCase.assertEquals(true, result);
        result = "Java.dd".matches(regularStr);
        TestCase.assertEquals(true, result);
        result = "JAva.dd".matches(regularStr);
        TestCase.assertEquals(false, result);
        result = "sJava.dd".matches(regularStr);
        TestCase.assertEquals(false, result);
        result = "Javas.dd".matches(regularStr);
        TestCase.assertEquals(false, result);
        result = "Javadd".matches(regularStr);
        TestCase.assertEquals(false, result);

        // 以 abc 开头或 _abc 开头
        pattern = Pattern.compile("^((abc)|(_abc)).*");
        matcher = pattern.matcher("abc_a25342");
        TestCase.assertEquals(true, matcher.matches());
        matcher = pattern.matcher("_abc_a25342");
        TestCase.assertEquals(true, matcher.matches());
        matcher = pattern.matcher("abd_a25342");
        TestCase.assertEquals(false, matcher.matches());
        matcher = pattern.matcher("_abd_a25342");
        TestCase.assertEquals(false, matcher.matches());
        matcher = pattern.matcher("_abd25342");
        TestCase.assertEquals(false, matcher.matches());

        // 匹配以下划线开头，并接着第一个字符是小写字母的项。 "_[]"
        pattern = Pattern.compile("[^@]*[_]{1}[a-z]{1}[^@]*");
        matcher = pattern.matcher("_a25342");
        TestCase.assertEquals(true, matcher.matches());
        matcher = pattern.matcher("_A25342");
        TestCase.assertEquals(false, matcher.matches());
        matcher = pattern.matcher("asdsa_A25342");
        TestCase.assertEquals(false, matcher.matches());
        matcher = pattern.matcher("asdsa_s25342");
        TestCase.assertEquals(true, matcher.matches());
        matcher = pattern.matcher("asdsa_safafasd_fadfafda");
        TestCase.assertEquals(true, matcher.matches());
        matcher = pattern.matcher("asdsa_safafasd_fadfafda");
        TestCase.assertEquals(true, matcher.find());

        pattern = Pattern.compile("[_]{1}[a-z]{1}");
        matcher = pattern.matcher("asdsa_safafasd_fadfafda");
        TestCase.assertEquals(true, matcher.find());
        TestCase.assertEquals("_s", matcher.group(0));
        TestCase.assertEquals(true, matcher.find());
        TestCase.assertEquals("asdsaAAAafafasd_fadfafda", matcher.replaceFirst("AAA"));
        TestCase.assertEquals(true, matcher.find());
        TestCase.assertEquals("_f", matcher.group(0));

        regularStr = "[^@]*[_]{1}[a-z]{1}[^@]*";
        result = "Java.".matches(regularStr);
        TestCase.assertEquals(false, result);
        result = "Java_asdf".matches(regularStr);
        TestCase.assertEquals(true, result);
        result = "_asdf".matches(regularStr);
        TestCase.assertEquals(true, result);
        result = "dasdfaf_a".matches(regularStr);
        TestCase.assertEquals(true, result);
        result = "Java_Asdf".matches(regularStr);
        TestCase.assertEquals(false, result);
        result = "Java_".matches(regularStr);
        TestCase.assertEquals(false, result);

//      "^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$";
        pattern = Pattern.compile("^\\d[a-z]*$");
        matcher = pattern.matcher("1savaasdf");
        TestCase.assertEquals(true, matcher.matches());
        matcher = pattern.matcher("savaasdf");
        TestCase.assertEquals(false, matcher.matches());

        // 以多条件分割
        pattern = Pattern.compile("[, ;]");
        String[] strs = pattern.split("Java Hello World  Java;Hello,,World|Sun");
        TestCase.assertEquals(8, strs.length);

        pattern = Pattern.compile("正则表达式");
        matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World");
        TestCase.assertEquals("Java Hello World,正则表达式 Hello World", matcher.replaceFirst("Java"));

        pattern = Pattern.compile("正则表达式");
        matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World");
        TestCase.assertEquals("Java Hello World,Java Hello World", matcher.replaceAll("Java"));

        pattern = Pattern.compile("^正则表达式");
        matcher = pattern.matcher("正则表达式 Hello World,正则表达式 Hello World");
        TestCase.assertEquals("Java Hello World,正则表达式 Hello World", matcher.replaceAll("Java"));

        pattern = Pattern.compile("^正则表达式");
        matcher = pattern.matcher("a正则表达式 Hello World,正则表达式 Hello World");
        TestCase.assertEquals("a正则表达式 Hello World,正则表达式 Hello World", matcher.replaceAll("Java"));

        pattern = Pattern.compile("^正则表达式$");
        matcher = pattern.matcher("a正则表达式 Hello World,正则表达式 Hello World");
        TestCase.assertEquals("a正则表达式 Hello World,正则表达式 Hello World", matcher.replaceAll("Java"));

        pattern = Pattern.compile("^正则表达式$");
        matcher = pattern.matcher("正则表达式");
        TestCase.assertEquals("Java", matcher.replaceAll("Java"));

        pattern = Pattern.compile("正则表达式$");
        matcher = pattern.matcher("正则表达式 Hello World,正则表达式");
        TestCase.assertEquals("正则表达式 Hello World,Java", matcher.replaceAll("Java"));
    }

    @Test
    public void testIp() {
        boolean result = false;
        result = RegularSample.checkIp("255.255.255.255");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkIp("0.0.0.0");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkIp("256.255.255.255");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkIp("-1.0.0.0");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkIp("255.254..255");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkIp("1.0.00.0");
        TestCase.assertEquals(true, result);
    }

    @Test
    public void tesCheckEmail() {
        boolean result = false;

        result = RegularSample.checkEmail("xzhk@126.com");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk.@123456.com");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkEmail("xzhk@123456.com");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk@.126.com");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkEmail("xzhk1@126.cn");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk1@126.com.cn");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk1_@126.com.cn");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk1_@126ss.com.cn");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzh-k1_@126ss.com.cn");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk1_@126s-s.com.cn");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkEmail("xzhk1_@126s_s.com.cn");
        TestCase.assertEquals(false, result);
    }

    @Test
    public void testCheckPhoneNumber() {
        boolean result = false;

        result = RegularSample.checkPhoneNumber("12345678901");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkPhoneNumber("13825689082");
        TestCase.assertEquals(true, result);
        result = RegularSample.checkPhoneNumber("23825689082");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkPhoneNumber("1382568908");
        TestCase.assertEquals(false, result);
        result = RegularSample.checkPhoneNumber("138256890822");
        TestCase.assertEquals(false, result);
    }

    @Test
    public void test2() {

        String regularStr = "^[\\S,^\\[]{1,}[\\[]{1}[\\S,^\\]]{1,}[\\]]{1}$";
        boolean result = false;

        result = "Java.".matches(regularStr);
        TestCase.assertEquals(false, result);

        result = "Java[".matches(regularStr);
        TestCase.assertEquals(false, result);

        result = "Java]".matches(regularStr);
        TestCase.assertEquals(false, result);

        result = "Java[]".matches(regularStr);
        TestCase.assertEquals(false, result);

        result = "Java[a]".matches(regularStr);
        TestCase.assertEquals(true, result);

    }

    @Test
    public void testRegular() {
        String rStr = "";
        String regStr = "";

        Pattern attern = null;
        Matcher matcher = null;

        regStr = "^[\\S,^\\[]{1,}[\\[]{1}[\\S,^\\]]{1,}[\\]]{1}$";
//      regStr = "^\\s*\\w+(?:\\.{0,1}[\\w-]+)*@[a-zA-Z0-9]+(?:[-.][a-zA-Z0-9]+)*\\.[a-zA-Z]+\\s*$";
//      regStr = "^\\S{0,}0$";
        // CASE_INSENSITIVE 不区分大小写
        attern = Pattern.compile(regStr, Pattern.CASE_INSENSITIVE);

        rStr = "asfda@126.com0";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(false, matcher.matches());

        rStr = "asdfasdfasf";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(false, matcher.matches());

        rStr = "asdfasdfasf[asd";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(false, matcher.matches());

        rStr = "asdfasdfasf]";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(false, matcher.matches());

        rStr = "[asdfa]";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(false, matcher.matches());

        rStr = "asdfasf[]";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(false, matcher.matches());

        rStr = "asdfasf[s]";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(true, matcher.matches());

        rStr = "asdfasf[sadf]";
        matcher = null;
        matcher = attern.matcher(rStr);
        TestCase.assertEquals(true, matcher.matches());
    }

}
