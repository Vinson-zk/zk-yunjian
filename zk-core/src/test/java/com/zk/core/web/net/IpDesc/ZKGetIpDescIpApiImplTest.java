/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKGetIpDescIpApiImplTest.java 
* @author Vinson 
* @Package com.zk.core.web.net.IpDesc 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 8, 2025 6:37:28 PM 
* @version V1.0 
*/
package com.zk.core.web.net.IpDesc;

import java.util.Locale;

import org.junit.Test;

import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.net.ZKIpDesc;

import junit.framework.TestCase;

/**
 * @ClassName: ZKGetIpDescIpApiImplTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKGetIpDescIpApiImplTest {

    @Test
    public void testGetIpDesc() {
        try {
            ZKIpDesc desc;
            String ip;
            Locale locale;
            
            ZKGetIpDescIpApiImpl getIpDescIpApiImpl = new ZKGetIpDescIpApiImpl();

            ip = "127.0.0.1";
            desc = getIpDescIpApiImpl.getIpDesc(ip, null);
            TestCase.assertEquals(ip, desc.getIp());
            
            locale = ZKLocaleUtils.distributeLocale("zh-CN");
            ip = "103.152.220.13";
            desc = getIpDescIpApiImpl.getIpDesc(ip, locale);
            TestCase.assertEquals(ip, desc.getIp());
            TestCase.assertEquals("HK", desc.getCountryCode());
            TestCase.assertEquals("HCW", desc.getRegionCode());

            ip = "175.8.94.202";
            desc = getIpDescIpApiImpl.getIpDesc(ip, null);
            TestCase.assertEquals(ip, desc.getIp());
            TestCase.assertEquals(ip, desc.getIp());
            TestCase.assertEquals("CN", desc.getCountryCode());
            TestCase.assertEquals("HN", desc.getRegionCode());

        }
        catch(Exception e) {
            TestCase.assertTrue(false);
            e.printStackTrace();
        }
    }

}
