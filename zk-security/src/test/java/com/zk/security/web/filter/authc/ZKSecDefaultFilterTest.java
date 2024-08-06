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
* @Title: ZKSecDefaultFilterTest.java 
* @author Vinson 
* @Package com.zk.security.web.filter.authc 
* @Description: TODO(simple description this file what to do. ) 
* @date May 14, 2022 11:05:02 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.authc;

import org.junit.Test;

import com.zk.security.web.filter.ZKSecAbstractFilter;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecDefaultFilterTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultFilterTest {

    @Test
    public void testNewInstance() {
        try {

            ZKSecAbstractFilter filter = (ZKSecAbstractFilter) ZKSecDefaultFilter.authcDev.newInstance();
            TestCase.assertEquals("authcDev", filter.getName());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
