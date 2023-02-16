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
* @Title: ZKPlatformCertTest.java 
* @author Vinson 
* @Package com.zk.wechat.pay.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 23, 2021 1:23:56 PM 
* @version V1.0 
*/
package com.zk.wechat.pay.entity;

import org.junit.Test;

import com.zk.core.utils.ZKDateUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKPlatformCertTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKPlatformCertTest {

    @Test
    public void test() {

        try {
            ZKPlatformCert cert = new ZKPlatformCert();
            int v = 0;

            v = 3;
            cert.setCertExpirationTime(ZKDateUtils.parseDate(System.currentTimeMillis() - v * 1000));
            TestCase.assertEquals(true, cert.isExpiration());
            TestCase.assertEquals(-1 * v, cert.getExpSecond());

            v = 32;
            cert.setCertExpirationTime(ZKDateUtils.parseDate(System.currentTimeMillis() + v * 1000));
            TestCase.assertEquals(false, cert.isExpiration());
            TestCase.assertEquals(v, cert.getExpSecond());


        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
