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
* @Title: ZKTestSpringBootMain.java 
* @author Vinson 
* @Package com.zk.test 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 20, 2020 11:43:28 PM 
* @version V1.0 
*/
package com.zk.demo;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;

import com.zk.core.utils.ZKJsonUtils;

/** 
* @ClassName: ZKTestSpringBootMain 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@SpringBootApplication
@EnableScheduling // @Scheduled 生效配置
@EnableAspectJAutoProxy // AOP @Aspect 生效配置
@PropertySources(@PropertySource(value = { "classpath:zk.test.properties" }))
public class ZKTestSpringBootMain {

    /**
     * 日志对象
     */
    static Logger log = LogManager.getLogger(ZKTestSpringBootMain.class);

    public static void main(String[] args) {

        // ALL < DEBUG < INFO < WARN < ERROR < FATAL < OFF
        System.out.println("[^_^:20201217-2157-001] " + ZKJsonUtils.toJsonStr(args));

        log.trace("[^_^:20200820-2021-001]=== log4j.trace");
        log.debug("[^_^:20200820-2021-002]=== log4j.debug");
        log.info("[^_^:20200820-2021-003]=== log4j.info");
        log.warn("[^_^:20200820-2021-004]=== log4j.warn");
        log.error("[^_^:20200820-2021-005]=== log4j.error");

        SpringApplication.run(ZKTestSpringBootMain.class, args);
    }

    @Bean
    public ThreadPoolTaskScheduler threadPoolTaskScheduler() {
        ThreadPoolTaskScheduler tpts = new ThreadPoolTaskScheduler();
//        tpts.set
//        tpts.sched

        return tpts;
    }



}
