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
* @Title: ZKSecAbstractAuthorizingAnnotationHandler.java 
* @author Vinson 
* @Package com.zk.security.authz 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 1:29:06 PM 
* @version V1.0 
*/
package com.zk.security.authz;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

import com.zk.core.exception.ZKSecAuthorizationException;
import com.zk.core.exception.base.ZKUnknownException;
import com.zk.security.common.ZKSecAnnotationResolver;
import com.zk.security.subject.ZKSecSubject;
import com.zk.security.utils.ZKSecSecurityUtils;

/** 
* @ClassName: ZKSecAbstractAuthorizingAnnotationHandler 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public abstract class ZKSecAbstractAuthorizingAnnotationHandler implements ZKSecAuthorizingAnnotationHandler {

    protected Class<? extends Annotation> annotationClass;

    private ZKSecAnnotationResolver resolver;

    public ZKSecAbstractAuthorizingAnnotationHandler(Class<? extends Annotation> annotationClass,
            ZKSecAnnotationResolver resolver) {
        this.annotationClass = annotationClass;
        this.resolver = resolver;
    }

    protected void setAnnotationClass(Class<? extends Annotation> annotationClass) throws IllegalArgumentException {
        if (annotationClass == null) {
            String msg = "annotationClass argument cannot be null";
            throw new ZKUnknownException(msg);
        }
        this.annotationClass = annotationClass;
    }

    /**
     * Returns the type of annotation this handler inspects and processes.
     *
     * @return the type of annotation this handler inspects and processes.
     */
    public Class<? extends Annotation> getAnnotationClass() {
        return this.annotationClass;
    }

    public ZKSecAnnotationResolver getResolver() {
        return resolver;
    }

    public void setResolver(ZKSecAnnotationResolver resolver) {
        this.resolver = resolver;
    }

    public boolean supports(MethodInvocation mi) {
        return getAnnotation(mi) != null;
    }

    protected Annotation getAnnotation(MethodInvocation mi) {
        return getResolver().getAnnotation(mi, this.getAnnotationClass());
    }

    @Override
    public void assertAuthorized(MethodInvocation mi) {
        try {
            Annotation a = getAnnotation(mi);
            if (a == null) {
                return;
            }
            this.doAssertAuthorized(a);
        }
        catch(ZKSecAuthorizationException ae) {
//            if (ae.getCause() == null)
//                ae.initCause(new AuthorizationException("Not authorized to invoke method: " + mi.getMethod()));
            throw ae;
        }
    }

    protected ZKSecSubject getZKSecSubject() {
        return ZKSecSecurityUtils.getSubject();
    }

    public abstract void doAssertAuthorized(Annotation annotation);

}
