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
* @Title: ZKEnableLog.java 
* @author Vinson 
* @Package com.zk.log.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 3:26:32 PM 
* @version V1.0 
*/
package com.zk.log.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

import com.zk.core.configuration.ZKCoreThreadPoolProperties;
import com.zk.log.configuration.ZKEnableLog.ZKLogInit;

/**
 * @ClassName: ZKEnableLog
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson s
 * @version 1.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(value = { ZKLogInit.class })
public @interface ZKEnableLog {
    
    public static final String printLog = "[^_^:20230209-2148-005] ----- zk-log config: ";

    @PropertySource(encoding = "UTF-8", value = { "classpath:zk.log.properties" })
    @ComponentScan(basePackages = { "com.zk.log.*" })
    public class ZKLogInit {

        public ZKLogInit(AbstractResourceBasedMessageSource messageSource) {
            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] =================================");

            System.out.println(printLog + "messageSource.addBasenames... [" + this.getClass().getSimpleName() + "] " + this.hashCode());
            messageSource.addBasenames("msg/zkMsg_log");

            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] ---------------------------------");
        }

        @ConditionalOnMissingBean(name = { "logThreadPoolProperties" })
        @ConfigurationProperties(prefix = "zk.log.save.thread.pool")
        @Bean("logThreadPoolProperties")
        ZKCoreThreadPoolProperties logThreadPoolProperties() {
            return new ZKCoreThreadPoolProperties();
        }

    }
    
}

