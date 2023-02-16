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
* @Title: ZKColInfoServiceTest.java 
* @author Vinson 
* @Package com.zk.code.generate.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 1, 2021 11:50:45 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.devleopment.tool.ZKDevleopmentToolTestHelper;
import com.zk.devleopment.tool.gen.action.ZKConvertUtils;
import com.zk.devleopment.tool.gen.entity.ZKColInfo;

import junit.framework.TestCase;

/** 
* @ClassName: ZKColInfoServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKColInfoServiceTest {

    @Test
    public void testDml() {

        ZKColInfoService s = ZKDevleopmentToolTestHelper.getMainCtx().getBean(ZKColInfoService.class);

        List<ZKColInfo> dels = new ArrayList<>();

        try {
            ZKColInfo entity = null;
            int result = 0;

            /*** 保存 ***/
            entity = new ZKColInfo();
            entity.setColName("c-test");
            entity.setColJdbcType("varchar(1,2)");
            entity.setColComments("c-test");
            ZKConvertUtils.convertAttrInfo(ZKDevleopmentToolTestHelper.getTestModule(),
                    ZKDevleopmentToolTestHelper.getTestTable(null),
                    entity);

            result = 0;
            result = s.save(entity);
            TestCase.assertEquals(1, result);
            dels.add(entity);

            /*** 修改 ***/
            result = 0;
            entity.setColName("c-test-u");
            entity.setColComments("c-test-u");
            entity.setTableId("1");
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
