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
 * @Title: ZKJsonUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 3, 2019 3:06:51 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKJsonUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJsonUtilsTest {

    @Test
    public void testJsonStrToList() {
        try {
            String str = "[{\"test1\":1, \"test2\":\"test2\", \"test3\":\"test3\", \"test4\":\"4\"},{\"test1\":1, \"test2\":\"test2\", \"test3\":\"test3\", \"test4\":\"4\"}]";
            List<Map<String, Object>> resultList = ZKJsonUtils.parseList(str);
            TestCase.assertEquals(2, resultList.size());
            Map<String, Object> map = resultList.get(0);

            TestCase.assertEquals(1, map.get("test1"));
            TestCase.assertEquals("test2", map.get("test2"));
            TestCase.assertEquals("test3", map.get("test3"));
            TestCase.assertEquals("4", map.get("test4"));

        }
        catch(Exception e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testJsonStrToMap() {
        try {
            String str = "{\"test1\":1, \"test2\":\"test2\", \"test3\":\"test3\", \"test4\":\"4\"}";
            Map<?, ?> map = ZKJsonUtils.parseMap(str);
            TestCase.assertEquals(4, map.size());

            TestCase.assertEquals(1, map.get("test1"));
            TestCase.assertEquals("test2", map.get("test2"));
            TestCase.assertEquals("test3", map.get("test3"));
            TestCase.assertEquals("4", map.get("test4"));

        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteObjectJson() {
        try {
            List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
            Map<Object, Object> map = new HashMap<Object, Object>();
            map.put("test1", 1);
            map.put("test2", "2");
            map.put(3, "test3");
            list.add(map);

            map = new HashMap<Object, Object>();
            map.put("test1", 1);
            map.put("test2", "2");
            map.put(3, "test3");
            list.add(map);

            String resultStr = ZKJsonUtils.toJsonStr(list);
            System.out.println(resultStr);

            resultStr = ZKJsonUtils.toJsonStr(1);
            System.out.println(resultStr);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testWriteBeanJson() {
        try {
            List<Map<Object, Object>> list = new ArrayList<Map<Object, Object>>();
            Map<Object, Object> map = new HashMap<Object, Object>();
            map.put("test1", 1);
            map.put("test2", "2");
            map.put(3, "test3");
            list.add(map);

            map = new HashMap<Object, Object>();
            map.put("test1", 1);
            map.put("test2", "2");
            map.put(3, "test3");
            list.add(map);

            String resultStr = ZKJsonUtils.toJsonStr(list);
            System.out.println(resultStr);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
