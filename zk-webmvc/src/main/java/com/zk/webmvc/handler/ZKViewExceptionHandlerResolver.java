/**   
 * Copyright (c) 2004-2014 Vinson Technologies, Inc.
 * address: 
 * All rights reserved. 
 * 
 * This software is the confidential and proprietary information of 
 * Vinson Technologies, Inc. ("Confidential Information").  You shall not 
 * disclose such Confidential Information and shall use it only in 
 * accordance with the terms of the license agreement you entered into 
 * with Vinson. 
 *
 * @Title: ZKViewExceptionHandlerResolver.java 
 * @author Vinson 
 * @Package com.zk.webmvc.resolver 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 3:14:31 PM 
 * @version V1.0   
*/
package com.zk.webmvc.handler;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.Ordered;
import org.springframework.lang.Nullable;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/** 
* @ClassName: ZKViewExceptionHandlerResolver 
* @Description: TODO(simple description this class what to do.) 
* @author Vinson 
* @version 1.0 
*/
@SuppressWarnings("deprecation")
public class ZKViewExceptionHandlerResolver extends ZKExceptionHandlerResolver {

    /** Logger available to subclasses. */
    protected final Logger log = LogManager.getLogger(getClass());

    private int order = Ordered.HIGHEST_PRECEDENCE + 55;

