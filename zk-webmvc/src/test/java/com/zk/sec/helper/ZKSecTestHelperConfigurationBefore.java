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
* @Title: ZKSecTestHelperConfigurationBefore.java 
* @author Vinson 
* @Package com.zk.security.helper 
* @Description: TODO(simple description this file what to do. ) 
* @date Aug 4, 2021 12:31:55 AM 
* @version V1.0 
*/
package com.zk.sec.helper;

import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.context.annotation.Bean;

import com.zk.core.configuration.ZKEnableCore;
import com.zk.security.configuration.ZKEnableSecurity;
import com.zk.security.helper.ZKSecTestHelperMongoConfiguration;
import com.zk.security.helper.ZKSecTestHelperRedisConfiguration;
import com.zk.security.helper.realm.ZKSecTestRealm;
import com.zk.security.ticket.ZKSecTicketManager;
import com.zk.security.web.mgt.ZKSecWebSecurityManager;
import com.zk.security.web.support.spring.ZKSecStaticMethodMatcherPointcutAdvisor;

/** 
* @ClassName: ZKSecTestHelperConfigurationBefore 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@AutoConfigureAfter(value = { //
        ZKSecTestHelperMongoConfiguration.class, //
        ZKSecTestHelperRedisConfiguration.class, // 
})
@ZKEnableCore
@ZKEnableSecurity
public class ZKSecTestHelperConfigurationBefore {

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
        return sm;
    }

}
