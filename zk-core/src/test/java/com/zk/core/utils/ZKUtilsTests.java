/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKUtilsTests.java 
* @author Vinson 
* @Package com.zk.core.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 23, 2024 12:28:10 AM 
* @version V1.0 
*/
package com.zk.core.utils;

import java.net.URL;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKUtilsTests 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKUtilsTests {

    @Test
    public void testGetAbsolutePath() {
        try {
            String path;
            String expected;
            String baseStr = "/Users/vinson/bs/bs_ws/ws_sts4/zk/zk-yunjian/zk-core/target/";

            // mybatis_config.xml
            expected = baseStr + "test-classes/";
            path = ZKUtils.getAbsolutePath("");
            System.out.println("[^_^:20240623-0047-001] " + path);
            TestCase.assertEquals(expected, path);

            expected = baseStr + "test-classes/";
            path = ZKUtils.getAbsolutePath("/");
            System.out.println("[^_^:20240623-0047-002] " + path);
            TestCase.assertEquals(expected, path);

            expected = baseStr + "classes/zk.properties";
            path = ZKUtils.getAbsolutePath("zk.properties");
            System.out.println("[^_^:20240623-0047-003] " + path);
            TestCase.assertEquals(expected, path);

            expected = baseStr + "test-classes/zk.properties";
            path = ZKUtils.getAbsolutePath("/zk.properties");
            System.out.println("[^_^:20240623-0047-004] " + path);
            TestCase.assertEquals(expected, path);

            expected = baseStr + "test-classes/mybatis_config.xml";
            path = ZKUtils.getAbsolutePath("mybatis_config.xml");
            System.out.println("[^_^:20240623-0047-005] " + path);
            TestCase.assertEquals(expected, path);

            expected = baseStr + "classes/xmlConfig/mybatis/mybatis_config.xml";
            path = ZKUtils.getAbsolutePath("xmlConfig/mybatis/mybatis_config.xml");
            System.out.println("[^_^:20240623-0047-006] " + path);
            TestCase.assertEquals(expected, path);
            
            @SuppressWarnings("deprecation")
            URL url = new URL("file://" + path);
            System.out.println("[^_^:20240623-0047-007] " + url.getPath());
            System.out.println("[^_^:20240623-0047-007] " + url.getFile());
            System.out.println("[^_^:20240623-0047-007] " + url.toString());
            System.out.println("[^_^:20240623-0047-007] "
                    + Thread.currentThread().getContextClassLoader().getResource("").toString());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