    @Override
    public int getOrder() {
        // TODO Auto-generated method stub
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public ModelAndView resolveException(HttpServletRequest hReq, HttpServletResponse hRes, Object handler,
            Exception ex) {

//        /* 使用response返回 */
//        hRes.setStatus(HttpStatus.OK.value()); // 设置状态码
//        hRes.setContentType(MediaType.APPLICATION_JSON_VALUE); // 设置ContentType
//        hRes.setCharacterEncoding("UTF-8"); // 避免乱码
//        hRes.setHeader("Cache-Control", "no-cache, must-revalidate");

        // Expose ModelAndView for chosen error view.
        String viewName = determineViewName(ex);
        if (viewName != null) {
            // Apply HTTP status code for error views, if specified.
            // Only apply it if we're processing a top-level request.
            Integer statusCode = determineStatusCode(hReq, viewName);
            if (statusCode != null) {
                applyStatusCodeIfPossible(hReq, hRes, statusCode);
            }
            return getModelAndView(viewName, ex);
        }
        else {
            return super.resolveException(hReq, hRes, handler, ex);
        }
    }

    /* ******************************************************************** */

    /** The default name of the exception attribute: "exception". */
    public static final String DEFAULT_EXCEPTION_ATTRIBUTE = "exception";

    // 默认错误页，异常类对应的错误码页面名称不存在时，返回这个默认的名称；为null，异常将异常转为 json 字符串返回；
//    @Nullable；
    private String defaultErrorView;

    // 默认状态码，错误码页面名称对应的错误码不存在里，返回这个默认错误码，默认错误码不存在里，返回系统处理结果的错误码；
    @Nullable
    private Integer defaultStatusCode;

    // 错误码页面名称对应的错误码；
    private Map<String, Integer> statusCodes = new HashMap<>();

    // 异常类对应的错误码页面名称
    @Nullable
    private Properties exceptionMappings;

    // 不处理的异常类
    @Nullable
    private Class<?>[] excludedExceptions;

    @Nullable
    private String exceptionAttribute = DEFAULT_EXCEPTION_ATTRIBUTE;

    public void setDefaultErrorView(String defaultErrorView) {
        this.defaultErrorView = defaultErrorView;
    }

    public void setExceptionMappings(Properties mappings) {
        this.exceptionMappings = mappings;
    }

    public void setExcludedExceptions(Class<?>... excludedExceptions) {
        this.excludedExceptions = excludedExceptions;
    }

    public void setStatusCodes(Properties statusCodes) {
        for (Enumeration<?> enumeration = statusCodes.propertyNames(); enumeration.hasMoreElements();) {
            String viewName = (String) enumeration.nextElement();
            Integer statusCode = Integer.valueOf(statusCodes.getProperty(viewName));
            this.statusCodes.put(viewName, statusCode);
        }
    }

    public void addStatusCode(String viewName, int statusCode) {
        this.statusCodes.put(viewName, statusCode);
    }

    public Map<String, Integer> getStatusCodesAsMap() {
        return Collections.unmodifiableMap(this.statusCodes);
    }

    public void setDefaultStatusCode(int defaultStatusCode) {
        this.defaultStatusCode = defaultStatusCode;
    }

    public void setExceptionAttribute(@Nullable String exceptionAttribute) {
        this.exceptionAttribute = exceptionAttribute;
    }

    // 确定跳转到哪个异常页面，未找到时，返回 null;
    protected String determineViewName(Exception ex) {
        String viewName = null;
        if (this.excludedExceptions != null) {
            for (Class<?> excludedEx : this.excludedExceptions) {
                if (excludedEx.equals(ex.getClass())) {
                    return null;
                }
            }
        }
        // Check for specific exception mappings.
        if (this.exceptionMappings != null) {
            viewName = findMatchingViewName(this.exceptionMappings, ex);
        }
        // Return default error view else, if defined.
        if (viewName == null && this.defaultErrorView != null) {
            if (log.isDebugEnabled()) {
                log.debug("Resolving to default view '" + this.defaultErrorView + "'");
            }
            viewName = this.defaultErrorView;
        }
        return viewName;
    }

    // 确定跳转是返回哪个错误码，未找到时，返回 null;
    protected Integer determineStatusCode() {
        return null;
    }

    @Nullable
    protected Integer determineStatusCode(HttpServletRequest request, String viewName) {
        if (this.statusCodes.containsKey(viewName)) {
            return this.statusCodes.get(viewName);
        }
        return this.defaultStatusCode;
    }

    protected void applyStatusCodeIfPossible(HttpServletRequest request, HttpServletResponse response, int statusCode) {
        if (!WebUtils.isIncludeRequest(request)) {
            if (log.isDebugEnabled()) {
                log.debug("Applying HTTP status " + statusCode);
            }
            response.setStatus(statusCode);
            request.setAttribute(WebUtils.ERROR_STATUS_CODE_ATTRIBUTE, statusCode);
        }
    }

    @Nullable
    protected String findMatchingViewName(Properties exceptionMappings, Exception ex) {
        String viewName = null;
        String dominantMapping = null;
        int deepest = Integer.MAX_VALUE;
        for (Enumeration<?> names = exceptionMappings.propertyNames(); names.hasMoreElements();) {
            String exceptionMapping = (String) names.nextElement();
            int depth = getDepth(exceptionMapping, ex);
            if (depth >= 0 && (depth < deepest || (depth == deepest && dominantMapping != null
                    && exceptionMapping.length() > dominantMapping.length()))) {
                deepest = depth;
                dominantMapping = exceptionMapping;
                viewName = exceptionMappings.getProperty(exceptionMapping);
            }
        }
        if (viewName != null && log.isDebugEnabled()) {
            log.debug("Resolving to view '" + viewName + "' based on mapping [" + dominantMapping + "]");
        }
        return viewName;
    }

    /**
     * Return the depth to the superclass matching.
     * <p>
     * 0 means ex matches exactly. Returns -1 if there's no match. Otherwise,
     * returns depth. Lowest depth wins.
     */
    protected int getDepth(String exceptionMapping, Exception ex) {
        return getDepth(exceptionMapping, ex.getClass(), 0);
    }

    private int getDepth(String exceptionMapping, Class<?> exceptionClass, int depth) {
        if (exceptionClass.getName().contains(exceptionMapping)) {
            // Found it!
            return depth;
        }
        // If we've gone as far as we can go and haven't found it...
        if (exceptionClass == Throwable.class) {
            return -1;
        }
        return getDepth(exceptionMapping, exceptionClass.getSuperclass(), depth + 1);
    }

    protected ModelAndView getModelAndView(String viewName, Exception ex) {
        ModelAndView mv = new ModelAndView(viewName);
        if (this.exceptionAttribute != null) {
            mv.addObject(this.exceptionAttribute, ex);
        }
        return mv;
    }

}
