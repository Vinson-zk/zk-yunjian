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
* @Title: ZKFileTestHelper.java 
* @author Vinson 
* @Package com.zk.file.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 25, 2022 11:45:05 AM 
* @version V1.0 
*/
package com.zk.file.helper;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.file.ZKFileSpringBootMain;

import junit.framework.TestCase;

/** 
* @ClassName: ZKFileTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFileTestHelper {

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        return getCtx();
    }

    public static ConfigurableApplicationContext getCtx() {
        if (ctx == null) {
            ctx = ZKFileSpringBootMain.run(new String[] {});
        }
        return ctx;
    }

    @Test
    public void testCtx() {
        TestCase.assertNotNull(getCtx());
    }

}
