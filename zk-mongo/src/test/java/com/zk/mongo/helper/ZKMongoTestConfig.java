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
 * @Title: ZKMongoTestConfig.java 
 * @author Vinson 
 * @Package com.zk.mongo.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:10:28 PM 
 * @version V1.0   
*/
package com.zk.mongo.helper;

import org.junit.Test;
import org.springframework.context.ApplicationContext;

import com.zk.mongo.configuration.ZKMongoAutoConfigurationTest;

import junit.framework.TestCase;

/** 
* @ClassName: ZKMongoTestConfig 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKMongoTestConfig {

    // command 集合名称
    public static final String colleationName_command = "test_command";

    // Administration 集合名称
    public static final String colleationName_Administration = "test_command_Administration";

    // QueryAndWriteOperation 测试 集合的名称
    public static final String colleationName_QueryAndWriteOperation = "test_command_QueryAndWriteOperation";

    // 过期时间属性名称
    public static final String attrExpireTimeName = "expireTime";

    // 过期索引名称
    public static final String indexExpireTimeName = "_index_expireTime_";

    public static final String config_path = "classpath:test_spring_context_mongo_3.x.xml";

    private static ApplicationContext ctx;

    public static ApplicationContext getCtx() {
        if (ctx == null) {
//            ctx = new FileSystemXmlApplicationContext(config_path);
            ctx = ZKMongoAutoConfigurationTest.run(new String[] {});
        }
        return ctx;
    }

    @Test
    public void test() {

        try {
            TestCase.assertNotNull(getCtx());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
