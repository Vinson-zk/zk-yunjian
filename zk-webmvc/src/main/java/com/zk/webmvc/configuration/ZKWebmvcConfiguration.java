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
* @Title: ZKWebmvcConfiguration.java 
* @author Vinson 
* @Package com.zk.web.configuration 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 29, 2023 4:47:26 PM 
* @version V1.0 
*/
package com.zk.webmvc.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

import com.zk.core.configuration.ZKCoreConfiguration;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKLocaleUtils;
import com.zk.core.web.utils.ZKWebUtils;

/**
 * @ClassName: ZKWebmvcConfiguration
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKWebmvcConfiguration extends ZKCoreConfiguration {

    @Autowired
    public void beforeWebmvc(RequestMappingHandlerAdapter requestMappingHandlerAdapter) {
//        super.before();
        System.out.println("[^_^:20230129-1648-001] === [" + ZKWebmvcConfiguration.class.getSimpleName() + "] " + this);
        // # 默认语言；
        ZKWebUtils.setLocale(ZKLocaleUtils.distributeLocale(ZKEnvironmentUtils.getString("zk.default.locale", "zh_CN")));
        // 设置下 RequestMappingHandlerAdapter 的 ignoreDefaultModelOnRedirect=true,
        // 这样可以提高效率，避免不必要的检索。
        requestMappingHandlerAdapter.setIgnoreDefaultModelOnRedirect(true);
        System.out.println("[^_^:20230129-1648-002] --- [" + ZKWebmvcConfiguration.class.getSimpleName() + "] " + this);
    }

}
