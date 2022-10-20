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

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.convert.DefaultDbRefResolver;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.core.mapping.MongoMappingContext;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.internal.connection.ServerAddressHelper;
import com.zk.base.mongo.repository.support.ZKMongoRepositoryImpl;

/** 
* @ClassName: ZKBaseHelperConfiguration 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@EnableMongoRepositories(repositoryBaseClass = ZKMongoRepositoryImpl.class)
public class ZKBaseHelperMongoConfiguration {

    // # mongodb 服务器 URL; 集群是使用这个配置
    // 例：172.16.20.129:10001,172.16.20.129:10002,172.16.20.129:10003
    @Value("${zk.base.mongodb.url}")
    private String mongodbUrl;

    // mongodb 服务器 端口，mongodb 服务默认端口为 27017，集群时端口拼接在URL中，此项将不起作用
    @Value("${zk.base.mongodb.port:27017}")
    private int mongodbPort;

//    # mongodb 服务器实例名
    @Value("${zk.base.mongodb.dbname}")
    private String mongodbName;

//    # mongodb 用户名
    @Value("${zk.base.mongodb.username}")
    private String mongodbUserName;

//    # mongodb 用户名密码
    @Value("${zk.base.mongodb.password}")
    private String mongodbPassword;

//    @Bean
//    public Mongo mongo() {
//        return new Mongo(mongodbUrl, mongodbPort);
//    }

    @Bean
    public List<ServerAddress> serverAddress() {
        return Arrays.asList(ServerAddressHelper.createServerAddress(mongodbUrl, mongodbPort));
    }

    @Bean
    public MongoClient mongoClient(List<ServerAddress> serverAddress) {

        MongoCredential mcc = MongoCredential.createCredential(mongodbUserName, mongodbName,
                mongodbPassword.toCharArray());
        return new MongoClient(serverAddress, mcc, new MongoClientOptions.Builder().build());

    }

    @Bean
    public SimpleMongoDbFactory mongoDbFactory(MongoClient mongoClient) {
//        return new SimpleMongoDbFactory(mongoClient, mongodbUserName);
        return new SimpleMongoDbFactory(mongoClient, mongodbName);
    }

    @Bean
    public MappingMongoConverter mongoMoxydomainConverter(MongoDbFactory mongoDbFactory) {
        MappingMongoConverter mmc = new MappingMongoConverter(new DefaultDbRefResolver(mongoDbFactory),
                new MongoMappingContext());
        mmc.setMapKeyDotReplacement("\\+");
        return mmc;
    }

    @Bean
    public MongoTemplate mongoTemplate(MongoDbFactory mongoDbFactory, MappingMongoConverter mongoMoxydomainConverter) {
        return new MongoTemplate(mongoDbFactory, mongoMoxydomainConverter);
    }

}
