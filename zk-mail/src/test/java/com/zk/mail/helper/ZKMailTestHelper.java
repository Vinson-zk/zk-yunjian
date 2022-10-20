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
* @Title: ZKMailTestHelper.java 
* @author Vinson 
* @Package com.zk.mail 
* @Description: TODO(simple description this file what to do. ) 
* @date May 26, 2022 5:23:22 PM 
* @version V1.0 
*/
package com.zk.mail.helper;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.mail.ZKMailSpringBootMain;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMailTestHelper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMailTestHelper {

    static ConfigurableApplicationContext ctx = null;

    public static ConfigurableApplicationContext getMainCtx() {
        return getCtx();
    }

    public static ConfigurableApplicationContext getCtx() {
        if (ctx == null) {
            ctx = ZKMailSpringBootMain.run(new String[] {});
        }
        return ctx;
    }

    @Test
    public void testCtx() {
        TestCase.assertNotNull(getCtx());
    }

}
