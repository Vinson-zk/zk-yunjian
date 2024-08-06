/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKSecFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 15, 2024 5:12:31 PM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import java.io.IOException;

import com.zk.security.web.filter.common.ZKSecFilterChain;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/**
 * @ClassName: ZKSecFilter
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSecFilter {

    default public void init() {

    }

    public void doFilter(ZKSecRequestWrapper zkRequest, ZKSecResponseWrapper zkResponse, ZKSecFilterChain zkChain)
            throws IOException;

    /**
     * <p>
     * Called by the web container to indicate to a filter that it is being taken out of service.
     * </p>
     *
     * <p>
     * This method is only called once all threads within the filter's doFilter method have exited or after a timeout period has passed. After the web container
     * calls this method, it will not call the doFilter method again on this instance of the filter.
     * </p>
     *
     * <p>
     * This method gives the filter an opportunity to clean up any resources that are being held (for example, memory, file handles, threads) and make sure that
     * any persistent state is synchronized with the filter's current state in memory.
     * </p>
     * 
     * @implSpec The default implementation takes no action.
     */
    default public void destroy() {
    }

}
