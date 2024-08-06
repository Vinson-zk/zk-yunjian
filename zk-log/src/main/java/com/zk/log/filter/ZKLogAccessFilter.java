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
* @Title: ZKLogAccessFilter.java 
* @author Vinson 
* @Package com.zk.log.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 31, 2023 6:08:44 PM 
* @version V1.0 
*/
package com.zk.log.filter;

import java.io.IOException;

import com.zk.core.web.support.servlet.filter.ZKOncePerFilter;
import com.zk.log.common.ZKLogUtils;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKLogAccessFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKLogAccessFilter extends ZKOncePerFilter {

    /**
     * 
     * @param request
     * @param response
     * @param chain
     * @throws ServletException
     * @throws IOException
     * @see com.zk.core.web.support.servlet.filter.ZKOncePerFilter#doFilterInternal(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    protected void doFilterInternal(ServletRequest request, ServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            throw new ServletException("ZKLogAccessFilter just supports HTTP requests");
        }

        Exception ex = null;
        try {
            chain.doFilter(request, response);
        }
        catch(Exception e) {
            ex = e;
            throw e;
        } finally {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            // 保存日志
            ZKLogUtils.saveAccessLog(httpRequest, httpResponse, ex);
            // 打印JVM信息。
            // if (logger.isDebugEnabled()){
            // long beginTime = startTimeThreadLocal.get();//得到线程绑定的局部变量（开始时间）
            // long endTime = System.currentTimeMillis(); //2、结束时间
            // logger.debug("计时结束：{} 耗时：{} URI: {} 最大内存: {}m 已分配内存: {}m 已分配内存中的剩余空间:
            // {}m 最大可用内存: {}m",
            // new SimpleDateFormat("hh:mm:ss.SSS").format(endTime),
            // DateUtils.formatDateTime(endTime - beginTime),
            // request.getRequestURI(), Runtime.getRuntime().maxMemory()/1024/1024,
            // Runtime.getRuntime().totalMemory()/1024/1024,
            // Runtime.getRuntime().freeMemory()/1024/1024,
            // (Runtime.getRuntime().maxMemory()-Runtime.getRuntime().totalMemory()+Runtime.getRuntime().freeMemory())/1024/1024);
            // //删除线程变量中的数据，防止内存泄漏
            // startTimeThreadLocal.remove();
            // }
        }
    }

}
