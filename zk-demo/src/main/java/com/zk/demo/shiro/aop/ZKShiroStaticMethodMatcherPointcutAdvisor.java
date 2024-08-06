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
* @Title: ZKShiroStaticMethodMatcherPointcutAdvisor.java 
* @author Vinson 
* @Package com.zk.demo.shiro.aop 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 29, 2021 6:50:21 PM 
* @version V1.0 
*/
package com.zk.demo.shiro.aop;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.apache.shiro.authz.annotation.RequiresGuest;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.apache.shiro.authz.annotation.RequiresUser;
import org.apache.shiro.mgt.SecurityManager;
import org.springframework.aop.support.StaticMethodMatcherPointcutAdvisor;
import org.springframework.core.annotation.AnnotationUtils;


/** 
* @ClassName: ZKShiroStaticMethodMatcherPointcutAdvisor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKShiroStaticMethodMatcherPointcutAdvisor extends StaticMethodMatcherPointcutAdvisor {

    /**
     * @Fields serialVersionUID : TODO(simple description what to do.)
     */
    private static final long serialVersionUID = -1423039621940468889L;

    @SuppressWarnings("unchecked")
    private static final Class<? extends Annotation>[] AUTHZ_ANNOTATION_CLASSES = new Class[] {
            RequiresPermissions.class, RequiresRoles.class, RequiresUser.class, RequiresGuest.class,
            RequiresAuthentication.class };

    protected SecurityManager securityManager = null;

    /**
     * Create a new AuthorizationAttributeSourceAdvisor.
     */
    public ZKShiroStaticMethodMatcherPointcutAdvisor() {
        setAdvice(new ZKShiroAopAllianceAnnotationsAuthorizingMethodInterceptor());
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
    }

    public void setSecurityManager(org.apache.shiro.mgt.SecurityManager securityManager) {
        this.securityManager = securityManager;
    }

    /**
     * Returns <tt>true</tt> if the method or the class has any Shiro
     * annotations, false otherwise. The annotations inspected are:
     * <ul>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresAuthentication
     * RequiresAuthentication}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresUser
     * RequiresUser}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresGuest
     * RequiresGuest}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresRoles
     * RequiresRoles}</li>
     * <li>{@link org.apache.shiro.authz.annotation.RequiresPermissions
     * RequiresPermissions}</li>
     * </ul>
     *
     * @param method
     *            the method to check for a Shiro annotation
     * @param targetClass
     *            the class potentially declaring Shiro annotations
     * @return <tt>true</tt> if the method has a Shiro annotation, false
     *         otherwise.
     * @see org.springframework.aop.MethodMatcher#matches(java.lang.reflect.Method,
     *      Class)
     */
    public boolean matches(Method method, Class<?> targetClass) {
        Method m = method;

        if (isAuthzAnnotationPresent(m)) {
            return true;
        }

        // The 'method' parameter could be from an interface that doesn't have
        // the annotation.
        // Check to see if the implementation has it.
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
