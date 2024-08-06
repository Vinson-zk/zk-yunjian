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
 * @Title: ZKDBMybatisJavaConfigSessionFactory.java 
 * @author Vinson 
 * @Package com.zk.db.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:06:27 AM 
 * @version V1.0   
*/
package com.zk.db.helper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.apache.ibatis.datasource.pooled.PooledDataSourceFactory;
import org.apache.ibatis.mapping.Environment;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import org.apache.ibatis.transaction.TransactionFactory;
import org.apache.ibatis.transaction.jdbc.JdbcTransactionFactory;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;

import com.zk.core.commons.data.ZKJson;
import com.zk.core.commons.data.ZKOrder;
import com.zk.core.commons.data.ZKPage;
import com.zk.db.ZKMybatisSessionFactory;
import com.zk.db.helper.dao.ZKDBTestDao;
import com.zk.db.helper.entity.ZKDBTestSampleEntity;
import com.zk.db.mybatis.plugins.ZKCustomParams;
import com.zk.db.mybatis.plugins.ZKSamplePlugins;
import com.zk.db.mybatis.plugins.ZKSqlLikeParameter;
import com.zk.db.mybatis.plugins.ZKSqlPage;
import com.zk.db.mybatis.plugins.ZKSqlRunCost;

/** 
* @ClassName: ZKDBMybatisJavaConfigSessionFactory 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SuppressWarnings("deprecation")
public class ZKDBMybatisJavaConfigSessionFactory extends ZKMybatisSessionFactory {

    protected static Logger logger = LogManager.getLogger(ZKDBMybatisJavaConfigSessionFactory.class);

    private static ZKDBMybatisJavaConfigSessionFactory myBatisSessionFactory = null;

    /**
     * 单例模式
     */
    private ZKDBMybatisJavaConfigSessionFactory() {
        
    }

    // 属性
    private static Properties properties = new Properties();

    // 属性源文件加载器
    private static ResourceLoader resourceLoader = new DefaultResourceLoader();

    public static ZKDBMybatisJavaConfigSessionFactory creatBean(String configPath) {
        if (myBatisSessionFactory == null) {
            myBatisSessionFactory = new ZKDBMybatisJavaConfigSessionFactory();
            InputStreamReader is = null;
            try {
                Resource resource = resourceLoader.getResource(configPath);
                is = new InputStreamReader(resource.getInputStream());
                properties.load(is);
            }
            catch(IOException ex) {
                ex.printStackTrace();
                logger.error(String.format("Could not load properties from path:%s; message:%s", configPath,
                        ex.getMessage()));
            } finally {
                IOUtils.closeQuietly(is);
            }
            myBatisSessionFactory.builder();
        }
        return myBatisSessionFactory;
    }

    public SqlSessionFactory builder() {
        rebuildSqlSessionFactory();
        return sqlSessionFactory;
    }

    /**
     * 重建立 SqlSessionFactory 对象
     */
    public void rebuildSqlSessionFactory() {
        try {

            Properties ps = new Properties();
            ps.setProperty("driver", "com.mysql.jdbc.Driver");
            ps.setProperty("url", properties.getProperty("zk.db.dynamic.jdbc.publicDruidPool.url"));
            ps.setProperty("username", properties.getProperty("zk.db.dynamic.jdbc.writeUserName"));
            ps.setProperty("password", properties.getProperty("zk.db.dynamic.jdbc.writePwd"));
            PooledDataSourceFactory pooledDataSourceFactory = new PooledDataSourceFactory();
            pooledDataSourceFactory.setProperties(ps);
            DataSource dataSource = pooledDataSourceFactory.getDataSource();
            // 事务
            TransactionFactory transactionFactory = new JdbcTransactionFactory();
            Environment environment = new Environment("development", transactionFactory, dataSource);

            Configuration configuration = new Configuration(environment);

            // registry type handler
            configuration.getTypeHandlerRegistry().register("com.zk.db.mybatis.typeHandler");

            /*
             * mappers 的配置要放在 type handler registry 后面，否则可能会报
             * org.apache.ibatis.builder.BuilderException: Could not find value
             * method on SQL annotation. Cause: java.lang.IllegalStateException:
             * Type handler was null on parameter mapping for property 'xxx'. It
             * was either not specified and/or could not be found for the
             * javaType (xxx) : jdbcType (null) combination.
             */
            // add mappers
//          configuration.addMappers("com.zk.db.helper");
            configuration.addMapper(ZKDBTestDao.class);
            // register type alias
            configuration.getTypeAliasRegistry().registerAlias("page", ZKPage.class);
            configuration.getTypeAliasRegistry().registerAlias("json", ZKJson.class);
            configuration.getTypeAliasRegistry().registerAlias("order", ZKOrder.class);
            configuration.getTypeAliasRegistry().registerAlias("testEntity", ZKDBTestSampleEntity.class);

            // add plugins interceptor
            configuration.addInterceptor(new ZKSqlLikeParameter());
            configuration.addInterceptor(new ZKSqlPage());
            configuration.addInterceptor(new ZKSamplePlugins());
            configuration.addInterceptor(new ZKCustomParams());
            configuration.addInterceptor(new ZKSqlRunCost());

            sqlSessionFactory = new SqlSessionFactoryBuilder().build(configuration);
            logger.info("[^_^:20180309-1110-001] 创建 myBatis SqlSessionFactory 对象成功！ ");
        }
        catch(Exception e) {
            sqlSessionFactory = null;
            logger.error("[>_<:20180309-1110-002]: 创建 myBatis SqlSessionFactory 对象失败！ ");
            e.printStackTrace();
        }
    }

}
