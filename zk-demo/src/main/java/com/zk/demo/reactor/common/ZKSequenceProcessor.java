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
* @Title: ZKSequenceProcessor.java 
* @author Vinson 
* @Package com.zk.demo.reactive 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 20, 2024 11:00:28 AM 
* @version V1.0 
*/
package com.zk.demo.reactor.common;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Flow.Publisher;
import java.util.concurrent.Flow.Subscriber;

/**
 * @ClassName: ZKSequenceProcessor
 * @Description: TODO(simple description this class what to do. )
 * @author Vinson
 * @version 1.0
 */
public class ZKSequenceProcessor<T> {

    Queue<ZKStationProcessor<T>> queue = new LinkedList<>();

    public void dispatcher(Publisher<T> publisher, Subscriber<T> subscriber) {
//        ZKStationProcessor<T> zkStationProcessor = new ZKStationProcessor<T>();
//        zkStationProcessor.subscribe(publisher, subscriber);
//
//        zkStationProcessor.subscribe(subscriber);
//
//        Mono<Void> mfp = Mono.create(callback -> {
////            return null;
//        });
    }

}
