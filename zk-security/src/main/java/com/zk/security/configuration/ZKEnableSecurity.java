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
* @Title: ZKEnableSecurity.java 
* @author Vinson 
* @Package com.zk.security.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 4:32:10 PM 
* @version V1.0 
*/
package com.zk.security.configuration;
/** 
* @ClassName: ZKEnableSecurity 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.context.support.AbstractResourceBasedMessageSource;

import com.zk.security.configuration.ZKEnableSecurity.ZKSecurityInit;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(value = { ZKSecurityConfiguration.class, ZKSecurityInit.class })
public @interface ZKEnableSecurity {

    public static final String printLog = "[^_^:20230209-2148-006] ----- zk-security config: ";

    public class ZKSecurityInit {
        
        public ZKSecurityInit(AbstractResourceBasedMessageSource messageSource, ZKSecPrincipalService zkSecPrincipalService) {
            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] =================================");

            ZKSecPrincipalUtils.setZKSecPrincipalService(zkSecPrincipalService);
            
            System.out.println(printLog + "messageSource.addBasenames... " + this.getClass().getSimpleName() + " " + this);
            messageSource.addBasenames("msg/zkMsg_sec");
            
            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] ---------------------------------");
        }
    }

}
