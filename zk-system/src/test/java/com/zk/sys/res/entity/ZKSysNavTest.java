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
* @Title: ZKSysNavTest.java 
* @author Vinson 
* @Package com.zk.sys.res.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 10, 2024 9:44:09 AM 
* @version V1.0 
*/
package com.zk.sys.res.entity;

import org.junit.Test;

import com.zk.core.exception.ZKValidatorException;
import com.zk.core.utils.ZKValidatorsBeanUtils;
import com.zk.sys.helper.ZKSysTestHelper;

import jakarta.validation.Validator;
import junit.framework.TestCase;

/** 
* @ClassName: ZKSysNavTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSysNavTest {

    @Test
    public void testValidator() {

        Validator validator = ZKSysTestHelper.getMainCtx().getBean(Validator.class);

        try {
            ZKSysNav sysNav = new ZKSysNav();

            sysNav.putName("test-name-1", "test-name-1");
            sysNav.setCode("test-nav-code");
            sysNav.setFuncModuleCode("test-funcModuleCode");
            sysNav.setFuncName("test-funcName");
            sysNav.setPath("test-path");
            sysNav.setIsShow(1);
            sysNav.setIsIndex(0);
            sysNav.setSort(1);
            ZKValidatorsBeanUtils.validateWithException(validator, sysNav);

            try {
                sysNav.setCode("test-nav:code");
                ZKValidatorsBeanUtils.validateWithException(validator, sysNav);
                TestCase.assertTrue(false);
            }
            catch(ZKValidatorException e) {
                e.printStackTrace();
                TestCase.assertTrue(true);
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
