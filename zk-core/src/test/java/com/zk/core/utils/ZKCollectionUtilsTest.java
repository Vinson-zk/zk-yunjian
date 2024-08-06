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
 * @Title: ZKCollectionUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:07:07 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKCollectionUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCollectionUtilsTest {

    static interface test_pn {
        public static final String pn_str = "pn_str";

        public static final String pn_strList = "pn_strList";

        public static final String pn_strArray = "pn_strArray";

        public static final String pn_entity = "pn_entity";

        public static final String pn_entityList = "pn_entityList";

        public static final String pn_entityArray = "pn_entityArray";

        public static final String pn_entityMap = "pn_entityMap";
    }

    static class Entity {
        String name;

        int age;

        public Entity(String name, int age) {
            this.name = name;
            this.age = age;
        }

        /**
         * @return name
         */
        public String getName() {
            return name;
        }

        /**
         * @param name
         *            the name to set
         */
        public void setName(String name) {
            this.name = name;
        }

        /**
         * @return age
         */
        public int getAge() {
            return age;
        }

        /**
         * @param age
         *            the age to set
         */
        public void setAge(int age) {
            this.age = age;
        }
    }

    @Test
    public void testMapToReqParametMap() {
        try {
            Map<String, Object> sourceMap = new HashMap<>();
            Map<String, String[]> targetMap = new HashMap<>();

            // 字符串参数
            sourceMap.put(test_pn.pn_str, test_pn.pn_str);
            // 字符串 Array 与 List 参数
            List<String> strList = Arrays.asList(test_pn.pn_strList, test_pn.pn_strList);
            sourceMap.put(test_pn.pn_strList, strList);
            String[] strArray = new String[] { test_pn.pn_strArray, test_pn.pn_strArray };
            sourceMap.put(test_pn.pn_strArray, strArray);

            // 实体参数
            sourceMap.put(test_pn.pn_entity + ".name", "name");
            sourceMap.put(test_pn.pn_entity + ".age", 2);

            // 实体 List
            Entity eListE1 = new Entity("name", 1);
            Entity eListE2 = new Entity("name", 2);
            List<Entity> eList = Arrays.asList(eListE1, eListE2);
            sourceMap.put(test_pn.pn_entityList, eList);

            // 实体 数组
            Entity eArrayE1 = new Entity("name", 1);
            Entity eArrayE2 = new Entity("name", 2);
            Entity[] eArray = new Entity[] { eArrayE1, eArrayE2 };
            sourceMap.put(test_pn.pn_entityArray, eArray);
            // 实体 Map
            Map<String, Entity> eMap = new HashMap<String, Entity>();
            Entity eMapE1 = new Entity("name", 1);
            Entity eMapE2 = new Entity("name", 2);
            eMap.put("eMape1", eMapE1);
            eMap.put("eMape2", eMapE2);
            sourceMap.put(test_pn.pn_entityMap, eMap);

            String jsonStr = ZKJsonUtils.toJsonStr(sourceMap);
            System.out.println("[^_^:20190625-1735-001] jsonStr: " + jsonStr);

            targetMap = ZKCollectionUtils.mapToReqParametMap(ZKJsonUtils.parseMap(jsonStr));
//            targetMap = ZKCollectionUtils.mapToReqParametMap(sourceMap);

            TestCase.assertEquals(test_pn.pn_str, targetMap.get(test_pn.pn_str)[0]);
            TestCase.assertEquals(test_pn.pn_strList, targetMap.get(test_pn.pn_strList)[0]);
            TestCase.assertEquals(test_pn.pn_strList, targetMap.get(test_pn.pn_strList)[1]);
            TestCase.assertEquals(test_pn.pn_strArray, targetMap.get(test_pn.pn_strArray)[0]);
            TestCase.assertEquals(test_pn.pn_strArray, targetMap.get(test_pn.pn_strArray)[1]);
            TestCase.assertEquals("name", targetMap.get(test_pn.pn_entity + ".name")[0]);
            TestCase.assertEquals("2", targetMap.get(test_pn.pn_entity + ".age")[0]);
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(eListE1), targetMap.get(test_pn.pn_entityList)[0]);
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(eListE2), targetMap.get(test_pn.pn_entityList)[1]);
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(eArrayE1), targetMap.get(test_pn.pn_entityArray)[0]);
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(eArrayE2), targetMap.get(test_pn.pn_entityArray)[1]);
            TestCase.assertEquals(ZKJsonUtils.toJsonStr(eMap), targetMap.get(test_pn.pn_entityMap)[0]);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testCutArray() {
        try {
            String[] strs = new String[] { "a", "b", "c" };
            String[] res = null;

            res = new String[3];
            ZKCollectionUtils.cutArray(strs, 0, 3, res);
            TestCase.assertEquals("a", res[0]);
            TestCase.assertEquals("b", res[1]);
            TestCase.assertEquals("c", res[2]);

            ZKCollectionUtils.cutArray(strs, 1, res);
            TestCase.assertEquals("b", res[0]);
            TestCase.assertEquals("c", res[1]);

            ZKCollectionUtils.cutArray(strs, 1, 3, res);
            TestCase.assertEquals("b", res[0]);
            TestCase.assertEquals("c", res[1]);

            ZKCollectionUtils.cutArray(strs, 1, 2, res);
            TestCase.assertEquals("b", res[0]);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
