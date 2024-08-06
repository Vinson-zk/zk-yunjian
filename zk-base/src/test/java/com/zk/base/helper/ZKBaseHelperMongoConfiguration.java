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
 * @Title: ZKBaseHelperConfiguration.java 
 * @author Vinson 
 * @Package com.zk.base.helper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 19, 2019 3:37:51 PM 
 * @version V1.0   
*/
package com.zk.base.helper;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.zk.base.mongo.repository.support.ZKMongoRepositoryImpl;
import com.zk.mongo.configuration.ZKEnableMongo;
import com.zk.mongo.configuration.ZKMongoProperties;

/** 
* @ClassName: ZKBaseHelperConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@EnableMongoRepositories(repositoryBaseClass = ZKMongoRepositoryImpl.class)
@PropertySources(value = { // 
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { "classpath:test.zk.base.mongodb.properties" }),//
})
@ZKEnableMongo
public class ZKBaseHelperMongoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "zk.base.mongodb")
    public ZKMongoProperties mongoProperties() {
        return new ZKMongoProperties();
    }

}
