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
* @Title: ZKDemoReactor.java 
* @author Vinson 
* @Package com.zk.demo.reactor 
* @Description: TODO(simple description this file what to do. ) 
* @date Apr 9, 2024 1:27:49 AM 
* @version V1.0 
*/
package com.zk.demo.reactor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.zk.core.commons.reactor.ZKReactorRelational;
import com.zk.core.commons.reactor.ZKResultCollect;
import com.zk.demo.ZKTestHelper.ZK;

import junit.framework.TestCase;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SignalType;
import reactor.util.context.Context;

/** 
* @ClassName: ZKDemoReactor 
* @Description: TODO(simple description this class what to do. ) 
* @author Vinson 
* @version 1.0 
*/
public class ZKDemoReactor {

    static final String printFromt = "[%s]:[%s].%s %s: %s";

    public ExecutorService threadPoolMono = Executors.newFixedThreadPool(10);
    public Mono<Integer> requestMono(String funName, int value) {
        ZK.p("[^_^:20240409-0134-000.threadPoolMono]", funName + ".threadPoolMono");
        return Mono.create(callback -> {
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ZK.p("[^_^:20240409-0134-000.threadPoolMono]", funName + ".threadPoolMono.run");
                    ZK.sleep("requestMono.run: ", 0.5 + Math.random());
                    callback.success(value);
                }
            };
            threadPoolMono.submit(r);
        });
    }

    public ExecutorService threadPoolFlux = Executors.newFixedThreadPool(10);
    public Flux<Integer> requestFlux(String funName, final int count) {
        ZK.p("[^_^:20240409-0134-000.threadPoolFlux]", funName + ".requestFlux");
        return Flux.create(emitter->{
            Runnable r = new Runnable() {
                @Override
                public void run() {
                    ZK.p("[^_^:20240409-0134-000.threadPoolFlux]", funName + ".requestFlux.run");
                    for (int i = 0; i < count; ++i) {
                        ZK.sleep("requestFlux.run.for: ", 0.5 + Math.random());
                        emitter.next(i);
                    }
                    // 得告诉 flux 已发布完数据
                    emitter.complete();
                }
            };
            threadPoolFlux.submit(r);
//            r.run();
//            Thread t = new Thread(r);
//            t.start();
//            t.join();
        });
    }

    public static void main(String[] args) {
        ZKDemoReactor dr = new ZKDemoReactor();
        dr.testFlux();
        dr.testMono();
    }

    @Test // 还没想好怎么写
    public void testMonoContext() {
        try {
            System.out.println("[^_^:20240409-0134-004.testMonoContext] --------------------------------");

            int value = 3;
            int expected = value * 10;
            int[] actual = new int[1];

            Mono<Integer> m = this.requestMono("testMonoContext", value);

            m = m.contextWrite(Context.of("key1", "value1"));
            m = m.map(mapper -> {
                Mono.deferContextual(contextualMonoFactory -> {
                    String s = contextualMonoFactory.get("key1");
                    return Mono.just(s);
                }).subscribe(consumer -> {
                    ZK.p("[^_^:20240409-0134-004.01]", "testMonoContext.Mono.deferContextual.subscribe: " + mapper);
                });
                ZK.p("[^_^:20240409-0134-004.03]", "testMonoContext.m.map: " + mapper);
                return mapper * 10;
            });

            m.subscribe(consumer -> {
                actual[0] = consumer;
                ZK.p("[^_^:20240409-0134-004.04]", "testMonoContext.m.subscribe: " + consumer);
            });

            System.out.println("[^_^:20240409-0134-004.testMonoContext] ================================");
            // 线程未返回，所以结果还不是期望值
            TestCase.assertNotSame(expected, actual[0]);
            // 等待线程池完成
            threadPoolMono.shutdown();
            threadPoolMono.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            TestCase.assertEquals(expected, actual[0]);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testMonoList() {
        try {
            System.out.println("[^_^:20240409-0134-003.testMonoList] --------------------------------");

            int count = 1, value = 3;
            String expected = count * value * 10 + "";
            String[] actual = new String[1];

            List<Mono<Integer>> drList = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                drList.add(this.requestMono("testMonoList", value));
            }

            // --- 111 ----------------------
            Mono<String> m = Mono.just(drList).flatMap(p -> {
                ZK.p("[^_^:20240409-0134-003.01]", "testMonoList.m.map: " + p.size());
                return Mono.create(callback -> {
                    ZKResultCollect<Integer> rc = new ZKResultCollect<Integer>(p.size(), callback, res -> {
                        int r = 0;
                        for (int v : res) {
                            r += v;
                        }
                        return r + "";
                    });
                    p.forEach(action -> {
                        ZK.p("[^_^:20240409-0134-003.03]", "testMonoList.m.p.forEach: " + action.hashCode());

                        action = action.doFinally(onFinally -> {
                            ZK.p("[^_^:20240409-0134-003.05]", "testMonoList.action.doFinally: " + onFinally);
                            if (SignalType.ON_COMPLETE == onFinally) {
                                rc.doSubscribe();
                            }
                        });

                        action = action.map(mapper -> {
                            return mapper * 10;
                        });

                        ZKReactorRelational<Integer> rr = rc.createRelational();
                        action = action.doOnSubscribe(subscription -> {
                            rr.setSubscription(subscription);
                        });
                        Disposable d = action.subscribe(consumer -> {
                            ZK.p("[^_^:20240409-0134-003.04]", "testMonoList.action.subscribe: " + consumer);
                            rc.accept(consumer);
                        });
                        rr.setDisposable(d);
                    });
                    rc.doSubscribe();
                });
            });
            m.subscribe(p -> {
                actual[0] = p;
                ZK.p("[^_^:20240409-0134-003.05]", "testFlux.m.subscribe: " + actual[0]);
            });

            // --- 222 达不到预期 ----------------------
//            for (Mono<Integer> action : drList) {
//                ZK.p("[^_^:20240409-0134-003.02]", "testMonoList.m.p.forEach: " + action.hashCode());
//                action.map(mapper -> {
//                    ZK.p("[^_^:20240409-0134-003.03]", "testMonoList.action.map " + mapper);
//                    return mapper * 10;
//                }).subscribe(consumer -> {
//                    ZK.p("[^_^:20240409-0134-003.04]", "testMonoList.action.subscribe " + consumer);
//                    actual[0] += consumer;
//                });
//            }

            // --- 333 达不到预期 ----------------------
//            Mono<Integer> m = Mono.just(drList).map(p -> {
//                ZK.p("[^_^:20240409-0134-003.01]", "testMonoList.m.map: " + p.size());
//                int res[] = new int[1];
//                p.forEach(action -> {
//                    ZK.p("[^_^:20240409-0134-003.02]", "testMonoList.m.p.forEach: " + action.hashCode());
//                    action = action.map(mapper -> {
//                        return mapper * 10;
//                    });
//                    action.subscribe(consumer -> {
//                        ZK.p("[^_^:20240409-0134-003.04]", "testMonoList.action.subscribe: " + consumer);
//                        res[0] += consumer;
//                    });
//                });
//                return res[0];
//            });
//            m.subscribe(p -> {
//                actual[0] = p;
//                ZK.p("[^_^:20240409-0134-003.05]", "testFlux.m.subscribe: " + p);
//            });
//            Integer block = m.block();
//            ZK.p("[^_^:20240409-0134-003.03]", "testMono.m.block: " + block);
//            TestCase.assertEquals(expected, block.intValue());

            System.out.println("[^_^:20240409-0134-003.testMonoList] ================================");
            // 线程未返回，所以结果还不是期望值
//            TestCase.assertNotSame(expected, actual[0]);
            // 等待线程池完成
            threadPoolMono.shutdown();
            threadPoolMono.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            TestCase.assertEquals(expected, actual[0]);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testFlux() {
        System.out.println("[^_^:20240409-0134-002.testFlux] --------------------------------");
        try {
            int count = 3, childCount = 3;
            int expected = 0;
            for (int i = 0; i < childCount; ++i) {
                expected += i * 10;
            }
            expected = expected * count;
            int[] actual = new int[1];
            
            List<Flux<Integer>> drList = new ArrayList<>();
            for (int i = 0; i < count; ++i) {
                drList.add(this.requestFlux("testFlux", childCount));
            }

//            List<ZKDemoReactor> drList = new ArrayList<>();
//            for (int i = 0; i < count; ++i) {
//                drList.add(new ZKDemoReactor());
//            }
            
            Mono<List<Integer>> m = Mono.just(drList).flatMap(p -> {
                ZK.p("[^_^:20240409-0134-002.01]", "testFlux.m.map: " + p.size());

                return Mono.create(callback -> {
                    ZKResultCollect<Integer> rc = new ZKResultCollect<Integer>(p.size(), callback);
                    p.forEach(action -> {
                        ZK.p("[^_^:20240409-0134-002.01]", "testFlux.m.p.forEach: " + action.hashCode());

                        action = action.map(mapper -> {
                            ZK.p("[^_^:20240409-0134-002.03]", "testFlux.action.map: " + mapper);
                            return mapper * 10;
                        });
                        // --- 222 方式一 -------------------------------------
                        action = action.doFinally(onFinally -> {
                            ZK.p("[^_^:20240409-0134-002.04]", "testFlux.action.doFinally: " + onFinally);
                            if (SignalType.ON_COMPLETE == onFinally) {
                                rc.doSubscribe();
                            }
                        });

                        ZKReactorRelational<Integer> rr = rc.createRelational();
                        Disposable d = action.subscribe(consumer -> {
                            ZK.p("[^_^:20240409-0134-002.05]", "testFlux.action.mono.subscribe: " + consumer);
                            rc.accept(consumer);
                        });
                        rr.setDisposable(d);

//                        // --- 222 方式二 -------------------------------------
//                        Mono<List<Integer>> cm = action.collectList();
//                        cm = cm.doFinally(onFinally -> {
//                            ZK.p("[^_^:20240409-0134-002.04]", "testFlux.action.mono.doFinally: " + onFinally);
//                            if (SignalType.ON_COMPLETE == onFinally) {
//                                rc.doSubscribe(callback);
//                            }
//                        });
//                        Disposable d = cm.subscribe(consumer -> {
//                            ZK.p("[^_^:20240409-0134-002.05]", "testFlux.action.mono.subscribe: " + consumer.size());
//                            rc.accept(consumer);
//                        });
//                        rc.add(d);
                    });
                    rc.doSubscribe();
                });
            });

            m.subscribe(p -> {
                p.forEach(v -> {
                    actual[0] += v;
                });
                ZK.p("[^_^:20240409-0134-002.06]", "testFlux.m.subscribe: " + actual[0]);
            });
            System.out.println("[^_^:20240409-0134-002.testFlux] ================================");

            TestCase.assertNotSame(expected, actual[0]);
            // 等待线程池完成
            threadPoolFlux.shutdown();
            threadPoolFlux.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            TestCase.assertEquals(expected, actual[0]);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

    @Test
    public void testMono() {
        try {
            System.out.println("[^_^:20240409-0134-001.testMono] --------------------------------");

            int value = 3;
            int expected = value * 10;
            int[] actual = new int[1];

            Mono<Integer> m = this.requestMono("testMono", value);

            m = m.map(mapper -> {
                ZK.p("[^_^:20240409-0134-001.01]", "testMono.m.map: " + mapper);
                return mapper * 10;
            });

            m.subscribe(consumer -> {
                actual[0] = consumer;
                ZK.p("[^_^:20240409-0134-001.02]", "testMono.m.subscribe: " + consumer);
            });

//            Integer block = m.block();
//            ZK.p("[^_^:20240409-0134-001.03]", "testMono.m.block: " + block);
//            TestCase.assertEquals(expected, block.intValue());

            System.out.println("[^_^:20240409-0134-001.testMono] ================================");
            // 线程未返回，所以结果还不是期望值
            TestCase.assertNotSame(expected, actual[0]);
            // 等待线程池完成
            threadPoolMono.shutdown();
            threadPoolMono.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            TestCase.assertEquals(expected, actual[0]);
        }
        catch(Exception e) {
            e.printStackTrace();
            TestCase.assertTrue(false);
        }
    }

}
