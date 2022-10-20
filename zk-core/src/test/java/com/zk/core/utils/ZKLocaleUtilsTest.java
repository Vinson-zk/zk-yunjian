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
 * @Title: ZKLocaleUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:09:27 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.Locale;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKLocaleUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLocaleUtilsTest {

    @Test
    public void testValueOf() {
        try {
            String localeStr = null;
            // 无法转换，返回默认 Locale.getDefault()
            TestCase.assertEquals(Locale.getDefault(), ZKLocaleUtils.valueOf(localeStr));
            //
            localeStr = "";
            TestCase.assertEquals(Locale.ROOT, ZKLocaleUtils.valueOf(localeStr));
            // en 转换
            localeStr = "en";
            TestCase.assertEquals(Locale.ENGLISH, ZKLocaleUtils.valueOf(localeStr));
            // en-US 转换
            localeStr = "en-US";
            TestCase.assertEquals(Locale.US, ZKLocaleUtils.valueOf(localeStr));
            // en_US 转换
            localeStr = "en_US";
            TestCase.assertEquals(Locale.US, ZKLocaleUtils.valueOf(localeStr));
            // en-US-dfsd 转换
            localeStr = "en-US-dfsd";
            TestCase.assertEquals(Locale.US, ZKLocaleUtils.valueOf(localeStr));
            // en-USdfsd 转换
            localeStr = "en-USdfsd";
            TestCase.assertEquals("en_" + "USdfsd".toUpperCase(), ZKLocaleUtils.valueOf(localeStr).toString());

            System.out.println("LocaleUtilsTest test LocaleUtils.valueOf end ------ ");
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetLocale() {
        try {
            Locale expectLocale = null;

            Locale locale = ZKLocaleUtils.getLocale();
            expectLocale = Locale.getDefault();
            TestCase.assertEquals(expectLocale, locale);

            System.out.println("[^_^:20210220-1028-001] locale.toLanguageTag: " + locale.toLanguageTag());
            System.out.println("[^_^:20210220-1028-001] locale.toString: " + locale.toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
