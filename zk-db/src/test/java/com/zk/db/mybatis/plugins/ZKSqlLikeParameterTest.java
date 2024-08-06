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
 * @Title: ZKSqlLikeParameterTest.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.plugins 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 12:30:23 PM 
 * @version V1.0   
*/
package com.zk.db.mybatis.plugins;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.zk.db.helper.dao.ZKDBTestDao;
import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.ZKMybatisOperation;
import com.zk.db.ZKMybatisSessionFactory;
import com.zk.db.helper.ZKDBTestConfig;

import junit.framework.TestCase;

/**
 * @ClassName: ZKSqlLikeParameterTest
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKSqlLikeParameterTest {

    public static ZKMybatisSessionFactory mybatisSessionFactory = null;

    public static ZKMybatisSessionFactory getMybatisSessionFactory(){
        if(mybatisSessionFactory == null){
            mybatisSessionFactory = ZKDBTestConfig.getXmlConfigSessionFactory();
//            mybatisSessionFactory = ZKDBTestConfig.getJavaConfigSessionFactory();
        }
        return mybatisSessionFactory;
    }

    @Test
    public void testLikeParameter() {
        try {
            int count = 0;
            Map<String, Object> paramsMap = null;
            String value = "value_likeParameter_测试_";
            String remarks = "remarks_备注";
            String specialStr;
            ZKJson json, json2, json3;

            Set<Long> mInts = new HashSet<>();

            System.out
                    .println("=== 删除 ============================================================================== ");
            // 物理删除 t_test 表中所有数据，防脏数据干扰
            count = ZKMybatisOperation.delete(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".del", null);
            System.out
                    .println("=== 插入 ============================================================================== ");

            json = new ZKJson();
            specialStr = "~!@#$%^&*()_+{}|:\"<>?`1234567890-=[]\\;',.//*' --";
//          specialStr = "-";
            json.put("k0_null", null);
            json.put("k1_String", "v1[" + specialStr + "]v1 -end");
            json.put("k2_Date", ZKDateUtils.parseDate("2018-03-11 21:53:59"));
            json.put("k3_int", 3);
            json.put("k4_long", 4l);
            json.put("k5_float", 5.5f);
            json.put("k6_double", 6.6d);
//          json.put("k7_BigDecimal", new BigDecimal("7.7"));
//          json.put("k8_BigInteger", new BigInteger("8"));
            json.put("k9_boolean", true);
//          json.put("k10_String_s", new String[]{specialStr, specialStr});
//          json.put("k10_Integer_s", new Integer[]{1, 2});
            json2 = new ZKJson();
            json2.put("jk1", "jv1[" + specialStr + "]jv1 -end");
//          json2.put("jk2", new String[]{specialStr, specialStr});
            json.put("jk1_json", json2);
            json3 = new ZKJson();
            json.put("jk2_json_empty", json3);

            int insertCount = 67;
            for (int i = 1; i <= insertCount; ++i) {
                paramsMap = new HashMap<>();
                paramsMap.put("id", i + "");
                paramsMap.put("id2", i + "");
                paramsMap.put("mInt", i % 9l);
                mInts.add(i % 9l);
                paramsMap.put("value", value + i + "-%--您好 " + i + "\\" + i + " end");
                paramsMap.put("remarks", remarks);
                paramsMap.put("json", json);
                count = 0;
                count = ZKMybatisOperation.insert(getMybatisSessionFactory().openSession(),
                        ZKDBTestDao.class.getName() + ".insert", paramsMap);
                TestCase.assertEquals(1, count);
            }
            System.out.println(
                    "=== 分页查询 ============================================================================== ");
            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", value);
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            List<?> list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-1]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, list.size());

            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", "_1");
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-2]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(11, list.size());

            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", "1-%");
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(7, list.size());

            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", "%1-");
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(0, list.size());

            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", "\\");
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(67, list.size());

            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", "_61-%--您好 61\\");
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-4]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(1, list.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
