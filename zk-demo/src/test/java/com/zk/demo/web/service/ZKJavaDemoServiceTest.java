package com.zk.demo.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.Test;

import com.zk.core.utils.ZKJsonUtils;
import com.zk.demo.web.entity.ZKJavaDemoEntity;
import com.zk.demo.web.helper.ZKDemoTestHelper;

import junit.framework.TestCase;

/**
 * Copyright (c) 2017-2022 Vinson.
 * address:
 * All rights reserved
 * This software is the confidential and proprietary information of Vinson.
 * ("Confidential Information"). You shall not disclose such Confidential Information
 * and shall use it only in accordance with the terms of the license agreement you
 * entered into with Vinson;
 *
 * @Description: demo service 测试
 * @ClassName ZKJavaDemoServiceTest
 * @Package com.zk.demo.service
 * @PROJECT zk-parent
 * @Author bs
 * @DATE 2022-09-05 09:28:03
 **/
public class ZKJavaDemoServiceTest {

    @Test
    public void testDml(){
        List<ZKJavaDemoEntity> dels = new ArrayList<ZKJavaDemoEntity>();
        ZKJavaDemoService s = ZKDemoTestHelper.getCtx().getBean(ZKJavaDemoService.class);
        try{
            ZKJavaDemoEntity e = new ZKJavaDemoEntity();
            int result = 0;
            result = s.save(e);
            TestCase.assertEquals(1, result);
            dels.add(e);
            System.out.println("[^_^: 20220905-1058-001] ======== " + e.getPkId());
        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }finally {
            if(s != null){
                dels.forEach(item->{
                    s.diskDel(item);
                });
            }
        }
    }

    @Test
    public void testRowLock(){
        List<ZKJavaDemoEntity> dels = new ArrayList<ZKJavaDemoEntity>();
        ZKJavaDemoService s = ZKDemoTestHelper.getCtx().getBean(ZKJavaDemoService.class);
        try{
            int result = 0;
//            String id1, id2, id3;
            ZKJavaDemoEntity e = new ZKJavaDemoEntity();
            ZKJavaDemoEntity e1 = new ZKJavaDemoEntity();
            ZKJavaDemoEntity e2 = new ZKJavaDemoEntity();
            ExecutorService threadPool = Executors.newWorkStealingPool(3);

            e.setThreadStr("e-begin");
            e.setValueInt(1);
            result = s.updateRowLock(e, 0);
            TestCase.assertEquals(1, result);
            dels.add(e);

            e1.setPkId(e.getPkId());
            e1.setValueInt(1);
            e1.setThreadStr("e1");
            e2.setPkId(e.getPkId());
            e2.setValueInt(2);
            e2.setThreadStr("e2");

            /*** 行锁，操作同一行 *****/
            System.out.println("[^_^: 20220905-1521-001]=== 行锁，操作同一行 ================================================================");
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        System.out.println("[^_^: 20220905-1151-002-0] ============================ 线程启动： " + e1.getThreadStr() );
                        s.updateRowLock(e1, 1500);
                        System.out.println("[^_^: 20220905-1151-002] ------------------------------- " + ZKJsonUtils.toJsonStr(e1));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });

            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("[^_^: 20220905-1151-003-0] ============================ 线程启动： " + e2.getThreadStr());
                        s.updateRowLock(e2, 0);
                        System.out.println("[^_^: 20220905-1151-003] ------------------------------- " + ZKJsonUtils.toJsonStr(e2));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            Thread.sleep(500);
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);

            e = s.get(e.getPkId());
            System.out.println("[^_^: 20220905-1151-004] ============================ " + ZKJsonUtils.toJsonStr(e));
            TestCase.assertEquals(e2.getValueInt(), e.getValueInt());

            /*** 行锁，操作不同行 *****/
            System.out.println("[^_^: 20220905-1520-001] === 行锁，操作不同行 ================================================================ ");
            threadPool = Executors.newWorkStealingPool(3);
            ZKJavaDemoEntity r3, r4, e3 = new ZKJavaDemoEntity(), e4 = new ZKJavaDemoEntity();
            result = s.updateRowLock(e3, 0);
            TestCase.assertEquals(1, result);
            dels.add(e3);
            result = 0;
            result = s.updateRowLock(e4, 0);
            TestCase.assertEquals(1, result);
            dels.add(e4);

            TestCase.assertTrue(e4.getValueDate().getTime() > e3.getValueDate().getTime());

            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try{
                        System.out.println("[^_^: 20220905-1510-001-0] ============================ 线程启动： " + e3.getThreadStr() );
                        s.updateRowLock(e3, 1500);
                        System.out.println("[^_^: 20220905-1510-001] ------------------------------- " + ZKJsonUtils.toJsonStr(e3));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            threadPool.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        System.out.println("[^_^: 20220905-1510-002-0] ============================ 线程启动： " + e4.getThreadStr());
                        s.updateRowLock(e4, 0);
                        System.out.println("[^_^: 20220905-1510-002] ------------------------------- " + ZKJsonUtils.toJsonStr(e4));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            });
            Thread.sleep(500);
            threadPool.shutdown();
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
            r3 = s.get(e3.getPkId());
            r4 = s.get(e4.getPkId());
            TestCase.assertTrue(r3.getValueDate().getTime() > r4.getValueDate().getTime());

        }catch (Exception e){
            e.printStackTrace();
            TestCase.assertTrue(false);
        }finally {
            if(s != null){
                dels.forEach(item->{
                    s.diskDel(item);
                });
            }
        }
    }

}
