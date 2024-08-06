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

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.security.service.ZKSecPrincipalService;

/** 
* @ClassName: ZKSecPrincipalUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecPrincipalUtils {

    protected static Logger log = LogManager.getLogger(ZKSecPrincipalUtils.class);


    public static void setZKSecPrincipalService(ZKSecPrincipalService secPrincipalService) {
        ZKSecPrincipalUtils.secPrincipalService = secPrincipalService;
    }

    static ZKSecPrincipalService secPrincipalService = null;

    public static ZKSecPrincipalService getSecPrincipalService() {
        if (secPrincipalService == null) {
            log.error("[>_<:20230202-2007-002] 无用户服务！");
        }
        return secPrincipalService;
    }

}
