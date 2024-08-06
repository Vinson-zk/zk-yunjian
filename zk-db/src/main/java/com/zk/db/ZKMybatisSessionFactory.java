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
 * @Title: ZKMybatisSessionFactory.java 
 * @author Vinson 
 * @Package com.zk.db 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:01:52 AM 
 * @version V1.0   
*/
package com.zk.db;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

/**
 * @ClassName: ZKMybatisSessionFactory
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKMybatisSessionFactory {

    protected static Logger logger = LogManager.getLogger(ZKMybatisSessionFactory.class);

    /**
     * 定义 SqlSessionFactory 对象，用于建立 SqlSession 对象
     */
    protected static SqlSessionFactory sqlSessionFactory = null;

    /**
     * 定义 ThreadLocal 对象，存放线程中的 Session；
     */
    private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

    /**
     * 取 SqlSessionFactory 对象
     * 
     * @return
     */
    public SqlSessionFactory getSqlSessionFactory() {
        return sqlSessionFactory;
    }

    /**
     * 取本线程中使用的 SqlSession 对象
     * 
     * @return
     */
    public SqlSession openSession() {
        // 从 ThreadLocal 对象中获得 Session 对象
        SqlSession sqlSession = (SqlSession) threadLocal.get();

        // 如果 ThreadLocal 对象中没有当前线程的 SqlSession 对象，则新建一个 SqlSession 对象
        if (sqlSession == null) {
            // 如果 SqlSessionFactory 对象未建立，重新建立一个 SqlSessionFactory 对象
            if (sqlSessionFactory == null) {
                rebuildSqlSessionFactory();
            }
            // 通过 SqlSessionFactory 的 openSession 方法建立一个 Session 对象
            sqlSession = (sqlSessionFactory != null) ? sqlSessionFactory.openSession() : null;
            // 将 SqlSession 对象保存到 ThreadLocal 中
            if (sqlSession != null) {
                threadLocal.set(sqlSession);
            }
        }
        logger.info("[^_^: 201706231123-000] class:{} ; session hashCode:{}", this.getClass().getName(),
                sqlSession.hashCode());

        return sqlSession;
    }

    public static boolean closeSession(SqlSession sqlSession) {
        // 从 ThreadLocal 对象中获得 Session 对象
        SqlSession s = (SqlSession) threadLocal.get();
        if (s.hashCode() == sqlSession.hashCode()) {
            threadLocal.remove();
            sqlSession.close();
            return true;
        }
        return false;
    }

    /**
     * 重建立 SqlSessionFactory 对象
     */
    public abstract void rebuildSqlSessionFactory();

}
