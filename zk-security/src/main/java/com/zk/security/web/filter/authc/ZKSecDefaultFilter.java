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
* @Title: ZKSecDefaultFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.authc 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:31:34 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.authc;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;

import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.security.web.filter.ZKSecAbstractFilter;

/** 
* @ClassName: ZKSecDefaultFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public enum ZKSecDefaultFilter {
    
    anon(ZKSecAnonymousFilter.class),
    authcUser(ZKSecAuthcUserFilter.class),
    authcDev(ZKSecAuthcDevFilter.class),
    user(ZKSecUserFilter.class),
    dev(ZKSecDevFilter.class),
    server(ZKSecServerFilter.class),
    serverOrUser(ZKSecServerOrUserFilter.class),
    serverAndUser(ZKSecServerAndUserFilter.class),
    userAndDev(ZKSecUserAndDevFilter.class),
    logout(ZKSecLogoutFilter.class);
    
    
    private final Class<? extends Filter> filterClass;

    private ZKSecDefaultFilter(Class<? extends Filter> filterClass) {
        this.filterClass = filterClass;
    }
    
    public Filter newInstance() {
        try {
            ZKSecAbstractFilter filter = (ZKSecAbstractFilter) ZKClassUtils.newInstance(this.filterClass);
            filter.setName(this.name());
            return filter;
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }
    
    public Class<? extends Filter> getFilterClass() {
        return this.filterClass;
    }

    public static Map<String, Filter> createInstanceMap(FilterConfig config) {
        Map<String, Filter> filters = new LinkedHashMap<String, Filter>(values().length);
        for (ZKSecDefaultFilter defaultFilter : values()) {
            Filter filter = defaultFilter.newInstance();
            if (config != null) {
                try {
                    filter.init(config);
                } catch (ServletException e) {
                    String msg = "Unable to correctly init default filter instance of type " +
                            filter.getClass().getName();
                    throw new IllegalStateException(msg, e);
                }
            }
            filters.put(defaultFilter.name(), filter);
        }
        return filters;
    }

}
