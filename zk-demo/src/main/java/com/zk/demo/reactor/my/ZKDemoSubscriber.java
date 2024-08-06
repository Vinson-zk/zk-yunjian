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
* @Title: ZKDemoSubscriber.java 
* @author Vinson 
* @Package com.zk.demo.reactive 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 20, 2024 9:34:01 AM 
* @version V1.0 
*/
package com.zk.demo.reactor.my;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/** 
* @ClassName: ZKDemoSubscriber 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoSubscriber implements Subscriber<String> {

    Subscription subscription;

    String name;

    boolean isFinish = false;

    public ZKDemoSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("[^_^:20240330-0924-001] Subscriber.onSubscribe: " + this.name);
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("[^_^:20240330-0924-001] Subscriber.onNext: " + this.name + " " + item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("[^_^:20240330-0924-001] Subscriber.onNext: " + this.name + " " + throwable.getMessage());
        this.finish();
    }

    @Override
    public void onComplete() {
        System.out.println("[^_^:20240330-0924-001] Subscriber.onComplete: " + this.name);
        this.finish();
    }

    private void finish() {
        this.isFinish = true;
    }

    public boolean isFinish() {
        return this.isFinish;
    }

}

