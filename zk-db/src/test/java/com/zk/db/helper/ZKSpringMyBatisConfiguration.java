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
 * @Title: ZKSpringMyBatisConfiguration.java 
 * @author Vinson 
 * @Package com.zk.db.dynamic.spring 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 11:32:59 AM 
 * @version V1.0   
*/
package com.zk.db.helper;

import org.springframework.context.annotation.ImportResource;

/** 
* @ClassName: ZKSpringMyBatisConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//@ImportResource(locations = { "classpath:mybatis/test_spring_dynamic_mybatis.xml" })
@ImportResource(locations = { "classpath:mybatis/test_spring_context_dynamic_mybatis.xml" })
public class ZKSpringMyBatisConfiguration {

//    @Value("${zk.db.dynamic.jdbc.username_w}")
//    private String dbUserName_w;
//
//    @Value("${zk.db.dynamic.jdbc.password_w}")
//    private String dbPwd_w;
//
//    @Value("${zk.db.dynamic.jdbc.username_r}")
//    private String dbUserName_r;
//
//    @Value("${zk.db.dynamic.jdbc.password_r}")
//    private String dbPwd_r;
//
//    @Autowired
//    ConfigurationPropertiesBindingPostProcessor configurationPropertiesBinder;
//
//    @Bean("zkDynamicDataSource")
//    public ZKDynamicDataSource dynamicDataSource() {
//
//        System.out.println("[^_^:20190821-0943-001] ===== ZKServerCentralConfigutation class dynamicDataSource ");
//        ZKDynamicDataSource dynamicDataSource = new ZKDynamicDataSource();
//
//        ZKDBParentDBSource dds_w = new ZKDBParentDBSource();
//        ZKDBParentDBSource dds_r = new ZKDBParentDBSource();
//
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - w url：" + dds_w.getUrl());
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - r url：" + dds_r.getUrl());
//
//        configurationPropertiesBinder.postProcessBeforeInitialization(dds_w, "parentDataSource");
//        configurationPropertiesBinder.postProcessBeforeInitialization(dds_r, "parentDataSource");
//
//        dds_w.setUsername(this.dbUserName_w);
//        dds_w.setPassword(dbPwd_w);
//
//        dds_r.setUsername(this.dbUserName_r);
//        dds_r.setPassword(dbPwd_r);
//
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - w：" + dds_w.hashCode());
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - w getUsername：" + dds_w.getUsername());
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - w url：" + dds_w.getUrl());
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - r ：" + dds_r.hashCode());
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - r getUsername：" + dds_r.getUsername());
//        System.out.println("[^_^:20190820-1752-001] DruidDataSource - r url：" + dds_r.getUrl());
//
//        dynamicDataSource.setWriteDataSource(dds_w);
//        dynamicDataSource.setReadDataSource(dds_r);
//        System.out.println("[^_^:20190821-0943-001] ----- ZKServerCentralConfigutation class dynamicDataSource ");
//
//        return dynamicDataSource;
//
//    }
//
//    @Bean("dynamicTransactionManager")
//    public ZKDynamicTransactionManager dynamicTransactionManager(ZKDynamicDataSource zkDynamicDataSource) {
//        ZKDynamicTransactionManager dynamicTransactionManager = new ZKDynamicTransactionManager();
//        dynamicTransactionManager.setDataSource(zkDynamicDataSource);
//        return dynamicTransactionManager;
//    }

}
