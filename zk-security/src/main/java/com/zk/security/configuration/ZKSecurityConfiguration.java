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
* @Title: ZKSecurityConfiguration.java 
* @author Vinson 
* @Package com.zk.security.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2023 1:46:29 PM 
* @version V1.0 
*/
package com.zk.security.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

import com.zk.security.service.ZKSecDefaultPrincipalService;
import com.zk.security.service.ZKSecPrincipalService;

/** 
* @ClassName: ZKSecurityConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecurityConfiguration {

    @ConditionalOnMissingBean(value = { ZKSecPrincipalService.class })
    @Bean(value = { "zkSecPrincipalService", "secPrincipalService" })
    ZKSecPrincipalService zkSecDefaultPrincipalService() {
        System.out.println(ZKEnableSecurity.printLog + " create zkSecPrincipalService: ZKSecDefaultPrincipalService; --- [" 
                + this.getClass().getSimpleName() + "] " + this.hashCode());
        return new ZKSecDefaultPrincipalService();
    }

}
