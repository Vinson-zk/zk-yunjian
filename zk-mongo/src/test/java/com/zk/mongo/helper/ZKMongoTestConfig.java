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
import org.springframework.context.support.FileSystemXmlApplicationContext;

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

    public static final String config_path_1 = "classpath:test_spring_context_mongo.xml";

    public static final String config_path_2 = "classpath:test_spring_context_mongo_2.0.xml";

    private static FileSystemXmlApplicationContext ctx1;

    private static FileSystemXmlApplicationContext ctx2;

    public static FileSystemXmlApplicationContext getCtx1() {
        if (ctx1 == null) {
            ctx1 = new FileSystemXmlApplicationContext(config_path_1);
        }
        return ctx1;
    }

    public static FileSystemXmlApplicationContext getCtx2() {
        if (ctx2 == null) {
            ctx2 = new FileSystemXmlApplicationContext(config_path_2);
        }
        return ctx2;
    }

    public static FileSystemXmlApplicationContext getCtx() {
        return getCtx2();
    }

    @Test
    public void test() {

        try {
//            TestCase.assertNotNull(getCtx1());
            TestCase.assertNotNull(getCtx2());
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
