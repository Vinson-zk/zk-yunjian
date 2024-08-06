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
 * @Title: ZKSerCenCertificateServiceTest.java 
 * @author Vinson 
 * @Package com.zk.server.central.service 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 7:33:59 PM 
 * @version V1.0   
*/
package com.zk.server.central.service;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.base.entity.ZKBaseEntity;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.core.encrypt.utils.ZKEncryptRsaUtils;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEncodingUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.utils.ZKMsgUtils;
import com.zk.server.central.ZKSerCenSpringBootMain;
import com.zk.server.central.entity.ZKSerCenCertificate;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSerCenCertificateServiceTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenCertificateServiceTest {

    private static ConfigurableApplicationContext ctx = ZKSerCenSpringBootMain.run(new String[] {});

    @Test
    public void testMsg() {
        try {
            String localeStr = "";
            String code = "";
            String msg = "";

            localeStr = "zh_CN";
            code = "zk.ser.cen.000012";
            msg = "账号不能为空。";
            TestCase.assertEquals(msg, ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), code));
            
            localeStr = "en_US";
            code = "zk.ser.cen.000012";
            msg = "The account cannot be empty.";
            TestCase.assertEquals(msg, ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), code));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试 ZKSerCenCertificateService 的 DML 方法
    @Test
    public void testDML() {

        // 保存下创建的 pkId，待测试完成，删除测试数据
        List<String> pkIdList = new ArrayList<>();
        ZKSerCenCertificateService service = null;
        try {
            service = ctx.getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(service);

            int result = 0;
            List<ZKSerCenCertificate> zkScResultList = new ArrayList<>();
            ZKSerCenCertificate zkSC = new ZKSerCenCertificate();
            ZKSerCenCertificate zkScResult = new ZKSerCenCertificate();
            zkSC.setServerName("test_server_name");
//            zkSC.setPublicKey("setPublicKey");
//            zkSC.setPrivateKey("setPrivateKey");

            /*** 插入 ***/
            result = 0;
            zkSC.setNewRecord(true);
            result = service.save(zkSC);
            pkIdList.add(zkSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            TestCase.assertEquals(1, result);
            zkScResult = service.get(zkSC);
            TestCase.assertNotNull(zkScResult);
            TestCase.assertNotNull(zkScResult.getCreateDate());
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Enable, zkScResult.getStatus().intValue());
            TestCase.assertEquals(zkSC.getPublicKey(), zkScResult.getPublicKey());
            TestCase.assertEquals(zkSC.getPrivateKey(), zkScResult.getPrivateKey());

            /*** 修改 ***/
            zkScResult.setValidStartDate(ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.setServerName("test_server_name_UPDATE");
            zkScResult.setStatus(1);
            result = 0;
            result = service.save(zkScResult);
            TestCase.assertEquals(1, result);
            zkScResult = service.get(zkSC);
            TestCase.assertNotNull(zkScResult);
            TestCase.assertNotNull(zkScResult.getUpdateDate());
            TestCase.assertEquals(zkSC.getStatus(), zkScResult.getStatus());
            TestCase.assertEquals("test_server_name", zkScResult.getServerName());
            TestCase.assertEquals(ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd),
                    zkScResult.getValidStartDate());
            TestCase.assertEquals(zkSC.getPublicKey(), zkScResult.getPublicKey());
            TestCase.assertEquals(zkSC.getPrivateKey(), zkScResult.getPrivateKey());

            /*** 修改状态 ***/
            zkScResult.setStatus(ZKSerCenCertificate.StatusType.Disabled);
            result = 0;
            result = service.updateStatus(zkScResult);
            TestCase.assertEquals(1, result);
            zkScResult = service.get(zkSC);
            TestCase.assertEquals(ZKSerCenCertificate.StatusType.Disabled, zkScResult.getStatus().intValue());
            TestCase.assertEquals("test_server_name", zkScResult.getServerName());
            TestCase.assertEquals(ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd),
                    zkScResult.getValidStartDate());
            TestCase.assertEquals(zkSC.getPublicKey(), zkScResult.getPublicKey());
            TestCase.assertEquals(zkSC.getPrivateKey(), zkScResult.getPrivateKey());

            /*** get 查询 ***/
            zkScResult = service.get(zkSC);
            TestCase.assertNotNull(zkScResult);

            /*** findList 查询 ***/
            zkSC = new ZKSerCenCertificate();
            zkSC.setServerName("test_server_name_1");
            zkSC.setPublicKey("setPublicKey");
            zkSC.setPrivateKey("setPrivateKey");
            zkSC.setValidStartDate(ZKDateUtils.parseDate("2019-08-09", ZKDateUtils.DF_yyyy_MM_dd));
            zkSC.setValidEndDate(ZKDateUtils.parseDate("2019-10-08", ZKDateUtils.DF_yyyy_MM_dd));
            result = 0;
            zkSC.setNewRecord(true);
            result = service.save(zkSC);
            pkIdList.add(zkSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            TestCase.assertEquals(1, result);

            zkSC = new ZKSerCenCertificate();
            zkSC.setServerName("test_server_name_2");
            zkSC.setPublicKey("setPublicKey");
            zkSC.setPrivateKey("setPrivateKey");
            zkSC.setValidStartDate(ZKDateUtils.parseDate("2019-08-10", ZKDateUtils.DF_yyyy_MM_dd));
            zkSC.setValidEndDate(ZKDateUtils.parseDate("2019-11-08", ZKDateUtils.DF_yyyy_MM_dd));
            result = 0;
            zkSC.setNewRecord(true);
            result = service.save(zkSC);
            pkIdList.add(zkSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            TestCase.assertEquals(1, result);

            // 有效期起始日期区间
            zkScResult = new ZKSerCenCertificate();
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_server_n");
            zkScResult.getExtraParams().put("validStartDateBegin",
                    ZKDateUtils.parseDate("2019-08-08", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validStartDateEnd",
                    ZKDateUtils.parseDate("2019-08-10", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResultList = service.findList(zkScResult);
            TestCase.assertEquals(3, zkScResultList.size());

            // 有效期结束日期区间
            zkScResult = new ZKSerCenCertificate();
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_server_n");
//            zkScResult.getExtraParams().put("validStartDateBegin",
//                    ZKDateUtils.parseDate("2019-09-05", ZKDateUtils.DF_yyyy_MM_dd));
//            zkScResult.getExtraParams().put("validStartDateEnd",
//                    ZKDateUtils.parseDate("2019-09-10", ZKDateUtils.DF_yyyy_MM_dd));

            zkScResult.getExtraParams().put("validEndDateBegin",
                    ZKDateUtils.parseDate("2019-10-05", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validEndDateEnd",
                    ZKDateUtils.parseDate("2019-10-20", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResultList = service.findList(zkScResult);
            TestCase.assertEquals(1, zkScResultList.size());
            // 有效期起始日期 到 有效期结束日期 之间
            zkScResult = new ZKSerCenCertificate();
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_server_n");
            zkScResult.getExtraParams().put("validStartDateBegin",
                    ZKDateUtils.parseDate("2019-08-05", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validEndDateEnd",
                    ZKDateUtils.parseDate("2019-11-20", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResultList = service.findList(zkScResult);
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
            zkScResult.setStatus(null);
            zkScResult.setServerName("test_server_n");
            zkScResult.getExtraParams().put("validStartDateBegin",
                    ZKDateUtils.parseDate("2019-08-09", ZKDateUtils.DF_yyyy_MM_dd));
            zkScResult.getExtraParams().put("validEndDateEnd",
                    ZKDateUtils.parseDate("2019-11-20", ZKDateUtils.DF_yyyy_MM_dd));
            page.setPageNo(2); // 返回第二页的数据
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(1, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_server_name_2", page.getResult().get(0).getServerName());

            page.setPageNo(0);// 设置 0 或 1 ；都是返回第1页的数据
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(1, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_server_name_1", page.getResult().get(0).getServerName());

            page.setPageNo(1);// 设置 0 或 1 ；都是返回第1页的数据
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(1, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_server_name_1", page.getResult().get(0).getServerName());

//            zkOrder = new ZKOrder("c_valid_start_date", ZKSortMode.DESC);
            zkOrder = new ZKOrder("validStartDate", ZKSortMode.DESC);
            zkOderList = new ArrayList<>();
            zkOderList.add(zkOrder);
            page.setSorters(zkOderList);
            page.setPageNo(2);
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(1, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());
            TestCase.assertEquals("test_server_name_1", page.getResult().get(0).getServerName());

            page.setPageNo(3); // 第三页没有数据
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(0, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());

            page.setPageNo(1); // 第1页数据
            page.setPageSize(8); // 每页 8 条数据
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(2, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());

            page.setPageNo(2); // 第2页没有数据
            page = service.findPage(page, zkScResult);
            TestCase.assertEquals(0, page.getResult().size());
            TestCase.assertEquals(2, page.getTotalCount());

            /*** 逻辑删除 ***/
            result = 0;
            zkScResult = service.get(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertNotNull(zkScResult);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.normal, zkScResult.getDelFlag().intValue());
            result = service.del(zkScResult);
            TestCase.assertEquals(1, result);
            zkScResult = service.get(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertNotNull(zkScResult);
            TestCase.assertEquals(ZKBaseEntity.DEL_FLAG.delete, zkScResult.getDelFlag().intValue());

            /*** 物理删除 ***/
            result = 0;
            result = service.diskDel(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertEquals(1, result);
            zkScResult = service.get(new ZKSerCenCertificate(pkIdList.get(0)));
            TestCase.assertNull(zkScResult);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (service != null) {
                for (String pkId : pkIdList) {
                    service.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

    @Test
    public void testScVlaid() {

        // 保存下创建的 pkId，待测试完成，删除测试数据
        List<String> pkIdList = new ArrayList<>();
        ZKSerCenCertificateService service = null;
        try {
            service = ctx.getBean(ZKSerCenCertificateService.class);
            TestCase.assertNotNull(service);
            ZKSerCenCertificate sSC = new ZKSerCenCertificate();
            ZKSerCenCertificate tSC = null;

            sSC.setServerName("test_server_name");

            /*** 插入 ***/
            sSC.setNewRecord(true);
            service.save(sSC);
            pkIdList.add(sSC.getPkId()); // 保存下创建的 pkId，待测试完成，删除测试数据
            tSC = service.get(sSC);
            TestCase.assertNotNull(tSC);
            TestCase.assertTrue(testScVlaid(sSC, tSC));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        } finally {
            if (service != null) {
                for (String pkId : pkIdList) {
                    service.diskDel(new ZKSerCenCertificate(pkId));
                }
            }
        }
    }

    /**
     * 测试 服务证书经过数据保存后，与刚生成时的证书 加解密是否一至。
     *
     * @Title: testSc
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Sep 10, 2019 4:47:35 PM
     * @return void
     */
    public static boolean testScVlaid(ZKSerCenCertificate sSc, ZKSerCenCertificate tSc) {

        String str = "天光乍破遇，暮雪白头老。寒剑默听奔雷，长枪独守空壕。";
        try {
            String encStr = null;
            String decStr = null;
            // 从数据取出的公钥加密
            byte[] publicKey = ZKEncodingUtils.decodeHex(tSc.getPublicKey());
            // 刚生成，未经数据库保存的私钥
            byte[] privateKey = ZKEncodingUtils.decodeHex(sSc.getPrivateKey());

            encStr = ZKEncodingUtils.encodeHex(ZKEncryptRsaUtils.encrypt(str.getBytes(), publicKey));

            decStr = new String(ZKEncryptRsaUtils.decrypt(ZKEncodingUtils.decodeHex(encStr), privateKey));

            return str.equals(decStr);

        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return false;

    }

}
