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
 * @Title: ZKDBMybatisJavaConfigSessionFactoryTest.java 
 * @author Vinson 
 * @Package com.zk.db 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:49:07 AM 
 * @version V1.0   
*/
package com.zk.db.mybatis;

import com.zk.db.ZKMybatisSessionFactory;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;

import com.zk.db.helper.ZKDBMybatisJavaConfigSessionFactory;
import com.zk.db.helper.ZKDBTestConfig;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDBMybatisJavaConfigSessionFactoryTest 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBMybatisJavaConfigSessionFactoryTest {

    @Test
    public void test() {
        try {
            ZKDBMybatisJavaConfigSessionFactory jcsf = ZKDBMybatisJavaConfigSessionFactory
                    .creatBean(ZKDBTestConfig.properties_file_path);
            TestCase.assertNotNull(jcsf);

            SqlSessionFactory ssf = jcsf.getSqlSessionFactory();

            TestCase.assertNotNull(ssf);

            SqlSession session = jcsf.openSession();

            int sid = session.hashCode();
            session = jcsf.openSession();
            TestCase.assertEquals(sid, session.hashCode());
            ZKMybatisSessionFactory.closeSession(session);
            session = jcsf.openSession();
            TestCase.assertTrue((sid != session.hashCode()));

        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
