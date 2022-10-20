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
 * @Title: ZKValidatorsBeanUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 5:01:48 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.List;

import javax.validation.ConstraintViolationException;
import javax.validation.Validator;

import org.junit.Test;

import com.zk.core.helper.ZKCoreTestHelper;
import com.zk.core.helper.ZKValidatorEntity;

import junit.framework.TestCase;

/** 
* @ClassName: ZKValidatorsBeanUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKValidatorsBeanUtilsTest {

    static Validator validator;

    static {

        TestCase.assertNotNull(ZKCoreTestHelper.getCtxUtils());
        ZKEnvironmentUtils.initContext(ZKCoreTestHelper.getCtxUtils());

        validator = ZKCoreTestHelper.getCtxUtils().getBean(Validator.class);
        TestCase.assertNotNull(validator);
    }

    @Test
    public void test() {
        try {

            ConstraintViolationException cve = null;
            ZKValidatorEntity ve = new ZKValidatorEntity();

            // 系统整个系统的语言，以便能验证消息国际化
            ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("zh_CN"));
//            ZKLocaleUtils.setSystemLocale(ZKLocaleUtils.valueOf("en_US"));

            // 测试实体数据验证抛异常
            try {
                ve.setpNotNull("pNotNull");
                ve.setpNotNullLocaleMsg("pNotNullLocaleMsg");
                ve.setpLength("123");
                ve.setpMax(5);
                ve.setpMin(5);
                ve.setpRange(5);
                ZKValidatorsBeanUtils.validateWithException(validator, ve);
                TestCase.assertTrue(true);
            }
            catch(ConstraintViolationException e) {
                cve = e;
            }
            TestCase.assertNull(cve);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGeneric() {
        try {

            ConstraintViolationException cve = null;
            ZKValidatorEntity ve = new ZKValidatorEntity();
            List<String> list;

            // 系统整个系统的语言，以便能验证消息国际化
            ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("zh_CN"));
//            ZKLocaleUtils.setSystemLocale(ZKLocaleUtils.valueOf("en_US"));

            // 测试实体数据验证抛异常
            try {
                ve.setpMax(9);
                ve.setpLength("1");
                ZKValidatorsBeanUtils.validateWithException(validator, ve);
                TestCase.assertTrue(false);
            }
            catch(ConstraintViolationException e) {
                cve = e;
            }
            TestCase.assertNotNull(cve);
            TestCase.assertEquals(6, cve.getConstraintViolations().size());

            // 测试数据难后异常消息处理
            list = ZKValidatorsBeanUtils.extractPropertyAndMessageAsList(cve, ":");
            for (String s : list) {
                System.out.println("[^_^:20190614-1657-001]: " + s);
                if (s.startsWith("pMin:")) {
                    TestCase.assertEquals("pMin:输入数字必须大于：2；", s);
                }
                if (s.startsWith("pRange:")) {
                    TestCase.assertEquals("pRange:输入数字必须大于：3, 小于：7；", s);
                }
                if (s.startsWith("pLength:")) {
                    TestCase.assertEquals("pLength:输入长度必须在: [2, 6] 之间；", s);
                }
                if (s.startsWith("pNotNull:")) {
                    TestCase.assertEquals("pNotNull:javax.validation.constraints.NotNull.message", s);
                }
                if (s.startsWith("pMax:")) {
                    TestCase.assertEquals("pMax:输入数字必须小于：6；", s);
                }
                if (s.startsWith("pNotNullLocaleMsg:")) {
                    TestCase.assertEquals("pNotNullLocaleMsg:测试消息，不能为空；", s);
                }
            }

            // 返回英文 消息
            ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("en_US"));

            // 测试实体数据验证抛异常
            try {
                ve.setpMax(9);
                ve.setpLength("1");
                ZKValidatorsBeanUtils.validateWithException(validator, ve);
                TestCase.assertTrue(false);
            }
            catch(ConstraintViolationException e) {
                cve = e;
            }
            TestCase.assertNotNull(cve);
            TestCase.assertEquals(6, cve.getConstraintViolations().size());

            // 测试数据难后异常消息处理
            list = ZKValidatorsBeanUtils.extractPropertyAndMessageAsList(cve, ":");
            for (String s : list) {
                System.out.println("[^_^:20190614-1657-002]: " + s);
                if (s.startsWith("pMin:")) {
                    TestCase.assertEquals("pMin:The input number must be greater than: 2;", s);
                }
                if (s.startsWith("pRange:")) {
                    TestCase.assertEquals("pRange:The input number must be greater than: 3, less than: 7;", s);
                }
                if (s.startsWith("pLength:")) {
                    TestCase.assertEquals("pLength:The input length must be between: [2, 6];", s);
                }
                if (s.startsWith("pNotNull:")) {
                    TestCase.assertEquals("pNotNull:javax.validation.constraints.NotNull.message", s);
                }
                if (s.startsWith("pMax:")) {
                    TestCase.assertEquals("pMax:The input number must be less than: 6;", s);
                }
                if (s.startsWith("pNotNullLocaleMsg:")) {
                    TestCase.assertEquals("pNotNullLocaleMsg:test msg not null;", s);
                }
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
