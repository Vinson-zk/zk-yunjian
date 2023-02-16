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
 * @Title: ZKEnvironmentUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:08:24 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import org.junit.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.helper.configuration.env.ZKCoreEnvChildConfiguration;
import com.zk.core.helper.configuration.env.ZKCoreEnvParentConfiguration;
import com.zk.core.helper.configuration.env.ZKCoreEnvSourceConfiguration;

import junit.framework.TestCase;

/**
 * @ClassName: ZKEnvironmentUtilsTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
//@PropertySources(value = { //
//        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { //
//                "classpath:env/env.test.source.properties", //
//        }), //
//        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = {
//                "classpath:env/env.test.parent.properties", //
//                "classpath:env/env.test.child.properties", //
//        })
//})
//@ConfigurationPropertiesScan
public class ZKEnvironmentUtilsTest {

    public static ConfigurableApplicationContext run(String[] args) {
        SpringApplicationBuilder springApplicationBuilder = new SpringApplicationBuilder(ZKEnvironmentUtilsTest.class);

        springApplicationBuilder = springApplicationBuilder.sources(ZKCoreEnvSourceConfiguration.class);
        springApplicationBuilder = springApplicationBuilder.parent(ZKCoreEnvParentConfiguration.class);
        springApplicationBuilder = springApplicationBuilder.child(ZKCoreEnvChildConfiguration.class);

        SpringApplication springApplication = springApplicationBuilder.build();
        springApplication.setWebApplicationType(WebApplicationType.NONE);
        ConfigurableApplicationContext ctx = springApplication.run(args);

        return ctx;
    }

    static {
        try {

            ConfigurableApplicationContext ctx = run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(ctx));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }

    }

    /*** 测试配置相互引用 ***/
    @Test
    public void testReference() {
        try {
            String key = "";
            String str = "";

            TestCase.assertNotNull(ZKEnvironmentUtils.getEnvironment());

            key = "env.test.p";
            str = ZKEnvironmentUtils.getString(key);
            TestCase.assertEquals("这是个引用配置属性是 [parent 配置【parent】; child 配置【child】;]", str);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /*** 测试取 字符串 配置属性 ***/
    @Test
    public void testGetString() {
        try {
            String key = "";
            String val = "";
            String expectValue = "";

            /*** 属性 存在 ***/
            expectValue = "字符";
            key = "env.test.str";

            val = ZKEnvironmentUtils.getString(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, "");
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, "xxx");
            TestCase.assertEquals(expectValue, val);

            /*** 属性 空 ***/
            expectValue = "";
            key = "env.test.str.empty";

            val = ZKEnvironmentUtils.getString(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, "");
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, "xxx");
            TestCase.assertEquals(expectValue, val);

            /*** 属性 不存在 ***/
            expectValue = null;
            key = "env.test.str.not.exist";

            val = ZKEnvironmentUtils.getString(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getString(key, "");
            TestCase.assertEquals("", val);
            val = ZKEnvironmentUtils.getString(key, "xxx");
            TestCase.assertEquals("xxx", val);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /*** 测试取 Integer 配置属性 ***/
    @Test
    public void testGetIngeter() {
        try {
            String key = "";
            Integer val = null;
            Integer expectValue = null;

            /*** 属性 存在 ***/
            expectValue = 1;
            key = "env.test.int";

            val = ZKEnvironmentUtils.getInteger(key);
            TestCase.assertEquals(expectValue.intValue(), val.intValue());
            val = ZKEnvironmentUtils.getInteger(key, null);
            TestCase.assertEquals(expectValue.intValue(), val.intValue());
            val = ZKEnvironmentUtils.getInteger(key, 0);
            TestCase.assertEquals(expectValue.intValue(), val.intValue());

            /*** 属性 不存在 ***/
            expectValue = null;
            key = "env.test.int.not.exist";

            val = ZKEnvironmentUtils.getInteger(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, 3);
            TestCase.assertEquals(3, val.intValue());

            /*** 属性 空 ***/
            expectValue = null;
            key = "env.test.int.empty";

            val = ZKEnvironmentUtils.getInteger(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, 3);
            TestCase.assertEquals(3, val.intValue());

            /*** 属性 非数字 ***/

            expectValue = null;
            key = "env.test.int.str";

            val = ZKEnvironmentUtils.getInteger(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, 3);
            TestCase.assertEquals(3, val.intValue());

            /*** 属性 符点型 ***/
            expectValue = null;
            key = "env.test.int.float";
            val = ZKEnvironmentUtils.getInteger(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getInteger(key, 3);
            TestCase.assertEquals(3, val.intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /*** 测试取 Long 配置属性 ***/
    @Test
    public void testGetLong() {
        try {
            String key = "";
            Long val = null;
            Long expectValue = null;

            /*** 属性 存在 ***/
            expectValue = 1l;
            key = "env.test.long";

            val = ZKEnvironmentUtils.getLong(key);
            TestCase.assertEquals(expectValue.longValue(), val.longValue());
            val = ZKEnvironmentUtils.getLong(key, null);
            TestCase.assertEquals(expectValue.longValue(), val.longValue());
            val = ZKEnvironmentUtils.getLong(key, 0l);
            TestCase.assertEquals(expectValue.longValue(), val.longValue());

            /*** 属性 不存在 ***/
            expectValue = null;
            key = "env.test.long.not.exist";

            val = ZKEnvironmentUtils.getLong(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, 3l);
            TestCase.assertEquals(3l, val.longValue());

            /*** 属性 空 ***/
            expectValue = null;
            key = "env.test.long.empty";

            val = ZKEnvironmentUtils.getLong(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, 3l);
            TestCase.assertEquals(3l, val.longValue());

            /*** 属性 非数字 ***/

            expectValue = null;
            key = "env.test.long.str";

            val = ZKEnvironmentUtils.getLong(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, 3l);
            TestCase.assertEquals(3l, val.longValue());

            /*** 属性 符点型 ***/
            expectValue = null;
            key = "env.test.long.float";
            val = ZKEnvironmentUtils.getLong(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getLong(key, 3l);
            TestCase.assertEquals(3l, val.longValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /*** 测试取 Float 配置属性 ***/
    @Test
    public void testGetFloat() {
        try {
            String key = "";
            Float val = null;
            Float expectValue = null;
            float measure = 0.000001f;

            /*** 属性 存在 ***/
            expectValue = 3.3f;
            key = "env.test.float";

            val = ZKEnvironmentUtils.getFloat(key);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() < measure);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() > -1 * measure);
            val = ZKEnvironmentUtils.getFloat(key, null);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() < measure);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() > -1 * measure);
            val = ZKEnvironmentUtils.getFloat(key, 0f);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() < measure);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() > -1 * measure);

            /*** 属性 不存在 ***/
            expectValue = null;
            key = "env.test.float.not.exist";

            val = ZKEnvironmentUtils.getFloat(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getFloat(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getFloat(key, 0f);
            TestCase.assertTrue(0f - val.floatValue() < measure);
            TestCase.assertTrue(0f - val.floatValue() > -1 * measure);

            /*** 属性 空 ***/
            expectValue = null;
            key = "env.test.float.empty";

            val = ZKEnvironmentUtils.getFloat(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getFloat(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getFloat(key, 0f);
            TestCase.assertTrue(0f - val.floatValue() < measure);
            TestCase.assertTrue(0f - val.floatValue() > -1 * measure);

            /*** 属性 非数字 ***/
            expectValue = null;
            key = "env.test.float.str";

            val = ZKEnvironmentUtils.getFloat(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getFloat(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getFloat(key, 0f);
            TestCase.assertTrue(0f - val.floatValue() < measure);
            TestCase.assertTrue(0f - val.floatValue() > -1 * measure);

            /*** 属性 int 型 ***/
            expectValue = 1f;
            key = "env.test.float.int";

            val = ZKEnvironmentUtils.getFloat(key);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() < measure);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() > -1 * measure);
            val = ZKEnvironmentUtils.getFloat(key, null);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() < measure);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() > -1 * measure);
            val = ZKEnvironmentUtils.getFloat(key, 0f);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() < measure);
            TestCase.assertTrue(expectValue.floatValue() - val.floatValue() > -1 * measure);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /*** 测试取 Float 配置属性 ***/
    @Test
    public void testGetDouble() {
        try {
            String key = "";
            Double val = null;
            Double expectValue = null;
            Double measure = 0.000001d;

            /*** 属性 存在 ***/
            expectValue = 3.3d;
            key = "env.test.double";

            val = ZKEnvironmentUtils.getDouble(key);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() < measure);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() > -1 * measure);
            val = ZKEnvironmentUtils.getDouble(key, null);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() < measure);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() > -1 * measure);
            val = ZKEnvironmentUtils.getDouble(key, 0d);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() < measure);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() > -1 * measure);

            /*** 属性 不存在 ***/
            expectValue = null;
            key = "env.test.double.not.exist";

            val = ZKEnvironmentUtils.getDouble(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getDouble(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getDouble(key, 0d);
            TestCase.assertTrue(0f - val.doubleValue() < measure);
            TestCase.assertTrue(0f - val.doubleValue() > -1 * measure);

            /*** 属性 空 ***/
            expectValue = null;
            key = "env.test.double.empty";

            val = ZKEnvironmentUtils.getDouble(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getDouble(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getDouble(key, 0d);
            TestCase.assertTrue(0f - val.doubleValue() < measure);
            TestCase.assertTrue(0f - val.doubleValue() > -1 * measure);

            /*** 属性 非数字 ***/
            expectValue = null;
            key = "env.test.double.str";

            val = ZKEnvironmentUtils.getDouble(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getDouble(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getDouble(key, 0d);
            TestCase.assertTrue(0f - val.doubleValue() < measure);
            TestCase.assertTrue(0f - val.doubleValue() > -1 * measure);

            /*** 属性 int 型 ***/
            expectValue = 1d;
            key = "env.test.double.int";

            val = ZKEnvironmentUtils.getDouble(key);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() < measure);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() > -1 * measure);
            val = ZKEnvironmentUtils.getDouble(key, null);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() < measure);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() > -1 * measure);
            val = ZKEnvironmentUtils.getDouble(key, 0d);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() < measure);
            TestCase.assertTrue(expectValue.doubleValue() - val.doubleValue() > -1 * measure);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /*** 测试取 Boolean 配置属性 ***/
    @Test
    public void testGetBoolean() {
        try {
            String key = "";
            Boolean val = null;
            Boolean expectValue = null;

            /*** 存在 0-false; false-false; 1-true; true-true; ***/
            expectValue = true;
            key = "env.test.boolean.1";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, false);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());

            key = "env.test.boolean.true";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, false);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());

            expectValue = false;

            key = "env.test.boolean.0";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, true);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());

            key = "env.test.boolean.false";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, true);
            TestCase.assertEquals(expectValue.booleanValue(), val.booleanValue());

            /*** 不存在 ***/
            expectValue = null;
            key = "env.test.boolean.not.exist";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getBoolean(key, true);
            TestCase.assertEquals(true, val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, false);
            TestCase.assertEquals(false, val.booleanValue());

            /*** 空 ***/
            expectValue = null;
            key = "env.test.boolean.empty";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getBoolean(key, true);
            TestCase.assertEquals(true, val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, false);
            TestCase.assertEquals(false, val.booleanValue());

            /*** 非 可转化为 boolean 型的字符 ***/
            expectValue = null;
            key = "env.test.boolean.str";
            val = ZKEnvironmentUtils.getBoolean(key);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getBoolean(key, null);
            TestCase.assertEquals(expectValue, val);
            val = ZKEnvironmentUtils.getBoolean(key, true);
            TestCase.assertEquals(true, val.booleanValue());
            val = ZKEnvironmentUtils.getBoolean(key, false);
            TestCase.assertEquals(false, val.booleanValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
