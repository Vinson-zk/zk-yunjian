/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKFluxSecTestHelperConfigurationBefore.java 
* @author Vinson 
* @Package com.zk.flux.sec.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 16, 2024 1:13:33 AM 
* @version V1.0 
*/
package com.zk.flux.sec.helper;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

import com.zk.core.configuration.ZKEnableCoreReactive;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.helper.ZKSecTestHelperMongoConfiguration;
import com.zk.security.helper.ZKSecTestHelperRedisConfiguration;
import com.zk.security.helper.realm.ZKSecTestRealm;
import com.zk.security.support.spring.ZKSecStaticMethodMatcherPointcutAdvisor;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;

/** 
* @ClassName: ZKFluxSecTestHelperConfigurationBefore 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureAfter(value = { //
        ZKSecTestHelperMongoConfiguration.class, //
        ZKSecTestHelperRedisConfiguration.class, //
})
@ZKEnableCoreReactive
@ZKEnableSecurity
public class ZKFluxSecTestHelperConfigurationBefore {

    /********************************************************************************/
    /*** zk security 配置 */
    /********************************************************************************/

    @Bean
    public ZKSecStaticMethodMatcherPointcutAdvisor zkSecStaticMethodMatcherPointcutAdvisor() {
        return new ZKSecStaticMethodMatcherPointcutAdvisor();
    }

    @Bean
    public ZKSecTestRealm zkSecTestRealm(ZKSecTicketManager zkSecTicketManager) {
        ZKSecTestRealm realm = new ZKSecTestRealm();
        realm.setTicketManager(zkSecTicketManager);
        return realm;
    }

    @Bean
    public ZKSecWebSecurityManager zkSecWebSecurityManager(ZKSecTicketManager zkSecTicketManager,
            ZKSecTestRealm realm) {
        ZKSecWebSecurityManager sm = new ZKSecWebSecurityManager();
        sm.setTicketManager(zkSecTicketManager);
        sm.setRealm(realm);
        // 启用 cookie 令牌
        sm.setTicketCookieEnabled(true);
        return sm;
    }

//    @Bean
//    public ResourceBundleMessageSource messageSource() {
//        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
//        messageSource.addBasenames("msg/zkMsg_sec");
//        messageSource.addBasenames("msg/zkMsg_core");
//        messageSource.setUseCodeAsDefaultMessage(true);
//        messageSource.setCacheSeconds(3600);
//        messageSource.setDefaultEncoding("UTF-8");
//        return messageSource;
//    }

}
