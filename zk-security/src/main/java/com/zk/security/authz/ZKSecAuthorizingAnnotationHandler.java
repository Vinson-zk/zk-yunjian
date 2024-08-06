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
* @Title: ZKSecAuthorizingAnnotationHandler.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 1:26:34 PM 
* @version V1.0 
*/
package com.zk.security.authz;

import org.aopalliance.intercept.MethodInvocation;

import com.zk.core.exception.ZKSecAuthorizationException;

/**
 * 针对不同权限代码注解实现拦截处理
 * 
 * @ClassName: ZKSecAuthorizingAnnotationHandler
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public interface ZKSecAuthorizingAnnotationHandler {

    /**
     * 鉴权接口
     *
     * @Title: assertAuthorized
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 28, 2021 2:22:08 PM
     * @param invocation
     * @throws ZKSecAuthorizationException
     * @return void
     */
    void assertAuthorized(MethodInvocation invocation) throws ZKSecAuthorizationException;

}
