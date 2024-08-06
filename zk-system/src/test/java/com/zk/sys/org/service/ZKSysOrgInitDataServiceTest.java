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
* @Title: ZKSysOrgInitDataServiceTest.java 
* @author Vinson 
* @Package com.zk.sys.org.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 26, 2022 8:29:09 AM 
* @version V1.0 
*/
package com.zk.sys.org.service;

import org.junit.Test;

import com.zk.sys.helper.ZKSysTestHelper;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysOrgInitDataServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysOrgInitDataServiceTest {

    @Test
    public void testInitOwnerPlatform() {

        ZKSysOrgInitDataService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysOrgInitDataService.class);

        try {
            s.initOwnerPlatform();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {

        }
    }

}
