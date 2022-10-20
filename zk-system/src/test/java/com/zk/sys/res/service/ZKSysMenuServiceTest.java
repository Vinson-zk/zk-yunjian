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
* @Title: ZKSysMenuServiceTest.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 5, 2020 12:25:04 PM 
* @version V1.0 
*/
package com.zk.sys.res.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.exception.ZKValidatorException;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.res.entity.ZKSysMenu;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSysMenuServiceTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysMenuServiceTest {

    @Test
    public void testDml() {

        ZKSysMenuService zkSysMenuService = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuService.class);

        List<ZKSysMenu> dels = new ArrayList<>();

        try {
            ZKSysMenu zkSysMenu = null, tempSysMenu = null;
            int result = 0;

            /*** 保存 ***/
            zkSysMenu = new ZKSysMenu();
            try {
                zkSysMenu.setName(new ZKJson());
                zkSysMenu.setParentId("012345678901234567890-1");
                zkSysMenuService.save(zkSysMenu);
                TestCase.assertTrue(false);
            }
            catch(ZKValidatorException e) {
                System.out.println("[^_^:20200805-1406-001] 数据验证失败：" + e.getMessage());
            }

            zkSysMenu = new ZKSysMenu();
            zkSysMenu.putName("test-name-1", "test-name-1");
            zkSysMenu.setCode("test-menu-code");
            zkSysMenu.setNavCode("navCode");
            zkSysMenu.setFuncModuleCode("funcModuleCode");
            zkSysMenu.setIsFrame(0);
            zkSysMenu.setIsShow(1);
            zkSysMenu.setIsIndex(0);
            zkSysMenu.setExact(false);
            zkSysMenu.setSort(1);
            result = 0;
            result = zkSysMenuService.save(zkSysMenu);
            TestCase.assertEquals(1, result);
            dels.add(zkSysMenu);

            /*** 修改 ***/
            zkSysMenu.putName("test-name-1", "test-name-1-update");
            zkSysMenu.putName("test-name-2", "test-name-2");
            zkSysMenu.setCode("test-menu-code-2");
            result = 0;
            result = zkSysMenuService.save(zkSysMenu);
            TestCase.assertEquals(1, result);

            /*** 查询 ***/
            tempSysMenu = zkSysMenuService.get(zkSysMenu);
            TestCase.assertNotNull(tempSysMenu);
            TestCase.assertEquals("test-name-1-update", tempSysMenu.getName().get("test-name-1"));
            TestCase.assertEquals("test-name-2", tempSysMenu.getName().get("test-name-2"));
            TestCase.assertEquals("test-menu-code", tempSysMenu.getCode());

            /*** 删除 ***/
            result = 0;
            result = zkSysMenuService.del(zkSysMenu);
            TestCase.assertEquals(1, result);
            tempSysMenu = zkSysMenuService.get(zkSysMenu);
            TestCase.assertNotNull(tempSysMenu);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, tempSysMenu.getDelFlag().intValue());

            /*** 恢复，新增 菜单代码存在，恢复并修改 ***/
            result = 0;
            zkSysMenu = new ZKSysMenu();
            zkSysMenu.putName("test-name-1", "test-name-1");
            zkSysMenu.setCode("test-menu-code");
            zkSysMenu.setNavCode("navCode");
            zkSysMenu.setFuncModuleCode("funcModuleCode");
            zkSysMenu.setIsFrame(0);
            zkSysMenu.setIsShow(1);
            zkSysMenu.setIsIndex(0);
            zkSysMenu.setExact(false);
            zkSysMenu.setSort(1);
            result = zkSysMenuService.save(zkSysMenu);
            dels.add(zkSysMenu);
            TestCase.assertEquals(1, result);
            tempSysMenu = zkSysMenuService.get(zkSysMenu);
            TestCase.assertNotNull(tempSysMenu);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.normal, tempSysMenu.getDelFlag().intValue());
            TestCase.assertEquals("test-name-1", tempSysMenu.getName().get("test-name-1"));
            // 代码已存在时，进行了物理 删除
            TestCase.assertEquals(null, tempSysMenu.getName().get("test-name-2"));
            TestCase.assertEquals(null, tempSysMenu.getName().get("test-name-3"));

            /*** 物理删除 ***/
            result = zkSysMenuService.diskDel(zkSysMenu);
            TestCase.assertEquals(1, result);
            tempSysMenu = zkSysMenuService.get(zkSysMenu);
            TestCase.assertNull(tempSysMenu);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }finally {
            dels.forEach(item->{
                zkSysMenuService.diskDel(item);
            });
        }
    }

    // 测试 list 查询 与树形查询
    @Test
    public void testFindList() {

        ZKSysMenuService zkSysMenuService = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuService.class);

        List<ZKSysMenu> dels = new ArrayList<>();

        try {
            ZKSysMenu zkSysMenu = null;
            List<ZKSysMenu> list = null;
            int nodeCount = 2, level = 3;

            /*** 制作树型数据 ***/
//            dels.addAll(makeTestMenu(zkSysMenuService, "findList-testName", nodeCount, level));

            /*** list 查询 ***/
            zkSysMenu = new ZKSysMenu();
            zkSysMenu.putName("test-name-1", "findList-testName");
            zkSysMenu.setCode("-findList-testName");
            // 查询父节点为 null
            zkSysMenu.setParentId("");
            list = zkSysMenuService.findList(zkSysMenu);
            TestCase.assertEquals(nodeCount, list.size());
            // 父节点不做查询条件
            zkSysMenu.setParentId(null);
            list = zkSysMenuService.findList(zkSysMenu);
            TestCase.assertEquals(calculate(nodeCount, level), list.size());

            // 指定父节点
            zkSysMenu.setParentId(dels.get(0).getPkId());
            list = zkSysMenuService.findList(zkSysMenu);
            TestCase.assertEquals(nodeCount, list.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                zkSysMenuService.diskDel(item);
            });
        }
    }

    // 计划 a 的 b 次方逐级的和；示例：x 3 ： x + x^2 + x^3
    private int calculate(int a, int b) {

        int c = 0;
        for (int i = 0; i < b; ++i) {
            c = c * a + a;
        }
        return c;
    }

    @Test
    public void testFindTree() {

        ZKSysMenuService zkSysMenuService = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuService.class);
        List<ZKSysMenu> dels = new ArrayList<>();
        try {
            ZKSysMenu zkSysMenu = null;
            List<ZKSysMenu> resList = null;
            ZKPage<ZKSysMenu> resPage = null;

            /*** 制作树型数据 ***/
            // this.calculate(7, 3) = 399
            int nodeCount = 7;
//            int level = 3;
//            dels.addAll(makeTestMenu(zkSysMenuService, "testName", nodeCount, level));

//            /*** 树型查询 ***/
//            zkSysMenu = new ZKSysMenu();
//            zkSysMenu.setPkId(dels.get(0).getPkId());
//            resList = zkSysMenuService.findTree(zkSysMenu);
//            TestCase.assertFalse(resList.isEmpty());
//            zkSysMenu = resList.get(0);
//            TestCase.assertNotNull(zkSysMenu);
//            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().size());
//            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(0).getChildren().size());
//            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(1).getChildren().size());
//            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(2).getChildren().size());

            // 所有
            zkSysMenu = new ZKSysMenu();
            resList = zkSysMenuService.findTree(zkSysMenu);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(nodeCount, resList.size());
            zkSysMenu = resList.get(0);
            TestCase.assertNotNull(zkSysMenu);
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(0).getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(1).getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(2).getChildren().size());

            // 过虑
            zkSysMenu = new ZKSysMenu();
            zkSysMenu.setCode("-1");
            resList = zkSysMenuService.findTree(zkSysMenu);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(1, resList.size());
            zkSysMenu = resList.get(0);
            TestCase.assertNotNull(zkSysMenu);
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(0).getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(1).getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(2).getChildren().size());

            // 分页
            zkSysMenu = new ZKSysMenu();
            zkSysMenu.setCode("test");
            resPage = new ZKPage<ZKSysMenu>();
            resPage.setPageSize(3);
            resPage = zkSysMenuService.findTree(resPage, zkSysMenu);
            TestCase.assertEquals(3, resPage.getResult().size());
            TestCase.assertEquals(nodeCount, resPage.getTotalCount());
            zkSysMenu = resPage.getResult().get(0);
            TestCase.assertNotNull(zkSysMenu);
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(0).getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(1).getChildren().size());
            TestCase.assertEquals(nodeCount, zkSysMenu.getChildren().get(2).getChildren().size());

            zkSysMenu = zkSysMenu.getChildren().get(2).getChildren().get(0);
            TestCase.assertNotNull(zkSysMenu);
            TestCase.assertNull(zkSysMenu.getParent());

            zkSysMenu = zkSysMenuService.getDetail(zkSysMenu);
            TestCase.assertNotNull(zkSysMenu);
            TestCase.assertNotNull(zkSysMenu.getParent());
            TestCase.assertNotNull(zkSysMenu.getParent().getParent());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                zkSysMenuService.diskDel(item);
            });
        }
    }

    @Test
    public void testFindTreeTest() {

        ZKSysMenuService zkSysMenuService = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuService.class);
        List<ZKSysMenu> dels = new ArrayList<>();
        try {
            ZKSysMenu zkSysMenu = null;
            List<ZKSysMenu> list = null;

            /*** 制作树型数据 ***/
//            int nodeCount = 7, level = 3; // this.calculate(7, 3) = 399
            System.out.println("[^_^:20201015-1753-001]: this.calculate(7, 3)：" + this.calculate(7, 3));
//            dels.addAll(makeTestMenu(zkSysMenuService, "testName", nodeCount, level));

            /*** 树型 列表 查询 些功能还未完成 ***/
            zkSysMenu = new ZKSysMenu();
            zkSysMenu.setCode("testMenuCode-testName-0");
//            zkSysMenu.setFuncName("f-123");
            list = zkSysMenuService.findTree(zkSysMenu);
            TestCase.assertEquals(1, list.size());

            zkSysMenu = list.get(0);

            TestCase.assertNotNull(zkSysMenu.getChildren());
            TestCase.assertFalse(zkSysMenu.getChildren().isEmpty());

            zkSysMenu.getChildren().forEach(item -> {
                TestCase.assertNotNull(item.getChildren());
                TestCase.assertFalse(item.getChildren().isEmpty());
            });
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                zkSysMenuService.diskDel(item);
            });
        }
    }

    @Test
    public void testGetDetail() {

        ZKSysMenuService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysMenuService.class);

        try {
            ZKSysMenu e = null;
            String pkId = "5823591463954416128";

            e = s.getDetail(new ZKSysMenu(pkId));

            TestCase.assertNotNull(e);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /** 创建测试数据 ******************/

    //
    public static List<ZKSysMenu> makeTestMenu(ZKSysMenuService zkSysMenuService, String name, int nodeCount,
            int level) {

        List<ZKSysMenu> ms = new ArrayList<>();
        List<ZKSysMenu> childMs = null, tempMs = null;
        int l = 0;
        do {
            if (childMs != null) {
                tempMs = childMs;
                childMs = new ArrayList<>();
                for (ZKSysMenu m : tempMs) {
                    childMs.addAll(makeTestMenu(zkSysMenuService, m, m.getName().getString("test-name-1"), nodeCount));
                }
            }
            else {
                childMs = new ArrayList<>();
                childMs.addAll(makeTestMenu(zkSysMenuService, null, name, nodeCount));
            }
            ms.addAll(childMs);
            ++l;
        } while (l < level);
        
        return ms;
    }

    public static List<ZKSysMenu> makeTestMenu(ZKSysMenuService zkSysMenuService, ZKSysMenu menu, String name,
            int nodeCount) {
        List<ZKSysMenu> ms = new ArrayList<>();
        ZKSysMenu tempMenu = null;
        for (int i = 0; i < nodeCount; ++i) {
            tempMenu = newMenu(name + "-" + i);
            if (menu != null) {
                tempMenu.setParentId(menu.getPkId());
            }
            zkSysMenuService.save(tempMenu);
            ms.add(tempMenu);
        }
        return ms;
    }

    public static ZKSysMenu newMenu(String name) {
        ZKSysMenu menu = new ZKSysMenu();
        menu.putName("test-name-1", name);
        menu.putName("test-name-2", name);
        menu.setCode("testMenuCode-" + name);
        menu.setNavCode("navCode");
        menu.setFuncModuleCode("funcModuleCode");
        menu.setFuncName("funcName-" + name);
        menu.setIsFrame(0);
        menu.setIsShow(1);
        menu.setIsIndex(0);
        menu.setExact(false);
        menu.setSort(1);

        return menu;
    }

}
