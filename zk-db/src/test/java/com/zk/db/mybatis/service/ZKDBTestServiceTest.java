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
 * @Title: ZKDBTestServiceTest.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic.service 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:35:42 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis.service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import com.zk.db.helper.entity.ZKDBTestSampleEntity;
import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.db.helper.ZKDBSpringBootMain;
import com.zk.db.helper.service.ZKDBTestService;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDBTestServiceTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBTestServiceTest {

    @Test
    public void test() {
        try {

            ConfigurableApplicationContext ctx = ZKDBSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKDBTestService zkTestService = ctx.getBean(ZKDBTestService.class);
            TestCase.assertNotNull(zkTestService);

            String specialStr;
            specialStr = "~!@#$%^&*()_+{}|:\"<>?`1234567890-=[]\\;',.//*' --";
//            specialStr = "1";
//          specialStr = "--";
            String id = "100";
            String value = "value_插入测试_[" + id + "] " + specialStr;
            String remarks = "remarks_备注_" + specialStr;
            String updateStr = "value_插入测试_update_自恨寻芳到已迟，往年曾见未开时。" + specialStr;
            ZKJson json, json2, json3;

            long type = 1;
            List<Long> types = new ArrayList<>();
            types.add(type);
            types.add(2l);

            json = new ZKJson();
            json.put("k0_null", null);
            json.put("k1_String", value);
            json.put("k2_Date", ZKDateUtils.parseDate("2018-03-11 21:53:59"));
            json.put("k3_int", 3);
            json.put("k4_long", 4l);
            json.put("k5_float", 5.5f);
            json.put("k6_double", 6.6d);
            json.put("k7_BigDecimal", new BigDecimal("7.7"));
            json.put("k8_BigInteger", new BigInteger("8"));
            json.put("k9_boolean", true);
            json.put("k10_String_s", new String[] { specialStr, specialStr });
            json.put("k10_Integer_s", new Integer[] { 1, 2 });
            json2 = new ZKJson();
            json2.put("jk1", "jv1[" + specialStr + "]jv1 -end");
            json2.put("jk2", new String[] { specialStr, specialStr });
            json.put("jk1_json", json2);
            json3 = new ZKJson();
            json.put("jk2_json_empty", json3);

            ZKDBTestSampleEntity zkTestEntity = new ZKDBTestSampleEntity();

            zkTestEntity.setJson(json);
            zkTestEntity.setmInt(type);
            zkTestEntity.setValue(value);
            zkTestEntity.setRemarks(remarks);
            zkTestEntity.setId(id);
            zkTestEntity.setId2(id);
            /*** 先删除，防止旧数据干扰 ****/
            zkTestService.del(id);

            int result = 0;
            ZKDBTestSampleEntity zkTe;
            List<ZKDBTestSampleEntity> zkTestEntityList = null;
            /*** insert ***/
            result = zkTestService.insert(zkTestEntity);
            TestCase.assertEquals(1, result);

            zkTe = zkTestService.get(id);
            TestCase.assertEquals(value, zkTe.getValue());
            TestCase.assertEquals(value, zkTe.getJson().get("k1_String"));

            /*** update ***/
            zkTestEntity.setValue(updateStr);
            json.put("k1_String", updateStr);
            result = zkTestService.update(zkTestEntity);
            TestCase.assertEquals(1, result);

            zkTe = zkTestService.get(id);
            TestCase.assertEquals(updateStr, zkTe.getValue());
            TestCase.assertEquals(updateStr, zkTe.getJson().get("k1_String"));

            /*** find ***/
            // 暂不支持 数组匹配查询
            json.remove("k10_String_s");
            json.remove("k10_Integer_s");
            json2.remove("jk2");
            zkTestEntityList = null;
            zkTestEntityList = zkTestService.find(zkTestEntity);
            TestCase.assertEquals(1, zkTestEntityList.size());
            zkTestEntity = zkTestEntityList.get(0);
            TestCase.assertEquals(updateStr, zkTestEntity.getValue());
            TestCase.assertEquals(updateStr, zkTestEntity.getJson().get("k1_String"));

            /*** del ***/
            result = 0;
            result = zkTestService.del(id);
            TestCase.assertEquals(1, result);

            zkTestEntityList = null;
            zkTestEntityList = zkTestService.find(zkTestEntity);
            TestCase.assertEquals(0, zkTestEntityList.size());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testTransactional() {
        try {

            ConfigurableApplicationContext ctx = ZKDBSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKDBTestService zkTestService = ctx.getBean(ZKDBTestService.class);
            TestCase.assertNotNull(zkTestService);

            String id = "100";
            String value = "value_插入测试_[" + id + "] ";
            String remarks = "remarks_备注_";
            long type = 1;

            ZKDBTestSampleEntity zkTestEntity = new ZKDBTestSampleEntity();

            zkTestEntity.setmInt(type);
            zkTestEntity.setRemarks(remarks);
            zkTestEntity.setId(id);
            zkTestEntity.setId2(id);
            /*** 先删除，防止旧数据干扰 ****/
            zkTestService.del(id);

            int result = 0;
            ZKDBTestSampleEntity zkTe;
            /*** insert 插入失败 ***/
            try {
                zkTestEntity.setValue(null);
                result = zkTestService.insertErrorValueNull(zkTestEntity);
                TestCase.assertEquals(1, result);
                TestCase.assertTrue(false);
            }
            catch(NullPointerException e) {
                TestCase.assertTrue(true);
            }

            zkTe = zkTestService.get(id);
            // 插入失败，数据不存在
            TestCase.assertNull(zkTe);

            /*** insert 插入正常 ***/
            zkTestEntity.setValue(value);
            result = zkTestService.insertErrorValueNull(zkTestEntity);
            TestCase.assertEquals(1, result);
            zkTe = zkTestService.get(id);
            // 插入成功，数据存在
            TestCase.assertNotNull(zkTe);
            TestCase.assertEquals(value, zkTe.getValue());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    // 测试同一个事务中未提交时，查出的数据是否是新数据
    @Test
    public void testTransactional2() {
        try {

            ConfigurableApplicationContext ctx = ZKDBSpringBootMain.run(new String[] {});
            TestCase.assertNotNull(ctx);

            ZKDBTestService zkTestService = ctx.getBean(ZKDBTestService.class);
            TestCase.assertNotNull(zkTestService);

            String id = "100";
            String value = "value_插入测试_[" + id + "] ";
            String remarks = "remarks_备注_";
            long type = 1;

            /*** 先删除，防止旧数据干扰 ****/
            zkTestService.del(id);

            int result = 0;
            ZKDBTestSampleEntity zkTe;

            /*** insert 插入正常 ***/
            zkTe = new ZKDBTestSampleEntity();
            zkTe.setmInt(type);
            zkTe.setRemarks(remarks);
            zkTe.setId(id);
            zkTe.setId2(id);
            zkTe.setValue(value);
            result = zkTestService.insert(zkTe);
            TestCase.assertEquals(1, result);

            /*** 修改 ***/
            zkTe = zkTestService.get(id);
            value = "这是修改值";
            type = 3;
            zkTe.setValue(value);
            zkTe.setmInt(type);
            zkTe = zkTestService.updateTransactional(zkTe);
            TestCase.assertNotNull(zkTe);
            TestCase.assertEquals(value, zkTe.getValue());
            TestCase.assertEquals(type, zkTe.getmInt().longValue());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
