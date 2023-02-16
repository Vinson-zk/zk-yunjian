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
* @Title: ZKMongoLockUtilsTest.java 
* @author Vinson 
* @Package com.zk.mongo.lock 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 16, 2021 3:19:15 PM 
* @version V1.0 
*/
package com.zk.mongo.lock;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.helper.ZKMongoTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoLockUtilsTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoLockUtilsTest {

    @Test
    public void testIncrByWithExpire() {
        try {

            ApplicationContext ctx = ZKMongoTestConfig.getCtx();
            TestCase.assertNotNull(ctx);
            MongoTemplate mongoTemplate = ctx.getBean(MongoTemplate.class);
            TestCase.assertNotNull(mongoTemplate);

            ZKMongoLock ml = new ZKMongoLock();
            ml.setKey("testKey1-3");
            ml.setValue(2l);

            // Calendar cal = Calendar.getInstance();
            // TimeZone timeZone = cal.getTimeZone();

            ml.setExpire(System.currentTimeMillis() + 6000l);
            ZKMongoLockUtils.incrByWithExpire(mongoTemplate, ml);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
