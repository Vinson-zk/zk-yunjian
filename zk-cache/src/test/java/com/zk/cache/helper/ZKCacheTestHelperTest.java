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
* @Title: ZKCacheTestHelperTest.java 
* @author Vinson 
* @Package com.zk.cache.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 7, 2021 12:45:18 PM 
* @version V1.0 
*/
package com.zk.cache.helper;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import junit.framework.TestCase;

/**
 * 测试配置项目
 * 
 * @ClassName: ZKCacheTestHelperTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKCacheTestHelperTest {

    @Test
    public void testRedisConfig() {
        ConfigurableApplicationContext ctx = ZKCacheTestSpringbootMain.run(new String[] {});
        TestCase.assertNotNull(ctx);
    }


}
