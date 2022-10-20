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
* @Title: ZKSecAbstractFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 6:53:05 AM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.zk.core.utils.ZKStringUtils;
import com.zk.core.web.filter.ZKOncePerFilter;
import com.zk.security.common.ZKSecNameable;

/** 
* @ClassName: ZKSecAbstractFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractFilter extends ZKOncePerFilter implements ZKSecNameable {

    protected Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * The name of this filter, unique within an application.
     */
    private String name;

    @Override
    public void destroy() {
        super.destroy();
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        setFilterConfig(filterConfig);
        try {
            onFilterConfigSet();
        }
        catch(Exception e) {
            if (e instanceof ServletException) {
                throw (ServletException) e;
            }
            else {
                if (logger.isErrorEnabled()) {
                    logger.error("Unable to start Filter: [" + e.getMessage() + "].", e);
                }
                throw new ServletException(e);
            }
        }
    }

    public String getName() {
        if (this.name == null) {
            FilterConfig config = getFilterConfig();
            if (config != null) {
                this.name = config.getFilterName();
            }
        }

        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    protected StringBuilder toStringBuilder() {
        String name = getName();
        if (name == null) {
            return super.toStringBuilder();
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append(name);
            return sb;
        }
    }

    ////////////////////////////////////////

    protected FilterConfig filterConfig;

    public void setFilterConfig(FilterConfig filterConfig) {
        this.filterConfig = filterConfig;
        setServletContext(filterConfig.getServletContext());
    }

    public FilterConfig getFilterConfig() {
        return filterConfig;
    }

    protected String getInitParam(String paramName) {
        FilterConfig config = getFilterConfig();
        if (config != null) {
            return ZKStringUtils.clean(config.getInitParameter(paramName));
        }
        return null;
    }

    /**
     * 初始化 Filter 时点燃此方法
     * 
     * @throws Exception
     */
    protected void onFilterConfigSet() throws Exception {
    }

}
