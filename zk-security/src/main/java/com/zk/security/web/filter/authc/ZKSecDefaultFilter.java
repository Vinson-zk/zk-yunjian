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

import com.zk.core.utils.ZKClassUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.security.web.filter.ZKSecAbstractFilter;
import com.zk.security.web.filter.ZKSecFilter;

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
    
    
    private final Class<? extends ZKSecFilter> filterClass;

    private ZKSecDefaultFilter(Class<? extends ZKSecFilter> filterClass) {
        this.filterClass = filterClass;
    }
    
    public ZKSecFilter newInstance() {
        try {
            ZKSecAbstractFilter filter = (ZKSecAbstractFilter) ZKClassUtils.newInstance(this.filterClass);
            filter.setName(this.name());
            return filter;
        }
        catch(Exception e) {
            throw ZKExceptionsUtils.unchecked(e);
        }
    }
    
    public Class<? extends ZKSecFilter> getFilterClass() {
        return this.filterClass;
    }

    public static Map<String, ZKSecFilter> createInstanceMaps() {
        Map<String, ZKSecFilter> filters = new LinkedHashMap<String, ZKSecFilter>(values().length);
        for (ZKSecDefaultFilter defaultFilter : values()) {
            ZKSecFilter filter = defaultFilter.newInstance();
            try {
                filter.init();
            }
            catch(Exception e) {
                String msg = "Unable to correctly init default filter instance of type " + filter.getClass().getName();
                throw new IllegalStateException(msg, e);
            }
            filters.put(defaultFilter.name(), filter);
        }
        return filters;
    }

}
