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
* @Title: ZKSysSendMailMsgServiceTest.java 
* @author Vinson 
* @Package com.zk.sys.service 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 15, 2024 12:14:50 AM 
* @version V1.0 
*/
package com.zk.sys.service;

import org.junit.Test;

import com.zk.core.commons.ZKMsgRes;
import com.zk.sys.helper.ZKSysTestHelper;
import com.zk.sys.service.ZKSysMsgConstants.ZKCodeType;

import junit.framework.TestCase;

/**
 * @ClassName: ZKSysSendMailMsgServiceTest
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSysSendMailMsgServiceTest {

    @Test
    public void testSendMailCode() {

        ZKSysSendMailMsgService s = ZKSysTestHelper.getMainCtx().getBean(ZKSysSendMailMsgService.class);

        try {
            String recipientMailAddr = "binary_space@126.com";
            String userName, verifyCode;
            ZKMsgRes res;

            userName = null;
            verifyCode = "test9527";
            res = s.sendMailCode(ZKCodeType.changeMail, userName, verifyCode, recipientMailAddr);
            System.out.println("[^_^:20240715-0023-001] res: " + res.toString());
            TestCase.assertTrue(res.isOk());

            userName = "华安";
            verifyCode = "test9527";
            res = s.sendMailCode(ZKCodeType.restPassword, userName, verifyCode, recipientMailAddr);
            System.out.println("[^_^:20240715-0023-002] res: " + res.toString());
            TestCase.assertTrue(res.isOk());

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
