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
* @Title: ZKMailSecConfiguration.java 
* @author Vinson 
* @Package com.zk.mail.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date May 25, 2022 8:46:29 AM 
* @version V1.0 
*/
package com.zk.mail.configuration;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.web.servlet.ServletWebServerFactoryAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration.EnableWebMvcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Primary;
import org.springframework.core.Ordered;

import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.core.web.filter.ZKDelegatingFilterProxyRegistrationBean;
import com.zk.framework.security.realm.ZKDistributedRealm;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.ticket.support.redis.ZKSecRedisTicketManager;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.rememberMe.ZKSecCookieRememberMemanager;
import com.zk.security.web.support.spring.ZKSecStaticMethodMatcherPointcutAdvisor;


/** 
* @ClassName: ZKMailSecConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Configuration
@AutoConfigureBefore(value = { ZKMailAfterConfiguration.class })
@AutoConfigureAfter(value = {
//        ZKMongoAutoConfiguration.class,
        ZKMailRedisConfiguration.class,
        EnableWebMvcConfiguration.class, 
        ServletWebServerFactoryAutoConfiguration.class})
@ImportResource(locations = { "classpath:xmlConfig/spring_ctx_mail_sec.xml" })
public class ZKMailSecConfiguration {

    @Bean
    public ZKSecStaticMethodMatcherPointcutAdvisor zkSecStaticMethodMatcherPointcutAdvisor() {
        return new ZKSecStaticMethodMatcherPointcutAdvisor();
    }

    @Bean
    public ZKDistributedRealm secRealm(ZKSecRedisTicketManager secRedisTicketManager) {
        ZKDistributedRealm realm = new ZKDistributedRealm();
//        realm.setSecUserService(secUserService);
        realm.setTicketManager(secRedisTicketManager);
        return realm;
    }

//    @Bean
//    public ZKSecMongoTicketManager zkSecMongoTicketManager(MongoTemplate mongoTemplate) {
//        return new ZKSecMongoTicketManager(mongoTemplate);
//    }

    @Primary
    @Bean
    public ZKSecRedisTicketManager redisTicketManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        return new ZKSecRedisTicketManager(jedisOperatorStringKey);
    }

    @Bean
    public ZKSecCookieRememberMemanager secCookieRememberMemanager() {
        ZKSecCookieRememberMemanager secCookieRememberMemanager = new ZKSecCookieRememberMemanager();
        return secCookieRememberMemanager;
    }

    @Bean
    public ZKSecWebSecurityManager zkSecWebSecurityManager(ZKSecCookieRememberMemanager rememberMeManager,
            ZKSecTicketManager zkSecTicketManager, ZKDistributedRealm secRealm) {
        ZKSecWebSecurityManager sm = new ZKSecWebSecurityManager();
        sm.setTicketManager(zkSecTicketManager);
        sm.setRememberMeManager(rememberMeManager);
        sm.setRealm(secRealm);
        return sm;
    }

    /**
     * 拦截器 - ZKDelegatingFilterProxyRegistrationBean
     *
     * @Title: secFilterProxyRegistrationBean
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Apr 21, 2022 9:48:22 AM
     * @return ZKDelegatingFilterProxyRegistrationBean
     */
    @Bean
    @ConditionalOnBean(value = { ZKSecWebSecurityManager.class, ZKSecStaticMethodMatcherPointcutAdvisor.class })
    public ZKDelegatingFilterProxyRegistrationBean secFilterProxyRegistrationBean() {
        String filterName = "zkSecFilter";
        ZKDelegatingFilterProxyRegistrationBean zkDfprb = new ZKDelegatingFilterProxyRegistrationBean(filterName);
        zkDfprb.setName(filterName);
        zkDfprb.addUrlPatterns("/*");
        zkDfprb.setOrder(Ordered.LOWEST_PRECEDENCE);
        return zkDfprb;
    }

}
