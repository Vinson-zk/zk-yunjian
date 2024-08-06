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
* @Title: ZKShiroAopAllianceAnnotationsAuthorizingMethodInterceptor.java 
* @author Vinson 
* @Package com.zk.demo.shiro.aop 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:49:55 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.aop;

import java.util.Collection;

import org.apache.shiro.aop.MethodInvocation;
import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.aop.AuthorizingAnnotationMethodInterceptor;
import org.apache.shiro.spring.security.interceptor.AopAllianceAnnotationsAuthorizingMethodInterceptor;

import com.zk.core.exception.ZKBusinessException;

/** 
* @ClassName: ZKShiroAopAllianceAnnotationsAuthorizingMethodInterceptor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroAopAllianceAnnotationsAuthorizingMethodInterceptor
        extends AopAllianceAnnotationsAuthorizingMethodInterceptor {

    @Override
    protected void assertAuthorized(MethodInvocation methodInvocation) throws AuthorizationException {
        // default implementation just ensures no deny votes are cast:
        try {
            Collection<AuthorizingAnnotationMethodInterceptor> aamis = getMethodInterceptors();
            if (aamis != null && !aamis.isEmpty()) {
                for (AuthorizingAnnotationMethodInterceptor aami : aamis) {
                    if (aami.supports(methodInvocation)) {
                        aami.assertAuthorized(methodInvocation);
                    }
                }
            }
        }
        catch(AuthorizationException e) {
            // 您没有操作权限
            throw ZKBusinessException.as("zk.test.000003", e);
        }
    }

}
