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
* @Title: ZKJavaProcessor.java 
* @author Vinson 
* @Package com.zk.demo.reactor.java 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 11:23:36 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.java;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

/** 
* @ClassName: ZKJavaProcessor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJavaProcessor extends SubmissionPublisher<String> implements Subscriber<String> {

    Subscription subscription;

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println("[^_^:20240330-0924-001] Processor.onSubscribe");
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("[^_^:20240330-0924-001] Processor.onNext: " + item);
        super.submit(item);
        this.subscription.request(1);
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println("[^_^:20240330-0924-001] Processor.onNext: " + throwable.getMessage());
        super.closeExceptionally(throwable);
    }

    @Override
    public void onComplete() {
        System.out.println("[^_^:20240330-0924-001] Processor.onComplete: ");
        super.close();
    }

    // ---------------------------------------
    @Override
    public void subscribe(Subscriber<? super String> subscriber) {
        System.out.println("[^_^:20240330-0924-001] Processor.subscribe: " + subscriber.hashCode());
        super.subscribe(subscriber);
//        this.subscriber.onSubscribe(subscription);
    }

}
