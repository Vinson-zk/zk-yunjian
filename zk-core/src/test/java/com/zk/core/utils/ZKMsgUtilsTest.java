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
 * @Title: ZKMsgUtilsTest.java 
 * @author Vinson 
 * @Package com.zk.core.utils 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 11, 2019 3:10:02 PM 
 * @version V1.0   
*/
package com.zk.core.utils;

import org.junit.Test;

import com.zk.core.helper.ZKCoreTestHelper;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMsgUtilsTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMsgUtilsTest {

    static {
        TestCase.assertNotNull(ZKCoreTestHelper.getCtxUtils());
    }

    @Test
    public void testMsg() {
        try {

            // test.msg.0 = 这个国际化信息 key 是：0
            String expectMsg_0 = "这个国际化信息 key 是：0";
            String expectMsg_0_en = "This msg key is：0";
            String expectMsg_1 = "这个国际化信息 key 是：1";
            String expectMsg_1_en = "This msg key is：1";
            // test.msg.testMsg = 这是国际化信息，参数 0 的值：{0}
            String expectMsg_msg = "这是国际化信息，参数 0 的值：";
            String expectMsg_msg_en = "This msg，param 0 value：";

            String msg = "";
            String localeStr = "";

            localeStr = "zh_CN";
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "0");
            TestCase.assertEquals(expectMsg_0, msg);
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "1");
            TestCase.assertEquals(expectMsg_1, msg);

            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "test.msg.testMsg");
            TestCase.assertEquals(expectMsg_msg + "{1}{0}", msg);
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "test.msg.testMsg", "p0");
            TestCase.assertEquals(expectMsg_msg + "{1}p0", msg);
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "test.msg.testMsg", "p0", "p1");
            TestCase.assertEquals(expectMsg_msg + "p1p0", msg);

            localeStr = "en_US";
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "0");
            TestCase.assertEquals(expectMsg_0_en, msg);
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "1");
            TestCase.assertEquals(expectMsg_1_en, msg);

            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "test.msg.testMsg");
            TestCase.assertEquals(expectMsg_msg_en + "{1}{0}", msg);
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "test.msg.testMsg", "p0");
            TestCase.assertEquals(expectMsg_msg_en + "{1}p0", msg);
            msg = ZKMsgUtils.getMessage(ZKLocaleUtils.valueOf(localeStr), "test.msg.testMsg", "p0", "p1");
            TestCase.assertEquals(expectMsg_msg_en + "p1p0", msg);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
