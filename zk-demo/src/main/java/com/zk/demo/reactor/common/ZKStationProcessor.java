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
* @Title: ZKStationProcessor.java 
* @author Vinson 
* @Package com.zk.demo.reactive.common 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 20, 2024 4:27:45 PM 
* @version V1.0 
*/
package com.zk.demo.reactor.common;
/** 
* @ClassName: ZKStationProcessor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;
import java.util.concurrent.Flow.Subscription;
import java.util.concurrent.SubmissionPublisher;

//public class ZKStationProcessor<T> implements Publisher<T>, Subscriber<T> {
public class ZKStationProcessor<T> extends SubmissionPublisher<T> implements Subscriber<T> {

    Subscription subscription;

    /**
     * 贮存消息发布者返回数据
     */
    Queue<ZKStationDataNode<T>> queue = new LinkedList<>();

    /**
     * 标识是否开始向订阅者返回数据
     */
    boolean start = false; //

    @Override
    public void onSubscribe(Subscription subscription) {
        this.subscription = subscription;
        this.subscription.request(1);
        queue.offer(new ZKStationDataNodeOnSubscribe<>());
        this.doExecute();
    }

    @Override
    public void onNext(T item) {
        queue.offer(new ZKStationDataNodeOnNext<T>(item));
        this.subscription.request(1);
        this.doExecute();
    }

    @Override
    public void onError(Throwable throwable) {
        queue.offer(new ZKStationDataNodeOnError<T>(throwable));
        this.doExecute();
    }

    @Override
    public void onComplete() {
        queue.offer(new ZKStationDataNodeOnComplete<T>());
        this.doExecute();
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {
        // TODO Auto-generated method stub
    }

    public void subscribe(Publisher<T> publisher, Subscriber<T> subscriber) {
        publisher.subscribe(this);
//        super.subscribe(subscriber);
    }

    public void doExecute() {
        while (this.start && queue.iterator().hasNext()) {
            queue.poll().doOffer(this);
        }
    }

    public void doExecuteIng() {
        this.start = true;
        this.doExecute();
    }

}
