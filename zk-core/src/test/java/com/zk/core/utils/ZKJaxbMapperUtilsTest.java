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
* @Title: ZKJaxbMapperUtilsTest.java 
* @author Vinson 
* @Package com.zk.core.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 24, 2021 6:06:21 PM 
* @version V1.0 
*/
package com.zk.core.utils;

import org.junit.Test;

import com.zk.core.helper.entity.ZKCoreEntity;

import junit.framework.TestCase;

/** 
* @ClassName: ZKJaxbMapperUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJaxbMapperUtilsTest {

    @Test
    public void testToXml() {
        try {
            ZKCoreEntity zkCoreEntity = new ZKCoreEntity();

            zkCoreEntity.setAge(3);
            zkCoreEntity.setName("ZKCoreEntity");
            zkCoreEntity.setRemark("setRemark");

            String xmlStr = ZKJaxbMapperUtils.toXml(zkCoreEntity);
            System.out.println("=== \n" + xmlStr);

            zkCoreEntity = ZKJaxbMapperUtils.fromXml(xmlStr, ZKCoreEntity.class);
            TestCase.assertEquals("ZKCoreEntity", zkCoreEntity.getName());


        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
