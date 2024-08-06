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
* @Title: ZKDBSourceConfiguration.java 
* @author Vinson 
* @Package com.zk.db.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 5:25:03 PM 
* @version V1.0 
*/
package com.zk.db.configuration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.ibatis.session.SqlSessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKStringUtils;
import com.zk.db.dynamic.spring.dataSource.ZKDynamicDataSource;
import com.zk.db.dynamic.spring.transaction.ZKDynamicTransactionManager;
import com.zk.db.mybatis.spring.ZKDBSqlSessionFactoryBean;

import jakarta.annotation.PostConstruct;

/** 
* @ClassName: ZKDBSourceConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDBSourceConfiguration {

    public ZKDBSourceConfiguration() {
        System.out.println(ZKEnableDB.printLog + "[" + this.getClass().getSimpleName() + "]");
    }

    @Autowired
    public void Autowired() {
        System.out.println(ZKEnableDB.printLog + "@Autowired: [" + this.getClass().getSimpleName() + "]");
    }

    @PostConstruct
    public void PostConstruct() {
        System.out.println(ZKEnableDB.printLog + "@PostConstruct: [" + this.getClass().getSimpleName() + "]");
    }

    //
    String typeAliasesPackage;

    Class<?> typeAliasesSuperType;

    // mapper xml 配置文件地址
    String[] mapperLocations;

    // mybatis xml 配置文件地址
    String configLocation;

    public void setTypeAliasesPackage(String typeAliasesPackage) {
        this.typeAliasesPackage = typeAliasesPackage;
    }

    public void setTypeAliasesSuperType(Class<?> typeAliasesSuperType) {
        this.typeAliasesSuperType = typeAliasesSuperType;
    }

    public void setMapperLocations(String[] mapperLocations) {
        this.mapperLocations = mapperLocations;
    }

    public void setConfigLocation(String configLocation) {
        this.configLocation = configLocation;
    }
    
    @Primary
    // 动态数据源
    @DependsOn(value = { "zkDBProperties" })
    @ConditionalOnMissingBean(value = { ZKDynamicDataSource.class })
    @Bean("zkDynamicDataSource")
    public ZKDynamicDataSource dynamicDataSource(ZKDBProperties zkDBProperties) throws CloneNotSupportedException {
        System.out.println(ZKEnableDB.printLog + "dynamicDataSource --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        if (zkDBProperties.getPublicDruidPool() == null) {
            throw new ZKUnknownException("PublicDruidPool in ZKDBProperties cannot be null");
        }

        ZKDynamicDataSource zkDynamicDataSource = new ZKDynamicDataSource();

        DruidDataSource ddsWrite = (DruidDataSource) zkDBProperties.getPublicDruidPool().clone();
        DruidDataSource ddsRead = (DruidDataSource) zkDBProperties.getPublicDruidPool().clone();
//        // 根据注解的 parentDataSource 数据源，初始化 读写两个数据子数据源
//        configurationPropertiesBinder.postProcessBeforeInitialization(ddsWrite, "parentDataSource");
//        configurationPropertiesBinder.postProcessBeforeInitialization(ddsRead, "parentDataSource");
        // 设置读写的两个子数据源的用户名称和密码
        ddsWrite.setUsername(zkDBProperties.getWriteUserName());
        ddsWrite.setPassword(zkDBProperties.getWritePwd());
        ddsRead.setUsername(zkDBProperties.getReadUserName());
        ddsRead.setPassword(zkDBProperties.getReadPwd());
        // 如果有分给给读写数据配置连接地址，则设置子数据源
        if (!ZKStringUtils.isEmpty(zkDBProperties.getWriteUrl())) {
            ddsWrite.setUrl(zkDBProperties.getWriteUrl());
        }
        if (!ZKStringUtils.isEmpty(zkDBProperties.getReadUrl())) {
            ddsRead.setUrl(zkDBProperties.getReadUrl());
        }

        zkDynamicDataSource.setWriteDataSource(ddsWrite);
        zkDynamicDataSource.setReadDataSource(ddsRead);

        System.out.println(ZKEnableDB.printLog + "database info [" + this.getClass().getSimpleName()
                + "] ==================================================");
        System.out.println(ZKEnableDB.printLog + "写数库: username:" + ddsWrite.getUsername() + "; 链接：" + ddsWrite.getUrl());
        System.out.println(ZKEnableDB.printLog + "读数据: username:" + ddsRead.getUsername() + "; 链接：" + ddsRead.getUrl());
        System.out.println(ZKEnableDB.printLog + "database info [" + this.getClass().getSimpleName()
                + "] --------------------------------------------------");

        return zkDynamicDataSource;
    }

    /**
     * 
     *
     * @Title: sqlSessionFactory
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 10, 2023 6:38:34 PM
     * @param dynamicDataSource
     * @return
     * @throws IOException
     * @return ZKDBSqlSessionFactoryBean
     */
    @DependsOn(value = { "zkEnvironment" })
    @ConditionalOnMissingBean(value = { SqlSessionFactory.class })
    @Bean("sqlSessionFactory")
    public ZKDBSqlSessionFactoryBean sqlSessionFactory(ZKDynamicDataSource dynamicDataSource) throws IOException {
        System.out.println(ZKEnableDB.printLog + "sqlSessionFactory --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        ZKDBSqlSessionFactoryBean sqlSessionFactoryBean = new ZKDBSqlSessionFactoryBean();
        sqlSessionFactoryBean.setDataSource(dynamicDataSource);
        sqlSessionFactoryBean.setTypeAliasesPackage(typeAliasesPackage);
        sqlSessionFactoryBean.setTypeAliasesSuperType(typeAliasesSuperType);

        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        List<Resource> rs = null;
        if (this.mapperLocations != null && this.mapperLocations.length > 0) {
            rs = new ArrayList<>();
            for (String rpath : this.mapperLocations) {
                rs.addAll(Arrays.asList(resolver.getResources(rpath)));
            }
            sqlSessionFactoryBean.setMapperLocations(rs.toArray(new Resource[rs.size()]));
        }

        if (this.configLocation != null) {
            sqlSessionFactoryBean.setConfigLocation(resolver.getResource(this.configLocation));
        }

//        sqlSessionFactoryBean.setPlugins(new Interceptor[] { new ZKDynamicDataSourcePlugin() });
        return sqlSessionFactoryBean;
    }

    /**
     * 动态数据源事务
     *
     * @Title: dynamicTransactionManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:02:16 PM
     * @param zkDynamicDataSource
     * @return
     * @return ZKDynamicTransactionManager
     */
    @ConditionalOnMissingBean(value = { ZKDynamicTransactionManager.class })
    @Bean("dynamicTransactionManager")
    public ZKDynamicTransactionManager dynamicTransactionManager(ZKDynamicDataSource dynamicDataSource) {
        System.out.println(ZKEnableDB.printLog + "dynamicTransactionManager --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        ZKDynamicTransactionManager zkDynamicTransactionManager = new ZKDynamicTransactionManager();
        zkDynamicTransactionManager.setDataSource(dynamicDataSource);
        return zkDynamicTransactionManager;
    }

}
