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
* @Title: ZKDemoPublisher.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 10:28:51 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.my;

import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.locks.ReentrantLock;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import io.netty.util.concurrent.ThreadPerTaskExecutor;

/** 
* @ClassName: ZKDemoPublisher 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoPublisher implements Publisher<String> {

    protected static final Executor ASYNC_POOL = (ForkJoinPool.getCommonPoolParallelism() > 1)
        ? ForkJoinPool.commonPool()
        : new ThreadPerTaskExecutor(null);

    final ReentrantLock lock = new ReentrantLock();
    ZKDemoSubscription subscription;

    @Override
    public void subscribe(Subscriber<? super String> sub) {
        this.lock.lock();
        try {
            ZKDemoSubscription subscription = new ZKDemoSubscription(ASYNC_POOL, sub);
            
            ZKDemoSubscription pred = this.getLastSubscription();

            try {
                sub.onSubscribe(subscription);
            }
            catch(Exception e) {
                sub.onError(e);
            }

            if (pred == null) {
                this.subscription = subscription;
            }
            else {
                pred.setNext(subscription);
            }

        } finally {
            this.lock.unlock();
        }
    }

    // 取最后一个 Subscription
    public ZKDemoSubscription getLastSubscription() {
        ZKDemoSubscription location = this.subscription, pred = null;
        while (location != null) {
            if (location.isCanceled()) {
                if (pred == null) {
                    this.subscription = location.next;
                }
                else {
                    pred.setNext(location.next);
                }
                location = location.next;
            }
            else {
                pred = location;
                location = pred.next;
            }
        }
        return pred;
    }

    public void put(String data) {
        ASYNC_POOL.execute(new Runnable() {

            @Override
            public void run() {
                subscription.dispatch(data);
            }
        });
    }

    public void close() {
        this.subscription.cancelAll();
    }

    public void close(Throwable e) {
        this.subscription.cancelAll(e);
    }

}
