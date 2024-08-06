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
 * @Title: ZKSqlPageTest.java 
 * @author Vinson 
 * @Package com.zk.db.mybatis.plugins 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 12:30:38 PM 
 * @version V1.0   
*/
package com.zk.db.mybatis.plugins;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.core.commons.data.ZKSortMode;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.db.ZKMybatisOperation;
import com.zk.db.ZKMybatisSessionFactory;
import com.zk.db.commons.ZKDBConstants;
import com.zk.db.helper.ZKDBTestConfig;
import com.zk.db.helper.dao.ZKDBTestDao;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSqlPageTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSqlPageTest {

    public static ZKMybatisSessionFactory mybatisSessionFactory = null;

    public static ZKMybatisSessionFactory getMybatisSessionFactory(){
        if(mybatisSessionFactory == null){
            mybatisSessionFactory = ZKDBTestConfig.getXmlConfigSessionFactory();
//            mybatisSessionFactory = ZKDBTestConfig.getJavaConfigSessionFactory();
        }
        return mybatisSessionFactory;
    }

    @Test
    public void testPageSample() {
        try {
            int count = 0;
            Map<String, Object> paramsMap = null;
            String value = "value_page_select_测试_";
            String remarks = "remarks_%-\\_备注";
            String specialStr;
            ZKJson json, json2, json3;

            Set<Long> mInts = new HashSet<>();

            System.out.println("=== 删除 =============================================== ");
            // 物理删除 t_test 表中所有数据，防脏数据干扰
            count = ZKMybatisOperation.delete(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".del", null);
            System.out.println("=== 插入 =============================================== ");

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
                paramsMap.put("value", value + i);
                paramsMap.put("remarks", remarks);
                paramsMap.put("json", json);
                count = 0;
                count = ZKMybatisOperation.insert(getMybatisSessionFactory().openSession(),
                        ZKDBTestDao.class.getName() + ".insert", paramsMap);
                TestCase.assertEquals(1, count);
            }
            System.out.println("=== 分页查询 =============================================== ");

            List<?> list = null;
            ZKPage<?> page = null;
            int pageSize = 0, pageNo = 0;
            List<ZKOrder> sorters = null;

            // list 查询所有，排序为 null
            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", value);
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);


            // 查询第二页，两个字段排序
            pageSize = 9;
            pageNo = 2;
            page = new ZKPage<>();
            sorters = new ArrayList<ZKOrder>();
            sorters.add(ZKOrder.asOrder("c_remarks", ZKSortMode.DESC));
            sorters.add(ZKOrder.asOrder("c_id", "DESC"));
            sorters.add(ZKOrder.asOrder("c_int", "sdaf"));
            page.setSorters(sorters);
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(page.getPageSize(), list.size());

            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".findListHaveCount", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(page.getPageSize(), list.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testPage() {
        try {
            int count = 0;
            Map<String, Object> paramsMap = null;
            String value = "value_page_select_测试_";
            String remarks = "remarks_%-\\_备注";
            String specialStr;
            ZKJson json, json2, json3;

            Set<Long> mInts = new HashSet<>();

            System.out.println("=== 删除 =============================================== ");
            // 物理删除 t_test 表中所有数据，防脏数据干扰
            count = ZKMybatisOperation.delete(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".del", null);
            System.out.println("=== 插入 =============================================== ");

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
                paramsMap.put("value", value + i);
                paramsMap.put("remarks", remarks);
                paramsMap.put("json", json);
                count = 0;
                count = ZKMybatisOperation.insert(getMybatisSessionFactory().openSession(),
                        ZKDBTestDao.class.getName() + ".insert", paramsMap);
                TestCase.assertEquals(1, count);
            }
            System.out.println("=== 分页查询 =============================================== ");

            List<?> list = null;
            ZKPage<?> page = null;
            int pageSize = 0, pageNo = 0;
            List<ZKOrder> sorters = null;

            // list 查询所有，排序为 null
            paramsMap = new HashMap<>();
            paramsMap.put("mInts", mInts);
            paramsMap.put("value", value);
            paramsMap.put("remarks", remarks);
            paramsMap.put("json", json);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-1]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, list.size());

            // 查询第一页，排序为 null
            page = new ZKPage<>();
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-2]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), list.size());

            // 查询第二页，排序为 null
            pageSize = 23;
            pageNo = 1;
            page = new ZKPage<>();
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(page.getPageSize(), list.size());
            // 查询最后一页，排序为 null
            pageSize = 23;
            pageNo = 2;
            page = new ZKPage<>();
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-4]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(insertCount - pageSize * pageNo, list.size());
            // 超出结果集，排序为 null
            pageSize = 23;
            pageNo = 4;
            page = new ZKPage<>();
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-4]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(0, list.size());

            sorters = new ArrayList<ZKOrder>();
            // 查询第二页，排序为空
            pageSize = 23;
            pageNo = 1;
            page = new ZKPage<>();
            page.setSorters(sorters);
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(page.getPageSize(), list.size());
            // 查询第二页，一字字段排序
            pageSize = 23;
            pageNo = 1;
            page = new ZKPage<>();
            sorters = new ArrayList<ZKOrder>();
            sorters.add(ZKOrder.asOrder("c_id", "asc"));
            page.setSorters(sorters);
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(page.getPageSize(), list.size());
            // 查询第二页，两个字段排序
            pageSize = 23;
            pageNo = 1;
            page = new ZKPage<>();
            sorters = new ArrayList<ZKOrder>();
            sorters.add(ZKOrder.asOrder("c_remarks", ZKSortMode.DESC));
            sorters.add(ZKOrder.asOrder("c_id", "DESC"));
            sorters.add(ZKOrder.asOrder("c_int", "sdaf"));
            page.setSorters(sorters);
            paramsMap.put(ZKDBConstants.PARAM_NAME.Page, page);
            page.setPageSize(pageSize);
            page.setPageNo(pageNo);
            list = ZKMybatisOperation.selectList(getMybatisSessionFactory().openSession(),
                    ZKDBTestDao.class.getName() + ".find", paramsMap);
            System.out.println("[^_^:2018030613-1232-001-3]" + ZKJsonUtils.toJsonStr(list));
            TestCase.assertEquals(insertCount, page.getTotalCount());
            TestCase.assertEquals(page.getPageSize(), pageSize);
            TestCase.assertEquals(page.getPageNo(), pageNo);
            TestCase.assertEquals(page.getPageSize(), list.size());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
