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
* @Title: ZKSecPathMatchingFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 6:59:43 AM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import javax.servlet.ServletRequest;

import com.zk.core.commons.ZKUrlPathMatcher;
import com.zk.core.web.utils.ZKWebUtils;

/**
 * 权限拦截器中设置的按路径过虑拦截
 * 
 * @ClassName: ZKSecPathMatchingFilter
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public abstract class ZKSecPathMatchingFilter extends ZKSecAdviceFilter {

    protected ZKUrlPathMatcher urlPathMatcher = new ZKUrlPathMatcher();

    /**
     * 取请求路径
     * 
     * @param request
     * @return
     */
    protected String getPathWithinApplication(ServletRequest request) {
        return ZKWebUtils.getPathWithinApplication(ZKWebUtils.toHttp(request));
    }

    /**
     * 对比请求路径
     * 
     * @param path
     * @param request
     * @return
     */
    protected boolean pathsMatch(String path, ServletRequest request) {
        String requestURI = getPathWithinApplication(request);
        logger.trace("Attempting to match pattern '{}' with current requestURI '{}'...", path, requestURI);
        return pathsMatch(path, requestURI);
    }

    /**
     * 对比请求路径
     * 
     * @param pattern
     * @param path
     * @return
     */
    protected boolean pathsMatch(String pattern, String path) {
        return urlPathMatcher.matches(pattern, path);
    }

}
