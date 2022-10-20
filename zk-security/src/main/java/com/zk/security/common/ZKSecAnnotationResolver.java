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
* @Title: ZKSecAnnotationResolver.java 
* @author Vinson 
* @Package com.zk.security.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 28, 2021 1:52:57 PM 
* @version V1.0 
*/
package com.zk.security.common;

import java.lang.annotation.Annotation;

import org.aopalliance.intercept.MethodInvocation;

/** 
* @ClassName: ZKSecAnnotationResolver 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public interface ZKSecAnnotationResolver {

    Annotation getAnnotation(MethodInvocation mi, Class<? extends Annotation> clazz);

}
