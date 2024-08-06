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
* @Title: ZKSecSubjectCallable.java 
* @author Vinson 
* @Package com.zk.security.subject 
* @Description: TODO(simple description this file what to do. ) 
* @date Jul 26, 2021 7:02:11 PM 
* @version V1.0 
*/
package com.zk.security.subject;

import java.util.concurrent.Callable;

import com.zk.security.thread.ZKSecThreadContext;

/** 
* @ClassName: ZKSecSubjectCallable 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKSecSubjectCallable<V> implements Callable<V> {

    private final Callable<V> callable;

    private final ZKSecSubject subject;

    @Override
    public V call() throws Exception {
        ZKSecThreadContext.remove();
        ZKSecThreadContext.bind(subject);
        return this.callable.call();
    }

    public ZKSecSubjectCallable(ZKSecSubject subject, Callable<V> delegate) {
        this.subject = subject;
        this.callable = delegate;
    }

}
