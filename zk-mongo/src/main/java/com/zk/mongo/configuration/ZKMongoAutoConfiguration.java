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
 * @Title: ZKMongoAutoConfiguration.java 
 * @author Vinson 
 * @Package com.zk.mongo.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 6:00:22 PM 
 * @version V1.0   
*/
package com.zk.mongo.configuration;

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.Assert;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.internal.connection.ServerAddressHelper;

/**
 * 引入此自动配置，需要注入 bean[com.zk.mongo.configuration.ZKMongoProperties]
 * 
 * 这里要注意引入配置的顺序，如果使用了 spring boot 的自动配置 @SpringBootApplication 要注意配置的顺序，要在
 * org.springframework.boot.autoconfigure.mongo.MongoAutoConfiguration
 * 类之前；一般在 @SpringBootApplication 注解下紧接着引入配置即可。
 * 
 * @ClassName: ZKMongoAutoConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKMongoAutoConfiguration {

    @ConditionalOnBean(value = { ZKMongoProperties.class })
//  @ConditionalOnBean(name = { "zkMongoProperties" })
    @Bean("mongodbServerAddress")
    public List<ServerAddress> mongodbServerAddress(ZKMongoProperties zkMongoProperties) {
        Assert.notNull(zkMongoProperties.getUrl(),
                "[>_<: 20190822-1509-001] AccessReal: mongodb url must not be null!");
        return Arrays.asList(ServerAddressHelper.createServerAddress(zkMongoProperties.getUrl()));

    }

    @ConditionalOnBean(value = { ZKMongoProperties.class }, name = { "mongodbServerAddress" })
    @Bean("mongoClient")
    public MongoClient mongoClient(List<ServerAddress> mongodbServerAddress,
            ZKMongoProperties zkMongoProperties) {
        Assert.notNull(zkMongoProperties.getDbname(),
                "[>_<: 20190822-1510-001] AccessReal: mongodb schema name must not be null!");

        MongoCredential mcc = MongoCredential.createCredential(zkMongoProperties.getUsername(),
                zkMongoProperties.getDbname(),
                zkMongoProperties.getPassword() == null ? null : zkMongoProperties.getPassword().toCharArray());
        return new MongoClient(mongodbServerAddress, mcc, new MongoClientOptions.Builder().build());

    }

    @ConditionalOnBean(value = { ZKMongoProperties.class }, name = { "mongoClient" })
    @Bean("mongoDbFactory")
    public SimpleMongoDbFactory mongoDbFactory(MongoClient mongoClient, ZKMongoProperties zkMongoProperties) {
        return new SimpleMongoDbFactory(mongoClient, zkMongoProperties.getDbname());
    }

    @ConditionalOnBean(name = { "mongoDbFactory" })
    @Bean("mongoMoxydomainConverter")
    public MappingMongoConverter mongoMoxydomainConverter(MongoDbFactory mongoDbFactory) {

        MappingMongoConverter mmc = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory),
                new MongoMappingContext());

        mmc.setMapKeyDotReplacement("\\+");

        return mmc;
    }

    @ConditionalOnBean(name = { "mongoDbFactory", "mongoMoxydomainConverter" })
    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mongoMoxydomainConverter) {
        return new MongoTemplate(mongoDbFactory, mongoMoxydomainConverter);
    }

}
