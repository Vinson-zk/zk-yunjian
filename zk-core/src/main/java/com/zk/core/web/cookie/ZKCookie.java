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
* @Title: ZKCookie.java 
* @author Vinson 
* @Package com.zk.security.web.cookie 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 27, 2021 12:11:40 AM 
* @version V1.0 
*/
package com.zk.core.web.cookie;

import com.zk.core.web.wrapper.ZKRequestWrapper;
import com.zk.core.web.wrapper.ZKResponseWrapper;

/** 
* @ClassName: ZKCookie 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKCookie {

    /**
     * The number of seconds in one year (= 60 * 60 * 24 * 365).
     */
    public static final int ONE_YEAR = 60 * 60 * 24 * 365;

    /**
     * Root path to use when the path hasn't been set and request context root
     * is empty or null.
     */
    public static final String ROOT_PATH = "/";

    String getName();

    void setName(String name);

    String getValue();

    void setValue(String value);

    String getComment();

    void setComment(String comment);

    String getDomain();

    void setDomain(String domain);

    int getMaxAge();

    void setMaxAge(int maxAge);

    String getPath();

    void setPath(String path);

    boolean isSecure();

    void setSecure(boolean secure);

    int getVersion();

    void setVersion(int version);

    void setHttpOnly(boolean httpOnly);

    boolean isHttpOnly();

    void saveTo(ZKRequestWrapper request, ZKResponseWrapper response);

    void removeFrom(ZKRequestWrapper request, ZKResponseWrapper response);

    String readValue(ZKRequestWrapper request, ZKResponseWrapper response);

}
