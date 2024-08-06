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

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;

import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.db.configuration.ZKDBProperties;
import com.zk.db.configuration.ZKEnableDB;

/** 
* @ClassName: ZKSpringMyBatisConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
//@ImportResource(locations = { "classpath:mybatis/test_spring_context_dynamic_mybatis.xml" })
@PropertySources(value = { // 
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { "classpath:test.zk.db.source.properties" }), // 
})
@ZKEnableCoreServlet
@ZKEnableDB(configLocation = "classpath:mybatis/test_mybatis_config.xml", 
    mapperLocations = {"classpath*:mappers/**/*.xml" })
public class ZKSpringMyBatisConfiguration {

    @Bean("zkDBProperties")
    @ConfigurationProperties(prefix = "zk.db.dynamic.jdbc")
    ZKDBProperties zkDBProperties() {
        return new ZKDBProperties();
    }

//    @Bean
//    public ZKEnvironment zkEnvironment(ApplicationContext ctx) {
//        ZKEnvironmentUtils.setZKEnvironment(new ZKEnvironment(ctx));
//        return ZKEnvironmentUtils.getZKEnvironment();
//    }

//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.setUseCodeAsDefaultMessage(true);
//        messageSource.setCacheSeconds(3600);
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

}
