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
* @Title: ZKMongoConfiguration.java 
* @author Vinson 
* @Package com.zk.mongo.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 1:43:39 PM 
* @version V1.0 
*/
package com.zk.mongo.configuration;

import java.util.Arrays;
import java.util.List;

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

/**
 * 引入此配置，需要注入 bean[com.zk.mongo.configuration.ZKMongoProperties]
 * 
 * @ClassName: ZKMongoConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKMongoConfiguration {

    @ConditionalOnMissingBean(name = { "mongodbServerAddress" })
    @Bean("mongodbServerAddress")
    public List<ServerAddress> mongodbServerAddress(ZKMongoProperties zkMongoProperties) {
        System.out.println(ZKEnableMongo.printLog + "mongodbServerAddress --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        Assert.notNull(zkMongoProperties.getUrl(), "[>_<: 20190822-1509-001] AccessReal: mongodb url must not be null!");
        return Arrays.asList(ServerAddressHelper.createServerAddress(zkMongoProperties.getUrl()));

    }

    @ConditionalOnMissingBean(MongoClientSettings.class)
    @Bean
    MongoClientSettings mongoClientSettings(List<ServerAddress> mongodbServerAddress,
            ZKMongoProperties zkMongoProperties) {
        System.out.println(ZKEnableMongo.printLog + "mongoClientSettings --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        Assert.notNull(zkMongoProperties.getDbname(), "[>_<: 20190822-1510-001] AccessReal: mongodb schema name must not be null!");

        System.out.println(ZKEnableMongo.printLog + "[" + this.getClass().getSimpleName() + "] ------------------------------------------------- ");
        System.out.println(ZKEnableMongo.printLog + "zkMongoProperties.url: " + zkMongoProperties.getUrl());
        System.out.println(ZKEnableMongo.printLog + "zkMongoProperties.username: " + zkMongoProperties.getUsername());
        System.out.println(ZKEnableMongo.printLog + "zkMongoProperties.dbname: " + zkMongoProperties.getDbname());
        System.out.println(ZKEnableMongo.printLog + "[" + this.getClass().getSimpleName() + "] ------------------------------------------------- ");

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

    @ConditionalOnMissingBean(value = { MongoClient.class })
    @Bean("mongoClient")
    public MongoClient mongoClient(MongoClientSettings mongoClientSettings) {
        System.out.println(ZKEnableMongo.printLog + "mongoClient --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
//        MongoClientFactoryBean mongo = new MongoClientFactoryBean();
        MongoClient mongoClient = MongoClients.create(mongoClientSettings);
        return mongoClient;
    }

    @ConditionalOnMissingBean(value = { MongoDatabaseFactory.class })
    @Bean("mongoDatabaseFactory")
    public SimpleMongoClientDatabaseFactory mongoDatabaseFactory(MongoClient mongoClient,
            ZKMongoProperties properties) {
        System.out.println(ZKEnableMongo.printLog + "mongoDatabaseFactory --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        return new SimpleMongoClientDatabaseFactory(mongoClient, properties.getDbname());
    }

    @ConditionalOnMissingBean(value = { MappingMongoConverter.class })
    @Bean("mongoMoxydomainConverter")
    public MappingMongoConverter mongoMoxydomainConverter(MongoDatabaseFactory mongoDatabaseFactory) {
        System.out.println(ZKEnableMongo.printLog + "mongoMoxydomainConverter --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        DbRefResolver dbRefResolver = new DefaultDbRefResolver(mongoDatabaseFactory);
        MappingMongoConverter mappingConverter = new MappingMongoConverter(dbRefResolver, new MongoMappingContext());
        mappingConverter.setMapKeyDotReplacement("\\+");
//        mappingConverter.setCustomConversions(conversions);
        return mappingConverter;
    }

    @ConditionalOnMissingBean(value = { MongoTemplate.class })
    @Bean("mongoTemplate")
    public MongoTemplate mongoTemplate(MongoDatabaseFactory mongoDatabaseFactory,
            MappingMongoConverter mongoMoxydomainConverter) {
        System.out.println(ZKEnableMongo.printLog + "mongoTemplate --- [" + this.getClass().getSimpleName() + "] " + this.hashCode());
        return new MongoTemplate(mongoDatabaseFactory, mongoMoxydomainConverter);
    }

}
