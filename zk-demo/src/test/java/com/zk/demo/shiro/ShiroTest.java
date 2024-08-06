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
* @Title: ShiroTest.java 
* @author Vinson 
* @Package com.zk.demo.shiro 
* @Description: TODO(simple description this file what to do. ) 
* @date May 21, 2021 8:46:01 AM 
* @version V1.0 
*/
package com.zk.demo.shiro;

import org.junit.Assert;
import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ShiroTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ShiroTest {

    public static final String config = "classpath:shiro/test_permitted.ini";

    @Test
    public void testLogin() {
        try {
            ShiroInit shiro = new ShiroInit(config);
            TestCase.assertTrue(shiro.login("huaan", "9527"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLogout() {
        try {
            ShiroInit shiro = new ShiroInit(config);
            TestCase.assertTrue(shiro.login("huaan", "9527"));
            TestCase.assertTrue(shiro.logout());
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testIsPermitted() {
        try {
            ShiroInit shiro = new ShiroInit(config);
            TestCase.assertTrue(shiro.login("huaan", "9527"));
            // 判断拥有权限：user:create
            Assert.assertTrue(shiro.getSubject().isPermitted("user:create"));
            // 判断拥有权限：user:update and user:delete
            Assert.assertTrue(shiro.getSubject().isPermittedAll("user:update", "user:delete"));
            // 判断没有权限：user:view
            Assert.assertFalse(shiro.getSubject().isPermitted("user:view"));
            // 有 user:test 权限，则有 user:test:* 权限
            Assert.assertTrue(shiro.getSubject().isPermitted("user:test:all"));
            // 有 user:test 权限，则有 user:test:* 权限
            Assert.assertTrue(shiro.getSubject().isPermitted("user:test:other"));
            // 有 user:test 权限，但没有有 user:other权限
            Assert.assertFalse(shiro.getSubject().isPermitted("user:other"));
            // 有 roleMax 权限，则有 roleMax:*:* 权限
            Assert.assertTrue(shiro.getSubject().isPermitted("roleMax:roleMid:roleMin"));
            shiro.logout();
            Assert.assertTrue(shiro.login("qiuxiang", "5120"));
            Assert.assertFalse(shiro.getSubject().isPermitted("user:test"));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}
