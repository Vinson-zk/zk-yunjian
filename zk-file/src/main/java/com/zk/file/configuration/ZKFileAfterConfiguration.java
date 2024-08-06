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
* @Title: ZKFileAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.file.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 21, 2022 11:44:30 AM 
* @version V1.0 
*/
package com.zk.file.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.cache.ZKCacheManager;
import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.support.ZKDiskFileTransfer;
import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.file.service.ZKFileDisposeService;
import com.zk.framework.common.feign.interceptor.ZKFeignRequestInterceptor;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

/** 
* @ClassName: ZKFileAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableLog
@ZKEnableRedis
@ZKEnableDB
@ZKEnableSecurity
@ZKEnableWebmvc
public class ZKFileAfterConfiguration implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    private String appName;

    // # 文件上传，最大上传大小，需要比邮件附件单个的文件大小配置值要大；50M=52428800
    @Value("${zk.file.upload.multipartResolver.maxInMemorySize:52428800}")
    long maxUploadSize;

    // # 文件上传，最大处理内存大小； 1M=1048576
    @Value("${zk.file.upload.multipartResolver.maxInMemorySize:1048576}")
    int maxInMemorySize;

    // # 文件上传，单个文件上传最大大小； 10M=10485760
    @Value("${zk.file.upload.multipartResolver.maxUploadSizePerFile:10485760}")
    int maxUploadSizePerFile;

    // # 文件上传，处理字符集
    @Value("${zk.file.upload.multipartResolver.defaultEncoding:UTF-8}")
    String defaultEncoding;

    /**
     * 文件上传 适配器
     *
     * @Title: multipartResolver
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 27, 2022 10:13:32 AM
     * @return
     * @return CommonsMultipartResolver
     */
//    @Bean
//    CommonsMultipartResolver multipartResolver() {
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding(this.defaultEncoding);
//        // 设置总上传数据总大小
//        multipartResolver.setMaxUploadSize(this.maxUploadSize);
//        multipartResolver.setMaxInMemorySize(this.maxInMemorySize);
//        // 设置单个文件最大大小
//        multipartResolver.setMaxUploadSizePerFile(maxUploadSizePerFile);
//        return multipartResolver;
//    }

    @Bean
    ZKFeignRequestInterceptor feignRequestInterceptor(ZKSecTicketManager redisTicketManager) {
        ZKFeignRequestInterceptor feignRequestInterceptor = new ZKFeignRequestInterceptor(redisTicketManager,
                this.appName);
        return feignRequestInterceptor;
    }

    /**
     * 项目的缓存管理
     *
     * @Title: zkCacheManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Nov 7, 2021 3:26:49 PM
     * @param jedisOperator
     * @return
     * @return ZKCacheManager<String>
     */
    @Bean(name = "zkCacheManager")
    ZKCacheManager<String> zkCacheManager(ZKJedisOperatorStringKey jedisOperator) {
        ZKRedisCacheManager zkCacheManager = new ZKRedisCacheManager();
        zkCacheManager.setJedisOperator(jedisOperator);
        return zkCacheManager;
    }

    @Bean(name = "fileDisposeService")
    ZKFileDisposeService fileDisposeService() {
        ZKFileDisposeService fds = new ZKFileDisposeService();
        return fds;
    }

    @Bean(name = "fileTransfer")
    ZKFileTransfer fileTransfer() {
        ZKDiskFileTransfer diskFileTransfer = new ZKDiskFileTransfer(0, 0, -1, null);
        return diskFileTransfer;
    }

}
