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
* @Title: ZKJavaSubscriber.java 
* @author Vinson 
* @Package com.zk.demo.reactor.java 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 21, 2024 10:45:28 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.java;

import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;

/** 
* @ClassName: ZKJavaSubscriber 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKJavaSubscriber implements Subscriber<String> {

    Subscription subscription;

    String name;

    boolean isFinish = false;

    public ZKJavaSubscriber(String name) {
        this.name = name;
    }

    @Override
    public void onSubscribe(Subscription subscription) {
        System.out.println(
                "[^_^:20240321-2246-001] ZKJavaSubscriber.onSubscribe: " + this.name + " " + subscription.hashCode());
        this.subscription = subscription;
        this.subscription.request(1);
    }

    @Override
    public void onNext(String item) {
        System.out.println("[^_^:20240321-2246-001] ZKJavaSubscriber.onNext: " + this.name + " " + item);
        this.subscription.request(1);
        try {
            Thread.sleep(1 * 1000);
        }
        catch(InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        System.out.println(
                "[^_^:20240321-2246-001] ZKJavaSubscriber.onError: " + this.name + " " + throwable.getMessage());
        this.finish();
    }

    @Override
    public void onComplete() {
        System.out.println("[^_^:20240321-2246-001] ZKJavaSubscriber.onComplete" + this.name);
        this.finish();
    }

    private void finish() {
        this.isFinish = true;
    }

    public boolean isFinish() {
        return this.isFinish;
    }

}
