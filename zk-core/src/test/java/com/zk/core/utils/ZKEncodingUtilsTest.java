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
 * @Title: ZKEncodingUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:07:54 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import java.io.UnsupportedEncodingException;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKEncodingUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKEncodingUtilsTest {

    @Test
    public void testByteInDouble() {
        try {

            byte[] zk = null;
            String targetStr = null;
            byte[] resBytes = null;

            zk = new byte[] { (byte) 0xff };
            System.out.println("------------------------------: " + (zk[0] & 0xff >> 4));
            // ===========================
            zk = new byte[] { (byte) 0x00 };
            targetStr = "0000";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xff };
            targetStr = "0f0f";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0x27 };
            targetStr = "0207";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xd4 };
            targetStr = "0d04";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xf0 };
            targetStr = "0f00";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xf2 };
            targetStr = "0f02";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xfa };
            targetStr = "0f0a";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xfc };
            targetStr = "0f0c";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0x0f };
            targetStr = "000f";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0x2f };
            targetStr = "020f";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xaf };
            targetStr = "0a0f";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);
            // ===========================
            zk = new byte[] { (byte) 0xcf };
            targetStr = "0c0f";
            resBytes = ZKEncodingUtils.byteInDouble(zk);
            System.out.println(String.format("=== %s:%s:%s:%s", zk[0], ZKEncodingUtils.encodeHex(zk),
                    ZKEncodingUtils.encodeHex(resBytes), ZKEncodingUtils.byteOutDouble(resBytes)[0]));
            TestCase.assertEquals(targetStr, ZKEncodingUtils.encodeHex(resBytes).toLowerCase());
            TestCase.assertEquals(zk[0], ZKEncodingUtils.byteOutDouble(resBytes)[0]);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    /**
     * hex 编码测试
     */
    @Test
    public void testEncodeByHexAndDecodeByHex() {
        String strSource = null;
        String strTarget = null;
        String result = null;

        strSource = "!@#%a} Jhex 编码测试 ";
        strTarget = "21402325617d204a68657820e7bc96e7a081e6b58be8af9520";
        result = ZKEncodingUtils.encodeHex(strSource.getBytes());
        System.out.println("[^_^:20200103-1449-001]" + result);
        TestCase.assertEquals(strTarget, result);

        result = new String(ZKEncodingUtils.decodeHex(strTarget));
        System.out.println("[^_^:20200103-1449-002]" + result);
        TestCase.assertEquals(strSource, result);

        strTarget = strTarget.toUpperCase();
        result = new String(ZKEncodingUtils.decodeHex(strTarget));
        System.out.println("[^_^:20200103-1449-003]" + result);
        TestCase.assertEquals(strSource, result);
    }

    /**
     * Base64 编码测试
     * 
     * @throws UnsupportedEncodingException
     */
    @Test
    public void testBase64() throws UnsupportedEncodingException {
        String strSource = null;
        String strTarget = null;
        String result = null;

        strSource = "莫雪白头老。.,;'[]\\|}{:\"?><“”·1234567890-=【】】、‘’；、。，~！@#￥%……&*（））——+{}}|“”：《》》？;''";
        strTarget = "6I6r6Zuq55m95aS06ICB44CCLiw7J1tdXHx9ezoiPz484oCc4oCdwrcxMjM0NTY3ODkwLT3jgJDjgJHjgJHjgIHigJjigJnvvJvjgIHjgILvvIx+77yBQCPvv6Ul4oCm4oCmJirvvIjvvInvvInigJTigJQre319fOKAnOKAne+8muOAiuOAi+OAi++8nzsnJw==";
        result = ZKEncodingUtils.encodeBase64ToStr(strSource);
        System.out.println("[^_^:20200109-1447-001]" + result);
        TestCase.assertEquals(strTarget, result);

        result = ZKEncodingUtils.decodeBase64ToStr(strTarget);
        System.out.println("[^_^:20200109-1447-002]" + result);
        TestCase.assertEquals(strSource, result);

    }

}
