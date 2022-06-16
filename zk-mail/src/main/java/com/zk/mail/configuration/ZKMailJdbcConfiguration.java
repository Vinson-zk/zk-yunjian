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
* @Title: ZKMailJdbcConfiguration.java 
* @author Vinson 
* @Package com.zk.mail.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 8:46:16 AM 
* @version V1.0 
*/
package com.zk.mail.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
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
* @ClassName: ZKMailJdbcConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { ZKMailAfterConfiguration.class })
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_dynamic_mybatis.xml" })
@PropertySource(encoding = "UTF-8", value = { "classpath:zk.mail.jdbc.properties" })
public class ZKMailJdbcConfiguration {

    protected Logger log = LoggerFactory.getLogger(this.getClass());

    @Value("${zk.mail.db.dynamic.jdbc.username_w}")
    private String dbUserName_w;

    @Value("${zk.mail.db.dynamic.jdbc.password_w}")
    private String dbPwd_w;

    @Value("${zk.mail.db.dynamic.jdbc.username_r}")
    private String dbUserName_r;

    @Value("${zk.mail.db.dynamic.jdbc.password_r}")
    private String dbPwd_r;

    @Autowired
    ConfigurationPropertiesBindingPostProcessor configurationPropertiesBinder;

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
    @ConfigurationProperties(prefix = "zk.mail.db.dynamic.jdbc.druid.pool")
    public DruidDataSource parentDataSource() {
        return new DruidDataSource();
    }

    // 动态数据源
    @Bean("zkDynamicDataSource")
    public ZKDynamicDataSource zkDynamicDataSource() {

        ZKDynamicDataSource zkDynamicDataSource = new ZKDynamicDataSource();

        DruidDataSource dds_w = new DruidDataSource();
        DruidDataSource dds_r = new DruidDataSource();

        configurationPropertiesBinder.postProcessBeforeInitialization(dds_w, "parentDataSource");
        configurationPropertiesBinder.postProcessBeforeInitialization(dds_r, "parentDataSource");

        dds_w.setUsername(this.dbUserName_w);
        dds_w.setPassword(dbPwd_w);

        dds_r.setUsername(this.dbUserName_r);
        dds_r.setPassword(dbPwd_r);

        zkDynamicDataSource.setWriteDataSource(dds_w);
        zkDynamicDataSource.setReadDataSource(dds_r);

        log.info("[^_^:20210225-1147-001] ====================================================");
        log.info("[^_^:20210225-1147-001] === 数据库信息 =======================================");
        log.info("[^_^:20210225-1147-001] ====================================================");
        log.info("[^_^:20210225-1147-001] 数据库：" + dds_w.getUrl());
        log.info("[^_^:20210225-1147-001] ====================================================");

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
