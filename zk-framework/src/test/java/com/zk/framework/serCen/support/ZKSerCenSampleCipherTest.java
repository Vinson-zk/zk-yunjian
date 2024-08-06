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
* @Title: ZKSerCenSampleCipherTest.java 
* @author Vinson 
* @Package com.zk.framework.serCen.support 
* @Description: TODO(simple description this file what to do.) 
* @date May 24, 2020 3:20:29 PM 
* @version V1.0 
*/
package com.zk.framework.serCen.support;

import java.util.Map;

import org.junit.Test;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.core.utils.ZKStringUtils;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSerCenSampleCipherTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSerCenSampleCipherTest {

    @Test
    public void testNormal() {

        ZKSerCenSampleCipher zkSerCenSampleCipher = new ZKSerCenSampleCipher();

        String serverName = "TEST-ZK-FRAMEWORE-EUREKA-CLIENT-APP";
//        String ip = "127.0.0.1";
//        Date time = new Date();

        String encStr;
        Map<String, Object> decMap;
        Map<String, String> encMap;
        zkSerCenSampleCipher.setServerName(serverName);

        encMap = zkSerCenSampleCipher.encrypt();
        encStr = encMap.get(zkSerCenSampleCipher.getSignature());
        System.out.println("[^_^:20200524-1525-001] encStr: " + encStr);

        decMap = zkSerCenSampleCipher.decode(encStr);
        System.out.println("[^_^:20200524-1525-002] decMap: " + ZKJsonUtils.toJsonStr(decMap));
        serverName = zkSerCenSampleCipher.getServerName();
        TestCase.assertEquals(serverName, decMap.get(ZKSerCenSampleCipher.Key.serverName));
        TestCase.assertTrue(ZKStringUtils.equals(serverName, (String) decMap.get(ZKSerCenSampleCipher.Key.serverName)));
//        TestCase.assertEquals(ip, decMap.get(ZKSerCenSampleCipher.Key.ip));
//        TestCase.assertEquals(time.getTime(), ((Date) decMap.get(ZKSerCenSampleCipher.Key.date)).getTime());


    }

}
