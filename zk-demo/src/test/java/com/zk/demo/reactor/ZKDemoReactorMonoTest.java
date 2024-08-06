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
* @Title: ZKDemoReactorMonoTest.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 1, 2024 10:41:42 AM 
* @version V1.0 
*/
package com.zk.demo.reactor;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import org.junit.Test;

import com.zk.core.utils.ZKDateUtils;
import com.zk.core.utils.ZKExceptionsUtils;
import com.zk.demo.ZKTestHelper.ZK;

import junit.framework.TestCase;
import reactor.core.Disposable;
import reactor.core.publisher.Mono;

/** 
* @ClassName: ZKDemoReactorMonoTest 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoReactorMonoTest {

    @Test
    public void testHelloWord() {
        System.out.println("[^_^:20240321-2251-001] --------------------------------");
        Mono<String> m = Mono.just("reactor Mono Hello ");
        m.map(a -> a + " World").subscribe(System.out::println);
        System.out.println("[^_^:20240321-2251-001] ================================");
    }

    @Test
    public void test() {
        System.out.println("[^_^:20240321-2251-000] --------------------------------");
//        Mono.just("hello").flatMap(a -> Mono.just(a + " world")).subscribe(System.out::println);

        Mono<String> m = Mono.create(callback -> {
            Runnable r = new Runnable() {
                public void run() {
                    System.out.println("[^_^:20240321-2251-000.01] m.create");
                    ZK.sleep("getMono.for: ", 2 + Math.random());
                    callback.success("ss");
                }
            };
            r.run();
        });

        Runnable r = new Runnable() {
            public void run() {
                m.subscribe(consumer -> {
                    System.out.println("[^_^:20240321-2251-000.02] m.subscribe consumer: " + consumer);
                });
            }
        };
        r.run();

        System.out.println("[^_^:20240321-2251-000] ================================");

    }

    @Test
    public void testMono() {
        System.out.println("[^_^:20240321-2251-002] --------------------------------");
        
        String data = "testMono.data 1 ";
        Mono<String> m = null;
        m = Mono.empty();
        StringBuffer resSb = new StringBuffer();
        String expectedStr = "doOnSubscribe->onSuccess->map->subscribe->doFinally->doOnSubscribe->onSuccess->map->doFinally->";

        // Mono.just 和 Mono.create 运行流程一样
//        m = Mono.just(data);
        m = Mono.create(callback -> {
            callback.success(data);
        });
        // 与添加位置顺序有关系
//        m = m.doOnNext(onNext -> {
//            resSb.append("onNext->");
//            System.out.println("[^_^:20240321-2251-002.05] m.doOnNext onNext: " + onNext);
//            ZK.sleep("testMono.m.doOnNext", Math.random());
//        });
        m = m.doOnSuccess(onSuccess -> {
            ZK.sleep("testMono.m.doOnSuccess", Math.random());
            resSb.append("onSuccess->");
            System.out.println("[^_^:20240321-2251-002.01] m.doOnSuccess onSuccess: " + onSuccess);
        });
        m = m.doFinally(onFinally -> {
            ZK.sleep("testMono.m.doFinally", Math.random());
            resSb.append("doFinally->");
            System.out.println("[^_^:20240321-2251-002.02] m.doFinally onFinally: " + onFinally);
        });
        m = m.doOnSubscribe(subscription -> {
            ZK.sleep("testMono.m.doOnSubscribe", Math.random());
            resSb.append("doOnSubscribe->");
            System.out.println("[^_^:20240321-2251-002.03] m.doOnSubscribe subscription: " + subscription);
        });
        m = m.map(p -> {
            ZK.sleep("testMono.m.map", Math.random());
            resSb.append("map->");
            p = "map -> " + p;
            System.out.println("[^_^:20240321-2251-002.04] m.map: " + p);
            return p;
        });

        Consumer<String> consumer = p -> {
            ZK.sleep("testMono.m.subscribe", Math.random());
            resSb.append("subscribe->");
            System.out.println("[^_^:20240321-2251-002.06] m.subscribe p: " + p);
        };

        m.subscribe(consumer);

        ////////////////////////
        // block()
        System.out.println("[^_^:20240321-2251-002.07] m.block(): " + m.block());
        System.out.println("[^_^:20240321-2251-002.08] resSb: " + resSb.toString());
        TestCase.assertEquals(expectedStr, resSb.toString());
        System.out.println("[^_^:20240321-2251-002] ================================");
    }

    // 嵌套 mono
    @Test
    public void testMonoNested() {
        System.out.println("[^_^:20240321-2251-003] --------------------------------");
        
        String data1 = "testMonoNested.data 1";
        Mono<String> m1 = null;
        StringBuffer resSb = new StringBuffer();
        String expectedStr = "m1.map.testMonoNested.data 1; m2.map.m1.map.testMonoNested.data 1";

        m1 = Mono.just(data1).map(p1 -> {
            ZK.sleep("testMonoNested.m1.doOnSubscribe", Math.random());
            p1 = "m1.map." + p1;
            System.out.println("[^_^:20240321-2251-003.01] m1.map: " + p1);
            Mono<String> m2 = Mono.just(p1);
            m2 = m2.map(p2 -> {
                ZK.sleep("testMonoNested.m2.doOnSubscribe", Math.random());
                p2 = "m2.map." + p2;
                System.out.println("[^_^:20240321-2251-003.02] m2.map: " + p2);
                return p2;
            });
            p1 = p1 + "; " + m2.block();
            return p1;
        });

        m1.subscribe(consumer -> {
            ZK.sleep("testMono.mEmpty.subscribe", Math.random());
            System.out.println("[^_^:20240321-2251-003.03] mEmpty.subscribe.consumer: " + consumer);
            resSb.append(consumer);
        });
        TestCase.assertEquals(expectedStr, resSb.toString());
        System.out.println("[^_^:20240321-2251-003] ================================");

    }

    @Test
    public void testMonoDefer() {
        System.out.println("[^_^:20240321-2251-004] --------------------------------");
        Mono<String> m1 = null, m2 = null;
        StringBuffer m1ExpectedSb1 = new StringBuffer();
        StringBuffer m2ExpectedSb1 = new StringBuffer();
        StringBuffer m2ExpectedSb2 = new StringBuffer();

        m1ExpectedSb1.append(
                "m1.just: " + ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
        m1 = Mono.just(m1ExpectedSb1.toString());
        m2 = Mono.defer(() -> {
            ZK.sleep("testMonoNested.m1.defer", Math.random());
            if (m2ExpectedSb1.isEmpty()) {
                m2ExpectedSb1.append("m2.defer: "
                        + ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                return Mono.just(m2ExpectedSb1.toString());
            }
            else {
                m2ExpectedSb2.append("m2.defer: "
                        + ZKDateUtils.formatDate(ZKDateUtils.getToday(), ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                return Mono.just(m2ExpectedSb2.toString());
            }
        });
        
        StringBuffer m1ResSb1 = new StringBuffer();
        StringBuffer m1ResSb2 = new StringBuffer();
        StringBuffer m2ResSb1 = new StringBuffer();
        StringBuffer m2ResSb2 = new StringBuffer();
        m1.subscribe(p -> {
            m1ResSb1.append(p);
            System.out.println("[^_^:20240321-2251-004.01] m1.01.subscribe: " + p);
        });
        m2.subscribe(p -> {
            m2ResSb1.append(p);
            System.out.println("[^_^:20240321-2251-004.01] m2.01.subscribe: " + p);
        });
        // 修息1秒以上
        ZK.sleep("testMonoNested.m1.defer", 1 + Math.random());
        m1.subscribe(p -> {
            m1ResSb2.append(p);
            System.out.println("[^_^:20240321-2251-004.01] m1.02.subscribe: " + p);
        });
        m2.subscribe(p -> {
            m2ResSb2.append(p);
            System.out.println("[^_^:20240321-2251-004.01] m2.02.subscribe: " + p);
        });

        TestCase.assertEquals(m1ExpectedSb1.toString(), m1ResSb1.toString());
        TestCase.assertEquals(m1ExpectedSb1.toString(), m1ResSb2.toString());
        TestCase.assertEquals(m2ExpectedSb1.toString(), m2ResSb1.toString());
        TestCase.assertEquals(m2ExpectedSb2.toString(), m2ResSb2.toString());
        System.out.println("[^_^:20240321-2251-004] ================================");

    }

    @Test
    public void testDisposable() {
        System.out.println("[^_^:20240321-2251-005] --------------------------------");

        String data1 = "testDisposable.data 1";
        Mono<String> m1 = null;
        
        m1 = Mono.defer(() -> {
            ZK.sleep("testMonoNested.m1.defer", Math.random());
            return Mono.just(data1);
        });

        m1 = m1.map(p1 -> {
            ZK.sleep("testMonoNested.m1.doOnSubscribe", 2);
            p1 = "m1.map." + p1 + "; thread: " + Thread.currentThread().getName() + ";";
            ;
            System.out.println("[^_^:20240321-2251-005.01] m1.map: " + p1);

            Mono<String> m2 = Mono.just(p1);
            m2 = m2.map(p2 -> {
                ZK.sleep("testMonoNested.m2.doOnSubscribe", 1 + Math.random());
                p2 = "m2.map." + p2 + Thread.currentThread().getName() + ";";
                System.out.println("[^_^:20240321-2251-005.02] m2.map: " + p2);
                return p2;
            });

//            m2.block();
//            return p1;

            StringBuffer sb = new StringBuffer();
            m2.subscribe(p -> {
                ZK.sleep("testDisposable.m1.subscribe", 1 + Math.random());
                sb.append(p).append(Thread.currentThread().getName() + ";");
                System.out.println("[^_^:20240321-2251-005.03] m2.subscribe.consumer: " + p);
            });
            return sb.toString();
        });

        Disposable disposable = m1.subscribe(consumer -> {
            ZK.sleep("testDisposable.m1.subscribe", Math.random());
            System.out.println("[^_^:20240321-2251-005.03] m1.subscribe.consumer: " + consumer
                    + Thread.currentThread().getName() + ";");
        });

        System.out.println("[^_^:20240321-2251-005.03] disposable.isDisposed(): " + disposable.isDisposed());
        disposable.dispose();

//        System.out.println("[^_^:20240321-2251-002.05] m.block(): " + m.block());
//        m.delaySubscription(Duration.ofSeconds(3));
        System.out.println("[^_^:20240321-2251-005] ================================");

    }

    @Test
    public void testManyMono() {
        System.out.println("[^_^:20240321-2251-006] --------------------------------");
        String data = "testManyMono.data.";
        int count = 6;
        int index = 0;
        List<Mono<String>> ms = new ArrayList<>();

        for (; index < count; ++index) {
            ms.add(Mono.just(data + index));
        }

        Consumer<String> consumer = p -> {
            ZK.sleep("testManyMono.m.subscribe", Math.random());
            System.out.println("[^_^:20240321-2251-006.02] subscribe p: " + p);
        };

        ms.forEach(item -> {
            item.subscribe(consumer);
        });

        System.out.println("[^_^:20240321-2251-006] ================================");

    }

    @Test
    public void testMonoError() {
        System.out.println("[^_^:20240321-2251-007] --------------------------------");

        String data = "testMonoError.data 1 ";
        Mono<String> m = null;
        m = Mono.empty();
        StringBuffer resSb = new StringBuffer();
        String expectedStr = "doOnError.";

        m = Mono.just(data);
        try {
            m = m.map(p -> {
                ZK.sleep("testMonoError.m.map", Math.random());
                System.out.println("[^_^:20240321-2251-007.01] m.map p: " + p);
                throw ZKExceptionsUtils.unchecked(new Exception("m.map.exception"));
            });
        }
        catch(Exception e) {
            resSb.append("catch.");
            System.out.println("[^_^:20240321-2251-007.02] testMonoError.catch: " + e.getMessage());
            e.printStackTrace();
        }

        m = m.doOnError(onError -> {
            resSb.append("doOnError.");
            ZK.sleep("testMonoError.m.subscribe", Math.random());
            System.out.println("[^_^:20240321-2251-007.03] m.doOnError onError: " + onError.getMessage());
        });

        m.subscribe(consumer -> {
            resSb.append("subscribe.");
            ZK.sleep("testMonoError.m.subscribe", Math.random());
            resSb.append("subscribe->");
            System.out.println("[^_^:20240321-2251-007.04] m.subscribe consumer: " + consumer);
        });

        System.out.println("[^_^:20240321-2251-007.05] resSb: " + resSb.toString());
        TestCase.assertEquals(expectedStr, resSb.toString());
        System.out.println("[^_^:20240321-2251-007] ================================");
    }

    public Mono<List<Mono<String>>> getMono(int count) {
        return Mono.fromCallable(() -> {
            ZK.sleep("getMono.for: ", Math.random());
            System.out.println(
                    "[^_^:20240321-2251-008.01] time: " + ZKDateUtils.formatDate(ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            List<Mono<String>> ms = new ArrayList<>();
            Mono<String> m = null;
            for (int i = 0; i < count; ++i) {
                int index = i;
                m = Mono.fromSupplier(() -> {
                    System.out.println("[^_^:20240321-2251-008.02] time: "
                            + ZKDateUtils.formatDate(ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
                    ZK.sleep("getMono.for: ", Math.random());
                    return "getMono.m" + index;
                });
                ms.add(m);
            }
            ZK.sleep("testMonoFromCallable.m.fromCallable", Math.random());
            System.out.println("[^_^:20240321-2251-008.01] getMono.create.count: " + count);
            System.out.println(
                    "[^_^:20240321-2251-008.03] time: " + ZKDateUtils.formatDate(ZKDateUtils.DF_yyyy_MM_dd_HH_mm_ss));
            return ms;
        });
    }

}
