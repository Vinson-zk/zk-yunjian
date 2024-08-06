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
* @Title: ZKSecDefaultConfiguration.java 
* @author Vinson 
* @Package com.zk.framework.security.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 14, 2023 2:26:35 PM 
* @version V1.0 
*/
package com.zk.framework.security.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.framework.security.realm.ZKDistributedRealm;
import com.zk.framework.security.service.ZKSecAuthService;
import com.zk.framework.security.service.ZKSecDistributedAuthService;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.realm.ZKSecRealm;
import com.zk.security.rememberMe.ZKSecRememberMeManager;
import com.zk.security.support.spring.ZKSecStaticMethodMatcherPointcutAdvisor;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.ticket.support.redis.ZKSecRedisTicketManager;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.rememberMe.ZKSecCookieRememberMemanager;

/** 
* @ClassName: ZKSecDefaultConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultConfiguration {

    @Value("${spring.application.name}")
    String applicationName;

    @ConditionalOnMissingBean(value = { ZKSecStaticMethodMatcherPointcutAdvisor.class })
    @Bean
    ZKSecStaticMethodMatcherPointcutAdvisor zkSecStaticMethodMatcherPointcutAdvisor() {
        System.out.println(ZKEnableSecurity.printLog + " zkSecStaticMethodMatcherPointcutAdvisor: "
                + ZKSecStaticMethodMatcherPointcutAdvisor.class.getSimpleName());
        return new ZKSecStaticMethodMatcherPointcutAdvisor();
    }

    @ConditionalOnMissingBean(value = { ZKSecAuthService.class })
    @Bean
    ZKSecDistributedAuthService secAuthService() {
        return new ZKSecDistributedAuthService();
    }

    /**
     * 分布式认证域
     *
     * @Title: secRealm
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Feb 14, 2023 1:55:16 PM
     * @param secRedisTicketManager
     * @return
     * @return ZKDistributedRealm
     */
    @ConditionalOnMissingBean(value = { ZKSecRealm.class })
    @Bean
    ZKDistributedRealm zkSecRealm(ZKSecTicketManager zkSecTicketManager, ZKSecAuthService<String> secAuthService) {
        System.out.println(ZKEnableSecurity.printLog + " zkSecRealm: " + ZKDistributedRealm.class.getSimpleName());
        System.out.println(ZKEnableSecurity.printLog + " zkSecRealm.applicationName: " + applicationName);
        ZKDistributedRealm realm = new ZKDistributedRealm(applicationName, secAuthService, zkSecTicketManager);
        return realm;
    }

//    // mongo 令牌管理
//    @ConditionalOnMissingBean(value = { ZKSecTicketManager.class })
//    @Bean
//    public ZKSecMongoTicketManager zkSecTicketManager(MongoTemplate mongoTemplate) {
//        return new ZKSecMongoTicketManager(mongoTemplate);
//    }

    // redis 令牌管理
    @ConditionalOnMissingBean(value = { ZKSecTicketManager.class })
    @Primary
    @Bean
    ZKSecRedisTicketManager zkSecTicketManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        System.out.println(
                ZKEnableSecurity.printLog + " zkSecTicketManager: " + ZKSecRedisTicketManager.class.getSimpleName());
        return new ZKSecRedisTicketManager(jedisOperatorStringKey);
    }

    // cookie 记住我管理
    @ConditionalOnMissingBean(value = { ZKSecRememberMeManager.class })
    @Bean
    ZKSecCookieRememberMemanager secCookieRememberMemanager() {
        System.out.println(ZKEnableSecurity.printLog + " secCookieRememberMemanager: "
                + ZKSecCookieRememberMemanager.class.getSimpleName());
        ZKSecCookieRememberMemanager secCookieRememberMemanager = new ZKSecCookieRememberMemanager();
        return secCookieRememberMemanager;
    }

    // 权限管理
    @Bean
    ZKSecWebSecurityManager zkSecWebSecurityManager(ZKSecRememberMeManager rememberMeManager,
            ZKSecTicketManager zkSecTicketManager, ZKSecRealm zkSecRealm) {
        System.out.println(ZKEnableSecurity.printLog + " zkSecWebSecurityManager: "
                + ZKSecWebSecurityManager.class.getSimpleName());
        System.out.println(ZKEnableSecurity.printLog + " zkSecWebSecurityManager.rememberMeManager: "
                + rememberMeManager.getClass().getSimpleName());
        System.out.println(ZKEnableSecurity.printLog + " zkSecWebSecurityManager.zkSecTicketManager: "
                + zkSecTicketManager.getClass().getSimpleName());
        System.out.println(ZKEnableSecurity.printLog + " zkSecWebSecurityManager.zkSecRealm: "
                + zkSecRealm.getClass().getSimpleName());
        ZKSecWebSecurityManager sm = new ZKSecWebSecurityManager();
        sm.setTicketManager(zkSecTicketManager);
        sm.setRememberMeManager(rememberMeManager);
        sm.setRealm(zkSecRealm);
        return sm;
    }

}
