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
* @Title: ZKSecDefaultFilterChainResolver.java 
* @author Vinson 
* @Package com.zk.security.web.filter.common
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 8:17:37 PM 
* @version V1.0 
*/
package com.zk.security.web.filter.common;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.core.commons.ZKPatternMatcher;
import com.zk.core.commons.ZKUrlPathMatcher;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecDefaultFilterChainResolver 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultFilterChainResolver implements ZKSecFilterChainResolver {

    protected Logger logger = LogManager.getLogger(getClass());

    private ZKSecFilterChainManager filterChainManager;

    public ZKSecDefaultFilterChainResolver(ZKSecFilterChainManager filterChainManager) {
        this.patternMatcher = new ZKUrlPathMatcher();
        this.filterChainManager = filterChainManager;
    }

    private ZKPatternMatcher patternMatcher;

    public ZKPatternMatcher getPatternMatcher() {
        return patternMatcher;
    }

    @Override
    public ZKSecFilterChain getChain(ZKSecRequestWrapper request, ZKSecResponseWrapper response,
            ZKSecFilterChain original) {
        if (filterChainManager == null || filterChainManager.hasChains()) {
            return null;
        }
        String requestURI = getPathWithinApplication(request);

        return getChain(original, requestURI);
    }

    public ZKSecFilterChain getChain(ZKSecFilterChain original, String requestURI) {

        for (String pathPattern : filterChainManager.getChainNames()) {
            // If the path does match, then pass on to the subclass
            // implementation for specific checks:
            if (pathMatches(pathPattern, requestURI)) {
                if (logger.isTraceEnabled()) {
                    logger.trace("Matched path pattern [" + pathPattern + "] for requestURI [" + requestURI + "].  "
                            + "Utilizing corresponding filter chain...");
                }
                return filterChainManager.proxy(original, pathPattern);
            }
        }
        return null;
    }

    /**********************/
    protected String getPathWithinApplication(ZKSecRequestWrapper request) {
        return request.getPathWithinApplication();
//        return ZKWebUtils.getPathWithinApplication(ZKWebUtils.toHttp(request));
    }

    protected boolean pathMatches(String pattern, String path) {
        ZKPatternMatcher pathMatcher = getPatternMatcher();
        return pathMatcher.matches(pattern, path);
    }

}
