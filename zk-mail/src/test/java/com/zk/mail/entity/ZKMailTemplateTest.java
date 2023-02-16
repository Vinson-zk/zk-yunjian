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
* @Title: ZKMailTemplateTest.java 
* @author Vinson 
* @Package com.zk.mail.entity 
* @Description: TODO(simple description this file what to do. ) 
* @date May 27, 2022 8:30:06 AM 
* @version V1.0 
*/
package com.zk.mail.entity;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMailTemplateTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMailTemplateTest {

    @Test
    public void testGetContentRenderString() {
        try {
            ZKMailTemplate mt = new ZKMailTemplate();
            Map<String, Object> params = new HashMap<String, Object>();

            mt.setContent("tttt-${content}");

            params.put("content", "content");
            TestCase.assertEquals("tttt-content", mt.getContent(params));
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }
}
