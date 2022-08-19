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
* @Title: ZKCodeGenServiceTest.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2021 6:36:30 PM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import org.junit.Test;

import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;

/** 
* @ClassName: ZKCodeGenServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCodeGenServiceTest {

    @Test
    public void testGenCode() {

        ZKCodeGenService s = ZKDevleopmentToolTestHelper.getMainCtx().getBean(ZKCodeGenService.class);

        String moduleId;
        String[] tableIds;
        
        moduleId = "5520657069075595776";
        tableIds = new String[] { "5716360042878337536" };

//        moduleId = "5713506574702477824";
//        tableIds = new String[] { "5715878812110029312" };

        try {
            s.genCode(moduleId, tableIds);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}

