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
 * @Title: ZKHttpServletResponseWrapper.java 
 * @author Vinson 
 * @Package com.zk.webmvc.wrapper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:37:55 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.wrapper;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import com.zk.core.web.wrapper.ZKResponseWrapper;

/**
 * @ClassName: ZKHttpServletResponseWrapper
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKHttpServletResponseWrapper extends HttpServletResponseWrapper implements ZKResponseWrapper {

    public ZKHttpServletResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getWrapperResponse
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param <T>
     * @return
     * @see com.zk.core.web.wrapper.ZKResponseWrapper#getWrapperResponse()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getWrapperResponse() {
        return (T) super.getResponse();
    }

}
