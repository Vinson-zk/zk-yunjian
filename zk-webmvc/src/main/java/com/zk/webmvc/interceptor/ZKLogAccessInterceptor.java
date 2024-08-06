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
* @Title: ZKLogAccessInterceptor.java 
* @author Vinson 
* @Package com.zk.log.interceptor 
* @Description: TODO(simple description this file what to do. ) 
* @date Jun 14, 2022 10:52:55 AM 
* @version V1.0 
*/
package com.zk.webmvc.interceptor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.zk.log.common.ZKLogUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 日志拦截器，需要记录日志时，注入拦截器
 * 需要创建日志表 t_log_access
 * @ClassName: ZKLogAccessInterceptor
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
@Deprecated // 那俩使用 com.zk.log.filter.ZKLogAccessFilter 访问日志处理拦截器
public class ZKLogAccessInterceptor implements HandlerInterceptor {

    /**
     * 日志对象
     */
    protected Logger logger = LogManager.getLogger(getClass());

    // private static final ThreadLocal<Long> startTimeThreadLocal = new
    // NamedThreadLocal<Long>("ThreadLocal StartTime");

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.HandlerInterceptor#preHandle(javax. servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.lang.Object) Action之前执行拦截
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        return true;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.HandlerInterceptor#postHandle(javax. servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.lang.Object, org.springframework.web.servlet.ModelAndView) 生成视图之前执行拦截
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
            ModelAndView modelAndView) throws Exception {
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.springframework.web.servlet.HandlerInterceptor#afterCompletion(javax. servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse,
     * java.lang.Object, java.lang.Exception) 最后执行拦截，可用于释放资源
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {

        // 保存日志
        ZKLogUtils.saveAccessLog(request, response, ex);
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
