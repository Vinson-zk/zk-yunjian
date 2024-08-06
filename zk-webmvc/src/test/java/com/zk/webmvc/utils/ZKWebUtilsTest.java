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
 * @Title: ZKWebUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.webmvc.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:53:10 PM 
 * @version V1.0   
*/
package com.zk.webmvc.utils;

import java.util.Locale;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.core.commons.ZKEnvironment;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.resolver.ZKLocaleResolver;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.webmvc.helper.ZKWebmvcTestHelperMvcSpringBootMain;

import junit.framework.TestCase;

/** 
* @ClassName: ZKWebUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKWebUtilsTest {

    @Test
    public void testGetLocale() {
        try {
            
            Locale expectLocale = null;
            Locale locale = null;

//            locale = ZKWebUtils.getLocale(null, null);
//            expectLocale = Locale.getDefault();
//            TestCase.assertEquals(expectLocale, locale);
            ConfigurableApplicationContext ctx = ZKWebmvcTestHelperMvcSpringBootMain.getCtx();
            TestCase.assertNotNull(ctx);
            ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(ctx));
            locale = ZKServletUtils.getLocale(null, null);
            expectLocale = ctx.getBean(ZKLocaleResolver.class).getDefaultLocale();
            TestCase.assertEquals(expectLocale, locale);

            locale = ZKWebUtils.getLocale();
            expectLocale = ZKLocaleUtils.valueOf("en_US");
            TestCase.assertEquals(expectLocale, locale);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
