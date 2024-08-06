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
* @Title: ZKDemoProcessorTest.java 
* @author Vinson 
* @Package com.zk.demo.reactive 
* @Description: TODO(simple description this file what to do. ) 
* @date Mar 20, 2024 8:53:51 AM 
* @version V1.0 
*/
package com.zk.demo.reactor.my;

import org.junit.Test;

import junit.framework.TestCase;

/** 
* @ClassName: ZKDemoProcessorTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoProcessorTest {

    @Test
    public void testDemoProcessor() {
        try {

//            Flux<String> f;

//            f.subscribe(actual);

//            f.just(data)

//            SubmissionPublisher<String> publisher = new SubmissionPublisher<>();
            ZKDemoPublisher publisher = new ZKDemoPublisher();

            ZKDemoProcessor zkDemoProcessor = new ZKDemoProcessor();
            ZKDemoSubscriber zkDemoSubscriber = new ZKDemoSubscriber("d");

            publisher.subscribe(zkDemoProcessor);
            zkDemoProcessor.subscribe(zkDemoSubscriber);
            
            publisher.put("t1");
            publisher.put("t2");
            publisher.put("t3");
            publisher.put("t4");
            publisher.put("t5");

            publisher.close();
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
