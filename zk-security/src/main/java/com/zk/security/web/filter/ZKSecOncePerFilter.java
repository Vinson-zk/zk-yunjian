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
* @Title: ZKSecOncePerFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 15, 2024 10:54:41 PM 
* @version V1.0 
*/
package com.zk.security.web.filter;

import java.io.IOException;

import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.LogManager;

import com.zk.security.web.filter.common.ZKSecFilterChain;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecOncePerFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecOncePerFilter implements ZKSecFilter {

    protected Logger log = LogManager.getLogger(getClass());

    public static final String ALREADY_FILTERED_SUFFIX = "_ZK.FILTERED";

    protected String getAlreadyFilteredAttributeName() {
        return ALREADY_FILTERED_SUFFIX + getClass().getName();
    }

    @Override
    public final void doFilter(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes, ZKSecFilterChain zkChain)
            throws IOException {
        String alreadyFilteredAttributeName = getAlreadyFilteredAttributeName();
        if (zkReq.getAttribute(alreadyFilteredAttributeName) != null) {
            // 此拦截对这次请求已执行过了
            log.trace("[^_^:20180622-0929-001] Filter '{}' already executed.  Proceeding without invoking this filter.",
                    this.getClass().getName());
            zkChain.doFilter(zkReq, zkRes);
        }
        else { // noinspection deprecation
               // Do invoke this filter...
            log.trace("Filter '{}' not yet executed.  Executing now.", this.getClass().getName());
            zkReq.setAttribute(alreadyFilteredAttributeName, Boolean.TRUE);

            try {
                doFilterInternal(zkReq, zkRes, zkChain);
            } finally {
                // Once the request has finished, we're done and we don't
                // need to mark as 'already filtered' any more.
                zkReq.removeAttribute(alreadyFilteredAttributeName);
            }
        }
    }

    protected abstract void doFilterInternal(ZKSecRequestWrapper zkReq, ZKSecResponseWrapper zkRes,
            ZKSecFilterChain zkChain)
            throws IOException;

}
