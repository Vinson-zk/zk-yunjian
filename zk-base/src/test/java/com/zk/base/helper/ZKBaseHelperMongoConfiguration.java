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

import java.util.Arrays;
import java.util.List;

import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoClientDatabaseFactory;
import org.springframework.data.mongodb.core.convert.DbRefResolver;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
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
import com.zk.base.mongo.repository.support.ZKMongoRepositoryImpl;
import com.zk.core.utils.ZKJsonUtils;
import com.zk.mongo.configuration.ZKMongoProperties;

/** 
* @ClassName: ZKBaseHelperConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@EnableMongoRepositories(repositoryBaseClass = ZKMongoRepositoryImpl.class)
@PropertySources(value = { @PropertySource(value = {
        "classpath:test.zk.base.mongodb.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8") })
public class ZKBaseHelperMongoConfiguration {

    @Bean
    @ConfigurationProperties(prefix = "zk.base.mongodb")
    public ZKMongoProperties mongoProperties() {
        return new ZKMongoProperties();
    }

    @Bean
    public List<ServerAddress> mongodbServerAddress(ZKMongoProperties zkMongoProperties) {
        System.out.println("[^_^:20230201-0810-001] ------------------------------------------------- ");
        System.out.println(
                "[^_^:20230201-0810-001] zkMongoProperties: " + ZKJsonUtils.writeObjectJson(zkMongoProperties));
        System.out.println("[^_^:20230201-0810-001] ------------------------------------------------- ");
        return Arrays.asList(ServerAddressHelper.createServerAddress(zkMongoProperties.getUrl()));
    }

//  @Configuration(proxyBeanMethods = false)
    @ConditionalOnMissingBean(MongoClientSettings.class)
    @ConditionalOnBean(value = { ZKMongoProperties.class })
    @Bean
    MongoClientSettings mongoClientSettings(List<ServerAddress> mongodbServerAddress,
            ZKMongoProperties zkMongoProperties) {

        Assert.notNull(zkMongoProperties.getDbname(),
                "[>_<: 20190822-1510-001] AccessReal: mongodb schema name must not be null!");

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
//                  builder.maxSize(connectionPoolSize);
            }
        };
        mongoClientSettingsBuilder.applyToConnectionPoolSettings(blockConnectionPoolSettingsBuilder);

//          mongoClientSettingsBuilder.applyToServerSettings(block)
//          mongoClientSettingsBuilder.applyToSocketSettings(block)
//          mongoClientSettingsBuilder.applyToSslSettings(block)

        // MongoCredential
        MongoCredential mcc = MongoCredential.createCredential(zkMongoProperties.getUsername(),
                zkMongoProperties.getDbname(),
                zkMongoProperties.getPassword() == null ? null : zkMongoProperties.getPassword().toCharArray());
        mongoClientSettingsBuilder = mongoClientSettingsBuilder.credential(mcc);

        return mongoClientSettingsBuilder.build();
    }

//  @Configuration(proxyBeanMethods = false)
//  @ConditionalOnMissingBean(MongoClientSettings.class)
//  @Bean
//  MongoPropertiesClientSettingsBuilderCustomizer mongoPropertiesCustomizer(MongoProperties properties,
//          Environment environment) {
//      return new MongoPropertiesClientSettingsBuilderCustomizer(properties, environment);
//  }

    @ConditionalOnBean(value = { MongoClientSettings.class }, name = { "mongodbServerAddress" })
    @Bean("mongoClient")
    public MongoClient mongoClient(MongoClientSettings mongoClientSettings) {
//      MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        return mongoClient;
    }

    @ConditionalOnBean(value = { ZKMongoProperties.class, MongoClient.class }, name = { "mongoClient" })
    @Bean("mongoDatabaseFactory")
    public SimpleMongoClientDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient,
            ZKMongoProperties properties) {
        return new SimpleMongoClientDatabaseFactory(mongoClient, properties.getDbname());
    }

    @ConditionalOnBean(name = { "mongoDatabaseFactory" })
    @Bean("mongoMoxydomainConverter")
    public MappingMongoConverter mongoMoxydomainConverter(MongoDatabaseFactory mongoDatabaseFactory) {
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        mappingConverter.setMapKeyDotReplacement("\\+");
//      mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @ConditionalOnBean(name = { "mongoDatabaseFactory", "mongoMoxydomainConverter" })
    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory,
            MappingMongoConverter mongoMoxydomainConverter) {
        return new MongoTemplate(mongoDatabaseFactory, mongoMoxydomainConverter);
    }

}
