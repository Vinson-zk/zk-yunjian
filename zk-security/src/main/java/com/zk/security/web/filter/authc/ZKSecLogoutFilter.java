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
* @Title: ZKSecLogoutFilter.java 
* @author Vinson 
* @Package com.zk.security.web.filter.authc 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 9:08:46 AM 
* @version V1.0 
*/
package com.zk.security.web.filter.authc;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.utils.ZKSecSecurityUtils;
import com.zk.security.web.filter.ZKSecAdviceFilter;
import com.zk.security.web.wrapper.ZKSecRequestWrapper;
import com.zk.security.web.wrapper.ZKSecResponseWrapper;

/** 
* @ClassName: ZKSecLogoutFilter 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecLogoutFilter extends ZKSecAdviceFilter {

    @Override
    protected boolean onPreHandle(ZKSecRequestWrapper request, ZKSecResponseWrapper response, Object mappedValue)
            throws Exception {
        ZKSecSubject subject = ZKSecSecurityUtils.getSubject();
        try {
            subject.logout();
        }
        catch(ZKUnknownException se) {
            logger.debug("Encountered session exception during logout.  This can generally safely be ignored.", se);
        }
        return true;
    }

}
