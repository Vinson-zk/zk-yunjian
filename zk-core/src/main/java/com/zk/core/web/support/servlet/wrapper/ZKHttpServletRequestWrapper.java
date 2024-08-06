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
 * @Title: ZKHttpServletRequestWrapper.java 
 * @author Vinson 
 * @Package com.zk.webmvc.wrapper 
 * @Description: TODO(simple description this file what to do.) 
 * @date Dec 18, 2019 2:37:32 PM 
 * @version V1.0   
*/
package com.zk.core.web.support.servlet.wrapper;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;

import com.zk.core.web.utils.ZKServletUtils;
import com.zk.core.web.wrapper.ZKRequestWrapper;

/**
 * @ClassName: ZKHttpServletRequestWrapper
 * @Description: TODO(simple description this class what to do.)
 * @author Vinson
 * @version 1.0
 */
public class ZKHttpServletRequestWrapper extends HttpServletRequestWrapper implements ZKRequestWrapper {

    /**
     * 
     * @param request
     */
    public ZKHttpServletRequestWrapper(HttpServletRequest request) {
        super(request); // TODO Auto-generated constructor stub
    }

    /**
     * 
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getPathWithinApplication()
     */
    @Override
    public String getPathWithinApplication() {
        return ZKServletUtils.getPathWithinApplication(ZKServletUtils.toHttp(super.getRequest()));
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getWrapperRequest
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param <T>
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getWrapperRequest()
     */
    @SuppressWarnings("unchecked")
    @Override
    public <T> T getWrapperRequest() {
        return (T) super.getRequest();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getCookieValue
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param <T>
     * @param cookieName
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getCookieValue(java.lang.String)
     */
    @Override
    public String getCookieValue(String cookieName) {
//        Cookie c = ZKWebUtils.getCookie(ZKWebUtils.toHttp(super.getRequest()), cookieName);
        Cookie c = ZKServletUtils.getCookieByRequest(ZKServletUtils.toHttp(super.getRequest()), cookieName);
        return c == null ? null : c.getValue();
    }

    /**
     * (not Javadoc)
     * <p>
     * Title: getCleanParam
     * </p>
     * <p>
     * Description:
     * </p>
     * 
     * @param paramName
     * @return
     * @see com.zk.core.web.wrapper.ZKRequestWrapper#getCleanParam(java.lang.String)
     */
    @Override
    public String getCleanParam(String paramName) {
        return ZKServletUtils.getCleanParam(getRequest(), paramName);
    }

}
