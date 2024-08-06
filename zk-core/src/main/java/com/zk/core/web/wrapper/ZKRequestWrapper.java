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
* @Title: ZKRequestWrapper.java 
* @author Vinson 
* @Package com.zk.core.web.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 16, 2024 9:13:19 AM 
* @version V1.0 
*/
package com.zk.core.web.wrapper;

/** 
* @ClassName: ZKRequestWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKRequestWrapper {

    public Object getAttribute(String name);

    public void setAttribute(String key, Object value);

    public void removeAttribute(String key);

    public String getPathWithinApplication();

    public String getParameter(String name);

    public String getContextPath();

    public String getCookieValue(String cookieName);

    public String getRequestURI();

    public String getHeader(String name);
    
    public <T> T getWrapperRequest();

    public String getCleanParam(String paramName);

}
