/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKSerCenCertificateDaoTest.java 
 * @author Vinson 
 * @Package com.zk.server.central.dao 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:33:17 PM 
 * @version V1.0   
*/
package com.zk.server.central.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.server.central.ZKSerCenSpringBootMain;
import com.zk.server.central.entity.ZKSerCenCertificate;
import com.zk.server.central.service.ZKSerCenCertificateService;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSerCenCertificateDaoTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenCertificateDaoTest {
    
    private static ConfigurableApplicationContext ctx = ZKSerCenSpringBootMain.run(new String[] {});

    // 测试 ZKSerCenCertificateDao 的 DML 方法
    @Test
    public void testDML() {

        // 保存下创建的 pkId，待测试完成，删除测试数据
        List<String> pkIdList = new ArrayList<>();
        ZKSerCenCertificateDao dao = null;
        try {
            dao = ctx.getBean(ZKSerCenCertificateDao.class);
            ZKSerCenCertificateService s = ctx.getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(dao);

            int result = 0;
            List<ZKSerCenCertificate> zkScResultList = new ArrayList<>();
            ZKSerCenCertificate zkSC = new ZKSerCenCertificate();
            ZKSerCenCertificate zkScResult = new ZKSerCenCertificate();

            zkSC.setServerName("test_dao_name");
            zkSC.setPublicKey("setPublicKey");
            zkSC.setPrivateKey("setPrivateKey");

            /*** 插入 ***/
            result = 0;
//            zkSC.preInsert();
            s.preInsert(zkSC);
            result = dao.insert(zkSC);
            pkIdList.add(zkSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            TestCase.assertEquals(1, result);
            zkScResult = dao.get(zkSC);
            TestCase.assertNotNull(zkScResult);
            TestCase.assertNotNull(zkScResult.getCreateDate());
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Enable, zkScResult.getStatus().intValue());

            /*** 修改 ***/
            zkScResult.setValidStartDate(ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.setServerName("test_dao_name_UPDATE");
            zkScResult.setStatus(4);
//            zkScResult.preUpdate();
            s.preUpdate(zkScResult);
            result = 0;
            result = dao.update(zkScResult);
            TestCase.assertEquals(1, result);
            zkScResult = dao.get(zkSC);
            TestCase.assertNotNull(zkScResult);
            TestCase.assertNotNull(zkScResult.getUpdateDate());
            TestCase.assertEquals(zkSC.getStatus(), zkScResult.getStatus());
            TestCase.assertEquals("test_dao_name", zkScResult.getServerName());
            TestCase.assertEquals(ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd),
                    zkScResult.getValidStartDate());

            /*** 修改状态 ***/
            zkScResult.setStatus(ZKSerCenCertificate.StatusType.Disabled);
            result = 0;
            result = dao.updateStatus(zkScResult);
            TestCase.assertEquals(1, result);
            zkScResult = dao.get(zkSC);
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Disabled, zkScResult.getStatus().intValue());
            TestCase.assertEquals("test_dao_name", zkScResult.getServerName());
            TestCase.assertEquals(ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd),
                    zkScResult.getValidStartDate());

            /*** get 查询 ***/
            zkScResult = dao.get(zkSC);
            TestCase.assertNotNull(zkScResult);

            /*** findList 查询 ***/
            zkSC = new ZKSerCenCertificate();
            zkSC.setServerName("test_dao_name_1");
            zkSC.setPublicKey("setPublicKey");
            zkSC.setPrivateKey("setPrivateKey");
            zkSC.setValidStartDate(ZKDateUtils.parseDate("2019-08-09", ZKDateUtils.DF_yyyy_MM_dd));
            zkSC.setValidEndDate(ZKDateUtils.parseDate("2019-10-08", ZKDateUtils.DF_yyyy_MM_dd));
            result = 0;
//            zkSC.preInsert();
            s.preInsert(zkSC);
            result = dao.insert(zkSC);
            pkIdList.add(zkSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            TestCase.assertEquals(1, result);

            zkSC = new ZKSerCenCertificate();
            zkSC.setServerName("test_dao_name_2");
            zkSC.setPublicKey("setPublicKey");
            zkSC.setPrivateKey("setPrivateKey");
            zkSC.setValidStartDate(ZKDateUtils.parseDate("2019-08-10", ZKDateUtils.DF_yyyy_MM_dd));
            zkSC.setValidEndDate(ZKDateUtils.parseDate("2019-11-08", ZKDateUtils.DF_yyyy_MM_dd));
            result = 0;
//            zkSC.preInsert();
            s.preInsert(zkSC);
            result = dao.insert(zkSC);
            pkIdList.add(zkSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            TestCase.assertEquals(1, result);

            // 有效期起始日期区间
            zkScResult = new ZKSerCenCertificate();
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_dao_n");
            zkScResult.getExtraParams().put("validStartDateBegin",
                    ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validStartDateEnd",
                    ZKDateUtils.parseDate("2019-08-12", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResultList = dao.findList(zkScResult);
            System.out
                    .println("[^_^:20190825-1350-001] zkScResultList: " + ZKJsonUtils.toJsonStr(zkScResultList));
            TestCase.assertEquals(3, zkScResultList.size());

            // 有效期结束日期区间
            zkScResult = new ZKSerCenCertificate();
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_dao_n");
//            zkScResult.getExtraParams().put("validStartDateBegin",
//                    ZKDateUtils.parseDate("2019-09-05", ZKDateUtils.DF_yyyy_MM_dd));
//            zkScResult.getExtraParams().put("validStartDateEnd",
//                    ZKDateUtils.parseDate("2019-09-10", ZKDateUtils.DF_yyyy_MM_dd));

            zkScResult.getExtraParams().put("validEndDateBegin",
                    ZKDateUtils.parseDate("2019-10-05", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validEndDateEnd",
                    ZKDateUtils.parseDate("2019-10-20", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResultList = dao.findList(zkScResult);

            System.out
                    .println("[^_^:20190825-1350-002] zkScResultList: " + ZKJsonUtils.toJsonStr(zkScResultList));
            TestCase.assertEquals(1, zkScResultList.size());
            // 有效期起始日期 到 有效期结束日期 之间
            zkScResult = new ZKSerCenCertificate();
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_dao_n");
            zkScResult.getExtraParams().put("validStartDateBegin",
                    ZKDateUtils.parseDate("2019-08-05", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validEndDateEnd",
                    ZKDateUtils.parseDate("2019-11-20", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResultList = dao.findList(zkScResult);
            System.out
                    .println("[^_^:20190825-1350-003] zkScResultList: " + ZKJsonUtils.toJsonStr(zkScResultList));
            TestCase.assertEquals(2, zkScResultList.size());

            /*** 分页查询 ***/
            System.out.println("[^_^:20190825-2353-001] ---------- 分页查询 ");
            System.out.println("[^_^:20190825-2353-001] ---------- 分页查询 ");
            System.out.println("[^_^:20190825-2353-001] ---------- 分页查询 ");
            List<ZKOrder> zkOderList = null;
            ZKOrder zkOrder = new ZKOrder("c_valid_start_date", ZKSortMode.ASC);
            ZKPage<ZKSerCenCertificate> page = new ZKPage<>();
            page.setPageSize(1); // 每页 1 条数据
            zkOderList = new ArrayList<>();
            zkOderList.add(zkOrder);
            page.setSorters(zkOderList);

            zkScResult = new ZKSerCenCertificate();
            zkScResult.setPage(page);
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_dao_n");
            zkScResult.getExtraParams().put("validStartDateBegin",
                    ZKDateUtils.parseDate("2019-08-09", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validEndDateEnd",
                    ZKDateUtils.parseDate("2019-11-20", ZKDateUtils.DF_yyyy_MM_dd));
            page.setPageNo(2); // 返回第二页的数据
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(1, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_dao_name_2", zkScResultList.get(0).getServerName());

            page.setPageNo(0);// 设置 0 或 1 ；都是返回第1页的数据
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(1, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_dao_name_1", zkScResultList.get(0).getServerName());

            page.setPageNo(1);// 设置 0 或 1 ；都是返回第1页的数据
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(1, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_dao_name_1", zkScResultList.get(0).getServerName());

//            zkOrder = new ZKOrder("c_valid_start_date", ZKSortMode.DESC);
            zkOrder = new ZKOrder("validStartDate", ZKSortMode.DESC);
            zkOderList = new ArrayList<>();
            zkOderList.add(zkOrder);
            page.setSorters(zkOderList);
            page.setPageNo(2);
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(1, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_dao_name_1", zkScResultList.get(0).getServerName());

            page.setPageNo(3); // 第三页没有数据
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(0, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());

            page.setPageNo(1); // 第1页数据
            page.setPageSize(8); // 每页 8 条数据
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(2, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());

            page.setPageNo(2); // 第2页没有数据
            zkScResultList = dao.findList(zkScResult);
            TestCase.assertEquals(0, zkScResultList.size());
            TestCase.assertEquals(2, page.getTotalCount());

            /*** 逻辑删除 ***/
            result = 0;
            zkScResult = dao.get(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertNotNull(zkScResult);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.normal, zkScResult.getDelFlag().intValue());
            zkScResult.setDelFlag(ZKBaseEntity.DEL_FLAG.delete);
            result = dao.del(zkScResult);
            TestCase.assertEquals(1, result);
            zkScResult = dao.get(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertNotNull(zkScResult);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, zkScResult.getDelFlag().intValue());

            /*** 物理删除 ***/
            result = 0;
            result = dao.diskDel(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertEquals(1, result);
            zkScResult = dao.get(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertNull(zkScResult);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (dao != null) {
                for (String pkId : pkIdList) {
                    dao.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

}
