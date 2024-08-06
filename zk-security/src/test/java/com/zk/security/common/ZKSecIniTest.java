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
* @Title: ZKSecIniTest.java 
* @author Vinson 
* @Package com.zk.security.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 2:17:39 PM 
* @version V1.0 
*/
package com.zk.security.common;

import org.junit.Test;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import com.zk.core.utils.ZKCollectionUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecIniTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecIniTest {

    private static final String iniStrPath = "classpath:common/test_zk_sec_ini.xml";

    public static FileSystemXmlApplicationContext ctx;

    static {
        try {
            ctx = new FileSystemXmlApplicationContext(iniStrPath);
            TestCase.assertNotNull(ctx);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testIni() {
        try {
            String iniStr = ctx.getBean("filterChainDefinitions", String.class);
            System.out.println("=================== \n" + iniStr);

            ZKSecIni ini = new ZKSecIni();
            ini.load(iniStr);
            ZKSecIni.Section section = ini.getSection(ZKSecIni.URLS);
            if (ZKCollectionUtils.isEmpty(section)) {
                // no urls section. Since this _is_ a urls chain definition
                // property, just assume the
                // default section contains only the definitions:
                section = ini.getSection(ZKSecIni.DEFAULT_SECTION_NAME);
            }
            TestCase.assertTrue(!ZKCollectionUtils.isEmpty(section));

            /*
             * uri/anon=anon uri/uri/login = validCodeFilter,formAuthcFilter
             * uri/uri/logout = logoutFilter uri/uri/** = userFilter
             */

            String key = null;
            String value = "";

            key = "uri/anon";
            value = "anon";
            TestCase.assertEquals(value, section.get(key));

            key = "uri/uri/login";
            value = "validCodeFilter,formAuthcFilter";
            TestCase.assertEquals(value, section.get(key));

            key = "uri/uri/logout";
            value = "logoutFilter";
            TestCase.assertEquals(value, section.get(key));

            key = "uri/uri/**";
            value = "userFilter";
            TestCase.assertEquals(value, section.get(key));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
