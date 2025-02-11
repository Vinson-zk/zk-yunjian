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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.web.servlet.DispatcherServletRegistrationBean;
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

import jakarta.servlet.MultipartConfigElement;

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

    @Value("${zk.core.servlet.file.upload.multipartconfigelement.tempFilePath:tmp/spittr/uploads}")
    String tempFilePath;

    @Value("${zk.core.servlet.file.upload.multipartconfigelement.maxFileSize}")
    long maxFileSize;

    @Value("${zk.core.servlet.file.upload.multipartconfigelement.maxRequestSize}")
    long maxRequestSize;

    @Autowired
    public void dispatcherServletRegistrationBean(DispatcherServletRegistrationBean registration) {
        MultipartConfigElement multipartConfigElement = new MultipartConfigElement(tempFilePath, maxFileSize,
                maxRequestSize, 0);
        registration.setMultipartConfig(multipartConfigElement);
    }

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
