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
* @Title: ZKDemoSubscription.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 11:42:16 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.my;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Executor;
import java.util.concurrent.locks.ReentrantLock;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/** 
* @ClassName: ZKDemoSubscription 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoSubscription implements Subscription {

    String data;

    Executor executor;

    Subscriber<? super String> actual;

    protected boolean isCanceled;

    protected ZKDemoSubscription next;

    Queue<String> dataQueue = new LinkedList<>();

    long requestCount = 0;

    long dispatchCount = 0;

    final ReentrantLock lock = new ReentrantLock();

    public ZKDemoSubscription(Executor executor, Subscriber<? super String> actual) {
        this.actual = actual;
        this.executor = executor;
    }

    @Override
    public void request(long n) {
        this.requestCount += n;
        this.doDispatch();
    }

    @Override
    public void cancel() {
        this.actual.onComplete();
        isCanceled = true;
    }

    public void cancelAll() {
        this.actual.onComplete();
        isCanceled = true;
        if (this.getNext() != null) {
            this.getNext().cancelAll();
        }
    }

    public void cancelAll(Throwable throwable) {
        this.actual.onError(throwable);
        isCanceled = true;
        if (this.getNext() != null) {
            this.getNext().cancelAll(throwable);
        }
    }

    public void setNext(ZKDemoSubscription subscription) {
        this.next = subscription;
    }

    public Subscriber<? super String> getActual() {
        return actual;
    }

    public ZKDemoSubscription getNext() {
        return next;
    }

    public boolean isCanceled() {
        return isCanceled;
    }

    public void dispatch(String data) {
        this.dataQueue.offer(data);
        this.doDispatch();
        if (this.getNext() != null) {
            this.getNext().dispatch(data);
        }
    }

    protected void doDispatch() {
        this.lock.lock();
        try {
//            executor.execute(new Runnable() {
//                @Override
//                public void run() {
                    try {
                        while (!dataQueue.isEmpty() && requestCount > dispatchCount) {
                            // System.out.println("--------- requestCount: " + requestCount);
                            // System.out.println("--------- dispatchCount: " + dispatchCount);
//                            System.out.println("------- " + dataQueue.size() + "  " + dataQueue.toString());
                            actual.onNext(dataQueue.poll());
                            dispatchCount = dispatchCount + 1;
                        }
                    }
                    catch(Exception e) {
                        actual.onError(e);
                        actual.onComplete();
                    }
//                }
//            });
        } finally {
            this.lock.unlock();
        }

    }

}
