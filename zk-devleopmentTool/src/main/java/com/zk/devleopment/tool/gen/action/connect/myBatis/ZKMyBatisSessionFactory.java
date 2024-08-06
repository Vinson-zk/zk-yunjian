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
* @Title: ZKMyBatisSessionFactory.java 
* @author Vinson 
* @Package com.zk.code.generate.connect.myBatis 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 25, 2021 10:22:25 AM 
* @version V1.0 
*/
package com.zk.devleopment.tool.gen.action.connect.myBatis;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.devleopment.tool.gen.entity.ZKModule;

/** 
* @ClassName: ZKMyBatisSessionFactory 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
class ZKMyBatisSessionFactory {

    protected static Logger log = LogManager.getLogger(ZKMyBatisSessionFactory.class);

    /**
     * 定义 SqlSessionFactory 对象，用于建立 SqlSession 对象
     */
    private static SqlSessionFactory sqlSessionFactory = null;

    /**
     * 定义 ThreadLocal 对象，存放线程中的 Session；
     */
    private static final ThreadLocal<SqlSession> threadLocal = new ThreadLocal<SqlSession>();

    /**
     * 重建立 SqlSessionFactory 对象
     */
    public static void createSessionFactory(String config) {
        try {
            // 设置读配置文件输入流
            InputStream inputStream = Resources.getResourceAsStream(config);
            // 创建 myBatis SqlSessionFactory 对象
            sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);

        }
        catch(Exception e) {
            log.error("[>_<:20210325-1056-001] 创建 myBatis SqlSessionFactory 对象失败！config:{}", config);
            sqlSessionFactory = null;
            e.printStackTrace();
        }
    }

    /**
     * 重建立 SqlSessionFactory 对象
     *
     * @Title: createSessionFactory
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 25, 2021 11:56:03 AM
     * @param dataSource
     * @param mappers
     *            config/myBatis/tableInfoMapper.xml
     * @return void
     */
    public static void createSessionFactory(DataSource dataSource, String[] mappers) {
        try {
            SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();

            sqlSessionFactoryBean.setDataSource(dataSource);
            // 事务
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            sqlSessionFactoryBean.setTransactionFactory(transactionFactory);
            sqlSessionFactoryBean.setTypeHandlersPackage("com.zk.db.mybatis.typeHandler");

            // 设置读配置文件输入流
            List<Resource> rs = new ArrayList<>();
            for (String mPath : mappers) {
                InputStream inputStream = Resources.getResourceAsStream(mPath);
                rs.add(new InputStreamResource(inputStream));
            }

            sqlSessionFactoryBean.setMapperLocations(rs.toArray(new Resource[rs.size()]));
            sqlSessionFactory = sqlSessionFactoryBean.getObject();
        }
        catch(Exception e) {
            sqlSessionFactory = null;
            log.error("[>_<:20210325-1056-002] 创建 myBatis SqlSessionFactory 对象失败！");
            e.printStackTrace();
        }
    }

    public static DataSource createDataSource(ZKModule zkModule) {
        try {
            Properties ps = new Properties();
//            ps.setProperty("driver", "com.mysql.jdbc.Driver");
//            ps.setProperty("url", properties.getProperty("zk.db.dynamic.jdbc.url"));
//            ps.setProperty("username", properties.getProperty("zk.db.dynamic.jdbc.writeUserName"));
//            ps.setProperty("password", properties.getProperty("zk.db.dynamic.jdbc.writePwd"));
            ps.setProperty("driver", zkModule.getDriver());
            ps.setProperty("url", zkModule.getUrl());
            ps.setProperty("username", zkModule.getUsername());
            ps.setProperty("password", zkModule.getPassword());
            PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
            pooledDataSourceFactory.setProperties(ps);
            DataSource dataSource = pooledDataSourceFactory.getDataSource();
            return dataSource;
        }
        catch(Exception e) {
            log.error("[>_<:20210325-1056-003] 创建 myBatis SqlSessionFactory 对象失败！zkModule: {}",
                    ZKJsonUtils.toJsonStr(zkModule));
            sqlSessionFactory = null;
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 调用前先要创建 sessionFactory
     * 
     * 取本线程中使用的 SqlSession 对象;
     * 
     * @return
     */
    public static SqlSession openSession() {
        // 从 ThreadLocal 对象中获得 Session 对象
        SqlSession sqlSession = (SqlSession) threadLocal.get();
        // 如果 ThreadLocal 对象中没有当前线程的 SqlSession 对象，则新建一个 SqlSession 对象
        if (sqlSession == null) {
            // 通过 SqlSessionFactory 的 openSession 方法建立一个 Session 对象
            sqlSession = (sqlSessionFactory != null) ? sqlSessionFactory.openSession() : null;
            // 将 SqlSession 对象保存到 ThreadLocal 中
            if (sqlSession != null) {
                threadLocal.set(sqlSession);
            }
        }
        return sqlSession;
    }

    public static void clean() {
        sqlSessionFactory = null;
        if (threadLocal.get() != null) {
            threadLocal.get().close();
        }
        threadLocal.remove();
    }

}
