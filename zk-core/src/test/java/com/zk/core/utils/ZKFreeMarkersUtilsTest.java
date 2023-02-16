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
* @Title: ZKFreeMarkersUtilsTest.java 
* @author Vinson 
* @Package com.zk.core.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date May 24, 2022 9:09:52 AM 
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
* @ClassName: ZKFreeMarkersUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKFreeMarkersUtilsTest {

    @Test
    public void testRenderList() {
        try {
            String templateString = "";
            String expecteStr = "";
            String transferString = null;
            List<Object> targetList = null;
            Map<String, Object> modelMap = new HashMap<String, Object>();

            // 1 ====
            targetList = Arrays.asList("a", "b");
            modelMap.put("list", targetList);
            templateString = "<#list list as item>index:${item?index}, value: ${item}; </#list>";
            expecteStr = "index:0, value: a; index:1, value: b; ";
            transferString = ZKFreeMarkersUtils.renderString(templateString, modelMap);
            System.out.println("[^_^: 20220127-0826-001] " + transferString);
            TestCase.assertEquals(expecteStr, transferString);

            // 2 ====
            targetList = Arrays.asList("a", "b", "c", "d");
            modelMap.put("list", targetList);
            templateString = "<#list list as item>index:${item?index},value:${item},<#if (item?index)%2 == 0>双数位<#else>单数位</#if>-<#if item?has_next>有下一个<#else>无下一个</#if>; </#list>";
            transferString = ZKFreeMarkersUtils.renderString(templateString, modelMap);
            System.out.println("[^_^: 20220127-0826-002] " + transferString);
            expecteStr = "index:0,value:a,双数位-有下一个; index:1,value:b,单数位-有下一个; index:2,value:c,双数位-有下一个; index:3,value:d,单数位-无下一个; ";
            TestCase.assertEquals(expecteStr, transferString);

            // 3 ====
            targetList = Arrays.asList("a", "b", "c", "d");
            modelMap.put("list", targetList);
            templateString = "<#list list as item>index:${item?index},value:${item},<#if (item?index)%2 == 0>双数位<#else>单数位</#if>-<#if item?has_next == false>无下一个<#else>有下一个</#if>; </#list>";
            transferString = ZKFreeMarkersUtils.renderString(templateString, modelMap);
            System.out.println("[^_^: 20220127-0826-002] " + transferString);
            expecteStr = "index:0,value:a,双数位-有下一个; index:1,value:b,单数位-有下一个; index:2,value:c,双数位-有下一个; index:3,value:d,单数位-无下一个; ";
            TestCase.assertEquals(expecteStr, transferString);

            // 4 ====
            targetList = Arrays.asList("a", "b", "c");
            modelMap.put("list", targetList);
            templateString = "<#assign col=''><#list list as item>${list[item?index]};<#if col == \"\">col:不存在; </#if><#assign col=\"${item}\"></#list>col:${col};";
            transferString = ZKFreeMarkersUtils.renderString(templateString, modelMap);
            System.out.println("[^_^: 20220127-0826-002] " + transferString);
            expecteStr = "a;col:不存在; b;c;col:c;";
            TestCase.assertEquals(expecteStr, transferString);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
