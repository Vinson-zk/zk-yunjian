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
* @Title: ZKModuleServiceTest.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 30, 2021 11:02:59 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.entity.ZKModule;

import junit.framework.TestCase;

/** 
* @ClassName: ZKModuleServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKModuleServiceTest {

    @Test
    public void testDml() {

        ZKModuleService s = ZKDevleopmentToolTestHelper.getMainCtx().getBean(ZKModuleService.class);

        List<ZKModule> dels = new ArrayList<>();

        try {
            ZKModule entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = new ZKModule();
            entity.setDbType("t-dbType");
            entity.setDriver("driver");
            entity.setUrl("url");
            entity.setUsername("username");
            entity.setPassword("password");
            entity.setIsRemovePrefix(false);
            entity.setPackagePrefix("packagePrefix");
            entity.setModuleName("moduleName");
            entity.setLabelName("setLabelName");
            entity.setColNamePrefix("colNamePrefix");

            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);
            dels.add(entity);

            /*** 修改 ***/
            entity.setDbType("t-dbType-u");
            entity.setDriver("driver");
            entity.setUrl("url");
            entity.setUsername("username");
            entity.setPassword("password");
            entity.setIsRemovePrefix(false);
            entity.setPackagePrefix("packagePrefix");
            entity.setModuleName("moduleName");
            entity.setLabelName("setLabelName");
            entity.setColNamePrefix("colNamePrefix");
            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            entity = s.get(entity);
            TestCase.assertNotNull(entity);

            /*** 删除 ***/
            result = 0;
            result = s.del(entity);
            TestCase.assertEquals(1, result);
            entity = s.get(entity);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, entity.getDelFlag().intValue());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                s.diskDel(item);
            });
        }
    }

}
