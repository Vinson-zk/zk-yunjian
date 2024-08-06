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

    public static void main(String[] args) {
        System.out.println("-----");
        System.out.println("--- 0 " + ZKLocaleUtils.getLocale().toLanguageTag());
        t1();
        System.out.println("--- 3 " + ZKLocaleUtils.getLocale().toLanguageTag());
    }

    private static void t1() {
        Runnable r1 = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println("--- 1-1 " + ZKLocaleUtils.getLocale().toLanguageTag());
                Locale.setDefault(ZKLocaleUtils.valueOf("en-US"));
                System.out.println("--- 1-2 " + ZKLocaleUtils.getLocale().toLanguageTag());
            }
        };
        r1.run();

        Runnable r2 = new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                System.out.println("--- 2-1 " + ZKLocaleUtils.getLocale().toLanguageTag());
                Locale.setDefault(ZKLocaleUtils.valueOf("zh-CN"));
                System.out.println("--- 2-2 " + ZKLocaleUtils.getLocale().toLanguageTag());
            }
        };
        r2.run();
        r1.run();
    }

    @Test
    public void testValueOf() {
        try {
            String localeStr = null;
//            // 无法转换，返回默认 Locale.getDefault()
//            TestCase.assertEquals(Locale.getDefault(), ZKLocaleUtils.valueOf(localeStr));
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

            Locale locale = ZKLocaleUtils.getDefautLocale();
            expectLocale = Locale.getDefault();
            TestCase.assertEquals(expectLocale, locale);

            System.out.println("[^_^:20210220-1028-001] locale.toLanguageTag: " + locale.toLanguageTag());
            System.out.println("[^_^:20210220-1028-001] locale.toString: " + locale.toString());
            System.out.println("[^_^:20210220-1028-001] locale.getCountry: " + locale.getCountry());
            System.out.println("[^_^:20210220-1028-001] locale.getDisplayCountry: " + locale.getDisplayCountry());
            System.out.println("[^_^:20210220-1028-001] locale.getDisplayLanguage: " + locale.getDisplayLanguage());
            System.out.println("[^_^:20210220-1028-001] locale.getDisplayName: " + locale.getDisplayName());
            System.out.println("[^_^:20210220-1028-001] locale.getLanguage: " + locale.getLanguage());
            System.out.println("[^_^:20210220-1028-001] locale.getDisplayScript: " + locale.getDisplayScript());
            System.out.println("[^_^:20210220-1028-001] locale.getDisplayVariant: " + locale.getDisplayVariant());
            System.out.println("[^_^:20210220-1028-001] locale.getVariant: " + locale.getVariant());
//            [^_^:20210220-1028-001] locale.toLanguageTag: en-CN
//            [^_^:20210220-1028-001] locale.toString: en_CN
//            [^_^:20210220-1028-001] locale.getCountry: CN
//            [^_^:20210220-1028-001] locale.getDisplayCountry: China
//            [^_^:20210220-1028-001] locale.getDisplayLanguage: English
//            [^_^:20210220-1028-001] locale.getDisplayName: English (China)
//            [^_^:20210220-1028-001] locale.getLanguage: en
//            [^_^:20210220-1028-001] locale.getDisplayScript: 
//            [^_^:20210220-1028-001] locale.getDisplayVariant: 
//            [^_^:20210220-1028-001] locale.getVariant: 

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
