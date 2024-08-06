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
* @Title: ZKSecStaticMethodMatcherPointcutAdvisor.java 
* @author Vinson 
* @Package com.zk.security.web.support.spring 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 12:43:39 PM 
* @version V1.0 
*/
package com.zk.security.support.spring;
/** 
* @ClassName: ZKSecStaticMethodMatcherPointcutAdvisor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;

import com.zk.security.annotation.ZKSecApiCode;
import com.zk.security.support.spring.interceptor.ZKSecAnnotationAuthorizationMethodInterceptor;

public class ZKSecStaticMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unchecked")
    protected static final Class<? extends Annotation>[] AUTHZ_ANNOTATION_CLASSES = new Class[] { ZKSecApiCode.class };

    public ZKSecStaticMethodMatcherPointcutAdvisor() {
        setAdvice(new ZKSecAnnotationAuthorizationMethodInterceptor());
    }

    public boolean matches(Method method, Class<?> targetClass) {
        Method m = method;
        // 如果方法有权限代码注解，返回 true
        if (isAuthzAnnotationPresent(m)) {
            return true;
        }

        // The 'method' parameter could be from an interface that doesn't have
        // the annotation.
        // Check to see if the implementation has it.
        // 功能方法可能来自未注解权限代码的接口，检查具体实现类的功能方法是否有注解权限代码
        if (targetClass != null) {
            try {
                m = targetClass.getMethod(m.getName(), m.getParameterTypes());
                return isAuthzAnnotationPresent(m) || isAuthzAnnotationPresent(targetClass);
            }
            catch(NoSuchMethodException ignored) {
                // default return value is false. If we can't find the method,
                // then obviously
                // there is no annotation, so just use the default return value.
            }
        }

        return false;
    }

    private boolean isAuthzAnnotationPresent(Class<?> targetClazz) {
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(targetClazz, annClass);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断方法是否有注解权限代码
     *
     * @Title: isAuthzAnnotationPresent
     * @Description: TODO(simple description this method what to do.)
     * @author Vinson
     * @date Jul 28, 2021 1:17:28 PM
     * @param method
     * @return
     * @return boolean
     */
    private boolean isAuthzAnnotationPresent(Method method) {
        for (Class<? extends Annotation> annClass : AUTHZ_ANNOTATION_CLASSES) {
            Annotation a = AnnotationUtils.findAnnotation(method, annClass);
            if (a != null) {
                return true;
            }
        }
        return false;
    }

}
