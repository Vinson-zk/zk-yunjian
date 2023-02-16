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
 * @Title: ZKDBTestConfig.java 
 * @author Vinson 
 * @Package com.zk.db.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:19:20 AM 
 * @version V1.0   
*/
package com.zk.db.helper;

import org.junit.Test;
import org.springframework.context.ConfigurableApplicationContext;

import com.zk.db.ZKMybatisSessionFactory;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDBTestConfig 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBTestConfig {


    public static final String properties_file_path = "classpath:test.zk.db.source.properties";

    public static final String xml_config_path = "mybatis/test_mybatis_config_env.xml";

    private static ZKMybatisSessionFactory javaConfigSessionFactory;

    private static ZKMybatisSessionFactory xmlConfigSessionFactory;

    /**
     * mybatis session factory；java 配置
     * 
     * @return
     */
    public static ZKMybatisSessionFactory getJavaConfigSessionFactory() {
        if (javaConfigSessionFactory == null) {
            javaConfigSessionFactory = ZKDBMybatisJavaConfigSessionFactory.creatBean(properties_file_path);
        }
        return javaConfigSessionFactory;
    }

    /**
     * mybatis session factory；xml 配置
     * 
     * @return
     */
    public static ZKMybatisSessionFactory getXmlConfigSessionFactory() {
        if (xmlConfigSessionFactory == null) {
            xmlConfigSessionFactory = ZKDBMybatisXmlConfigSessionFactory.creatBean(xml_config_path);
        }
        return xmlConfigSessionFactory;
    }

    // ---------------------------------------------------------------
    // ---------------------------------------------------------------

    public static ConfigurableApplicationContext dynamicCtx = null;

    /**
     * 取 动态数据源 contenxt；读写分离
     * 
     * @return
     */
    public static ConfigurableApplicationContext getDynamicCtx() {
        if (dynamicCtx == null) {
            dynamicCtx = ZKDBSpringBootMain.run(new String[] {});
        }
        return dynamicCtx;
    }

    @Test
    public void test() {
        try {
            ZKMybatisSessionFactory mbsf = ZKDBTestConfig.getXmlConfigSessionFactory();
            TestCase.assertNotNull(mbsf);

//        mbsf = TestConfig.getJavaConfigSessionFactory();
//        TestCase.assertNotNull(mbsf);

//        FileSystemXmlApplicationContext ctx = TestConfig.getCtx();
//        TestCase.assertNotNull(ctx);
//        
//        FileSystemXmlApplicationContext dynamicCtx = TestConfig.getCtx();
//        TestCase.assertNotNull(dynamicCtx);

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
