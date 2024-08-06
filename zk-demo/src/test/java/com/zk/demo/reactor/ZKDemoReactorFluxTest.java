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
* @Title: ZKDemoReactorFluxTest.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 1, 2024 1:49:44 PM 
* @version V1.0 
*/
package com.zk.demo.reactor;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.zk.demo.ZKTestHelper.ZK;

import junit.framework.TestCase;
import reactor.core.publisher.Flux;

/** 
* @ClassName: ZKDemoReactorFluxTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoReactorFluxTest {

    @Test
    public void testHelloWord() {
        System.out.println("[^_^:20240322-2251-001] --------------------------------");
        Flux<String> f = Flux.just("reactor Flux Hello");
        f.map(a -> a + " World").subscribe(System.out::println);

        System.out.println("[^_^:20240322-2251-001] ================================");
    }

    @Test
    public void test() {
        System.out.println("[^_^:20240322-2251-00] --------------------------------");

//        Thread t1 = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                
//                while (Mono.when(m).) {
//
//                    System.out.println(
//                            "[^_^:20240321-2250-001] sub.isFinish(): ");
//                    try {
//                        Thread.sleep(1500);
//                    }
//                    catch(InterruptedException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        });
//        t1.start();
//        t1.join();
        System.out.println("[^_^:20240322-2251-00] ================================");
    }

    @Test
    public void testFlux() {
        System.out.println("[^_^:20240322-2251-002] --------------------------------");

        Flux<String> flux = Flux.just("flux.data.01", "flux.data.02", "flux.data.03");

        StringBuffer resSb = new StringBuffer();
        String expectedStr = "map->subscribe->map->subscribe->map->subscribe->doFinally->";

        flux = flux.map(p -> {
            ZK.sleep("testFlux.flux.map", Math.random());
            resSb.append("map->");
            p = "flux.map->" + p;
            System.out.println("[^_^:20240321-2251-002.01] flux.map p: " + p);
            return p;
        });

        flux = flux.doFinally(onFinally -> {
            resSb.append("doFinally->");
            System.out.println("[^_^:20240321-2251-002.02] flux.doFinally onFinally: " + onFinally);
        });

        flux.subscribe(consumer -> {
            ZK.sleep("testFlux.flux.map", Math.random());
            resSb.append("subscribe->");
            System.out.println("[^_^:20240321-2251-002.03] flux.subscribe consumer: " + consumer);
        });

        System.out.println("[^_^:20240321-2251-002.04] resSb: " + resSb.toString());

        System.out.println("[^_^:20240322-2251-002] ================================");
        TestCase.assertEquals(expectedStr, resSb.toString());
    }

    @Test
    public void testFluxNested() {
        System.out.println("[^_^:20240322-2251-003] --------------------------------");

        Flux<Integer> flux = Flux.just(1, 2, 3);
        int count = 8;

        Flux<List<String>> fluxRes = flux.map(p -> {
            System.out.println("[^_^:20240321-2251-003.01] flux.map p: " + p);
//            ZKTestHelper.sleep("testFlux.flux.map", Math.random());
            String[] strs = new String[count];
            for (int i = 0; i < count; ++i) {
                strs[i] = "fluxNested.data." + i + "." + p;
            }

            Flux<String> f = Flux.defer(() -> {
                ZK.sleep("testFlux.f.map", 1 + Math.random());
                return Flux.just(strs);
            });

            f = f.map(item -> {
                ZK.sleep("testFlux.f.map", Math.random());
                item = "f.map->" + item;
                System.out.println("[^_^:20240321-2251-003.02] f.map item: " + item);
                return item;
            });

            List<String> resList = new ArrayList<>();
            f.subscribe(consumer -> {
//                ZKTestHelper.sleep("testFlux.f.map", Math.random());
                System.out.println("[^_^:20240321-2251-003.03] f.subscribe item: " + consumer);
                resList.add(consumer);
            });
            return resList;
        });
        

        fluxRes.subscribe(consumer -> {
            TestCase.assertEquals(count, consumer.size());
//            ZKTestHelper.sleep("testFlux.flux.map", Math.random());
            consumer.forEach(item -> {
                System.out.println("[^_^:20240321-2251-003.04] fluxRes.subscribe consumer.forEach: " + item);
            });
        });

        System.out.println("[^_^:20240322-2251-003] ================================");
    }

}
