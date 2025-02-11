/** 
* Copyright (c) 2012-2025 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKLogSaveThread.java 
* @author Vinson 
* @Package com.zk.log.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Feb 9, 2025 3:42:10 PM 
* @version V1.0 
*/
package com.zk.log.common;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.server.ServerWebExchange;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.web.utils.ZKReactiveUtils;
import com.zk.core.web.utils.ZKServletUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.log.entity.ZKLogAccess;
import com.zk.log.service.ZKLogAccessService;
import com.zk.security.service.ZKSecPrincipalService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKLogSaveThread 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLogSaveThread extends Thread {

    /**
     * 日志对象
     */
    protected Logger logger = LogManager.getLogger(getClass());

    private ZKLogAccessService logAccessService;

    private ZKSecPrincipalService secPrincipalService;

    // private Object handler;

    private Exception ex;

    private ZKLogAccess logAccess;

//    private HttpServletRequest hReq;
//
//    private HttpServletResponse hRes;
//
//    private ServerWebExchange exchange;

//    private int setType = 0; // 0-根据 HttpServletRequest 设置日志；1-根据 ServerWebExchange 设置日志；

    public ZKLogSaveThread(ZKLogAccessService logAccessService, ZKSecPrincipalService secPrincipalService,
            HttpServletRequest hReq, HttpServletResponse hRes, Exception ex) {
        super(ZKLogSaveThread.class.getSimpleName());
//        this.setType = 0;
        this.logAccessService = logAccessService;
        this.secPrincipalService = secPrincipalService;
        // this.handler = handler;
        this.ex = ex;
//        this.hReq = hReq;
//        this.hRes = hRes;
        // 创建日志记录会用到 request 所以不能放到子线程中，放在子线程中存在 request 被销毁的风险。
        this.logAccess = this.initLogAccessByServlet(hReq, hRes);
    }

    public ZKLogSaveThread(ZKLogAccessService logAccessService, ZKSecPrincipalService secPrincipalService,
            ServerWebExchange exchange, Exception ex) {
        super(ZKLogSaveThread.class.getSimpleName());
//        this.setType = 1;
        this.logAccessService = logAccessService;
        this.secPrincipalService = secPrincipalService;
        // this.handler = handler;
        this.ex = ex;
//        this.exchange = exchange;
        this.logAccess = this.initLogAccessByServerWebExchange(exchange);
    }

    private ZKLogAccess initLogAccess() {
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

    private ZKLogAccess initLogAccessByServlet(HttpServletRequest hReq, HttpServletResponse hRes) {
        ZKLogAccess logAccess = initLogAccess();
        logAccess.setRemoteAddr(ZKWebUtils.getRemoteAddr(hReq));
        logAccess.setUserAgent(ZKServletUtils.getUserAgent(hReq));
        logAccess.setRequestUri(ZKServletUtils.getPathWithinApplication(hReq));
        logAccess.setParamsMap(hReq.getParameterMap());
        logAccess.setMethod(hReq.getMethod());
        logAccess.setResStatus(String.valueOf(hRes.getStatus()));
        return logAccess;
    }

    private ZKLogAccess initLogAccessByServerWebExchange(ServerWebExchange exchange) {
        ZKLogAccess logAccess = initLogAccess();
        logAccess.setRemoteAddr(ZKWebUtils.getRemoteAddr(exchange.getRequest()));
        logAccess.setUserAgent(ZKReactiveUtils.getUserAgent(exchange.getRequest()));
        logAccess.setRequestUri(exchange.getRequest().getURI().getPath());
        logAccess.setParams(exchange.getFormData().toString());
//      logAccess.setParamsMap(exchange.getFormData().toString());
        logAccess.setMethod(exchange.getRequest().getMethod().name());
        logAccess.setResStatus(String.valueOf(exchange.getResponse().getStatusCode().value()));
        return logAccess;
    }

    @Override
    public void run() {
        try {
            // 判断日志是否需要过虑
            if (this.logAccessService.isFilters(logAccess.getRequestUri())) {
                return;
            }
//            // 获取日志标题
//            if (ZKStringUtils.isBlank(logAccess.getTitle())) {
//                String permission = "";
//                if (handler instanceof HandlerMethod) {
//                    Method m = ((HandlerMethod) handler).getMethod();
//                    RequiresPermissions rp = m.getAnnotation(RequiresPermissions.class);
//                    permission = (rp != null ? ZKStringUtils.join(rp.value(), ",") : "");
//                }
//                logAccess.setTitle(getMenuNamePath(logAccess.getRequestUri(), permission));
//            }
            // 如果有异常，设置异常信息
            logAccess.setException(ZKExceptionsUtils.getStackTraceAsString(ex));
            // 如果无标题并无异常日志，则不保存信息
//            if (ZKStringUtils.isBlank(logAccess.getTitle()) && ZKStringUtils.isBlank(logAccess.getException())) {
//                return;
//            }
            // 保存日志信息
            this.logAccessService.save(logAccess);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
}

