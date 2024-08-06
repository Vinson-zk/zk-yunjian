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
* @Title: ZKWechatAfterConfiguration.java 
* @author Vinson 
* @Package com.zk.wechat.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 16, 2022 11:31:25 AM 
* @version V1.0 
*/
package com.zk.wechat.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.zk.cache.ZKCacheManager;
import com.zk.cache.redis.ZKRedisCacheManager;
import com.zk.core.configuration.ZKEnableCoreServlet;
import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.db.configuration.ZKEnableDB;
import com.zk.framework.common.feign.interceptor.ZKFeignRequestInterceptor;
import com.zk.log.configuration.ZKEnableLog;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.webmvc.configuration.ZKEnableWebmvc;

/** 
* @ClassName: ZKWechatAfterConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@ZKEnableCoreServlet
@ZKEnableLog
@ZKEnableRedis
@ZKEnableDB
@ZKEnableWebmvc
@ZKEnableSecurity
public class ZKWechatAfterConfiguration implements WebMvcConfigurer {

    @Value("${spring.application.name}")
    private String appName;

    @Bean
    ZKFeignRequestInterceptor feignRequestInterceptor(ZKSecTicketManager redisTicketManager) {
        ZKFeignRequestInterceptor feignRequestInterceptor = new ZKFeignRequestInterceptor(redisTicketManager, this.appName);
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

}
