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
* @Title: ZKBaseHelperTreeEntityDaoTest.java 
* @author Vinson 
* @Package com.zk.base.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 2, 2023 7:52:09 PM 
* @version V1.0 
*/
package com.zk.base.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.helper.ZKBaseHelperCtx;
import com.zk.base.helper.dao.ZKBaseHelperTreeEntityDao;
import com.zk.base.helper.entity.ZKBaseHelperTreeEntity;
import com.zk.base.helper.service.ZKBaseHelperTreeEntityService;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.entity.ZKDBEntity;
import com.zk.db.mybatis.dao.ZKDBDao;

import junit.framework.TestCase;

/** 
* @ClassName: ZKBaseHelperTreeEntityDaoTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKBaseHelperTreeEntityDaoTest {

    @Test
    public void testGetSuperclassByName() {
        try {
            Class<ZKDBEntity<?>> classz = ZKClassUtils.getSuperclassByName(ZKDBDao.class,
                    ZKBaseHelperTreeEntityDao.class, "E");
            System.out.println("[^_^:20221012-1526-001] classz: " + classz.getName());
            TestCase.assertEquals(ZKBaseHelperTreeEntity.class, classz);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 创建树弄结构 测试数据; 不要重复创建，免得冗余数据影响测试断言
//    @Test
    public void testMaskTreeData() {
        try {
            ZKBaseHelperTreeEntityDaoTest.makeTree();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testFindTree() {
        ConfigurableApplicationContext ctx = ZKBaseHelperCtx.run(new String[] {});
        ZKBaseHelperTreeEntityDao dao = ctx.getBean(ZKBaseHelperTreeEntityDao.class);
        TestCase.assertNotNull(dao);
        List<ZKBaseHelperTreeEntity> dels = new ArrayList<>();
        try {
            int nodeCount = 3, level = 3;
//            dels.addAll(makeTestTree(dao, KeyName.valueName, nodeCount, level)); // 创建树形数据的层级

            System.out.println("[^_^:20221017-1453-001] ----------------------------------------");
            System.out.println("[^_^:20221017-1453-001] 初始树形数据，创建完成。");

//            int resInt = 0;
            ZKPage<ZKBaseHelperTreeEntity> page = null;
            List<ZKBaseHelperTreeEntity> resList = null;
            ZKBaseHelperTreeEntity e = null;
            /* 列表查询 ********************************************************************/
            e = new ZKBaseHelperTreeEntity();
            e.setValue(KeyName.valueName);
            resList = dao.findList(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(calculateNodeCount(level, nodeCount), resList.size());

            page = new ZKPage<ZKBaseHelperTreeEntity>();
            page.setPageSize(calculateNodeCount(level, nodeCount));
            e.setPage(page);
            resList = dao.findList(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(calculateNodeCount(level, nodeCount), resList.size());

            /* 树型查询：1、子节点不过滤，不分页 ********************************************************************/
            e = new ZKBaseHelperTreeEntity();
            e.setJson(new ZKJson());
            e.setValue(KeyName.valueName);
            e.setParentIdIsEmpty(true);

            // 查询空
            e.getJson().put(KeyName.jsonKey, "KeyName.valueName");
            resList = dao.findTree(e);
            TestCase.assertNotNull(resList);
            TestCase.assertTrue(resList.isEmpty());
            // 树形查询
            e.getJson().put(KeyName.jsonKey, KeyName.valueName);
            resList = dao.findTree(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            verifyTree(resList, level, nodeCount);
            // 树形查询，每个层级只查一个
            e = new ZKBaseHelperTreeEntity();
            e.setValue(".n1");
            e.setParentIdIsEmpty(true);
            resList = dao.findTree(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(1, resList.size());
            verifyTree(resList.get(0).getChildren(), 2, nodeCount);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            for (ZKBaseHelperTreeEntity item : dels) {
                dao.diskDel(item);
            }
        }
    }

    /** 一些计算 ******************************************************************************************/
    public static List<ZKBaseHelperTreeEntity> makeTree() {
        try {
            ConfigurableApplicationContext ctx = ZKBaseHelperCtx.run(new String[] {});
            ZKBaseHelperTreeEntityDao dao = ctx.getBean(ZKBaseHelperTreeEntityDao.class);
            TestCase.assertNotNull(dao);
            int nodeCount = 3, level = 3;
            return ZKBaseHelperTreeEntityDaoTest.makeTestTree(dao, KeyName.valueName, nodeCount, level); // 创建树形数据的层级
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
        return null;
    }

    public int calculateNodeCount(int level, int nodeCount) {
        int res = 0;
        for (int i = 1; i <= level; ++i) {
            res = res + (int) Math.pow(nodeCount, i);
        }
        return res;
    }

    public void verifyTree(List<ZKBaseHelperTreeEntity> list, int level, int nodeCount) {
        if (level > 0) {
            TestCase.assertEquals(nodeCount, list.size());
            for (ZKBaseHelperTreeEntity item : list) {
                verifyTree(item.getChildren(), level - 1, nodeCount);
            }
        }
        else {
            for (ZKBaseHelperTreeEntity item : list) {
                System.out.println("[^_^:20221017-1833-001] item.getChildren(): "
                        + ZKJsonUtils.toJsonStr(item.getChildren()));
                TestCase.assertTrue(item.getChildren().isEmpty());
            }
        }
    }

//    /** 创建测试数据 ******************************************************************************/
    public static List<ZKBaseHelperTreeEntity> makeTestTree(ZKBaseHelperTreeEntityDao treeDao, String namePrefix,
            int nodeCount, int level) {

        List<ZKBaseHelperTreeEntity> ms = new ArrayList<>();
        List<ZKBaseHelperTreeEntity> childMs = null, tempMs = null;
        int l = 0;
        do {
            if (childMs != null) {
                tempMs = childMs;
                childMs = new ArrayList<>();
                for (ZKBaseHelperTreeEntity m : tempMs) {
                    childMs.addAll(ZKBaseHelperTreeEntityDaoTest.makeTestMenu(treeDao, m, namePrefix + ".l" + l,
                            m.getRemarks(), nodeCount));
                }
            }
            else {
                childMs = new ArrayList<>();
                childMs.addAll(ZKBaseHelperTreeEntityDaoTest.makeTestMenu(treeDao, null, namePrefix + ".l" + l, "",
                        nodeCount));
            }
            ms.addAll(childMs);
            ++l;
        } while (l < level);
        return ms;
    }

    public static List<ZKBaseHelperTreeEntity> makeTestMenu(ZKBaseHelperTreeEntityDao dao, ZKBaseHelperTreeEntity e,
            String namePrefix, String parentName, int nodeCount) {

        List<ZKBaseHelperTreeEntity> ms = new ArrayList<>();
        ZKBaseHelperTreeEntity tempE = null;

        ZKBaseHelperTreeEntityService s = new ZKBaseHelperTreeEntityService();

        for (int i = 0; i < nodeCount; ++i) {
            tempE = ZKBaseHelperTreeEntityDaoTest.newEntity(namePrefix + parentName + ".n" + i);
            if (e != null) {
                tempE.setParentId(e.getPkId());
            }
            tempE.setmInt(i * 1l);
            tempE.setRemarks(parentName + ".n" + i);
//            tempE.preInsert();
            s.preInsert(tempE);
            dao.insert(tempE);
            ms.add(tempE);
        }
        return ms;
    }

    public static ZKBaseHelperTreeEntity newEntity(String name) {
        ZKBaseHelperTreeEntity e = new ZKBaseHelperTreeEntity();
        e.setValue(name);
        e.setJson(ZKJson.parse("{}"));
        e.getJson().put(KeyName.jsonKey, name);
        e.setId2("1");
        e.setmBoolean(true);
        e.setmInt(1l);
        e.setmDate(ZKDateUtils.getToday());
        return e;
    }

    public static interface KeyName {
        public static final String jsonKey = "t-key-1";

        public static final String valueName = "t-node";
    }

}
