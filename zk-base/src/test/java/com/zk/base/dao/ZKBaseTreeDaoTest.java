package com.zk.base.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.helper.ZKBaseHelperCtx;
import com.zk.base.helper.dao.ZKBaseHelperTreeDao;
import com.zk.base.helper.entity.ZKBaseHelperTreeEntity;
import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.entity.ZKDBBaseEntity;
import com.zk.db.mybatis.dao.ZKDBBaseDao;

import junit.framework.TestCase;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description:
 * @ClassName ZKBaseTreeDaoTest
 * @Package com.zk.base.dao
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-10-12 15:22:55
 **/
public class ZKBaseTreeDaoTest {

    @Test
    public void testGetSuperclassByName(){
        try {
            Class<ZKDBBaseEntity<?>> classz = ZKClassUtils.getSuperclassByName(ZKDBBaseDao.class,
                    ZKBaseHelperTreeDao.class, "E");
            System.out.println("[^_^:20221012-1526-001] classz: " + classz.getName());
            TestCase.assertEquals(ZKBaseHelperTreeEntity.class, classz);
        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testFindTree(){
        ConfigurableApplicationContext ctx = ZKBaseHelperCtx.run(new String[] {});
        ZKBaseHelperTreeDao dao = ctx.getBean(ZKBaseHelperTreeDao.class);
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
            /* 列表查询  ********************************************************************/
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

        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }finally {
            for(ZKBaseHelperTreeEntity item : dels){
                dao.diskDel(item);
            }
        }
    }

    @Test
    public void testFindTreeFilter(){
        ConfigurableApplicationContext ctx = ZKBaseHelperCtx.run(new String[] {});
        ZKBaseHelperTreeDao dao = ctx.getBean(ZKBaseHelperTreeDao.class);
        TestCase.assertNotNull(dao);
        List<ZKBaseHelperTreeEntity> dels = new ArrayList<>();
        try {
//            int nodeCount = 3, level = 3;
//            dels.addAll(makeTestTree(dao, KeyName.valueName, nodeCount, level)); // 创建树形数据的层级

            System.out.println("[^_^:20221018-1453-001] ----------------------------------------");
            System.out.println("[^_^:20221018-1453-001] 初始树形数据，创建完成。");

            int resInt = 0;
//            ZKPage<ZKBaseHelperTreeEntity> page = null;
            List<ZKBaseHelperTreeEntity> resList = null;
            ZKBaseHelperTreeEntity e = null;

            /* 树型 过滤查询：树形所有节点，统一过滤，过滤结果中不是根结点时，如果父节点不在过滤结果中，升级为结果中的根节点；注：仅支持根节点分页；
             *  ********************************************************************/
            e = new ZKBaseHelperTreeEntity();
            e.setParentIdIsEmpty(true);
            resList = dao.findTreeFilter(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(3, resList.size());
            //
//            System.out.println("[^_^:20221018-2130-001] ------------------------------------------------------------------- ");
//            System.out.println("[^_^:20221018-2130-001] ------------------------------------------------------------------- ");
//            System.out.println("[^_^:20221018-2130-001] ------------------------------------------------------------------- ");
            //
            e = new ZKBaseHelperTreeEntity();
            e.getExtraParams().put("ListRemarks", Arrays.asList(".n0", ".n0.n0", ".n0.n0.n0", ".n1.n1", ".n1.n1.n1",
                    ".n2", ".n2.n2.n2"));
            resList = dao.findTreeFilter(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(4, resList.size());
            for(ZKBaseHelperTreeEntity item:resList){
                TestCase.assertNull(item.getChildren());
                if(".n0".equals(item.getRemarks())){
                    resInt = resInt|1;
                }
                if(".n1.n1".equals(item.getRemarks())){
                    resInt = resInt|(1<<1);
                }
                if(".n2".equals(item.getRemarks())){
                    resInt = resInt|(1<<2);
                }
                if(".n2.n2.n2".equals(item.getRemarks())){
                    resInt = resInt|(1<<3);
                }
            }
            TestCase.assertEquals(15, resInt);

            // 查询指定父节点下的 数据
            e = new ZKBaseHelperTreeEntity();
            e.setRemarks(".n1");
            resList = dao.findTreeFilter(e);
            TestCase.assertEquals(1, resList.size());
            e = new ZKBaseHelperTreeEntity();
            e.setParentId(resList.get(0).getPkId());
            e.getExtraParams().put("ListRemarks", Arrays.asList(".n0", ".n0.n0", ".n0.n0.n0", ".n1", ".n1.n0", ".n1.n1.n1"));
            resList = dao.findTreeFilter(e);
            TestCase.assertNotNull(resList);
            TestCase.assertFalse(resList.isEmpty());
            TestCase.assertEquals(2, resList.size());
            resInt = 0;
            for(ZKBaseHelperTreeEntity item:resList){
                TestCase.assertNull(item.getChildren());
                if(".n1.n0".equals(item.getRemarks())){
                    resInt = resInt|1;
                }
                if(".n1.n1.n1".equals(item.getRemarks())){
                    resInt = resInt|(1<<1);
                }
            }
            TestCase.assertEquals(3, resInt);

        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }finally {
            for(ZKBaseHelperTreeEntity item : dels){
                dao.diskDel(item);
            }
        }
    }

    /** 一些计算 ******************************************************************************************/
    public int calculateNodeCount(int level, int nodeCount){
        int res = 0;
        for(int i = 1; i <= level; ++i){
            res = res + (int)Math.pow(nodeCount, i);
        }
        return res;
    }
    public void verifyTree(List<ZKBaseHelperTreeEntity> list, int level, int nodeCount){
        if(level > 0){
            TestCase.assertEquals(nodeCount, list.size());
            for(ZKBaseHelperTreeEntity item : list){
                verifyTree(item.getChildren(), level-1, nodeCount);
            }
        }else{
            for(ZKBaseHelperTreeEntity item : list){
                System.out.println("[^_^:20221017-1833-001] item.getChildren(): " + ZKJsonUtils.writeObjectJson(item.getChildren()));
                TestCase.assertTrue(item.getChildren().isEmpty());
            }
        }
    }

    /** 创建测试数据 ******************************************************************************/
    public static List<ZKBaseHelperTreeEntity> makeTestTree(ZKBaseHelperTreeDao treeDao, String namePrefix,
        int nodeCount, int level) {

        List<ZKBaseHelperTreeEntity> ms = new ArrayList<>();
        List<ZKBaseHelperTreeEntity> childMs = null, tempMs = null;
        int l = 0;
        do {
            if (childMs != null) {
                tempMs = childMs;
                childMs = new ArrayList<>();
                for (ZKBaseHelperTreeEntity m : tempMs) {
                    childMs.addAll(makeTestMenu(treeDao, m, namePrefix + ".l" + l, m.getRemarks(), nodeCount));
                }
            }
            else {
                childMs = new ArrayList<>();
                childMs.addAll(makeTestMenu(treeDao, null, namePrefix + ".l" + l, "", nodeCount));
            }
            ms.addAll(childMs);
            ++l;
        } while (l < level);
        return ms;
    }

    public static List<ZKBaseHelperTreeEntity> makeTestMenu(ZKBaseHelperTreeDao dao, ZKBaseHelperTreeEntity e,
        String namePrefix, String parentName, int nodeCount) {
        List<ZKBaseHelperTreeEntity> ms = new ArrayList<>();
        ZKBaseHelperTreeEntity tempE = null;

        for (int i = 0; i < nodeCount; ++i) {
            tempE = newMenu(namePrefix + parentName + ".n" + i);
            if (e != null) {
                tempE.setParentId(e.getPkId());
            }
            tempE.setmInt(i*1l);
            tempE.setRemarks(parentName + ".n" + i);
            tempE.preInsert();
            dao.insert(tempE);
            ms.add(tempE);
        }
        return ms;
    }

    public static ZKBaseHelperTreeEntity newMenu(String name) {
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
