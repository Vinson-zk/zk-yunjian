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
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.util.Assert;

import com.mongodb.Block;
import com.mongodb.MongoClientSettings;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.connection.ClusterSettings;
import com.mongodb.connection.ClusterSettings.Builder;
import com.mongodb.connection.ConnectionPoolSettings;
import com.mongodb.internal.connection.ServerAddressHelper;
import com.zk.core.utils.ZKJsonUtils;

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
    // @ConditionalOnBean(name = { "zkMongoProperties" })
    @Bean("mongodbServerAddress")
    public List<ServerAddress> mongodbServerAddress(ZKMongoProperties zkMongoProperties) {
        Assert.notNull(zkMongoProperties.getUrl(),
                "[>_<: 20190822-1509-001] AccessReal: mongodb url must not be null!");
        return Arrays.asList(ServerAddressHelper.createServerAddress(zkMongoProperties.getUrl()));

    }

//    @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(MongoClientSettings.class)
    @ConditionalOnBean(value = { ZKMongoProperties.class })
    @Bean
    MongoClientSettings mongoClientSettings(List<ServerAddress> mongodbServerAddress,
            ZKMongoProperties zkMongoProperties) {
        Assert.notNull(zkMongoProperties.getDbname(),
                "[>_<: 20190822-1510-001] AccessReal: mongodb schema name must not be null!");

        System.out.println("[^_^:20230201-0810-001] ------------------------------------------------- ");
        System.out.println(
                "[^_^:20230201-0810-001] zkMongoProperties: " + ZKJsonUtils.writeObjectJson(zkMongoProperties));
        System.out.println("[^_^:20230201-0810-001] ------------------------------------------------- ");

        MongoClientSettings.Builder mongoClientSettingsBuilder = MongoClientSettings.builder();
        // ClusterSettings
        Block<ClusterSettings.Builder> blockClusterSettingsBuilder = new Block<ClusterSettings.Builder>() {
            @Override
            public void apply(Builder b) {
                b.hosts(mongodbServerAddress);
                b.srvServiceName(zkMongoProperties.getDbname());
            }
        };
        mongoClientSettingsBuilder = mongoClientSettingsBuilder.applyToClusterSettings(blockClusterSettingsBuilder);

        // ConnectionPoolSettings
        Block<ConnectionPoolSettings.Builder> blockConnectionPoolSettingsBuilder = new Block<ConnectionPoolSettings.Builder>() {
            @Override
            public void apply(ConnectionPoolSettings.Builder builder) {
//                    builder.maxSize(connectionPoolSize);
            }
        };
        mongoClientSettingsBuilder.applyToConnectionPoolSettings(blockConnectionPoolSettingsBuilder);

//            mongoClientSettingsBuilder.applyToServerSettings(block)
//            mongoClientSettingsBuilder.applyToSocketSettings(block)
//            mongoClientSettingsBuilder.applyToSslSettings(block)

        // MongoCredential
        MongoCredential mcc = MongoCredential.createCredential(zkMongoProperties.getUsername(),
                zkMongoProperties.getDbname(),
                zkMongoProperties.getPassword() == null ? null : zkMongoProperties.getPassword().toCharArray());
        mongoClientSettingsBuilder = mongoClientSettingsBuilder.credential(mcc);

        return mongoClientSettingsBuilder.build();
    }

//    @Configuration(proxyBeanMethods = false)
//    @ConditionalOnMissingBean(MongoClientSettings.class)
//    @Bean
//    MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(MongoProperties properties,
//            Environment environment) {
//        return new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
//    }

    @ConditionalOnBean(value = { MongoClientSettings.class }, name = { "mongodbServerAddress" })
    @Bean("mongoClient")
    public MongoClient mongoClient(MongoClientSettings mongoClientSettings) {
//        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        return mongoClient;
    }
  
    @ConditionalOnBean(value = { ZKMongoProperties.class, MongoClient.class }, name = { "mongoClient" })
    @Bean("mongoDatabaseFactory")
    public SimpleMongoClientDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient, ZKMongoProperties properties) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, properties.getDbname());
    }

    @ConditionalOnBean(name = { "mongoDatabaseFactory" })
    @Bean("mongoMoxydomainConverter")
    public MappingMongoConverter mongoMoxydomainConverter(MongoDatabaseFactory mongoDatabaseFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        mappingConverter.setMapKeyDotReplacement("\\+");
//        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @ConditionalOnBean(name = { "mongoDatabaseFactory", "mongoMoxydomainConverter" })
    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory,
            MappingMongoConverter mongoMoxydomainConverter) {
        return new MongoTemplate(mongoDatabaseFactory, mongoMoxydomainConverter);
    }

}
