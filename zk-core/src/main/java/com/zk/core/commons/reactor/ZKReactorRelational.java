/** 
* Copyright (c) 2012-2024 ZK-Vinson Technologies, Inc.
* address: 
* All rights reserved. 
* 
* This software is the confidential and proprietary information of 
* ZK-Vinson Technologies, Inc. ("Confidential Information"). You shall not 
* disclose such Confidential Information and shall use it only in 
* accordance with the terms of the license agreement you entered into 
* with ZK-Vinson. 
*
* @Title: ZKReactorRelational.java 
* @author Vinson 
* @Package com.zk.core.commons.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 25, 2024 1:30:13 AM 
* @version V1.0 
*/
package com.zk.core.commons.reactor;

import org.reactivestreams.Subscription;

import reactor.core.Disposable;

/** 
* @ClassName: ZKReactorRelational 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKReactorRelational<T> {

    public ZKReactorRelational(ZKResultCollect<T> rc) {
        this.rc = rc;
    }

    ZKResultCollect<T> rc;

    Disposable disposable;

    Subscription subscription;

    /**
     * @return disposable sa
     */
    public Disposable getDisposable() {
        return disposable;
    }

    /**
     * @return subscription sa
     */
    public Subscription getSubscription() {
        return subscription;
    }

    /**
     * @param disposable
     *            the disposable to set
     */
    public void setDisposable(Disposable disposable) {
        this.disposable = disposable;
//        this.rc.doSubscribe();
    }

    /**
     * @param subscription
     *            the subscription to set
     */
    public void setSubscription(Subscription subscription) {
        this.subscription = subscription;
    }

    public boolean isFinsh() {
        if (this.getDisposable() != null) {
            return this.getDisposable().isDisposed();
        }
        return false;
    }

}
