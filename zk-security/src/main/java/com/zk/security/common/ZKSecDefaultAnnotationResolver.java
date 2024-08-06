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
* @Title: ZKSecDefaultAnnotationResolver.java 
* @author Vinson 
* @Package com.zk.security.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 1:53:47 PM 
* @version V1.0 
*/
package com.zk.security.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.aopalliance.intercept.MethodInvocation;
import org.springframework.core.annotation.AnnotationUtils;

import com.zk.core.exception.base.ZKUnknownException;
import com.zk.core.utils.ZKAnnotationUtils;
import com.zk.core.utils.ZKClassUtils;

/** 
* @ClassName: ZKSecDefaultAnnotationResolver 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecDefaultAnnotationResolver implements ZKSecAnnotationResolver {

    @Override
    public Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz) {

        if (mi == null) {
            throw new ZKUnknownException("method argument cannot be null");
        }
        Method m = mi.getMethod();

        Annotation a = ZKAnnotationUtils.findAnnotation(m, clazz);
        if (a != null)
            return a;

        Class<?> targetClass = mi.getThis().getClass();
        m = ZKClassUtils.getMostSpecificMethod(m, targetClass);
        a = ZKAnnotationUtils.findAnnotation(m, clazz);
        if (a != null)
            return a;
        // See if the class has the same annotation
        return AnnotationUtils.findAnnotation(mi.getThis().getClass(), clazz);
    }

}
