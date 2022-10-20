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
* @Title: ZKCoreConfiguration.java 
* @author Vinson 
* @Package com.zk.core.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 10, 2021 1:59:07 PM 
* @version V1.0 
*/
package com.zk.core.configuration;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationPropertiesBindingPostProcessor;
import org.springframework.context.ApplicationContext;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.utils.ZKWebUtils;

/** 
 * 注间引入此基础配置类，需要加载配置文件 zk.core.properties
* @ClassName: ZKCoreConfiguration 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKCoreConfiguration {
    
    @Autowired
    ApplicationContext applicationContext;

    @Autowired
    ConfigurationPropertiesBindingPostProcessor configurationPropertiesBinder;

    @PostConstruct
    public void postConstruct() {
        // 方法在 @Autowired before 方法后执行
//        System.out.println("[^_^:20210210-2154-001] ===== ZKCoreConfiguration class postConstruct ");
//        System.out.println("[^_^:20210210-2154-001] ----- ZKCoreConfiguration class postConstruct ");
    }

    @Autowired
    public void before(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
        System.out.println("[^_^:20210210-1808-001] -------- configuration before begin... ... " + this.getClass());

        ZKEnvironmentUtils.initContext(applicationContext);
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("en_US"));
//        ZKLocaleUtils.setLocale(ZKLocaleUtils.valueOf("zh_CN"));
//        // # 默认语言；注意这里不影响到 localeResolver 的默认语言
        ZKWebUtils.setLocale(
                ZKLocaleUtils.distributeLocale(ZKEnvironmentUtils.getString("zk.core.default.locale", "zh_CN")));

        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20210210-1808-002] -------- configuration before end______ " + this.getClass());
    }

}
