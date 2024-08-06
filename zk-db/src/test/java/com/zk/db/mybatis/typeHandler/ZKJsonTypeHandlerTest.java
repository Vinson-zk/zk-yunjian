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
 * @Title: ZKJsonTypeHandlerTest.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.typeHandler 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 12:29:49 PM 
 * @version V1.0   
*/
package com.zk.db.mybatis.typeHandler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.ZKMybatisOperation;
import com.zk.db.ZKMybatisSessionFactory;
import com.zk.db.helper.ZKDBTestConfig;
import com.zk.db.helper.dao.ZKDBTestDao;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;

import junit.framework.TestCase;

/**
 * @ClassName: ZKJsonTypeHandlerTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKJsonTypeHandlerTest {

    public static ZKMybatisSessionFactory mybatisSessionFactory = null;

    public static ZKMybatisSessionFactory getMybatisSessionFactory(){
        if(mybatisSessionFactory == null){
            mybatisSessionFactory = ZKDBTestConfig.getXmlConfigSessionFactory();
//            mybatisSessionFactory = ZKDBTestConfig.getJavaConfigSessionFactory();
        }
        return mybatisSessionFactory;
    }

    @Test
    public void testDml() {
        try {
            Map<String, Object> paramsMap = new HashMap<>();
            int count = 0;
            String id = "100";

            long mInt = 1;
            String value = "value_插入测试_" + id;
            String remarks = "remarks_备注";
            String updateStr = "value_插入测试_update_自恨寻芳到已迟，往年曾见未开时。";
            ZKJson json, json2, json3;
            String specialStr;
            List<Long> mInts = new ArrayList<>();
            mInts.add(mInt);
            mInts.add(2l);

            json = new ZKJson();
            specialStr = "~!@#$%^&*()_+{}|:\"<>?`1234567890-=[]\\;',.//*' --";
//          specialStr = "--";
            json.put("k0_null", null);
            json.put("k1_String", "v1[" + specialStr + "]v1 -end");
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
            json2.put("jk-1", "jv1[" + specialStr + "]jv1 -end");
            json2.put("jk-2", new String[] { specialStr, specialStr });
            json.put("jk-1-json", json2);
            json3 = new ZKJson();
            json.put("jk-2-json-empty", json3);

            paramsMap = new HashMap<>();
            paramsMap.put("id", id);
            paramsMap.put("id2", id);
            System.out.println("=== 删除 ================================== ");
            // 物理删除 t_test 表中所有数据，防脏数据干扰
            count = ZKMybatisOperation.delete(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".del", null);
            System.out.println("=== 插入 ================================== ");
            // 插入
            paramsMap.put("mInt", mInt);
            paramsMap.put("value", value);
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            count = 0;
            count = ZKMybatisOperation.insert(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".insert", paramsMap);
            TestCase.assertEquals(1, count);
            paramsMap.put("id", id + 1);
            count = 0;
            count = ZKMybatisOperation.insert(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".insert", paramsMap);
            TestCase.assertEquals(1, count);
            System.out.println("=== 修改 ================================== ");
            // 修改
            paramsMap = new HashMap<>();
            paramsMap.put("id", id);
            paramsMap.put("mInt", mInt);
            paramsMap.put("value", updateStr);
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            count = ZKMybatisOperation.update(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".update", paramsMap);
            TestCase.assertEquals(1, count);

            paramsMap.put("id", id + 2);
            paramsMap.put("id2", id + 2);
            json.put("otherKey", "otherKey");
            paramsMap.put("remarks", remarks);
            count = 0;
            count = ZKMybatisOperation.insert(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".insert", paramsMap);
            TestCase.assertEquals(1, count);

            System.out.println("=== 查询 ================================== ");
            // Json 查询；暂不支持数组的查询
            json = new ZKJson();
            json.put("k0_null", null);
            json.put("k1_String", "v1[" + specialStr + "]v1 -end");
            json.put("k2_Date", ZKDateUtils.parseDate("2018-03-11 21:53:59"));
            json.put("k3_int", 3);
            json.put("k4_long", 4l);
            json.put("k5_float", 5.5f);
            json.put("k6_double", 6.6d);
            json.put("k7_BigDecimal", new BigDecimal("7.7"));
            json.put("k8_BigInteger", new BigInteger("8"));
            json.put("k9_boolean", true);
//          json.put("k10_String_s", new String[]{specialStr, specialStr});
//          json.put("k10_Integer_s", new Integer[]{1, 2});
            json2 = new ZKJson();
            json2.put("jk-1", "jv1[" + specialStr + "]jv1 -end");
//          json2.put("jk2", new String[]{specialStr, specialStr});
            json.put("jk-1-json", json2);
            json3 = new ZKJson();
            json.put("jk-2-json-empty", json3);

//            json.put("a-b", "dd");

            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            List<?> list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:20180306-0904-001-1]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(3, list.size());

            @SuppressWarnings("unchecked")
            List<ZKDBTestSampleEntity> entityList = (List<ZKDBTestSampleEntity>) list;
            String expectedStr = "jv1[" + specialStr + "]jv1 -end";
            TestCase.assertEquals(expectedStr, ((Map<?, ?>) entityList.get(0).getJson().get("jk-1-json")).get("jk-1"));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
