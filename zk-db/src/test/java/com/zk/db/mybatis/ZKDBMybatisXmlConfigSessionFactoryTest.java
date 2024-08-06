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
 * @Title: ZKDBMybatisXmlConfigSessionFactoryTest.java 
 * @author Vinson 
 * @Package com.zk.db 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:48:11 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis;

import com.zk.db.ZKMybatisSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.zk.db.helper.ZKDBMybatisXmlConfigSessionFactory;
import com.zk.db.helper.ZKDBTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDBMybatisXmlConfigSessionFactoryTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBMybatisXmlConfigSessionFactoryTest {

    @Test
    public void test() {
        try {
            ZKDBMybatisXmlConfigSessionFactory xcsf = ZKDBMybatisXmlConfigSessionFactory
                    .creatBean(ZKDBTestConfig.xml_config_path);
            TestCase.assertNotNull(xcsf);

            SqlSessionFactory ssf = xcsf.getSqlSessionFactory();

            TestCase.assertNotNull(ssf);

            SqlSession session = xcsf.openSession();

            int sid = session.hashCode();
            session = xcsf.openSession();
            TestCase.assertEquals(sid, session.hashCode());
            ZKMybatisSessionFactory.closeSession(session);
            session = xcsf.openSession();
            TestCase.assertTrue((sid != session.hashCode()));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
