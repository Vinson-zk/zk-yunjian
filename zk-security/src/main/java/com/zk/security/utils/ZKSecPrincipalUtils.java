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
* @Title: ZKSecPrincipalUtils.java 
* @author Vinson 
* @Package com.zk.security.utils 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 2, 2023 8:24:37 PM 
* @version V1.0 
*/
package com.zk.security.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.security.service.ZKSecPrincipalService;

/** 
* @ClassName: ZKSecPrincipalUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecPrincipalUtils {

    protected static Logger log = LoggerFactory.getLogger(ZKSecPrincipalUtils.class);

    private static ApplicationContext applicationContext;

    public static void setCtx(ApplicationContext applicationContext) {
        ZKSecPrincipalUtils.applicationContext = applicationContext;
    }

    public static ApplicationContext getCtx() {
        if (ZKSecPrincipalUtils.applicationContext == null) {
            ZKSecPrincipalUtils.applicationContext = ZKEnvironmentUtils.getApplicationContext();
        }
        return ZKSecPrincipalUtils.applicationContext;
    }

    static ZKSecPrincipalService sps = null;

    public static ZKSecPrincipalService getSecPrincipalService() {
        if (getCtx() == null) {
            log.error("[>_<:20230202-2007-001] 无上下文环境，没法取用户服务！");
            return null;
        }
        if (sps == null) {
            sps = getCtx().getBean(ZKSecPrincipalService.class);
        }
        if (sps == null) {
            log.error("[>_<:20230202-2007-002] 无用户服务！");
        }
        return sps;
    }

}
