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
* @Title: ZKEnableWebmvc.java 
* @author Vinson 
* @Package com.zk.webmvc.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 6, 2023 3:55:11 PM 
* @version V1.0 
*/
package com.zk.webmvc.configuration;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.commons.ZKEnvironment;
import com.zk.webmvc.configuration.ZKEnableWebmvc.ZKWebmvcInit;

/** 
* @ClassName: ZKEnableWebmvc 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@ImportAutoConfiguration(value = { ZKWebmvcConfiguration.class, ZKWebmvcInit.class })
public @interface ZKEnableWebmvc {

    public static final String printLog = "[^_^:20230209-2148-007] ----- zk-webmvc config: ";

    public class ZKWebmvcInit {

//        @DependsOn(value = { "localeResolver", "requestMappingHandlerAdapter" })
        @Autowired
        public ZKWebmvcInit(RequestMappingHandlerAdapter requestMappingHandlerAdapter, ZKEnvironment zkEnv) {
            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] =================================");

            // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
            // 这样可以提高效率，避免不必要的检索。
            requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);

            System.out.println(printLog + "init [" + this.getClass().getSimpleName() + ":" + this.hashCode()
                    + "] ---------------------------------");
        }

    }

}
