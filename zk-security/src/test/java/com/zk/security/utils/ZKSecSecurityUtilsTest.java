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
* @Title: ZKSecSecurityUtilsTest.java 
* @author Vinson 
* @Package com.zk.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 3:06:14 PM 
* @version V1.0 
*/
package com.zk.security.utils;

import org.junit.Test;

import com.zk.core.exception.ZKSecAuthenticationException;
import com.zk.security.helper.ZKSecTestHelper;
import com.zk.security.mgt.ZKSecSecurityManager;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.token.ZKSecAuthenticationToken;
import com.zk.security.token.ZKSecDefaultAuthcUserToken;

import junit.framework.TestCase;

/** 
* @ClassName: ZKSecSecurityUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSecurityUtilsTest {

    public static ZKSecSecurityManager securiityManager;

    static {
        try {
            securiityManager = ZKSecTestHelper.getCtxSec().getBean(ZKSecSecurityManager.class);
            TestCase.assertNotNull(securiityManager);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testSec() {

        try {
            ZKSecSecurityUtils.setSecurityManager(securiityManager);

            ZKSecSubject subject = ZKSecSecurityUtils.getSubject();

            ZKSecAuthenticationToken authenticationToken;

            String username;
            String pwd;

            /*** 用户不存在 ***/
            username = "admin_err";
            pwd = "admin";
            authenticationToken = new ZKSecDefaultAuthcUserToken("groupCode", username, pwd, false, 0, "",
                    0, "");
            try {
                subject.login(authenticationToken);
            }
            catch(ZKSecAuthenticationException ae) {
//              zk.sec.000001=用户不存在
                TestCase.assertEquals("zk.sec.000001", ae.getCode());
//                ae.printStackTrace();
            }

            /*** 密码错误 ***/
            username = "admin";
            pwd = "admin_err";
            authenticationToken = new ZKSecDefaultAuthcUserToken("groupCode", username, pwd, false, 0, "",
                    0, "");
            try {
                subject.login(authenticationToken);
            }
            catch(ZKSecAuthenticationException ae) {
//              zk.sec.000002=密码错误
                TestCase.assertEquals("zk.sec.000002", ae.getCode());
//                ae.printStackTrace();
            }

            /*** admin 成功登录 ***/
            username = "admin";
            pwd = "admin";
            authenticationToken = new ZKSecDefaultAuthcUserToken("groupCode", username, pwd, false, 0, "",
                    0, "");
            subject.login(authenticationToken);
            TestCase.assertEquals(username, ZKSecSecurityUtils.getUserId());

            String permissionCode = "test_permissionCode";
            TestCase.assertTrue(subject.checkPermission(permissionCode));
            permissionCode = "test_permissionCode_err";
            TestCase.assertFalse(subject.checkPermission(permissionCode));
            String roleCode = "test_adminRole";
            TestCase.assertTrue(subject.checkApiCode(roleCode));
            roleCode = "test_adminRole_error";
            TestCase.assertFalse(subject.checkApiCode(roleCode));

            /*** test 成功登录 ***/
            username = "test";
            pwd = "test";
            authenticationToken = new ZKSecDefaultAuthcUserToken("groupCode", username, pwd, false, 0, "",
                    0, "");
            subject.login(authenticationToken);
            TestCase.assertEquals(username, ZKSecSecurityUtils.getUserId());

            permissionCode = "test_permissionCode";
            TestCase.assertFalse(subject.checkPermission(permissionCode));
            roleCode = "test_adminRole";
            TestCase.assertFalse(subject.checkApiCode(roleCode));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
