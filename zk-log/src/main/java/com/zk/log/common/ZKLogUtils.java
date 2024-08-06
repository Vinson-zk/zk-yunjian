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

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.web.utils.ZKReactiveUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.log.entity.ZKLogAccess;
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

    /**
     * 保存日志
     */
    public static void saveAccessLog(HttpServletRequest hReq, HttpServletResponse hRes, Exception ex) {
        try {
            // 保存日志信息
            new ZKLogSaveThread(hReq, hRes, ex).start();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static void saveAccessLog(ServerWebExchange exchange, Exception ex) {
        try {
            // 保存日志信息
            new ZKLogSaveThread(exchange, ex).start();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 保存日志线程
     */
    public static class ZKLogSaveThread extends Thread {

        /**
         * 日志对象
         */
        protected Logger logger = LogManager.getLogger(getClass());

        private ZKLogAccess logAccess;

        // private Object handler;

        private Exception ex;

        public ZKLogSaveThread(HttpServletRequest hReq, HttpServletResponse hRes, Exception ex) {
            super(ZKLogSaveThread.class.getSimpleName());
            this.logAccess = createLogAccess();
            createLogAccess(this.logAccess, hReq, hRes);
            // this.handler = handler;
            this.ex = ex;
        }

        public ZKLogSaveThread(ServerWebExchange exchange, Exception ex) {
            super(ZKLogSaveThread.class.getSimpleName());
            this.logAccess = createLogAccess();
            createLogAccess(this.logAccess, exchange);
            // this.handler = handler;
            this.ex = ex;
        }

        private ZKLogAccess createLogAccess() {
            ZKLogAccess logAccess = new ZKLogAccess();
            logAccess.setTitle(null);
            logAccess.setDateTime(ZKDateUtils.getToday());
            try {
                logAccess.setGroupCode(secPrincipalService == null ? null : secPrincipalService.getGroupCode());
                logAccess.setCompanyId(secPrincipalService == null ? null : secPrincipalService.getCompanyId());
                logAccess.setCompanyCode(secPrincipalService == null ? null : secPrincipalService.getCompanyCode());
            }
            catch(ZKUnknownException e) {
                logger.error("[>_<:20240617-0035-001] 记录日志时，获取当前用户信息失败，但不影响运行！");
                e.printStackTrace();
            }
            return logAccess;
        }

        private ZKLogAccess createLogAccess(ZKLogAccess logAccess, HttpServletRequest hReq, HttpServletResponse hRes) {
            logAccess.setRemoteAddr(ZKServletUtils.getRemoteAddr(hReq));
            logAccess.setUserAgent(hReq.getHeader("user-agent"));
            logAccess.setRequestUri(ZKServletUtils.getPathWithinApplication(hReq));
            logAccess.setParamsMap(hReq.getParameterMap());
            logAccess.setMethod(hReq.getMethod());
            logAccess.setResStatus(String.valueOf(hRes.getStatus()));
            return logAccess;
        }

        private ZKLogAccess createLogAccess(ZKLogAccess logAccess, ServerWebExchange exchange) {
            logAccess.setRemoteAddr(ZKReactiveUtils.getRemoteAddr(exchange.getRequest()));
            logAccess.setUserAgent(exchange.getRequest().getHeaders().getFirst("user-agent"));
            logAccess.setRequestUri(exchange.getRequest().getURI().getPath());
            logAccess.setParams(exchange.getFormData().toString());
//          logAccess.setParamsMap(exchange.getFormData().toString());
            logAccess.setMethod(exchange.getRequest().getMethod().name());
            logAccess.setResStatus(String.valueOf(exchange.getResponse().getStatusCode().value()));
            return logAccess;
        }

        @Override
        public void run() {
            try {
                // 判断日志是否需要过虑
                if (s.isFilters(logAccess.getRequestUri())) {
                    return;
                }
//                // 获取日志标题
//                if (ZKStringUtils.isBlank(logAccess.getTitle())) {
//                    String permission = "";
//                    if (handler instanceof HandlerMethod) {
//                        Method m = ((HandlerMethod) handler).getMethod();
//                        RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
//                        permission = (rp != null ? ZKStringUtils.join(rp.value(), ",") : "");
//                    }
//                    logAccess.setTitle(getMenuNamePath(logAccess.getRequestUri(), permission));
//                }
                // 如果有异常，设置异常信息
                logAccess.setException(ZKExceptionsUtils.getStackTraceAsString(ex));
                // 如果无标题并无异常日志，则不保存信息
//                if (ZKStringUtils.isBlank(logAccess.getTitle()) && ZKStringUtils.isBlank(logAccess.getException())) {
//                    return;
//                }
                // 保存日志信息
                s.save(logAccess);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

}
