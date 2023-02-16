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
 * @Title: ZKCoreEnvChildConfiguration.java 
 * @author Vinson 
 * @Package com.zk.core.helper.configuration 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 6, 2019 1:48:34 PM 
 * @version V1.0   
*/
package com.zk.core.helper.configuration.env;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.Environment;

/**
 * @ClassName: ZKCoreEnvChildConfiguration
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
@ConfigurationPropertiesScan
@PropertySources(value = { @PropertySource(value = {
        "classpath:env/env.test.child.properties" }, ignoreResourceNotFound = true, encoding = "UTF-8") })
public class ZKCoreEnvChildConfiguration {

    @Autowired
    Environment env;

    @Autowired
    ApplicationContext applicationContext;

    /**
     * 等配置文件 properties 加载完成，再执行这个方法；
     *
     * @Title: runGetEnvironment
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Mar 16, 2019 8:04:32 AM
     * @return void
     */
    @Autowired
//    @ConditionalOnClass(value = { StringValueResolver.class })
//    @ConditionalOnEnabledResourceChain
//    @ConditionalOnBean(value = { ApplicationContextAware.class })
    public void runGetEnvironment() {

//        PropertiesEnvUtils.setEnvironment(env);

        System.out.println("[|------------------------------------------------------------------] ");
        System.out.println("[|--- MyEnvChildConfiguration.runGetEnvironment] ");
        System.out.println("[|--- env.test.source] " + env.getProperty("env.test.source"));
        System.out.println("[|--- env.test.parent] " + env.getProperty("env.test.parent"));
        System.out.println("[|--- env.test.child] " + env.getProperty("env.test.child"));
        System.out.println("[|--- env.test.child.reference] " + env.getProperty("env.test.child.reference"));
        System.out.println("[|--- env.test.p] " + applicationContext.getEnvironment().getProperty("env.test.p"));
        System.out.println("[|------------------------------------------------------------------] ");

    }

//    @Bean
//    public MyEnvMain getTestBean() {
//        System.out.println("|--- MyEnvChildConfiguration.getTestBean ... " + env.getProperty("env.test.parent"));
//        System.out.println("|--- MyEnvChildConfiguration.getTestBean ... " + env.getProperty("env.test.child.parent"));
//        System.out.println("|--- MyEnvChildConfiguration.getTestBean ... " + env.getProperty("env.test.p"));
//        return new MyEnvMain();
//    }

//    @Bean
//    public ApplicationContextAware myMyEnvApplicationAware() {
//        return new MyEnvApplicationAware();
//    }

}
