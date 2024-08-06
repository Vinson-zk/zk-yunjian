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
* @Title: ZKSecTestHelperMongoConfiguration.java 
* @author Vinson 
* @Package com.zk.security.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 4, 2021 12:42:48 AM 
* @version V1.0 
*/
package com.zk.security.helper;

import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.zk.mongo.configuration.ZKEnableMongo;
import com.zk.mongo.configuration.ZKMongoProperties;
import com.zk.security.ticket.support.mongo.ZKSecMongoTicketManager;

/** 
* @ClassName: ZKSecTestHelperMongoConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
//@Configuration
@PropertySources(value = { @PropertySource(value = {
        "classpath:test.zk.sec.mongo.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8") })
@AutoConfigureBefore(value = { ZKEnableMongo.class })
public class ZKSecTestHelperMongoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "zk.sec.mongodb")
    public ZKMongoProperties zkMongoProperties() {
        return new ZKMongoProperties();
    }

    @Bean
    public ZKSecMongoTicketManager zkSecMongoTicketManager(MongoTemplate mongoTemplate) {
        return new ZKSecMongoTicketManager(mongoTemplate);
    }

}
