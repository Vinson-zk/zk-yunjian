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
* @Title: ZKSecDefaultFilterChainManagerTest.java 
* @author Vinson 
* @Package com.zk.security.web.mgt 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 3:02:18 PM 
* @version V1.0 
*/
package com.zk.security.web.filter.common;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.zk.security.web.filter.ZKSecFilter;
import com.zk.security.web.filter.authc.ZKSecAnonymousFilter;
import com.zk.security.web.filter.authc.ZKSecAuthcDevFilter;
import com.zk.security.web.filter.authc.ZKSecAuthcUserFilter;
import com.zk.security.web.filter.authc.ZKSecDevFilter;
import com.zk.security.web.filter.authc.ZKSecLogoutFilter;
import com.zk.security.web.filter.authc.ZKSecUserFilter;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecDefaultFilterChainManagerTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultFilterChainManagerTest {

    @Test
    public void testFilterChainDefinition() {
        try {
            ZKSecDefaultFilterChainManager manager = new ZKSecDefaultFilterChainManager();
            manager.addDefaultFilters();

            Map<String, String> filterChainDefinitionMap = new HashMap<>();
            filterChainDefinitionMap.put("test/anon", "anon");
            filterChainDefinitionMap.put("test/login", "authcUser");
            filterChainDefinitionMap.put("test/logout", "logout");
            filterChainDefinitionMap.put("test/**", "user,dev");

            for (Map.Entry<String, String> entry : filterChainDefinitionMap.entrySet()) {
                String url = entry.getKey();
                String chainDefinition = entry.getValue();
                manager.createChain(url, chainDefinition);
            }

            String urlPath = "";
            ZKSecFilterChain secFilterChain = null;
            List<ZKSecFilter> ls = null;

            urlPath = "test/login";
            secFilterChain = (ZKSecFilterChain) manager.proxy(null, urlPath);
            ls = secFilterChain.getFilters();
            if (ls.get(0) instanceof ZKSecAuthcUserFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            urlPath = "test/**";
            secFilterChain = (ZKSecFilterChain) manager.proxy(null, urlPath);
            ls = secFilterChain.getFilters();
            int i = 0;
            for (ZKSecFilter f : ls) {
                if (f instanceof ZKSecUserFilter) {
                    i = i | 1;
                }
                if (f instanceof ZKSecDevFilter) {
                    i = i | 2;
                }
                if (f instanceof ZKSecUserFilter) {
                    i = i | 4;
                }
            }
            TestCase.assertEquals(7, i);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testAddFilter() {
        try {
            ZKSecDefaultFilterChainManager manager = new ZKSecDefaultFilterChainManager();

            String filterName = "anon";

            manager.addDefaultFilters();
            ZKSecFilter filter = manager.getFilter(filterName);
            if (filter instanceof ZKSecAnonymousFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            Map<String, ZKSecFilter> filterMap = new HashMap<>();
            filterMap.put(filterName, new ZKSecAuthcUserFilter());

            manager.addFilter(filterName, filterMap.get(filterName), false, false);
            filter = manager.getFilter(filterName);
            if (filter instanceof ZKSecAnonymousFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            manager.addFilter(filterName, filterMap.get(filterName));
            filter = manager.getFilter(filterName);
            if (filter instanceof ZKSecAuthcUserFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testDefaultFilter() {
        try {
            ZKSecDefaultFilterChainManager manager = new ZKSecDefaultFilterChainManager();
            manager.addDefaultFilters();

            ZKSecFilter filter = manager.getFilter("anon");
            if (filter instanceof ZKSecAnonymousFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            filter = manager.getFilter("authcUser");
            if (filter instanceof ZKSecAuthcUserFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            filter = manager.getFilter("authcDev");
            if (filter instanceof ZKSecAuthcDevFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }


            filter = manager.getFilter("user");
            if (filter instanceof ZKSecUserFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            filter = manager.getFilter("dev");
            if (filter instanceof ZKSecDevFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

            filter = manager.getFilter("logout");
            if (filter instanceof ZKSecLogoutFilter) {
                TestCase.assertTrue(true);
            }
            else {
                TestCase.assertTrue(false);
            }

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
