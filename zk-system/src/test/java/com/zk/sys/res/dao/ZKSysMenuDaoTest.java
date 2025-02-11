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
* @Title: ZKSysMenuDaoTest.java 
* @author Vinson 
* @Package com.zk.sys.res.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 6, 2021 9:34:51 PM 
* @version V1.0 
*/
package com.zk.sys.res.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysMenu;
import com.zk.sys.res.service.ZKSysMenuService;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysMenuDaoTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysMenuDaoTest {

    @Test
    public void testDml() {

        ZKSysMenuDao zkSysMenuDao = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuDao.class);
        ZKSysMenuService zkSysMenuService = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuService.class);

        List<ZKSysMenu> dels = new ArrayList<>();

        try {
            ZKSysMenu zkSysMenu = null, tempSysMenu = null;
            int result = 0;
            List<ZKSysMenu> res = null;

            /*** 保存 ***/
            zkSysMenu = new ZKSysMenu();
            zkSysMenu = new ZKSysMenu();
            zkSysMenu.putName("test-name-1", "test-name-1");
            zkSysMenu.setCode("test-menu-code-dao");
            zkSysMenu.setNavCode("navCode");
            zkSysMenu.setFuncModuleCode("funcModuleCode");
            zkSysMenu.setIsFrame(0);
            zkSysMenu.setIsShow(1);
            zkSysMenu.setIsIndex(0);
            zkSysMenu.setExact(false);
            zkSysMenu.setSort(1);
            result = 0;
//            zkSysMenu.preInsert();
            zkSysMenuService.preInsert(zkSysMenu);
            result = zkSysMenuDao.insert(zkSysMenu);
            TestCase.assertEquals(1, result);
            dels.add(zkSysMenu);

            /*** 修改 ***/
            zkSysMenu.putName("test-name-1", "test-name-1-update");
            zkSysMenu.putName("test-name-2", "test-name-2");
            zkSysMenu.setCode("test-menu-code-2");
            result = 0;
            result = zkSysMenuDao.update(zkSysMenu);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            tempSysMenu = zkSysMenuDao.get(zkSysMenu);
            TestCase.assertNotNull(tempSysMenu);
            TestCase.assertEquals("test-name-1-update", tempSysMenu.getName().get("test-name-1"));
            TestCase.assertEquals("test-name-2", tempSysMenu.getName().get("test-name-2"));
            TestCase.assertEquals("test-menu-code-dao", tempSysMenu.getCode());

            /*** 列表查询 ***/
            res = zkSysMenuDao.findList(tempSysMenu);
            TestCase.assertEquals(1, res.size());

            /*** 删除 ***/
            result = 0;
            zkSysMenu.setDelFlag(ZKBaseEntity.DEL_FLAG.delete);
            result = zkSysMenuDao.del(zkSysMenu);
            TestCase.assertEquals(1, result);
            tempSysMenu = zkSysMenuDao.get(zkSysMenu);
            TestCase.assertNotNull(tempSysMenu);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, tempSysMenu.getDelFlag().intValue());

            /*** 物理删除 ***/
            result = zkSysMenuDao.diskDel(zkSysMenu);
            TestCase.assertEquals(1, result);
            tempSysMenu = zkSysMenuDao.get(zkSysMenu);
            TestCase.assertNull(tempSysMenu);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                zkSysMenuDao.diskDel(item);
            });
        }
    }

}
