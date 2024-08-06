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
* @Title: ZKSecRequestWrapper.java 
* @author Vinson 
* @Package com.zk.security.web.wrapper 
* @Description: TODO(simple description this file what to do. ) 
* @date Jan 15, 2024 5:10:07 PM 
* @version V1.0 
*/
package com.zk.security.web.wrapper;

import java.io.Serializable;

import com.zk.core.web.wrapper.ZKRequestWrapper;

/** 
* @ClassName: ZKSecRequestWrapper 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecRequestWrapper extends ZKRequestWrapper {

    boolean isLoginSubmission();

    default Serializable getTikcetId() {
        return getTikcetId(false);
    }

    Serializable getTikcetId(boolean isTicketCookieEnabled);

}
