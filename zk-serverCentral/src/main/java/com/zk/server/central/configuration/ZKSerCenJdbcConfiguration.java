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
* @Title: ZKSerCenJdbcConfiguration.java 
* @author Vinson 
* @Package com.zk.server.central.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 5, 2023 12:38:29 AM 
* @version V1.0 
*/
package com.zk.server.central.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.PropertySource;

import com.alibaba.druid.pool.DruidDataSource;
import com.zk.db.dynamic.spring.dataSource.ZKDynamicDataSource;
import com.zk.db.dynamic.spring.transaction.ZKDynamicTransactionManager;

/** 
* @ClassName: ZKSerCenJdbcConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_sc_dynamic_mybatis.xml" })
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.ser.cen.jdbc.properties" })
public class ZKSerCenJdbcConfiguration {

    @Value("${zk.ser.cen.db.dynamic.jdbc.username_w}")
    private String dbUserName_w;

    @Value("${zk.ser.cen.db.dynamic.jdbc.password_w}")
    private String dbPwd_w;

    @Value("${zk.ser.cen.db.dynamic.jdbc.username_r}")
    private String dbUserName_r;

    @Value("${zk.ser.cen.db.dynamic.jdbc.password_r}")
    private String dbPwd_r;

    /**
     * 数据源
     *
     * @Title: parentDataSource
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Oct 28, 2019 3:02:07 PM
     * @return
     * @return DruidDataSource
     */
//    @Primary
    @Bean("parentDataSource")
    @ConfigurationProperties(prefix = "zk.ser.cen.db.dynamic.jdbc.druid.pool")
    public DruidDataSource parentDataSource() {
        return new DruidDataSource();
    }

    // 动态数据源
    @ConditionalOnClass(value = { ConfigurationPropertiesBindingPostProcessor.class })
    @Bean("zkDynamicDataSource")
    public ZKDynamicDataSource zkDynamicDataSource(
            ConfigurationPropertiesBindingPostProcessor configurationPropertiesBinder) {

        ZKDynamicDataSource zkDynamicDataSource = new ZKDynamicDataSource();

        DruidDataSource dds_w = new DruidDataSource();
        DruidDataSource dds_r = new DruidDataSource();

        configurationPropertiesBinder.postProcessBeforeInitialization(dds_w, "parentDataSource");
        configurationPropertiesBinder.postProcessBeforeInitialization(dds_r, "parentDataSource");

        dds_w.setUsername(this.dbUserName_w);
        dds_w.setPassword(dbPwd_w);

        dds_r.setUsername(this.dbUserName_r);
        dds_r.setPassword(dbPwd_r);

        System.out.println("[^_^:20220521-1030-001] ====================================================");
        System.out.println("[^_^:20220521-1030-001] 数据库链接：" + dds_w.getUrl());
        System.out.println("[^_^:20220521-1030-001] ====================================================");

        zkDynamicDataSource.setWriteDataSource(dds_w);
        zkDynamicDataSource.setReadDataSource(dds_r);

        return zkDynamicDataSource;
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
    @Bean("dynamicTransactionManager")
    public ZKDynamicTransactionManager dynamicTransactionManager(ZKDynamicDataSource zkDynamicDataSource) {
        ZKDynamicTransactionManager zkDynamicTransactionManager = new ZKDynamicTransactionManager();
        zkDynamicTransactionManager.setDataSource(zkDynamicDataSource);
        return zkDynamicTransactionManager;
    }

}
