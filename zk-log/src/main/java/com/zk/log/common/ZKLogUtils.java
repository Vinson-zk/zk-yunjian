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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKEnvironmentUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.core.web.utils.ZKWebUtils;
import com.zk.log.entity.ZKLogAccess;
import com.zk.log.service.ZKLogAccessService;
import com.zk.security.utils.ZKSecSecurityUtils;

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
    protected static Logger logger = LoggerFactory.getLogger(ZKLogUtils.class);

    private static ZKLogAccessService s = ZKEnvironmentUtils.getApplicationContext().getBean(ZKLogAccessService.class);

    /**
     * 保存日志
     */
    public static void saveAccessLog(HttpServletRequest hReq, HttpServletResponse hRes, Object handler, Exception ex) {
        try {
            // 保存日志信息
            new ZKLogSaveThread(hReq, hRes, handler, ex).start();
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
        protected Logger logger = LoggerFactory.getLogger(getClass());

        private ZKLogAccess logAccess;

        // private Object handler;

        private Exception ex;


        public ZKLogSaveThread(HttpServletRequest hReq, HttpServletResponse hRes,
                Object handler, Exception ex) {
            super(ZKLogSaveThread.class.getSimpleName());
            this.logAccess = createLogAccess(hReq);
            // this.handler = handler;
            this.ex = ex;
        }

        private ZKLogAccess createLogAccess(HttpServletRequest hReq) {
            ZKLogAccess logAccess = new ZKLogAccess();
            logAccess.setTitle(null);
            logAccess.setRemoteAddr(ZKWebUtils.getRemoteAddr(hReq));
            logAccess.setUserAgent(hReq.getHeader("user-agent"));
            logAccess.setRequestUri(ZKWebUtils.getPathWithinApplication(hReq));
            logAccess.setParamsMap(hReq.getParameterMap());
            logAccess.setMethod(hReq.getMethod());
            logAccess.setDateTime(ZKDateUtils.getToday());
            logAccess.setGroupCode(ZKSecSecurityUtils.getGroupCode());
            logAccess.setCompanyId(ZKSecSecurityUtils.getCompanyId());
            logAccess.setCompanyCode(ZKSecSecurityUtils.getCompanyCode());
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
