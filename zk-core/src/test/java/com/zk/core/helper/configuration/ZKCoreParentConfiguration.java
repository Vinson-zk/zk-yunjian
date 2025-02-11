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
 * @Title: ZKCoreParentConfiguration.java 
 * @author Vinson 
 * @Package com.zk.core.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 12, 2019 5:15:13 PM 
 * @version V1.0   
*/
package com.zk.core.helper.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.env.Environment;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

import com.zk.core.commons.ZKFileTransfer;
import com.zk.core.commons.ZKValidatorMessageInterpolator;
import com.zk.core.commons.support.ZKDiskFileTransfer;
import com.zk.core.configuration.ZKEnableCore;
import com.zk.core.redis.configuration.ZKEnableRedis;
import com.zk.core.redis.configuration.ZKRedisProperties;

import jakarta.validation.Validator;

/**
 * @ClassName: ZKCoreParentConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@PropertySources(value = { //
        @PropertySource(ignoreResourceNotFound = true, encoding = "UTF-8", value = { //
                "classpath:test.zk.core.properties", //
                "classpath:redis/test_redis.properties", //
        }) //
})
@ZKEnableCore
@ZKEnableRedis
public class ZKCoreParentConfiguration {

    @Autowired
    Environment env;

    @Autowired
    public void step() {
        System.out.println("== step == -----------------------------------------------");
        System.out.println("[^_^:20190625-1716-001] zkCoreServerPort:"
                + env.getProperty("zk.core.server.port", Integer.class, 8080));
        System.out.println(
                "[^_^:20190625-1716-001] --- zk.core.server.port:" + env.getProperty("zk.core.server.port"));
        System.out.println("[^_^:20190625-1716-001] zk.path.admin:" + env.getProperty("zk.path.admin"));
        System.out.println("[^_^:20190625-1716-001] zk.path.core:" + env.getProperty("zk.path.core"));
//        System.out.println("[^_^:20230206-2014-001] redis: " + JSONObject.toJSONString(redisProperties));
        System.out
                .println("[^_^:20190625-1716-001] --- zk.default.locale:" + env.getProperty("zk.default.locale"));

    }

    @Bean("redisProperties")
    @ConfigurationProperties(prefix = "zk.core.redis")
    ZKRedisProperties redisProperties() {
        return new ZKRedisProperties();
    }

    @Bean
    ZKFileTransfer zkFileTransfer() {
        return new ZKDiskFileTransfer(-1, -1, -1, null);
    }

    @Bean
    ResourceBundleMessageSource messageSource() {
        System.out.println(ZKEnableCore.printLog + "ResourceBundleMessageSource --- [" + this.getClass().getSimpleName()
                + "] " + this.hashCode());
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        // 文件名不能包含 [.]，相同 key 配置在前面的生效，后配置的不会覆盖前面的
        messageSource.addBasenames("msg/test_msg");
        messageSource.setUseCodeAsDefaultMessage(true);
        messageSource.setCacheSeconds(3600);
        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }

    /**
     * 数据验证消息处理
     *
     * @Title: validator
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Dec 12, 2019 5:43:19 PM
     * @param messageSource
     * @param applicationContext
     * @return
     * @return Validator
     */
    @Bean
    Validator validator(MessageSource messageSource, ApplicationContext applicationContext) {
        LocalValidatorFactoryBean localValidatorFactoryBean = new LocalValidatorFactoryBean();
        ZKValidatorMessageInterpolator zkValidatorMessageInterpolator = new ZKValidatorMessageInterpolator(
                messageSource);
        localValidatorFactoryBean.setMessageInterpolator(zkValidatorMessageInterpolator);
//         localValidatorFactoryBean.setValidationMessageSource(messageSource);
        return localValidatorFactoryBean;
    }

}
