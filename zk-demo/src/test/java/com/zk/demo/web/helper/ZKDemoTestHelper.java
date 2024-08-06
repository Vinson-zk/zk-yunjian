package com.zk.demo.web.helper;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.demo.web.ZKDemoSpringBootMain;

import junit.framework.TestCase;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: 测试助手
 * @ClassName ZKDemoTestHelper
 * @Package com.zk.demo.helper
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 09:43:07
 **/
public class ZKDemoTestHelper {

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        return getCtx();
    }

    public static ConfigurableApplicationContext getCtx() {
        if(ctx == null) {
            ctx = ZKDemoSpringBootMain.run(new String[] {});
        }
        return ctx;
    }

    @Test
    public void testCtx() {
        TestCase.assertNotNull(getCtx());
    }

}
