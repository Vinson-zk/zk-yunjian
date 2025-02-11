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
* @Title: ZKSysAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.sys.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 13, 2023 11:14:47 AM 
* @version V1.0 
*/
package com.zk.sys.configuration;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;

import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.configuration.ZKCoreThreadPoolProperties;
import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.framework.common.feign.interceptor.ZKFeignRequestInterceptor;
import com.zk.framework.common.feign.support.ZKFeignSpringFormEncoder;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.ticket.ZKSecTicketManager;

/** 
* @ClassName: ZKSysAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableLog
@ZKEnableRedis
@ZKEnableDB
public class ZKSysAfterConfiguration {

    @Value("${spring.application.name}")
    private String appName;

    /**
     * 缓存管理
     *
     * @Title: redisCacheManager
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date May 11, 2022 7:17:31 PM
     * @param jedisOperatorStringKey
     * @return
     * @return ZKRedisCacheManager
     */
    @Bean
    ZKRedisCacheManager redisCacheManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        ZKRedisCacheManager cm = new ZKRedisCacheManager();
        cm.setJedisOperator(jedisOperatorStringKey);
        return cm;
    }

    @Bean
    ZKFeignRequestInterceptor feignRequestInterceptor(ZKSecTicketManager redisTicketManager) {
        ZKFeignRequestInterceptor feignRequestInterceptor = new ZKFeignRequestInterceptor(redisTicketManager,
                this.appName);
        return feignRequestInterceptor;
    }

    @Bean
    ZKFeignSpringFormEncoder feignFormEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        ZKFeignSpringFormEncoder feignSpringFormEncoder = new ZKFeignSpringFormEncoder(
                new SpringEncoder(messageConverters));
        return feignSpringFormEncoder;
    }

    @ConditionalOnMissingBean(name = { "userOptLogThreadPoolProperties" })
    @ConfigurationProperties(prefix = "zk.sys.user.opt.log.thread.pool")
    @Bean("userOptLogThreadPoolProperties")
    ZKCoreThreadPoolProperties userOptLogThreadPoolProperties() {
        return new ZKCoreThreadPoolProperties();
    }


}
