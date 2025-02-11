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
* @Title: ZKLogUtils.java 
* @author Vinson 
* @Package com.zk.log.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 8:42:32 AM 
* @version V1.0 
*/
package com.zk.log.common;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.configuration.ZKCoreThreadPoolProperties;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.log.service.ZKLogAccessService;
import com.zk.security.service.ZKSecPrincipalService;
import com.zk.security.utils.ZKSecPrincipalUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKLogUtils 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLogUtils {

    /**
     * 日志对象
     */
    protected static Logger logger = LogManager.getLogger(ZKLogUtils.class);

    private static ZKLogAccessService s = ZKEnvironmentUtils.getApplicationContext().getBean(ZKLogAccessService.class);

    private static ZKSecPrincipalService secPrincipalService = ZKSecPrincipalUtils.getSecPrincipalService();

    private static ExecutorService logSaveExecutorService = null;

    private static ExecutorService getLogSaveExecutorService() {
        if (logSaveExecutorService == null) {
            ZKCoreThreadPoolProperties logThreadPoolProperties = ZKEnvironmentUtils.getApplicationContext()
                    .getBean("logThreadPoolProperties", ZKCoreThreadPoolProperties.class);
            /*
             * int corePoolSize, int maximumPoolSize, long keepAliveTime,
             * 
             * new ThreadPoolExecutor(10, 10, 60L, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(10))
             */
            logSaveExecutorService = new ThreadPoolExecutor(logThreadPoolProperties.getCorePoolSize(),
                    logThreadPoolProperties.getMaximumPoolSize(), logThreadPoolProperties.getKeepAliveTime(),
                    logThreadPoolProperties.getUnit(),
                    new ArrayBlockingQueue<Runnable>(logThreadPoolProperties.getWorkQueueCount()));
        }
        return logSaveExecutorService;
    }

    /**
     * 保存日志
     */
    public static void saveAccessLog(HttpServletRequest hReq, HttpServletResponse hRes, Exception ex) {
        try {
            // 保存日志信息
            getLogSaveExecutorService().execute(new ZKLogSaveThread(s, secPrincipalService, hReq, hRes, ex));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAccessLog(ServerWebExchange exchange, Exception ex) {
        try {
            // 保存日志信息
            getLogSaveExecutorService().execute(new ZKLogSaveThread(s, secPrincipalService, exchange, ex));
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

}




