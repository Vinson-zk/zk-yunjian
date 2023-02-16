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
* @Title: ZKSysNavServiceTest.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 2:02:09 PM 
* @version V1.0 
*/
package com.zk.sys.res.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.exception.ZKValidatorException;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysNav;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysNavServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysNavServiceTest {

    @Test
    public void testDml() {

        ZKSysNavService zkSysNavService = ZKSysTestHelper.getMainCtx().getBean(ZKSysNavService.class);

        List<ZKSysNav> dels = new ArrayList<>();

        try {
            ZKSysNav zkSysNav = null, tempSysNav = null;
            int result = 0;

            /*** 保存 ***/
            zkSysNav = new ZKSysNav();
            try {
                zkSysNav.setName(new ZKJson());
                zkSysNavService.save(zkSysNav);
                TestCase.assertTrue(false);
            }
            catch(ZKValidatorException e) {
                System.out.println("[^_^:20200820-1406-001] 数据验证失败：" + e.getMessage());
            }

            zkSysNav = new ZKSysNav();
            zkSysNav.putName("test-name-1", "test-name-1");
            zkSysNav.setCode("test-nav-code");
            zkSysNav.setFuncModuleCode("test-funcModuleCode");
            zkSysNav.setFuncName("test-funcName");
            zkSysNav.setPath("test-path");
            zkSysNav.setIsShow(1);
            zkSysNav.setIsIndex(0);
            zkSysNav.setSort(1);
            result = 0;
            result = zkSysNavService.save(zkSysNav);
            TestCase.assertEquals(1, result);
            dels.add(zkSysNav);

            /*** 修改 ***/
            zkSysNav.putName("test-name-1", "test-name-1-update");
            zkSysNav.putName("test-name-2", "test-name-2");
            zkSysNav.setCode("test-nav-code-2");
            zkSysNav.setFuncModuleCode("test-funcModuleCode-2");
            zkSysNav.setFuncName("test-funcName-update");
            zkSysNav.setPath("test-path-update");
            result = 0;
            result = zkSysNavService.save(zkSysNav);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            tempSysNav = zkSysNavService.get(zkSysNav);
            TestCase.assertNotNull(tempSysNav);
            TestCase.assertEquals("test-name-1-update", tempSysNav.getName().get("test-name-1"));
            TestCase.assertEquals("test-name-2", tempSysNav.getName().get("test-name-2"));
            TestCase.assertEquals("test-nav-code", tempSysNav.getCode());
            TestCase.assertEquals("test-funcModuleCode", tempSysNav.getFuncModuleCode());
            TestCase.assertEquals("test-path-update", tempSysNav.getPath());
            TestCase.assertEquals("test-funcName-update", tempSysNav.getFuncName());

            /*** 删除 ***/
            result = 0;
            result = zkSysNavService.del(zkSysNav);
            TestCase.assertEquals(1, result);
            tempSysNav = zkSysNavService.get(zkSysNav);
            TestCase.assertNotNull(tempSysNav);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, tempSysNav.getDelFlag().intValue());

            /*** 恢复，新增 菜单代码存在，恢复并修改 ***/
            result = 0;
            zkSysNav = new ZKSysNav();
            zkSysNav.putName("test-name-1", "test-name-1");
            zkSysNav.setCode("test-nav-code");
            zkSysNav.setFuncModuleCode("funcModuleCode");
            zkSysNav.setFuncName("test-funcName-update");
            zkSysNav.setPath("test-path-update");
            zkSysNav.setIsShow(1);
            zkSysNav.setIsIndex(0);
            zkSysNav.setSort(1);
            result = zkSysNavService.save(zkSysNav);
            dels.add(zkSysNav);
            TestCase.assertEquals(1, result);
            tempSysNav = zkSysNavService.get(zkSysNav);
            TestCase.assertNotNull(tempSysNav);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.normal, tempSysNav.getDelFlag().intValue());
            TestCase.assertEquals("test-name-1", tempSysNav.getName().get("test-name-1"));
            // 导航代码已存在时，进行了物理 删除
//            TestCase.assertEquals("test-name-2", tempSysNav.getName().get("test-name-2"));
            TestCase.assertEquals(null, tempSysNav.getName().get("test-name-2"));
            TestCase.assertEquals("test-funcName-update", tempSysNav.getFuncName());
            TestCase.assertEquals(null, tempSysNav.getName().get("test-name-3"));

            /*** 物理删除 ***/
            result = zkSysNavService.diskDel(zkSysNav);
            TestCase.assertEquals(1, result);
            tempSysNav = zkSysNavService.get(zkSysNav);
            TestCase.assertNull(tempSysNav);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                zkSysNavService.diskDel(item);
            });
        }
    }

}
