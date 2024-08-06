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
* @Title: ZKDemoProcessor.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 19, 2024 3:13:59 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.my;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

/** 
* @ClassName: ZKDemoProcessor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoProcessor extends ZKDemoPublisher implements Subscriber<String> {

    Subscription subscription;

//    Subscriber<? super String> subscriber;

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("[^_^:20240330-0924-001] Processor.onSubscribe");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("[^_^:20240330-0924-001] Processor.onNext: " + item);
        this.subscription.request(1);
        this.put(item);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("[^_^:20240330-0924-001] Processor.onNext: " + throwable.getMessage());
        super.close(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("[^_^:20240330-0924-001] Processor.onComplete: ");
        this.close();
    }

    // ---------------------------------------
//    @Override
//    public void subscribe(Subscriber<? super String> subscriber) {
//        System.out.println("[^_^:20240330-0924-001] Processor.subscribe: " + subscriber.hashCode());
//        Subscription subscription = new ZKDemoSubscription(ZKDemoPublisher.ASYNC_POOL, subscriber);
//        subscriber.onSubscribe(subscription);
//    }

}
