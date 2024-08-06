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
* @Title: ZKSecAnnotationAuthorizationMethodInterceptor.java 
* @author Vinson 
* @Package com.zk.security.web.support.spring.interceptor 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 12:51:24 PM 
* @version V1.0 
*/
package com.zk.security.support.spring.interceptor;

import java.util.HashSet;
import java.util.Set;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.zk.core.exception.ZKSecAuthorizationException;
import com.zk.security.authz.ZKSecAuthorizingAnnotationHandler;
import com.zk.security.authz.hander.ZKSecApiCodeAnnotationHandler;
import com.zk.security.common.ZKSecAnnotationResolver;
import com.zk.security.common.ZKSecDefaultAnnotationResolver;

/** 
* @ClassName: ZKSecAnnotationAuthorizationMethodInterceptor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecAnnotationAuthorizationMethodInterceptor implements MethodInterceptor {

    protected final Logger log = LogManager.getLogger(this.getClass());

    protected final Set<ZKSecAuthorizingAnnotationHandler> secAuthorizationSet = new HashSet<ZKSecAuthorizingAnnotationHandler>();

    /**
     * 构造函数，添加鉴权器
     */
    public ZKSecAnnotationAuthorizationMethodInterceptor() {
        ZKSecAnnotationResolver resolver = new ZKSecDefaultAnnotationResolver();
        // api 接口代码 鉴定器
        secAuthorizationSet.add(new ZKSecApiCodeAnnotationHandler(resolver));
    }

    public Set<ZKSecAuthorizingAnnotationHandler> getSecAuthorizationSet() {

        return secAuthorizationSet;
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {

        try {
            for (ZKSecAuthorizingAnnotationHandler secAuthcMethodInterceptor : getSecAuthorizationSet()) {
                // 通异常，终止功能继续执行 // sec.000014=没有访问权限 throw new
                // MsgException("zk.sec.000014", ue, null, ue.getMessage());
                secAuthcMethodInterceptor.assertAuthorized(invocation);
            }
        }
        catch(ZKSecAuthorizationException authE) {
            authE.printStackTrace();
            log.error("[>_<:20220511-1923-001] [zk.sec.000003] 您没有操作权限:[{}]", authE.getDataStr());
//          logger.error("您没有操作权限; [{}]", apiCode);
            throw com.zk.core.exception.ZKSecAuthorizationException.as("zk.sec.000003", authE.getDataStr());
        }

        return invocation.proceed();
    }

}
