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

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

import com.zk.core.redis.ZKJedisOperatorStringKey;
import com.zk.framework.security.realm.ZKDistributedRealm;
import com.zk.security.realm.ZKSecRealm;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.ticket.support.redis.ZKSecRedisTicketManager;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.rememberMe.ZKSecCookieRememberMemanager;
import com.zk.security.web.support.spring.ZKSecStaticMethodMatcherPointcutAdvisor;

/** 
* @ClassName: ZKSecDefaultConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultConfiguration {

    @ConditionalOnMissingBean(value = { ZKSecStaticMethodMatcherPointcutAdvisor.class })
    @Bean
    public ZKSecStaticMethodMatcherPointcutAdvisor zkSecStaticMethodMatcherPointcutAdvisor() {
        return new ZKSecStaticMethodMatcherPointcutAdvisor();
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
    public ZKDistributedRealm secRealm(ZKSecRedisTicketManager secRedisTicketManager) {
        ZKDistributedRealm realm = new ZKDistributedRealm();
//        realm.setSecUserService(secUserService);
        realm.setTicketManager(secRedisTicketManager);
        return realm;
    }

//    // mongo 令牌管理
//    @ConditionalOnMissingBean(value = { ZKSecTicketManager.class })
//    @Bean
//    public ZKSecMongoTicketManager zkSecMongoTicketManager(MongoTemplate mongoTemplate) {
//        return new ZKSecMongoTicketManager(mongoTemplate);
//    }

    // redis 令牌管理
    @ConditionalOnMissingBean(value = { ZKSecTicketManager.class })
    @Primary
    @Bean
    public ZKSecRedisTicketManager redisTicketManager(ZKJedisOperatorStringKey jedisOperatorStringKey) {
        return new ZKSecRedisTicketManager(jedisOperatorStringKey);
    }

    // cookie 记住我管理
    @Bean
    public ZKSecCookieRememberMemanager secCookieRememberMemanager() {
        ZKSecCookieRememberMemanager secCookieRememberMemanager = new ZKSecCookieRememberMemanager();
        return secCookieRememberMemanager;
    }

    // 权限管理
    @Bean
    public ZKSecWebSecurityManager zkSecWebSecurityManager(ZKSecCookieRememberMemanager rememberMeManager,
            ZKSecTicketManager zkSecTicketManager, ZKSecRealm secRealm) {
        ZKSecWebSecurityManager sm = new ZKSecWebSecurityManager();
        sm.setTicketManager(zkSecTicketManager);
        sm.setRememberMeManager(rememberMeManager);
        sm.setRealm(secRealm);
        return sm;
    }

//  /**
//   * 权限管理，与鉴权服务
//   *
//   * @Title: getSecAuthService
//   * @Description: TODO(simple description this method what to do.)
//   * @author Vinson
//   * @date May 11, 2022 3:52:24 PM
//   * @return
//   * @return ZKSecAuthService<String>
//   */
//  @Bean
//  @Primary
//  public ZKSecAuthService<String> getSecAuthService() {
//      ZKSecAuthService<String> bean = new ZKSysSecAuthService();
//      return bean;
//  }

}
