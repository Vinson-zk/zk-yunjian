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
* @Title: testSampleDaoTest.java 
* @author Vinson 
* @Package com.zk.db.mybatis.dao 
* @Description: TODO(simple description this file what to do. ) 
* @date Sep 14, 2020 3:45:25 PM 
* @version V1.0 
*/
package com.zk.db.mybatis.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.helper.ZKDBSpringBootMain;
import com.zk.db.helper.ZKDBTestConfig;
import com.zk.db.helper.dao.ZKDBTestSampleDao;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDBTestSampleDaoTest
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBTestSampleDaoTest {

    @Test
    public void testDml() {

        ZKDBTestSampleDao testSampleDao = ZKDBTestConfig.getDynamicCtx().getBean(ZKDBTestSampleDao.class);
        TestCase.assertNotNull(testSampleDao);

        List<ZKDBTestSampleEntity> dels = new ArrayList<>();
        try {
            String specialCharacters = "/.,';\\\\][=-0987654321`?><\":|}{+_)(*&^%$#@!~)、。，‘’；、】【】=-0987654321·？》《》“”：|}{}+——）（）*&……%￥#@！~";
            List<ZKDBTestSampleEntity> resList;
            ZKDBTestSampleEntity entity = null;
            int result = 0;
            ZKDBTestSampleEntity tempE;
            ZKJson json = new ZKJson();
            json.put("key-1", "v-1");

            /** 插入/新增 */
            entity = new ZKDBTestSampleEntity();
            entity.setId("101");
            entity.setId2 ("101");
            entity.setmInt(1L);
            entity.setValue("TET");
            entity.setRemarks("SS");
            entity.setJson(json);
            entity.setmDate(new Date());
            entity.setmBoolean(false);
            result = testSampleDao.insert(entity);
            TestCase.assertEquals(1, result);
            dels.add(entity);

            /** get 查询 */
            System.out.println("[^_^:20220419-1524-001] sqlGet: " + entity.getSqlHelper().getSqlGet());
            tempE = testSampleDao.get(entity);
            TestCase.assertEquals(entity.getId(), tempE.getId());
            TestCase.assertEquals(entity.getmBoolean(), tempE.getmBoolean());
            TestCase.assertEquals(ZKDateUtils.formatDate(entity.getmDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm),
                    ZKDateUtils.formatDate(tempE.getmDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm));

            /** 修改 */
            entity.setRemarks("update-r");
//            entity.setmInt(2L);
            json.put("key-1", "update-v-1");
            json.put("key-2", "v-2");
            json.put("key-3", specialCharacters);
            entity.setJson(json);
            entity.setmDate(ZKDateUtils.parseDate("2020-05-20"));
            entity.setmBoolean(true);
            entity.setRemarks(specialCharacters);
            result = testSampleDao.update(entity);
            TestCase.assertEquals(1, result);
            tempE = testSampleDao.get(entity);
            TestCase.assertNotNull(tempE);
            TestCase.assertEquals(entity.getRemarks(), tempE.getRemarks());
            TestCase.assertEquals(entity.getmInt().intValue()+1, tempE.getmInt().intValue());
            TestCase.assertEquals(json.get("key-1"), tempE.getJson().get("key-1"));
            TestCase.assertEquals(json.get("key-2"), tempE.getJson().get("key-2"));
            TestCase.assertEquals(specialCharacters, tempE.getJson().get("key-3"));
            TestCase.assertEquals(entity.getmBoolean(), tempE.getmBoolean());
            TestCase.assertEquals(specialCharacters, tempE.getRemarks());
            TestCase.assertEquals(ZKDateUtils.formatDate(entity.getmDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm),
                    ZKDateUtils.formatDate(tempE.getmDate(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm));

            entity.setmInt(entity.getmInt() + 1);
            /** 列表查询 */
            // 1、boolean 查询测试
            entity.setmBoolean(false);
            System.out.println("[^_^:20221008-1510-001] ---: " + entity.getSqlHelper().getBlockSqlWhere());
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());
            entity.setmBoolean(true);
            resList = testSampleDao.findList(entity);
            TestCase.assertEquals(1, resList.size());

            // 2、ZKJson 查询测试
            json.put("key-2", "v-3");
            entity.setJson(json);
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());

            json.put("key-2", "v-2");
            entity.setJson(json);
            resList = testSampleDao.findList(entity);
            TestCase.assertEquals(1, resList.size());

            // 3、日期区间 查询测试
            entity.getExtraParams().put("startDate", ZKDateUtils.parseDate("2020-05-21"));
            entity.getExtraParams().put("endDate", ZKDateUtils.parseDate("2020-05-19"));
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());

            entity.getExtraParams().put("startDate", ZKDateUtils.parseDate("2020-05-19"));
            entity.getExtraParams().put("endDate", ZKDateUtils.parseDate("2020-05-21"));
            resList = testSampleDao.findList(entity);
            TestCase.assertEquals(1, resList.size());

            // 4、type in
            // int
            entity.setmInts(new Long[] {});
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(!resList.isEmpty());

            entity.setmInts(new Long[] { -1l, -2l, -3l });
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());

            entity.setmInts(new Long[] { -1l, 1l, 2l });
            resList = testSampleDao.findList(entity);
            resList = testSampleDao.findList(entity);
            TestCase.assertEquals(1, resList.size());

            // String
            entity.getExtraParams().put("mIntStrs", Arrays.asList("-1", "-2", "-3"));
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());

            entity.getExtraParams().put("mIntStrs", Arrays.asList("1", "2", "3"));
            resList = testSampleDao.findList(entity);
            TestCase.assertEquals(1, resList.size());

            /** Order by */
            ZKPage<ZKDBTestSampleEntity> page = new ZKPage<ZKDBTestSampleEntity>();
            entity.setPage(page);
            page.setSorters(Arrays.asList(ZKOrder.asOrder("id", "ASC"), ZKOrder.asOrder("type", "desc"),
                    ZKOrder.asOrder("value", "desc")));
            resList = testSampleDao.findList(entity);
            TestCase.assertEquals(1, resList.size());

            /** 逻辑删除; 这里无法测试 逻辑删除；因为表未设置逻辑删除；需要在定制项目中根据逻辑删除的设计进行测试 */

            /** 物理删除 */
            result = testSampleDao.diskDel(entity);
            TestCase.assertEquals(1, result);
            tempE = testSampleDao.get(entity);
            TestCase.assertNull(tempE);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            dels.forEach(item -> {
                testSampleDao.diskDel(item);
            });

        }
    }

    @Test
    public void testSelectList() {

        ZKDBTestSampleDao testSampleDao = ZKDBTestConfig.getDynamicCtx().getBean(ZKDBTestSampleDao.class);
        TestCase.assertNotNull(testSampleDao);

        try {
            List<ZKDBTestSampleEntity> resList;
            ZKDBTestSampleEntity entity = new ZKDBTestSampleEntity();
            ZKJson json = new ZKJson();
            json.put("key-1", "v-1");
            json.put("key-2", "v-2");
            json.put("key-3", "v-3; delete * from t_dd;\\");

            /* 插入/新增 */
            entity.setId("12");
            entity.setmInt(1L);
            entity.setValue("TET");
            entity.setRemarks("SS");
            entity.setJson(json);
//            entity.setmInts(new Long[] { 1l, 3l });
//            entity.getExtraParams().put("mIntStrs", Arrays.asList("str-1", "str-0"));
            entity.setmInts(null);
            entity.getExtraParams().clear();

            System.out.println("=============== " + entity.getSqlHelper().getBlockSqlSelelctList());

            /* 列表查询 */
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());

            entity.setmInts(new Long[] {});
            entity.getExtraParams().put("mIntStrs", Arrays.asList());

            /* 列表查询 */
            resList = testSampleDao.findList(entity);
            TestCase.assertTrue(resList.isEmpty());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testDemo() {

        ZKDBTestSampleDao testSampleDao = ZKDBTestConfig.getDynamicCtx().getBean(ZKDBTestSampleDao.class);
        TestCase.assertNotNull(testSampleDao);

        try {

            ////////////////////////////////////
            String pkId = "pkId";
            String updateUserId = "updateUserId";
            Date updateDate = new Date();

            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("pkId", pkId);
            paramMap.put("updateUserId", updateUserId);
            paramMap.put("updateDate", updateDate);

//            testSampleDao.demo(paramMap);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testGetById() {
        try {

            ConfigurableApplicationContext ctx = ZKDBSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKDBTestSampleDao testSampleDao = ctx.getBean(ZKDBTestSampleDao.class);
            TestCase.assertNotNull(testSampleDao);

            String id = "";
            ZKDBTestSampleEntity res = null;

            res = testSampleDao.getById(id, new Integer[]{1, 2});
            TestCase.assertNull(res);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
