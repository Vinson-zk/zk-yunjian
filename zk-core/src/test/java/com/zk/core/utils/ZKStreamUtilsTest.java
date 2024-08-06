/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKStreamUtilsTest.java 
* @author Vinson 
* @Package com.zk.core.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 6, 2024 9:48:46 AM 
* @version V1.0 
*/
package com.zk.core.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKStreamUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKStreamUtilsTest {

    @Test
    public void test() {
        try {

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testReadAndWrite() {
        try {
            byte[] bytes = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890".getBytes();
            String resStr = null;
            int start = 0;
            long readLength = 0;
            long resLength = -1;

            InputStream is = null;
            ByteArrayOutputStream baos = null;

//            byte[] bys = null;

            start = 0;
            readLength = bytes.length;
            is = new ByteArrayInputStream(bytes);
            baos = new ByteArrayOutputStream();
            resLength = -1;
            resLength = ZKStreamUtils.readAndWrite(is, baos, start, readLength);
            System.out.println("[^_^:202404056-1001-001] start: " + start);
            System.out.println("[^_^:202404056-1001-001] readLength: " + readLength);
            System.out.println("[^_^:202404056-1001-001] resLength: " + resLength);
            resStr = baos.toString();
            System.out.println("[^_^:202404056-1001-001] baos.resStr: " + resStr);
            TestCase.assertEquals("ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890", resStr);

            start = 26;
            readLength = bytes.length;
            is = new ByteArrayInputStream(bytes);
            baos = new ByteArrayOutputStream();
            resLength = -1;
            resLength = ZKStreamUtils.readAndWrite(is, baos, start, readLength);
            System.out.println("[^_^:202404056-1001-002] start: " + start);
            System.out.println("[^_^:202404056-1001-002] readLength: " + readLength);
            System.out.println("[^_^:202404056-1001-002] resLength: " + resLength);
            resStr = baos.toString();
            System.out.println("[^_^:202404056-1001-002] baos.resStr: " + resStr);
            TestCase.assertEquals("1234567890", resStr);

            start = 26;
            readLength = 3;
            is = new ByteArrayInputStream(bytes);
            baos = new ByteArrayOutputStream();
            resLength = -1;
            resLength = ZKStreamUtils.readAndWrite(is, baos, start, readLength);
            System.out.println("[^_^:202404056-1001-003] start: " + start);
            System.out.println("[^_^:202404056-1001-003] readLength: " + readLength);
            System.out.println("[^_^:202404056-1001-003] resLength: " + resLength);
            resStr = baos.toString();
            System.out.println("[^_^:202404056-1001-003] baos.resStr: " + resStr);
            TestCase.assertEquals("123", resStr);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
